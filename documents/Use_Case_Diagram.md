
# USE CASE DIAGRAM
## College Event Management and Participation Analysis System

### Actors

1. Student
2. Administrator

### Student Use Cases

- View Events
- Register for Events
- Submit Feedback

### Administrator Use Cases

- Add Events
- Update Events
- Delete Events
- View Registrations
- Mark Attendance
- View Dashboard

### Use Case Diagram

```mermaid
flowchart LR
    Student([Student])
    Admin([Administrator])

    subgraph System[College Event Management System]
        A((View Events))
        B((Register for Events))
        C((Submit Feedback))
        D((Add Events))
        E((Update Events))
        F((Delete Events))
        G((View Registrations))
        H((Mark Attendance))
        I((View Dashboard))
    end

    Student --- A
    Student --- B
    Student --- C

    Admin --- D
    Admin --- E
    Admin --- F
    Admin --- G
    Admin --- H
    Admin --- I
```
