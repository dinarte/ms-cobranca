package test;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class BoletoCloudTeste {
	
	
	
	
	
	public static void main(String[] args) {
	
	
		String uri = "http://sandbox.boletocloud.com/api/v1/boletos";
		
		String username="api-key_pK0gkBALwFMhWC9oqjBVr_X_wl5Cjd5ypQbr0boZ5-8=";
		String password="token";
	
		String auth = username + ":" + password;
	    byte[] encodedAuth = Base64.getEncoder().encode( auth.getBytes(Charset.forName("US-ASCII")) );
	    String authHeader = "Basic " + new String( encodedAuth );
	    
		
		
		RestTemplate rest = new RestTemplate();	
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic "+authHeader);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		
		map.add("boleto.conta.token", "api-key_FNd8Szp-K1dmxG-Zh3xfmRtXs8Pa4QGKJjFXlBqmiFE=");
		map.add("boleto.emissao","2014-07-11");
		map.add("boleto.vencimento","2020-05-30");
		map.add("boleto.documento","EX1");
		map.add("boleto.numero","12345678903-P");
		map.add("boleto.titulo","DM");
		map.add("boleto.valor","1250.43");
		map.add("boleto.pagador.nome","Alberto Santos Dumont");
		map.add("boleto.pagador.cprf","111.111.111-11");
		map.add("boleto.pagador.endereco.cep","36240-000");
		map.add("boleto.pagador.endereco.uf","MG");
		map.add("boleto.pagador.endereco.localidade","Santos Dumont");
		map.add("boleto.pagador.endereco.bairro","Casa Natal");
		map.add("boleto.pagador.endereco.logradouro","BR-499");
		map.add("boleto.pagador.endereco.numero","s/n");
		map.add("boleto.pagador.endereco.complemento","Teste - Subindo a serra da Mantiqueira");
		map.add("boleto.instrucao","Atenção, não RECEBER ESTE BOLETO.");
		map.add("boleto.instrucao","Este é apenas um teste utilizando a API Boleto Cloud");
		map.add("boleto.instrucao","Mais info em http://www.boletocloud.com/app/dev/api");
		
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,headers);
		
		
		ParameterizedTypeReference<HashMap<String, String>> responseType = 
	               new ParameterizedTypeReference<HashMap<String, String>>() {};
		
		
	//	Object ret1 = rest.exchange (uri, HttpMethod.GET, httpEntity, Object.class);
	
	//	ResponseEntity<HashMap<String, String>> ret2 = rest.exchange (uri, HttpMethod.POST, httpEntity, responseType);

		ResponseEntity<String> ret3 = rest.exchange (uri, HttpMethod.POST, httpEntity, String.class);
		
		

		
		System.out.println(ret3);
		
	
		//MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		//map.add("nome", "João Silva");
		//map.add("valor", new BigDecimal("1.00"));

		//HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		//ResponseEntity<Pedido> response = restTemplate.postForEntity( url, request , Pedido.class )
		
				 
		 
	}

}
