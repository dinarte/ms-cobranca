package br.com.dfframeworck.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eflux.comum.domain.UnidadeFederativa;


public class ObjectToMap {

	@SuppressWarnings("unchecked")
    public static Map<String, Object>  toMap(Object obj) {
    	ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(obj, Map.class);
		return map;
    }
	
	
    public static Object toObject(Map<?,?> data, Class<?> type) throws JsonParseException, JsonMappingException, IOException {
    	ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);
		return new ObjectMapper().readValue(json, type);
    }
    
    
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
    	Map<String, Object> data = new HashMap<>();
    	
    	Map<String, Object> pais = new HashMap<>();
    	
    	data.put("id", "5");
    	data.put("nome", "Brasil");
    	data.put("sigla", "BR");
    	
    	pais.put("id", "1");
    	
    	data.put("pais", pais);
    	
    	UnidadeFederativa p = (UnidadeFederativa) toObject(data, UnidadeFederativa.class);
    	
    	System.out.println(p);
	}
	
}

