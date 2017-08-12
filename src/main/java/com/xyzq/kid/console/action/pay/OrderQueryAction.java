package com.xyzq.kid.console.action.pay;

import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import com.xyzq.kid.finance.service.OrderService;
import com.xyzq.kid.finance.service.entity.OrderInfoEntity;
import com.xyzq.kid.logic.config.service.GoodsTypeService;
import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
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
 * 支付订单查询动作
 */
@MaggieAction(path = "kid/console/query/order")
public class OrderQueryAction extends AdminAjaxAction {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(OrderQueryAction.class);
    /**
     * 支付订单服务
     */
    @Autowired
    private OrderService orderService;
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
     * 飞行日志服务
     */
    @Autowired
    protected RecordService recordService;


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
        String mobileNo = (String) context.parameter("mobileNo");
        String serialNo = (String) context.parameter("serialNo");
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
        if(null != mobileNo) {
            UserEntity userEntity = userService.selectByMolieNo(mobileNo);
            if(null != userEntity) {
                openId = userEntity.openid;
            }
        }
        Integer status = null;
        if(!Text.isBlank((String) context.parameter("status"))) {
            status = (Integer) context.parameter("status");
        }
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
        int begin = (Integer) context.parameter("begin", 1);
        int size = (Integer) context.parameter("limit", 10);
        begin = size * (begin - 1);
        if(begin < 0) {
            begin = 0;
        }
        Page<OrderInfoEntity> orderInfoEntityPage = orderService.find(orderNo, openId, status, beginTime, endTime, begin, size);
        page.list = new List<Table<String, Object>>();
        for(OrderInfoEntity orderInfoEntity : orderInfoEntityPage.list) {
            Table<String, Object> table = new Table<String, Object>();
            table.put("userName", "" + orderInfoEntity.userName);
            table.put("mobileNo", "" + orderInfoEntity.mobileNo);
            table.put("goodsTypeTitle", "" + goodsTypeService.getGoodsTypeTitle(orderInfoEntity.goodsType));
            table.put("orderNo", "" + orderInfoEntity.orderNo);
            table.put("fee", orderInfoEntity.fee);
            if(goodsTypeService.isRecord(orderInfoEntity.goodsType)) {
                table.put("serialNo", orderInfoEntity.tag);
            }
            else {
                table.put("serialNo", (null == orderInfoEntity.serialNo)?"":orderInfoEntity.serialNo);
            }
            table.put("status", orderInfoEntity.status);
            table.put("time", orderInfoEntity.time.toString());
            page.list.add(table);
        }
        page.total = orderInfoEntityPage.total;
        context.set("data", page.toString());
        return "success.json";
    }
}
