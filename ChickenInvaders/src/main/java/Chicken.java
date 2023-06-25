import java.util.*;
import processing.core.PImage;

public class Chicken implements ChickenInterface {

    public static ArrayList<Chicken> chickens = new ArrayList<>();
    public static PImage chickenImage;
    private final int width =60;
    private final int height = 48;
    private float x;
    private float y;
    private float speedX;

    public Chicken(float x, float y) {
        this.x = x;
        this.y = y;
        this.speedX = 1;
    }

    public void createChickens() {
        int rows = 4;
        int cols = 4;
        float startX = 30;
        float startY = 30;
        float xOffset = 90;
        float yOffset = 90;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float x = startX + j * xOffset;
                float y = startY + i * yOffset;
                chickens.add(new Chicken(x, y));
            }
        }
    }

    public void update(int screenWidth) {
        x += speedX;

        // Change direction if the chicken hits the screen edges
        if (x <= 0 || x + width >= screenWidth) {
            speedX = -speedX;
            y += height;
        }
    }

    public void display(int screenWidth) {
        for (Chicken chicken : chickens) {
            chicken.update(screenWidth);
            // Draw the chicken image instead of a rectangle
            Main.pApplet.image(chickenImage, chicken.getX(), chicken.getY(), chicken.getWidth(), chicken.getHeight());
        }
    }

    //*******************************************************************

    // Getters and setters
    public static ArrayList<Chicken> getChickens() {
        return chickens;
    }

    public static void setChickens(ArrayList<Chicken> chickens) {
        Chicken.chickens = chickens;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }
}