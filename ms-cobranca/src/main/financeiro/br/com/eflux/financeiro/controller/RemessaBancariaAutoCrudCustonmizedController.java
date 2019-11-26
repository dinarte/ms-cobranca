package br.com.eflux.financeiro.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.financeiro.domain.RemessaBancaria;
import br.com.eflux.financeiro.repository.RemessaBancariaRepository;

@Controller
@Customizer(RemessaBancaria.class)
public class RemessaBancariaAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {

	@Autowired
	private RemessaBancariaRepository remessaRepo;
	
	@Override
	public void addActions(List<AutoCrudAction> actions) {
		
		actions.add(new AutoCrudAction("Baixar Arquivo", "/invoice/remessa/{id}/baixar", "fa fa-download","btn btn-info btn-circle", true, 1));
	}
	
	@RequestMapping("/invoice/remessa/{idRemessa}/baixar")
	@Functionality(isPublic=false, name="Baixar", menu="none")
	public void baixar(@PathVariable("idRemessa") Long idRemessa, HttpServletResponse response) throws IOException {
		
		
		RemessaBancaria remessa = remessaRepo.findById(idRemessa)
				.orElseThrow(()-> new IllegalArgumentException("Não encontramos remessa com o id informado"));
		

		byte[] blobData = remessa.getFile();
		String nome = remessa.getName();
		String caminho = (nome);
		
		//Now you got your data in blobData... write to file
		FileOutputStream fileOut = new FileOutputStream(caminho);
		fileOut.write(blobData);
		   	            
		response.setContentType("text/plain;charset=utf-8");
		
		String arq = "attachment;filename="+ nome;
		response.setHeader("Content-Disposition", arq);
		
		ServletOutputStream os = response.getOutputStream();
		fileOut.close();
		os.write( blobData );
		os.close();
		  
	}
	
}
