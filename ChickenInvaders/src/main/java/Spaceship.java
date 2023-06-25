import java.util.*;
import processing.core.PImage;

public class Spaceship {
    public static PImage spaceshipImage;
    private final int width = 400;
    private final int height = 700;
    private int spaceshipColor;
    private float spaceshipWidth;
    private float spaceshipHeight;
    private float xPos;
    private float yPos;

    public Spaceship() {
        // Creating spaceship
        spaceshipColor = Main.pApplet.color(255, 0, 0);
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

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public float getSpaceshipWidth() {
        return spaceshipWidth;
    }

    public float getSpaceshipHeight() {
        return spaceshipHeight;
    }
}