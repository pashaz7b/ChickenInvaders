import processing.core.*;
import processing.sound.*;
import java.util.*;

public class Main extends PApplet {
    public static PApplet pApplet;

    private final int screenWidth = 400;
    private final int screenHeight = 700;
    public Chicken chicken;
    public Spaceship spaceship;
    private ArrayList<Missile> missiles = new ArrayList<>();
    private boolean startMenu = true;
    private PFont pressStartFont;
    private PImage startMenuBackground;
    private SoundFile notificationSound;
    private boolean mouseWasOverstartButton = false;
    private boolean mouseWasOverExitButton = false;

    public void setup() {
        pApplet = this;
        chicken = new Chicken(0, 0, 0, 0, 0);
        spaceship = new Spaceship();
        chicken.createChickens();

        // Load custom font, background image, and sound
        pressStartFont = createFont("PressStart2P-Regular.ttf", 24);
        startMenuBackground = loadImage("start_menu_background4.jpg");
        notificationSound = new SoundFile(this, "notification_sound.wav");
    }

    public void draw() {
        if (startMenu) {
            drawStartMenu();
        } else {
            background(0);
            chicken.display();
            spaceship.drawSpaceship();

            for (Missile missile : missiles) {
                missile.update();
                missile.drawMissile();
            }
        }
    }

    public void drawStartMenu() {
        // Draw background image stretched to fit the screen
        image(startMenuBackground, 0, 0, screenWidth, screenHeight);

        // Title
        textFont(pressStartFont, 24);
        fill(255, 255, 0); // Change the title color to yellow
        textAlign(CENTER);
        text("Chicken Invaders", screenWidth / 2, screenHeight / 3);

        // Start button background
        noStroke();
        fill(255, 0, 0, 180);// Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight / 2, 200, 60, 10);

        //Exit button background
        noStroke();
        fill(255, 0, 0, 180);// Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight / 2 + 80, 200, 60, 10);

        // Check if the mouse is over the start button
        boolean mouseOverstartButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 - 30 && mouseY < screenHeight / 2 + 30;
        boolean mouseOverExitButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 50 && mouseY < screenHeight / 2 + 110;

        // Play notification sound if the mouse is over the button for the first time
        if (mouseOverstartButton && !mouseWasOverstartButton) {
            notificationSound.play();
        }
        mouseWasOverstartButton = mouseOverstartButton;

        // Play notification sound if the mouse is over the exit button for the first time
        if (mouseOverExitButton && !mouseWasOverExitButton) {
            notificationSound.play();
        }
        mouseWasOverExitButton = mouseOverExitButton;

        // Start button text
        textFont(pressStartFont, mouseOverstartButton ? 20 : 18);
        fill(255, 255,255); // Change the button text color to white
        text("Start Game", screenWidth / 2, screenHeight / 2 + 6);

        // Exit button text
        textFont(pressStartFont, mouseOverExitButton ? 20 : 18);
        fill(255, 255,255); // Change the button text color to white
        text("Exit Game", screenWidth / 2, screenHeight / 2 + 86);
    }

    public void mouseClicked() {
        if (startMenu) {
            // Check if the mouse was clicked inside the start button
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 - 30 && mouseY < screenHeight / 2 + 30) {
                startMenu = false;
            }

            // Check if the mouse was clicked inside the exit button
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 50 && mouseY < screenHeight / 2 + 110) {
                exit();
            }
        }
    }

    public void mousePressed() {
        if (!startMenu) {
            float missileX = spaceship.getXPos();
            float missileY = spaceship.getYPos() - spaceship.getSpaceshipHeight() / 2;
            missiles.add(new Missile(missileX, missileY));
        }
    }

    public void settings() {
        size(screenWidth, screenHeight);
    }

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }
}