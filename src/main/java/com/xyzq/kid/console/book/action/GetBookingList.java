package com.xyzq.kid.console.book.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
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
		
		String mobileNo=null;
		if(context.parameter("telephone")!=null){
			mobileNo=(String)context.parameter("telephone");
		}
		
		String ticketSerialNo=null;
		if(context.parameter("serialNumber")!=null){
			ticketSerialNo=(String)context.parameter("serialNumber");
		}
		String startDate=null;
		if(context.parameter("startTime")!=null){
			startDate=(String)context.parameter("startTime");
		}
		String endDate=null;
		if(context.parameter("endTime")!=null){
			endDate=(String)context.parameter("endTime");
		}
		String status=null;
		if(context.parameter("status")!=null){
			status=(String)context.parameter("status");
		}
		String begin="1";
		if(context.parameter("begin")!=null){
			begin=(String)context.parameter("begin");
		}
		String limit="5";
		if(context.parameter("limit")!=null){
			limit=(String)context.parameter("limit");
		}
		
		Page<Book> bookPage=bookService.queryByCondPage(mobileNo, ticketSerialNo, startDate, endDate, status, Integer.valueOf(begin), Integer.valueOf(limit));
		Map<String,Object> resultMap=new HashMap<>();
		List<Map<String,String>> mapList=new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		if(bookPage!=null&&bookPage.getResultList()!=null&&bookPage.getResultList().size()>0){
			resultMap.put("total", bookPage.getRows());
			List<Book> bookList=bookPage.getResultList();
			for(Book book:bookList){
				UserEntity user=userService.getUserById(book.getUserid());
				if(user!=null){
					String userName=user.telephone;
					map.put("name", userName);
				}
				map.put("serialNumber", ticketSerialNo);
				map.put("telephone", mobileNo);
				map.put("status", statusTransfer(status));
				map.put("time", book.getBookdate()+" "+book.getBooktime());
				mapList.add(map);
			}
			resultMap.put("list", mapList);
			context.set("code", 0);
			context.set("data", gson.toJson(resultMap));
		}
		return "success.json";
	}
	
	public String statusTransfer(String status){
		//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
		String desc=status;
		if(!StringUtils.isNullOrEmpty(status)){
			if(status.equals("1")){
				desc="已预约";
			}
			if(status.equals("2")){
				desc="改期申请中";
			}
			if(status.equals("3")){
				desc="改期通过";
			}
			if(status.equals("4")){
				desc="改期拒绝";
			}
			if(status.equals("5")){
				desc="核销完成";
			}
			if(status.equals("6")){
				desc="撤销申请中";
			}
			if(status.equals("7")){
				desc="撤销通过";
			}
			if(status.equals("8")){
				desc="拒绝撤销";
			}
		}
		return desc;
	}
	
}
