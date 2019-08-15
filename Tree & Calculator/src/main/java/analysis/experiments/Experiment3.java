package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.interfaces.IList;

public class Experiment3 {
    public static final int NUM_TRIALS = 5;
    public static final int NUM_TIMES_TO_REPEAT = 1000;
    public static final long MAX_LIST_SIZE = 20000;
    public static final long STEP = 100;

    public static void main(String[] args) {
        IList<Long> indices = AnalysisUtils.makeDoubleLinkedList(0L, MAX_LIST_SIZE, STEP);

        System.out.println("Starting experiment 3");
        IList<Long> testResults = AnalysisUtils.runTrials(indices, Experiment3::test, NUM_TRIALS);

        System.out.println("Saving results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("InputIndices", indices);
        writer.addColumn("TestResults", testResults);
        writer.writeToFile("experimentdata/experiment3.csv");

        System.out.println("All done!");
    }

    /**
     * We will call test on all indices in the indices IList constructed above.  
     * So indices will have values 0 to MAX_LIST_SIZE, in increments of 100.
     * Your prediction should estimate what the overall trends of the plot will look like,
     * and address what indices (when passed to the get method) you would expect to be faster or slower.
     *
     * @param index The index of the DoubleLinkedList that we want to get. This will the the x-axis of your plot.
     * @return the amount of time it takes to return the element of a DoubleLinkedList at
     *          the index specified in the parameter, in milliseconds.
     */
    public static long test(long index) {
        // We don't include the cost of constructing the list when running this test
        IList<Long> list = AnalysisUtils.makeDoubleLinkedList(0L, MAX_LIST_SIZE, 1L);

        long start = System.currentTimeMillis();

        // We try getting the same thing multiple times mainly because a single get, by itself,
        // is too fast to reliably measure. By testing the same amount of .get()s for each
        // index, we come up with a kind of "average" runtime of .get() for that index.
        long temp = 0L;
        for (int i = 0; i < NUM_TIMES_TO_REPEAT; i++) {
            temp += list.get((int) index);
        }

        // Returns time elapsed. This will end up being the real-world amount of time
        // it took to get that index of the DoubleLinkedList
        return System.currentTimeMillis() - start;
    }
}
