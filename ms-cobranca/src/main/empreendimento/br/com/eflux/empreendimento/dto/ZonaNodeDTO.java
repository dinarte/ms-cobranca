package br.com.eflux.empreendimento.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.eflux.empreendimento.domain.Zona;

public class ZonaNodeDTO implements TreeNode {
	

	Zona zona;
	
	@JsonIgnore
	ZonaNodeDTO parent;
	
	@JsonIgnore
	List<ZonaNodeDTO> children = new ArrayList<ZonaNodeDTO>();

	
	public ZonaNodeDTO(){
		super();
	}
	
	public ZonaNodeDTO(ZonaNodeDTO parent, Zona zona) {
		this.zona = zona;
		this.parent = parent;
	}
	
	
	@Override
	public String getText() {
		zona = Optional.ofNullable(zona).orElse(new Zona()); 
		return  zona.getNome();
	}
	
	
	@Override
	public List<ZonaNodeDTO> getNodes(){
		return children.size() == 0 ? null : children;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public ZonaNodeDTO getParent() {
		return parent;
	}

	public void setParent(ZonaNodeDTO parent) {
		this.parent = parent;
	}

	public List<ZonaNodeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<ZonaNodeDTO> children) {
		this.children = children;
	}
	
	
	

}
