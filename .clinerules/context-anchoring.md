## Problem

AI no persistent memory.
Context decay real: by message 30+, early decisions contradicted, naming inconsistent, "why" evaporate, forgotten
decision become potential contradiction, unresolved question become. silent assumption.

Context anchor solve:

- **Feature-bound** -- one file per feature (task), scoped decisions only
- **Decision-focused** -- capture what, why, what-else-considered for every choice
- **Session-spanning** -- file outlive conversation, carry context forward

Store the file in a `current_task.md`.

## Instructions

Read `current_task.md` from the project root before answering, generating code, or updating documentation for that task.

## Context file structure

Preserve the following structure and headings in context file, do not add new sections:

```markdown
## Goal
Describe in plain text the intended task outcome.
Optionally use mermaid diagrams.

## Decisions
List of decisions.

## Constraints
List of constraints

## Open questions
List of open questions

## Status
List of checkboxes, like the one on bottom.
Each checkbox correspons with subtask or subgoal.
When acomplished: `[x]`.
When outside current focus: `[ ] (next session)`

- [ ] subtask description
```

## Context File Lifecycle

Three behaviors govern context anchor file lifecycle.
Each triggered reactively (user ask) or proactively (AI suggest).
Both cases, AI **always confirm before acting** - propose, user dispose.

| Behavior   | Purpose                                  | Reactive Trigger           | Proactive Trigger                                     |
|------------|------------------------------------------|----------------------------|-------------------------------------------------------|
| **Create** | Start new context file                   | User ask create one        | AI detect feature work beginning without context file |
| **Load**   | Restore context from existing file       | User ask load/resume       | AI detect existing file context and suggest loading   |
| **Enrich** | Add new decision, constraint, resolution | User ask capture something | AI detect decision made in conversation               |

### Create Behavior

1. Clarify with user that current `current_task.md` is not from current task.
2. Fill `current_task.md` with user provided information using template from `Context file structure`.

### Load Behavior

Prior feature context exists in file - read it and use.

## Enrich Behavior

Always confirm before writing.
**What capture in Decisions Log**:

- **Decision (including rejected alternatives)** -- what decided, stated clearly and concisely
- **Reasoning** -- why this choice made, key factors

**Rules**:

1. **Concise but complete.** Each decision understandable on own without re-reading full conversation.
2. **Feature-bound only.** Only capture decisions relevant to this specific feature
3. **Resolve open questions explicitly.** When open question answered, add answer as decision and remove question from
   Open Questions list.
4. **Constraints non-negotiable.** Once constraint recorded, it binding. Changing constraint require new decision entry
   explaining why constraint being revised.
5. **Remove obsolete decisions.** When a decision is superseded and no longer relevant, remove the obsolete entry to
   avoid conflicting guidance. Preserve key rationale in the replacement decision entry.
6. **Constraint Override Protocol.** If user explicitly say override constraint (e.g., "we've changed direction"), not
   silently delete.
   Instead: (a) ask user to confirm, (b) add decision entry.