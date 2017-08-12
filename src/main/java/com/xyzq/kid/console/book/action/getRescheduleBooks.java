package com.xyzq.kid.console.book.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookChangeRequest;
import com.xyzq.kid.logic.book.service.BookChangeRequestService;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.book.service.BookService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/getRescheduleBooks")
public class getRescheduleBooks extends AdminAjaxAction {
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	Gson gson=new Gson();
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		
		Integer begin=1;
		if(context.parameter("begin")!=null){
			begin=Integer.valueOf((String)context.parameter("begin"));
		}
		Integer limit=10;
		if(context.parameter("limit")!=null){
			limit=Integer.valueOf((String)context.parameter("limit"));
		}
		Map<String,Object> resultMap=new HashMap<>();
		List<Map<String,Object>> mapList=new ArrayList<>();
		Page<BookChangeRequest> page=bookChangeRequestService.queryByCondPage(null, null, null, "1", "1", begin, limit);
		if(page!=null&&page.getResultList()!=null){
			resultMap.put("total", page.getRows());
			List<BookChangeRequest> bcrList=page.getResultList();
			for(BookChangeRequest request:bcrList){
				Map<String,Object> map=new HashMap<>();
				map.put("id", request.getId());
				Integer bookId=request.getBookid();
				Book book=bookService.getBookByPk(bookId);
				if(book!=null){
					Integer ticketId=book.getTicketid();
					Integer userId=book.getUserid();
					UserEntity user=userService.getUserById(userId.intValue());
					if(user!=null){
						String userName=user.userName;
						String mobileNum=user.telephone;
						map.put("userName", userName+"("+mobileNum+")");
					}
					TicketEntity ticket=ticketService.getTicketByPk(ticketId);
					if(ticket!=null){
						String serialNum=ticket.serialNumber;
						map.put("serialNumber", serialNum);
						String expireTime=ticket.expire;
						map.put("expire", expireTime);
					}
					String bookTime=bookRepositoryService.getBookTimeByBookTimeId(book.getBooktimeid());
					map.put("bookTime",bookTime);
					if(request.getBooktimeid()!=null){
						String rescheduleTime=bookRepositoryService.getBookTimeByBookTimeId(request.getBooktimeid());
						map.put("rescheduleTime", rescheduleTime);
					}
					
				}
				Date requestTime=request.getCreatetime();
				map.put("requestTime",sdf.format(requestTime));
				mapList.add(map);
			}
			resultMap.put("list", mapList);
		}
		context.set("code", 0);
		context.set("data", gson.toJson(resultMap));
		return "success.json";
	}

}
