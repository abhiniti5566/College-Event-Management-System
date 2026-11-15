# Relational Model
## College Event Management and Participation Analysis System

The database consists of six relations.

### 1. STUDENTS
**STUDENTS** (`student_id`, roll_no, name, department)

- Primary Key: `student_id`
- Unique Key: `roll_no`

### 2. EVENTS
**EVENTS** (`event_id`, title, category, event_date, venue, capacity, description)

- Primary Key: `event_id`

### 3. REGISTRATIONS
**REGISTRATIONS** (`registration_id`, student_id, event_id, registered_at, status)

- Primary Key: `registration_id`
- Foreign Key: `student_id` references STUDENTS(`student_id`)
- Foreign Key: `event_id` references EVENTS(`event_id`)

### 4. ATTENDANCE
**ATTENDANCE** (`attendance_id`, registration_id, attended, marked_at)

- Primary Key: `attendance_id`
- Foreign Key: `registration_id` references REGISTRATIONS(`registration_id`)
- Unique Key: `registration_id`

### 5. FEEDBACK
**FEEDBACK** (`feedback_id`, registration_id, rating, comments, submitted_at)

- Primary Key: `feedback_id`
- Foreign Key: `registration_id` references REGISTRATIONS(`registration_id`)
- Unique Key: `registration_id`

### 6. EVENT_PARTICIPATION
**EVENT_PARTICIPATION** (event_id, title, category, event_date, capacity, registrations, attendees)

- Summary table containing event-wise participation information.
- No Primary Key or Foreign Key was identified in the current database structure.

## Relationships

1. One student can have multiple registrations.
2. One event can have multiple registrations.
3. One registration can have one attendance record.
4. One registration can have one feedback record.

## Conclusion

The relational model organizes student, event, registration, attendance, feedback, and event participation information into structured tables. Primary keys, foreign keys, and unique constraints help maintain data integrity.