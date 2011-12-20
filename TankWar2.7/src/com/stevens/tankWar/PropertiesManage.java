package com.stevens.tankWar;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManage {
	private static Properties pro;
	
	static{
		 	pro=new Properties();
		try {
			pro.load(PropertiesManage.class.getClassLoader().getResourceAsStream("configure/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private PropertiesManage(){
		
	}

	public static Properties getPro() {
		return pro;
	};
	
	
	
}
