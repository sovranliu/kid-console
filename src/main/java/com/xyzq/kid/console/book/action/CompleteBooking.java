package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.service.BookService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;

@MaggieAction(path="kid/console/completeBooking")
public class CompleteBooking extends AdminAjaxAction {

	@Autowired
	BookService bookService;
	
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		Integer bookId=null;
		if(context.parameter("id")!=null){
			bookId=Integer.parseInt((String)context.parameter("id"));
		}
		if(bookId!=null){
			Book book=bookService.getBookByPk(bookId);
			if(book!=null){
				//检查预约单状态，改期申请中、撤销申请中、撤销通过、核销完成状态的预约单不能核销
				//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
				String status=book.getBookstatus();
				if("2".equals(status)||"5".equals(status)||"6".equals(status)||"7".equals(status)){
					context.set("code", -9);
					context.set("msg", "当前状态不允许核销");
				}else{
					//status 1:改期，2：撤销，3：核销
					if(bookService.updateBookStatus(bookId, "3", "管理员核销", null)){
						context.set("code", 0);
					}else{
						context.set("code", -9);
						context.set("msg", "核销失败");
					}
				}
			}
		}else{
			context.set("code", -9);
			context.set("msg", "参数异常，找不到预约信息");
		}
		return "success.json";
	}

}
