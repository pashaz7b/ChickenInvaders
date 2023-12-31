import java.sql.*;
import java.util.*;

public class Score {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/scores";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pashaz7b";
    private int currentScore;

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

    public void drawScoreInfo() {
        Main.pApplet.textSize(14);
        Main.pApplet.fill(255, 0, 0); // White text color
        Main.pApplet.text("Score:" + currentScore, 60, 20); // Display the score number at the top-left corner
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

    public List<Integer> getAllScores() {
        List<Integer> scores = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT score FROM scores.scores;";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int score = rs.getInt("score");
                scores.add(score);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scores;
    }

    public void drawAllScores() {
        List<Integer> scores = getAllScores();

        Main.pApplet.background(0);
        Main.pApplet.textSize(24);
        Main.pApplet.fill(255);
        Main.pApplet.textAlign(Main.pApplet.CENTER, Main.pApplet.CENTER);

        int yOffset = 50;
        for (int i = 0; i < scores.size(); i++) {
            int score = scores.get(i);
            Main.pApplet.text("Score " + (i + 1) + ": " + score, Main.pApplet.width / 2, yOffset);
            yOffset += 30; // Adjust the spacing between the scores as needed
        }
    }

    public int getHighScore() {
        int highScore = 0;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            // Get all the rows from the scores.scores table
            String query = "SELECT id, score FROM scores.scores;";
            ResultSet rs = stmt.executeQuery(query);

            // Iterate through the result set and find the highest score
            while (rs.next()) {
                int score = rs.getInt("score");
                if (score > highScore) {
                    highScore = score;
                    int id = rs.getInt("id");
                    System.out.println("New high score found. ID: " + id + ", Score: " + highScore);
                }
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScore;
    }

}