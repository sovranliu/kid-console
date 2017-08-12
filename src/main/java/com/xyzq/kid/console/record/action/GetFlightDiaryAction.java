package com.xyzq.kid.console.record.action;

import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.simpson.base.json.JSONArray;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.base.json.JSONString;
import com.xyzq.simpson.base.json.core.IJSON;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


/**
 * 根据票圈serialNumber查询查询飞行日志
 */
@MaggieAction(path = "kid/console/getFlightDiary")
public class GetFlightDiaryAction extends AdminAjaxAction {
	/**
	 * 飞行日志上传后下载地址
	 */
	@Value("${KID.UPLOAD.URL.RECORD}")
	private String recordUploadUrl;
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
		String serialNo = String.valueOf(context.parameter("serialNumber"));
		JSONArray jsonArray = JSONArray.convertFromSet(recordService.findBy(serialNo, null));
		for(IJSON item : jsonArray) {
			JSONObject jsonObject = (JSONObject) item;
			jsonObject.put("url", new JSONString(recordUploadUrl + "/" + ((JSONString) jsonObject.get("path")).get()));
		}
		context.set("data", jsonArray);
		return "success.json";
	}
}
