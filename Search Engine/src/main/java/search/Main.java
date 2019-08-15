package search;

public class Main {
    // The dataset to index and search.
    // MUST be the name of one of the folders in the 'data' folder.
    public static final String DATA_FOLDER_NAME = "wikipedia-with-spam";

    // The name of your search engine (feel free to change this).
    public static final String SITE_TITLE = "Noodle";

    // The port to serve your web server on.
    // You can ignore this constant. If you're familiar with web development
    // and know what ports are, feel free to change this if it's convenient.
    public static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Indexing web pages");
        SearchEngine engine = new SearchEngine(DATA_FOLDER_NAME);

        System.out.println("Setting up web server");
        Webapp app = new Webapp(engine, SITE_TITLE, PORT);

        System.out.println(String.format(
                "Ready! Open 'http://localhost:%d' in your web browser to access the search engine.",
                PORT));
        app.launch();
    }
}
