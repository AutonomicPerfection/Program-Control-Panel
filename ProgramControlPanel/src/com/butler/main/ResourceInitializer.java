package com.butler.main;

import javax.swing.JPanel;

public interface ResourceInitializer {
	/**This method should be overridden to create resources and add
	 * them to {@link com.butler.main.SystemVariables} at the index specified*/
	public void initializeResource(int index);
	
	/**Tells the program hub whether a resource should be consumed when used by an app*/
	public default boolean isConsumable() {
		return true;
	}
	
	public default JPanel getGui() {
		return new JPanel();
	}
}
