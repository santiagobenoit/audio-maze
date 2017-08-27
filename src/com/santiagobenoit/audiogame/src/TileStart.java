package com.santiagobenoit.audiogame.src;

/**
 * The start tile.
 * @author Santiago Benoit
 */
public class TileStart extends Tile {
    
    public TileStart(int x, int y) {
        super(x, y);
        setPlayerPassable(true);
        setMarblePassable(true);
    }
    
    @Override
    public void initialize() {
        GUI.maze.getTiles().stream().filter((tile) -> (tile instanceof TileStart && tile != this)).forEach((tile) -> {
            tile.replaceWith(TileFloor.class);
        });
    }
    
    @Override
    public void playerTouch() {
        try {
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/footstep.wav", 0.0f, -0.2f);
            Thread.sleep(250);
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/footstep.wav", -5.0f, 0.2f);
        } catch (InterruptedException e) {
            
        }
    }
    
    @Override
    public void marbleTouch(float gain, float pan) {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_roll.wav", gain, pan);
    }
    
    @Override
    public void interact() {
        
    }
}
