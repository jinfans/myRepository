package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Goods;
import cn.itcast.erp.entity.Returnorders;

import java.util.List;
import java.util.Map;

/**
 * 退货订单数据访问接口
 *
 * @author Liberty
 * @date 07/26/2018
 */
public interface IReturnordersDao extends IBaseDao<Returnorders> {

    /**
     * 通过传入的供应商编号，来查询历史订单中对应这个供应商存在的商品名称和商品ID
     * 查询语句：
     * select od.GOODSNAME
     * from ORDERS o, ORDERDETAIL od
     * where o.SUPPLIERUUID = 7 and od.ORDERSUUID = o.UUID
     * group by GOODSNAME;
     *
     * @param supplieruuid
     * @return List
     * @author Liberty
     * @date 07/26/2018
     */
    List<Map<String, Object>> getGoodsDetailListBySupplierId(Long supplieruuid);

    // List<Map<String, Object>> getGoodsDetailListBySupplierId(Long supplierId);

    /**
     * 传入供应商Id和商品Id，发回历史订单中在这个供应商买的这个商品总数量
     *
     * @param supplierId
     * @param goodsId
     * @return Integer
     * @author Liberty
     * @date 07/27/2018
     */
    Long getMaximumBySupplierIdAndGoodsId(Long supplierId, Long goodsId);
}
