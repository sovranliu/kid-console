package com.xyzq.kid.console.config.action;

import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.simpson.base.json.JSONArray;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查询所有的系统该参数
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/getConfig")
public class GetConfigAction extends AdminAjaxAction {
	@Autowired
	private ConfigService configService;

	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		List<ConfigEntity> configEntityList = configService.find();

		context.set("msg", "查询成功");
		context.set("code", "0");

		context.set("data", JSONArray.convertFromSet(configEntityList));

		return "success.json";
	}

}
