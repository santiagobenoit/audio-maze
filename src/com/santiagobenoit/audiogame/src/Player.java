package com.santiagobenoit.audiogame.src;

import java.awt.Point;

/**
 * The Player object.
 * @author Santiago Benoit
 */
public class Player {
    
    public Player() {
        position = new Point(-1, -1);
        angle = 0;
    }
    
    public void rollMarble() {
        Thread thread = new Thread(() -> {
            Marble marble = new Marble(position, angle);
            float gain = 0.0f;
            while (!marble.isObstructed() || marble.getPosition().equals(getPosition())) {
                marble.moveForwards();
                gain = (float) marble.distanceToPlayer() * -5.0f;
                if (GUI.maze.getTile(marble.getPosition().x, marble.getPosition().y) != null) {
                    GUI.maze.getTile(marble.getPosition().x, marble.getPosition().y).marbleTouch(gain, 0.0f);
                } else {
                    Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_hit.wav", gain, 0.0f);
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    
                }
                if (marble.distanceToPlayer() > 15) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    public void setPosition(int x, int y) {
        position.setLocation(new Point(x, y));
    }
    
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    public void rotateLeft() {
        if (angle == 0) {
            angle = 270;
        } else {
            angle -= 90;
        }
    }
    
    public void rotateRight() {
        if (angle == 360) {
            angle = 90;
        } else {
            angle += 90;
        }
    }
    
    public void moveForwards() {
        switch (angle) {
            case 0:
            case 360:
                setPosition(position.x, position.y - 1);
                break;
            case 270:
                setPosition(position.x - 1, position.y);
                break;
            case 180:
                setPosition(position.x, position.y + 1);
                break;
            case 90:
                setPosition(position.x + 1, position. y);
                break;
            default:
                break;
        }
    }
    
    public void moveBackwards() {
        switch (angle) {
            case 0:
            case 360:
                setPosition(position.x, position.y + 1);
                break;
            case 270:
                setPosition(position.x + 1, position.y);
                break;
            case 180:
                setPosition(position.x, position.y - 1);
                break;
            case 90:
                setPosition(position.x - 1, position. y);
                break;
            default:
                break;
        }
    }
    
    public void kill() {
        try {
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/scream.wav", 0.0f, 0.0f);
            GUI.gameover = true;
            Thread.sleep(1000);
            GUI.resetGame();
        } catch (InterruptedException e) {
            
        }
    }
    
    public Point getPosition() {
        return position;
    }
    
    public int getAngle() {
        return angle;
    }
    
    public Point getFront() {
        switch (angle) {
            case 0:
            case 360:
                return new Point(position.x, position.y - 1);
            case 270:
                return new Point(position.x - 1, position.y);
            case 180:
                return new Point(position.x, position.y + 1);
            case 90:
                return new Point(position.x + 1, position. y);
            default:
                return null;
        }
    }
    
    public Point getBack() {
        switch (angle) {
            case 0:
            case 360:
                return new Point(position.x, position.y + 1);
            case 270:
                return new Point(position.x + 1, position.y);
            case 180:
                return new Point(position.x, position.y - 1);
            case 90:
                return new Point(position.x - 1, position. y);
            default:
                return null;
        }
    }
    
    public boolean frontObstructed() {
        Tile front = GUI.maze.getTile(getFront().x, getFront().y);
        return front == null || !front.isPlayerPassable();
    }
    
    public boolean backObstructed() {
        Tile back = GUI.maze.getTile(getBack().x, getBack().y);
        return back == null || !back.isPlayerPassable();
    }
     
    private final Point position;
    private int angle;
}
