package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报表Dao
 *
 */
public interface IReportDao {

    /**
     * 销售统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String,Object>> orderReport(Date startDate, Date endDate);

    /**
     * 销售趋势
     * @param month
     * @param year
     * @return
     */
    Map<String,Object> trendReport(int month,int year);

    /**销售退货趋势统计图
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, Object>> returnorderTrend(int year);

	/**销售退货统计图
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, Object>> returnOrders(Date startDate, Date endDate);
	
	
	/**
	 * 销售分析下钻图
	 * @param goodstypeuuid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String,Object>> orderReport2(Long goodstypeuuid,Date startDate, Date endDate);

	List<Map<String, Object>> returnorderTrend2(int year, int month);
}
