package com.xyzq.kid.console.book.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.dateUnviable.entity.DateUnviableEntity;
import com.xyzq.kid.logic.dateUnviable.service.DateUnviableService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/deleteUnviableDate")
public class DeleteUnviableDate extends AdminAjaxAction {
	
	@Autowired
	DateUnviableService dateUnviableService;

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		String date=(String)context.parameter("unviableDate");
		if(StringUtils.isNullOrEmpty(date)){
			DateUnviableEntity entity=dateUnviableService.findBy(date);
			if(entity!=null){
				if(dateUnviableService.deleteDateUnviable(date)>0){
					context.set("code", "0");
					context.set("msg", "删除不可预约日期成功！");
				}
			}else{
				context.set("code", "-9");
				context.set("msg", "该日期不存在");
			}
		}
		
		return "success.json";
	}

}
