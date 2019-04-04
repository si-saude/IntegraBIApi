package br.com.saude.api.generic;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericConstant {
	public Map<String,String> getList() throws IllegalArgumentException, IllegalAccessException{
		Map<String,String> map = new HashMap<String,String>();
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field:fields) {
			if(!field.getName().equals("instance"))
				map.put((String)field.get(this), (String)field.get(this));
		}
		return map;
	}
}
