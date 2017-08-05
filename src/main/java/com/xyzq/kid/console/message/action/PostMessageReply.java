package com.xyzq.kid.console.message.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.kid.logic.message.service.MessageService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;

@MaggieAction(path="kid/console/postMessageReply")
public class PostMessageReply implements IAction {
	
	@Autowired
	MessageService messageService;

	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		Integer id=(Integer)context.parameter("id");
		String answer=(String)context.parameter("answer");
		if(id!=null){
			if(messageService.answerMessage(id, null, answer)){
				context.set("code", "0");
				context.set("msg", "回复留言成功");
			}
		}
		return "success.json";
	}

}
