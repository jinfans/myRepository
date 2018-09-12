package cn.itcast.erp.biz;

import cn.itcast.erp.entity.Returnorders;

import java.util.List;
import java.util.Map;

/**
 * 退货订单业务逻辑层接口
 *
 * @author Liberty
 * @date 07/26/2018
 */
public interface IReturnordersBiz extends IBaseBiz<Returnorders> {

    /**
     * 用传入的供应商ID，然后调用持久层查询历史订单中对应供应商存在的商品名称和商品ID，返回List
     *
     * @param supplieruuid
     * @return List
     * @author Liberty
     * @date 07/26/2018
     */
    List<Map<String, Object>> getGoodsDetailListBySupplierId(Long supplieruuid);
	void doCheck(Long uuid, Long id);

}

