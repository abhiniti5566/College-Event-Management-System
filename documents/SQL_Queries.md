# SQL Queries
## College Event Management and Participation Analysis System

### 1. Display All Students
```sql
SELECT * FROM students;
```

### 2. Display All Events
```sql
SELECT * FROM events;
```

### 3. Display Upcoming Events
```sql
SELECT * FROM events
WHERE event_date >= CURDATE()
ORDER BY event_date;
```

### 4. Register a Student for an Event
```sql
INSERT INTO registrations
(student_id, event_id, registered_at, status)
VALUES (101, 1, NOW(), 'REGISTERED');
```

### 5. Display Student Registrations
```sql
SELECT r.registration_id, s.name, s.roll_no,
       e.title, r.status
FROM registrations r
JOIN students s ON r.student_id = s.student_id
JOIN events e ON r.event_id = e.event_id;
```

### 6. Display Attendance Details
```sql
SELECT s.name, e.title, a.attended
FROM attendance a
JOIN registrations r
ON a.registration_id = r.registration_id
JOIN students s ON r.student_id = s.student_id
JOIN events e ON r.event_id = e.event_id;
```

### 7. Display Student Feedback
```sql
SELECT s.name, e.title, f.rating, f.comments
FROM feedback f
JOIN registrations r
ON f.registration_id = r.registration_id
JOIN students s ON r.student_id = s.student_id
JOIN events e ON r.event_id = e.event_id;
```

### 8. Count Registrations for Each Event
```sql
SELECT e.title, COUNT(r.registration_id) AS total_registrations
FROM events e
LEFT JOIN registrations r ON e.event_id = r.event_id
GROUP BY e.event_id, e.title;
```

### 9. Count Attended Students for Each Event
```sql
SELECT e.title, COUNT(a.attendance_id) AS total_attendees
FROM events e
JOIN registrations r ON e.event_id = r.event_id
JOIN attendance a ON r.registration_id = a.registration_id
WHERE a.attended = 1
GROUP BY e.event_id, e.title;
```

### 10. Calculate Average Feedback Rating
```sql
SELECT e.title, AVG(f.rating) AS average_rating
FROM events e
JOIN registrations r ON e.event_id = r.event_id
JOIN feedback f ON r.registration_id = f.registration_id
GROUP BY e.event_id, e.title;
```

### Conclusion
These SQL queries perform student and event retrieval, registration management, attendance tracking, feedback analysis, and event-wise participation reporting.