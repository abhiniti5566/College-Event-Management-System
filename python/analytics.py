import os
import mysql.connector
import pandas as pd
import matplotlib.pyplot as plt
from db_config import DB_CONFIG

OUTPUT = os.path.join(os.path.dirname(__file__), "output")
os.makedirs(OUTPUT, exist_ok=True)

def main():
    connection = mysql.connector.connect(**DB_CONFIG)
    try:
        query = '''
        SELECT e.event_id, e.title, e.category, e.event_date, e.capacity,
               COUNT(DISTINCT CASE WHEN r.status='REGISTERED' THEN r.registration_id END) AS registrations,
               COUNT(DISTINCT CASE WHEN r.status='REGISTERED' AND a.attended=1 THEN r.registration_id END) AS attendees
        FROM events e
        LEFT JOIN registrations r ON e.event_id=r.event_id
        LEFT JOIN attendance a ON r.registration_id=a.registration_id
        GROUP BY e.event_id,e.title,e.category,e.event_date,e.capacity
        ORDER BY e.event_date
        '''
        df = pd.read_sql(query, connection)
        if df.empty:
            print("No event data found. Add events and registrations first.")
            return

        # Preprocessing
        df["event_date"] = pd.to_datetime(df["event_date"], errors="coerce")
        df["registrations"] = pd.to_numeric(df["registrations"], errors="coerce").fillna(0).astype(int)
        df["attendees"] = pd.to_numeric(df["attendees"], errors="coerce").fillna(0).astype(int)
        df["capacity"] = pd.to_numeric(df["capacity"], errors="coerce").fillna(0).astype(int)
        df["attendance_rate"] = (df["attendees"] / df["registrations"].replace(0, pd.NA) * 100).fillna(0)
        df = df.dropna(subset=["title", "event_date"])

        print("\\nEVENT PARTICIPATION DATA")
        print(df.to_string(index=False))
        df.to_csv(os.path.join(OUTPUT, "event_analysis.csv"), index=False)

        # Chart 1: registrations by event
        plt.figure(figsize=(9, 5))
        plt.bar(df["title"], df["registrations"])
        plt.title("Event-wise Student Registrations")
        plt.xlabel("Event")
        plt.ylabel("Number of Registrations")
        plt.xticks(rotation=25, ha="right")
        plt.tight_layout()
        plt.savefig(os.path.join(OUTPUT, "event_registrations.png"))
        plt.close()

        # Chart 2: registrations vs attendees
        plt.figure(figsize=(9, 5))
        x = range(len(df))
        plt.bar([i - 0.2 for i in x], df["registrations"], width=0.4, label="Registrations")
        plt.bar([i + 0.2 for i in x], df["attendees"], width=0.4, label="Attendees")
        plt.xticks(list(x), df["title"], rotation=25, ha="right")
        plt.title("Registrations vs Actual Attendance")
        plt.ylabel("Students")
        plt.legend()
        plt.tight_layout()
        plt.savefig(os.path.join(OUTPUT, "attendance_comparison.png"))
        plt.close()

        # Chart 3: category popularity
        category = df.groupby("category", as_index=False)["registrations"].sum()
        plt.figure(figsize=(7, 5))
        plt.pie(category["registrations"], labels=category["category"],
                autopct="%1.1f%%", startangle=90)
        plt.title("Participation by Event Category")
        plt.tight_layout()
        plt.savefig(os.path.join(OUTPUT, "category_popularity.png"))
        plt.close()

        print("\\nSaved CSV and charts in:", OUTPUT)
    finally:
        connection.close()

if __name__ == "__main__":
    main()
