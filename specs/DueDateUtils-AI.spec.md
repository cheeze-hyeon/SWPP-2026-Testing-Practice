# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the task's D-day 
label. Completed tasks show `CLEAR`; active tasks without a due date show no label; active tasks with a due date show the signed number of calendar days between today and the due date.

**Pre-conditions**
- `today` is a valid `LocalDate`. `dueDate` is either a valid `LocalDate` or `null`.

**Post-conditions**
- Success cases
  - A completed task returns `CLEAR`, whether its due date is `2026-10-21` or absent. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `isCompleted` is `true`, and `dueDate` is `2026-10-21` / `null` / When `dueDateLabel` is called / Then it returns exactly `CLEAR`.
  - An active task with no due date returns an empty string. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `isCompleted` is `false`, and `dueDate` is `null` / When `dueDateLabel` is called / Then it returns exactly `""`.
  - An active task due today returns `D-Day`. `[Unit]`
    - Tip: Given `today` and `dueDate` are both `2026-10-20` and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-Day`.
  - An active task due one calendar day later returns `D-1`. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `2026-10-21`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-1`.
  - An active task overdue by one calendar day returns `D+1`. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `2026-10-19`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D+1`.
  - An active task due across a month boundary shows the total calendar-day difference. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `2026-11-25`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-36`.
  - An active task due across a year boundary shows the total calendar-day difference. `[Unit]`
    - Tip: Given `today` is `2026-12-30`, `dueDate` is `2027-01-02`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-3`.
- Error cases
  - None; every `LocalDate` value and a missing due date are valid inputs. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `null` or `2026-10-21`, and `isCompleted` is either `false` or `true` / When `dueDateLabel` is called / Then it returns a label and does not throw.
- Edge cases
  - The label changes at the due-date boundary: one day before, on the due date, and one day after produce `D+1`, `D-Day`, and `D-1`, respectively. `[Unit]`
    - Tip: Given `dueDate` is `2026-10-20` and `isCompleted` is `false` / When `dueDateLabel` is called with `today` `2026-10-19`, `2026-10-20`, and `2026-10-21` / Then it returns exactly `D+1`, `D-Day`, and `D-1`, respectively.
  - Calendar-day counting remains correct immediately around a month boundary. `[Unit]`
    - Tip: Given `today` is `2026-10-31`, `isCompleted` is `false`, and `dueDate` is `2026-10-30`, `2026-10-31`, or `2026-11-01` / When `dueDateLabel` is called / Then it returns exactly `D+1`, `D-Day`, or `D-1`, respectively.

