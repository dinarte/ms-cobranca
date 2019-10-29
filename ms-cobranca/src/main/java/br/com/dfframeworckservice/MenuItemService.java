package br.com.dfframeworckservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.dfframeworck.security.MenuItem;

@Service
public class MenuItemService {
	
	
	List<MenuItem> menuItens = new ArrayList<MenuItem>();

	public List<MenuItem> getMenuItens() {
		return menuItens;
	}

	public void setMenuItens(List<MenuItem> menuItens) {
		this.menuItens = menuItens;
	}
	

}
