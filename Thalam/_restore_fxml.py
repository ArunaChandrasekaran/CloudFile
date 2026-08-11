import json
from pathlib import Path

transcripts_root = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts"
)
reports_out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Reports")
employees_out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Employees")
reports_out.mkdir(parents=True, exist_ok=True)
employees_out.mkdir(parents=True, exist_ok=True)


def norm_path(p: str) -> str:
    return (p or "").replace("\\", "/")


def collect_ops(filename_substr: str):
    ops = []
    for p in transcripts_root.rglob("*.jsonl"):
        mtime = p.stat().st_mtime
        with open(p, "r", encoding="utf-8", errors="replace") as f:
            for i, line in enumerate(f, 1):
                if filename_substr not in line:
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
                    path = norm_path(inp.get("path", ""))
                    if filename_substr not in path:
                        continue
                    if name == "Write" and "contents" in inp:
                        ops.append((mtime, str(p), i, bi, "Write", inp))
                    elif name == "StrReplace" and "old_string" in inp and "new_string" in inp:
                        ops.append((mtime, str(p), i, bi, "StrReplace", inp))
    ops.sort(key=lambda x: (x[0], x[1], x[2], x[3]))
    return ops


def replay(ops):
    content = None
    applied = 0
    failed = []
    for o in ops:
        kind, inp = o[4], o[5]
        if kind == "Write":
            content = inp["contents"]
            applied += 1
            print(f"APPLY Write line={o[2]} len={len(content)} file={Path(o[1]).name}")
        else:
            if content is None:
                failed.append((o[2], "no content yet"))
                continue
            old, new = inp["old_string"], inp["new_string"]
            if old not in content:
                failed.append((o[2], "old not found: " + old[:120].replace("\n", "\\n")))
                continue
            content = content.replace(old, new, 1)
            applied += 1
            print(f"APPLY StrReplace line={o[2]} file={Path(o[1]).name}")
    print(f"APPLIED={applied} FAILED={len(failed)}")
    for line_no, reason in failed:
        print(f"FAIL line={line_no} {reason}")
    return content


print("=== Reports.fxml ===")
reports_ops = collect_ops("Reports.fxml")
print("OPS", len(reports_ops))
for o in reports_ops:
    kind, inp = o[4], o[5]
    if kind == "Write":
        print(f"  Write line={o[2]} bytes={len(inp.get('contents', ''))} {Path(o[1]).name}")
    else:
        print(
            f"  StrReplace line={o[2]} old={len(inp['old_string'])} new={len(inp['new_string'])} {Path(o[1]).name}"
        )

reports_content = replay(reports_ops)
if reports_content:
    out = reports_out / "Reports.fxml"
    out.write_text(reports_content, encoding="utf-8", newline="\n")
    print("WROTE", out, "bytes", out.stat().st_size)
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
        "Reports.ReportsController",
    ]
    for id_ in ids:
        print(("OK" if id_ in reports_content else "MISSING"), id_)
    print("has sidebar Employees:", "Employees" in reports_content)
    print("has ReportsController package shell:", 'fx:controller="Reports.ReportsController"' in reports_content)

print("\n=== Employees.fxml ===")
employees_ops = collect_ops("Employees.fxml")
print("OPS", len(employees_ops))
for o in employees_ops:
    kind, inp = o[4], o[5]
    if kind == "Write":
        print(f"  Write line={o[2]} bytes={len(inp.get('contents', ''))} {Path(o[1]).name}")
    else:
        print(
            f"  StrReplace line={o[2]} old={len(inp['old_string'])} new={len(inp['new_string'])} {Path(o[1]).name}"
        )

employees_content = replay(employees_ops)
if employees_content:
    out = employees_out / "Employees.fxml"
    out.write_text(employees_content, encoding="utf-8", newline="\n")
    print("WROTE", out, "bytes", out.stat().st_size)
else:
    print("No Employees.fxml content found")
