package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Returnorderdetail;
/**
 * 退货订单明细业务逻辑层接口
 *
 */
public interface IReturnorderdetailBiz extends IBaseBiz<Returnorderdetail>{

	/**
	 * 采购退货出库
	 * @param uuid: 明细编号
	 * @param storeuuid : 仓库编号
	 * @param empuuid : 库管员编号
	 */
	void doOutStore(Long uuid, Long storeuuid, Long empuuid);
	
	void doInStore(Long uuid, Long storeuuid, Long id);
}

