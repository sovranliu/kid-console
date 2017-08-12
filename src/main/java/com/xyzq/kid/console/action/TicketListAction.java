package com.xyzq.kid.console.action;

import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 范例动作
 */
@MaggieAction(path = "kid/console/ticketList")
public class TicketListAction extends AdminAjaxAction {
	/**
	 * Action中只支持Autowired注解引入SpringBean
	 */
	@Autowired
	private TicketService ticketService;


	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		return "success.json";
	}
}
