package com.xyzq.kid.console.ticket.action;

import com.google.gson.Gson;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取所有退票记录
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/accessRefund")
public class AccessRefundAction extends AdminAjaxAction {
	@Autowired
	private TicketService ticketService;

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(AccessRefundAction.class);
	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {

		String serialNumber = (String) context.parameter("serialNumber");
		logger.info("[kid/console/accessRefund]-in:" + serialNumber);

		TicketEntity ticketEntity = ticketService.getTicketsInfoBySerialno(serialNumber);
		boolean result = ticketService.accessRefund(ticketEntity.id);

		if(result) {
			context.set("code", "0");
			context.set("data", "退款成功！");
			logger.info("[kid/console/getAllRefund]-out:退款成功！");
		} else {
			context.set("code", "-1");
			context.set("data", "退款失败！");
			logger.info("[kid/console/getAllRefund]-out:退款失败！");
		}

		return "success.json";
	}

}
