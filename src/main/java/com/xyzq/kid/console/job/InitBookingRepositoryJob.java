package com.xyzq.kid.console.job;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.service.BookRepositoryService;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.kid.logic.dateUnviable.entity.DateUnviableEntity;
import com.xyzq.kid.logic.dateUnviable.service.DateUnviableService;

public class InitBookingRepositoryJob{
	
	public static Logger logger = LoggerFactory.getLogger(InitBookingRepositoryJob.class);

	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	DateUnviableService dateUnviableService;
	
	@Autowired
	ConfigService configService;
	
	static final String BOOK_REPOSITORY_COUNT="book_repository_count";
//	static final String BOOK_MAX_DAYS="book_max_days";
	static final String TICKET_EXPIRE_DAYS="ticket_expiretime";
	static final String REPOSITORY_LASTINIT_DATE="repository_lastinit_date";
	static final String IP_WHITE_NAMELIST="ip_white_namelist";//IP白名单，名单内的服务器可以执行该定时任务
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	protected void init() {
		String ips=configService.fetch(IP_WHITE_NAMELIST);
		try {
			if(StringUtils.isNullOrEmpty(ips)){
				work();
			}else{
				String localIP=InetAddress.getLocalHost().getHostAddress();
				if(ips.contains(localIP)){
					work();
				}
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}
	
	private void work() {
		String repoCount = configService.fetch(BOOK_REPOSITORY_COUNT);

		String maxBookDays = configService.fetch(TICKET_EXPIRE_DAYS);

		String bookDate = configService.fetch(REPOSITORY_LASTINIT_DATE);

		if (StringUtils.isNullOrEmpty(repoCount)) {
			repoCount = "5";
		}

		if (StringUtils.isNullOrEmpty(maxBookDays)) {
			maxBookDays = "180";
		}
		try {
			Date lastDate = sdf.parse(bookDate);
			Calendar last = Calendar.getInstance();
			last.setTime(lastDate);
			Date nowDate=new Date();
			Calendar now=Calendar.getInstance();
			now.setTime(nowDate);
			Calendar max = Calendar.getInstance();
			max.setTime(nowDate);
			max.add(Calendar.DATE, Integer.parseInt(maxBookDays));
			while (last.before(max)) {
				last.add(Calendar.DATE, 1);
				String initDate = sdf.format(last.getTime());
				DateUnviableEntity unviableDate = dateUnviableService
						.findBy(initDate);
				if (unviableDate == null) {
					if (bookRepositoryService.initRepositoryByDate(initDate,
							Integer.valueOf(repoCount))) {
						configService.alter(REPOSITORY_LASTINIT_DATE, initDate);
						logger.info(initDate + " repository init success");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
