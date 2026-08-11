import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

root = Path(
    r"C:\Users\aruna\.cursor\projects\c-Users-aruna-OneDrive-Documents-NetBeansProjects-Thalam\agent-transcripts\b21eae3a-b89e-4508-bd58-66bf6cd6e06d"
)

# Search main + subagents for anything that could enlarge Reports.fxml between lines conceptually
# Focus: Write to Reports.fxml, Shell copy, StrReplace large new_string with reports-filter-bar

hits = []
for p in sorted(root.rglob("*.jsonl")):
    with open(p, "r", encoding="utf-8", errors="replace") as f:
        for i, line in enumerate(f, 1):
            if "Reports.fxml" not in line and "reports-filter-bar" not in line:
                continue
            try:
                obj = json.loads(line)
            except Exception:
                continue
            content = obj.get("message", {}).get("content", [])
            if not isinstance(content, list):
                continue
            for block in content:
                if not isinstance(block, dict) or block.get("type") != "tool_use":
                    continue
                name = block.get("name")
                inp = block.get("input") or {}
                path = str(inp.get("path", ""))
                cmd = str(inp.get("command", ""))
                if name == "Write" and "Reports.fxml" in path.replace("\\", "/"):
                    c = inp.get("contents", "")
                    hits.append((p.name, i, "Write", len(c), c[:60].replace("\n", "\\n")))
                elif name == "Shell" and ("Reports.fxml" in cmd or "Reports" in cmd and "copy" in cmd.lower()):
                    hits.append((p.name, i, "Shell", len(cmd), cmd[:120].replace("\n", "\\n")))
                elif name == "StrReplace" and "Reports.fxml" in path.replace("\\", "/"):
                    ns = inp.get("new_string", "")
                    if "reports-filter-bar" in ns or "projectProgressHost" in ns or len(ns) > 2000:
                        hits.append(
                            (
                                p.name,
                                i,
                                "StrReplace-new",
                                len(ns),
                                ns[:60].replace("\n", "\\n"),
                            )
                        )
                    os_ = inp.get("old_string", "")
                    if "reports-filter-bar" in os_ or "projectProgressHost" in os_ or len(os_) > 2000:
                        hits.append(
                            (
                                p.name,
                                i,
                                "StrReplace-old",
                                len(os_),
                                os_[:60].replace("\n", "\\n"),
                            )
                        )

print("HITS", len(hits))
for h in hits:
    print(h)

# Extract the StrReplace at 1407 old_string fully — that's the pre-image of a large chunk
transcript = root / "b21eae3a-b89e-4508-bd58-66bf6cd6e06d.jsonl"
with open(transcript, "r", encoding="utf-8", errors="replace") as f:
    for i, line in enumerate(f, 1):
        if i != 1407:
            continue
        obj = json.loads(line)
        for block in obj["message"]["content"]:
            if (
                isinstance(block, dict)
                and block.get("name") == "StrReplace"
                and "Reports.fxml" in str(block.get("input", {}).get("path", ""))
            ):
                old = block["input"]["old_string"]
                new = block["input"]["new_string"]
                print("\n=== 1407 old len", len(old))
                print(old[:500])
                print("...")
                print(old[-300:])
                print("\n=== 1407 new len", len(new))
                print(new[:500])
                print("has projectCombo", "projectCombo" in old, "in new", "projectCombo" in new)
                print("has sidebar", "sidebar" in old.lower(), "nav-item" in old)
