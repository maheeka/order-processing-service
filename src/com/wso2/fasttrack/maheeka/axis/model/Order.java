package com.wso2.fasttrack.maheeka.axis.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Order {

	private final static Logger LOGGER = Logger.getLogger(Order.class.getName());

	private int orderId;

	private Map<Integer, OrderItem> orderItems;

	private String customerName;

	private double totalPrice;

	private Date orderDate;

	public Order(String customerName) {
		this.customerName = customerName;
		orderItems = new HashMap<Integer, OrderItem>();
		orderDate = Calendar.getInstance().getTime();
	}

	public OrderItem[] getOrderItems() {
		return orderItems.values().toArray(new OrderItem[] {});
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void addOrderItem(Item item, double units) {
		if (orderItems.containsKey(item.getItemId())) {
			OrderItem orderItem = orderItems.get(item.getItemId());
			orderItems.put(item.getItemId(), new OrderItem(item, units + orderItem.getUnits()));
		} else {
			orderItems.put(item.getItemId(), new OrderItem(item, units));
		}
	}

	public void modifyOrderItem(Item item, double units) {
		if (orderItems.containsKey(item.getItemId())) {
			orderItems.put(item.getItemId(), new OrderItem(item, units));
		}
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void addOrderItem(OrderItem orderItem) {
		LOGGER.info("Before adding Order Items size is " + orderItems.size());
		// LOGGER.info("===Going to add " + orderItem + " to the order");
		if (orderItems.containsKey(orderItem.getItem().getItemId())) {
			OrderItem orderItemOld = orderItems.get(orderItem.getItem().getItemId());
			// LOGGER.info("*old order item = " + orderItemOld);
			// LOGGER.info("*new order item = " + orderItem);
			double newUnits = orderItemOld.getUnits() + orderItem.getUnits();
			LOGGER.info("*New units = " + newUnits);
			orderItem.setUnits(newUnits);
		}
		orderItems.put(orderItem.getItem().getItemId(), orderItem);
		// LOGGER.info("-Order Items Map >>");
		// printMap(orderItems);
		LOGGER.info("A new order item (orderItem=" + orderItem +
		            " is added to the order (orderId=" + orderId);
		LOGGER.info("After adding Order Items size is " + orderItems.size());
		// printMap(orderItems);
	}

	// private void printMap(Map<Integer, OrderItem> map) {
	//
	// Iterator it = map.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry pairs = (Map.Entry) it.next();
	// LOGGER.info(pairs.getKey() + " = " + pairs.getValue());
	// it.remove(); // avoids a ConcurrentModificationException
	// }
	//
	// }

	public void removeItem(int itemId) {
		orderItems.remove(itemId);

	}

	public void processOrder() {
		OrderItem[] orderItemsList = orderItems.values().toArray(new OrderItem[] {});
		totalPrice = 0;
		for (OrderItem oi : orderItemsList) {
			totalPrice += (oi.getUnits() * oi.getItem().getUnitPrice());
		}
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderItems=" + orderItems + ", customerName=" +
		       customerName + ", totalPrice=" + totalPrice + ", orderDate=" + orderDate + "]";
	}
}
