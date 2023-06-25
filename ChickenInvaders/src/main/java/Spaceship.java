import java.util.*;
import processing.core.PImage;

public class Spaceship implements SpaceshipInterface{
    public static PImage spaceshipImage;
    private final int width = 400;
    private final int height = 700;
    private float spaceshipWidth;
    private float spaceshipHeight;
    private float xPos;
    private float yPos;

    public Spaceship() {
        // Creating spaceship
        spaceshipWidth = 100;
        spaceshipHeight = 80;
        xPos = width / 2;
        yPos = height - 50;
    }

    public void drawSpaceship() {
        // Update position based on mouse cursor
        xPos = Main.pApplet.mouseX;

        // Draw spaceship
        Main.pApplet.imageMode(Main.pApplet.CENTER);
        Main.pApplet.image(spaceshipImage, xPos, yPos, spaceshipWidth, spaceshipHeight);
    }

    public static PImage getSpaceshipImage() {
        return spaceshipImage;
    }

    public static void setSpaceshipImage(PImage spaceshipImage) {
        Spaceship.spaceshipImage = spaceshipImage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getSpaceshipWidth() {
        return spaceshipWidth;
    }

    public void setSpaceshipWidth(float spaceshipWidth) {
        this.spaceshipWidth = spaceshipWidth;
    }

    public float getSpaceshipHeight() {
        return spaceshipHeight;
    }

    public void setSpaceshipHeight(float spaceshipHeight) {
        this.spaceshipHeight = spaceshipHeight;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }
}