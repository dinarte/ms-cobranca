package br.com.dfframeworck.util;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ObjectToMap {

	@SuppressWarnings("unchecked")
    public static Map<String, Object>  toMap(Object obj) {
    	ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(obj, Map.class);
		return map;
    }
	
}

