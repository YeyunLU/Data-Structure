package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

public class Experiment1 {
    // Note: please do not change these constants (or the constants in any of the other experiments)
    // while working on your writeup

    public static final int NUM_TRIALS = 5;
    public static final long MAX_DICTIONARY_SIZE = 20000;
    public static final long STEP = 100;

    public static void main(String[] args) {
        IList<Long> dictionarySizes = AnalysisUtils.makeDoubleLinkedList(0L, MAX_DICTIONARY_SIZE, STEP);

        // Note: You may be wondering what doing 'Experiment1::test1' do?
        // Basically, what's happening here is that we're telling Java to:
        //
        // a. Take the Experiment1.test1 static method and construct an object representing that method
        // b. Pass that "function object" to the runTrials method
        //
        // Then, what 'runTrials' can do is use that wrapper object to directly call our test1 method
        // whenever it wants.
        //
        // This may seem a little weird at first: we're basically treating a method as a "value"
        // so we can pass it around. On the other hand, why not? We can pass all kinds of things
        // ranging from ints and Strings and objects into methods -- why can't we also pass methods
        // themselves?
        //
        // For more information on how AnalysisUtils.runTrials uses the function object, see its
        // method header comment.  
        //
        // You can do (ctrl or command) + click on AnalysisUtils.runTrials to jump to its source code
        // and see its method header comment.

        System.out.println("Starting experiment 1, test 1");
        IList<Long> test1Results = AnalysisUtils.runTrials(dictionarySizes, Experiment1::test1, NUM_TRIALS);

        System.out.println("Starting experiment 1, test 2");
        IList<Long> test2Results = AnalysisUtils.runTrials(dictionarySizes, Experiment1::test2, NUM_TRIALS);

        System.out.println("Saving experiment 1 results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("InputDictionarySize", dictionarySizes);
        writer.addColumn("Test1Results", test1Results);
        writer.addColumn("Test2Results", test2Results);
        writer.writeToFile("experimentdata/experiment1.csv");

        System.out.println("All done!");
    }

    /**
     * We will repeatedly call test1 and test2  with different dictionarySizes, and report that result. Your prediction
     * should estimate how test1 will end up comparing to test2 as the size of the dictionary changes.
     * 
     * Both test1 and test2 have the same parameter and return meaning:
     * @param dictionarySize the size of the arrayDictionary. This will the the x-axis of your plot.
     * @return the amount of time this test took to run, in milliseconds.
     */

    public static long test1(long dictionarySize) {
        // We don't include the cost of constructing the dictionary when running this test
        IDictionary<Long, Long> dictionary = AnalysisUtils.makeArrayDictionary(dictionarySize);

        long start = System.currentTimeMillis();
        for (long i = 0L; i < dictionarySize; i++) {
            //Because our loop starts at 0, this means we're removing the current first element
            //of the ArrayDictionary every time
            dictionary.remove(i);
        }

        // Returns time elapsed. This will end up being the real-world amount of time
        // it took to remove all elements of the dictionary
        return System.currentTimeMillis() - start;
    }

    public static long test2(long dictionarySize) {
        IDictionary<Long, Long> dictionary = AnalysisUtils.makeArrayDictionary(dictionarySize);

        long start = System.currentTimeMillis();
        for (long i = dictionarySize - 1; i >= 0; i--) {
            // Because this test goes from size - 1 down to 0, we end up removing the last element every time.
            dictionary.remove(i);
        }
        // Returns time elapsed. This will end up being the real-world amount of time
        // it took to remove all elements of the dictionary
        return System.currentTimeMillis() - start;
    }
}
