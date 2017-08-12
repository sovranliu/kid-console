package com.xyzq.kid.console.cms.action;

import com.google.gson.Gson;
import com.xyzq.kid.logic.cms.entity.CMSEntity;
import com.xyzq.kid.logic.cms.service.CMSService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取所有退票记录
 * Created by Brann on 17/7/29.
 */
@MaggieAction(path = "kid/console/getMateriel")
public class GetMaterielAction implements IAction {
	@Autowired
	private CMSService cmsService;

	Gson gson=new Gson();

	/**
	 * 日志对象
	 */
	public static Logger logger = LoggerFactory.getLogger(GetMaterielAction.class);
	/**
	 * 动作执行
	 *
	 * @param visitor 访问者
	 * @param context 请求上下文
	 * @return 下一步动作，包括后缀名，null表示结束
	 */
	@Override
	public String execute(Visitor visitor, Context context) throws Exception {

		Integer categoryid = (Integer) context.parameter("type", 0);
		logger.info("[kid/wechat/getMateriel]-in:categoryid[" + categoryid + "]");

		List<Map<String,Object>> mapList=new ArrayList<>();

		List<CMSEntity> cmsEntityList = cmsService.getCMSByCategoryid(categoryid);
		if(null != cmsEntityList && cmsEntityList.size() > 0) {
			for(CMSEntity cmsEntity : cmsEntityList) {
				Map<String, Object> map = new HashMap<>();
				map.put("title", cmsEntity.title);
				map.put("imgUrl", cmsEntity.imageurl);
				map.put("link", cmsEntity.link);
				map.put("updateTime", cmsEntity.updatetime);
				mapList.add(map);
			}
		}

		context.set("data", gson.toJson(mapList));
		logger.info("[kid/console/getMateriel]-out:" + gson.toJson(mapList));

		return "success.json";
	}

//	{
//
//		Integer categoryid = (Integer) context.parameter("categoryid");
//		String title = (String) context.parameter("title");
//		logger.info("[kid/console/getCMSList]-in:categoryid[" + categoryid + "],title[" + title + "]");
//
//		Map<String,Object> resultMap=new HashMap<>();
//		List<Map<String,Object>> mapList=new ArrayList<>();
//
//		Page page = cmsService.getCMSCond(categoryid, title);
//		if(null != page && page.getResultList() != null) {
//			resultMap.put("total", page.getRows());
//			List<CMSEntity> cmsEntityList = page.getResultList();
//			for (CMSEntity cmsEntity : cmsEntityList) {
//				Map<String, Object> map = new HashMap<>();
//				map.put("id", cmsEntity.id);
//				map.put("categoryid", cmsEntity.categoryid);
//				map.put("title", cmsEntity.title);
//				map.put("imageurl", cmsEntity.imageurl);
//				map.put("link", cmsEntity.link);
//				map.put("createtime", cmsEntity.createtime);
//				map.put("updatetime", cmsEntity.updatetime);
//				mapList.add(map);
//			}
//			resultMap.put("list", mapList);
//		}
//
//		context.set("data", gson.toJson(resultMap));
//		logger.info("[kid/console/getTicketList]-out:" + gson.toJson(resultMap));
//
//		return "success.json";
//	}

}
