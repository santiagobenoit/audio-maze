package com.santiagobenoit.audiogame.src;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Maze object.
 * @author Santiago Benoit
 */
public class Maze implements Serializable {
    
    public Maze() {
        width = 0;
        height = 0;
        tiles = new CopyOnWriteArrayList<>();
    }
    
    public Maze(Maze other) {
        width = other.width;
        height = other.height;
        tiles = (CopyOnWriteArrayList<Tile>) other.tiles.clone();
    }
    
    public void setWidth(int width) {
        if (this.width < width) {
            for (int x = this.width; x < width; x++) {
                for (int y = 0; y < this.height; y++) {
                    addTile(new TileFloor(x, y));
                }
            }
        } else {
            for (int x = width; x < this.width; x++) {
                for (int y = 0; y < height; y++) {
                    removeTile(x, y);
                }
            }
        }
        this.width = width;
    }
    
    public void setHeight(int height) {
        if (this.height < height) {
            for (int x = 0; x < this.width; x++) {
                for (int y = this.height; y < height; y++) {
                    addTile(new TileFloor(x, y));
                }
            }
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = height; y < this.height; y++) {
                    removeTile(x, y);
                }
            }
        }
        this.height = height;
    }
    
    public void setTiles(CopyOnWriteArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public void addTile(Tile tile) {
        removeTile(tile.getX(), tile.getY());
        tiles.add(tile);
    }
    
    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }
    
    public void removeTile(int x, int y) {
        tiles.remove(getTile(x, y));
    }

    public void initializeTiles() {
        tiles.stream().forEach((tile) -> {
            tile.initialize();
        });
    }
    
    public void serialize(String path) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(path); ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(this);
        }
    }
    
    public static Maze deserialize(String path) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(path); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            return (Maze) objectIn.readObject();
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Tile getStart() {
        for (Tile tile : tiles) {
            if (tile instanceof TileStart) {
                return tile;
            }
        }
        return null;
    }
    
    public Tile getFinish() {
        for (Tile tile : tiles) {
            if (tile instanceof TileFinish) {
                return tile;
            }
        }
        return null;
    }

    public CopyOnWriteArrayList<Tile> getTiles() {
        return tiles;
    }
    
    public boolean hasTile(Class<? extends Tile> tileClass) {
        return tiles.stream().anyMatch((tile) -> (tileClass.isInstance(tile)));
    }

    public Tile getTile(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.getPosition().equals(new Point(x, y))) {
                return tile;
            }
        }
        return null;
    }
    
    private int width, height;
    private CopyOnWriteArrayList<Tile> tiles;
}
