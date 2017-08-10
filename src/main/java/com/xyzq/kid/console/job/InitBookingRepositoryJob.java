package com.xyzq.kid.console.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.config.service.ConfigService;

public class InitBookingRepositoryJob extends QuartzJobBean{
	

	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	ConfigService configService;
	
	static final String BOOK_REPOSITORY_COUNT="book_repository_count";
	static final String BOOK_MAX_DAYS="book_max_days";

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		String repoCount=configService.fetch(BOOK_REPOSITORY_COUNT);
		
		String maxBookDays=configService.fetch(BOOK_MAX_DAYS);
		
		String bookDate=null;
		
		if(StringUtils.isNullOrEmpty(repoCount)){
			repoCount="5";
		}
		
		if(StringUtils.isNullOrEmpty(maxBookDays)){
			maxBookDays="180";
		}
		
		if(bookRepositoryService.initRepositoryByDate(bookDate, Integer.valueOf(repoCount))){
			
		}
		
	}

}
