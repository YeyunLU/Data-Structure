package mazes.gui;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.generators.base.BaseMazeGenerator;
import mazes.generators.maze.MazeCarver;
import misc.exceptions.NoPathExistsException;
import datastructures.concrete.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class OptionsPanel extends JPanel {
    private ImageDrawer drawer;
    private IDictionary<String, BaseMazeGenerator> baseGenerators;
    private IDictionary<String, MazeCarver> baseCarvers;

    private String baseGeneratorName;
    private String baseCarverName;

    private Maze originalMaze;
    private Maze currentMaze;

    private Room start;
    private Room end;

    public OptionsPanel(ImageDrawer drawer,
                        IDictionary<String, BaseMazeGenerator> baseGenerators,
                        IDictionary<String, MazeCarver> baseCarvers) {
        this.drawer = drawer;
        this.baseGenerators = baseGenerators;
        this.baseCarvers = baseCarvers;

        this.buildLayout();

        this.generateMaze(null);
    }

    private void buildLayout() {
        this.setLayout(new GridBagLayout());

        JLabel label1 = new JLabel();
        label1.setText("Base maze shape");
        this.add(label1, 0, 0, GridBagConstraints.WEST);

        JLabel label2 = new JLabel();
        label2.setText("Maze generator");
        this.add(label2, 0, 1, GridBagConstraints.WEST);

        JComboBox<String> baseShapeComboBox = new JComboBox<>(this.getKeysAsArray(this.baseGenerators));
        baseShapeComboBox.addItemListener(this::onBaseShapeChange);
        this.baseGeneratorName = (String) baseShapeComboBox.getSelectedItem();
        this.add(baseShapeComboBox, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0);

        JComboBox<String> mazeGeneratorComboBox = new JComboBox<>(this.getKeysAsArray(this.baseCarvers));
        mazeGeneratorComboBox.addItemListener(this::onBaseCarverChange);
        this.baseCarverName = (String) mazeGeneratorComboBox.getSelectedItem();
        this.add(mazeGeneratorComboBox, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0);

        this.add(new JPanel(), 2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 0.5);
        this.add(new JPanel(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 0.5);

        JButton generateMazeButton = new JButton();
        generateMazeButton.setText("Generate new maze");
        generateMazeButton.addActionListener(this::generateMaze);
        this.add(generateMazeButton, 3, 0, GridBagConstraints.EAST);

        JButton findPathButton = new JButton();
        findPathButton.setText("Find shortest path");
        findPathButton.addActionListener(this::drawShortestPath);
        this.add(findPathButton, 3, 1, GridBagConstraints.EAST);
    }

    private String[] getKeysAsArray(IDictionary<String, ?> dict) {
        String[] out = new String[dict.size()];
        int i = 0;
        for (KVPair<String, ?> pair : dict) {
            out[i] = pair.getKey();
            i += 1;
        }
        return out;
    }

    private void onBaseShapeChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            this.baseGeneratorName = (String) event.getItem();
        }
    }

    private void onBaseCarverChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            this.baseCarverName = (String) event.getItem();
        }
    }

    private void generateMaze(ActionEvent event) {
        this.drawer.clear();

        BaseMazeGenerator generator = this.baseGenerators.get(this.baseGeneratorName);
        MazeCarver carver = this.baseCarvers.get(this.baseCarverName);

        Rectangle bound = drawer.getBoundingBox();
        this.originalMaze = generator.generateBaseMaze(bound);
        this.currentMaze = carver.generateMaze(this.originalMaze);

        this.start = this.currentMaze.getRoom(new Point(bound.x + 5, bound.y + 5));
        this.end = this.currentMaze.getRoom(new Point(bound.x + bound.width - 5, bound.y + bound.height - 5));

        this.drawer.drawMaze(this.currentMaze);
        drawer.drawPoint(start.getCenter());
        drawer.drawPoint(end.getCenter());
    }

    private void drawShortestPath(ActionEvent event) {
        Graph<Room, Wall> graph = new Graph<>(
                this.originalMaze.getRooms(),
                this.subtract(this.originalMaze.getWalls(), this.currentMaze.getWalls()));
        try {
            IList<Wall> path = graph.findShortestPathBetween(this.start, this.end);
            this.drawer.drawPath(path);
        } catch (NoPathExistsException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "This maze seems to be unsolvable: no path could be found between the start and the end!");
        }
    }

    private <T> ISet<T> subtract(ISet<T> bigger, ISet<T> smaller) {
        ISet<T> output = new ChainedHashSet<>();
        for (T item : bigger) {
            if (!smaller.contains(item)) {
                output.add(item);
            }
        }
        return output;
    }

    private void add(JComponent component, int x, int y, int anchor, int fill, double weightX) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightX;
        this.add(component, gbc);
    }

    private void add(JComponent component, int x, int y, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        gbc.weightx = 0.0;
        this.add(component, gbc);
    }
}
