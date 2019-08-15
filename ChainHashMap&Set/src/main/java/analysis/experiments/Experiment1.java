package analysis.experiments;

import analysis.utils.AnalysisUtils;
import analysis.utils.CsvWriter;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NotYetImplementedException;

import java.util.Random;

public class Experiment1 {
    public static final int NUM_TRIALS = 5;
    public static final long MAX_DICTIONARY_SIZE = 80000;
    public static final long STEP = 1000;

    public static final int LENGTH_PER_ARRAY = 200;

    public static void main(String[] args) {
        IList<Long> dictionarySize = AnalysisUtils.makeList(0L, MAX_DICTIONARY_SIZE, STEP);

        System.out.println("Starting test");
        IList<Long> test1Results = AnalysisUtils.runTrials(dictionarySize, Experiment1::test1, NUM_TRIALS);
        IList<Long> test2Results = AnalysisUtils.runTrials(dictionarySize, Experiment1::test2, NUM_TRIALS);
        IList<Long> test3Results = AnalysisUtils.runTrials(dictionarySize, Experiment1::test3, NUM_TRIALS);

        System.out.println("Saving experiment results to file");
        CsvWriter writer = new CsvWriter();
        writer.addColumn("DictionarySize", dictionarySize);
        writer.addColumn("Test1Result", test1Results);
        writer.addColumn("Test2Result", test2Results);
        writer.addColumn("Test3Result", test3Results);
        writer.writeToFile("experimentdata/experiment1.csv");

        System.out.println("All done!");
    }

    public static long test1(long dictionarySize) {
        IList<char[]> chars = generateRandomCharArrays((int) dictionarySize, LENGTH_PER_ARRAY);

        long start = System.currentTimeMillis();
        IDictionary<FakeString1, char[]> dict = new ChainedHashDictionary<>();
        for (char[] array : chars) {
            dict.put(new FakeString1(array), array);
        }
        return System.currentTimeMillis() - start;
    }

    public static long test2(long dictionarySize) {
        IList<char[]> chars = generateRandomCharArrays((int) dictionarySize, LENGTH_PER_ARRAY);

        long start = System.currentTimeMillis();
        IDictionary<FakeString2, char[]> dict = new ChainedHashDictionary<>();
        for (char[] array : chars) {
            dict.put(new FakeString2(array), array);
        }
        return System.currentTimeMillis() - start;
    }

    public static long test3(long dictionarySize) {
        IList<char[]> chars = generateRandomCharArrays((int) dictionarySize, LENGTH_PER_ARRAY);

        long start = System.currentTimeMillis();
        IDictionary<FakeString3, char[]> dict = new ChainedHashDictionary<>();
        for (char[] array : chars) {
            dict.put(new FakeString3(array), array);
        }
        return System.currentTimeMillis() - start;
    }

    private static IList<char[]> generateRandomCharArrays(int numArrays, int lengthPerArray) {
        Random rand = new Random();

        IList<char[]> output = new DoubleLinkedList<>();
        for (int i = 0; i < numArrays; i++) {
            char[] array = new char[lengthPerArray];
            for (int j = 0; j < lengthPerArray; j++) {
                array[j] = (char) (rand.nextInt('z' - 'a') + 'a');
            }
            output.add(array);
        }
        return output;
    }

    public static class FakeString {
        protected char[] chars;

        public FakeString(char[] chars) {
            this.chars = chars;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof FakeString)) {
                return false;
            }
            FakeString otherFake = (FakeString) other;
            if (this.chars.length != otherFake.chars.length) {
                return false;
            }
            for (int i = 0; i < this.chars.length; i++) {
                if (this.chars[i] != otherFake.chars[i]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            throw new NotYetImplementedException();
        }
    }

    public static class FakeString1 extends FakeString {
        public FakeString1(char[] chars) {
            super(chars);
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            // Returning just a constant makes the ChainedHashMap take a hideously long amount of time
            return this.chars[0] + this.chars[1] + this.chars[2] + this.chars[3];
        }
    }

    public static class FakeString2 extends FakeString {
        public FakeString2(char[] chars) {
            super(chars);
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            int out = 0;
            for (char c : this.chars) {
                out += c;
            }
            return out;
        }
    }

    public static class FakeString3 extends FakeString {
        public FakeString3(char[] chars) {
            super(chars);
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            // Note: this is basically what Java's List.hashCode() method does.
            // See https://docs.oracle.com/javase/8/docs/api/java/util/List.html#hashCode()
            int out = 1;
            for (char c : this.chars) {
                out = out * 31 + c;
            }
            return out;
        }
    }
}
