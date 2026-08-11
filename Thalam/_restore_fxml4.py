import json
import sys
import re
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

transcript = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\b21eae3a-b89e-4508-bd58-66bf6cd6e06d\b21eae3a-b89e-4508-bd58-66bf6cd6e06d.jsonl"
)

# Collect Read tool_use call IDs for Reports.fxml, then find matching tool_results
reads = []  # (line, tool_call_id)
with open(transcript, "r", encoding="utf-8", errors="replace") as f:
    for i, line in enumerate(f, 1):
        if "Reports.fxml" not in line:
            continue
        try:
            obj = json.loads(line)
        except Exception:
            continue
        content = obj.get("message", {}).get("content", [])
        if not isinstance(content, list):
            continue
        for block in content:
            if not isinstance(block, dict):
                continue
            if block.get("type") == "tool_use" and block.get("name") == "Read":
                path = (block.get("input") or {}).get("path", "")
                if path.replace("\\", "/").endswith("Reports/Reports.fxml"):
                    reads.append((i, block.get("id"), block.get("input")))

print(f"Found {len(reads)} Read tool_uses for Reports.fxml")
for r in reads:
    print(r[0], r[1], r[2])

# Now find tool results. In some formats results are role=tool or user with tool_result
# Also search for lines that look like file read dumps with line numbers

results = []
with open(transcript, "r", encoding="utf-8", errors="replace") as f:
    for i, line in enumerate(f, 1):
        if "projectProgressHost" not in line and "Reports.ReportsController" not in line:
            continue
        try:
            obj = json.loads(line)
        except Exception:
            continue
        role = obj.get("role")
        content = obj.get("message", {}).get("content", [])
        # Alternative shapes
        if content is None and "content" in obj:
            content = obj["content"]

        texts = []
        if isinstance(content, str):
            texts.append(content)
        elif isinstance(content, list):
            for block in content:
                if isinstance(block, dict):
                    if block.get("type") == "tool_result":
                        c = block.get("content")
                        if isinstance(c, str):
                            texts.append(c)
                        elif isinstance(c, list):
                            for part in c:
                                if isinstance(part, dict) and part.get("type") == "text":
                                    texts.append(part.get("text", ""))
                                elif isinstance(part, str):
                                    texts.append(part)
                    elif block.get("type") == "text":
                        texts.append(block.get("text", ""))
                elif isinstance(block, str):
                    texts.append(block)

        for t in texts:
            if "Reports.ReportsController" in t or "projectProgressHost" in t:
                # Heuristic: Read output usually has "     1|<?xml" style
                if "<?xml" in t or "projectProgressHost" in t:
                    results.append((i, role, len(t), t[:80].replace("\n", " "), t))

print(f"\nFound {len(results)} candidate result/text blobs")
for r in results:
    print(f"line={r[0]} role={r[1]} len={r[2]} preview={r[3]}")

# Prefer latest/largest read dump that looks like FXML content
best = None
for r in results:
    t = r[4]
    # strip line-number prefixes if present
    if "<?xml" in t and ("fx:controller" in t or "projectProgressHost" in t):
        if best is None or r[2] > best[2]:
            best = r

if best:
    print(f"\nBEST line={best[0]} len={best[2]}")
    text = best[4]
    # Convert read format "   123|content" to raw
    if re.search(r"(?m)^\s*\d+\|", text):
        lines = []
        for ln in text.splitlines():
            m = re.match(r"\s*\d+\|(.*)$", ln)
            if m:
                lines.append(m.group(1))
            else:
                # sometimes header lines
                if ln.startswith("     1|") or "|" in ln[:8]:
                    continue
                # keep non-numbered only if already collecting? skip headers
                if "Reports.fxml" in ln or ln.startswith("...") or "limit" in ln.lower():
                    continue
        raw = "\n".join(lines)
        # Prefer extracting between first <?xml and end
        if "<?xml" in text:
            # Better: only numbered lines
            numbered = []
            for ln in text.splitlines():
                m = re.match(r"\s*\d+\|(.*)$", ln)
                if m:
                    numbered.append(m.group(1))
            raw = "\n".join(numbered)
            if not raw.strip().startswith("<?xml"):
                # maybe first lines missing
                pass
        print("Extracted numbered lines len", len(raw))
        print("starts", repr(raw[:100]))
        print("ends", repr(raw[-100:]))
        ids = [
            "projectCombo",
            "dateRangeCombo",
            "projectProgressHost",
            "expensesChartHost",
            "invoicesChartHost",
            "profitLossChartHost",
            "purchasesChartHost",
            "budgetSpendEmptyLabel",
            "budgetSpendContent",
            "budgetValueLabel",
            "spendValueLabel",
            "remainingValueLabel",
            "budgetSpendProgress",
            "budgetSpendPercentLabel",
        ]
        for id_ in ids:
            print(("OK" if id_ in raw else "MISSING"), id_)
        out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Reports\Reports.fxml")
        out.write_text(raw + ("\n" if not raw.endswith("\n") else ""), encoding="utf-8", newline="\n")
        print("WROTE", out, out.stat().st_size)
    else:
        print("No line numbers; dumping markers")
        print("has xml", "<?xml" in text)
        print(text[:500])
