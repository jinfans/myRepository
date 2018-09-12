package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IReportDao;
import cn.itcast.erp.entity.Goods;

@Service("reportBiz")
public class ReportBiz implements IReportBiz {

    @Autowired
    private IReportDao reportDao;
    
    @Autowired
    private IGoodsDao goodsDao;

    @Override
    public List<Map<String,Object>> orderReport(Date startDate, Date endDate) {
        return reportDao.orderReport(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> trendReport(int year) {
        // 保存12个月的数据
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 循环12个月，分别每个月查询数据
        Map<String, Object> monthData = null;
        for(int i = 1; i <= 12; i++) {
            monthData = reportDao.trendReport(i, year);
            if(null == monthData) {
                // 没有这个月的销售额，要补0
                //{name:1,y:0}
                monthData = new HashMap<String,Object>();
                monthData.put("name", i);
                monthData.put("y", 0);
            }
            result.add(monthData);
        }
        return result;
    }

    @Override
	public List<Map<String, Object>> returnorderTrend(int year) {
		List<Map<String, Object>> resultList=new ArrayList<Map<String, Object>>(12);
		List<Map<String, Object>> list=reportDao.returnorderTrend(year);
		Map<String, Map<String,Object>> map = new HashMap<String,Map<String,Object>>();
		for (Map<String, Object> m : list) {
			map.put((String)m.get("name"), m);
		}
		Map<String,Object> data = null;
	    //按12个月，对每个月份的数据进行封装，最终以List<Map<String,Object>>形式返回
	    for(int i = 1; i <= 12; i++){
	        data = map.get(i + "月");
	        if(null == data){
	            //如果当月没有销售额，则补上当月的月份和数据0
	            data = new HashMap<String,Object>();
	            data.put("name", i+"月");
	            data.put("y", 0);
	        }
	        resultList.add(data);
	    }
		return resultList;
	}
	@Override
	public List<Map<String, Object>> returnOrders(Date startDate, Date endDate) {
		return reportDao.returnOrders(startDate, endDate);
	}
	
	
	@Override
    public List<Map<String,Object>> orderReport2(Long goodstypeuuid, Date startDate, Date endDate) {
        return reportDao.orderReport2(goodstypeuuid, startDate, endDate);
    }

	@Override
	public List<Map<String, Object>> returnorderTrend2(int year, int month) {
		List<Goods> list = goodsDao.getList(null, null, null);
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		Set<String> set=new HashSet<String>();
		List<Map<String,Object>> returnorderTrend2 = reportDao.returnorderTrend2(year, month);
		for (Map<String, Object> map : returnorderTrend2) {
			set = map.keySet();
			result.add(map);
		}
		for (Goods goods : list) {
			if(!set.contains(goods.getName())) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("name", goods.getName());
				map.put("y", 0);
				result.add(map);
			}
		}
		return result;
	}

}
