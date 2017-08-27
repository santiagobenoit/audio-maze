package com.santiagobenoit.audiogame.src;

/**
 * The finish tile.
 * @author Santiago Benoit
 */
public class TileFinish extends Tile {
    
    public TileFinish(int x, int y) {
        super(x, y);
        setPlayerPassable(true);
        setMarblePassable(true);
    }
    
    @Override
    public void initialize() {
        GUI.maze.getTiles().stream().filter((tile) -> ((tile instanceof TileFinish && tile != this))).forEach((tile) -> {
            tile.replaceWith(TileFloor.class);
        });
    }
    
    @Override
    public void playerTouch() {
        try {
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/applause.wav", 0.0f, 0.0f);
            GUI.victory = true;
            Thread.sleep(6000);
            GUI.resetGame();
        } catch (InterruptedException e) {
            
        }
    }
    
    @Override
    public void marbleTouch(float gain, float pan) {
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/ding.wav", gain, pan);
        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/marble_roll.wav", gain, pan);
    }
    
    @Override
    public void interact() {
        
    }
}
