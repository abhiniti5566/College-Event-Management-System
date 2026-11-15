# Software Requirements Specification (SRS)
## College Event Management and Participation Analysis System

### 1. Introduction
The system digitizes the management of technical symposiums, workshops, seminars and cultural events conducted by a college. It replaces manual records with a centralized database and provides participation analysis.

### 2. Problem statement
Manual event records can cause duplicate registrations, capacity errors, difficulty tracking attendance and limited understanding of event popularity. The proposed system stores event, student, registration, attendance and feedback information in MySQL. Java provides the application logic and Python generates analytical visualizations.

### 3. Objectives
- Maintain accurate event information.
- Allow students to register for events.
- Prevent duplicate registrations and over-capacity registration.
- Record attendance and feedback.
- Generate participation reports.
- Analyze event trends using Python.

### 4. Scope
The prototype supports a single college and its events. It includes admin event CRUD, student registration, attendance, feedback, reports and Python analytics. Online payment, email/SMS notifications and production authentication are outside the current scope.

### 5. Stakeholders
- Administrator / event coordinator
- Students
- Faculty evaluator

### 6. Functional requirements
| ID | Requirement |
|---|---|
| FR-01 | Admin can create an event. |
| FR-02 | Admin can update event details. |
| FR-03 | Admin can view all events. |
| FR-04 | Admin can delete an event. |
| FR-05 | Student can register for an event. |
| FR-06 | Duplicate registration must be rejected. |
| FR-07 | Registration must be rejected when capacity is full. |
| FR-08 | Admin can record attendance for a registration. |
| FR-09 | Student can submit feedback for an event. |
| FR-10 | User can view event and registration status. |
| FR-11 | System can report registration and attendance counts. |
| FR-12 | Python can retrieve, preprocess and visualize event data. |

### 7. Non-functional requirements
- Usability: simple menu-driven interface.
- Performance: database queries should complete promptly for normal college-sized data.
- Reliability: database constraints prevent duplicate registrations.
- Maintainability: Java classes and Python analysis are separated.
- Portability: Java and Python are cross-platform.
- Data integrity: primary keys, foreign keys, unique constraints and validation are used.
- Security: credentials are kept in a configuration file and must not be committed publicly.

### 8. Hardware and software requirements
- Computer with 4 GB RAM or more.
- Windows / Linux / macOS.
- Java JDK 17+, Maven, MySQL 8+.
- Python 3.10+, pandas, matplotlib, mysql-connector-python.

### 9. Assumptions and constraints
- Each student has a unique roll number.
- Each event has a positive capacity.
- A student may register once for a particular event.
- Feedback rating is from 1 to 5.
- Attendance is recorded only for an existing registration.

### 10. Inputs and outputs
Inputs: event title, category, date, venue, capacity, student roll number, name, department, attendance status and feedback.
Outputs: event list, registration status, attendance summary, feedback summary and Python charts.

### 11. Future enhancements
Web/mobile interface, secure login, QR attendance, email notifications, certificates and cloud hosting.
