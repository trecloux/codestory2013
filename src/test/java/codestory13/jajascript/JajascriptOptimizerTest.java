package codestory13.jajascript;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Integer.compare;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class JajascriptOptimizerTest {

    private static final Logger log = LoggerFactory.getLogger(JajascriptOptimizerTest.class);
    static Random random = new Random();


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

    @Test
    public void should_have_same_results_than_old_impl() throws Exception {
        Stopwatch oldImplWatch = new Stopwatch();
        Stopwatch newImplWatch = new Stopwatch();
        for (int i=1; i<=20; i++) {
            List<Order> orders = randomOrders(random.nextInt(10_000));
            oldImplWatch.start();
            OrderPath bestPathOld = new JajascriptOldOptimizer(new ArrayList(orders)).bestPath;
            oldImplWatch.stop();
            newImplWatch.start();
            OrderPath bestPathNew = new JajascriptOptimizer(new ArrayList<>(orders)).bestPath;
            newImplWatch.stop();
            assertThat(bestPathNew.gain).isEqualTo(bestPathOld.gain);
            assertThat(bestPathNew.getPath()).isEqualTo(bestPathOld.getPath());
        }
        double gain = 100* (oldImplWatch.elapsedMillis() - newImplWatch.elapsedMillis()) / (double)oldImplWatch.elapsedMillis();
        log.info("Gain : {}%", gain);
    }

    private List<Order> randomOrders(int size) {
        List<Order> orders = new ArrayList<>();
        for (int i=0;i<size; i++) {
            orders.add(new Order("custom-" + i, random.nextInt(size)+1, random.nextInt(10)+1, random.nextInt(20)+1));
        }
        orders.sort((a,b) -> Integer.compare(a.start, b.start));
        return orders;
    }

    private OrderPath optimize(Order ... orders) {
        return new JajascriptOptimizer(asList(orders)).bestPath;
    }


    private void assertThatPathIsAsExpected(OrderPath orderPath, int expectedGain, String... path) {
        assertThat(orderPath.gain).isEqualTo(expectedGain);
        assertThat(orderPath.getPath()).containsExactly(path);
    }

    public static class JajascriptOldOptimizer {
        public OrderPath bestPath = null;
        private Map<String, OrderPath> bestPathPerOrder = new HashMap<>();

        public JajascriptOldOptimizer(List<Order> orders) {
            Stopwatch stopwatch = new Stopwatch().start();
            orders.sort((a, b) -> compare(a.start, b.start));
            for (int i = orders.size() - 1; i >= 0; i--) {
                findBestCombinationStartingWith(i, orders);
            }
            log.info("Old optimize took {}", stopwatch);

        }

        private void findBestCombinationStartingWith(int orderIndex, List<Order> orders) {
            Order startingOrder = orders.get(orderIndex);
            OrderPath bestCombinablePath = null;
            for (int i = orders.size() -1; i > orderIndex; i--) {
                OrderPath orderPath = bestPathPerOrder.get(orders.get(i).flight);
                if (orderPath.canStartWith(startingOrder)) {
                    if (orderPath.isBetterThan(bestCombinablePath)) {
                        bestCombinablePath = orderPath;
                    }
                } else {
                    break;
                }
            }
            OrderPath bestCombinationIncludingStartingOrder = new OrderPath(startingOrder, bestCombinablePath);
            bestPathPerOrder.put(startingOrder.flight, bestCombinationIncludingStartingOrder);
            if (bestCombinationIncludingStartingOrder.isBetterThan(bestPath)) {
                bestPath = bestCombinationIncludingStartingOrder;
            }
        }
    }
}
