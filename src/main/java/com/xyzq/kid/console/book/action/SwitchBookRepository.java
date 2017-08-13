package com.xyzq.kid.console.book.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.service.BookChangeRequestService;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.book.service.BookService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.simpson.base.type.core.ITable;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.common.service.SMSService;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/switchBookRepository")
public class SwitchBookRepository extends AdminAjaxAction {
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SMSService smsService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
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
			if(switchFlag.equals("1")){//当天打烊，如果当天有预约记录，则强制取消预约，并短信通知用户
				List<Book> bookList=bookService.queryByBookTimeId(bookId);
				if(bookList!=null&&bookList.size()>0){
					for(Book book:bookList){
						if(bookChangeRequestService.createRequest(book.getId(), "2", null, book.getUserid(), null, "2")){
							UserEntity user=userService.getUserById(book.getUserid());
							if(user!=null){
								String mobileNo=user.telephone;
								String template="book_closed";
								TicketEntity ticket=ticketService.getTicketByPk(book.getTicketid());
								String serialNumber=ticket.serialNumber;
								sms(mobileNo, template, serialNumber);
							}
						}
					}
				}
			}
		}else{
			context.set("code", -9);
			context.set("msg", "参数不能为空！");
		}
		return "success.json";
	}
	
	public void sms(String mobileNo,String template,String serialNumber){
		ITable<String, String> data = new Table<String, String>();
	    data.put("serialNumber", serialNumber);
	    smsService.sendSMS(mobileNo, template, data);
	}

}
