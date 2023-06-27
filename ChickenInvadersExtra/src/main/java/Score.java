import java.sql.*;
import java.util.*;

public class Score {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/scores";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pashaz7b";
    private int currentScore;
    public static boolean flag = false;

    public Score() {
        this.currentScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void increaseScore(int waveNumber) {
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

    public void drawScoreInfo(){
        Main.pApplet.textSize(14);
        Main.pApplet.fill(255,0,0); // White text color
        Main.pApplet.text("Score:" + currentScore , 60, 20); // Display the score number at the top-left corner
    }

    public void saveScore() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO `scores`.`scores` (`score`) VALUES (" + currentScore + ");";
            stmt.executeUpdate(query);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Handle the ClassNotFoundException
            e.printStackTrace();
        }
    }

    public int getHighScore() {
        int highScore = 0;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM scores.scores WHERE score = (SELECT MAX(score) FROM scores.scores) LIMIT 1;";
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
                String query = String.format("UPDATE scores.scores SET score = ? WHERE score = ?;", currentScore, currentHighScore);
                stmt.executeUpdate(query);
                flag = true;
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}