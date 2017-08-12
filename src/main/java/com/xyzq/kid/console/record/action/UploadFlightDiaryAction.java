package com.xyzq.kid.console.record.action;

import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;


/**
 * 上传飞行日志
 */
@MaggieAction(path = "kid/console/uploadFlightDiary")
public class UploadFlightDiaryAction extends AdminAjaxAction {
	/**
	 * 飞行日志服务
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
		File file = (File) context.parameter("file");
		if(null == file || !file.exists()) {
			context.set("msg", "飞行日志上传失败");
			return "fail.json";
		}
		context.set("data", JSONObject.convertFromTable(recordService.uploadFile(file)));
		return "success.json";
	}
}
