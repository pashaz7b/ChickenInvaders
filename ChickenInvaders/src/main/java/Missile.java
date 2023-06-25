import java.util.*;

public class Missile implements MissileInterface{
    private float x;
    private float y;
    private float speed;
    private int missileColor;
    private float missileWidth;
    private float missileHeight;

    public Missile(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = -5;
        this.missileColor = Main.pApplet.color(255, 255, 255);
        this.missileWidth = 5;
        this.missileHeight = 10;
    }

    public void update() {
        y += speed;
    }

    public void drawMissile() {
        Main.pApplet.fill(missileColor);
        Main.pApplet.rect(x, y, missileWidth, missileHeight);
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