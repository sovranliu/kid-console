package com.xyzq.kid.console.config.action;

import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查询所有的系统该参数
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/getConfig")
public class GetConfigAction implements IAction {
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
	public String execute(Visitor visitor, Context context) throws Exception {
		List<ConfigEntity> configEntityList = configService.find();

		context.set("msg", "查询成功");
		context.set("code", "0");
		context.set("data", JSONObject.convertFromObject(configEntityList));

		return "success.json";
	}

}
