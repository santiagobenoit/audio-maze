package com.santiagobenoit.audiogame.src;

/**
 * The water tile.
 * @author Santiago Benoit
 */
public class TileWater extends Tile {
    
    public TileWater(int x, int y) {
        super(x, y);
        setPlayerPassable(true);
        setMarblePassable(false);
    }
    
    @Override
    public void initialize() {
        
    }
    
    @Override
    public void playerTouch() {
        try {
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/footstep_water.wav", 0.0f, -0.2f);
            Thread.sleep(250);
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/footstep_water.wav", -5.0f, 0.2f);
        } catch (InterruptedException e) {
            
        }
    }
    
    @Override
    public void marbleTouch(float gain, float pan) {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_water.wav", gain, pan);
    }
    
    @Override
    public void interact() {
        
    }
}
