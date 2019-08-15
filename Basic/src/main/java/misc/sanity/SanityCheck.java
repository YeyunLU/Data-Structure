package misc.sanity;

import java.util.ArrayList;
import java.util.function.Function;

// Checkstyle will report an error with this line:
import java.util.List;

public class SanityCheck {
    public static void main(String[] args) {
        // The following two lines should compile only if you correctly installed Java 8 or higher.
        Function<String, String> test = (a) -> a;
        System.out.println(test.apply("Java 8 or above is correctly installed!"));

        // The following four lines should run, but checkstyle should complain about style errors in each lines.
        // TODO: checkstyle will flag all todos as errors
        ArrayList<String> a = new ArrayList<>();
        a.add("test"); // Deliberately long line: ............................................................................................... end
        System.out.println( !a.isEmpty());

        // The following line should produce an error when not commented.
        // List l = new ArrayList();

        System.out.println("Sanity check complete: everything seems to have been configured correctly!");
    }
}
