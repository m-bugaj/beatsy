---
name: commit-push
description: Use when the user asks to generate/suggest a commit message, propose commit names, "jak nazwać commit" / "podaj nazwę commita" / "commit message", or wants to split pending changes into scoped commits and push them. Defines the user's fixed message format AND an interactive, area-by-area approve → commit → push flow.
version: 2.0.0
---

# Commit message generator & area-by-area committer

Turns the user's pending changes into one or more scoped commits. It (1) groups uncommitted changes into logical areas, (2) proposes 3 commit messages per area, (3) lets the user approve one message per area interactively (plan-mode style), and (4) after every area is approved, makes one commit per area and pushes to the current branch.

The message format below is a fixed personal preference — do not guess a different one.

## Step 1 — Inspect and group the changes

1. Read the full set of pending changes: `git status --porcelain`, then `git diff` and `git diff --staged` (use `--stat` first if the diff is large).
2. Group the changes into **areas** — clusters that belong to one logical change, not just "one file". Typical split: a feature/logic implementation is one area; unrelated edits (app configuration, build/deps, docs, formatting) are separate areas. Files serving the same change go in the same area; a config tweak unrelated to the feature is its own area.
3. For each area, note the concrete **files or classes** it covers — the user wants to see, per area, which files/classes are grouped there.
4. If one file contains changes belonging to two different areas, say so explicitly. Default to putting it in its most relevant area; only reach for `git add -p` (hunk staging) if the user wants that file genuinely split across commits.

## Step 2 — Propose, one area at a time (plan-mode style)

Go area by area. For each area, present:
- the area's files/classes, and
- **exactly 3** commit-message proposals for that area (numbered).

Then collect the user's approval of exactly one message for that area before moving to the next. `AskUserQuestion` is a clean way to collect the pick (the 3 proposals as options; the built-in "Other" lets the user type a custom message) — a plain numbered reply is fine too. You may batch a few areas into one `AskUserQuestion` call to cut round-trips, but keep one pick per area and don't advance past an area until it has an approved message.

Do not commit anything during this step — only collect approvals.

## Format rules for each message (non-negotiable, the user's fixed preference)

- English only.
- Exactly 3 proposals per area.
- Each message starts with a **capital letter**.
- Short and concise — a single line, roughly under ~70 characters. No body/footer unless the user asks for one.
- Imperative mood ("Add", "Fix", "Refactor"), not past tense ("Added") or gerund ("Adding").
- Describe *what changed in the logic/project/app* for that specific area, not mechanical file operations ("Update files", "Edit CLAUDE.md").
- No conventional-commit prefixes (`feat:`, `docs:`…) unless the user explicitly asks for that convention.
- Each area's 3 proposals describe THAT area's change only — never blend two areas into one message.

## Step 3 — Commit each area, then push

Only after every area has an approved message:

1. Check `git status` so nothing unintended is already staged — this flow stages each area itself.
2. For each area, in turn: stage only that area's files (`git add <files>`), then commit with its approved message (`git commit -m "<message>"`). One commit per area. Append the required `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>` trailer to each commit per the harness rules.
3. After all commits are made, `git push` to the **current** branch — the branch the user is already on. Finishing with that push is this skill's whole purpose and is the user's standing, explicit request.
   - Safety: never `--no-verify`, never `--amend` or force-push unless the user asks. If a pre-commit/pre-push hook fails, stop and report it — don't bypass it.
   - If the current branch is the repo's default/main branch, flag that before pushing so the user can confirm or branch first — but the current branch is still the target they asked for.

## Scope note

The commit + push at the end is the point of this skill and is an explicit standing request — invoking this skill *is* the user asking for it. If the user says they only want message suggestions (no commit, no push), stop after Step 2 and just present the proposals.
