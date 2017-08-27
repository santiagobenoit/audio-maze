package com.santiagobenoit.audiogame.src;

/**
 * The wall tile.
 * @author Santiago Benoit
 */
public class TileWall extends Tile {
    
    public TileWall(int x, int y) {
        super(x, y);
        setPlayerPassable(false);
        setMarblePassable(false);
    }
    
    @Override
    public void initialize() {
        
    }
    
    @Override
    public void playerTouch() {
        if (GUI.hardcoreEnabled) {
            GUI.player.kill();
        }
    }
    
    @Override
    public void marbleTouch(float gain, float pan) {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_hit.wav", gain, pan);
    }
    
    @Override
    public void interact() {
        
    }
}
