package analysis.utils;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * A utility class that will write a CSV file.
 */
public class CsvWriter {
    private IList<String> columnNames;
    private IList<IList<Long>> columns;

    public CsvWriter() {
        this.columnNames = new DoubleLinkedList<>();
        this.columns = new DoubleLinkedList<>();
    }

    /**
     * Adds a column of data with the given name.
     *
     * Precondition: the column must be the same length as all other columns.
     */
    public void addColumn(String name, IList<Long> column) {
        this.columnNames.add(name);
        this.columns.add(column);
    }

    /**
     * Writes all of the saved columns to the given file location.
     */
    public void writeToFile(String filename) {
        if (this.columnNames.isEmpty()) {
            throw new IllegalStateException("Error: trying to write a CSV file containing no columns");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Print header
            writer.println(this.join(this.columnNames, ","));

            RowIterator<Long> rowIter = new RowIterator<>(this.columns);
            while (rowIter.hasNext()) {
                writer.println(this.join(rowIter.next(), ","));
            }
        } catch (IOException ex) {
            // Having to include a "throws IOException" in our method headers everywhere
            // is annoying, so we rethrow the exception as a RuntimeException instead.
            throw new RuntimeException(ex);
        }
    }

    private <T extends Object> String join(IList<T> values, String divider) {
        StringBuilder builder = new StringBuilder();
        Iterator<T> iter = values.iterator();

        builder.append(iter.next().toString());

        while (iter.hasNext()) {
            builder.append(",");
            builder.append(iter.next().toString());
        }

        return builder.toString();
    }
}
