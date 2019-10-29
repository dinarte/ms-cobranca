package br.com.eflux.comum.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.security.Functionality;

/**
 * Usuários que utilizam o sistema. 
 * @author dinarte
 */
@Entity
@Table(schema="comum", name="usuario")
@AutoCrud(name="Usuário", description="Usuários do sistema", funtionality=@Functionality(isPublic=false, name="Usuários", menu="root->Configurações->usuario"))
public class Usuario implements Persistable<Long> {

	@Id
	@GeneratedValue(generator = "userGenerator")
	@GenericGenerator(name = "userGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "comum.usuario_seq") })
	@Column(name = "id_usuario", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@EnableAutoCrudField(label="Nome", enableForFilter=true, enableForList=true, ordinal=1)
	private String name;
	
	@EnableAutoCrudField(label="Email", enableForFilter=true, enableForList=true, ordinal=2)
	private String email;
	
	@EnableAutoCrudField(label="Senha", enableForList=false, ordinal=3)
	private String pass;
	
	@Transient
	@EnableAutoCrudField(label="Confirmacao da Senha", enableForList=false, ordinal=4)
	private String passConfirm;
	
	private String faceId;
	
	private String authToken;
	
	@EnableAutoCrudField(label="Super Usuário")
	private Boolean admin;

	public Usuario() {}
	
	public Usuario(Long id) {
	 this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getFaceId() {
		return faceId;
	}

	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getPassConfirm() {
		return passConfirm;
	}

	public void setPassConfirm(String passConfirm) {
		this.passConfirm = passConfirm;
	}
	
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
}
