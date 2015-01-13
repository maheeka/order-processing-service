package com.wso2.fasttrack.maheeka.axis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.wso2.fasttrack.maheeka.axis.model.Item;
import com.wso2.fasttrack.maheeka.axis.model.Order;
import com.wso2.fasttrack.maheeka.axis.model.OrderItem;
import com.wso2.fasttrack.maheeka.axis.util.OrderProcessingException;

/**
 * Order Processing Service containing all methods that are exposed as services.
 * 
 * @author maheeka
 * 
 */
public class OrderProcessingService {

	private final static Logger LOGGER = Logger.getLogger(OrderProcessingService.class.getName());

	private static int orderCount = 0;

	private static Map<Integer, Order> orders = new HashMap<Integer, Order>();
	private static Map<Integer, Item> items = new HashMap<Integer, Item>();

	static {
		items.put(1, new Item(1, "Book", 10.0));
		items.put(2, new Item(2, "Pen", 5.0));
		items.put(3, new Item(3, "Pencil Box", 30.0));
		items.put(4, new Item(4, "School Bag", 60.0));
		items.put(5, new Item(5, "Eraser", 2.0));

		for (int i = 6; i <= 245; i++) {
			items.put(i, new Item(i, "Item" + i, i));
		}

	}

	static {
		try {
			createDefaultOrders();
		} catch (OrderProcessingException e) {
			LOGGER.info("Unable to create default orders " + e.getMessage());
		}
	}

	/**
	 * Create a new order with the given customer name.
	 * 
	 * @param customerName
	 */
	public int createOrder(String customerName) {
		Order order = new Order(customerName);
		orderCount++;
		order.setOrderId(orderCount);
		orders.put(orderCount, order);
		LOGGER.info("New order is created as OrderID=" + orderCount + " for customer " +
		            customerName);
		return orderCount;
	}

	/**
	 * Add an item to an existing order by with the number of units
	 * 
	 * @param orderId
	 * @param itemId
	 * @param units
	 * @throws OrderProcessingException
	 *             if the orderID or itemID is invalid
	 */
	public void addOrderItem(int orderId, int itemId, double units) throws OrderProcessingException {
		LOGGER.info("Adding orderItem to order = " + orderId + " itemId=" + itemId);
		if (orders.containsKey(orderId)) {
			Order order = orders.get(orderId);
			LOGGER.info("order " + orderId + " has " + order.getOrderItems().length +
			            " number of items");
			if (items.containsKey(itemId)) {
				OrderItem orderItem = new OrderItem(items.get(itemId), units);
				order.addOrderItem(orderItem);
				LOGGER.info("Item ItemID=" + itemId + " (units=" + units +
				            " is successfully added to order OrderID=" + orderId + " ");
			} else {
				LOGGER.severe("An item by ItemID=" + itemId + " does not exist");
				throw new OrderProcessingException("An item by ItemID=" + itemId +
				                                   " does not exist");
			}
		} else {
			LOGGER.severe("An order by OrderID=" + orderId + " does not exist");
			throw new OrderProcessingException("An order by OrderID=" + orderId + " does not exist");
		}
	}

	/**
	 * Get the order for a given order id
	 * 
	 * @param orderId
	 * @return
	 * @throws OrderProcessingException
	 *             if an order by the given order id does not exist
	 */
	public Order getOrder(int orderId) throws OrderProcessingException {
		if (orders.containsKey(orderId)) {
			return orders.get(orderId);
		} else {
			LOGGER.severe("An order by OrderID=" + orderId + " does not exist");
			throw new OrderProcessingException("An order by OrderID=" + orderId + " does not exist");
		}
	}

	/**
	 * Get an array of order items of a given order
	 * 
	 * @param orderId
	 * @return array of order items
	 * @throws OrderProcessingException
	 *             if an order by the given orderId does not exist
	 */
	public OrderItem[] getOrderItems(int orderId) throws OrderProcessingException {
		if (orders.containsKey(orderId)) {
			Order order = orders.get(orderId);
			OrderItem[] orderItems = order.getOrderItems();
			return orderItems;
		} else {
			LOGGER.severe("An order by OrderID=" + orderId + " does not exist");
			throw new OrderProcessingException("An order by OrderID=" + orderId + " does not exist");
		}
	}

	/**
	 * Removes an item from an order
	 * 
	 * @param orderId
	 *            id of the order
	 * @param itemId
	 *            id of the item to be removed
	 */
	public void removeItemFromOrder(int orderId, int itemId) {
		if (orders.containsKey(orderId)) {
			Order order = orders.get(orderId);
			order.removeItem(itemId);
		}
	}

	/**
	 * Called when preparing for checkout an order by calculating the total
	 * price
	 * 
	 * @param orderId
	 * @return processed order
	 * @throws OrderProcessingException
	 */
	public double getTotalPrice(int orderId) throws OrderProcessingException {
		if (orders.containsKey(orderId)) {
			Order order = orders.get(orderId);
			order.processOrder();
			return order.getTotalPrice();
		} else {
			LOGGER.severe("**An order by OrderID=" + orderId + " does not exist");
			throw new OrderProcessingException("An order by OrderID=" + orderId + " does not exist");
		}
	}

	private static void createDefaultOrders() throws OrderProcessingException {

		// ================== Test Subject Order 1 ============================
		Order order1 = createOrderSt("ann");
		addOrderItemSt(order1, 1, 2.0);
		addOrderItemSt(order1, 2, 1.0);
		addOrderItemSt(order1, 3, 3.0);
		addOrderItemSt(order1, 4, 2.0);

		// ================== Test Subject Order 2 ============================
		Order order2 = createOrderSt("tim");
		addOrderItemSt(order2, 1, 2.0);
		addOrderItemSt(order2, 2, 1.0);
		addOrderItemSt(order2, 3, 1.0);
		addOrderItemSt(order2, 4, 2.0);
		addOrderItemSt(order2, 5, 3.0);

		// ================== Lower Margin Order (5KB) ========================
		Order order3 = createOrderSt("tom");
		for (int i = 1; i < 25; i++) {
			addOrderItemSt(order3, i, i);
		}

		// ================== Higher Margin Order (50KB) ======================
		Order order4 = createOrderSt("john");
		for (int i = 1; i < 240; i++) {
			addOrderItemSt(order4, i, i);
		}

	}

	private static Order createOrderSt(String customerName) {
		Order order = new Order(customerName);
		orderCount++;
		order.setOrderId(orderCount);
		orders.put(orderCount, order);
		LOGGER.info("New order is created as OrderID=" + orderCount + " for customer " +
		            customerName);
		return order;
	}

	private static void addOrderItemSt(Order order, int itemId, double units)
	                                                                         throws OrderProcessingException {
		if (items.containsKey(itemId)) {
			OrderItem orderItem = new OrderItem(items.get(itemId), units);
			order.addOrderItem(orderItem);
			LOGGER.info("Item ItemID=" + itemId + " (units=" + units +
			            " is successfully added to order OrderID=" + order.getOrderId() + " ");
		} else {
			LOGGER.severe("An item by ItemID=" + itemId + " does not exist");
			throw new OrderProcessingException("An item by ItemID=" + itemId + " does not exist");
		}

	}

}
