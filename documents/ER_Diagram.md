erDiagram
    STUDENTS ||--o{ REGISTRATIONS : makes
    EVENTS ||--o{ REGISTRATIONS : has
    REGISTRATIONS ||--o| ATTENDANCE : has
    REGISTRATIONS ||--o| FEEDBACK : submits

    STUDENTS {
        int student_id PK
        varchar roll_no UK
        varchar name
        varchar department
    }

    EVENTS {
        int event_id PK
        varchar title
        varchar category
        date event_date
        varchar venue
        int capacity
        varchar description
    }

    REGISTRATIONS {
        int registration_id PK
        int student_id FK
        int event_id FK
        timestamp registered_at
        enum status
    }

    ATTENDANCE {
        int attendance_id PK
        int registration_id FK,UK
        boolean attended
        timestamp marked_at
    }

    FEEDBACK {
        int feedback_id PK
        int registration_id FK,UK
        int rating
        varchar comments
        timestamp submitted_at
    }

    EVENT_PARTICIPATION {
        int event_id
        varchar title
        varchar category
        date event_date
        int capacity
        bigint registrations
        bigint attendees
    }