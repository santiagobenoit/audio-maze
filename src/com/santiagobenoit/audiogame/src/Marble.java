package com.santiagobenoit.audiogame.src;

import java.awt.Point;

/**
 * The Marble object.
 * @author Santiago Benoit
 */
public class Marble {
    
    public Marble(Point position, int angle) {
        this.position = position;
        this.angle = angle;
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
    
    public void setPosition(int x, int y) {
        position = new Point(x, y);
    }
    
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    public Point getPosition() {
        return position;
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
    
    public int getAngle() {
        return angle;
    }
    
    public double distanceToPlayer() {
        return Math.sqrt(Math.pow(GUI.player.getPosition().x - getPosition().x, 2) + Math.pow(GUI.player.getPosition().y - getPosition().y, 2));
    }
    
    public boolean isObstructed() {
        Tile tile = GUI.maze.getTile(getPosition().x, getPosition().y);
        return tile == null || !tile.isMarblePassable();
    }
    
    private Point position;
    private int angle;
}
