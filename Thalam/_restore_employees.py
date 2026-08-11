import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

root = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts"
)
out = Path(r"C:\Users\aruna\OneDrive\Documents\NetBeansProjects\Thalam\src\Employees\Employees.fxml")

writes = []
replaces = []
for p in root.rglob("*.jsonl"):
    with open(p, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if "Employees.fxml" not in line:
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
                path = (inp.get("path") or "").replace("\\", "/")
                if "Employees.fxml" not in path:
                    continue
                if name == "Write" and "contents" in inp:
                    writes.append((p.stat().st_mtime, str(p), i, bi, inp["contents"]))
                elif name == "StrReplace" and "old_string" in inp:
                    replaces.append((p.stat().st_mtime, str(p), i, bi, inp))

writes.sort()
replaces.sort()
print("WRITES", len(writes))
for w in writes:
    print(Path(w[1]).name, "line", w[2], "len", len(w[4]), "sidebar", "sidebar" in w[4], "EmployeesController", "EmployeesController" in w[4])
    print("  preview", w[4][:120].replace("\n", "\\n"))

print("REPLACES", len(replaces))
for r in replaces:
    print(Path(r[1]).name, "line", r[2], "old", len(r[4]["old_string"]), "new", len(r[4]["new_string"]))

# Prefer largest Write with sidebar, else largest overall; then apply succeeding StrReplaces in order
best = None
for w in writes:
    c = w[4]
    score = len(c) + (100000 if "sidebar" in c else 0) + (50000 if "Employees.EmployeesController" in c else 0)
    if best is None or score > best[0]:
        best = (score, w)

if best:
    content = best[1][4]
    print("SEED from", Path(best[1][1]).name, "line", best[1][2], "len", len(content))
    for r in replaces:
        old, new = r[4]["old_string"], r[4]["new_string"]
        if old in content:
            content = content.replace(old, new, 1)
            print("APPLY", Path(r[1]).name, r[2])
        else:
            print("FAIL", Path(r[1]).name, r[2], old[:60].replace("\n", "\\n"))
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(content, encoding="utf-8", newline="\n")
    print("WROTE", out, out.stat().st_size)
else:
    print("No Employees writes")
