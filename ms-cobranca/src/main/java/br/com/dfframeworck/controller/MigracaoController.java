package br.com.dfframeworck.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.messages.AppMessages;
import br.com.dfframeworck.messages.SucessMsg;
import br.com.dfframeworck.repository.MigrationService;
import br.com.dfframeworck.security.Functionality;

@Controller
@RequestScope
public class MigracaoController {
	
	@Autowired
	MigrationService migrationService;
	
	@Autowired
	AppMessages msgs;
	

	@RequestMapping("/index")
	@Functionality(isPublic=false, name="Migração de Dados", menu="root->Configurações->migracao", icon="fa fa-database")
	public String index(Model model) throws ClassNotFoundException {	
		List<Class<?>> classList = migrationService.getAllMigrables();
		model.addAttribute("classList",classList);
		model.addAttribute("gRepo",migrationService);
		return "migracao/listar";
	}

	@SucessMsg
	@PostMapping("/upload/{packageName}/{className}")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
    		@PathVariable(name="packageName") String packageName,
            @PathVariable(name="className") String className, Model model) throws ValidacaoException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
	
		if (Objects.isNull(file))
			throw new ValidacaoException("Arquivo de migração precisa ser informado.");
		
		if (!file.getContentType().equals("text/csv") && !file.getContentType().equals("text/plain")) 
			throw new ValidacaoException("Tipo de arquivo errado: ("+file.getContentType()+"). O tipo de arquivo deve ser txt ou csv.");
		
		
		migrationService.migrar(file, packageName, className);

        return index(model);
    }
	
	@SucessMsg
	@RequestMapping("/migracao/{entity}/apagar")
	@Functionality(name="Apagar", menu="none", isPublic=false)
    public String delete(@PathVariable("entity") String entity, Model m) throws ClassNotFoundException {
    	migrationService.deleteEntity(entity);
    	return index(m);
    	
    }
	
	
	
	
}
