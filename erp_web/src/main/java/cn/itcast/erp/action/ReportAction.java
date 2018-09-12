package cn.itcast.erp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.util.WebUtil;

@Controller("reportAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("report")
public class ReportAction {

    private Date startDate;
    private Date endDate;
    private int year;
    
    private String month;
    

    public void setMonth(String month) {
		this.month = month;
	}

    
	public String getMonth() {
		return month;
	}


	@Autowired
    private IReportBiz reportBiz;

    /**
     * 销售统计报表
     */
    public void orderReport() {
        List<Map<String,Object>> list = reportBiz.orderReport(startDate, endDate);
        WebUtil.write(list);
    }

    /**
     * 销售趋势
     */
    public void trendReport() {
        List<Map<String, Object>> list = reportBiz.trendReport(year);
        WebUtil.write(list);
    }
    
    /**
   	 * 销售退货趋势统计图
   	 */
    public void returnorderTrend() {
		List<Map<String,Object>> list = reportBiz.returnorderTrend(year);
		/*Map<String,Object> resultMap = new HashMap<String,Object>();
		 resultMap.put("total", 2);
	     resultMap.put("rows", list);
		WebUtil.write(resultMap);*/
		WebUtil.write(list);
	}
    public void returnorderTrend2() {
    	int months=Integer.parseInt(getMonth().substring(0, 1));
		List<Map<String,Object>> list = reportBiz.returnorderTrend2(year,months);
		/*Map<String,Object> resultMap = new HashMap<String,Object>();
		 resultMap.put("total", 2);
	     resultMap.put("rows", list);
		WebUtil.write(resultMap);*/
		WebUtil.write(list);
	}

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    
    /**
	 * 销售退货统计图
	 */
	public void returnOrders() {
		List<Map<String, Object>> list=reportBiz.returnOrders(startDate, endDate);
		WebUtil.write(list);
	}
	
	private Long goodstypeuuid;

	public void setGoodstypeuuid(Long goodstypeuuid) {
		this.goodstypeuuid = goodstypeuuid;
	}
	
	 /**
     * 销售统计下钻报表
     */
    public void orderReport2() {
        List<Map<String,Object>> list = reportBiz.orderReport2(goodstypeuuid,startDate, endDate);
        WebUtil.write(list);
    }
}
