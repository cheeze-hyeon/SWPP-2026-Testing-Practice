# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Show the correct due-date label for a task in its list row. Hide the label when there is no label to show, and clear any old text so a recycled row cannot display stale information.

**Pre-conditions**
- `textView` is a valid `TextView` used for the task row's due-date label.
- `task` is either a valid task or `null`.

**Post-conditions**
- Success cases
- A completed task displays `CLEAR`, whether it has a due date or not. `[Integration]`
    - Tip: Given a `TextView` with empty text and `GONE` visibility and a completed task with title `"Task Title"`, description `"Task Description"`, `isCompleted` `true`, and `dueDate` `2026-10-21` / When `setDueDateLabel` is called / Then the view text is exactly `"CLEAR"` and visibility is `VISIBLE`.
- An active task due on the current local date displays `D-Day`. `[Integration]`
    - Tip: Given a `TextView` with empty text and `GONE` visibility and an active task with title `"Task Title"`, description `"Task Description"`, `isCompleted` `false`, and `dueDate` equal to `LocalDate.now()` / When `setDueDateLabel` is called / Then the view text is exactly `"D-Day"` and visibility is `VISIBLE`.
- Error cases
- None; a null task is a valid input and means there is no label to display. `[Integration]`
    - Tip: Given a `TextView` with text `"CLEAR"` and `VISIBLE` visibility and `task` is `null` / When `setDueDateLabel` is called / Then the view text is empty, visibility is `GONE`, and no exception is thrown.
- Edge cases
- An active task with no due date has no label, and stale text from a recycled row is cleared. `[Integration]`
    - Tip: Given a `TextView` with text `"CLEAR"` and `VISIBLE` visibility and an active task with title `"Task Title"`, description `"Task Description"`, `isCompleted` `false`, and `dueDate` `null` / When `setDueDateLabel` is called / Then the view text is empty and visibility is `GONE`.
- Completing and then reactivating a task with no due date removes `CLEAR` from the same recycled view. `[Integration]`
    - Tip: Given the same `TextView`, first call `setDueDateLabel` with a task whose title is `"Task Title"`, description is `"Task Description"`, `isCompleted` is `true`, and `dueDate` is `null`, then call it with the same task changed to `isCompleted` `false` and `dueDate` `null` / When both calls complete / Then after the first call the text is exactly `"CLEAR"` and visibility is `VISIBLE`, and after the second call the text is empty and visibility is `GONE`.

**Undefined behavior** (optional)
- <Input left undefined, and why.>
