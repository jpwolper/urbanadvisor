package com.techelevator.citymap.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// import com.techelevator.citymap.model.Message;
// import com.techelevator.citymap.model.MessageDAO;

/*@Controller
public class MessageController {
	
	private MessageDAO messageDAO;

	@Autowired
	public MessageController(MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	@RequestMapping(path="/messages", method=RequestMethod.GET)
	public String messageSearch(Map<String, Object> model, 
								@RequestParam String userName) {
		
		model.put("messages", messageDAO.searchByUsername(userName));
		return "messages";
	}
	
	@RequestMapping(path="/users/{userName}/messages/new", method=RequestMethod.GET)
	public String displayMessageForm() {
		return "newMessage";
	}
	
	@RequestMapping(path="/users/{userName}/messages", method=RequestMethod.POST)
	public String createNewMessage(@PathVariable String userName,
								   @RequestParam String visibility, 
								   @RequestParam(required=false) String messageTo, 
								   @RequestParam String messageText) {
		
		Message message = new Message();
		message.setFromUsername(userName);
		message.setText(messageText);
		message.setCreateTime(LocalDateTime.now());
		if("private".equals(visibility)) {
			message.setPrivate(true);
			message.setToUsername(messageTo);
		}
		messageDAO.saveMessage(message);
		
		return "redirect:/users/"+userName+"/messages";
}

	/*@RequestMapping(path="/users/{userName}", method=RequestMethod.GET)
	public String displayDashboard(ModelMap model, @PathVariable String userName) {
	//	model.put("conversations", messageDAO.getConversationsForUser(userName));
		return "userDash";
	}
	
	@RequestMapping(path="/users/{userName}/messages", method=RequestMethod.GET)
	public String displaySentMessages(Map<String, Object> model, @PathVariable String userName) {
	//	model.put("messages", messageDAO.getMessagesSentByUser(userName));		
		return "sentMessages";
	}
	} */
