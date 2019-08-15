package mazes.entities;

import datastructures.interfaces.IEdge;

/**
 * Represents a wall between two rooms.
 *
 * A 'Wall' object also serves as an 'edge' of sorts between rooms.
 *
 * A Wall is also comparable according to the distance between the two
 * given rooms.
 */
public class Wall implements IEdge<Room>, Comparable<Wall> {
    private Room room1;
    private Room room2;
    private LineSegment dividingLine;
    private double distance;

    /**
     * Constructs a wall between the two given rooms.
     *
     * The 'distance' is the length (in pixels) between the center of the two
     * rooms; the dividingLine is the actual line to draw on the screen.
     */
    public Wall(Room room1, Room room2, LineSegment dividingLine, double distance) {
        this.room1 = room1;
        this.room2 = room2;
        this.dividingLine = dividingLine;
        this.distance = distance;
    }

    /**
     * Constructs a wall between the two given rooms, and automatically constructs
     * the distance.
     */
    public Wall(Room room1, Room room2, LineSegment dividingLine) {
        this(room1, room2, dividingLine, room1.getCenter().distance(room2.getCenter()));
    }

    /**
     * Returns a pointer to the first room.
     */
    public Room getRoom1() {
        return this.room1;
    }

    /**
     * Returns a pointer to the other room.
     */
    public Room getRoom2() {
        return this.room2;
    }

    /**
     * Returns the physical line dividing the two rooms.
     */
    public LineSegment getDividingLine() {
        return this.dividingLine;
    }

    /**
     * Returns the distance between two rooms.
     */
    public double getDistance() {
        return this.distance;
    }


    /**
     * Equivalent to getRoom1()
     */
    public Room getVertex1() {
        return this.getRoom1();
    }

    /**
     * Equivalent to getRoom2()
     */
    public Room getVertex2() {
        return this.getRoom2();
    }

    /**
     * Equivalent to getDistance()
     */
    public double getWeight() {
        return this.getDistance();
    }

    /**
     * Changes the distance to some custom value.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Recomputes the distance to be the actual distance between the
     * center of the two rooms.
     */
    public void resetDistanceToOriginal() {
        this.distance = this.getVertex1().getCenter().distance(this.getVertex2().getCenter());
    }

    /**
     * We compare walls by reference. We deliberately DO NOT include the distance in equals or hashCode,
     * since KruskalMazeCarver involves randomizing the weights of the walls before finding an MST,
     * which could cause inconsistent behavior for walls that were put into maps or sets.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result;
        result = room1.hashCode();
        result = 31 * result + room2.hashCode();
        result = 31 * result + dividingLine.hashCode();
        return result;
    }

    @Override
    public int compareTo(Wall other) {
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return String.format("Wall(room1=%s, room2=%s, distance=%s", this.room1, this.room2, this.distance);
    }
}
