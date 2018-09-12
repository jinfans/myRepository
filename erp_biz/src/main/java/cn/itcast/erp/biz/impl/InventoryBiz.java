package cn.itcast.erp.biz.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.itcast.erp.biz.IInventoryBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.entity.Inventory;
import cn.itcast.erp.exception.ErpException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 盘盈盘亏业务逻辑类
 *
 */
@Service("inventoryBiz")
public class InventoryBiz extends BaseBiz<Inventory> implements IInventoryBiz {

    private IInventoryDao inventoryDao;
    @Autowired
    private IEmpDao empDao;
    @Autowired
    private IStoreDao storeDao;
    @Autowired
    private IGoodsDao goodsDao;

    @Resource(name="inventoryDao")
    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
        super.setBaseDao(this.inventoryDao);
    }

	@Override
	@Transactional
	public void doCheck(Long empuuid, Long uuid) {
		// TODO Auto-generated method stub
		// 获取订单对象，进入持久化状态, 事务一提交就会自动更新到数据库
        Inventory inventory = inventoryDao.get(uuid);
        // 判断订单的状态
        if(!Inventory.STATE_CREATE.equals(inventory.getState())) {
            // 不是未审核状态
            throw new ErpException("亲，该盘盈盘亏已经审核过了");
        }
        // 审核日期
        inventory.setChecktime(new Date());
        // 订单状态
        inventory.setState(Inventory.STATE_CHECK);
        // 审核人
        inventory.setChecker(empuuid);
	}
    

    @Override
    public List<Inventory> getListByPage(Inventory t1, Inventory t2, Object obj, int startRow, int maxResults) {
        List<Inventory> list = super.getListByPage(t1, t2, obj, startRow, maxResults);
        for (Inventory inventory : list) {
            inventory.setGoodsName(goodsDao.get(inventory.getGoodsuuid()).getName());
            inventory.setStoreName(storeDao.get(inventory.getStoreuuid()).getName());
            inventory.setCreaterName(getEmpName(inventory.getCreater()));
            inventory.setCheckerName(getEmpName(inventory.getChecker()));
        }
        return list;
    }

    /**
     * 获取员工名称
     * @param uuid
     * @return
     */
    private String getEmpName(Long uuid) {
        if(null == uuid) {
            return null;
        }
        return empDao.get(uuid).getName();
    }
}
