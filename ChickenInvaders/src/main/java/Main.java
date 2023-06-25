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
    private int currentWave = 1;
    private int killedChickens = 0;
    private final int totalWaves = 3;

    public void setup() {
        pApplet = this;
        chicken = new Chicken(0, 0, 0, 0, 0);
        Spaceship.spaceshipImage = loadImage("spaceship3.png");
        spaceship = new Spaceship();
        Chicken.chickenImage = loadImage("chicken2.png");
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
            background(128, 52, 235);
            drawWaveInfo();
            chicken.display();
            spaceship.drawSpaceship();

            for (Missile missile : missiles) {
                missile.update();
                missile.drawMissile();
            }

            checkCollisions();
        }
    }

    private boolean collides(Chicken chicken, Missile missile) {
        float leftChicken = chicken.getX();
        float rightChicken = chicken.getX() + chicken.getWidth();
        float topChicken = chicken.getY();
        float bottomChicken = chicken.getY() + chicken.getHeight();

        float leftMissile = missile.getX();
        float rightMissile = missile.getX() + missile.getMissileWidth();
        float topMissile = missile.getY();
        float bottomMissile = missile.getY() + missile.getMissileHeight();

        return leftMissile < rightChicken && rightMissile > leftChicken && topMissile < bottomChicken && bottomMissile > topChicken;
    }

    private void checkCollisions() {
        int chickensPerWave = 4 * 4; // This should match the rows * cols in createChickens()

        Iterator<Chicken> chickenIterator = Chicken.chickens.iterator();
        while (chickenIterator.hasNext()) {
            Chicken currentChicken = chickenIterator.next();
            Iterator<Missile> missileIterator = missiles.iterator();
            while (missileIterator.hasNext()) {
                Missile currentMissile = missileIterator.next();

                // Check for collision between the current chicken and missile
                if (collides(currentChicken, currentMissile)) {
                    // Remove the chicken and missile from their respective lists
                    chickenIterator.remove();
                    missileIterator.remove();
                    // Increment the killedChickens counter
                    killedChickens++;
                    break;
                }
            }
        }

        // Check if all chickens are killed and start the next wave if there are more waves
        if (killedChickens == chickensPerWave && currentWave < totalWaves) {
            currentWave++;
            chicken.createChickens();
            killedChickens = 0;
        }
    }

    private void drawWaveInfo() {
        pApplet.textSize(14);
        pApplet.fill(255,0,0); // White text color
        pApplet.text("Wave: " + currentWave, 60, 20); // Display the wave number at the top-left corner
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