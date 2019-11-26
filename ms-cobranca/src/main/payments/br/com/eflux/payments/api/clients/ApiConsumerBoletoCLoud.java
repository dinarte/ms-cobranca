package br.com.eflux.payments.api.clients;

import static javax.ws.rs.core.MediaType.WILDCARD;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.util.Strings;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.util.SerializationException;
import br.com.eflux.config.financeiro.domain.ConfiguracaoBoleto;
import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;
import br.com.eflux.financeiro.domain.Boleto;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.payments.api.BatchFile;
import br.com.eflux.payments.api.Invoice;
import br.com.eflux.payments.api.PaymentApiConfiguration;
import br.com.eflux.payments.api.PaymentApiConfigurationAccount;
import br.com.eflux.payments.api.PaymentApiConsumer;
import br.com.eflux.payments.api.PaymentApiException;

@Component
public class ApiConsumerBoletoCLoud implements PaymentApiConsumer {
	
	private Client client;
	
	private PaymentApiConfigurationAccount config;
	

	@Override
	public String getDescription() {
		return "Boletos Cloud - https//boleto.cloud";
	}
	
	@Override
	public void basicAuthentication(PaymentApiConfigurationAccount config) {
			client = ClientBuilder.newClient().register(HttpAuthenticationFeature.basic(
					config.getBoletoApiConfiguration().getUserApi(),
					config.getBoletoApiConfiguration().getPass()));
			this.config = config;
	}
	
	
	@Override
	public Invoice createFromDebito(Debito debito) throws PaymentApiException {
		return create(extractDataFromDebito(debito));
	}
	
	@Override
	public Invoice create(Map<String, String> data) throws PaymentApiException {
		Invoice b = new Boleto();
		
		Form formData = createFormData(data);
		
		Response response = client
				.target(config.getBoletoApiConfiguration().getUri())
				.path("/boletos")
				.request(WILDCARD)
				.post(Entity.form(formData));
		
		if(response.getStatus() == CREATED.getStatusCode()){
			
			b.setLocation(response.getHeaderString("Location"));
			b.setNossoNumero(response.getHeaderString("X-BoletoCloud-NIB-Nosso-Numero"));
			b.setTokenId(response.getHeaderString("X-BoletoCloud-Token"));
			b.setStatus(Invoice.STATUS_NOVO);
			b.setPdfFile(extractFile(response));
			
		}else{
			throw new PaymentApiException(response.readEntity(String.class));
		}
		
		return b;
	}
	
	@Override
	public byte[] get(String tokenId) throws PaymentApiException{
		
		Response response = client
				.target(config.getBoletoApiConfiguration().getUri())
				.path("/boletos/"+tokenId)
				.request(WILDCARD)
				.get();
		
		if(response.getStatus() == OK.getStatusCode()){
			return extractFile(response);
		}else {
			throw new PaymentApiException(response.readEntity(String.class));
		}
		
	}
	
	
	@Override
	public void writeOff(String tokenId, String msg) throws PaymentApiException {

		if (Strings.isBlank(msg))
			msg = "Baixa feita através do sistema de origem.";
		
		try {
			Entity<String> entity = Entity.json("{\"boleto\":{\"baixa\":{\"motivo\":\""+msg+"\"}}}");
					
			Response response = client
					.target(config.getBoletoApiConfiguration().getUri())
					.path("/boletos/"+tokenId+"/baixa")
					.request("(application/json;charset=utf-8)")
					.put(entity);
					
			
			if(! (response.getStatus() == CREATED.getStatusCode()))
				throw new PaymentApiException(response.readEntity(String.class));
			
		} catch (SerializationException e) {
			throw new PaymentApiException(e.getMessage());
		}		
	}
	
	@Override
	public byte[] duplicate(String tokenId, Date date) throws PaymentApiException {

		if (Objects.isNull(date))
			throw new PaymentApiException("É obrigatório informar uma nova data de vencimento do boleto.");
		
		SimpleDateFormat formater = new SimpleDateFormat("yyy-MM-dd");
				
		Response response = client
				.target(config.getBoletoApiConfiguration().getUri())
				.path("/boletos/"+tokenId+"/atualizado/vencimento/"+formater.format(date))
				.request("(application/json;charset=utf-8)")
				.get();
				
		
		if(response.getStatus() == OK.getStatusCode()){
			return extractFile(response);
		}else {
			throw new PaymentApiException(response.readEntity(String.class));
		}			
		
	}
	
	
	@Override
	public BatchFile createBatch(String accountTokenId, BatchFile file) throws PaymentApiException {
		
		
		Form formData = new Form();
		formData.param("remessa.conta.token", accountTokenId);
		
	
		Response response = client
				.target(config.getBoletoApiConfiguration().getUri())
				.path("/arquivos/cnab/remessas")
				.request(WILDCARD)
				.post(Entity.form(formData));
		
		if(response.getStatus() == CREATED.getStatusCode()){
			
			file.setLocation(response.getHeaderString("Location"));
			file.setTokenId(response.getHeaderString("X-BoletoCloud-Token"));
			file.setName(response.getHeaderString("Content-Disposition").split(";")[1].trim());
			file.setFile(extractFile(response));
			
		}else{
			throw new PaymentApiException(response.readEntity(String.class));
		}
		
		return file;
	}

	
	private Form createFormData(Map<String, String> data) {
		Form formData = new Form();
		formData.param("boleto.conta.token", config.getToken());
		formData.param("boleto.instrucao",config.getInstrucoes());
		data.keySet().forEach(k -> formData.param(k, data.get(k)));
		return formData;
	}
	
	private Map<String, String> extractDataFromDebito(Debito d) {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("boleto.emissao",d.getDataLancamentoFormatada("yyyy-MM-dd"));
		ret.put("boleto.vencimento",d.getDataVencimentoFormatada("yyyy-MM-dd"));
		ret.put("boleto.documento",d.getContrato().getNumeroContrato().toString().concat(".").concat(d.getNumero().toString()));
		//ret.put("boleto.sequencial", d.getContrato().getId().toString().concat(d.getId().toString()));
		ret.put("boleto.titulo","DM");
		ret.put("boleto.valor",d.getValorAtualizado().toString());
		ret.put("boleto.pagador.nome",d.getContrato().getPessoa().getNome());
		ret.put("boleto.pagador.cprf",d.getContrato().getPessoa().getCpf());
		ret.put("boleto.pagador.endereco.cep",d.getContrato().getPessoa().getCep());
		ret.put("boleto.pagador.endereco.uf",d.getContrato().getPessoa().getMunicipio().getUf().getSigla());
		ret.put("boleto.pagador.endereco.localidade",d.getContrato().getPessoa().getMunicipio().getNome());
		ret.put("boleto.pagador.endereco.bairro",d.getContrato().getPessoa().getBairro());
		ret.put("boleto.pagador.endereco.logradouro",d.getContrato().getPessoa().getLogradouro());
		ret.put("boleto.pagador.endereco.numero",d.getContrato().getPessoa().getNumero());
		ret.put("boleto.pagador.endereco.complemento",d.getContrato().getPessoa().getComplemento());
		return ret;
	}

	private byte[] extractFile(Response response) {
		InputStream is = response.readEntity(InputStream.class);
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] dados = new byte[16384];

		try {
			while ((nRead = is.read(dados, 0, dados.length)) != -1) {
			  buffer.write(dados, 0, nRead);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return buffer.toByteArray();
	}	
	
	public static void main(String[] args) throws ParseException {
	
		 PaymentApiConfiguration configuration = new ConfiguracaoBoleto();
		 configuration.setApiImplementation("");
		 configuration.setUri("https://sandbox.boletocloud.com/api/v1");
		 configuration.setUserApi("api-key_pK0gkBALwFMhWC9oqjBVr_X_wl5Cjd5ypQbr0boZ5-8=");
		 configuration.setPass("token");
		 
		 PaymentApiConfigurationAccount configAccount = new ConfiguracaoBoletoConta();
		 configAccount.setBoletoApiConfiguration(configuration);
		 configAccount.setToken("api-key_FNd8Szp-K1dmxG-Zh3xfmRtXs8Pa4QGKJjFXlBqmiFE=");
		 
		 ApiConsumerBoletoCLoud consumer = new ApiConsumerBoletoCLoud();
		
		 consumer.basicAuthentication(configAccount);
		
		 
		 
		 try {
			 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			 Date vencimento = formato.parse("2019-11-1");
			 
			//consumer.writeOff("1-wWc3yoplLcPlSRyAC3U-tlhHTj-nxSvkGduAdiumM=", "Pagamento efetuado em loco recebido por Dinarte ");
			byte[] ret = consumer.duplicate("1-wWc3yoplLcPlSRyAC3U-tlhHTj-nxSvkGduAdiumM=", vencimento);
			System.out.println("operacao realizada");
		} catch (PaymentApiException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		 
		 /*
		byte[] pdf;
		try {
			pdf = consumer.get("k8htUkLzJyT8LY5PihIYjgMpIlgST1WIpdfzGalDaAc43=");
			 System.out.println(pdf);
		} catch (PaymentApiException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}*/
		 
	}

}
