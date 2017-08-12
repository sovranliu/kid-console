package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/bookRepoInit")
public class InitBookRepositoryAction extends AdminAjaxAction {
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	ConfigService configService;
	
	static final String BOOK_REPOSITORY_COUNT="book_repository_count";
	
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		String repoCount=configService.fetch(BOOK_REPOSITORY_COUNT);
		String bookDate=(String)context.parameter("bookDate");
		if(StringUtils.isNullOrEmpty(repoCount)){
			repoCount="5";
		}
		if(bookRepositoryService.initRepositoryByDate(bookDate, Integer.valueOf(repoCount))){
			context.set("code", 0);
		}
		return "success.json";
	}

}
