package com.vcet.event;

import java.sql.*;
import java.util.Scanner;

public class EventManagementApp {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== COLLEGE EVENT MANAGEMENT SYSTEM ===");
        try (Connection c = DBConnection.getConnection()) {
            System.out.println("Database connected successfully.");
            while (true) {
                System.out.println("\\n1.Admin  2.Student  3.View Events  4.Exit");
                int choice = readInt("Choose: ");
                try {
                    switch (choice) {
                        case 1 -> adminMenu(c);
                        case 2 -> studentMenu(c);
                        case 3 -> listEvents(c);
                        case 4 -> { System.out.println("Thank you!"); return; }
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (Exception ex) { System.out.println("Operation failed: " + ex.getMessage()); }
            }
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Check MySQL service and src/main/resources/db.properties.");
        }
    }

    private static void adminMenu(Connection c) throws Exception {
        while (true) {
            System.out.println("\\n--- ADMIN ---\\n1.Create event 2.Update event 3.Delete event 4.View events 5.Mark attendance 6.Reports 7.Back");
            int ch = readInt("Choose: ");
            switch (ch) {
                case 1 -> createEvent(c);
                case 2 -> updateEvent(c);
                case 3 -> deleteEvent(c);
                case 4 -> listEvents(c);
                case 5 -> markAttendance(c);
                case 6 -> reports(c);
                case 7 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void studentMenu(Connection c) throws Exception {
        while (true) {
            System.out.println("\\n--- STUDENT ---\\n1.Register 2.View registration status 3.Submit feedback 4.Back");
            int ch = readInt("Choose: ");
            switch (ch) {
                case 1 -> register(c);
                case 2 -> viewStatus(c);
                case 3 -> feedback(c);
                case 4 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }
    private static String read(String prompt) { System.out.print(prompt); return sc.nextLine().trim(); }

    private static void createEvent(Connection c) throws Exception {
        String title=read("Title: "), category=read("Category: "), date=read("Date (YYYY-MM-DD): ");
        String venue=read("Venue: "); int cap=readInt("Capacity: "); String desc=read("Description: ");
        if (cap <= 0) { System.out.println("Capacity must be positive."); return; }
        String sql="INSERT INTO events(title,category,event_date,venue,capacity,description) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,title); p.setString(2,category); p.setDate(3,Date.valueOf(date));
            p.setString(4,venue); p.setInt(5,cap); p.setString(6,desc); p.executeUpdate();
            System.out.println("Event created successfully.");
        }
    }

    private static void updateEvent(Connection c) throws Exception {
        int id=readInt("Event ID: "); String title=read("New title: ");
        String category=read("New category: "), date=read("New date (YYYY-MM-DD): ");
        String venue=read("New venue: "); int cap=readInt("New capacity: ");
        String sql="UPDATE events SET title=?,category=?,event_date=?,venue=?,capacity=? WHERE event_id=?";
        try (PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,title); p.setString(2,category); p.setDate(3,Date.valueOf(date));
            p.setString(4,venue); p.setInt(5,cap); p.setInt(6,id);
            System.out.println(p.executeUpdate()>0 ? "Event updated." : "Event ID not found.");
        }
    }

    private static void deleteEvent(Connection c) throws Exception {
        int id=readInt("Event ID to delete: ");
        try (PreparedStatement p=c.prepareStatement("DELETE FROM events WHERE event_id=?")) {
            p.setInt(1,id);
            System.out.println(p.executeUpdate()>0 ? "Event deleted." : "Event ID not found.");
        }
    }

    private static void listEvents(Connection c) throws Exception {
        String sql="SELECT e.event_id,e.title,e.category,e.event_date,e.venue,e.capacity, " +
                   "(SELECT COUNT(*) FROM registrations r WHERE r.event_id=e.event_id AND r.status='REGISTERED') AS seats " +
                   "FROM events e ORDER BY e.event_date";
        try (Statement s=c.createStatement(); ResultSet rs=s.executeQuery(sql)) {
            System.out.println("\\nID | EVENT | CATEGORY | DATE | VENUE | REGISTERED/CAPACITY");
            while(rs.next()) System.out.printf("%d | %s | %s | %s | %s | %d/%d%n",
                rs.getInt("event_id"),rs.getString("title"),rs.getString("category"),
                rs.getDate("event_date"),rs.getString("venue"),rs.getInt("seats"),rs.getInt("capacity"));
        }
    }

    private static int getOrCreateStudent(Connection c, String roll, String name, String dept) throws Exception {
        try (PreparedStatement p=c.prepareStatement("SELECT student_id FROM students WHERE roll_no=?")) {
            p.setString(1,roll); try(ResultSet r=p.executeQuery()){ if(r.next()) return r.getInt(1); }
        }
        try (PreparedStatement p=c.prepareStatement(
                "INSERT INTO students(roll_no,name,department) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1,roll); p.setString(2,name); p.setString(3,dept); p.executeUpdate();
            try(ResultSet k=p.getGeneratedKeys()){ k.next(); return k.getInt(1); }
        }
    }

    private static void register(Connection c) throws Exception {
        String roll=read("Roll number: "), name=read("Student name: "), dept=read("Department: ");
        int eventId=readInt("Event ID: ");
        c.setAutoCommit(false);
        try {
            int studentId=getOrCreateStudent(c,roll,name,dept);
            int capacity, count;
            try(PreparedStatement p=c.prepareStatement("SELECT capacity FROM events WHERE event_id=? FOR UPDATE")) {
                p.setInt(1,eventId); try(ResultSet r=p.executeQuery()) {
                    if(!r.next()) throw new Exception("Event not found.");
                    capacity=r.getInt(1);
                }
            }
            try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM registrations WHERE event_id=? AND status='REGISTERED'")) {
                p.setInt(1,eventId); try(ResultSet r=p.executeQuery()){r.next(); count=r.getInt(1);}
            }
            if(count>=capacity) throw new Exception("Event capacity is full.");
            try(PreparedStatement p=c.prepareStatement("INSERT INTO registrations(student_id,event_id) VALUES(?,?)")) {
                p.setInt(1,studentId); p.setInt(2,eventId); p.executeUpdate();
            }
            c.commit(); System.out.println("Registration successful!");
        } catch(Exception e) {
            c.rollback();
            if(e.getMessage()!=null && e.getMessage().contains("Duplicate entry"))
                System.out.println("Duplicate registration is not allowed.");
            else System.out.println("Registration failed: "+e.getMessage());
        } finally { c.setAutoCommit(true); }
    }

    private static void viewStatus(Connection c) throws Exception {
        String roll=read("Enter roll number: ");
        String sql="SELECT e.title,r.status,r.registered_at FROM registrations r JOIN students s ON r.student_id=s.student_id JOIN events e ON r.event_id=e.event_id WHERE s.roll_no=?";
        try(PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,roll); try(ResultSet r=p.executeQuery()) {
                boolean found=false;
                while(r.next()){found=true;System.out.println(r.getString(1)+" | "+r.getString(2)+" | "+r.getTimestamp(3));}
                if(!found) System.out.println("No registrations found.");
            }
        }
    }

    private static void markAttendance(Connection c) throws Exception {
        int regId=readInt("Registration ID: "); String value=read("Attended? (yes/no): ");
        boolean attended=value.equalsIgnoreCase("yes");
        String sql="INSERT INTO attendance(registration_id,attended) VALUES(?,?) ON DUPLICATE KEY UPDATE attended=VALUES(attended),marked_at=CURRENT_TIMESTAMP";
        try(PreparedStatement p=c.prepareStatement(sql)) {
            p.setInt(1,regId); p.setBoolean(2,attended);
            System.out.println(p.executeUpdate()>0 ? "Attendance saved." : "Attendance not saved.");
        }
    }

    private static void feedback(Connection c) throws Exception {
        String roll=read("Roll number: "); int eventId=readInt("Event ID: ");
        int rating=readInt("Rating (1-5): "); String comments=read("Comments: ");
        if(rating<1 || rating>5){System.out.println("Rating must be between 1 and 5.");return;}
        String find="SELECT r.registration_id FROM registrations r JOIN students s ON r.student_id=s.student_id WHERE s.roll_no=? AND r.event_id=? AND r.status='REGISTERED'";
        try(PreparedStatement p=c.prepareStatement(find)) {
            p.setString(1,roll);p.setInt(2,eventId);
            try(ResultSet r=p.executeQuery()) {
                if(!r.next()){System.out.println("Register for the event before submitting feedback.");return;}
                int regId=r.getInt(1);
                try(PreparedStatement f=c.prepareStatement("INSERT INTO feedback(registration_id,rating,comments) VALUES(?,?,?) ON DUPLICATE KEY UPDATE rating=VALUES(rating),comments=VALUES(comments),submitted_at=CURRENT_TIMESTAMP")) {
                    f.setInt(1,regId);f.setInt(2,rating);f.setString(3,comments);f.executeUpdate();
                    System.out.println("Feedback submitted.");
                }
            }
        }
    }

    private static void reports(Connection c) throws Exception {
        String sql="SELECT e.title,COUNT(r.registration_id) registrations,SUM(CASE WHEN a.attended=TRUE THEN 1 ELSE 0 END) attendees " +
                   "FROM events e LEFT JOIN registrations r ON e.event_id=r.event_id AND r.status='REGISTERED' " +
                   "LEFT JOIN attendance a ON r.registration_id=a.registration_id GROUP BY e.event_id,e.title ORDER BY registrations DESC";
        try(Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){
            System.out.println("\\nEVENT | REGISTRATIONS | ATTENDEES");
            while(r.next()) System.out.printf("%s | %d | %d%n",r.getString(1),r.getInt(2),r.getInt(3));
        }
    }
}
