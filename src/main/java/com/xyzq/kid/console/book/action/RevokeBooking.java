package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.service.BookChangeRequestService;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.book.service.BookService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
@MaggieAction(path="kid/console/revokeBooking")
public class RevokeBooking extends AdminAjaxAction {
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		String serialNumber=(String)context.parameter("serialNumber");
		TicketEntity ticket=ticketService.getTicketsInfoBySerialno(serialNumber);
		if(ticket!=null){
			Book book=bookService.queryBookRecByTicketId(ticket.id);
			if(book!=null&&isRevokeAble(book)){
				//管理员直接撤销
				if(bookChangeRequestService.createRequest(book.getId(), "2", null, book.getUserid(), null, "2")){
					context.set("code", "0");
				}
			}else{
				context.set("code", "-9");
			}
		}
		return "success.json";
	}
	
	private boolean isRevokeAble(Book book){
		boolean flag=true;
		if(book!=null){
			//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
			if(book.getBookstatus().equals("1")||book.getBookstatus().equals("3")||book.getBookstatus().equals("4")||book.getBookstatus().equals("8")){
				flag=true;
			}
		}
		return flag;
	}

}
