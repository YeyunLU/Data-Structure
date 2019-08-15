package mazes.gui;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import mazes.entities.Maze;
import mazes.entities.Wall;
import mazes.entities.LineSegment;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.ImageObserver;

/**
 * This class contains some useful utility methods for drawing to the plotting window.
 *
 * You do not need to understand how this class works: you should just stick with
 * reading the method header comments.
 */
public class ImageDrawer implements ImageObserver {
    private static final int UNKNOWN_DIMENSION = -1;

    private Graphics2D graphics;
    private int width = UNKNOWN_DIMENSION;
    private int height = UNKNOWN_DIMENSION;

    private int padding;

    /**
     * Creates a new ImageDrawer object based on the given panel.
     */
    public ImageDrawer(Image image, int padding) {
        this.graphics = (Graphics2D) image.getGraphics();
        this.width = image.getWidth(this);
        this.height = image.getHeight(this);
        this.padding = padding;

        this.graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * Creates a new ImageDrawer object based on the given panel.
     */
    public ImageDrawer(Graphics2D graphics, int width, int height) {
        this.graphics = graphics;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns a Graphics object that can draw on the panel.
     * For more details on how to use the Graphics object, try reviewing
     * the material from CSE 142.
     *
     * This project does not use CSE 142's 'DrawingPanel' object, but we
     * *do* use the same 'Graphics' object.
     */
    public Graphics2D getGraphics() {
        return this.graphics;
    }

    /**
     * Returns the width of the window, in pixels.
     *
     * Note: the width may change if the user resizes the window, so be sure to
     * recheck what this value is every time you need to use it again.
     *
     * @throws IllegalStateException   if the image width is somehow unknown
     */
    public int getWidth() {
        if (this.width == UNKNOWN_DIMENSION) {
            throw new IllegalStateException("Unexpected fatal error: Image width unknown");
        }
        return this.width;
    }

    /**
     * Returns the height of the window, in pixels.
     *
     * Note: the height may change if the user resizes the window, so be sure to
     * recheck what this value is every time you need to use it again.
     *
     * @throws IllegalStateException   if the image width is somehow unknown
     */
    public int getHeight() {
        if (this.height == UNKNOWN_DIMENSION) {
            throw new IllegalStateException("Unexpected fatal error: Image height unknown");
        }
        return this.height;
    }

    /**
     * Returns the region of the screen in which we actually plan on drawing the maze.
     * The bounding box is slightly smaller than the actual size of the panel,
     * minus the 'padding' argument given to the constructor.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                padding,
                padding,
                this.width - 2*padding,
                this.height - 2*padding);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int newWidth, int newHeight) {
        boolean widthReady = (infoflags & ImageObserver.WIDTH) != 0;
        boolean heightReady = (infoflags & ImageObserver.HEIGHT) != 0;

        if (widthReady && heightReady) {
            this.width = newWidth;
            this.height = newHeight;
            return false;
        } else {
            return true;
        }
    }

    public void drawMaze(Maze maze) {
        this.drawMaze(maze, false);
    }

    public void drawMaze(Maze maze, boolean showEdges) {
        Color originalColor = this.graphics.getColor();
        Stroke originalStroke = this.graphics.getStroke();

        this.graphics.setColor(Color.BLACK);
        this.graphics.setStroke(new BasicStroke(4));

        this.graphics.draw(this.getBoundingBox());
        for (Wall wall : maze.getWalls()) {
            this.drawLineSegment(wall.getDividingLine());
            if (showEdges) {
                this.drawPath(wall);
            }
        }
        for (Wall wall : maze.getUntouchableWalls()) {
            this.drawLineSegment(wall.getDividingLine());
        }

        this.graphics.setColor(originalColor);
        this.graphics.setStroke(originalStroke);
    }

    public void drawLineSegment(LineSegment segment) {
        this.drawLineSegment(segment.start, segment.end);
    }

    public void drawLineSegment(Point a, Point b) {
        this.graphics.drawLine(a.x, a.y, b.x, b.y);
    }

    public void drawPath(Wall wall) {
        IList<Wall> walls = new DoubleLinkedList<>();
        walls.add(wall);
        this.drawPath(walls);
    }

    public void drawPath(IList<Wall> wallsBypassed) {
        Color originalColor = this.graphics.getColor();
        Stroke originalStroke = this.graphics.getStroke();

        this.graphics.setColor(Color.RED);
        this.graphics.setStroke(new BasicStroke(2));

        for (Wall wall : wallsBypassed) {
            LineSegment segment = wall.getDividingLine();

            Point center1 = wall.getRoom1().getCenter();
            Point center2 = wall.getRoom2().getCenter();
            Point midpoint = segment.midpoint();

            this.drawPoint(center1);
            this.drawPoint(center2);

            Path2D.Double path = new Path2D.Double();
            path.moveTo(center1.x, center1.y);
            path.curveTo(midpoint.x, midpoint.y, midpoint.x, midpoint.y, center2.x, center2.y);
            this.graphics.draw(path);
        }

        this.graphics.setColor(originalColor);
        this.graphics.setStroke(originalStroke);
    }

    public void drawPoint(Point point) {
        Color originalColor = this.graphics.getColor();
        this.graphics.setColor(Color.RED);
        int radius = 4;
        this.graphics.fillOval(point.x - radius, point.y - radius, radius * 2, radius * 2);
        this.graphics.setColor(originalColor);
    }

    public void clear() {
        Color originalColor = this.graphics.getColor();
        this.graphics.setColor(Color.WHITE);
        this.graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        this.graphics.setColor(originalColor);
    }
}
