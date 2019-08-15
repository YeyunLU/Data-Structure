package analysis.utils;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.AVLDictionary;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import org.openjdk.jol.info.GraphLayout;

import java.util.function.Function;

/**
 * This class contains a variety of utility methods useful when running
 * experiments. You do NOT need to understand how each method works.
 * Instead, focus on reading the method header comments so you understand
 * what each method DOES.
 */
public class AnalysisUtils {
    /**
     * Constructs a list of longs starting with 'size', going to 'end', in 'step' increments.
     */
    public static IList<Long> makeList(long start, long end, long step) {
        IList<Long> out = new DoubleLinkedList<>();
        for (long i = start; i < end; i += step) {
            out.add(i);
        }
        return out;
    }

    /**
     * Constructs a dictionary containing keys from 0 to 'size' (with dummy values)
     */
    public static IDictionary<Long, Long> makeArrayDictionary(long size) {
        IDictionary<Long, Long> dictionary = new ArrayDictionary<>();
        for (long i = 0; i < size; i += 1) {
            dictionary.put(i, -1L);
        }
        return dictionary;
    }

    /**
     * Constructs a dictionary containing keys from 0 to 'size' (with dummy values)
     */
    public static IDictionary<Long, Long> makeAVLDictionary(long size) {
        IDictionary<Long, Long> dictionary = new AVLDictionary<>();
        for (long i = 0; i < size; i += 1) {
            dictionary.put(i, -1L);
        }
        return dictionary;
    }

    /**
     * Handles running a single experiment.
     *
     * The 'testerFunction' object is expected to be a function that:
     *
     * a. Accepts some long as input (the 'n'). The exact meaning of 'n' is entirely up to the tester function.
     * b. Runs some arbitrary test.
     * c. Returns some number indicating the final result. This number could be the time it took to run
     *    some code, the amount of memory used...
     *
     * The 'inputs' list must contains all of the different values of 'n' we want to try testing.
     *
     * This method will take each item inside 'input', pass them one by one into the testerFunction,
     * and return a list containing the corresponding outputs.
     *
     * NOTE: this helper method is appropriate mainly when trying to test something that's *deterministic*:
     * that's guaranteed to return the same result each time.
     *
     * If you want to test something that has some degree of randomness (e.g. testing how long it takes
     * for some code to run), you should try using the 'runTrials' helper method below.
     */
    public static IList<Long> runSingleTrial(IList<Long> inputs, Function<Long, Long> testerFunction) {
        IList<Long> trial = new DoubleLinkedList<>();
        for (long input : inputs) {
            trial.add(testerFunction.apply(input));
        }
        return trial;
    }

    /**
     * This method re-runs 'runSingleTrial' the given `numTrial' number of times and returns
     * a list containing the (rounded) average of each trial.
     */
    public static IList<Long> runTrials(IList<Long> inputs, Function<Long, Long> testerFunction, int numTrials) {
        // Run tester function once but discard result to warm up cache
        // (This helps us obtain more accurate results when testing timing code)
        System.out.println("    Running preliminary trial to warm up caches (will discard result)");
        runSingleTrial(inputs, testerFunction);

        IList<IList<Long>> trials = new DoubleLinkedList<>();
        for (int i = 0; i < numTrials; i++) {
            System.out.println("    Starting trial " + i);
            trials.add(runSingleTrial(inputs, testerFunction));
        }

        System.out.println("    Averaging results");
        return average(trials);
    }

    /**
     * Accepts a bunch of lists, where each inner list represents a column in a table.
     *
     * Returns a list containing the (rounded) average of each row.
     */
    public static IList<Long> average(IList<IList<Long>> entries) {
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one entry before we can take an average");
        }
        int numEntries = entries.size();

        IList<Long> result = new DoubleLinkedList<>();
        RowIterator<Long> rowIter = new RowIterator<>(entries);
        while (rowIter.hasNext()) {
            IList<Long> trial = rowIter.next();
            long curr = 0L;
            for (long val : trial) {
                curr += val;
            }
            result.add(Math.round((double) curr / numEntries));
        }

        return result;
    }

    /**
     * Returns the approximate amount of memory by the entire object, in bytes.
     */
    public static long getApproximateMemoryUsed(Object obj) {
        return GraphLayout.parseInstance(obj).totalSize();
    }
}
