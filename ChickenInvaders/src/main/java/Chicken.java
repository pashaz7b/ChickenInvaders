import java.util.*;
import processing.core.PImage;

public class Chicken {

   public static ArrayList<Chicken> chickens = new ArrayList<>();
    public static PImage chickenImage;
    private final int width = 50;
    private final int height = 40;
    private float x;
    private float y;
    private float speedX;
    private int colorR;
    private int colorG;
    private int colorB;

   public Chicken(float x, float y, int colorR, int colorG, int colorB) {
        this.x = x;
        this.y = y;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.speedX = 1;
    }

   public void createChickens() {
       int rows = 4;
       int cols = 4; // Change the number of columns here
       float startX = 30;
       float startY = 30;
       float xOffset = 90;
       float yOffset = 90;

       for (int i = 0; i < rows; i++) {
           for (int j = 0; j < cols; j++) {
               int randomR = (int) Main.pApplet.random(256);
               int randomG = (int) Main.pApplet.random(256);
               int randomB = (int) Main.pApplet.random(256);
               float x = startX + j * xOffset;
               float y = startY + i * yOffset;
               chickens.add(new Chicken(x, y, randomR, randomG, randomB));
           }
       }
    }

   public void update() {
        x += speedX;

        // Change direction if the chicken hits the screen edges
        if (x <= 0 || x + width >= Main.pApplet.width) {
            speedX = -speedX;
            y += height;
        }
    }

    public void display() {
        for (Chicken chicken : chickens) {
            chicken.update();
            // Draw the chicken image instead of a rectangle
            Main.pApplet.image(chickenImage, chicken.getX(), chicken.getY(), chicken.getWidth(), chicken.getHeight());
        }
    }

//***********************************************************************************************

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

    public int getColorR() {
        return colorR;
    }

    public void setColorR(int colorR) {
        this.colorR = colorR;
    }

    public int getColorG() {
        return colorG;
    }

    public void setColorG(int colorG) {
        this.colorG = colorG;
    }

    public int getColorB() {
        return colorB;
    }

    public void setColorB(int colorB) {
        this.colorB = colorB;
    }
}