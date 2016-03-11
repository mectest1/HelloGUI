package com.mec.fx.beans;

import java.util.AbstractMap;
import java.util.Set;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

public class ItemBuilder implements Builder<Item>{
	long id;
	String name;
	
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
	public Item build() {
		return new Item(id, name);
	}

	public static class ItemBuilderFactory implements BuilderFactory{

		final JavaFXBuilderFactory fxFactory = new JavaFXBuilderFactory();
		@Override
		public Builder<?> getBuilder(Class<?> type) {
			//You supply a Builder only for Item type
			if(Item.class == type){
				return new ItemBuilder();
			}
			
			//Let the default Builder do the magic
			return fxFactory.getBuilder(type);
		}
		
	}
	
	
	
	static class ItemBuilderMap extends AbstractMap<String, Object> implements Builder<Item>{
		String name;
		long id;
		
		@Override
		public Item build() {
			return new Item(id, name);
		}

		@Override
		public Set<Entry<String, Object>> entrySet() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object put(String key, Object value) {
			if("name".equals(key)){
				name = (String) value;
			}else if("id".equals(key)){
				id = Long.valueOf((String)value);
			}else{
				throw new IllegalArgumentException("Unknown Item property: " + key);
			}
			return null;
		}
		
		
	}
	
}
