package com.xyzq.kid.console.record.action;

import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 购买飞行日志
 */
@MaggieAction(path = "kid/console/postRecord")
public class RecordBuyAction extends AdminAjaxAction {
	/**
	 * Action中只支持Autowired注解引入SpringBean
	 */
	@Autowired
	private RecordService recordService;


	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {

		context.set("code", "0");
		if (null != context.parameter("serialNumber")) {
			context.set("data", JSONObject.convertFromObject(recordService.buyRecords(String.valueOf(context.parameter("serialNumber")))));
		} else if (null != context.parameter("id")) {
			Integer id = Integer.valueOf(String.valueOf(context.parameter("id")));
			context.set("data", JSONObject.convertFromObject(recordService.buyRecord(id)));
		} else {
			context.set("msg", "飞行日志礼物购买失败,缺少参数!");
			context.set("code", "-101");
		}
		return "success.json";
	}

}
