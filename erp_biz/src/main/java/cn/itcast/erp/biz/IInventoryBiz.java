package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Inventory;
/**
 * 盘盈盘亏业务逻辑层接口
 *
 */
public interface IInventoryBiz extends IBaseBiz<Inventory>{

	void doCheck(Long empuuid, Long id);

}

