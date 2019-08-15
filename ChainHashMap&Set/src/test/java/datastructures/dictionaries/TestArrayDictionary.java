package datastructures.dictionaries;

import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayDictionary extends BaseTestDictionary {
    protected <K, V> IDictionary<K, V> newDictionary() {
        return new ArrayDictionary<>();
    }
}
