package com.rain.map;

public class Test {

	public static void main(String[] args) {
		RainHashMap<String,String> rainMap = new RainHashMap<>();
		
		for (int i = 0; i < 10000; i++) {
			rainMap.put("key"+i, "value"+i);
		}
		
		for (int i = 0; i < 10000; i++) {
			System.out.println("key: "+"key"+i+",value:"+rainMap.get("key"+i));
		}
		
		
		

	}

}
