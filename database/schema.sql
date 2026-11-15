CREATE DATABASE IF NOT EXISTS college_event_db;
USE college_event_db;

CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    category VARCHAR(40) NOT NULL,
    event_date DATE NOT NULL,
    venue VARCHAR(120) NOT NULL,
    capacity INT NOT NULL,
    description VARCHAR(500),
    CHECK (capacity > 0)
);

CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    event_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('REGISTERED','CANCELLED') DEFAULT 'REGISTERED',
    CONSTRAINT fk_reg_student FOREIGN KEY (student_id) REFERENCES students(student_id),
    CONSTRAINT fk_reg_event FOREIGN KEY (event_id) REFERENCES events(event_id),
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id)
);

CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    registration_id INT NOT NULL UNIQUE,
    attended BOOLEAN NOT NULL DEFAULT FALSE,
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (registration_id) REFERENCES registrations(registration_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    registration_id INT NOT NULL UNIQUE,
    rating INT NOT NULL,
    comments VARCHAR(500),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (registration_id) REFERENCES registrations(registration_id) ON DELETE CASCADE,
    CHECK (rating BETWEEN 1 AND 5)
);

-- Report: registration count for each event
CREATE OR REPLACE VIEW event_participation AS
SELECT e.event_id, e.title, e.category, e.event_date, e.capacity,
       COUNT(CASE WHEN r.status='REGISTERED' THEN 1 END) AS registrations,
       COUNT(CASE WHEN r.status='REGISTERED' AND a.attended=TRUE THEN 1 END) AS attendees
FROM events e
LEFT JOIN registrations r ON e.event_id=r.event_id
LEFT JOIN attendance a ON r.registration_id=a.registration_id
GROUP BY e.event_id, e.title, e.category, e.event_date, e.capacity;

-- Useful SQL queries
-- 1. View all events
SELECT * FROM events ORDER BY event_date;
-- 2. Student registration status
SELECT s.roll_no, s.name, e.title, r.status
FROM registrations r JOIN students s ON r.student_id=s.student_id
JOIN events e ON r.event_id=e.event_id;
-- 3. Department-wise registrations
SELECT s.department, COUNT(*) AS total_registrations
FROM registrations r JOIN students s ON r.student_id=s.student_id
WHERE r.status='REGISTERED' GROUP BY s.department;
-- 4. Average feedback by event
SELECT e.title, ROUND(AVG(f.rating),2) AS average_rating
FROM feedback f JOIN registrations r ON f.registration_id=r.registration_id
JOIN events e ON r.event_id=e.event_id GROUP BY e.event_id,e.title;
-- 5. Event popularity
SELECT * FROM event_participation ORDER BY registrations DESC;
