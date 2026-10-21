# Testing Report: Due Date Feature

## Forked Repository

[https://github.com/cheeze-hyeon/SWPP-2026-Testing-Practice](https://github.com/cheeze-hyeon/SWPP-2026-Testing-Practice)

## Specification Refinements

### `DueDateUtils.spec.md`

The AI draft already included a multi-month example with the expected total difference of `D-36` from `2026-10-20` to `2026-11-25`. I retained that case because it directly checks the intended total-calendar-day semantics and clarified in the intent that `N` means the total number of calendar days, not only a component of a date period.

I retained and reviewed the exact expected results for completed tasks both with and without a due date (`CLEAR`), active tasks without a due date (an empty string), tasks due today (`D-Day`), tasks due one day in the future (`D-1`), and tasks overdue by one day (`D+1`). I verified that these cases define the labels and their exact formats consistently with the feature requirements.

The AI draft's expected labels immediately before and after the due date were reversed. I corrected them: with a due date of `2026-10-20`, today `2026-10-19` must produce `D-1`, and today `2026-10-21` must produce `D+1`. I also extended the boundary coverage with cases immediately before, on, and after year end and leap day. These added cases check that one-day differences remain correct across those calendar boundaries.

### `TasksListBindings.spec.md`

The AI draft already identified that the label should be cleared and hidden when there is no label, including the stale `CLEAR` case for a recycled row. I refined these conditions by making the expected UI state explicit and consistent: when a label exists, the text must match the expected label and the view must be `VISIBLE`; when no label exists, the text must be exactly `""` and the visibility must be `GONE`.

I made the completed-task case explicitly exercise both a task with a due date and one without it. I retained the active-task-without-a-date and completed-to-active rebinding cases because they verify the stale-label behavior already identified by the AI draft. The rebinding case checks that the same recycled `TextView` does not keep `CLEAR` after a completed task without a due date becomes active.

The binding calculates dates using the current local date. Its D-Day test captures `LocalDate.now()` immediately before creating the task and calls the binding immediately, rather than hard-coding a date that will become stale. Fixed date arithmetic and boundary behavior are tested separately through `dueDateLabel`.

## Test Execution

The tests were run locally from the project root with:

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

The integration tests ran on `Medium_Phone (AVD) - 17`. No production code was changed while writing or running these tests.

## Results and Comparison with the Actual Bugs

### Bug 1: Incorrect D-day count across months

The unit suite ran 15 tests: 14 passed and one failed. The failing test was `dueDateLabel_activeTaskDueAcrossMonthBoundary_countsTotalDays`.

Given `today = 2026-10-20` and `dueDate = 2026-11-25`, the specification expects `D-36`. The implementation returned `D-5`. It uses the day component of a date period rather than the total number of calendar days. The failing test therefore detects the wrong D-day count bug described in the exercise. The other unit cases, including ordinary past/today/future dates and year/leap-day boundaries, passed.

### Bug 2: Stale label is not cleared after rebinding

The integration suite ran 6 tests: 3 passed and 3 failed. The failing tests were:

- `setDueDateLabel_nullTask_clearsAndHidesLabel`
- `setDueDateLabel_activeTaskWithoutDueDate_clearsAndHidesRecycledLabel`
- `setDueDateLabel_completedThenActiveTaskWithoutDueDate_clearsStaleClear`

In each case, the specification expects empty text and `GONE` visibility when there is no label. The view retained `CLEAR` instead. The binding only updates the view when the new label is non-empty, so it leaves the old contents in a recycled row. These failures detect the stale-label bug described in the exercise, including the completed-then-active sequence. The completed-task and D-Day binding cases passed.

## Conclusion

The tests derived from the refined specifications expose both bugs described in the exercise. The failing tests identify the mismatched behavior and provide exact inputs and expected results for fixing the implementation in a separate change.
