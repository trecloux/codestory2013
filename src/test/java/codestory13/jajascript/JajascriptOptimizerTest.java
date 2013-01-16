package codestory13.jajascript;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class JajascriptOptimizerTest {

    @Test
    public void should_optimize_with_one_order() throws Exception {
        OrderPath bestPath = optimize(new Order("MONAD42", 0, 5, 10));
        assertThatPathIsAsExpected(bestPath, 10, "MONAD42");
    }

    @Test
    public void should_optimize_with_two_order_that_cannot_be_chained() throws Exception {
        OrderPath bestPath = optimize(
                new Order("flight1", 1, 2, 11),
                new Order("flight2", 2, 2, 10));

        assertThatPathIsAsExpected(bestPath, 11, "flight1");
    }

    @Test
    public void should_optimize_with_two_order_that_cannot_be_chained_reversed_order() throws Exception {
        OrderPath bestPath = optimize(
                new Order("flight2", 2, 2, 11),
                new Order("flight1", 1, 2, 10));

        assertThatPathIsAsExpected(bestPath, 11, "flight2");
    }
    @Test
    public void should_optimize_with_two_order_that_can_be_chained() throws Exception {
        OrderPath bestPath = optimize(
                new Order("flight1", 0, 1, 1),
                new Order("flight2", 1, 2, 1));

        assertThatPathIsAsExpected(bestPath, 2, "flight1", "flight2");
    }

    @Test
    public void should_optimize_with_three_order() throws Exception {
        OrderPath bestPath = optimize(
                new Order("flight1", 0, 2, 1),
                new Order("flight2", 1, 2, 3),
                new Order("flight3", 2, 1, 1));

        assertThatPathIsAsExpected(bestPath, 3, "flight2");
    }

    @Test
    public void should_optimize() throws Exception {
        OrderPath bestPath = optimize(
                new Order("MONAD42", 0, 5, 10),
                new Order("META18", 3, 7, 14),
                new Order("LEGACY01", 5, 9, 8),
                new Order("YAGNI17", 5, 9, 7));

        assertThatPathIsAsExpected(bestPath, 18, "MONAD42", "LEGACY01");
    }

    private OrderPath optimize(Order ... orders) {
        return new JajascriptOptimizer(asList(orders)).bestPath;
    }


    private void assertThatPathIsAsExpected(OrderPath orderPath, int expectedGain, String... path) {
        assertThat(orderPath.gain).isEqualTo(expectedGain);
        assertThat(orderPath.path).containsExactly(path);
    }
}
