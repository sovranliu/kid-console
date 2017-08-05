package com.xyzq.kid.console.action.pay;

import com.xyzq.kid.finance.service.RefundService;
import com.xyzq.kid.finance.service.entity.RefundInfoEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.base.json.JSONArray;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.simpson.base.type.List;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 退款查询动作
 */
@MaggieAction(path = "kid/console/query/refund")
public class RefundQueryAction implements IAction {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(RefundQueryAction.class);
    /**
     * 退款服务
     */
    @Autowired
    private RefundService refundService;
    /**
     * 用户服务
     */
    @Autowired
    protected UserService userService;


    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String execute(Visitor visitor, Context context) throws Exception {
        String orderNo = (String) context.parameter("orderNo");
        String mobileNo = (String) context.parameter("mobileNo");
        String openId = null;
        UserEntity userEntity = userService.selectByMolieNo(mobileNo);
        if(null != userEntity) {
            openId = userEntity.openid;
        }
        int status = (Integer) context.parameter("status", 0);
        DateTime beginTime = null;
        try {
            beginTime = DateTime.parse((String) context.parameter("beginTime"));
        }
        catch (Exception ex) { }
        DateTime endTime = null;
        try {
            endTime = DateTime.parse((String) context.parameter("endTime"));
        }
        catch (Exception ex) { }
        int begin = (Integer) context.parameter("begin", 0);
        int size = (Integer) context.parameter("limit", 10);
        List<RefundInfoEntity> refundInfoEntityList = refundService.find(orderNo, openId, status, beginTime, endTime, begin, size);
        context.set("data", JSONArray.convertFromSet(refundInfoEntityList));
        return "success.json";
    }
}
