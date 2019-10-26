package br.com.dfframeworck.messages;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class AppFlashMessages{
	
	private AppMessages messages = new AppMessages();

	public AppMessages getMessages() {
		return messages;
	}

	public void setMessages(AppMessages messages) {
		this.messages = messages;
	}

}