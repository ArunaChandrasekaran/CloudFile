import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

root = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts"
)

candidates = []
for p in root.rglob("*.jsonl"):
    with open(p, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if "Employees.EmployeesController" not in line and 'fx:controller="Employees.EmployeesController"' not in line:
                continue
            if "<?xml" not in line:
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
                if name == "Write" and "contents" in inp:
                    c = inp["contents"]
                    if "Employees.EmployeesController" in c or 'fx:controller="Employees.EmployeesController"' in c:
                        candidates.append((len(c), p.name, i, "sidebar" in c, c[:80].replace("\n", "\\n"), inp.get("path")))
                if name == "StrReplace" and "Employees.fxml" in str(inp.get("path", "")):
                    old = inp.get("old_string", "")
                    if "sidebar" in old or "serialColumn" in old:
                        candidates.append((len(old), p.name, i, "OLD sidebar" in old or "sidebar" in old, old[:80].replace("\n", "\\n"), "StrReplace-old"))

candidates.sort(reverse=True)
print("candidates", len(candidates))
for c in candidates[:30]:
    print(c[0], c[1], "line", c[2], c[3], c[5], c[4])

# Also search early transcript for Write path Employees
early = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01\07df8a0b-581a-4cb1-92e0-f7c4d6ecbf01.jsonl"
)
with open(early, "r", encoding="utf-8", errors="replace") as f:
    for i, line in enumerate(f, 1):
        if "Employees" not in line or "Write" not in line:
            continue
        try:
            obj = json.loads(line)
        except Exception:
            continue
        for block in obj.get("message", {}).get("content", []) or []:
            if isinstance(block, dict) and block.get("name") == "Write":
                path = str((block.get("input") or {}).get("path", ""))
                if "Employees" in path:
                    c = block["input"].get("contents", "")
                    print("EARLY WRITE", i, path, len(c), "sidebar", "sidebar" in c)
