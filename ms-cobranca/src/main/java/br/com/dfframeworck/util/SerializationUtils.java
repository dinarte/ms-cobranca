package br.com.dfframeworck.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dfframeworck.autocrud.AutoCrudDataMap;
import br.com.eflux.financeiro.dto.AcordoDTO;


public class SerializationUtils {

	@SuppressWarnings("unchecked")
    public static Map<String, Object>  toMap(Object obj) throws SerializationException {
	    try {	
			ObjectMapper oMapper = new ObjectMapper();
			Map<String, Object> map = oMapper.convertValue(obj, Map.class);
			return map;
		}catch (Exception e) {
			throw new SerializationException("Erro ao serializar em Map"+e.getMessage(), e.getCause());
		}
    }
	
    public static AutoCrudDataMap  toAutoCrudMap(Object obj) throws SerializationException {
	    try {	
			ObjectMapper oMapper = new ObjectMapper();
			AutoCrudDataMap map = oMapper.convertValue(obj, AutoCrudDataMap.class);
			return map;
		}catch (Exception e) {
			throw new SerializationException("Erro ao serializar em Map"+e.getMessage(), e.getCause());
		}
    }
	
	
    public static Object toObject(Map<?,?> data, Class<?> type) throws SerializationException{
    	try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String json = objectMapper.writeValueAsString(data);
			return new ObjectMapper().readValue(json, type);
    	}catch (Exception e) {
			throw new SerializationException("Erro em Deserializar o objeto do tipo "+type.getName()
					+ " Provavelmente fata um método get para a priopriedade: "+e.getMessage(), e.getCause());
		}
    }
    
    @SuppressWarnings("unchecked")
	public static Map<String, Object>jsonToMap(String json) throws SerializationException{
    	ObjectMapper objectMapper = new ObjectMapper();
    	try {
			objectMapper.readValue(json, Map.class);
			return objectMapper.readValue(json, Map.class);
		} catch (IOException e) {
			throw new SerializationException(e.getMessage(), e.getCause());
		}
    }
    
    public static Object clone(Object obj) {
    	return org.springframework.util.SerializationUtils.deserialize( org.springframework.util.SerializationUtils.serialize(obj) );
    }
    
    
    public static void main(String[] args) throws SerializationException{
		
    	Map<String, Object> mapa = SerializationUtils.jsonToMap("{\"boleto\":{\"baixa\":{\"motivo\":\"Cancelamento do plano\"}}}");
    	System.out.println(mapa);
	}


	public static String toJson(Object obj) {
		try {	
			ObjectMapper oMapper = new ObjectMapper();
			String json = oMapper.writeValueAsString(obj);
			return json;
		}catch (Exception e) {
			throw new SerializationException("Erro ao serializar em Json: "+e.getMessage(), e.getCause());
		}
	}
	
}

