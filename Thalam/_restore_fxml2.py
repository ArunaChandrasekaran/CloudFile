import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

transcript = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\b21eae3a-b89e-4508-bd58-66bf6cd6e06d\b21eae3a-b89e-4508-bd58-66bf6cd6e06d.jsonl"
)
early = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01.jsonl"
)
reports_out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Reports\Reports.fxml")
employees_out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Employees\Employees.fxml")
reports_out.parent.mkdir(parents=True, exist_ok=True)
employees_out.parent.mkdir(parents=True, exist_ok=True)


def extract_ops(path: Path, filename: str):
    ops = []
    with open(path, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if filename not in line:
                continue
            try:
                obj = json.loads(line)
            except Exception:
                continue
            content = obj.get("message", {}).get("content", [])
            if not isinstance(content, list):
                continue
            for bi, block in enumerate(content):
                if not isinstance(block, dict) or block.get("type") != "tool_use":
                    continue
                name = block.get("name")
                inp = block.get("input") or {}
                p = (inp.get("path") or "").replace("\\", "/")
                if filename not in p:
                    continue
                if name == "Write" and "contents" in inp:
                    ops.append((i, bi, "Write", inp))
                elif name == "StrReplace" and "old_string" in inp:
                    ops.append((i, bi, "StrReplace", inp))
                elif name == "Read" and "path" in inp:
                    # ignore
                    pass
    return ops


def replay_from_last_write(ops):
    # find last Write
    last_write_idx = None
    for idx, o in enumerate(ops):
        if o[2] == "Write":
            last_write_idx = idx
    if last_write_idx is None:
        return None, []
    content = ops[last_write_idx][3]["contents"]
    print(f"Start from Write line={ops[last_write_idx][0]} len={len(content)}")
    failed = []
    applied = 0
    for o in ops[last_write_idx + 1 :]:
        if o[2] != "StrReplace":
            continue
        old, new = o[3]["old_string"], o[3]["new_string"]
        if old not in content:
            failed.append((o[0], old[:140].replace("\n", "\\n")))
            continue
        content = content.replace(old, new, 1)
        applied += 1
        print(f"Applied StrReplace line={o[0]}")
    print(f"Applied={applied} Failed={len(failed)}")
    for ln, snip in failed:
        print(f"FAIL line={ln}: {snip}")
    return content, failed


print("=== Reports from b21eae3a (last Write + StrReplaces) ===")
ops = extract_ops(transcript, "Reports.fxml")
print("ops", len(ops), [(o[0], o[2], len(o[3].get("contents", o[3].get("old_string", "")))) for o in ops])
content, failed = replay_from_last_write(ops)

# Also try early transcript last write for comparison
print("\n=== Early full Write (07df8a0b) ===")
early_ops = extract_ops(early, "Reports.fxml")
print("early ops", [(o[0], o[2], len(o[3].get("contents", o[3].get("old_string", "")))) for o in early_ops])
early_content = None
for o in early_ops:
    if o[2] == "Write":
        early_content = o[3]["contents"]
if early_content:
    print("early write len", len(early_content))
    print("early has projectProgressHost", "projectProgressHost" in early_content)
    print("early has budgetSpendProgress", "budgetSpendProgress" in early_content)
    print("early has sidebar", "nav-item" in early_content or "lhsSidebar" in early_content)

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
    'fx:controller="Reports.ReportsController"',
]

if content:
    print("\nFinal reconstructed len", len(content))
    for id_ in ids:
        print(("OK" if id_ in content else "MISSING"), id_)
    print("has nav/sidebar markers:", "styleClass=\"nav-item" in content or "sidebar" in content.lower())
    reports_out.write_text(content, encoding="utf-8", newline="\n")
    print("WROTE", reports_out, reports_out.stat().st_size)

# Employees
print("\n=== Employees ===")
root = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts"
)
best = None
best_len = -1
best_src = None
for p in root.rglob("*.jsonl"):
    with open(p, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if "Employees.fxml" not in line:
                continue
            try:
                obj = json.loads(line)
            except Exception:
                continue
            content_blocks = obj.get("message", {}).get("content", [])
            if not isinstance(content_blocks, list):
                continue
            for block in content_blocks:
                if not isinstance(block, dict) or block.get("type") != "tool_use":
                    continue
                if block.get("name") != "Write":
                    continue
                inp = block.get("input") or {}
                path = (inp.get("path") or "").replace("\\", "/")
                if "Employees.fxml" not in path:
                    continue
                c = inp.get("contents") or ""
                if len(c) > best_len:
                    best = c
                    best_len = len(c)
                    best_src = f"{p.name}:{i}"

if best:
    employees_out.write_text(best, encoding="utf-8", newline="\n")
    print("WROTE Employees", best_src, "bytes", employees_out.stat().st_size)
    print(best[:400])
else:
    print("No Employees Write found")
