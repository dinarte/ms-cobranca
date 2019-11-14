package br.com.dfframeworck.repository;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.dfframeworck.converters.ConverteresProvider;
import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.messages.AppFlashMessages;
import br.com.dfframeworck.util.PackageUtils;

@Component
@Transactional(rollbackFor=Exception.class)
public class MigrationService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	ConverteresProvider converterProvider;
	
	@Autowired
	AppFlashMessages appMsgs;

	public Long countEntity(Class<?> entity) {
		return (long) em.createQuery( "select count(*) from "+entity.getSimpleName() ).getSingleResult();
	}
	
	public Long getIdByOriginal(Class<?> entity, String originalId) {
		return (long) em.createQuery( "select id from  "+entity.getSimpleName() + " where originalId = :originalId" )
				.setParameter("originalId", originalId.toString())
				.getSingleResult();
	}
	
	
	public Object findOne(Class<?> entity, Long id) {
		return  em.createQuery( "from  "+entity.getSimpleName() + " where id = :id" )
				.setParameter("id", id)
				.getSingleResult();
	}
	
	public void deleteEntity(String entity) {
		em.createQuery("delete from "+entity).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public void persistir(Persistable<Long> obj) {
		((Migrable<Long>) obj).setOriginalId(obj.getId().toString());
		((Migrable<Long>) obj).setId(null);
		em.persist(obj);
		em.flush();
	}

	public List<Class<?>> getAllMigrables() throws ClassNotFoundException {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		classList.addAll( PackageUtils.getClassList("br.com.eflux.comum.domain") );
		classList.addAll( PackageUtils.getClassList("br.com.eflux.empreendimento.domain") );
		classList.addAll( PackageUtils.getClassList("br.com.eflux.financeiro.domain") );
		classList = classList.stream().filter( c -> c.isAnnotationPresent(Entity.class) ).collect(Collectors.toList());
		return classList;
	}

	@SuppressWarnings("unchecked")
	public void migrar(MultipartFile file, String packageName, String className) throws IOException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, ValidacaoException {
		
		ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		int  rowCont = 0;
		String[] cols = null;
		Class<?>[] types = null;
		
		List<String> erros = new ArrayList<>();
		
		while(linha != null) {
			boolean erroNalinha = false;
			Object obj = Class.forName(packageName+'.'+className).newInstance();
	
			if (rowCont == 0) {
				cols = linha.split(";");
				types = new Class<?>[cols.length];
				for(int i = 0; i < cols.length; i++) {
					try {
						types[i] = 	obj.getClass().getMethod("get" +  converterProvider.firstCharUpper(cols[i].trim()), null).getReturnType();
					} catch (NoSuchMethodException e) {
						try {
							types[i] = 	obj.getClass().getMethod("is" + converterProvider.firstCharUpper(cols[i].trim()), null).getReturnType();
						} catch (NoSuchMethodException e1) {
							erros.add("Erro na Linha "+ rowCont + ": Não foi possível localizar a coluna " + cols[i] + " na entidade " +className);
							//throw  new ValidacaoException("Erro na Linha "+ rowCont + ": Não foi possível localizar a coluna " + cols[i] + " na entidade " +className);
						}
					}
				}
				if (erros.size() > 0) {
					appMsgs.getMessages().getInfoList().add("Erros no cabeçalho impedem que o processamento continue:");
					throw  new ValidacaoException(erros);
				}	
			}else {
			
				System.out.println("------------------");
				System.out.println("LINHA: "+ rowCont);
				System.out.println("------------------");
				
				
				String[] values = linha.split(";");
				for (int i=0; i<values.length; i++) {
					
					System.out.print("("+types[i]+") ");
					System.out.print(cols[i]);
					System.out.print("=");
					System.out.print(values[i]);
					System.out.println("; ");
					
					try {
						converterProvider.invokeForMigration(values[i], types[i], cols[i], obj);
					}catch (Exception e) {
						erros.add("Erro na <strong>Linha "+ rowCont + "</strong> [CONVERSAO]: Não foi possível mapear o valor da coluna " + cols[i] + " ( "+values[i]+" : "+types[i].getSimpleName()+") para entidade " +className+ ". Causa: "+ e.getMessage());
						erroNalinha = true;
						//throw  new ValidacaoException("Erro na Linha "+ rowCont + ": Não foi possível mapear o valor da coluna " + cols[i] + " ( "+values[i]+" : "+types[i]+") para entidade " +className+ ". Causa: "+ e.getMessage());
					}
					
				}
				if (!erroNalinha) {
					Persistable<Long> persistable = (Persistable<Long>) obj;
					try {
						persistir(persistable);
					}catch (Exception e) {
						erros.add("Erro na Linha "+ rowCont + "[PERSISTENCIA]: Não foi possível persistir o objeto com o id = "+ ((Migrable<Long>)persistable).getOriginalId() +" para entidade " +className+ ". Causa: "+ e.getCause().getCause().getMessage());
					}
				}
				System.out.println("------------------");
	
			}
			
			rowCont += 1;
		    linha = br.readLine();
		}
		
		System.out.println( file.getName() +" - "+ file.getContentType() );
		
		if (erros.size() > 0) {
			appMsgs.getMessages().getInfoList().add("Erros impediram a migração da entiddade:");
			throw  new ValidacaoException(erros);
		}
	}
	
}
