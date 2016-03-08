package com.butler.main;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class ProgramHub {
	ControlPanelFrame frame;
	Properties props;
	File programDirectory;
	ArrayList<Class<?>> programList = new ArrayList<Class<?>>();
	ArrayList<String[]> programArgs = new ArrayList<String[]>();
	ArrayList<Resource[]> programResources = new ArrayList<Resource[]>();
	
	public ProgramHub() {
		
		Properties resourceProps = new Properties();
		try {
			programDirectory = new File("./resources/ResourceDefinitions.properties");
			if (!programDirectory.exists()) programDirectory.createNewFile();
			resourceProps.load(new FileInputStream(programDirectory));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("List of resources registered:\n{");
		
		Set<String> resourcePropNames = resourceProps.stringPropertyNames();
		int resourceNum = 0;
		for (String currentProp : resourcePropNames) {
			if (currentProp.startsWith("ResourceInitializer_")) resourceNum++;
			
		}
		for (int i = 0; i < resourceNum; i++) {
			System.out.println("\t" + resourceProps.getProperty("ResourceInitializer_" + i + "_ID"));
			try {
				SystemVariables.addResourceInitializer((ResourceInitializer) Class.forName(resourceProps.getProperty("ResourceInitializer_" + i + "_ID")).newInstance());
				SystemVariables.getResourceInitializers().get(i).initializeResource(i);
			} catch (Exception e) {
			}
		}
		
		System.out.println("}\n");
		
		//Load programs
		props = new Properties();		
		try {
			programDirectory = new File("./resources/Programs.properties");
			if (!programDirectory.exists()) programDirectory.createNewFile();
			props.load(new FileInputStream(programDirectory));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("List of programs registered:\n{");
		int numOfPrograms = 0;
		Set<String> propNames = props.stringPropertyNames();
		for (String currentProp : propNames) {
			if (currentProp.startsWith("Program_")) numOfPrograms++;
			
		}
		for (int i = 0; i < numOfPrograms; i++) {
			String programName = null;
			Class<?> programClass = null;
			String[] args = null;
			String[] resourceNames = null;
			//System.out.println("\t" + props.getProperty("Program_" + i) + " args: " + props.getProperty("args_" + i) + " required resources: " + props.getProperty("resources_" + i));
			try {
				programName = props.getProperty("Program_" + i);
			} catch (Exception e) {
			} try {
				args = props.getProperty("args_" + i).split(" ");
			} catch (Exception e) {
			} try {
				resourceNames = props.getProperty("resources_" + i).split(" ");
			} catch (Exception e) {
			}
			System.out.println("\t" + programName + " args: " + props.getProperty("args_" + i) + " required resources: " + props.getProperty("resources_" + i));
			try {
				programList.add(i, (Class<?>) Class.forName(programName));
			} catch (ClassNotFoundException e) {
				System.err.println("No class with such name found. Check spelling in Programs.properties");
			}
			programArgs.add(i, args);
			programResources.add(i, SystemVariables.findResources(resourceNames));
		}
		System.out.println("}");
		frame = new ControlPanelFrame(this);
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProgramHub hub = new ProgramHub();
	}

}
