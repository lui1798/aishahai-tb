package com.nzonly.tb.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nzonly.tb.entity.Logistics;
import com.nzonly.tb.entity.TaobaoTrade;
import com.nzonly.tb.persistence.BaseDao;

/**
 * @author yinheli <yinheli@gmail.com>
 * @date 2012-8-1 下午11:12:12
 * @version V1.0
 */
@Component
@Transactional(readOnly = true)
public class LogisticsService {
	
	@Autowired
	private BaseDao dao;
	
	@Autowired
	private TaobaoTradeService tradeService;
	
	/**
	 * 分页读取
	 * 
	 * @param page
	 * @return
	 * @author yinheli
	 * @date 2012-8-1 下午11:16:37
	 */
	public Page<Logistics> getByPage(PageRequest page) {
		return dao.getByPage("Logistics.getByPage", page);
	}
	
	/**
	 * 保存或更新一条数据
	 * 
	 * @param logistics
	 * @author yinheli
	 * @date 2012-8-1 下午11:22:04
	 */
	@Transactional(readOnly = false)
	public void saveOuUpdate(Logistics logistics) {
		long count = dao.getOne("Logistics.countByTid", logistics.getTid());
		if (count > 0) {
			logistics.setLastUpdateTime(new Date());
			dao.update("Logistics.updateByPrimaryKeySelective", logistics);
		} else {
			logistics.setCreateTime(new Date());
			dao.insert("Logistics.insertSelective", logistics);
		}
		
		TaobaoTrade trade = tradeService.getByTid(logistics.getTid());
		if (trade != null) {
			trade.setOutSid(logistics.getOutSid());
			trade.setCompanyName(logistics.getCompanyName());
			tradeService.update(trade);
		}
	}

}
