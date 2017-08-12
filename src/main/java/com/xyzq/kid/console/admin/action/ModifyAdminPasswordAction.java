package com.xyzq.kid.console.admin.action;

import com.xyzq.kid.logic.admin.entity.AdminEntity;
import com.xyzq.kid.logic.admin.service.AdminService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修改密码
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
			context.set("code", "-101");
		} else {
			//不能更改用户名

			context.set("msg", "修改密码成功");
			context.set("code", "0");

			String userName = String.valueOf(context.parameter("userName")) == "null" ? "" : String.valueOf(context.parameter("userName"));
			String password = String.valueOf(context.parameter("password")) == "null" ? "" : String.valueOf(context.parameter("password"));
			String email = String.valueOf(context.parameter("email")) == "null" ? "" : String.valueOf(context.parameter("email"));
			String mobile = String.valueOf(context.parameter("mobile")) == "null" ? "" : String.valueOf(context.parameter("mobile"));

			AdminEntity newEntity = new AdminEntity(id, userName, password, email, mobile);
			adminService.updateAdmin(newEntity);
		}
		return "success.json";
	}
}
