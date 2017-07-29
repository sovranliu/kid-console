package com.xyzq.kid.console.action;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;

/**
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/admin")
public class AdminAction implements IAction {

    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String execute(Visitor visitor, Context context) throws Exception {
        return "success.json";
    }

}
