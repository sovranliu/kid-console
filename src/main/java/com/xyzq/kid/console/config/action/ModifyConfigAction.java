package com.xyzq.kid.console.config.action;

import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 新增系统该参数
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/modifyConfig")
public class ModifyConfigAction extends AdminAjaxAction {
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

		String name = String.valueOf(context.parameter("name")) == "null" ? "" : String.valueOf(context.parameter("name"));
		String content = String.valueOf(context.parameter("content")) == "null" ? "" : String.valueOf(context.parameter("content"));

		ConfigEntity entity = configService.load(name);
		if (entity == null) {
			context.set("msg", "修改失败，记录不存在！");
			context.set("code", "-100");
			return "success.json";
		}
		if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(entity.pattern)) {
			Pattern regPattern = Pattern.compile(entity.pattern);
			Matcher matcher = regPattern.matcher(content);
			if (matcher.matches()) {
				configService.alter(entity.name, content);
				context.set("msg", "修改成功！");
				context.set("code", "0");
			} else {
				context.set("msg", "修改的参数不满足规则！");
				context.set("code", "-101");
			}
		} else {
			configService.alter(entity.name, content);
			context.set("msg", "修改成功！");
			context.set("code", "0");
		}

		return "success.json";
	}

}
