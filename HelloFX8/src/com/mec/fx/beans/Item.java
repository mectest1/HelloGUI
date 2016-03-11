package com.mec.fx.beans;


public class Item{
	long id;
	String name;
	
	
	public Item() {
//		super();
	}
	public Item(Item item){
		id = item.id + 100;
		name = item.name + "(Copied)";
	}
	public Item(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + "]";
	}
}
