package com.xyzq.kid.console.action;

import com.xyzq.kid.logic.dateUnviable.service.DateUnviableService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 范例动作
 */
@MaggieAction(path = "kid/console/ticketList")
public class TicketListAction implements IAction {
	/**
	 * Action中只支持Autowired注解引入SpringBean
	 */
	@Autowired
	private DateUnviableService dateUnviableService;


	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		 
		if (dateUnviableService.findBy(String.valueOf(context.parameter("unviableDate"))) == null) {
			context.set("code", "0");
			context.set("msg", "新增不可预约日期成功！");
			context.set("data", JSONObject.convertFromObject(dateUnviableService.insertDateUnviable(String.valueOf(context.parameter("unviableDate")))));
		} else {
			context.set("code", "1");
			context.set("msg", "该日期已经是不可预约日期了！");
		}
		return "success.json";
	}
}
