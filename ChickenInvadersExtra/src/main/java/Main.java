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
    private boolean mouseWasOverHelpButton = false;
    private boolean gameOver = false;
    public static int currentWave = 1;
    private int killedChickens = 0;
    private final int totalWaves = 3;
    public static String[] chickenImages = {"chicken1.png", "chicken2.png", "chicken3.png"};
    Score score = new Score();
    private boolean gameWon = false;
    private SoundFile chickenHitSound;
    float time = 400;
    float changeTime = 1.0f;
    PImage gameBackground;

    public void setup() {
        pApplet = this;
        chicken = new Chicken(0, 0, currentWave);
        Spaceship.spaceshipImage = loadImage("spaceship1.png");
        spaceship = new Spaceship();
        updateChickenImage();
        chicken.createChickens();
        score.getHighScore();

        // Load custom font, background image, and sound
        pressStartFont = createFont("PressStart2P-Regular.ttf", 24);
        startMenuBackground = loadImage("start_menu_background1.jpg");
        notificationSound = new SoundFile(this, "notification_sound.wav");
        chickenHitSound = new SoundFile(this, "chicken_hit_sound.wav");
        gameBackground = loadImage("gameBackground.jpg");
    }

    public void draw() {
        if (startMenu) {
            drawStartMenu();
            keyPressed();
        } else if (!gameOver && !gameWon) {
            background(gameBackground);
            drawWaveInfo();
            score.drawScoreInfo();
            chicken.display(screenWidth);
            spaceship.drawSpaceship();
            for (Missile missile : missiles) {
                missile.update();
                missile.drawMissile();
            }
            stroke(255, 0, 0);
            strokeWeight(8);
            time -= changeTime;
            line(0, 689, time, 689);
            noStroke();
            checkCollisions();
            checkGameOver();
        } else if (gameOver) {
            score.saveScore();
            score.updateHighScore();
            drawGameOverMessage();
        } else if (gameWon) {
            score.saveScore();
            score.updateHighScore();
            drawGameWonMessage();
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

    private boolean collidesWithSpaceship(Chicken chicken, Spaceship spaceship) {
        float leftChicken = chicken.getX();
        float rightChicken = chicken.getX() + chicken.getWidth();
        float topChicken = chicken.getY();
        float bottomChicken = chicken.getY() + chicken.getHeight();

        float leftSpaceship = spaceship.getxPos();
        float rightSpaceship = spaceship.getxPos() + spaceship.getSpaceshipWidth();
        float topSpaceship = spaceship.getyPos();
        float bottomSpaceship = spaceship.getyPos() + spaceship.getSpaceshipHeight();

        return leftSpaceship < rightChicken && rightSpaceship > leftChicken && topSpaceship < bottomChicken && bottomSpaceship > topChicken;
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

                    // Play chicken hit sound
                    chickenHitSound.play();
                    // Decrease the chicken's resistance
                    currentChicken.decreaseResistance();
                    missileIterator.remove();

                    // If the chicken's resistance reaches 0, remove the chicken and increment the killedChickens counter
                    if (currentChicken.getResistance() == 0) {
                        chickenIterator.remove();
                        score.increaseScore(currentWave);
                        killedChickens++;
                    }
                    break;
                }
            }
        }

        // Check if all chickens are killed and start the next wave if there are more waves
        if (killedChickens == chickensPerWave && currentWave < totalWaves) {
            currentWave++;
            updateChickenImage(); // Update the chicken image
            chicken.createChickens();
            killedChickens = 0;
        } else if (killedChickens == chickensPerWave && currentWave == totalWaves) {
            gameWon = true;
        }
    }

    private void checkGameOver() {
        for (Chicken currentChicken : Chicken.chickens) {
            // Check if the chicken touched the spaceship
            if (collidesWithSpaceship(currentChicken, spaceship)) {
                gameOver = true;
                break;
            }

            // Check if the chicken touched the bottom of the screen
            if (currentChicken.getY() + currentChicken.getHeight() >= screenHeight) {
                gameOver = true;
                break;
            }

            // Check if time is less than or equal to 0
            if (time <= 0) {
                gameOver = true;
                break;
            }
        }
    }

    public void updateChickenImage() {
        int index = currentWave - 1;
        if (index < chickenImages.length) {
            Chicken.chickenImage = loadImage(chickenImages[index]);
        }
    }

    private void drawWaveInfo() {
        pApplet.textSize(14);
        pApplet.fill(255, 0, 0); // White text color
        pApplet.text("Wave:" + currentWave, 350, 20); // Display the wave number at the top-left corner
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

        //Help button background
        noStroke();
        fill(255, 0, 0, 180);// Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight / 2 + 160, 200, 60, 10);

        // Check if the mouse is over the start button
        boolean mouseOverstartButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 - 30 && mouseY < screenHeight / 2 + 30;
        boolean mouseOverExitButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 50 && mouseY < screenHeight / 2 + 110;
        boolean mouseOverHelpButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 130 && mouseY < screenHeight / 2 + 190;

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

        // Play notification sound if the mouse is over the exit button for the first time
        if (mouseOverHelpButton && !mouseWasOverHelpButton) {
            notificationSound.play();
        }
        mouseWasOverHelpButton = mouseOverHelpButton;

        // Start button text
        textFont(pressStartFont, mouseOverstartButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Start Game", screenWidth / 2, screenHeight / 2 + 6);

        // Exit button text
        textFont(pressStartFont, mouseOverExitButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Exit Game", screenWidth / 2, screenHeight / 2 + 86);

        // Help button text
        textFont(pressStartFont, mouseOverHelpButton ? 18 : 15);
        fill(255, 255, 255); // Change the button text color to white
        text(" Help \n press 'h' ", screenWidth / 2, screenHeight / 2 + 160);
    }

    public void drawHelp() {
        background(0);
        textSize(24);
        fill(255);
        textAlign(CENTER, CENTER);
        text("Help", screenWidth / 2, screenHeight / 4);
        textSize(8);
        text("Click anywhere on the screen or press the space key to shoot.\n\nMove the spaceship left and right using the mouse.\n\nEliminate all the chickens to win the game.\n\nComplete all waves to win the game.", screenWidth / 2, screenHeight / 2 - 50);

        // Exit option
        textSize(15);
        fill(255);
        text("Back \n press anykey ", width / 2, height / 2 + 300);
    }

    private void drawGameOverMessage() {

        background(0);
        textSize(24);
        fill(255);
        textAlign(CENTER, CENTER);
        text("GAME OVER", screenWidth / 2, screenHeight / 2 - 50);
        text("Score: " + score.getCurrentScore(), screenWidth / 2, screenHeight / 2);

        int minutes = frameCount / 60;
        int seconds = frameCount % 60;
        String timePlayed = String.format("Time Played: %02d:%02d", minutes, seconds);
        textSize(18);
        fill(255);
        text(timePlayed, screenWidth / 2, screenHeight / 2 + 50);

        if(Score.flag) {
            text("highScore" + score.getHighScore(), screenWidth / 2, screenHeight / 2 + 100);
        }

        noLoop();
    }

    private void drawGameWonMessage() {
        background(0);
        textSize(24);
        fill(255);
        textAlign(CENTER, CENTER);
        text("YOU WIN!", screenWidth / 2, screenHeight / 2 - 50);
        text("Score: " + score.getCurrentScore(), screenWidth / 2, screenHeight / 2);

        int minutes = frameCount / 60;
        int seconds = frameCount % 60;
        String timePlayed = String.format("Time Played: %02d:%02d", minutes, seconds);
        textSize(18);
        fill(255);
        text(timePlayed, screenWidth / 2, screenHeight / 2 + 50);

        noLoop();
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

            // Check if the mouse was clicked inside the help button
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 130 && mouseY < screenHeight / 2 + 190) {
                drawHelp();
            }
        }
    }

    public void mousePressed() {
        if (!startMenu) {
            float missileX = spaceship.getxPos();
            float missileY = spaceship.getyPos() - spaceship.getSpaceshipHeight() / 2;
            missiles.add(new Missile(missileX, missileY));
        }
    }

    public void keyPressed() {
        if (!startMenu && !gameOver) {
            if (key == ' ') {
                float missileX = spaceship.getxPos();
                float missileY = spaceship.getyPos() - spaceship.getSpaceshipHeight() / 2;
                missiles.add(new Missile(missileX, missileY));
            }
        }
        else if (key == 'h') {
            drawHelp();
        }
    }

    public void settings()
    {size(screenWidth, screenHeight);}

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }
}