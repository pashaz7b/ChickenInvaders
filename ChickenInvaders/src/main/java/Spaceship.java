import java.util.*;

public class Spaceship {
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
        spaceshipWidth = 40;
        spaceshipHeight = 20;
        xPos = width / 2;
        yPos = height - 50;
    }

    public void drawSpaceship() {
        // Update position based on mouse cursor
        xPos = Main.pApplet.mouseX;

        // Draw spaceship
        Main.pApplet.stroke(0);
        Main.pApplet.fill(spaceshipColor);
        Main.pApplet.rectMode(Main.pApplet.CENTER);
        Main.pApplet.rect(xPos, yPos, spaceshipWidth, spaceshipHeight);

        // Draw spaceship top
        Main.pApplet.fill(spaceshipColor);
        Main.pApplet.triangle(xPos - spaceshipWidth / 2, yPos, xPos, yPos - spaceshipHeight / 2, xPos + spaceshipWidth / 2, yPos);
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