package com.xyzq.kid.console.ticket.action;

import com.google.gson.Gson;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
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
@MaggieAction(path = "kid/console/getTicketList")
public class GetTicketListAction implements IAction {
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserService userService;

	Gson gson=new Gson();

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(GetTicketListAction.class);

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
		String mobileNo = (String) context.parameter("telephone");
		String startTime = (String) context.parameter("startTime");
		String endTime = (String) context.parameter("endTime");
		Integer status = (Integer) context.parameter("status");
		Integer begin = (Integer) context.parameter("begin", 0);
		Integer limit = (Integer) context.parameter("limit", 15);

		logger.info("[kid/console/getTicketList]-in:serialNumber-" + serialNumber +
				",mobileNo-" + mobileNo +
				",startTime-" + startTime +
				",endTime-" + endTime +
				",status-" + status +
				",begin-" + begin +
				",limit-" + limit);

		Map<String,Object> resultMap=new HashMap<>();
		List<Map<String,Object>> mapList=new ArrayList<>();
		Page page = ticketService.queryTicketByCond(
				serialNumber,
				mobileNo,
				startTime,
				endTime,
				status,
				begin,
				limit);
		if(null != page && page.getResultList() != null) {
			resultMap.put("total", page.getRows());
			List<TicketEntity> ticketEntityList = page.getResultList();
			for(TicketEntity ticketEntity : ticketEntityList) {
				Map<String,Object> map = new HashMap<>();
				map.put("serialNumber", ticketEntity.serialNumber);
				UserEntity userEntity = userService.selectByMolieNo(ticketEntity.telephone);
				map.put("userName", userEntity.userName);
				map.put("telephone", ticketEntity.telephone);
				map.put("price", ticketEntity.price);
				if(ticketEntity.type == TicketEntity.TICKET_TYPE_GROUP) {
					map.put("type", "团体票");
				} else if(ticketEntity.type == TicketEntity.TICKET_TYPE_SINGLE) {
					map.put("type", "个人票");
				}
				if(ticketEntity.status == TicketEntity.TICKET_STATUS_NEW) {
					map.put("status", "可用");
				} else if(ticketEntity.status == TicketEntity.TICKET_STATUS_USED) {
					map.put("status", "已用");
				} else if(ticketEntity.status == TicketEntity.TICKET_STATUS_EXPIRED) {
					map.put("status", "过期");
				} else if(ticketEntity.status == TicketEntity.TICKET_STATUS_BACKING) {
					map.put("status", "退票申请中");
				} else if(ticketEntity.status == TicketEntity.TICKET_STATUS_BACK) {
					map.put("status", "已退票");
				}

				map.put("status", ticketEntity.status);
				map.put("payNumber", ticketEntity.price);
				map.put("createTime", ticketEntity.createtime);
				map.put("expireTime", ticketEntity.expire);

				mapList.add(map);
			}
			resultMap.put("list", mapList);
		}

		context.set("data", gson.toJson(resultMap));
		logger.info("[kid/console/getTicketList]-out:" + gson.toJson(resultMap));

		return "success.json";
	}

}
