package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

public class Experiment2 {
    public static final long MAX_LIST_SIZE = 20000;
    public static final long STEP = 100;

    public static void main(String[] args) {
        IList<Long> listSizes = AnalysisUtils.makeList(0L, MAX_LIST_SIZE, STEP);

        System.out.println("Starting experiment 2, test 1");
        IList<Long> test1Results = AnalysisUtils.runSingleTrial(listSizes, Experiment2::test1);

        System.out.println("Starting experiment 2, test 2");
        IList<Long> test2Results = AnalysisUtils.runSingleTrial(listSizes, Experiment2::test2);

        System.out.println("Starting experiment 2, test 3");
        IList<Long> test3Results = AnalysisUtils.runSingleTrial(listSizes, Experiment2::test3);
        System.out.println("Saving results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("InputListSize", listSizes);
        writer.addColumn("Test1Result", test1Results);
        writer.addColumn("Test2Result", test2Results);
        writer.addColumn("Test3Result", test3Results);
        writer.writeToFile("experimentdata/experiment2.csv");

        System.out.println("All done!");
    }

    /**
     * We will call the test methods with varying sizes. Your prediction should estimate the differences between the
     * memory usage of DoubleLinkedLists, ArrayDictionaries, and AVLDictionaries, and how those differences might
     * change as size increases.
     *
     * @param size The number of elements in the data structure. This will the the x-axis of your plot.
     * @return the approximate memory usage of the data structure
     */

    public static long test1(long size) {
        IList<Long> list = AnalysisUtils.makeList(0L, size, 1L);
        // Note: we're measuring memory usage, which is deterministic
        // (assuming we constructed our DoubleLinkedList correctly) - that is,
        // two DLLs with the same number of elements will always use the same
        // amount of memory. So, there's no need to conduct trials, as they
        // would end up being exactly the same.
        //
        // AnalysisUtils has a method that will tell us how much memory is being
        // so we can compare the DLL to a similarly sized ArrayDictionary.
        return AnalysisUtils.getApproximateMemoryUsed(list);
    }

    public static long test2(long size) {
        IDictionary<Long, Long> dictionary = AnalysisUtils.makeArrayDictionary(size);
        // ArrayDictionary uses a deterministic amount of memory, so we don't need
        // multiple trials.
        //
        // AnalysisUtils has a method that will tell us how much memory is being
        // so we can compare the ArrayDictionary to a similarly sized DLL.
        return AnalysisUtils.getApproximateMemoryUsed(dictionary);
    }

    public static long test3(long size) {
        IDictionary<Long, Long> dictionary = AnalysisUtils.makeAVLDictionary(size);
        return AnalysisUtils.getApproximateMemoryUsed(dictionary);
    }
}
