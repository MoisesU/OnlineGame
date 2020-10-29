/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

/**
 *
 * @author MoisésUlises
 */
public class GameEntity {
    
    /*public static final int UP = 38;
    public static final int DOWN = 40;
    public static final int RIGTH = 39;
    public static final int LEFT = 37;
    public static final int HEIGHT = 720;
    public static final int WIDTH = 960;*/
    
    public static final int GAME_WIDTH = 960;
    public static final int GAME_HEIGHT = 720;
    
    private final String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private int step = 1;
    

    public GameEntity(String name) {
        this.name = name;
        this.x = 0;
        this.y = 0;
    }

    public GameEntity(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
    
    public void move(int direction){
        switch (direction) {
            case 37:
                if(this.x >= 0)
                    this.x -= step;
                break;
            case 38:
                if(this.y >= 0)
                    this.y -= step;
                break;
            case 39:
                if(this.x <= this.GAME_WIDTH - this.width)
                    this.x += step;
                break;
            case 40:
                if(this.y <= this.GAME_HEIGHT - this.height)
                    this.y += step;
                break;
        }
    }
    
}
