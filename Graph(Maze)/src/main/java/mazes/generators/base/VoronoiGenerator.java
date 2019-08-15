package mazes.generators.base;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.entities.LineSegment;

import java.awt.*;
import java.util.Random;

/**
 * Generates a maze where the rooms are randomly distributed across the
 * bounding box. Walls are placed equidistant between two adjacent rooms.
 */
public class VoronoiGenerator extends BaseMazeGenerator {
    private int numRooms;
    private int sampleRate;
    private int minimumWallLength;
    private int minimumSpaceFromBoundingBox;

    /**
     * @param numRooms  the number of rooms the graph should contain
     * @param sampleRate  how many times the algorithm should try and find a good position for a new node
     * @param minimumWallLength  if a generated wall is less than this length, we don't connect the two adjacent rooms
     * @param minimumSpaceFromBoundingBox  how far away the center of the room should be from the bounding box
     */
    public VoronoiGenerator(int numRooms, int sampleRate, int minimumWallLength, int minimumSpaceFromBoundingBox) {
        this.numRooms = numRooms;
        this.sampleRate = sampleRate;
        this.minimumWallLength = minimumWallLength;
        this.minimumSpaceFromBoundingBox = minimumSpaceFromBoundingBox;
    }

    public Maze generateBaseMaze(Rectangle boundingBox) {
        Rectangle centerBoundingBox = new Rectangle(
                boundingBox.x + this.minimumSpaceFromBoundingBox,
                boundingBox.y + this.minimumSpaceFromBoundingBox,
                boundingBox.width - 2 * this.minimumSpaceFromBoundingBox,
                boundingBox.height - 2 * this.minimumSpaceFromBoundingBox);

        // Generate initial room locations
        OpenList sites = this.generateCells(centerBoundingBox);
        PolygonSimple boundingPolygon = this.boundingBoxToPolygon(boundingBox);

        // Run Voronoi
        PowerDiagram diagram = new PowerDiagram();
        diagram.setSites(sites);
        diagram.setClipPoly(boundingPolygon);
        diagram.computeDiagram();

        // Convert sites into rooms
        ISet<Room> rooms = new ChainedHashSet<>();
        IDictionary<Point, Room> pointsToSite = new ChainedHashDictionary<>();
        for (Site site : sites) {
            Room room = this.siteToRoom(site);
            pointsToSite.put(room.getCenter(), room);
            rooms.add(room);
        }

        // Convert sites into walls; build edges
        ISet<Wall> walls = new ChainedHashSet<>();
        ISet<Wall> untouchableWalls = new ChainedHashSet<>();
        IDictionary<LineSegment, Point> edgeToPoints = new ChainedHashDictionary<>();
        for (Room room : rooms) {
            Point vertex = room.getCenter();
            for (LineSegment seg : this.polygonToLineSegment(room.getPolygon())) {
                if (!edgeToPoints.containsKey(seg)) {
                    edgeToPoints.put(seg, vertex);
                } else {
                    Point otherVertex = edgeToPoints.get(seg);
                    Wall wall = new Wall(pointsToSite.get(vertex), pointsToSite.get(otherVertex), seg);
                    if (seg.length() > this.minimumWallLength) {
                        walls.add(wall);
                    } else {
                        untouchableWalls.add(wall);
                    }
                }
            }
        }

        return new Maze(rooms, walls, untouchableWalls);
    }

    private PolygonSimple boundingBoxToPolygon(Rectangle boundingBox) {
        PolygonSimple boundingPolygon = new PolygonSimple();
        boundingPolygon.add(boundingBox.getMinX(), boundingBox.getMinY());
        boundingPolygon.add(boundingBox.getMinX(), boundingBox.getMaxY());
        boundingPolygon.add(boundingBox.getMaxX(), boundingBox.getMaxY());
        boundingPolygon.add(boundingBox.getMaxX(), boundingBox.getMinY());
        return boundingPolygon;
    }

    private Room siteToRoom(Site site) {
        PolygonSimple oldPolygon = site.getPolygon();

        Point center = new Point(round(site.getX()), round(site.getY()));
        Polygon polygon = new Polygon(
                roundArray(oldPolygon.getXPoints(), oldPolygon.length),
                roundArray(oldPolygon.getYPoints(), oldPolygon.length),
                oldPolygon.length);

        return new Room(center, polygon);
    }

    private OpenList generateCells(Rectangle boundingBox) {
        OpenList output = new OpenList();
        IList<Point> points = new DoubleLinkedList<>();

        Random rand = new Random();
        for (int i = 0; i < this.numRooms; i++) {
            double bestDistance = 0;
            Point bestPoint = null;

            for (int j = 0; j < this.sampleRate; j++) {
                int randX = this.nextInt(rand, boundingBox.x, boundingBox.x + boundingBox.width);
                int randY = this.nextInt(rand, boundingBox.y, boundingBox.y + boundingBox.height);
                Point candidate = new Point(randX, randY);

                double distance = this.getClosest(candidate, points);
                if (distance > bestDistance) {
                    bestDistance = distance;
                    bestPoint = candidate;
                }
            }

            assert bestPoint != null;
            output.add(new Site(bestPoint.x, bestPoint.y));
            points.add(bestPoint);
        }

        return output;
    }

    private double getClosest(Point target, IList<Point> coords) {
        double bestDistance = Double.MAX_VALUE;
        for (Point point : coords) {
            double distance = point.distance(target);
            if (distance < bestDistance) {
                bestDistance = distance;
            }
        }

        return bestDistance;
    }

    private int nextInt(Random rand, int min, int max) {
        return rand.nextInt(max - min) + min;
    }
}
