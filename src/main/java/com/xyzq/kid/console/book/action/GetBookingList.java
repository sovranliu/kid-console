package com.xyzq.kid.console.book.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.service.BookService;
import com.xyzq.kid.logic.book.service.BookTimeSpanService;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;

@MaggieAction(path="kid/console/getBookingList")
public class GetBookingList implements IAction {
	
	@Autowired
	BookService bookService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	BookTimeSpanService bookTimeSpanService;
	
	Gson gson=new Gson();

	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		
		String mobileNo=(String)context.parameter("telephone");
		String ticketSerialNo=(String)context.parameter("serialNumber");
		String startDate=(String)context.parameter("startTime");
		String endDate=(String)context.parameter("endTime");
		String status=(String)context.parameter("status");
		String begin=(String)context.parameter("begin");
		String limit=(String)context.parameter("limit");
		
		Page<Book> bookPage=bookService.queryByCondPage(mobileNo, ticketSerialNo, startDate, endDate, status, Integer.valueOf(begin), Integer.valueOf(limit));
		List<Map<String,String>> mapList=new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		if(bookPage!=null&&bookPage.getResultList()!=null&&bookPage.getResultList().size()>0){
			List<Book> bookList=bookPage.getResultList();
			for(Book book:bookList){
				UserEntity user=userService.getUserById(book.getUserid());
				String userName=user.realname;
				map.put("name", userName);
				map.put("serialNumber", ticketSerialNo);
				map.put("telephone", mobileNo);
				map.put("status", status);
				map.put("time", book.getBookdate()+" "+book.getBooktime());
				mapList.add(map);
			}
			context.set("code", 0);
			context.set("data", gson.toJson(mapList));
		}
		return "success.json";
	}
	
	
}
