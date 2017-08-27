package com.santiagobenoit.audiogame.src;

/**
 * The key tile.
 * @author Santiago Benoit
 */
public class TileKey extends Tile {
    
    public TileKey(int x, int y) {
        super(x, y);
        setPlayerPassable(true);
        setMarblePassable(true);
    }
    
    @Override
    public void initialize() {
        
    }
    
    @Override
    public void playerTouch() {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/key_get.wav", 0.0f, 0.0f);
        GUI.keyCount++;
        replaceWith(TileFloor.class);
    }
    
    @Override
    public void marbleTouch(float gain, float pan) {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_roll.wav", gain, pan);
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/key_hit.wav", gain, pan);
    }
    
    @Override
    public void interact() {
        
    }
}
