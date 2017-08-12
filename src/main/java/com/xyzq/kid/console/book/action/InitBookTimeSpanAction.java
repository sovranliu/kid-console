package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.service.BookTimeSpanService;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/timeSpanInit")
public class InitBookTimeSpanAction extends AdminAjaxAction {
	
	@Autowired
	BookTimeSpanService bookTimeSpanService;	
	
	@Autowired
	ConfigService configService;
	
	static final String BOOK_TIME_START="book_time_start";
	
	static final String BOOK_TIME_END="book_time_end";
	
	static final String BOOK_TIME_INTERVAL="book_interval_mins";
	
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		String start=configService.fetch(BOOK_TIME_START);
		if(StringUtils.isNullOrEmpty(start)){
			start="9";
		}
		String end=configService.fetch(BOOK_TIME_END);
		if(StringUtils.isNullOrEmpty(end)){
			end="15";
		}
		String interval=configService.fetch(BOOK_TIME_INTERVAL);
		if(StringUtils.isNullOrEmpty(interval)){
			interval="30";
		}
		if(bookTimeSpanService.initTimeSpan(Integer.valueOf(start), Integer.valueOf(end), Integer.valueOf(interval))){
			context.set("code", 0);
		}
		return "success.json";
	}

}
