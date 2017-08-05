package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;
import com.xyzq.kid.logic.book.service.BookChangeRequestService;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.book.service.BookService;
import com.xyzq.kid.logic.book.service.BookTimeSpanService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;

@MaggieAction(path="kid/console/rescheduleBooking")
public class RescheduleBooking implements IAction {

	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BookTimeSpanService bookTimeSpanService;
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;
	
	@Autowired
	BookService bookService;
	
	
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		String serialNumber=(String)context.parameter("serialNumber");
		String year=(String)context.parameter("year");
		String month=(String)context.parameter("month");
		String day=(String)context.parameter("day");
		String start=(String)context.parameter("start");
		String end=(String)context.parameter("end");
		TicketEntity ticket=ticketService.getTicketsInfoBySerialno(serialNumber);
		if(!StringUtils.isNullOrEmpty(year)&&!StringUtils.isNullOrEmpty(month)&&!StringUtils.isNullOrEmpty(day)){
			String bookDate=year+"-"+month+"-"+day;
			String timeSpan=start+"-"+end;
			BookTimeSpan bs=bookTimeSpanService.queryByTimeSpan(timeSpan);
			if(bs!=null){
				BookTimeRepository repo=bookRepositoryService.queryRepositoryByDateAndTimeSpan(bookDate, bs.getId());
				Book book=bookService.queryBookRecByTicketId(ticket.id);
				if(book!=null&&isRescheduleAble(book)){
					if(bookChangeRequestService.createRequest(book.getId(), "1", null, book.getUserid(), repo.getId(),"2")){
						context.set("code", "0");
					}
				}
			}
		}
		return "success.json";
	}
	private boolean isRescheduleAble(Book book){
		boolean flag=false;
		if(book!=null){
			//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
			if(book.getBookstatus().equals("1")||book.getBookstatus().equals("3")||book.getBookstatus().equals("4")||book.getBookstatus().equals("8")){
				flag=true;
			}
		}
		return flag;
	}

}
