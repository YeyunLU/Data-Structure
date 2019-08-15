package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.interfaces.IList;
import misc.Sorter;

public class Experiment2 {
    public static final int NUM_TRIALS = 5;
    public static final long MAX_LIST_SIZE = 200000;
    public static final long STEP = 1000;

    public static void main(String[] args) {
        IList<Long> valuesOfK = AnalysisUtils.makeDoubleLinkedList(0L, MAX_LIST_SIZE, STEP);

        System.out.println("Starting test");
        IList<Long> testResults = AnalysisUtils.runTrials(
                valuesOfK, Experiment2::test, NUM_TRIALS);

        System.out.println("Saving experiment results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("K", valuesOfK);
        writer.addColumn("TestResult", testResults);
        writer.writeToFile("experimentdata/experiment2.csv");

        System.out.println("All done!");
    }

    public static long test(long rawK) {
        int k = (int) rawK;
        IList<Long> list = AnalysisUtils.makeDoubleLinkedList(0, MAX_LIST_SIZE, 1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Sorter.topKSort(k, list);
        }
        return System.currentTimeMillis() - start;
    }
}
