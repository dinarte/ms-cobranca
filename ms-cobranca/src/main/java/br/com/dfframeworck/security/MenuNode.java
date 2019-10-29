package br.com.dfframeworck.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component("menuRootNode")
@RequestScope
public class MenuNode {
	
	private MenuItem item;
	
	private MenuNode parent;
	
	private List<MenuNode> children = new ArrayList<MenuNode>();
	
	private String hierarchy = "root";


	/**
	 * Adicona um item na hierarquia do menu.
	 * @param item
	 * @param hierarchy
	 * @return
	 */
	public  MenuNode add(MenuItem item,String hierarchy) {
		
		MenuNode node = this;
		String[] hierarchyLevels = hierarchy.split("->");
		
		String hierarchySearched = hierarchyLevels[0];
		for (String level : hierarchyLevels) {
			if (!level.equals(hierarchyLevels[0])) {
				hierarchySearched = hierarchySearched + "->" + level;
				//System.out.println(hierarchySearched);
				
				MenuNode newNode = new MenuNode();
				newNode.setHierarchy(hierarchySearched); 
				newNode.setParent(node);
				
				if (node.getChildren().contains(newNode)) {
					final String hierarchyFound = hierarchySearched;
					newNode = node.getChildren().stream().filter(i -> i.getHierarchy().equals(hierarchyFound)).findFirst().get();
					newNode.setItem(new MenuItem());
					newNode.getItem().setIcon("fa fa-folder");
					newNode.getItem().setPath("#");
					newNode.getItem().setName(hierarchyFound.split("->")[hierarchyFound.split("->").length-1]);
				}else {
					node.getChildren().add(newNode);
				}
			 node = newNode;	
			}
		}
		node.setItem(item);
		return node;
	}
	
	public static void main(String[] args) {
		
		MenuNode root = new MenuNode();
		MenuNode newnode = root.add(new MenuItem(),"root->empreendimento->new");
		MenuNode newnode2 = root.add(new MenuItem(),"root->empreendimento->add");
		root.add(new MenuItem(),"root->empreendimento->relatorios->rel1");
		root.add(new MenuItem(),"root->empreendimento->relatorios->rel2");
		root.add(new MenuItem(),"root->empreendimento->relatorios->rel3");
		
		System.out.println("-----------------------------------------------");
		
		root.getChildren().forEach(node-> {
			
			System.out.println(node.getHierarchy());
			
			node.getChildren().forEach(node2-> {
				System.out.println(">>>>>>>" + node2.getHierarchy());
				node2.getChildren().forEach(node3-> {
					System.out.println(">>>>>>>>>>>>" + node3.getHierarchy());
				});
			});
		});
	}
	
	
	public MenuItem getItem() {
		return item;
	}

	public void setItem(MenuItem item) {
		this.item = item;
	}

	public MenuNode getParent() {
		return parent;
	}

	public void setParent(MenuNode parent) {
		this.parent = parent;
	}

	public List<MenuNode> getChildren() {
		return children;
	}

	public void setChildren(List<MenuNode> children) {
		this.children = children;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.hierarchy.equals((((MenuNode)obj).getHierarchy()));
	}

}
