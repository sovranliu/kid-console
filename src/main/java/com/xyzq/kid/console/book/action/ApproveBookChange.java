package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.service.BookChangeRequestService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/approveBookChange")
public class ApproveBookChange extends AdminAjaxAction {
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;

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
			if(bookChangeRequestService.approveRequest(requestId, approveStatus, null, null)){
				context.set("code", 0);
				if(approveStatus.equals("2")){
					context.set("msg", "审批通过");
				}else if(approveStatus.equals("3")){
					context.set("msg", "审批拒绝");
				}
			}else{
				context.set("code", -9);
				context.set("msg", "审批失败");
			}
		}else{
			context.set("code", -9);
			context.set("msg", "参数不能为空！");
		}
		return "success.json";
	}

}
