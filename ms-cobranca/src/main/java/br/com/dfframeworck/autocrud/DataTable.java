package br.com.dfframeworck.autocrud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.dfframeworck.util.SerializationUtils;

public class DataTable {
	
	private List<List<String>> data = new ArrayList<List<String>>();

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}
	
	public static DataTable fromData(List<?> oData, String[] fields) {
	
		DataTable dataTable = new DataTable();
		List<String> fieldList = Arrays.asList(fields);
		
		oData.forEach(item->{
			
			List<String> row = new ArrayList<String>();
			Map<String, Object> objMap = SerializationUtils.toMap(item);
			objMap.keySet()
				.stream()
				.filter(k -> fieldList.contains(k))
				.forEach(k -> {
					
					row.add(objMap.get(k) == null ? "" : objMap.get(k).toString());
					
				});
			dataTable.getData().add(row);
			
		});
		
		return dataTable;
	}
	

}
