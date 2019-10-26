package br.com.dfframeworck.messages;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AppMessages {

	private List<String> successList = new ArrayList<>();
	private List<String> infoList = new ArrayList<>();
	private List<String> warningList = new ArrayList<>();
	private List<String> errorList = new ArrayList<>();
	

	
	public List<String> getSuccessList() {
		return successList;
	}

	public void setSuccessList(List<String> successList) {
		this.successList = successList;
	}

	public List<String> getInfoList() {
		return infoList;
	}
	
	public void setInfoList(List<String> infoList) {
		this.infoList = infoList;
	}
	
	public List<String> getWarningList() {
		return warningList;
	}
	public void setWarningList(List<String> warningList) {
		this.warningList = warningList;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

}
