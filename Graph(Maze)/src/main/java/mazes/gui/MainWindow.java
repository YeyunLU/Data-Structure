package mazes.gui;

import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import mazes.generators.base.BaseMazeGenerator;
import mazes.generators.base.GridGenerator;
import mazes.generators.base.VoronoiGenerator;
import mazes.generators.maze.KruskalMazeCarver;
import mazes.generators.maze.MazeCarver;
import mazes.generators.maze.RandomMazeCarver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The main entry point of the user interface.
 */
public class MainWindow extends JFrame {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    /**
     * Responsible for configuring and launching the GUI.
     */
    public static void launch() {
        // This dictionary contains objects that are responsible for creating the initial
        // "shape" of the maze. We've implemented two options for you: one that creates a
        // grid of rectangle, and another that creates a more "cellular" shape.
        //
        // Feel free to tweak any of the constants below -- for example, if you want the
        // grid generator to have a different number of rows and columns.
        //
        // (Note: we are using ArrayDictionaries so we can control the order in which we present
        // the options)
        IDictionary<String, BaseMazeGenerator> baseGenerators = new ArrayDictionary<>();
        baseGenerators.put(
                "Grid",
                new GridGenerator(40, 40));
        baseGenerators.put(
                "Voronoi",
                new VoronoiGenerator(800, 10, 10, 5));

        // This dictionary contains objects that are responsible for taking a
        // maze and removing or "carving out" walls to produce an actually-usable maze.
        //
        // We've implemented one for you -- a RandomMazeCarver that deletes edges at random.
        // You will need to implement a second carver that uses your Kruskal's algorithm
        // implementation (see spec for details).
        IDictionary<String, MazeCarver> baseCarvers = new ArrayDictionary<>();
        baseCarvers.put(
                "Do not delete any edges",
                new RandomMazeCarver(1.0));
        baseCarvers.put(
                "Delete random edges (keep 30% of edges)",
                new RandomMazeCarver(0.3));
        baseCarvers.put(
                "Delete random edges (keep 50% of edges)",
                new RandomMazeCarver(0.5));
        baseCarvers.put(
                "Run (randomized) Kruskal",
                new KruskalMazeCarver());

        // This actually launches the window itself and starts the program.
        EventQueue.invokeLater(() -> {
            MainWindow window = new MainWindow(
                    "MazeGenerator",
                    DEFAULT_WIDTH,
                    DEFAULT_HEIGHT,
                    baseGenerators,
                    baseCarvers);
            window.construct();
        });
    }

    private String title;
    private int width;
    private int height;
    private ImageDrawer drawer;

    private IDictionary<String, BaseMazeGenerator> baseGenerators;
    private IDictionary<String, MazeCarver> baseCarvers;

    public MainWindow(String title, int width, int height,
                IDictionary<String, BaseMazeGenerator> baseGenerators,
                IDictionary<String, MazeCarver> baseCarvers) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.baseGenerators = baseGenerators;
        this.baseCarvers = baseCarvers;
    }

    public void construct() {
        this.setupMainWindow();

        ImagePanel imagePanel = this.makeDrawingPane();
        OptionsPanel optionsPanel = new OptionsPanel(this.drawer, this.baseGenerators, this.baseCarvers);

        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set up pane
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(imagePanel);
        pane.add(optionsPanel);
        this.add(pane);

        // Finish setting up geometry
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setupMainWindow() {
        this.setTitle(this.title);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Ignore
        }
    }

    public ImagePanel makeDrawingPane() {
        BufferedImage image = new BufferedImage(
                this.width,
                this.height, BufferedImage.TYPE_BYTE_INDEXED);

        // Setup initial background color
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width + 1, this.height + 1);

        this.drawer = new ImageDrawer(image, 5);

        ImagePanel panel = new ImagePanel(image);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
}
