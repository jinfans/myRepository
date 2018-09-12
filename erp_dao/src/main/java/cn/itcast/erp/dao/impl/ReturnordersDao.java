package cn.itcast.erp.dao.impl;

import cn.itcast.erp.entity.Goods;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReturnordersDao;
import cn.itcast.erp.entity.Returnorders;

import java.util.List;
import java.util.Map;

/**
 * 退货订单数据访问类
 *
 * @author Liberty
 * @date 07/26/2018
 *
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */
@Repository("returnordersDao")
public class ReturnordersDao extends BaseDao<Returnorders> implements IReturnordersDao {

    /**
     * 构建查询条件
     *
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Returnorders returnorders1, Returnorders returnorders2, Object param) {
        DetachedCriteria dc = DetachedCriteria.forClass(Returnorders.class);
        if (returnorders1 != null) {
            if (!StringUtils.isEmpty(returnorders1.getType())) {
                dc.add(Restrictions.eq("type", returnorders1.getType()));
            }
            if (!StringUtils.isEmpty(returnorders1.getState())) {
                dc.add(Restrictions.eq("state", returnorders1.getState()));
            }
          //查询销售退货订单
            if (null != returnorders1.getCreater()) {
				dc.add(Restrictions.eq("creater", returnorders1.getCreater()));
			}	
        }
        return dc;
    }

    /**
     * 通过传入的供应商编号，来查询历史订单中对应这个供应商存在的商品名称和商品ID
     * 查询语句：
     * select od.GOODSNAME
     * from ORDERS o, ORDERDETAIL od
     * where o.SUPPLIERUUID = ? and od.ORDERSUUID = o.UUID
     * group by GOODSNAME;
     *
     * param supplieruuid
     * @return List
     * @author Liberty
     * @date 07/26/2018
     */
    /*@Override
    public List<Map<String, Object>> getGoodsNameAndGoodsIdListBySupplieruuid(Long supplieruuid) {
        String hql = "SELECT new Map(OD.goodsuuid as uuid, OD.goodsname as name) FROM Orders O, Orderdetail OD " +
            "WHERE O.supplieruuid = ? AND OD.orders = O GROUP BY OD.goodsuuid, OD.goodsname";
        return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, supplieruuid);
    }*/
/*getGoodsDetailListBySupplierId*/
    @Override
    public List<Map<String, Object>> getGoodsDetailListBySupplierId(Long supplierId) {
        /*select
        od.GOODSUUID,
        od.GOODSNAME,
        g.INPRICE,
        sum(od.NUM)
        from ORDERS o, ORDERDETAIL od, GOODS g
        where o.SUPPLIERUUID = 2 and od.ORDERSUUID = o.UUID and od.GOODSUUID = g.UUID
        group by od.GOODSNAME, od.GOODSUUID, g.INPRICE;*/
        String hql = "SELECT new Map(OD.goodsuuid as uuid, OD.goodsname as name, G.inprice as inprice, sum(OD.num) as maximum) " +
            "FROM Orders O, Orderdetail OD, Goods G " +
            "WHERE O.supplieruuid = ? AND OD.orders = O AND OD.goodsuuid = G.uuid GROUP BY OD.goodsuuid, OD.goodsname, G.inprice";
        return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, supplierId);
    }

    /**
     * 传入供应商Id和商品Id，发回历史订单中在这个供应商买的这个商品总数量
     * 查询语句：
     * SELECT SUM(OD.NUM)
     * FROM ORDERDETAIL OD, ORDERS O, GOODS G
     * WHERE OD.ORDERSUUID = O.UUID AND G.UUID = OD.GOODSUUID AND O.SUPPLIERUUID = ? AND G.UUID = ?
     *
     * @param supplierId
     * @param goodsId
     * @return Integer
     * @author Liberty
     * @date 07/27/2018
     */
    @Override
    public Long getMaximumBySupplierIdAndGoodsId(Long supplierId, Long goodsId) {
        String hql = "SELECT sum(OD.num) FROM Orderdetail OD, Orders O, Goods G " +
            "WHERE OD.orders = O AND OD.goodsuuid = G.uuid AND O.supplieruuid = ? AND G.uuid = ?";
        List<?> list = this.getHibernateTemplate().find(hql, supplierId, goodsId);
        return (Long) list.get(0);
    }
}
