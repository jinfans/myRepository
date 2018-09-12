package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.entity.Inventory;

/**
 * 盘盈盘亏数据访问类
 *
 */
@Repository("inventoryDao")
public class InventoryDao extends BaseDao<Inventory> implements IInventoryDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Inventory inventory1,Inventory inventory2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Inventory.class);
        if(inventory1!=null){
            if(!StringUtils.isEmpty(inventory1.getType())){
                dc.add(Restrictions.eq("type", inventory1.getType()));
            }
            if(!StringUtils.isEmpty(inventory1.getState())){
                dc.add(Restrictions.eq("state", inventory1.getState()));
            }
            if(!StringUtils.isEmpty(inventory1.getRemark())){
                dc.add(Restrictions.like("remark", inventory1.getRemark(), MatchMode.ANYWHERE));
            }
            
            
            
            if(null != inventory1.getChecker()){
            	dc.add(Restrictions.eq("checker", inventory1.getChecker()));
            }
            if(null != inventory1.getGoodsuuid()){
            	dc.add(Restrictions.eq("goodsuuid", inventory1.getGoodsuuid()));
            }
            if(null != inventory1.getStoreuuid()){
            	dc.add(Restrictions.eq("storeuuid", inventory1.getStoreuuid()));
            }
            if(null != inventory1.getNum()){
            	dc.add(Restrictions.eq("num", inventory1.getNum()));
            }
            if(null != inventory1.getCreater()){
            	dc.add(Restrictions.eq("creater", inventory1.getCreater()));
            }
            
            
            if(null != inventory1.getCreatetime()){
            	dc.add(Restrictions.ge("createtime", inventory1.getCreatetime()));
            }
            if(null != inventory1.getChecktime()){
            	dc.add(Restrictions.ge("checktime", inventory1.getChecktime()));
            }

        }
        
        if (null != inventory2) {
        	 if(null != inventory2.getCreatetime()){
             	dc.add(Restrictions.le("createtime", inventory2.getCreatetime()));
             }
             if(null != inventory2.getChecktime()){
             	dc.add(Restrictions.le("checktime", inventory2.getChecktime()));
             }
		}
        
        
        return dc;
    }
}
