# Test Plan and Test Cases

| Test ID | Feature | Test input / action | Expected result | Actual result |
|---|---|---|---|---|
| TC-01 | Create event | Valid title, date, venue, capacity 50 | Event is saved | To be filled during execution |
| TC-02 | Create event | Capacity 0 | Validation message; event not saved | To be filled |
| TC-03 | View events | Select View Events | All stored events are displayed | To be filled |
| TC-04 | Update event | Existing event ID and new details | Event is updated | To be filled |
| TC-05 | Delete event | Existing event ID | Event is deleted | To be filled |
| TC-06 | Student registration | Valid student and available event | Registration successful | To be filled |
| TC-07 | Duplicate registration | Same roll number and event again | Duplicate is rejected by unique constraint | To be filled |
| TC-08 | Capacity validation | Register when event is full | Registration is rejected | To be filled |
| TC-09 | Registration status | Existing roll number | Event and status are displayed | To be filled |
| TC-10 | Attendance | Existing registration ID, yes | Attendance is saved | To be filled |
| TC-11 | Feedback | Rating 5 for registered student | Feedback is saved | To be filled |
| TC-12 | Feedback validation | Rating 8 | Validation message | To be filled |
| TC-13 | Reports | Select report | Registration and attendee counts appear | To be filled |
| TC-14 | Python analytics | Run analytics with database data | CSV and three chart files are generated | To be filled |

## Testing techniques
- Equivalence partitioning: valid and invalid capacity/rating values.
- Boundary value analysis: capacity 1, capacity 0, rating 1 and rating 5.
- Negative testing: unknown event ID, duplicate registration, invalid date.
- Integration testing: Java JDBC operations with MySQL.
- Data validation testing: Python handles missing numeric values and invalid dates.
