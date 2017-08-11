package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;

@MaggieAction(path="kid/console/switchBookRepository")
public class SwitchBookRepository implements IAction {
	
	@Autowired
	BookRepositoryService bookRepositoryService;

	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		Integer bookId=null;
		if(context.parameter("id")!=null){
			bookId=Integer.valueOf((String)context.parameter("id"));
		}
		String switchFlag=null;
		if(context.parameter("type")!=null){//0：打开 1：关闭
			switchFlag=(String)context.parameter("type");
		}
		if(bookId!=null&&!StringUtils.isNullOrEmpty(switchFlag)){
			if(bookRepositoryService.updateStatusById(bookId, switchFlag)){
				context.set("code", 0);
			}
		}else{
			context.set("code", -9);
			context.set("msg", "参数不能为空！");
		}
		return "success.json";
	}

}
