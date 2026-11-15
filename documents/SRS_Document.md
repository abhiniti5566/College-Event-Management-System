# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)

## Project Title
**College Event Management and Participation Analysis System**

## 1. INTRODUCTION

### 1.1 Purpose

The purpose of this project is to develop a College Event Management and Participation Analysis System that simplifies the process of organizing and managing college events. The system allows students to view upcoming events, register for events, and submit feedback. Administrators can manage events, monitor student registrations, maintain attendance records, and view event statistics.

### 1.2 Scope

The system provides a centralized platform for managing college events and student participation.

The major functionalities include:

- Displaying upcoming college events.
- Allowing students to register for events.
- Managing event details by the administrator.
- Adding, updating, viewing, and deleting events.
- Recording student attendance.
- Collecting student feedback.
- Displaying dashboard statistics.
- Providing an upcoming events preview in the admin dashboard.
- Generating event participation reports.
- Analyzing event data using Python.
- Visualizing event registration, attendance, and category popularity.

### 1.3 Intended Audience

This document is intended for:

- Project developers.
- Project guides and faculty members.
- College administrators.
- Students using the system.

### 1.4 Project Overview

The application is developed using HTML, CSS, and JavaScript for the frontend, Java 17 for backend operations, and MySQL for database management. Python is used for data retrieval, preprocessing, analysis, and visualization of event participation.

The website contains Home, Events, About, Contact, and Admin pages. The administrator can access different management sections through the admin dashboard.

## 2. FUNCTIONAL REQUIREMENTS

Functional requirements describe the main operations performed by the system.

### FR-01: View Events

The system shall allow students to view the list of available college events with event name, category, date, venue, capacity, and description.

### FR-02: Student Registration

The system shall allow students to register for available college events by providing their roll number, name, and department.

### FR-03: Registration Management

The system shall store student registration details in the MySQL database and allow administrators to view student registrations.

### FR-04: Event Management

The administrator shall be able to add, update, view, and delete event details through the Admin Dashboard.

### FR-05: Attendance Management

The administrator shall be able to mark student attendance for registered events. The system shall store attendance details in the database.

### FR-06: Feedback Management

The system shall allow students to submit event ratings and comments. Administrators shall be able to view submitted feedback.

### FR-07: Admin Dashboard Management

The system shall provide an administrator dashboard displaying:

- Total number of events.
- Total student registrations.
- Total attendance.
- Total feedback submissions.

### FR-08: Admin Navigation

The system shall provide separate navigation options in the Admin Dashboard for:

- Manage Events.
- Existing Events.
- Student Registrations.
- Student Feedback.

The administrator shall be able to open each management section separately and return to the dashboard.

### FR-09: Upcoming Events Preview

The system shall display a preview of upcoming events in the Admin Dashboard, including event name, category, date, and venue.

### FR-10: Data Analysis

The system shall retrieve event participation data from the MySQL database using Python and preprocess the retrieved data for analysis.

### FR-11: Data Visualization

The system shall generate charts and reports to analyze:

- Event registrations.
- Student attendance.
- Category popularity.
- Event participation patterns.

## 3. NON-FUNCTIONAL REQUIREMENTS

Non-functional requirements describe the quality and performance of the system.

### NFR-01: Performance

The system should load event details and display database records within a reasonable response time.

### NFR-02: Usability

The website should provide a simple and user-friendly interface for students and administrators, with clear navigation and separate management sections.

### NFR-03: Reliability

The system should store registration, attendance, and feedback records accurately in the database.

### NFR-04: Security

Database credentials should be protected. User inputs should be validated before processing.

### NFR-05: Maintainability

The application should be organized into separate frontend, backend, database, and data analysis components.

### NFR-06: Compatibility

The website should work on commonly used web browsers and support desktop and mobile screens.

### NFR-07: Data Integrity

The system should maintain accurate relationships between students, events, registrations, attendance, and feedback records.

## 4. SYSTEM REQUIREMENTS

### 4.1 Hardware Requirements

- Processor: Intel Core i3 or above.
- RAM: 4 GB or above.
- Hard Disk: Minimum 500 MB free space.
- System: Desktop or Laptop.

### 4.2 Software Requirements

- Operating System: Windows 10 or above.
- Frontend: HTML, CSS, JavaScript.
- Backend: Java 17.
- Database: MySQL.
- Data Analysis: Python 3.
- IDE: Visual Studio Code.
- Build Tool: Apache Maven.
- Database Tool: MySQL Workbench.
- Web Browser: Google Chrome or any modern browser.

## 5. SYSTEM CONSTRAINTS

- The system requires a working MySQL database connection.
- The Java backend server must be running to access dynamic website features.
- Python analysis requires the database connection and required Python libraries.
- The system is designed for college event management activities.
- Admin dashboard operations require access to the backend server and database.

## 6. CONCLUSION

The College Event Management and Participation Analysis System provides a centralized solution for managing college events. It simplifies event viewing, student registration, event management, attendance tracking, and feedback collection.

The administrator dashboard provides separate management sections, event previews, and summary statistics. The system uses Java and MySQL for backend operations and database management. Python is used to analyze participation data and generate visual reports.

This system helps improve the organization, management, and monitoring of college events.