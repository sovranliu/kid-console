package com.xyzq.kid.console.message.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.kid.logic.message.service.MessageService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

//@MaggieAction(path="kid/console/postMessageReply")
public class PostMessageReply extends AdminAjaxAction {
	
	@Autowired
	MessageService messageService;

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
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
