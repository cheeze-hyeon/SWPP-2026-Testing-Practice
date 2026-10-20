# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Display the task's due-date label in its list row. Show the view when a label exists. When the task has no label, clear its text and hide it so a recycled row cannot show stale content.

**Pre-conditions**
- `textView` is the due-date label view for a task row.
- `task` is either a valid `Task` or `null`.
- The label for a dated active task is calculated against the current local date when the adapter is called.

**Post-conditions**
- Success cases
  - A completed task displays `CLEAR` whether it has a due date or not. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE` and a task with title `"Task Title"`, description `"Task Description"`, `isCompleted` `true`, and `dueDate` first `2026-10-21` and then `null` / When `setDueDateLabel` is called for each task / Then the view displays exactly `"CLEAR"` and has visibility `VISIBLE` after both calls.
  - An active task due on the date captured from `LocalDate.now()` immediately before the call displays `D-Day`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, `today` is captured as `LocalDate.now()` immediately before the call, and a task has title `"Task Title"`, description `"Task Description"`, `isCompleted` `false`, and `dueDate` equal to that captured `today` / When `setDueDateLabel` is called immediately / Then the view displays exactly `"D-Day"` and has visibility `VISIBLE`.
- Error cases
  - None; a null task is valid input and means there is no label to display. `[Integration]`
    - Tip: Given a `TextView` with text `"CLEAR"` and visibility `VISIBLE` and `task` is `null` / When `setDueDateLabel` is called / Then the view text is exactly `""`, its visibility is `GONE`, and no exception is thrown.
- Edge cases
  - An active task without a due date clears stale text and hides the label view. `[Integration]`
    - Tip: Given a `TextView` with text `"CLEAR"` and visibility `VISIBLE` and a task with title `"Task Title"`, description `"Task Description"`, `isCompleted` `false`, and `dueDate` `null` / When `setDueDateLabel` is called / Then the view text is exactly `""` and its visibility is `GONE`.
  - When a completed task without a due date becomes active without a due date, the same recycled view no longer displays `CLEAR`. `[Integration]`
    - Tip: Given the same `TextView`, first call `setDueDateLabel` with a task with title `"Task Title"`, description `"Task Description"`, `isCompleted` `true`, and `dueDate` `null`, then call it with the same task changed to `isCompleted` `false` and `dueDate` `null` / When both calls complete / Then after the first call the text is exactly `"CLEAR"` and visibility is `VISIBLE`, and after the second call the text is exactly `""` and visibility is `GONE`.

