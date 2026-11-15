# College Event Management and Participation Analysis System

A web-based college event management system developed to manage events, student registrations, attendance, feedback, and participation analysis.

## Technologies Used

- **Frontend:** HTML, CSS, JavaScript
- **Backend:** Java 17, JDBC, Maven
- **Database:** MySQL 8+
- **Data Analysis:** Python 3
- **Python Libraries:** pandas, matplotlib, mysql-connector-python
- **IDE:** Visual Studio Code

## Main Modules

1. **Home:** Displays the college event management website.
2. **Events:** Displays available college events.
3. **Admin:** Manage events, view registrations, and student feedback.
4. **Student Registration:** Allows students to register for events.
5. **Attendance:** Records attendance for registered students.
6. **Feedback:** Collects student ratings and comments.
7. **Dashboard:** Displays event, registration, attendance, and feedback statistics.
8. **Python Analytics:** Retrieves database information, processes data, and generates visualizations.

## Database Design

Database Name: `college_event_db`

The database contains six tables:

- students
- events
- registrations
- attendance
- feedback
- event_participation

Primary keys and foreign keys are used to maintain relationships and data integrity.

## Project Structure

```text
College_Event_Management_System/
│
├── frontend/
│   ├── index.html
│   ├── events.html
│   ├── admin.html
│   ├── about.html
│   ├── contact.html
│   ├── style.css
│   └── JavaScript files
│
├── src/
│   └── main/
│       ├── java/com/vcet/event/
│       │   ├── ApiServer.java
│       │   ├── DBConnection.java
│       │   ├── Event.java
│       │   ├── Student.java
│       │   └── EventManagementApp.java
│       │
│       └── resources/
│           └── db.properties
│
├── database/
│
├── python/
│   ├── analytics.py
│   └── output/
│
├── documents/
│   ├── SRS_Document.md
│   ├── Class_Diagram.md
│   ├── System_Architecture.md
│   ├── ER_Diagram.md
│   ├── Relational_Model.md
│   ├── Normalization.md
│   └── SQL_Queries.md
│
├── testing/
│
└── README.md
```

## Database Setup

1. Install MySQL 8+.
2. Start the MySQL server.
3. Open MySQL Workbench.
4. Create or select the database `college_event_db`.
5. Import and execute the database SQL scripts from the `database` folder.
6. Configure the database username and password in `src/main/resources/db.properties`.

## Run the Java Backend

Open the project root folder in VS Code terminal.

Execute:

```bash
mvn clean compile
```

Then start the backend:

```bash
mvn exec:java
```

Backend API URL:

`http://localhost:8080`

Keep the backend terminal running while using the website.

## Run the Frontend

1. Open the `frontend` folder in VS Code.
2. Open `index.html`.
3. Run it using the Live Server extension.
4. Use the website navigation to access the available pages.

## Run Python Analytics

Install the required libraries:

```bash
python -m pip install pandas matplotlib mysql-connector-python
```

Configure the MySQL connection details in the Python configuration file, if required.

Run:

```bash
python python/analytics.py
```

Generated charts and reports are saved in:

`python/output/`

## Testing

The system is tested for:

- Event creation, updating, and deletion.
- Student event registration.
- Duplicate registration prevention.
- Attendance management.
- Feedback submission.
- Dashboard statistics.
- Python data analysis and visualization.

Refer to `documents/Testing_Documentation.md` for test cases and results.

## Future Enhancements

- Secure admin and student login.
- Email notifications for event registration.
- Improved student dashboard.
- Online event participation certificates.
- Advanced participation analytics.

## Conclusion

The College Event Management and Participation Analysis System simplifies college event administration by integrating a website, Java backend, MySQL database, and Python-based data visualization.

This project is developed for academic and educational purposes.