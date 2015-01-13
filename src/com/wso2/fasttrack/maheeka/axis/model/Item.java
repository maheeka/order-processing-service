package com.wso2.fasttrack.maheeka.axis.model;

public class Item {

	private String name;

	private int itemId;

	private double unitPrice;

	public Item(int itemId, String name, double unitPrice) {
		this.name = name;
		this.itemId = itemId;
		this.unitPrice = unitPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", itemId=" + itemId + ", unitPrice=" + unitPrice + "]";
	}

}
