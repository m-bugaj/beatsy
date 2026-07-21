---
name: commit-push
description: Use when the user asks to generate/suggest a commit message, propose commit names, "jak nazwać commit" / "podaj nazwę commita" / "commit message", or wants to split pending changes into scoped commits and push them. Interactive area-by-area approve -> commit -> push flow using the user's fixed message format.
version: 2.1.0
---

# commit-push

Group pending changes into logical areas, propose 3 commit messages per area, get one approved per area, then commit per area and push the chosen branch.

## Token discipline (important)
Multi-turn interactive flow -> anything pulled into context early is re-billed every turn. Stay lean:
- NEVER run a full `git diff`. Get the shape from `git status --porcelain` + `git diff --stat` (+ the `--staged` variants). That is usually enough to define areas and name commits.
- If a file's purpose is unclear from path + stat, read only that file: `git diff --unified=0 -- <file>`. Skip contents of large/generated/binary files; name them from path + stat.
- Batch the branch choice and area picks into as few AskUserQuestion calls as possible (up to 4 questions each).

## Flow
1. Overview via `git status --porcelain` + `git diff --stat` (+ staged). Group changed files into **areas** (one logical change each: feature/logic vs config vs deps vs docs). List each area's files/classes. Read a compact per-file diff only where naming needs it.
2. Ask up front which branch to use: stay on current, or create a new one. Present each area's files + exactly 3 proposals; get one approved per area. Batch into as few questions as possible. Do not commit yet.
3. When every area has an approved message: stage only that area's files (`git add <files>`) and `git commit -m "<message>"` -- one commit per area, each ending with the `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>` trailer. Then `git push` the chosen branch.

## Message format (fixed user preference)
English; exactly 3 per area; capital first letter; single line ~<=70 chars; imperative mood ("Add"/"Fix"/"Refactor"); describe the logic/app change for that area, not file mechanics; no `feat:`/`docs:` prefixes; each area's messages cover only that area.

## Safety
Never `--no-verify`, `--amend`, or force-push unless asked. If a hook fails, stop and report. If only suggestions are wanted (no commit/push), stop after step 2.
