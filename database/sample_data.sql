USE college_event_db;
INSERT IGNORE INTO students (roll_no,name,department) VALUES
('25IT001','Arun','IT'),('25IT002','Bala','CSE'),('25IT003','Divya','IT'),
('25IT004','Kavin','ECE'),('25IT005','Meena','IT'),('25IT006','Ravi','CSE');

INSERT INTO events (title,category,event_date,venue,capacity,description) VALUES
('Code Sprint','Technical','2026-11-20','IT Lab',50,'Programming contest')
ON DUPLICATE KEY UPDATE title=VALUES(title);
INSERT INTO events (title,category,event_date,venue,capacity,description) VALUES
('AI Workshop','Workshop','2026-11-22','Seminar Hall',40,'Introduction to AI'),
('Cultural Fest','Cultural','2026-11-25','Auditorium',200,'Cultural performances');
