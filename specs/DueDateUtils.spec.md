# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the D-day label for a task. A completed task shows `CLEAR` regardless of its due date. An active task without a due date has no label. Otherwise, show the signed total number of calendar days from today to the due date: `D-Day` for today, `D-N` for a future date, and `D+N` for an overdue date.

**Pre-conditions**
- `today` is a valid `LocalDate`. `dueDate` is either a valid `LocalDate` or `null`.

**Post-conditions**
- Success cases
  - A completed task returns `CLEAR` whether its due date is `2026-10-21` or `null`. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `isCompleted` is `true`, and `dueDate` is first `2026-10-21` and then `null` / When `dueDateLabel` is called for each value / Then each call returns exactly `CLEAR`.
  - An active task without a due date returns an empty string. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `isCompleted` is `false`, and `dueDate` is `null` / When `dueDateLabel` is called / Then it returns exactly `""`.
  - An active task due today returns `D-Day`. `[Unit]`
    - Tip: Given `today` and `dueDate` are both `2026-10-20` and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-Day`.
  - An active task due one calendar day later returns `D-1`. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `2026-10-21`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-1`.
  - An active task overdue by one calendar day returns `D+1`. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `2026-10-19`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D+1`.
  - Dates more than one month apart use the total calendar-day difference, not only the day component. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `2026-11-25`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-36`.
  - Dates on opposite sides of a year boundary use the total calendar-day difference. `[Unit]`
    - Tip: Given `today` is `2026-12-30`, `dueDate` is `2027-01-02`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-3`.
- Error cases
  - None; every `LocalDate` and `null` due date is valid input. `[Unit]`
    - Tip: Given `today` is `2026-10-20`, `dueDate` is `null` or `2026-10-21`, and `isCompleted` is either `false` or `true` / When `dueDateLabel` is called / Then it returns the corresponding label and does not throw.
- Edge cases
  - Immediately before, on, and after the due date, the labels are `D-1`, `D-Day`, and `D+1`. `[Unit]`
    - Tip: Given `dueDate` is `2026-10-20` and `isCompleted` is `false` / When `dueDateLabel` is called with `today` `2026-10-19`, `2026-10-20`, and `2026-10-21` / Then the results are exactly `D-1`, `D-Day`, and `D+1`, respectively.
  - Immediately before, on, and after a month boundary, one-day differences remain correct. `[Unit]`
    - Tip: Given `today` is `2026-10-31`, `isCompleted` is `false`, and `dueDate` is `2026-10-30`, `2026-10-31`, and `2026-11-01` in turn / When `dueDateLabel` is called for each date / Then the results are exactly `D+1`, `D-Day`, and `D-1`, respectively.
  - Immediately before, on, and after a year boundary, one-day differences remain correct. `[Unit]`
    - Tip: Given `today` is `2026-12-31`, `isCompleted` is `false`, and `dueDate` is `2026-12-30`, `2026-12-31`, and `2027-01-01` in turn / When `dueDateLabel` is called for each date / Then the results are exactly `D+1`, `D-Day`, and `D-1`, respectively.
  - Leap day is included in calendar-day counting. `[Unit]`
    - Tip: Given `today` is `2028-02-28`, `dueDate` is `2028-03-01`, and `isCompleted` is `false` / When `dueDateLabel` is called / Then it returns exactly `D-2`.
  - Immediately before, on, and after leap day, one-day differences remain correct. `[Unit]`
    - Tip: Given `today` is `2028-02-29`, `isCompleted` is `false`, and `dueDate` is `2028-02-28`, `2028-02-29`, and `2028-03-01` in turn / When `dueDateLabel` is called for each date / Then the results are exactly `D+1`, `D-Day`, and `D-1`, respectively.

