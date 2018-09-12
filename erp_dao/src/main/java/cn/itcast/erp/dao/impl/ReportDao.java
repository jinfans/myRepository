package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReportDao;

@Repository("reportDao")
@SuppressWarnings("unchecked")
public class ReportDao extends HibernateDaoSupport implements IReportDao {

    @Resource(name="sessionFactory")
    public void setSf(SessionFactory sf) {
        super.setSessionFactory(sf);
    }


    @Override
    public List<Map<String,Object>> orderReport(Date startDate, Date endDate) {
        String hql = "select new Map(gt.name as name,sum(od.money) as y,gt.uuid as uuid) from Goodstype gt,Goods g,Orderdetail od, Orders o " +
                "where gt=g.goodstype and g.uuid=od.goodsuuid " +
                "and o=od.orders and o.type='2' ";
        List<Date> params = new ArrayList<Date>();
        if(null != startDate) {
            hql += "and o.createtime>=? ";
            params.add(startDate);
        }
        if(null != endDate) {
            hql += "and o.createtime<=? ";
            params.add(endDate);
        }
        hql+="group by gt.name,gt.uuid";
        return (List<Map<String,Object>>)this.getHibernateTemplate().find(hql, params.toArray());
    }


    @Override
    public Map<String, Object> trendReport(int month, int year) {
        //select extract (month from sysdate) from dual;
        String hql = "select new Map(month(o.createtime) as name,sum(od.money) as y) from Orders o, Orderdetail od " +
                "where o=od.orders and o.type='2' " +
                "and year(o.createtime)=? and month(o.createtime)=? " +
                "group by month(o.createtime)";
        List<?> list = this.getHibernateTemplate().find(hql, year, month);
        if (null != list && list.size() > 0) {
            return (Map<String, Object>) list.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> returnorderTrend(int year) {
        String hql = "select new Map(month(o.createtime) || '月' as name ,sum(od.money) as y) from Returnorders o," +
                "Returnorderdetail od where o=od.returnOrders and year(o.createtime)=? and type=2 and o.state>0 group" +
                " by month(o.createtime)";
        List<Map<String, Object>> list = (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, year);
        return list;
    }

    @Override
    public List<Map<String, Object>> returnorderTrend2(int year,int month) {
    	String yues=null;
    	if(month<10) {
    		yues="0"+month;
    	}else {
    		yues=month+"";
		}
        String hql = "select new Map(t.name as name,t.money as y) from Trendorder t where yue=? and nian=?";
        List<Map<String, Object>> list = (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, yues,year+"");
        return list;
    }

    @Override
    public List<Map<String, Object>> returnOrders(Date startDate, Date endDate) {
        String hql = "select new Map(gt.name as name,sum(rd.money) as y ) from "
                + "Goodstype gt, Returnorders r, Returnorderdetail rd, Goods g "
                + "where "
                + "r = rd.returnOrders "
                + "and gt=g.goodstype "
                + "and g.uuid=rd.goodsuuid "
                + "and r.type='2' and r.state>0";
        //保存参数
        List<Date> params = new ArrayList<Date>();
        if (null != startDate) {
            hql += "and r.createtime>=? ";
            params.add(startDate);
        }
        if (null != endDate) {
            hql += "and r.createtime<=? ";
            params.add(endDate);
        }
        hql += "group by gt.name";
        return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, params.toArray());
    }
	
	
	 @Override
	    public List<Map<String,Object>> orderReport2(Long goodstypeuuid, Date startDate, Date endDate) {
	        String hql = "select new Map(g.name as name,sum(od.money) as y,g.uuid as uuid) from Goods g,Orderdetail od, Orders o " +
	                "where g.uuid=od.goodsuuid and o=od.orders and o.type='2' and g.goodstype.uuid=?";
	        List<Object> params = new ArrayList<Object>();
	        params.add(goodstypeuuid);
	        if(null != startDate) {
	            hql += "and o.createtime>=? ";
	            params.add(startDate);
	        }
	        if(null != endDate) {
	            hql += "and o.createtime<=? ";
	            params.add(endDate);
	        }
	        hql+="group by g.name,g.uuid";
	        return (List<Map<String,Object>>)this.getHibernateTemplate().find(hql, params.toArray());
	    }
}
