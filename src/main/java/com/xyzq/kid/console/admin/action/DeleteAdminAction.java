package com.xyzq.kid.console.admin.action;

import com.xyzq.kid.logic.admin.entity.AdminEntity;
import com.xyzq.kid.logic.admin.service.AdminService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除后台管理员
 */
@MaggieAction(path = "kid/console/deleteAdmin")
public class DeleteAdminAction extends AdminAjaxAction {
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
	public String doExecute(Visitor visitor, Context context) throws Exception {
		Integer id = Integer.valueOf(String.valueOf(context.parameter("id")));
		AdminEntity sanmeUserNameEntity = adminService.load(id);
		if (sanmeUserNameEntity == null) {
			context.set("msg", "要删除的记录不存在");
			context.set("code", "-101");
		} else {

			context.set("msg", "删除成功");
			context.set("code", "0");

			adminService.deleteAdmin(id);
		}
		return "success.json";
	}
}
