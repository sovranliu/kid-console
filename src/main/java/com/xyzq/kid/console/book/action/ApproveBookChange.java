package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.common.service.SMSService;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookChangeRequest;
import com.xyzq.kid.logic.book.service.BookChangeRequestService;
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

@MaggieAction(path="kid/console/approveBookChange")
public class ApproveBookChange extends AdminAjaxAction {
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SMSService smsService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	TicketService ticketService;

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		Integer requestId=null;
		if(context.parameter("id")!=null){
			requestId=Integer.valueOf((String)context.parameter("id"));
		}
		String approveStatus=null;
		//type:审批结果：0：拒绝，1：通过
		if(context.parameter("type")!=null){
			approveStatus=(String)context.parameter("type");
			if("0".equals(approveStatus)){
				approveStatus="3";//1:申请中，2：通过，3：拒绝
			}else if("1".equals(approveStatus)){
				approveStatus="2";
			}
		}
		if(requestId!=null&&!StringUtils.isNullOrEmpty(approveStatus)){
			BookChangeRequest request=bookChangeRequestService.selectByPk(requestId);
			if(request!=null){
				Book book=bookService.getBookByPk(request.getBookid());
				TicketEntity ticket=ticketService.getTicketByPk(book.getTicketid());
				String serialNumber=ticket.serialNumber;
				UserEntity user=userService.getUserById(book.getUserid());
				String mobileNo=user.telephone;
				if(bookChangeRequestService.approveRequest(requestId, approveStatus, null, null)){
					context.set("code", 0);
					if(approveStatus.equals("2")){
						context.set("msg", "审批通过");
						//1：改期，2：撤销
						if(request.getReqesttype().equals("1")){
							sms(mobileNo,"change_success",serialNumber);
						}else{
							sms(mobileNo,"book_success",serialNumber);
						}
					}else if(approveStatus.equals("3")){
						context.set("msg", "审批拒绝");
						//1：改期，2：撤销
						if(request.getReqesttype().equals("1")){
							sms(mobileNo,"change_fail",serialNumber);
						}else{
							sms(mobileNo,"book_fail",serialNumber);
						}
					}
				}else{
					context.set("code", -9);
					context.set("msg", "审批失败");
				}
			}else{
				context.set("code", -9);
				context.set("msg", "找不到申请记录");
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
