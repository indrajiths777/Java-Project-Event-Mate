package projk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserEventDAO {

    // --------- USER OPERATIONS ---------

    public static boolean validateUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users(username,password) VALUES(?,?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- EVENT OPERATIONS ---------

    public static boolean addEvent(String name, String venue, String date, String college) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO events(event_name,venue,event_date,college_name) VALUES(?,?,?,?)")) {
            ps.setString(1, name);
            ps.setString(2, venue);
            ps.setString(3, date);
            ps.setString(4, college);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllEvents() {
        List<String> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id,event_name,venue,event_date,college_name FROM events");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String line = rs.getInt("id") + " | " + rs.getString("event_name") + " | " +
                        rs.getString("venue") + " | " + rs.getString("event_date") + " | " +
                        rs.getString("college_name");
                events.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    public static boolean deleteEvent(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM events WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateEvent(int id, String name, String venue, String date, String college) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE events SET event_name=?,venue=?,event_date=?,college_name=? WHERE id=?")) {
            ps.setString(1, name);
            ps.setString(2, venue);
            ps.setString(3, date);
            ps.setString(4, college);
            ps.setInt(5, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

