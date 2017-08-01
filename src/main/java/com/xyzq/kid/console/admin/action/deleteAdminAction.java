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
@MaggieAction(path = "kid/console/deleteAdmin")
public class deleteAdminAction implements IAction {
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
			context.set("msg", "要删除的记录不存在");
			context.set("code", "1");
		} else {

			context.set("msg", "删除成功");
			context.set("code", "1");

			adminService.deleteAdmin(id);
		}
		return "success.json";
	}

}
