package com.xyzq.kid.console.action.pay;

import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import com.xyzq.kid.finance.service.RefundService;
import com.xyzq.kid.finance.service.entity.OrderInfoEntity;
import com.xyzq.kid.finance.service.entity.RefundInfoEntity;
import com.xyzq.kid.logic.config.service.GoodsTypeService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.base.json.JSONArray;
import com.xyzq.simpson.base.model.Page;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.simpson.base.type.List;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 退款查询动作
 */
@MaggieAction(path = "kid/console/query/refund")
public class RefundQueryAction extends AdminAjaxAction {
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
     * 票务服务
     */
    @Autowired
    protected TicketService ticketService;
    /**
     * 商品类型服务
     */
    @Autowired
    protected GoodsTypeService goodsTypeService;


    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String doExecute(Visitor visitor, Context context) throws Exception {
        Page<Table<String, Object>> page = new Page<Table<String, Object>>();
        String serialNo = (String) context.parameter("serialNo");
        String mobileNo = (String) context.parameter("mobileNo");
        String orderNo = null;
        if(!Text.isBlank(serialNo)) {
            TicketEntity ticketEntity = ticketService.getTicketsInfoBySerialno(serialNo);
            if(null == ticketEntity) {
                // 票号不存在
                page.list = new List<Table<String, Object>>();
                context.set("data", page);
                return "success.json";
            }
            orderNo = ticketEntity.payNumber;
        }
        String openId = null;
        UserEntity userEntity = userService.selectByMolieNo(mobileNo);
        if(null != userEntity) {
            openId = userEntity.openid;
        }
        Integer status = null;
        if(!Text.isBlank((String) context.parameter("status"))) {
            status = (Integer) context.parameter("status", 0);
        }
        DateTime beginTime = null;
        try {
            if(!Text.isBlank((String) context.parameter("beginTime"))) {
                beginTime = DateTime.parse((String) context.parameter("beginTime") + " 00:00:00");
            }
        }
        catch (Exception ex) { }
        DateTime endTime = null;
        try {
            if(!Text.isBlank((String) context.parameter("endTime"))) {
                endTime = DateTime.parse((String) context.parameter("endTime") + " 23:59:59");
            }
        }
        catch (Exception ex) { }
        int begin = (Integer) context.parameter("begin", 1);
        int size = (Integer) context.parameter("limit", 10);
        begin = size * (begin - 1);
        if(begin < 0) {
            begin = 0;
        }
        Page<RefundInfoEntity> refundInfoEntityPage = refundService.find(orderNo, openId, status, beginTime, endTime, begin, size);
        page.list = new List<Table<String, Object>>();
        for(RefundInfoEntity refundInfoEntity : refundInfoEntityPage.list) {
            Table<String, Object> table = new Table<String, Object>();
            table.put("userName", "" + refundInfoEntity.userName);
            table.put("mobileNo", "" + refundInfoEntity.mobileNo);
            table.put("goodsTypeTitle", "" + goodsTypeService.getGoodsTypeTitle(refundInfoEntity.goodsType));
            table.put("refundNo", "" + refundInfoEntity.refundNo);
            table.put("fee", refundInfoEntity.fee);
            table.put("refundFee", refundInfoEntity.refundFee);
            if(goodsTypeService.isRecord(refundInfoEntity.goodsType)) {
                table.put("serialNo", refundInfoEntity.tag);
            }
            else {
                table.put("serialNo", (null == refundInfoEntity.serialNo)?"":refundInfoEntity.serialNo);
            }
            table.put("time", "" + refundInfoEntity.time.toString());
            table.put("status", refundInfoEntity.status);
            page.list.add(table);
        }
        page.total = refundInfoEntityPage.total;
        context.set("data", page.toString());
        return "success.json";
    }
}
