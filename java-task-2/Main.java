import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class Main {

    public class StreamTasks {
        public record Prop(UUID id, String name, int value) {
        }

        // Stream Generator
        public static Stream<Prop> generate(int n) {
            return IntStream.range(0, n)
                    .mapToObj(i -> new Prop(UUID.randomUUID(), "Prop-" + i, i));
        }

        // Flattening
        public static Stream<UUID> toIds(List<List<Prop>> nestedProps) {
            return nestedProps.stream()
                    .flatMap(List::stream)
                    .map(Prop::id);
        }

        // Count
        public static long countEven(Stream<Integer> numbers) {
            return numbers.filter(num -> num % 2 == 0).count();
        }

        // Shortening Operations
        public static Stream<Character> firstWord(Stream<Character> chars) {
            return chars.dropWhile(Character::isWhitespace)
                    .takeWhile(c -> !Character.isWhitespace(c));
        }

        // Indirect Mapping
        public static Stream<String> toPropertyStrings(Stream<Prop> props) {
            return props.flatMap(p -> Stream.of(p.id().toString(), p.name()))
                    .filter(Objects::nonNull)
                    .map(Objects::toString);
        }

        // Sorting
        public static List<Prop> sortByValueThenName(Stream<Prop> props) {
            return props.sorted(Comparator.comparingInt(Prop::value)
                    .thenComparing(Prop::name, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        }

        // Filter by Property
        public static List<Prop> filterNonNullName(Stream<Prop> props) {
            return props.filter(p -> p.name != null)
                    .collect(Collectors.toList());
        }

        // Statefull Filter
        public static Stream<Prop> distinctById(Stream<Prop> props) {
            Set<UUID> seendIds = new HashSet<>();
            return props.filter(p -> seendIds.add(p.id));
        }

        // Aggregation
        public static String getNameOfHighestValue(Stream<Prop> props) {
            return props.max(Comparator.comparingInt(Prop::value))
                    .map(Prop::name)
                    .orElse(null);
        }

        // Combining Collectors

        public static Map<String, Set<UUID>> findNameConflicts(Stream<Prop> props) {
            return props.filter(p -> p.name != null)
                    .collect(Collectors.collectingAndThen(
                            Collectors.groupingBy(Prop::name, Collectors.mapping(Prop::id, Collectors.toSet())),
                            map -> {
                                map.values().removeIf(set -> set.size() <= 1);
                                return map;
                            }));
        }

        // Stateful Collectors

        public static Map<String, Integer> sumValueByName(Stream<Prop> props) {
            return props.collect(Collectors.groupingBy(Prop::name, Collectors.summingInt(Prop::value)));
        }

        // Collector Chaining

        public static Map<Boolean, Integer> sumEvenAddOdd(Stream<Integer> numbers) {
            return numbers.filter(Objects::nonNull)
                    .collect(Collectors.partitioningBy(n -> n % 2 == 0, Collectors.summingInt(Integer::intValue)));
        }

        // Custom Aggregation

        public record MinMaxNames(String lowestValueName, String highestValueNames) {
        };

        public static MinMaxNames getHighestAndLowest(Stream<Prop> props) {
            return props.collect(Collectors.teeing(Collectors.minBy(Comparator.comparingInt(Prop::value)),
                    Collectors.maxBy(Comparator.comparingInt(Prop::value)),
                    (min, max) -> new MinMaxNames(min.map(Prop::name).orElse(null), max.map(Prop::name).orElse(null))));
        }
    }

        public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println("1. Generate: " + StreamTasks.generate(3).toList());

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        StreamTasks.Prop p1 = new StreamTasks.Prop(id1, "Alpha", 10);
        StreamTasks.Prop p2 = new StreamTasks.Prop(id2, "Beta", 5);
        StreamTasks.Prop p3 = new StreamTasks.Prop(UUID.randomUUID(), "Alpha", 20);

        System.out.println("2. Flattening: "
            + StreamTasks.toIds(List.of(List.of(p1), List.of(p2))).toList());
        System.out.println("3. Counts (even): "
            + StreamTasks.countEven(Stream.of(1, 2, 3, 4, 6)));
        System.out.println("4. First word: "
            + StreamTasks.firstWord(" hello world".chars()
                .mapToObj(character -> (char) character))
                .toList());
        System.out.println("5. Indirect Mapping: "
            + StreamTasks.toPropertyStrings(Stream.of(new StreamTasks.Prop(id1, null, 42)))
                .toList());
        System.out.println("6. Sorting: "
            + StreamTasks.sortByValueThenName(Stream.of(p1, p2,
                new StreamTasks.Prop(id1, "Aaron", 15))));
        System.out.println("7. Filter non-null name: "
            + StreamTasks.filterNonNullName(Stream.of(p1,
                new StreamTasks.Prop(id2, null, 1))));
        System.out.println("8. Stateful filter (distinct ID): "
            + StreamTasks.distinctById(Stream.of(p1, p1, p2)).toList());
        System.out.println("9. Highest value name: "
            + StreamTasks.getNameOfHighestValue(Stream.of(p1, p2, p3)));
        System.out.println("10. Name conflicts: "
            + StreamTasks.findNameConflicts(Stream.of(p1, p2, p3)));
        System.out.println("11. Sum values by name: "
            + StreamTasks.sumValueByName(Stream.of(p1, p2, p3)));
        System.out.println("12. Sum even & odd: "
            + StreamTasks.sumEvenAddOdd(Stream.of(1, 2, 3, 4, 5)));
        System.out.println("13. Lowest & Highest names: "
            + StreamTasks.getHighestAndLowest(Stream.of(p1, p2, p3)));
    }
}