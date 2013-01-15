package codestory13.jajascript;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

public class OrderCombination {
    public int gain = 0;
    public List<Order> orders = new ArrayList<>();

    public OrderCombination(Order... orders) {
        for (Order order : orders) {
            gain = gain + order.price;
            this.orders.add(order);
        }
    }

    public boolean canStartWith(Order order) {
        Order firstOrder = Iterables.getFirst(orders, null);
        if (ordersCanBeChained(order,firstOrder)) {
            return true;
        }
        return false;
    }

    private boolean ordersCanBeChained(Order startingOrder, Order nextOrder) {
        return nextOrder.start >= startingOrder.start + startingOrder.duration;
    }

    public boolean isBetterThan(OrderCombination orderCombination) {
        return orderCombination == null || gain > orderCombination.gain;
    }

    public OrderPath getOrderPath() {
        OrderPath orderPath = new OrderPath();
        orderPath.gain = gain;
        for (Order order : orders) {
            orderPath.path.add(order.flight);
        }
        return orderPath;
    }

    public void add(Order startingOrder, OrderCombination orderCombinationToAppend) {
        add(startingOrder);
        add(orderCombinationToAppend);
    }

    public void add(Order order) {
        orders.add(order);
        gain = gain + order.price;
    }

    public void add(OrderCombination orderCombination) {
        if (orderCombination != null) {
            orders.addAll(orderCombination.orders);
            gain = gain + orderCombination.gain;
        }
    }
}