package com.butler.main;

import java.util.ArrayList;

public class SystemVariables {
	private static ArrayList<Resource> availableResources = new ArrayList<Resource>();
	
	public static ArrayList<Resource> getAvailableResources() {
		return availableResources;
	}
	
	public static void addResource(int i, Resource newResource) {
		availableResources.add(i, newResource);
	}
	
	public static Resource findResource(String resourceName) {
		for (Resource resource : availableResources) {
			if (resource.getClass().getName().endsWith(resourceName)) return resource;
		}
		return null;
	}
	
	public static Resource[] findResources(String[] resourceNames) {
		if (resourceNames == null) return null;
		Resource[] resources = new Resource[resourceNames.length];
		for (int i = 0; i < resourceNames.length; i++) {
			resources[i] = findResource(resourceNames[i]);
		}
		return resources;
	}
	
	public static boolean removeResource (Resource targetResource) {
		return availableResources.remove(targetResource);
	}
	
	private static ArrayList<ResourceInitializer> resourceInitializers = new ArrayList<ResourceInitializer>();
	
	public static ArrayList<ResourceInitializer> getResourceInitializers() {
		return resourceInitializers;
	}
	
	public static void addResourceInitializer(ResourceInitializer newResource) {
		resourceInitializers.add(newResource);
	}
	
	public static boolean removeResourceInitializer(ResourceInitializer targetResource) {
		return resourceInitializers.remove(targetResource);
	}
}
