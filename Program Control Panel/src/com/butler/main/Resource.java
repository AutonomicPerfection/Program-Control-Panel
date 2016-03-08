package com.butler.main;

public interface Resource {
	
	/**Tells the program hub whether a resource should be consumed when used by an app*/
	public default boolean isConsumable() {
		return true;
	}
}
