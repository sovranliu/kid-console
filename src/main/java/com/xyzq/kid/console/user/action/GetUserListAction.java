package com.xyzq.kid.console.user.action;

import com.google.gson.Gson;
import com.xyzq.kid.logic.Page;
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
@MaggieAction(path = "kid/console/getUserList")
public class GetUserListAction implements IAction {
	@Autowired
	private UserService userService;

	Gson gson=new Gson();

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(GetUserListAction.class);

	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {

		String userName = (String) context.parameter("userName");
		String mobileNo = (String) context.parameter("telephone");
		Integer begin = (Integer) context.parameter("begin", 1);
		Integer limit = (Integer) context.parameter("limit", 15);

		logger.info("[kid/console/getTicketList]-in:mobileNo-" + mobileNo +
				",begin-" + begin +
				",limit-" + limit);

		Map<String,Object> resultMap=new HashMap<>();
		List<Map<String,Object>> mapList=new ArrayList<>();
		Page page = userService.getUserList(userName, mobileNo, begin, limit);
		if(null != page && page.getResultList() != null) {
			resultMap.put("total", page.getRows());
			List<UserEntity> userEntityList = page.getResultList();
			for(UserEntity userEntity : userEntityList) {
				Map<String,Object> map = new HashMap<>();
				map.put("userName", userEntity.userName);
				map.put("telephone", userEntity.telephone);
				if(userEntity.sex == UserEntity.USER_MALE) {
					map.put("sex", "男");
				} else {
					map.put("sex", "女");
				}
				map.put("address", userEntity.address);
				map.put("createTime", userEntity.createtime);
				mapList.add(map);
			}
			resultMap.put("list", mapList);
		}

		context.set("data", gson.toJson(resultMap));
		logger.info("[kid/console/getTicketList]-out:" + gson.toJson(resultMap));

		return "success.json";
	}

}
