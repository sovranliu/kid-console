package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;

@MaggieAction(path="kid/console/bookrepository/init")
public class InitBookRepositoryAction implements IAction {
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		bookRepositoryService.initRepositoryByDate("2017-08-05", 20);
		context.set("code", 0);
		return "success.json";
	}

}