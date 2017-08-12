package com.xyzq.kid.console.message.action;

import com.xyzq.kid.logic.admin.entity.AdminEntity;
import com.xyzq.kid.logic.admin.service.AdminService;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.kid.logic.message.service.MessageService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 回复留言
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/postMessageReply")
public class PostMessageAction extends AdminAjaxAction {
	@Autowired
	private MessageService messageService;
	@Autowired
	private AdminService adminService;
	/**
	 * 上下文中的键
	 */
	public final static String CONTEXT_KEY_AID = "aid";
	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		Integer id = Integer.valueOf(String.valueOf(context.parameter("id")));
		String answerContent = String.valueOf(context.parameter("content"));
		Integer adminId = Integer.valueOf(String.valueOf(context.get(CONTEXT_KEY_AID)));

		AdminEntity adminEntity = adminService.load(adminId);

		messageService.modifiedAnswer(id, adminEntity.userName,answerContent);
		context.set("msg", "回复成功");
		context.set("code", "0");

		return "success.json";
	}

}
