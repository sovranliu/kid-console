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
import com.xyzq.simpson.maggie.framework.action.core.IAction;
@MaggieAction(path="kid/console/revokeBooking")
public class RevokeBooking implements IAction{
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;

	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		String serialNumber=(String)context.parameter("serialNumber");
		TicketEntity ticket=ticketService.getTicketsInfoBySerialno(serialNumber);
		if(ticket!=null){
			Book book=bookService.queryBookRecByTicketId(ticket.id);
			if(book!=null){
				//管理员直接撤销
				if(bookChangeRequestService.createRequest(book.getId(), "2", null, book.getUserid(), null, "2")){
					context.set("code", "0");
				}
			}
		}
		return "success.json";
	}

}
