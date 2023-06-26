import java.sql.*;
import java.util.*;

public class Score {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/scores";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pashaz7b";
    private int currentScore;
    private int waveNumber;

    public Score(int waveNumber) {
        this.waveNumber = waveNumber;
        this.currentScore = getHighScore();
    }

    public void increaseScore() {
        switch (waveNumber) {
            case 1:
                currentScore += 10;
                break;
            case 2:
                currentScore += 20;
                break;
            case 3:
                currentScore += 30;
                break;
            default:
                break;
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void saveScore() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = String.format("INSERT INTO scores (score) VALUES (%d)", currentScore);
            stmt.executeUpdate(query);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getHighScore() {
        int highScore = 0;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(score) FROM scores";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                highScore = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScore;
    }

    public void updateHighScore() {
        int currentHighScore = getHighScore();
        if (currentScore > currentHighScore) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement stmt = conn.createStatement();
                String query = String.format("UPDATE scores SET score = %d WHERE score = %d", currentScore, currentHighScore);
                stmt.executeUpdate(query);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}