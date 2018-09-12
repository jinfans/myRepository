package cn.itcast.erp.dao.impl;

import java.util.List;
import java.util.Map;

import cn.itcast.erp.entity.Goods;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存数据访问类
 *
 */
@Repository("storedetailDao")
@SuppressWarnings("unchecked")
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Storedetail sd1,Storedetail sd2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Storedetail.class);
        if(sd1!=null){
            // 仓库编号
            if(null != sd1.getStoreuuid()) {
                dc.add(Restrictions.eq("storeuuid", sd1.getStoreuuid()));
            }
            // 商品编号
            if(null != sd1.getGoodsuuid()) {
                dc.add(Restrictions.eq("goodsuuid", sd1.getGoodsuuid()));
            }
        }
        return dc;
    }

    @Override
    public List<Storealert> getStorealertList() {
        String hql = "from Storealert where storenum < outnum";
        return (List<Storealert>) this.getHibernateTemplate().find(hql);
    }

    @Override
    public List<Map<String, Object>> getGoodsByStore(Long storeuuid) {
        String sql = "SELECT new Map(g.uuid as uuid, g.name as name) FROM Storedetail s, Goods g " +
                "WHERE g.uuid = s.goodsuuid and s.storeuuid = ?";
        return (List<Map<String, Object>>) this.getHibernateTemplate().find(sql, storeuuid);
    }
}
