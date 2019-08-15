package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.interfaces.IList;
import misc.Sorter;

public class Experiment1 {
    public static final int NUM_TRIALS = 5;
    public static final long MAX_LIST_SIZE = 200000;
    public static final long STEP = 1000;
    public static final int K = 500;

    public static void main(String[] args) {
        IList<Long> listSizes = AnalysisUtils.makeDoubleLinkedList(0L, MAX_LIST_SIZE, STEP);

        System.out.println("Starting test");
        IList<Long> testResults = AnalysisUtils.runTrials(
                listSizes, Experiment1::test, NUM_TRIALS);

        System.out.println("Saving experiment results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("ListSizes", listSizes);
        writer.addColumn("TestResult", testResults);
        writer.writeToFile("experimentdata/experiment1.csv");

        System.out.println("All done!");
    }

    public static long test(long listSize) {
        IList<Long> list = AnalysisUtils.makeDoubleLinkedList(0, listSize, 1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Sorter.topKSort(K, list);
        }
        return System.currentTimeMillis() - start;
    }
}
