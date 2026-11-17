# System Architecture

```mermaid
flowchart TD
    A[Admin / Student] --> B[Java Console Application]
    B --> C[Business Logic and Validation]
    C --> D[JDBC Data Access]
    D --> E[(MySQL Database)]
    E --> F[Python Data Retrieval]
    F --> G[Pandas Preprocessing]
    G --> H[Matplotlib Charts and Reports]
```![System Architecture](SYSTEM_ARCHITECTURE.png)

### Layers

1. Presentation layer: Java menu and console input/output.
2. Business layer: event capacity, duplicate registration, attendance and feedback rules.
3. Data access layer: JDBC prepared statements and transactions.
4. Database layer: normalized MySQL tables with keys and constraints.
5. Analytics layer: Python retrieves database records, preprocesses data and creates charts.