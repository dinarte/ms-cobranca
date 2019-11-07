package test;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javax.ws.rs.core.MediaType.WILDCARD;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Exemplo de criaï¿½ï¿½o de um boleto inexistente utilizando o mï¿½todo HTTP POST.
 * Apï¿½s enviar os dados e obter o PDF do boleto como resposta, o arquivo ï¿½
 * aberto utilizando o leitor de PDF do sistema operacional.
 *
 * Para executar o exemplo ï¿½ preciso: # Java 6 ou superior #JAX-RS 2.x ou
 * superior # Jersey Client 2.x ou superior
 */
public class CriarBoleto {

	public static void main(String[] args) throws Exception {
		/*
		 * Dados para criaï¿½ï¿½o do boleto
		 */
		Form formData = new Form();
		formData.param("boleto.conta.token", "api-key_FNd8Szp-K1dmxG-Zh3xfmRtXs8Pa4QGKJjFXlBqmiFE=");
		formData.param("boleto.emissao","2014-07-11");
		formData.param("boleto.vencimento","2020-05-30");
		formData.param("boleto.documento","EX1");
		formData.param("boleto.sequencial","6");
		formData.param("boleto.titulo","DM");
		formData.param("boleto.valor","1250.43");
		formData.param("boleto.pagador.nome","Alberto Santos Dumont");
		formData.param("boleto.pagador.cprf","111.111.111-11");
		formData.param("boleto.pagador.endereco.cep","36240-000");
		formData.param("boleto.pagador.endereco.uf","MG");
		formData.param("boleto.pagador.endereco.localidade","Santos Dumont");
		formData.param("boleto.pagador.endereco.bairro","Casa Natal");
		formData.param("boleto.pagador.endereco.logradouro","BR-499");
		formData.param("boleto.pagador.endereco.numero","s/n");
		formData.param("boleto.pagador.endereco.complemento","Teste - Subindo a serra da Mantiqueira");
		formData.param("boleto.instrucao","Atenção, não RECEBER ESTE BOLETO.");
		formData.param("boleto.instrucao","Este é apenas um teste utilizando a API Boleto Cloud");
		formData.param("boleto.instrucao","Mais info em http://www.boletocloud.com/app/dev/api");
		/*
		 * Requisiï¿½ï¿½o para criaï¿½ï¿½o do boleto
		 */
		Response response = ClientBuilder
				.newClient()
				.target("https://sandbox.boletocloud.com/api/v1")
				.path("/boletos")
				.register(
						//Define o tipo de autenticaï¿½ï¿½o HTTP Basic 
						HttpAuthenticationFeature.basic(
								"api-key_pK0gkBALwFMhWC9oqjBVr_X_wl5Cjd5ypQbr0boZ5-8=",
								"token"
						)
				)
				.request(WILDCARD)//Aceita qualquer resposta
				.post(Entity.form(formData));
		/*
		 * Dados da resposta
		 */
		System.out.println("HTTP Status Code: "+response.getStatus());
		System.out.println("Boleto Cloud Version: "+response.getHeaderString("X-BoletoCloud-Version"));
		System.out.println("Boleto Cloud Token: "+response.getHeaderString("X-BoletoCloud-Token"));
		/*
		 * Identifica se o boleto foi criado ou houve erro
		 */		
		if(response.getStatus() == CREATED.getStatusCode()){
			
			//Salva o arquivo do diretï¿½rio corrente.
			Files.copy(response.readEntity(InputStream.class), Paths.get("arquivo-api-boleto-post-teste.pdf"), REPLACE_EXISTING);
			
			//Caso tenha leitor pdf no sistema..
			//Abrirï¿½ o arquivo PDF utilizando o leitor de PDF do sistema operacional
			//java.awt.Desktop.getDesktop().open(new File("arquivo-api-boleto-post-teste.pdf"));
			
		}else{
			System.err.println("Erro retornado em json: "+response.readEntity(String.class));
		}
		//Para saber mais sobre tratamento de erros veja a seï¿½ï¿½o Status & Erros
	}
}
