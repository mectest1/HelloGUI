package com.mec.duke.util;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class OptionalUtil {

	public static Optional<Integer> strToInt(String str){
		Objects.requireNonNull(str);
		try{
			return Optional.of(Integer.parseInt(str));
		}catch(NumberFormatException e){
			return Optional.empty();
		}
	}
	
	public static <K, V> Optional<V> get(Map<K,V> map, K key){
		Objects.requireNonNull(map);
		Objects.requireNonNull(key);
		return Optional.ofNullable(map.get(key));
	}
}
