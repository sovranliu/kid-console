package com.xyzq.kid.console.admin.action;

import com.xyzq.kid.logic.admin.service.AdminService;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 登录管理员基类
 */
public abstract class AdminAjaxAction implements IAction {
    /**
     * 上下文中的键
     */
    public final static String CONTEXT_KEY_AID = "aid";

    /**
     * 站点域名
     */
    @Value("${KID.CONSOLE.URL_DOMAIN}")
    public String url_domain;
    @Value("${KID.CONSOLE.URL_PAGE_LOGIN}")
    public String url_page_login;
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
        int adminId = 0;
        String aId = visitor.cookie("aid");
        if(!Text.isBlank(aId)) {
            Integer id = adminService.fetchAdminId(aId);
            if(null == id) {
                visitor.setCookie("aid", null);
            }
            else {
                adminId = id;
                context.put(CONTEXT_KEY_AID, adminId);
            }
        }
        if(0 == adminId) {
            context.set("redirect", url_page_login);
            return "fail.json";
        }
        return doExecute(visitor, context);
    }

    /**
     * 派生类动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    public abstract String doExecute(Visitor visitor, Context context) throws Exception;
}
