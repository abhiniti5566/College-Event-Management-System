
package com.vcet.event;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ApiServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
            new InetSocketAddress(8080), 0
        );

        server.createContext("/api/events", exchange -> {
            try {
                if (!exchange.getRequestMethod().equals("GET")) {
                    send(exchange, 405, "{\"error\":\"Method not allowed\"}");
                    return;
                }

                StringBuilder json = new StringBuilder("[");
                boolean first = true;

                String sql = "SELECT event_id,title,category,event_date,venue,capacity,description FROM events ORDER BY event_date";

                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        if (!first) json.append(",");
                        first = false;

                        json.append("{")
                            .append("\"eventId\":").append(rs.getInt("event_id")).append(",")
                            .append("\"title\":\"").append(escape(rs.getString("title"))).append("\",")
                            .append("\"category\":\"").append(escape(rs.getString("category"))).append("\",")
                            .append("\"eventDate\":\"").append(rs.getDate("event_date")).append("\",")
                            .append("\"venue\":\"").append(escape(rs.getString("venue"))).append("\",")
                            .append("\"capacity\":").append(rs.getInt("capacity")).append(",")
                            .append("\"description\":\"").append(escape(rs.getString("description"))).append("\"")
                            .append("}");
                    }
                }

                json.append("]");
                send(exchange, 200, json.toString());

            } catch (Exception e) {
                send(exchange, 500, "{\"error\":\"Unable to load events\"}");
                e.printStackTrace();
            }
        });
        server.createContext("/api/register", exchange -> {
    try {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, 405, "{\"error\":\"POST method required\"}");
            return;
        }

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> data = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                data.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        String rollNo = data.getOrDefault("rollNo", "").trim();
        String name = data.getOrDefault("name", "").trim();
        String department = data.getOrDefault("department", "").trim();
        int eventId = Integer.parseInt(data.getOrDefault("eventId", "0"));

        if (rollNo.isEmpty() || name.isEmpty() ||
            department.isEmpty() || eventId <= 0) {
            send(exchange, 400, "{\"error\":\"All fields are required\"}");
            return;
        }

        if (rollNo.length() > 20 || name.length() > 100 ||
            department.length() > 50) {
            send(exchange, 400, "{\"error\":\"Input is too long\"}");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int studentId;

                String findStudent = "SELECT student_id FROM students WHERE roll_no = ?";

                try (PreparedStatement ps = con.prepareStatement(findStudent)) {
                    ps.setString(1, rollNo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            studentId = rs.getInt("student_id");
                        } else {
                            String insertStudent =
                                "INSERT INTO students (roll_no, name, department) VALUES (?, ?, ?)";

                            try (PreparedStatement insert = con.prepareStatement(
                                    insertStudent,
                                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                                insert.setString(1, rollNo);
                                insert.setString(2, name);
                                insert.setString(3, department);
                                insert.executeUpdate();

                                try (ResultSet keys = insert.getGeneratedKeys()) {
                                    keys.next();
                                    studentId = keys.getInt(1);
                                }
                            }
                        }
                    }
                }

                String checkEvent = "SELECT event_id FROM events WHERE event_id = ?";

                try (PreparedStatement ps = con.prepareStatement(checkEvent)) {
                    ps.setInt(1, eventId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            send(exchange, 404, "{\"error\":\"Event not found\"}");
                            return;
                        }
                    }
                }

                String checkRegistration =
                    "SELECT registration_id FROM registrations WHERE student_id = ? AND event_id = ?";

                try (PreparedStatement ps = con.prepareStatement(checkRegistration)) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, eventId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            con.rollback();
                            send(exchange, 409, "{\"error\":\"Already registered for this event\"}");
                            return;
                        }
                    }
                }

                String insertRegistration =
                    "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";

                try (PreparedStatement ps = con.prepareStatement(insertRegistration)) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, eventId);
                    ps.executeUpdate();
                }

                con.commit();
                send(exchange, 201, "{\"message\":\"Registration successful\"}");

            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Registration failed\"}");
    }
});


server.createContext("/api/admin/events", exchange -> {
    try {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            send(exchange, 405, "{\"error\":\"POST method required\"}");
            return;
        }

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> data = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                data.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        String title = data.getOrDefault("title", "").trim();
        String category = data.getOrDefault("category", "").trim();
        String eventDate = data.getOrDefault("eventDate", "").trim();
        String venue = data.getOrDefault("venue", "").trim();
        String description = data.getOrDefault("description", "").trim();
        int capacity = Integer.parseInt(
            data.getOrDefault("capacity", "0")
        );

        if (title.isEmpty() || category.isEmpty() ||
            eventDate.isEmpty() || venue.isEmpty() || capacity <= 0) {
            send(exchange, 400, "{\"error\":\"All required fields must be filled\"}");
            return;
        }

        String sql = """
            INSERT INTO events
            (title, category, event_date, venue, capacity, description)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, category);
            ps.setDate(3, java.sql.Date.valueOf(eventDate));
            ps.setString(4, venue);
            ps.setInt(5, capacity);
            ps.setString(6, description);

            ps.executeUpdate();
        }

        send(exchange, 201, "{\"message\":\"Event added successfully\"}");

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to add event\"}");
    }
});

server.createContext("/api/admin/events/update", exchange -> {
    try {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            send(exchange, 405, "{\"error\":\"POST method required\"}");
            return;
        }

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> data = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                data.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        int eventId = Integer.parseInt(data.getOrDefault("eventId", "0"));
        String title = data.getOrDefault("title", "").trim();
        String category = data.getOrDefault("category", "").trim();
        String eventDate = data.getOrDefault("eventDate", "").trim();
        String venue = data.getOrDefault("venue", "").trim();
        String description = data.getOrDefault("description", "").trim();
        int capacity = Integer.parseInt(data.getOrDefault("capacity", "0"));

        if (eventId <= 0 || title.isEmpty() || category.isEmpty()
            || eventDate.isEmpty() || venue.isEmpty() || capacity <= 0) {
            send(exchange, 400, "{\"error\":\"Invalid event details\"}");
            return;
        }

        String sql = """
            UPDATE events
            SET title=?, category=?, event_date=?, venue=?,
                capacity=?, description=?
            WHERE event_id=?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, category);
            ps.setDate(3, java.sql.Date.valueOf(eventDate));
            ps.setString(4, venue);
            ps.setInt(5, capacity);
            ps.setString(6, description);
            ps.setInt(7, eventId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                send(exchange, 404, "{\"error\":\"Event not found\"}");
                return;
            }
        }

        send(exchange, 200, "{\"message\":\"Event updated successfully\"}");

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to update event\"}");
    }
});

server.createContext("/api/admin/events/delete", exchange -> {
    try {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            send(exchange, 405, "{\"error\":\"POST method required\"}");
            return;
        }

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> data = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                data.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        int eventId = Integer.parseInt(data.getOrDefault("eventId", "0"));

        if (eventId <= 0) {
            send(exchange, 400, "{\"error\":\"Invalid event ID\"}");
            return;
        }

        String sql = "DELETE FROM events WHERE event_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            int deleted = ps.executeUpdate();

            if (deleted == 0) {
                send(exchange, 404, "{\"error\":\"Event not found\"}");
                return;
            }
        }

        send(exchange, 200, "{\"message\":\"Event deleted successfully\"}");

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to delete event. Check whether registrations exist.\"}");
    }
});

server.createContext("/api/registrations", exchange -> {
    try {
        if (!exchange.getRequestMethod().equals("GET")) {
            send(exchange, 405, "{\"error\":\"GET method required\"}");
            return;
        }

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        String sql = """
            SELECT r.registration_id, s.roll_no, s.name,
                   s.department, e.title, r.registered_at
            FROM registrations r
            JOIN students s ON r.student_id = s.student_id
            JOIN events e ON r.event_id = e.event_id
            ORDER BY r.registered_at DESC
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                    .append("\"registrationId\":").append(rs.getInt("registration_id")).append(",")
                    .append("\"rollNo\":\"").append(escape(rs.getString("roll_no"))).append("\",")
                    .append("\"name\":\"").append(escape(rs.getString("name"))).append("\",")
                    .append("\"department\":\"").append(escape(rs.getString("department"))).append("\",")
                    .append("\"eventTitle\":\"").append(escape(rs.getString("title"))).append("\",")
                    .append("\"registeredAt\":\"").append(rs.getTimestamp("registered_at")).append("\"")
                    .append("}");
            }
        }

        json.append("]");
        send(exchange, 200, json.toString());

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to load registrations\"}");
    }
});
server.createContext("/api/attendance", exchange -> {
    try {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, 405, "{\"error\":\"POST method required\"}");
            return;
        }

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> data = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                data.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        int registrationId = Integer.parseInt(
            data.getOrDefault("registrationId", "0")
        );

        int attended = Integer.parseInt(
            data.getOrDefault("attended", "-1")
        );

        if (registrationId <= 0 || (attended != 0 && attended != 1)) {
            send(exchange, 400, "{\"error\":\"Invalid attendance details\"}");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            String checkSql =
                "SELECT registration_id FROM registrations WHERE registration_id = ?";

            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, registrationId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        send(exchange, 404, "{\"error\":\"Registration not found\"}");
                        return;
                    }
                }
            }

            String findSql =
                "SELECT attendance_id FROM attendance WHERE registration_id = ?";

            boolean exists;

            try (PreparedStatement ps = con.prepareStatement(findSql)) {
                ps.setInt(1, registrationId);

                try (ResultSet rs = ps.executeQuery()) {
                    exists = rs.next();
                }
            }

            String sql;

            if (exists) {
                sql = "UPDATE attendance SET attended = ?, marked_at = CURRENT_TIMESTAMP WHERE registration_id = ?";
            } else {
                sql = "INSERT INTO attendance (attended, registration_id) VALUES (?, ?)";
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, attended);
                ps.setInt(2, registrationId);
                ps.executeUpdate();
            }
        }

        send(exchange, 200, "{\"message\":\"Attendance saved successfully\"}");

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to save attendance\"}");
    }
});
server.createContext("/api/feedback", exchange -> {
    try {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            send(exchange, 405, "{\"error\":\"POST method required\"}");
            return;
        }

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> data = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);

            if (parts.length == 2) {
                data.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        int registrationId = Integer.parseInt(
            data.getOrDefault("registrationId", "0")
        );

        int rating = Integer.parseInt(
            data.getOrDefault("rating", "0")
        );

        String comments = data.getOrDefault("comments", "");

        if (registrationId <= 0 || rating < 1 || rating > 5) {
            send(exchange, 400, "{\"error\":\"Invalid feedback details\"}");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO feedback (registration_id, rating, comments) VALUES (?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, registrationId);
                ps.setInt(2, rating);
                ps.setString(3, comments);
                ps.executeUpdate();
            }
        }

        send(exchange, 201, "{\"message\":\"Feedback submitted successfully\"}");

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Feedback submission failed\"}");
    }
});
server.createContext("/api/feedback-list", exchange -> {
    try {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "{\"error\":\"GET method required\"}");
            return;
        }

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        String sql = """
            SELECT f.feedback_id, f.registration_id,
                   s.name, e.title, f.rating, f.comments
            FROM feedback f
            JOIN registrations r
                ON f.registration_id = r.registration_id
            JOIN students s
                ON r.student_id = s.student_id
            JOIN events e
                ON r.event_id = e.event_id
            ORDER BY f.feedback_id DESC
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                    .append("\"feedbackId\":").append(rs.getInt("feedback_id")).append(",")
                    .append("\"registrationId\":").append(rs.getInt("registration_id")).append(",")
                    .append("\"name\":\"").append(escape(rs.getString("name"))).append("\",")
                    .append("\"eventTitle\":\"").append(escape(rs.getString("title"))).append("\",")
                    .append("\"rating\":").append(rs.getInt("rating")).append(",")
                    .append("\"comments\":\"").append(escape(rs.getString("comments"))).append("\"")
                    .append("}");
            }
        }

        json.append("]");
        send(exchange, 200, json.toString());

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to load feedback\"}");
    }
});

server.createContext("/api/dashboard-stats", exchange -> {
    try {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "{\"error\":\"GET method required\"}");
            return;
        }

        String sql = """
            SELECT
                (SELECT COUNT(*) FROM events) AS totalEvents,
                (SELECT COUNT(*) FROM registrations) AS totalRegistrations,
                (SELECT COUNT(*) FROM attendance
                 WHERE attended = 1) AS totalAttendance,
                (SELECT COUNT(*) FROM feedback) AS totalFeedback
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String json = "{"
                    + "\"totalEvents\":" + rs.getInt("totalEvents") + ","
                    + "\"totalRegistrations\":" + rs.getInt("totalRegistrations") + ","
                    + "\"totalAttendance\":" + rs.getInt("totalAttendance") + ","
                    + "\"totalFeedback\":" + rs.getInt("totalFeedback")
                    + "}";

                send(exchange, 200, json);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        send(exchange, 500, "{\"error\":\"Unable to load dashboard statistics\"}");
    }
});

        server.start();
        System.out.println("API Server running at http://localhost:8080");
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    private static void send(HttpExchange exchange, int status, String response)
            throws IOException {

        exchange.getResponseHeaders().set(
            "Content-Type", "application/json; charset=UTF-8"
        );
        exchange.getResponseHeaders().set(
            "Access-Control-Allow-Origin", "*"
        );

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);

        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
