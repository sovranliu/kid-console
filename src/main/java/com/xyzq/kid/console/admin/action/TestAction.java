package com.xyzq.kid.console.admin.action;

import com.xyzq.kid.logic.admin.service.AdminService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 范例动作
 */
@MaggieAction(path = "kid/console/test")
public class TestAction implements IAction {
    /**
     * 管理员服务
     */
    @Autowired
    protected AdminService adminService;


    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String execute(Visitor visitor, Context context) throws Exception {
        String aId = adminService.saveSession(1);
        visitor.setCookie("aid", aId);
        context.set("msg", "测试连接，管理员ID = 1");
        return "success.json";
    }
}
