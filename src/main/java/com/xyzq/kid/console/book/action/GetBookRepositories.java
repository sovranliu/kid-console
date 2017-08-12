package com.xyzq.kid.console.book.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.book.service.BookTimeSpanService;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;

@MaggieAction(path="kid/console/getBookRepositories")
public class GetBookRepositories extends AdminAjaxAction {
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	BookTimeSpanService bookTimeSpanService;
	
	Gson gson=new Gson();

	@Override
	public String doExecute(Visitor visitor, Context context) throws Exception {
		String bookDate=null;
		if(context.parameter("bookDate")!=null){
			bookDate=(String)context.parameter("bookDate");
		}
		List<Map<String,Object>> mapList=new ArrayList<>();
		if(bookDate!=null){
			List<BookTimeRepository> repoList=bookRepositoryService.queryAllByBookDate(bookDate);
			if(repoList!=null){
				for(BookTimeRepository repo:repoList){
					Map<String,Object> map=new HashMap<>();
					map.put("id", repo.getId());
					BookTimeSpan timeSpan=bookTimeSpanService.queryByPrimaryKey(repo.getBooktimespanid());
					if(timeSpan!=null){
						map.put("bookTime", timeSpan.getTimespan());//可预约时间段，9：00-9：30
					}
					if("0".equals(repo.getDeleteflag())){
						map.put("status", "1");//1：可预约，2：打烊
					}else{
						map.put("status", "2");
					}
					if(repo.getBookamount()<=0){
						map.put("bookedDesc", "未预约");
					}else{
						map.put("bookedDesc", "已预约"+repo.getBookamount());
					}
					if(repo.getBooktotal()-repo.getBookamount()>0){
						map.put("bookAbleDesc", "可预约"+(repo.getBooktotal()-repo.getBookamount()));
					}else{
						map.put("bookAbleDesc", "已约满");
					}
					mapList.add(map);
				}
			}
		}
		context.set("code", 0);
		context.set("data", gson.toJson(mapList));
		return "success.json";
	}

}
