package com.wso2.fasttrack.maheeka.axis.model;

public class OrderItem {

	private Item item;

	private double units;

	public OrderItem(Item item, double units) {
		this.item = item;
		this.units = units;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public double getUnits() {
		return units;
	}

	public void setUnits(double units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "OrderItem [item=" + item + ", units=" + units + "]";
	}

}
