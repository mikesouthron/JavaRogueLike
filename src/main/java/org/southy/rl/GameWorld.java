package org.southy.rl;

import org.southy.rl.exceptions.Impossible;
import org.southy.rl.gen.Procgen;

import java.io.Serializable;

public class GameWorld implements Serializable {

    Engine engine;
    int mapWidth;
    int mapHeight;
    int maxRooms;
    int roomMaxSize;
    int roomMinSize;
    int maxMonstersPerRoom;
    int maxItemsPerRoom;
    int currentFloor = 0;

    public GameWorld(Engine engine, int mapWidth, int mapHeight, int maxRooms, int roomMaxSize, int roomMinSize, int maxMonstersPerRoom, int maxItemsPerRoom) {
        this.engine = engine;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.maxRooms = maxRooms;
        this.roomMaxSize = roomMaxSize;
        this.roomMinSize = roomMinSize;
        this.maxMonstersPerRoom = maxMonstersPerRoom;
        this.maxItemsPerRoom = maxItemsPerRoom;
    }

    public void generateFloor() throws Impossible {
        currentFloor++;
        engine.gameMap = Procgen.generateDungeon(engine, maxRooms, roomMinSize, roomMaxSize, mapWidth, mapHeight, maxMonstersPerRoom, maxItemsPerRoom);
    }


    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setMaxRooms(int maxRooms) {
        this.maxRooms = maxRooms;
    }

    public void setRoomMaxSize(int roomMaxSize) {
        this.roomMaxSize = roomMaxSize;
    }

    public void setRoomMinSize(int roomMinSize) {
        this.roomMinSize = roomMinSize;
    }

    public void setMaxMonstersPerRoom(int maxMonstersPerRoom) {
        this.maxMonstersPerRoom = maxMonstersPerRoom;
    }

    public void setMaxItemsPerRoom(int maxItemsPerRoom) {
        this.maxItemsPerRoom = maxItemsPerRoom;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}
