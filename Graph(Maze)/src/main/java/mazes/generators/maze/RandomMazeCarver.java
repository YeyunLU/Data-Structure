package mazes.generators.maze;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Wall;

import java.util.Random;

/**
 * Randomly removes edges with a certain percent probability
 */
public class RandomMazeCarver implements MazeCarver {
    private double probabilityOfKeepingEdge;

    public RandomMazeCarver(double probabilityOfKeepingEdge) {
        this.probabilityOfKeepingEdge = probabilityOfKeepingEdge;
    }

    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        Random rand = new Random();

        ISet<Wall> toRemove = new ChainedHashSet<>();
        for (Wall wall : maze.getWalls()) {
            if (rand.nextDouble() >= this.probabilityOfKeepingEdge) {
                toRemove.add(wall);
            }
        }
        return toRemove;
    }
}
