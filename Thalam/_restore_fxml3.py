import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

transcript = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\b21eae3a-b89e-4508-bd58-66bf6cd6e06d\b21eae3a-b89e-4508-bd58-66bf6cd6e06d.jsonl"
)

# Scan lines 890-1660 for anything mentioning Reports.fxml / key ids
keys = [
    "Reports.fxml",
    "reports-filter-bar",
    "projectProgressHost",
    "budgetSpendProgress",
    "materialsChartHost",
    "profitLossChartHost",
]

with open(transcript, "r", encoding="utf-8", errors="replace") as f:
    for i, line in enumerate(f, 1):
        if i < 890 or i > 1670:
            continue
        if not any(k in line for k in keys):
            continue
        try:
            obj = json.loads(line)
        except Exception:
            print(f"LINE {i}: non-json len={len(line)}")
            continue
        role = obj.get("role")
        content = obj.get("message", {}).get("content", [])
        if isinstance(content, str):
            print(f"LINE {i} role={role} text-content len={len(content)} hasPPH={'projectProgressHost' in content}")
            continue
        if not isinstance(content, list):
            print(f"LINE {i} role={role} content_type={type(content)}")
            continue
        for bi, block in enumerate(content):
            if isinstance(block, dict):
                t = block.get("type")
                name = block.get("name")
                if t == "tool_use":
                    inp = block.get("input") or {}
                    path = inp.get("path", "")
                    if "Reports" in str(path) or "Reports" in json.dumps(inp)[:200]:
                        extra = ""
                        if "contents" in inp:
                            extra = f" contents_len={len(inp['contents'])}"
                        if "old_string" in inp:
                            extra += f" old={len(inp['old_string'])} new={len(inp['new_string'])}"
                        print(f"LINE {i} tool_use {name} path={path}{extra}")
                elif t == "tool_result":
                    text = block.get("content")
                    if isinstance(text, list):
                        text = json.dumps(text)
                    text = text or ""
                    if "Reports.fxml" in text or "projectProgressHost" in text or "reports-filter" in text:
                        print(f"LINE {i} tool_result len={len(text)} preview={text[:120].replace(chr(10),' ')}")
                elif t == "text":
                    text = block.get("text") or ""
                    if any(k in text for k in keys):
                        print(f"LINE {i} text len={len(text)} snippet={text[:100].replace(chr(10),' ')}")
            else:
                print(f"LINE {i} block type {type(block)}")

print("\n=== Search all transcripts for largest tool_result / Write containing projectProgressHost ===")
root = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts"
)
candidates = []
for p in root.rglob("*.jsonl"):
    with open(p, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if "projectProgressHost" not in line and "budgetSpendProgress" not in line:
                continue
            # size of match context
            if "<?xml" in line and "Reports.ReportsController" in line:
                candidates.append((len(line), str(p), i, "xml+controller"))
            elif "projectProgressHost" in line:
                candidates.append((len(line), str(p), i, "pph"))

candidates.sort(reverse=True)
print("top candidates:")
for c in candidates[:20]:
    print(c)
