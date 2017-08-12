package com.xyzq.kid.console.message.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyzq.kid.logic.record.common.ConsolePage;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.message.dao.po.Message;
import com.xyzq.kid.logic.message.service.MessageService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path = "kid/console/getMessages")
public class GetMessages extends AdminAjaxAction {

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	Gson gson = new Gson();

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {

		String mobileNo = (String) context.parameter("mobileNo");
		String beginTime = (String) context.parameter("beginTime");
		String endTime = (String) context.parameter("endTime");
		Integer begin = (Integer) context.parameter("begin", 1);
		Integer limit = (Integer) context.parameter("limit", 10);
		UserEntity user = userService.selectByMolieNo(mobileNo);
		Integer userId = null;
		if (user != null) {
			userId = user.id;
		}
		List<Map<String, Object>> mapList = new ArrayList<>();
		Page<Message> msgPage = messageService.queryByCondPage(userId, beginTime, endTime, begin, limit);
		if (msgPage != null && msgPage.getResultList() != null) {
			List<Message> msgList = msgPage.getResultList();
			if (msgList != null && msgList.size() > 0) {
				for (Message msg : msgList) {
					Map<String, Object> map = new HashMap<>();
					UserEntity userEntity = userService.getUserById(msg.getUserid());
					map.put("mobileNo", userEntity.telephone);
					map.put("id", msg.getId());
					map.put("message", msg.getMessage());
					map.put("reply", msg.getAnswer());
					map.put("beginTime", msg.getCreatetime());
					map.put("endTime", msg.getAnswertime());
					mapList.add(map);
				}
			}

			ConsolePage<Map<String, Object>> page = new ConsolePage<Map<String, Object>>();
			page.total = msgPage.getRows();
			page.list = mapList;
			page.begin = begin;
			page.limit = limit;

			context.set("code", "0");
			context.set("data", gson.toJson(page));
		}
		return "success.json";
	}

}
