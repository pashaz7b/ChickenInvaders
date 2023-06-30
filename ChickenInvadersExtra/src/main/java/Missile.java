import java.util.*;
import processing.core.*;
import processing.sound.SoundFile;

public class Missile implements MissileInterface {
    private float x;
    private float y;
    private float speed;
    private int missileColor;
    private float missileWidth;
    private float missileHeight;
    private PImage missileImage;
    private SoundFile missileSound;
    private boolean soundPlayed;

    public Missile(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = -5;
        this.missileColor = Main.pApplet.color(255, 255, 255);
        this.missileWidth = 15;
        this.missileHeight = 30;

        // Load missile image and sound
        this.missileImage = Main.pApplet.loadImage("missile.png");
        this.missileSound = new SoundFile(Main.pApplet, "missile_sound.wav");
        this.soundPlayed = false; // Initialize soundPlayed as false
    }

    public void update() {
        y += speed;
        // Play missile sound if it has just been fired and the sound has not been played yet
        if (y + speed <= Main.pApplet.height && !soundPlayed) {
            missileSound.play();
            soundPlayed = true; // Set soundPlayed to true after playing the sound
        }
    }

    public void drawMissile() {
        Main.pApplet.fill(missileColor);
        // Draw missile using the image
        Main.pApplet.image(missileImage, x, y, missileWidth, missileHeight);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getMissileColor() {
        return missileColor;
    }

    public void setMissileColor(int missileColor) {
        this.missileColor = missileColor;
    }

    public float getMissileWidth() {
        return missileWidth;
    }

    public void setMissileWidth(float missileWidth) {
        this.missileWidth = missileWidth;
    }

    public float getMissileHeight() {
        return missileHeight;
    }

    public void setMissileHeight(float missileHeight) {
        this.missileHeight = missileHeight;
    }
}