package problems;

//import misc.exceptions.NotYetImplementedException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Parts b.i and b.ii should go here.
 *
 * (Implement contains3 and intersect as described by the spec
 *  See the spec on the website for examples and more explanation!)
 */
public class MapProblems {

    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> ans = new HashMap<String, Integer>();
        for (String key:m1.keySet())
        {
            if (m2.containsKey(key)&& m2.get(key)==m1.get(key))
            {
                ans.put(key, m1.get(key));
            }
        }

        return ans;
    }

    public static boolean contains3(List<String> input) {
        Map<String, Integer> count = new HashMap<String, Integer>();
        for (int i=0; i<input.size(); i++) {
            count.put(input.get(i), count.getOrDefault(input.get(i), 0)+1);
        }
        for (int c: count.values())
        {
            if (c==3) {
                return true;
            }
        }
        return false;
    }
}
