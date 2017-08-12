package com.xyzq.kid.console.book.action;

import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.dateUnviable.entity.DateUnviableEntity;
import com.xyzq.kid.logic.dateUnviable.service.DateUnviableService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/addUnviableDate")
public class AddUnviableDate extends AdminAjaxAction {
	
	@Autowired
	DateUnviableService dateUnviableService;

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		String date=(String)context.parameter("unviableDate");
		if(StringUtils.isNullOrEmpty(date)){
			DateUnviableEntity entity=dateUnviableService.findBy(date);
			if(entity==null){
				if(dateUnviableService.insertDateUnviable(date)>0){
					context.set("code", "0");
					context.set("msg", "新增不可预约日期成功！");
				}
			}else{
				context.set("code", "-9");
				context.set("msg", "该日期已不可预约");
			}
		}
		return "success.json";
	}

}
