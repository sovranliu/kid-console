package com.xyzq.kid.console.record.action;

import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 保存新增飞行日志
 */
@MaggieAction(path = "kid/console/postFlightDiary")
public class PostFlightDiaryAction implements IAction {
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
	public String execute(Visitor visitor, Context context) throws Exception {

		String serialNumber = String.valueOf(context.parameter("serialNumber"));
		//Path 是数组，使用分号分割。
		String idStr = String.valueOf(context.parameter("idStr"));
		String[] idArray = idStr.split(",");

		if (idArray != null && idArray.length > 0) {
			recordService.saveRecords(serialNumber, idArray);
			context.set("code", "0");
			return "success.json";
		} else {
			context.set("msg", "保存飞行日志,缺少id参数!");
			context.set("code", "-101");
			return "fail.json";
		}

	}

}
