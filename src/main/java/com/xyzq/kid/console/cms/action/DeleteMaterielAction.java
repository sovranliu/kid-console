package com.xyzq.kid.console.cms.action;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.cms.entity.CMSEntity;
import com.xyzq.kid.logic.cms.service.CMSService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 获取所有退票记录
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/deleteMateriel")
public class DeleteMaterielAction implements IAction {
	@Autowired
	private CMSService cmsService;

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(DeleteMaterielAction.class);
	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {

		Integer cmsId = (Integer) context.parameter("id", 0);

		logger.info("[kid/console/deleteMateriel]-in:categoryid[" + cmsId + "]");

		CMSEntity cmsEntity = new CMSEntity();
		cmsEntity.id = cmsId;
		cmsEntity.deleted = CommonTool.STATUS_DELETE;
		cmsService.updateCMS(cmsEntity);

		return "success.json";
	}

}
