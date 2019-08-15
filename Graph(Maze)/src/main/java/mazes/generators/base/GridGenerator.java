package mazes.generators.base;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.entities.LineSegment;

import java.awt.*;

/**
 * Generates a grid-like maze, where every room is a rectangle connected to
 * up to four adjacent rooms.
 */
public class GridGenerator extends BaseMazeGenerator {
    private int numRows;
    private int numColumns;

    /**
     * Accepts the number of rows and columns the grid ought to have.
     */
    public GridGenerator(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public Maze generateBaseMaze(Rectangle boundingBox) {
        Room[][] grid = this.buildRooms(boundingBox);
        return new Maze(
                this.extractRooms(grid),
                this.extractWalls(grid),
                new ChainedHashSet<>());
    }

    private Room[][] buildRooms(Rectangle boundingBox) {
        Room[][] grid = new Room[this.numColumns][this.numRows];

        double yDelta = 1.0 * boundingBox.height / this.numRows;
        double xDelta = 1.0 * boundingBox.width / this.numColumns;

        for (int i = 0; i < numRows; i++) {
            int yMin = round(i * yDelta + boundingBox.y);
            int yMax = round((i + 1) * yDelta + boundingBox.y);
            for (int j = 0; j < numColumns; j++) {
                int xMin = round(j * xDelta + boundingBox.x);
                int xMax = round((j + 1) * xDelta + boundingBox.x);

                Point center = new Point(
                        round((xMin + xMax) / 2.0),
                        round((yMin + yMax) / 2.0));

                Polygon polygon = new Polygon(
                        new int[]{xMin, xMax, xMax, xMin},
                        new int[]{yMin, yMin, yMax, yMax},
                        4);

                grid[j][i] = new Room(center, polygon);
            }
        }

        return grid;
    }

    private ISet<Room> extractRooms(Room[][] grid) {
        ISet<Room> rooms = new ChainedHashSet<>();
        for (int x = 0; x < this.numColumns; x++) {
            for (int y = 0; y < this.numRows; y++) {
                rooms.add(grid[x][y]);
            }
        }
        return rooms;
    }

    private ISet<Wall> extractWalls(Room[][] grid) {
        ISet<Wall> walls = new ChainedHashSet<>();

        for (int x = 0; x < this.numColumns; x++) {
            for (int y = 0; y < this.numRows; y++) {
                Room room = grid[x][y];
                IList<LineSegment> segments = this.polygonToLineSegment(room.getPolygon());

                if (x > 0) {
                    walls.add(new Wall(room, grid[x - 1][y], segments.get(3)));
                }
                if (y > 0) {
                    walls.add(new Wall(room, grid[x][y - 1], segments.get(0)));
                }
            }
        }

        return walls;
    }
}
