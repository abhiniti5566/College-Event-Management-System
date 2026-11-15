# Database Normalization
## College Event Management and Participation Analysis System

### Introduction
Database normalization is the process of organizing data into tables to reduce data redundancy and improve data consistency.

### 1. First Normal Form (1NF)
A table is in 1NF when:
- Each column contains atomic (single) values.
- Each row is unique.
- There are no repeating groups.

**Example: STUDENTS Table**

| student_id (PK) | roll_no | name | department |
|---|---|---|---|
| 101 | IT001 | Arun | IT |
| 102 | IT002 | Priya | IT |

Each column contains a single value. Therefore, the table satisfies 1NF.

### 2. Second Normal Form (2NF)
A table is in 2NF when:
- It is already in 1NF.
- Every non-key attribute depends on the whole primary key.
- There is no partial dependency.

**Example: REGISTRATIONS Table**

| registration_id (PK) | student_id (FK) | event_id (FK) | status |
|---|---|---|---|
| 1 | 101 | 201 | REGISTERED |
| 2 | 102 | 202 | REGISTERED |

The registration details are stored separately from student and event details. This avoids unnecessary duplication.

### 3. Third Normal Form (3NF)
A table is in 3NF when:
- It is already in 2NF.
- There is no transitive dependency.
- Non-key attributes depend only on the primary key.

**Example: STUDENTS and EVENTS Tables**

Student details are stored in STUDENTS, while event details are stored in EVENTS. REGISTRATIONS stores only the corresponding student and event IDs with registration details.

This separation reduces duplication and improves consistency.

### Normalization Applied in the Project

1. STUDENTS – stores student information.
2. EVENTS – stores event information.
3. REGISTRATIONS – stores student-event registration details.
4. ATTENDANCE – stores attendance information.
5. FEEDBACK – stores student feedback.
6. EVENT_PARTICIPATION – stores event-wise participation summary information.

### Advantages of Normalization
- Reduces data redundancy.
- Improves data consistency.
- Avoids update anomalies.
- Makes database maintenance easier.
- Improves data organization.

### Conclusion
The project database separates student, event, registration, attendance, and feedback information into different tables. This structure supports normalization and helps maintain database integrity.