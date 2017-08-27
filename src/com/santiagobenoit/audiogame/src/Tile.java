package com.santiagobenoit.audiogame.src;

import java.awt.Point;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * The Tile object.
 * @author Santiago Benoit
 */
public abstract class Tile implements Serializable {
    
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public final void setPlayerPassable(boolean passable) {
        playerPassable = passable;
    }
    
    public final void setMarblePassable(boolean passable) {
        marblePassable = passable;
    }
    
    public final void replaceWith(Class<? extends Tile> tileClass) {
        GUI.maze.removeTile(this);
        try {
            GUI.maze.addTile(tileClass.getConstructor(int.class, int.class).newInstance(x, y));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            
        }
    }
    
    public abstract void initialize();
    
    public abstract void playerTouch();
    
    public abstract void marbleTouch(float gain, float pan);
    
    public abstract void interact();
    
    public final boolean isPlayerPassable() {
        return playerPassable;
    }
    
    public final boolean isMarblePassable() {
        return marblePassable;
    }
    
    public final int getX() {
        return x;
    }
    
    public final int getY() {
        return y;
    }
    
    public final Point getPosition() {
        return new Point(x, y);
    }
    
    private boolean playerPassable, marblePassable;
    private final int x, y;
}
