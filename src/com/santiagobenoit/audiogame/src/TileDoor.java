package com.santiagobenoit.audiogame.src;

/**
 * The door tile.
 * @author Santiago Benoit
 */
public class TileDoor extends Tile {
    
    public TileDoor(int x, int y) {
        super(x, y);
        setPlayerPassable(false);
        setMarblePassable(true);
    }
    
    @Override
    public void initialize() {
        
    }
    
    @Override
    public void playerTouch() {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/door_locked.wav", 0.0f, 0.0f);
    }
    
    @Override
    public void marbleTouch(float gain, float pan) {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_roll.wav", gain, pan);
    }
    
    @Override
    public void interact() {
        if (GUI.keyCount > 0) {
            try {
                GUI.keyCount--;
                Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/door_unlock.wav", 0.0f, 0.0f);
                Thread.sleep(500);
                Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/door_open.wav", 0.0f, 0.0f);
                replaceWith(TileFloor.class);
            } catch (InterruptedException e) {
                
            }
        }
    }
}
