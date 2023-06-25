import java.util.*;

public class Chicken {

   public static ArrayList<Chicken> chickens = new ArrayList<>();

    private final int width = 40;
    private final int height = 30;
    private float x;
    private float y;
    private float speedX;
    private int colorR;
    private int colorG;
    private int colorB;

   public Chicken(float x, float y, int colorR, int colorG, int colorB) {
        this.x = x;
        this.y = y;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.speedX = 1;
    }

   public void createChickens() {
        int rows = 5;
        int cols = 8;
        float startX = 30;
        float startY = 30;
        float xOffset = 50;
        float yOffset = 50;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int randomR = (int) Main.pApplet.random(256);
                int randomG = (int) Main.pApplet.random(256);
                int randomB = (int) Main.pApplet.random(256);
                float x = startX + j * xOffset;
                float y = startY + i * yOffset;
                chickens.add(new Chicken(x, y, randomR, randomG, randomB));
            }
        }
    }

   public void update() {
        x += speedX;

        // Change direction if the chicken hits the screen edges
        if (x <= 0 || x + width >= Main.pApplet.width) {
            speedX = -speedX;
            y += height;
        }
    }

    public void display() {
        for (Chicken chicken : chickens) {
            chicken.update();
            Main.pApplet.fill(chicken.getColorR(), chicken.getColorG(), chicken.getColorB());
            Main.pApplet.rect(chicken.getX(), chicken.getY(), chicken.getWidth(), chicken.getHeight());
        }
    }

//***********************************************************************************************

    public static ArrayList<Chicken> getChickens() {
        return chickens;
    }

    public static void setChickens(ArrayList<Chicken> chickens) {
        Chicken.chickens = chickens;
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

    public int getColorR() {
        return colorR;
    }

    public void setColorR(int colorR) {
        this.colorR = colorR;
    }

    public int getColorG() {
        return colorG;
    }

    public void setColorG(int colorG) {
        this.colorG = colorG;
    }

    public int getColorB() {
        return colorB;
    }

    public void setColorB(int colorB) {
        this.colorB = colorB;
    }
}