package mazes.generators.base;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import mazes.entities.Maze;
import mazes.entities.LineSegment;

import java.awt.*;

/**
 * A class responsible for returning an initial maze with walls present between every single room.
 *
 * A 'MazeCarver' class is then responsible for figuring out which walls to remove to form
 * a valid maze.
 */
public abstract class BaseMazeGenerator {
    /**
     * Generates an initial 'maze'.
     */
    public abstract Maze generateBaseMaze(Rectangle boundingBox);

    protected IList<LineSegment> polygonToLineSegment(Polygon polygon) {
        IList<LineSegment> output = new DoubleLinkedList<>();

        int[] xs = polygon.xpoints;
        int[] ys = polygon.ypoints;
        for (int i = 0; i < polygon.npoints; i++) {
            Point p1 = new Point(xs[i], ys[i]);
            Point p2 = new Point(
                    xs[(i + 1) % polygon.npoints],
                    ys[(i + 1) % polygon.npoints]);

            output.add(new LineSegment(p1, p2));
        }

        return output;
    }

    protected int round(double num) {
        return (int) Math.round(num);
    }

    protected int[] roundArray(double[] array, int length) {
        int[] output = new int[length];
        for (int i = 0; i < length; i++) {
            output[i] = round(array[i]);
        }
        return output;
    }

}
