import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

early_path = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01.jsonl"
)
main_path = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\b21eae3a-b89e-4508-bd58-66bf6cd6e06d\b21eae3a-b89e-4508-bd58-66bf6cd6e06d.jsonl"
)
out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Reports\Reports.fxml")


def extract_ops(path, fname):
    ops = []
    with open(path, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if fname not in line:
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
                if fname not in p:
                    continue
                if name == "Write" and "contents" in inp:
                    ops.append((i, bi, "Write", inp))
                elif name == "StrReplace" and "old_string" in inp:
                    ops.append((i, bi, "StrReplace", inp))
    return ops


# 1) Seed from early Write 173
early_ops = extract_ops(early_path, "Reports.fxml")
content = None
for o in early_ops:
    if o[2] == "Write" and o[0] == 173:
        content = o[3]["contents"]
print("seed len", len(content))

# apply early StrReplaces after Write 173
for o in early_ops:
    if o[0] <= 173 or o[2] != "StrReplace":
        continue
    old, new = o[3]["old_string"], o[3]["new_string"]
    if old in content:
        content = content.replace(old, new, 1)
        print(f"early apply StrReplace line={o[0]}")
    else:
        print(f"early FAIL StrReplace line={o[0]}")

# 2) Apply main transcript StrReplaces that touch Reports.fxml, SKIP Writes
# Also include subagent StrReplaces that succeed
sub_ops = []
subs = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts"
)
# Order: early shell patches from 07df8a0b subagents / b21 subagents by line is messy;
# Prefer main transcript chronological StrReplaces only first.
main_ops = extract_ops(main_path, "Reports.fxml")
for o in main_ops:
    if o[2] != "StrReplace":
        print(f"skip {o[2]} line={o[0]}")
        continue
    # Skip early module-list-ish replaces that don't belong to shell reports
    old, new = o[3]["old_string"], o[3]["new_string"]
    if old not in content:
        print(f"FAIL line={o[0]} bi={o[1]} old_len={len(old)} preview={old[:80]!r}")
        continue
    content = content.replace(old, new, 1)
    print(f"APPLY line={o[0]} bi={o[1]}")

# Try remaining subagent StrReplaces (may update nav icons etc.)
for p in sorted(subs.rglob("*.jsonl")):
    if p.name in {early_path.name, main_path.name}:
        continue
    ops = extract_ops(p, "Reports.fxml")
    for o in ops:
        if o[2] != "StrReplace":
            continue
        old, new = o[3]["old_string"], o[3]["new_string"]
        if old in content:
            content = content.replace(old, new, 1)
            print(f"SUB APPLY {p.name} line={o[0]}")
        else:
            # quiet fail
            pass

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
    "Employees",
    "nav-item-current",
]
print("\nFINAL len", len(content))
for id_ in ids:
    print(("OK" if id_ in content else "MISSING"), id_)

out.parent.mkdir(parents=True, exist_ok=True)
out.write_text(content, encoding="utf-8", newline="\n")
print("WROTE", out, out.stat().st_size)
