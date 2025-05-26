package snake;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Score")
public class Score extends HttpServlet {

    // PostgreSQL connection details (Render)
    private static final String JDBC_URL = "jdbc:postgresql://dpg-d0pnqb3e5dus73e1j4g0-a.oregon-postgres.render.com:5432/snake_uvtx";
    private static final String JDBC_USER = "snake_uvtx_user";
    private static final String JDBC_PASS = "7hOpA7eoytpjHqmVHnTrZzqesevoSGQw";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String playerName = request.getParameter("playerName");
        String scoreStr = request.getParameter("score");

        response.setContentType("text/plain");

        if (playerName == null || scoreStr == null || playerName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or invalid parameters");
            return;
        }

        try {
            int score = Integer.parseInt(scoreStr);

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String sql = "INSERT INTO scores (player, score) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Score saved");

            stmt.close();
            conn.close();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid score format");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error saving score");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String sql = "SELECT player, score FROM scores ORDER BY score DESC LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
                String name = rs.getString("player");
                int score = rs.getInt("score");
                json.append("{\"name\":\"").append(escapeJson(name))
                    .append("\",\"score\":").append(score).append("},");
            }
            if (json.length() > 1) json.setLength(json.length() - 1); // Remove trailing comma
            json.append("]");

            out.print(json.toString());

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]"); // Fallback to empty list on error
        }
    }

    private String escapeJson(String s) {
        return s.replace("\"", "\\\"");
    }
}
