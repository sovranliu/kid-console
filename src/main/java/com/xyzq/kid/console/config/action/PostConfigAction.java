package com.xyzq.kid.console.config.action;

import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 新增系统该参数
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/postConfig")
public class PostConfigAction implements IAction {
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

		String name = String.valueOf(context.parameter("name"));
		String title = String.valueOf(context.parameter("title"));
		String content = String.valueOf(context.parameter("content"));
		String pattern = String.valueOf(context.parameter("pattern"));

		Integer id = configService.addConfig(name, title,content, pattern);
		context.set("msg", "新增成功");
		context.set("code", "0");

		return "success.json";
	}

}
