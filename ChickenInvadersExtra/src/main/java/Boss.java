import processing.core.PImage;

public class Boss  {

    public static PImage bossImage;
    private final int width = 120; // Boss width (double the chicken width)
    private final int height = 96; // Boss height (double the chicken height)
    private float x;
    private float y;
    private float speedX;
    private int resistance;

    public Boss(float x, float y, int resistance) {
        this.x = x;
        this.y = y;
        this.resistance = resistance;
        this.speedX = 0.5f; // Boss speed (half the chicken speed)
    }

    public void update(int screenWidth) {
        x += speedX;

        // Change direction if the boss hits the screen edges
        if (x <= 0 || x + width >= screenWidth) {
            speedX = -speedX;
            y += height;
        }
    }

    public void display(int screenWidth) {
        // Update boss position and draw the boss image
        update(screenWidth);
        Main.pApplet.image(bossImage, x, y, width, height);
    }

    public void decreaseResistance() {
        resistance--;
    }

    public static PImage getBossImage() {
        return bossImage;
    }

    public static void setBossImage(PImage bossImage) {
        Boss.bossImage = bossImage;
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

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }
}