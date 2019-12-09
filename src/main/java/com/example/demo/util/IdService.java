//信管182 徐学印 201802104067
package com.example.demo.util;

public class IdService {
	private static int nextId=1000;
	public synchronized static int getId(){
		return nextId++;
	}		
}
