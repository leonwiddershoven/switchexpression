import java.util.List;
import java.util.function.Consumer;

enum WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}

public class Main {

    // Install Java 17 in intellij


    public static void main(String[] args) {
        basics();
//        experimental_patternMatching();
//        experimental_dealingWithNull();
//        exotic_dealingWithLabels();
//        doesnotexist_recordDeconstruction();
    }

    public static void basics() {
        var day = WeekDay.SUNDAY;

        // note fallthrough unless breaks
        switch (day) {
            case MONDAY:
            case FRIDAY:
            case SUNDAY:
                System.out.println(6);
                break;
            case TUESDAY:
                System.out.println(7);
                break;
            case THURSDAY:
            case SATURDAY:
                System.out.println(8);
                break;
            case WEDNESDAY:
                System.out.println(9);
                break;
            // default not required whether all options are checked or not
            // Show this by commenting a case
        }

        // combine cases, no break, note ->
        switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
            case TUESDAY -> System.out.println(7);
            case THURSDAY, SATURDAY -> System.out.println(8);
            case WEDNESDAY -> System.out.println(9);
        }

        // switch expression returns a value
        var greatDay = switch (day) {
            case SATURDAY, SUNDAY -> "Yes";
            case FRIDAY -> "Kind of";
            case TUESDAY, WEDNESDAY, THURSDAY -> "No";
            case MONDAY -> "Absolutely not!";
            // No default because compiler can see there are no other options
            // show this by commenting a case
        };

        // switch expression with : can fallthrough
        var greatDay2 = switch (day) {
            case SATURDAY:
            case SUNDAY:
                yield "Yes";
            case FRIDAY:
                yield "Kind of";
            case TUESDAY, WEDNESDAY:
            case THURSDAY: {
                System.out.println("Code blocks work just fine");
                yield "No";
            }
            case MONDAY:
                yield "Absolutely not!";
                // No default because compiler can see there are no other options
        };

        var summary = """
                    The switch _statement_ does not return a value.
                    The switch _statement_ does not need to be exhaustive (e.g. catch all possible cases)
                    The switch _statement_ always uses :
                    
                    Cases with : are fallthrough in _statement_ and _expression_ form.
                    Cases with -> do not fallthrough but are only available in the _expression_ form
                    
                    The switch _expression_ does not use break;
                    The switch _expression_ uses no break or yield for one-liners without fallthrough.
                    The switch _expression_ can use yield for one-liners without fallthrough.
                    
                    Both _statement_ and _expression_ throw a NullPointerException pre-Java 18/19
                """;

    }

    public static void experimental_patternMatching() {
        Object opt = "a";
        // Object opt = Long.valueOf(1L);

        // Set language level to 17 - pattern matching for switch
        // opt is not a var because the compiler is to smart

//        var result = switch (opt) {
//            case String s -> "a";
//            case Long l -> "b";
//            default -> "c";
//        };
//        System.out.println("Result was " + result);

        var text = """
         What is pattern matching?
         
         Verify that the variable matches some precondition.
         Then do something with it.        
         
         if (a instanceof String s) { ... }
         
         a instanceof String is the precondition (test)
         String s = (String)a; is the action.             
         """;


//        result = switch (opt) {
////            case String s -> "a--"; Not allowed here because it 'eats' the next expression
//            case String s && (s.length() == 2) -> "a";
////            case String s -> "a++"; // this is fine - this is only an option AFTER the previous one
////            case String s || s.length() == 2 -> "a"; // or (||) is not allowed because s is not defined if opt is not a string
//            case Long l -> "b";
//            default -> "c";
//        };
//        System.out.println("Result with lengthCheck was " + result);

    }

    public static void experimental_dealingWithNull() {
        var option = "a";

        switch (option) {
            case "a":
                break;
        }
        System.out.println("DealingWithNull : a finished");

        option = null;
        switch (option) {
            case "a":
                break;
        }
        System.out.println("DealingWithNull : null finished");
// Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.hashCode()" because "<local1>" is null

        option = null;
        var res = switch (option) {
            case "a" -> "b";
            default -> "c";
        };

        System.out.println("DealingWithNull expression finished");
        // Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.hashCode()" because "<local2>" is null

        // Uncomment - set language level to 17 preview
//        option = null;
//        var res= switch (option) {
//            case null -> "NullPointerException - just kidding!";
//            case "a" -> "b";
//            default -> "c";
//        };
//
//        System.out.println("DealingWithNull expression with case null resulted in: " + res);
    }

    public static void exotic_dealingWithLabels() {
        var option = "a";
        var result = "";

        switch (option) {
            case "a":
                result = "b";
                break;
            case "b":
                result = "a";
                break;
            default:
                result = "c";
        }
        System.out.println(result);

        var options = List.of("a", "b", "c", "d");

        for (String opt : options) {
            switch (opt) {
                case "a":
                    result = "b";
                    break;
                case "b":
                    result = "a";
                    break;
                default:
                    result = "c";
            }
            System.out.printf("Option %s is mapped to %s%n", opt, result);
        }

        // TADA - The label !

        z:
        // this is what a label looks like in java.
        // Don't let me see you use this in real life !
        for (String opt : options) {
            switch (opt) {
                case "a":
                    result = "b";
                    break;
                case "b":
                    result = "a";
                    break;
                default:
                    System.out.printf("Passed default with option %s%n", opt);
                    continue z; // jump to a label. With break z we would abort the indicated loop
            }
            System.out.printf("Option %s is mapped to %s%n", opt, result);
        }

        zz:
        for (String opt : options) {
            result = switch (opt) {
                case "a" -> "b";
                case "b" -> "a";
                default -> {
                    yield "placeholder";
//                    System.out.printf("Passed default with option %s%n", opt);
//                    continue zz; // you cannot jump out of a switch expression
                }
            };
            System.out.printf("Option %s is mapped to %s%n", opt, result);
        }
    }

    public static void doesnotexist_recordDeconstruction() {
        record Point(int x, int y) {}

        Consumer<Object> fun = (Object o) -> {
            if (o instanceof Point p) {
                int x = p.x();
                int y = p.y();
                System.out.println(x + y);
            }
        };

        // experimental for Java 18/19+
//        Consumer<Object> funky = (Object o) -> {
//            if (o instanceof Point(int x, int y)) {
//                System.out.println(x+y);
//            }
//        };
//
//        switch (obj) {
//            case Point(int x, int y) -> x+y;
//            default -> throw new DoesNotComputeException(42);
//        }
    }

}

