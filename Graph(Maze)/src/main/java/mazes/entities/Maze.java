package mazes.entities;

import datastructures.interfaces.ISet;

import java.awt.*;

/**
 * Represents a maze.
 */
public class Maze {
    private ISet<Room> rooms;
    private ISet<Wall> walls;
    private ISet<Wall> untouchableWalls;

    public Maze(ISet<Room> rooms, ISet<Wall> walls, ISet<Wall> untouchableWalls) {
        this.rooms = rooms;
        this.walls = walls;
        this.untouchableWalls = untouchableWalls;
    }

    /**
     * Returns the set of all walls in the maze.
     */
    public ISet<Room> getRooms() {
        return this.rooms;
    }

    /**
     * Returns the set of all removable walls between rooms.
     */
    public ISet<Wall> getWalls() {
        return this.walls;
    }

    /**
     * Returns the set of all unremovable walls between rooms.
     *
     * In other words, when we're generating a maze, these walls
     * must ALWAYS be present in the final maze, for one reason or another.
     */
    public ISet<Wall> getUntouchableWalls() {
        return this.untouchableWalls;
    }

    /**
     * Returns the room containing the given point.
     *
     * Returns 'null' if there does not exist a room under that point
     * for some reason.
     */
    public Room getRoom(Point point) {
        for (Room room : this.rooms) {
            if (room.contains(point)) {
                return room;
            }
        }
        return null;
    }
}
