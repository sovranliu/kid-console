package com.xyzq.kid.console.ticket.action;

import com.google.gson.Gson;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.base.json.JSONArray;
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
@MaggieAction(path = "kid/console/getAllRefund")
public class GetAllRefundAction extends AdminAjaxAction {
	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	Gson gson=new Gson();

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(GetAllRefundAction.class);

	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {

		List<TicketRefundEntity> ticketRefundEntityList = ticketService.selectAllRefunding();
		List<Map<String,Object>> mapList=new ArrayList<>();
		if(null != ticketRefundEntityList && ticketRefundEntityList.size() > 0) {
			for (int i = 0; i < ticketRefundEntityList.size(); i++) {
				TicketEntity ticketEntity = ticketService.getTicketByPk(ticketRefundEntityList.get(i).ticketid);
				UserEntity userEntity = userService.selectByMolieNo(ticketEntity.telephone);
				Map<String,Object> map=new HashMap<>();

				map.put("name", userEntity.userName);
				map.put("mobileNo", userEntity.telephone);
				map.put("serialNumber", ticketEntity.serialNumber);
				map.put("expireTime", ticketEntity.expire);
				map.put("price", ticketEntity.price);
				map.put("backPrice", "");
				map.put("createTime", ticketRefundEntityList.get(i).createtime);

				mapList.add(map);
			}
		}

		context.set("code", "0");
		context.set("data", gson.toJson(mapList));
		logger.info("[kid/console/getAllRefund]-out:" + gson.toJson(mapList));

		return "success.json";
	}

}
