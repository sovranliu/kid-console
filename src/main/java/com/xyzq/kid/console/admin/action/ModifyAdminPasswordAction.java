package com.xyzq.kid.console.admin.action;

import com.xyzq.kid.logic.admin.entity.AdminEntity;
import com.xyzq.kid.logic.admin.service.AdminService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 新增后台管理员
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/modifyPassword")
public class ModifyAdminPasswordAction implements IAction {
	@Autowired
	private AdminService adminService;

	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {
		Integer id = Integer.valueOf(String.valueOf(context.parameter("id")));
		AdminEntity sanmeUserNameEntity = adminService.load(id);
		if (sanmeUserNameEntity == null) {
			context.set("msg", "更新的记录不存在");
			context.set("code", "1");
		} else {
			//不能更改用户名

			context.set("msg", "新增成功");
			context.set("code", "1");

			String userName = String.valueOf(context.parameter("userName"));
			String password = String.valueOf(context.parameter("password"));
			String email = String.valueOf(context.parameter("email"));
			String mobile = String.valueOf(context.parameter("mobile"));

			AdminEntity newEntity = new AdminEntity(id, userName, password, email, mobile);
			adminService.updateAdmin(newEntity);
		}
		return "success.json";
	}

}
