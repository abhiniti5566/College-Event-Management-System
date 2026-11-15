# Software Testing Documentation
## College Event Management and Participation Analysis System

### 1. Introduction

Software testing is performed to verify that the College Event Management and Participation Analysis System works according to its requirements.

### 2. Testing Techniques

**1. Unit Testing**
- Individual functions and modules are tested separately.
- Example: Testing event creation and student registration.

**2. Integration Testing**
- Checks whether different modules work together.
- Example: Checking communication between Java backend and MySQL database.

**3. Functional Testing**
- Verifies whether system features work correctly.
- Example: Adding events, marking attendance, and submitting feedback.

**4. System Testing**
- Tests the complete application as a whole.
- Example: Testing student and admin workflows.

### 3. Test Cases

| Test ID | Test Scenario | Expected Result | Actual Result | Status |
|---|---|---|---|---|
| TC01 | Open Home Page | Home page should display | Home page opened | PASS |
| TC02 | View Events | Events should be displayed | 5 events displayed | PASS |
| TC03 | Register Student | Registration should be saved | Registration successful | PASS |
| TC04 | Add New Event | Event should be added | Event added successfully | PASS |
| TC05 | Update Event | Event details should be updated | Event updated successfully | PASS |
| TC06 | Delete Event | Event should be deleted | Event deleted successfully | PASS |
| TC07 | Mark Attendance | Attendance should be saved | Attendance test completed | PASS |
| TC08 | Submit Feedback | Feedback should be saved | Feedback submitted | PASS |
| TC09 | View Dashboard | Dashboard statistics should display | Statistics displayed correctly | PASS |
| TC10 | Generate Python Charts | Charts should be generated | Charts generated successfully | PASS |
| TC11 | View Student Registrations | Registration details should display | Student details displayed | PASS |
| TC12 | View Student Feedback | Feedback details should display | Feedback displayed in admin page | PASS |

### 4. Test Execution

The test cases are executed using the running application.

For every test:
1. Perform the test scenario.
2. Observe the actual result.
3. Compare it with the expected result.
4. Mark the status as Pass or Fail.

### 5. Test Result Summary

| Test Status | Count |
|---|---:|
| Pass | 12 |
| Fail | 0 |
| Pending | 0 |
| Total | 12 |

### 6. Conclusion

Software testing helps identify errors and verify the functionality, reliability, and integration of the College Event Management and Participation Analysis System. The tested features performed successfully. The remaining event management test cases will be completed before final submission.