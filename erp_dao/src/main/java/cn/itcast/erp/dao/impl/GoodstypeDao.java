package cn.itcast.erp.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IGoodstypeDao;
import cn.itcast.erp.entity.Goodstype;

/**
 * 商品分类数据访问类
 *
 */
@Repository("goodstypeDao")
public class GoodstypeDao extends BaseDao<Goodstype> implements IGoodstypeDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Goodstype goodstype1,Goodstype goodstype2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Goodstype.class);
        if(goodstype1!=null){
            if(!StringUtils.isEmpty(goodstype1.getName())){
                dc.add(Restrictions.like("name", goodstype1.getName(), MatchMode.ANYWHERE));
            }

        }
        if(null != goodstype2) {
            // 按名称精确查询
            if(!StringUtils.isEmpty(goodstype2.getName())){
                dc.add(Restrictions.eq("name", goodstype2.getName()));
            }
        }
        return dc;
    }

	@Override
	public String findGoodsTypeName(Goodstype goodstype) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = getDetachedCriteria(null, null, null);
		dc.add(Restrictions.eq("name", goodstype.getName()));
		@SuppressWarnings("unchecked")
		List<Goodstype> goodstypes = (List<Goodstype>) this.getHibernateTemplate().findByCriteria(dc);
		if (null != goodstypes && goodstypes.size() > 0) {
			return goodstypes.get(0).getName();
		} else {
			return null;
		}
	}

}
