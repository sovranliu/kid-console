package com.xyzq.kid.console.action;

import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.DemoService;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 范例动作
 */
@MaggieAction(path = "kid/console/findUser")
public class UserAction implements IAction {
    /**
     * Action中只支持Autowired注解引入SpringBean
     */
    @Autowired
    private UserService userService;


    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String execute(Visitor visitor, Context context) throws Exception {
        UserEntity entity = userService.selectByOpenId(String.valueOf(context.parameter("openId")));
        if(entity==null){
            context.set("msg", "记录为空");
            context.set("code", "1");
        }else{
            context.set("msg", "这个是前端需要展示的消息");
            context.set("code", "0");
            context.set("data", JSONObject.convertFromObject(userService.selectByOpenId(String.valueOf(context.parameter("openId")))).toString());
        }
        return "success.json";
    }
}
