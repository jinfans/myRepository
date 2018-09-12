package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

import java.util.List;
import java.util.Map;

/**
 * 仓库库存数据访问接口
 *
 */
public interface IStoredetailDao extends IBaseDao<Storedetail>{
    /**
     * 获取库存预警列表
     * @return
     */
    List<Storealert> getStorealertList();

    /**
     * 获取仓库里所有商品
     * @param storeuuid 仓库id
     * @return
     */
    List<Map<String, Object>> getGoodsByStore(Long storeuuid);
}
