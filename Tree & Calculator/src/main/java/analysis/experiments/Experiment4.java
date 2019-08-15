package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

public class Experiment4 {

    public static final long MAX_LIST_SIZE = 20000;
    public static final long STEP = 100;

    public static void main(String[] args) {
        IList<Long> listSizes = AnalysisUtils.makeDoubleLinkedList(0L, MAX_LIST_SIZE, STEP);

        System.out.println("Starting experiment 4, test 1");
        IList<Long> test1Results = AnalysisUtils.runSingleTrial(listSizes, Experiment4::test1);

        System.out.println("Starting experiment 4, test 2");
        IList<Long> test2Results = AnalysisUtils.runSingleTrial(listSizes, Experiment4::test2);

        System.out.println("Saving results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("InputListSize", listSizes);
        writer.addColumn("ResultsTest1", test1Results);
        writer.addColumn("ResultsTest2", test2Results);
        writer.writeToFile("experimentdata/experiment4.csv");

        System.out.println("All done!");
    }

    /**
     * We will call test1 and test2 with varying sizes. Your prediction should estimate the differences
     * between the memory usage of DoubleLinkedLists and ArrayDictionaries, and how those differences might
     * change as size increases.
     *
     * @param size The number of elements in the data structure. This will the the x-axis of your plot.
     * @return the approximate memory usage of the data structure
     */

    public static long test1(long size) {
        IList<Long> list = AnalysisUtils.makeDoubleLinkedList(0L, size, 1L);
        // Note: we're measuring memory usage, which is deterministic
        // (assuming we constructed our DoubleLinkedList correctly) - that is,
        // two DLLs with the same number of elements will always use the same
        // amount of memory. So, there's no need to conduct trials, as they
        // would end up beinge exactly the same.
        //
        // AnalysisUtils has a method that will tell us how much memory is being
        // so we can compare the DLL to a similarly sized ArrayDictionary.
        return AnalysisUtils.getApproximateMemoryUsed(list);
    }

    public static long test2(long size) {
        IDictionary<Long, Long> dictionary = AnalysisUtils.makeArrayDictionary(size);
        // ArrayDictionaries also use a deterministic amount of memory, so no trials.
        //
        // AnalysisUtils has a method that will tell us how much memory is being
        // so we can compare the ArrayDictionary to a similarly sized DLL.
        return AnalysisUtils.getApproximateMemoryUsed(dictionary);
    }
}
