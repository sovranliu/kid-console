package com.xyzq.kid.console.ticket.action;

import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 获取所有退票记录
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/refuseRefund")
public class RefuseRefundAction implements IAction {
	@Autowired
	private TicketService ticketService;

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(RefuseRefundAction.class);
	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {

		String serialNumber = (String) context.parameter("serialNumber");
		logger.info("[kid/console/getAllRefund]-in:" + serialNumber);

		TicketEntity ticketEntity = ticketService.getTicketsInfoBySerialno(serialNumber);
		ticketService.refuseRefund(ticketEntity.id);

		return "success.json";
	}

}
