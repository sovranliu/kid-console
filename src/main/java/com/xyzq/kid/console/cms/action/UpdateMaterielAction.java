package com.xyzq.kid.console.cms.action;

import com.xyzq.kid.logic.cms.entity.CMSEntity;
import com.xyzq.kid.logic.cms.service.CMSService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 获取所有退票记录
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/updateMateriel")
public class UpdateMaterielAction extends AdminAjaxAction {
	@Autowired
	private CMSService cmsService;

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(UpdateMaterielAction.class);
	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {

		Integer cmsId = (Integer) context.parameter("id", 0);
		Integer categoryid = (Integer) context.parameter("type", 0);
		String title = (String) context.parameter("title");
		String imageurl = (String) context.parameter("imgurl");
		//TODO
		String link = "test2";
		logger.info("[kid/console/updateMateriel]-in:categoryid[" + categoryid + "],title[" + title + "]");

		CMSEntity cmsEntity = new CMSEntity();
		cmsEntity.id = cmsId;
		cmsEntity.categoryid = categoryid;
		cmsEntity.title = title;
		cmsEntity.imageurl = imageurl;
		cmsEntity.link = link;
		cmsService.updateCMS(cmsEntity);

		return "success.json";
	}
}
