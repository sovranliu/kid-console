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
@MaggieAction(path = "kid/console/addAdmin")
public class AddAdminAction implements IAction {
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
		AdminEntity sanmeUserNameEntity = adminService.findByUserName(String.valueOf(context.parameter("userName")));
		if (sanmeUserNameEntity == null) {
			context.set("msg", "新增成功");
			context.set("code", "0");

			String userName = String.valueOf(context.parameter("userName"));
			String password = String.valueOf(context.parameter("password"));
			String email = String.valueOf(context.parameter("email"));
			String mobile = String.valueOf(context.parameter("mobile"));

			AdminEntity newEntity = new AdminEntity(null,userName, password, email, mobile);
			adminService.addAdmin(newEntity);
		} else {
			context.set("msg", "新建失败，已经存在相同userName的记录");
			context.set("code", "-101");
		}
		return "success.json";
	}

}
