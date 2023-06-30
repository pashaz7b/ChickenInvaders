import processing.core.*;
import processing.sound.*;
import java.util.*;

public class Main extends PApplet {
    public static PApplet pApplet;
    public final int screenWidth = 400;
    public final int screenHeight = 700;
    public Chicken chicken;
    public Spaceship spaceship;
    public ArrayList<Missile> missiles = new ArrayList<>();
    public boolean startMenu = true;
    public PFont pressStartFont;
    public PImage startMenuBackground;
    public SoundFile notificationSound;
    public boolean mouseWasOverstartButton = false;
    public boolean mouseWasOverExitButton = false;
    public boolean mouseWasOverHelpButton = false;
    public boolean mouseWasOverScoresButton = false;
    public boolean gameOver = false;
    public static int currentWave = 1;
    public int killedChickens = 0;
    public final int totalWaves = 3;
    public static String[] chickenImages = {"chicken1.png", "chicken2.png", "chicken3.png"};
    Score score = new Score();
    public boolean gameWon = false;
    public SoundFile chickenHitSound;
    public float time = 400;
    public float changeTime = 0.08f;
    PImage gameBackground;
    public Boss boss;
    boolean displayHelp = false;
    boolean displayScores = false;
    boolean mouseWasOverBackButton = false;
    public int currentHighScore = 0;
    boolean bossFightTime = false;
    boolean mouseWasOverFinalExitButton = false;

    public void setup()  {
        pApplet = this;
        chicken = new Chicken(0, 0, currentWave);
        Spaceship.spaceshipImage = loadImage("spaceship1.png");
        spaceship = new Spaceship();
        updateChickenImage();
        chicken.createChickens();
        currentHighScore = score.getHighScore();

        // Load custom font, background image, and sound
        pressStartFont = createFont("PressStart2P-Regular.ttf", 24);
        startMenuBackground = loadImage("start_menu_background1.jpg");
        notificationSound = new SoundFile(this, "notification_sound.wav");
        chickenHitSound = new SoundFile(this, "chicken_hit_sound.wav");
        gameBackground = loadImage("gameBackground.jpg");
        Boss.bossImage = loadImage("Boss.png");
    }

    public void draw() {
        if (startMenu) {
            drawStartMenu();
        } else if (displayHelp) {
            drawHelp();
        } else if (displayScores) {
            drawScores();
        } else if (!gameOver && !gameWon) {
            background(gameBackground);
            if(!bossFightTime){
            drawWaveInfo();
            }
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
            if (boss != null) {
                boss.display(screenWidth);
                drawBossInfo();
            }
        } else if (gameOver) {
            score.saveScore();
            drawGameOverMessage();
        } else if (gameWon) {
            score.saveScore();
            drawGameWonMessage();
        }
    }

    public boolean collides(Chicken chicken, Missile missile) {
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

    public boolean collidesWithSpaceship(Chicken chicken, Spaceship spaceship) {
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

    public boolean collidesWithSpaceship(Boss boss, Spaceship spaceship) {
        if (boss == null || spaceship == null) {
            return false;
        }

        float leftBoss = boss.getX();
        float rightBoss = boss.getX() + boss.getWidth();
        float topBoss = boss.getY();
        float bottomBoss = boss.getY() + boss.getHeight();

        float leftSpaceship = spaceship.getxPos();
        float rightSpaceship = spaceship.getxPos() + spaceship.getSpaceshipWidth();
        float topSpaceship = spaceship.getyPos();
        float bottomSpaceship = spaceship.getyPos() + spaceship.getSpaceshipHeight();

        return leftSpaceship < rightBoss && rightSpaceship > leftBoss && topSpaceship < bottomBoss && bottomSpaceship > topBoss;
    }

    public boolean collides(Boss boss, Missile missile) {
        float leftBoss = boss.getX();
        float rightBoss = boss.getX() + boss.getWidth();
        float topBoss = boss.getY();
        float bottomBoss = boss.getY() + boss.getHeight();

        float leftMissile = missile.getX();
        float rightMissile = missile.getX() + missile.getMissileWidth();
        float topMissile = missile.getY();
        float bottomMissile = missile.getY() + missile.getMissileHeight();

        return leftMissile < rightBoss && rightMissile > leftBoss && topMissile < bottomBoss && bottomMissile > topBoss;
    }

    public void checkCollisions() {
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
        if (killedChickens == chickensPerWave) {
            nextWave();
        }

        if (boss != null) {
            Iterator<Missile> missileIterator = missiles.iterator();
            while (missileIterator.hasNext()) {
                Missile currentMissile = missileIterator.next();

                // Check for collision between the boss and missile
                if (collides(boss, currentMissile)) {
                    // Decrease the boss's resistance
                    boss.decreaseResistance();
                    chickenHitSound.play();
                    missileIterator.remove();

                    // Increment the score by 10
                    score.setCurrentScore(score.getCurrentScore() + 10);

                    // If the boss's resistance reaches 0, remove the boss and set the gameWon flag
                    if (boss.getResistance() == 0) {
                        boss = null;
                        gameWon = true;
                    }
                    break;
                }
            }
        }
    }

    public void nextWave() {
        if (currentWave < totalWaves) {
            currentWave++;
            updateChickenImage(); // Update the chicken image
            chicken.createChickens();
            killedChickens = 0; // Reset the killedChickens count
        } else {
            // Create the boss
            if (boss == null) {
                boss = new Boss(screenWidth / 2.0f - 60, 50, 30); // Set the initial position and resistance
                bossFightTime = true;
            }
        }
    }

    public void drawBossInfo(){
        textFont(pressStartFont,12);
        fill(255,0,0);
        text("Boss Life :" + boss.getResistance(),320,20);
        
    }

    public void checkGameOver() {
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

        // Check if the boss touched the spaceship
        if (!gameOver && collidesWithSpaceship(boss, spaceship)) {
            gameOver = true;
        }
    }

    public void updateChickenImage() {
        int index = currentWave - 1;
        if (index < chickenImages.length) {
            Chicken.chickenImage = loadImage(chickenImages[index]);
        }
    }

    public void drawWaveInfo() {
        pApplet.textSize(14);
        pApplet.fill(255, 0, 0); // White text color
        pApplet.text("Wave:" + currentWave, 350, 20); // Display the wave number at the top-left corner
    }

    public void drawStartMenu() {
        // Draw background image stretched to fit the screen
        image(startMenuBackground, 0, 0, screenWidth, screenHeight);

        // Title
        textFont(pressStartFont, 24);
        fill(251, 255, 0); // Change the title color
        textAlign(CENTER);
        text("Chicken Invaders", screenWidth / 2 + 10, screenHeight / 3 - 150);

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

        //Scores button background
        noStroke();
        fill(255, 0, 0, 180);// Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight / 2 + 240, 200, 60, 10);

        // Check if the mouse is over the start button
        boolean mouseOverstartButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 - 30 && mouseY < screenHeight / 2 + 30;
        boolean mouseOverExitButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 50 && mouseY < screenHeight / 2 + 110;
        boolean mouseOverHelpButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 130 && mouseY < screenHeight / 2 + 190;
        boolean mouseOverScoresButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 210 && mouseY < screenHeight / 2 + 270;

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

        // Play notification sound if the mouse is over the help button for the first time
        if (mouseOverHelpButton && !mouseWasOverHelpButton) {
            notificationSound.play();
        }
        mouseWasOverHelpButton = mouseOverHelpButton;

        // Play notification sound if the mouse is over the scores button for the first time
        if (mouseOverScoresButton && !mouseWasOverScoresButton) {
            notificationSound.play();
        }
        mouseWasOverScoresButton = mouseOverScoresButton;

        // Start button text
        textFont(pressStartFont, mouseOverstartButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Start Game", screenWidth / 2, screenHeight / 2 + 6);

        // Exit button text
        textFont(pressStartFont, mouseOverExitButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Exit Game", screenWidth / 2, screenHeight / 2 + 86);

        // Help button text
        textFont(pressStartFont, mouseOverHelpButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text(" Help ", screenWidth / 2, screenHeight / 2 + 166);

        // Scores button text
        textFont(pressStartFont, mouseOverScoresButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Scores", screenWidth / 2, screenHeight / 2 + 246);
    }

    public void drawHelp() {
        background(0);
        textSize(24);
        fill(255);
        textAlign(CENTER, CENTER);
        text("Help", screenWidth / 2, screenHeight / 4 - 130);
        textSize(8);
        text("Click anywhere on the screen or press the space key to shoot.\n\nMove the spaceship left and right using the mouse.\n\nEliminate all the chickens to win the game.\n\nComplete all waves to win the game.", screenWidth / 2, screenHeight / 2 - 50);

        // Back button background
        noStroke();
        fill(255, 0, 0, 180); // Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight - 100, 200, 60, 10);

        // Check if the mouse is over the back button
        boolean mouseOverBackButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70;

        // Play notification sound if the mouse is over the back button for the first time
        if (mouseOverBackButton && !mouseWasOverBackButton) {
            notificationSound.play();
        }
        mouseWasOverBackButton = mouseOverBackButton;

        // Back button text
        textFont(pressStartFont, mouseOverBackButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Back", screenWidth / 2, screenHeight - 100);

    }

    public void drawScores() {
        score.drawAllScores();

        // Back button background
        noStroke();
        fill(255, 0, 0, 180); // Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight - 100, 200, 60, 10);

        // Check if the mouse is over the back button
        boolean mouseOverBackButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70;

        // Play notification sound if the mouse is over the back button for the first time
        if (mouseOverBackButton && !mouseWasOverBackButton) {
            notificationSound.play();
        }
        mouseWasOverBackButton = mouseOverBackButton;

        // Back button text
        textFont(pressStartFont, mouseOverBackButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Back", screenWidth / 2, screenHeight - 100);
    }

    public void drawGameOverMessage() {

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

        if (score.getCurrentScore() >= currentHighScore) {
            textSize(12);
            text("You Got HighScore: " + score.getCurrentScore(), screenWidth / 2, screenHeight / 2 + 100);
        }

        // Back button background
        noStroke();
        fill(255, 0, 0, 180); // Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight - 100, 200, 60, 10);

        // Check if the mouse is over the back button
        boolean mouseOverExitButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70;

        // Play notification sound if the mouse is over the back button for the first time
        if (mouseOverExitButton && !mouseWasOverFinalExitButton) {
            notificationSound.play();
        }
        mouseWasOverFinalExitButton = mouseOverExitButton;

        // Back button text
        textFont(pressStartFont, mouseOverExitButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Exit", screenWidth / 2, screenHeight - 100);

        noLoop();
    }

    public void drawGameWonMessage() {
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

        if (score.getCurrentScore() >= currentHighScore) {
            textSize(12);
            text("You Got HighScore: " + score.getCurrentScore(), screenWidth / 2, screenHeight / 2 + 100);
        }

        // Back button background
        noStroke();
        fill(255, 0, 0, 180); // Change the button background color to a lighter red
        rectMode(CENTER);
        rect(screenWidth / 2, screenHeight - 100, 200, 60, 10);

        // Check if the mouse is over the back button
        boolean mouseOverExitButton = mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70;

        // Play notification sound if the mouse is over the back button for the first time
        if (mouseOverExitButton && !mouseWasOverFinalExitButton) {
            notificationSound.play();
        }
        mouseWasOverFinalExitButton = mouseOverExitButton;

        // Back button text
        textFont(pressStartFont, mouseOverExitButton ? 20 : 18);
        fill(255, 255, 255); // Change the button text color to white
        text("Exit", screenWidth / 2, screenHeight - 100);

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
                displayHelp = true;
                startMenu = false;
            }

            // Check if the mouse was clicked inside the help button
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight / 2 + 210 && mouseY < screenHeight / 2 + 270) {
                displayScores = true;
                startMenu = false;
            }
        } else if (displayHelp) {
            // Check if the mouse was clicked inside the back button
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70) {
                displayHelp = false;
                startMenu = true;
            }
        } else if (displayScores) {
            // Check if the mouse was clicked inside the back button
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70) {
                displayScores = false;
                startMenu = true;
            }
        } else if (gameWon || gameOver){
            if (mouseX > screenWidth / 2 - 100 && mouseX < screenWidth / 2 + 100 && mouseY > screenHeight - 130 && mouseY < screenHeight - 70) {
                exit();
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
    }

    public void settings() {
        size(screenWidth, screenHeight);
    }

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }
}