package search.misc;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Bridge {
    public static <T> IListCollector<T> toIList() {
        return new IListCollector<>();
    }

    public static <T> ISetCollector<T> toISet() {
        return new ISetCollector<>();
    }

    public static <A, B> Function<A, B> wrapCheckedMethod(FunctionThrowsException<A, B> func) {
        return (input) -> {
            try {
                return func.apply(input);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @FunctionalInterface
    public interface FunctionThrowsException<A, B> {
        B apply(A item) throws Exception;
    }

    public static class IListCollector<T> implements Collector<T, IList<T>, IList<T>> {
        @Override
        public Supplier<IList<T>> supplier() {
            return DoubleLinkedList::new;
        }

        @Override
        public BiConsumer<IList<T>, T> accumulator() {
            return (list, item) -> list.add(item);
        }

        @Override
        public BinaryOperator<IList<T>> combiner() {
            return (a, b) -> {
                IList<T> out = new DoubleLinkedList<>();
                for (T i : a) {
                    out.add(i);
                }
                for (T i : b) {
                    out.add(i);
                }
                return out;
            };
        }

        @Override
        public Function<IList<T>, IList<T>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.IDENTITY_FINISH);
        }
    }

    public static class ISetCollector<T> implements Collector<T, ISet<T>, ISet<T>> {
        @Override
        public Supplier<ISet<T>> supplier() {
            return ChainedHashSet::new;
        }

        @Override
        public BiConsumer<ISet<T>, T> accumulator() {
            return (list, item) -> list.add(item);
        }

        @Override
        public BinaryOperator<ISet<T>> combiner() {
            return (a, b) -> {
                ISet<T> out = new ChainedHashSet<>();
                for (T i : a) {
                    out.add(i);
                }
                for (T i : b) {
                    out.add(i);
                }
                return out;
            };
        }

        @Override
        public Function<ISet<T>, ISet<T>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.IDENTITY_FINISH);
        }
    }
}
