package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IReturnorderdetailBiz;
import cn.itcast.erp.dao.IReturnorderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.entity.Returnorders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.exception.ErpException;

/**
 * 退货订单明细业务逻辑类
 *
 */
@Service("returnorderdetailBiz")
public class ReturnorderdetailBiz extends BaseBiz<Returnorderdetail> implements IReturnorderdetailBiz {

    private IReturnorderdetailDao returnorderdetailDao;
    
    @Resource(name="returnorderdetailDao")
    public void setReturnorderdetailDao(IReturnorderdetailDao returnorderdetailDao) {
        this.returnorderdetailDao = returnorderdetailDao;
        super.setBaseDao(this.returnorderdetailDao);
    }

    @Autowired
    private IStoredetailDao storedetailDao;
    @Autowired
    private IStoreoperDao storeoperDao;
    
	@Override
	@Transactional
	public void doOutStore(Long uuid, Long storeuuid, Long empuuid) {
		//更新明细表
		Returnorderdetail detail = returnorderdetailDao.get(uuid);
		if(!Returnorderdetail.STATE_NOT_OUT.equals(detail.getState())){
			throw new ErpException("该明细已出库");
		}
		detail.setState(Returnorderdetail.STATE_OUT);
		detail.setEnder(empuuid);
		detail.setStoreuuid(storeuuid);
		detail.setEndtime(new Date());
		//更新库存表
		Storedetail sd = new Storedetail();
		sd.setGoodsuuid(detail.getGoodsuuid());
		sd.setStoreuuid(storeuuid);
		List<Storedetail> list = storedetailDao.getList(sd, null, null);
		long num = -1;
		if(null != list && list.size()>0){
			sd = list.get(0);
			num = sd.getNum() - detail.getNum();
			if(num < 0){
				throw new ErpException("库存不足");
			}
			sd.setNum(num);
		}else{
			throw new ErpException("库存不足");
		}
		//更新记录表
		Storeoper oper = new Storeoper();
		oper.setEmpuuid(empuuid);
		oper.setGoodsuuid(detail.getGoodsuuid());
		oper.setNum(detail.getNum());
		oper.setOpertime(detail.getEndtime());
		oper.setStoreuuid(storeuuid);
		oper.setType(Storeoper.TYPE_OUT);
		storeoperDao.add(oper);
		//更新订单表
		Returnorders orders = detail.getReturnOrders();
		Returnorderdetail param = new Returnorderdetail();
		param.setState(Returnorderdetail.STATE_NOT_OUT);
		param.setReturnOrders(orders);
		Long count = returnorderdetailDao.getCount(param, null, null);
		if(count == 0){
			orders.setState(Returnorders.STATE_OUT);
			orders.setEnder(empuuid);
			orders.setEndtime(detail.getEndtime());
		}
	}

	@Override
	@Transactional
	public void doInStore(Long uuid, Long storeuuid, Long id) {
		
		Returnorderdetail rd = returnorderdetailDao.get(id);
		
		if (Returnorderdetail.STATE_IN.equals(rd.getState())) {
			throw new ErpException("亲，该明细已经入库了");
		}
		
		rd.setEndtime(new Date());
		rd.setEnder(uuid);
		rd.setStoreuuid(storeuuid);
		rd.setState(Returnorderdetail.STATE_IN);
		
		

        // 2. 库存表storedetail
        // 2.1 判断是否存在库存信息
        // 根据仓库编号，商品编号 查询库存表 list.size() > 0
        // 构建查询条件
        Storedetail sd = new Storedetail();
        // 查询条件
        sd.setGoodsuuid(rd.getGoodsuuid());
        sd.setStoreuuid(storeuuid);
        List<Storedetail> list = storedetailDao.getList(sd, null, null);
        // 2.2 如果存在库存信息
        if (list.size() > 0) {
            // 数量累加 list.get(0) 库存信息 持久状
            sd = list.get(0);
            // 取出库存的数量 + 明细的数量
            sd.setNum(sd.getNum() + rd.getNum());
        } else {
            // 2.3 不存在库存信息
            // 插入新的记录:
            // 仓库编号 前端传过来
            // 商品编号 明细有
            // 数量 明细的数量
            sd.setNum(rd.getNum());
            storedetailDao.add(sd);
        }
        
     // 3. 日志记录storeoper
        // 插入记录
        Storeoper log = new Storeoper();
        // 3.1 操作员工编号 登陆用户
        log.setEmpuuid(uuid);
        // 3.2 操作日期 系统时间, 让入库的时间保持一致
        log.setOpertime(rd.getEndtime());
        // 3.3 仓库编号 前端传过来
        log.setStoreuuid(storeuuid);
        // 3.4 商品编号 明细有
        log.setGoodsuuid(rd.getGoodsuuid());
        // 3.5 数量 明细的数量
        log.setNum(rd.getNum());
        // 3.6 操作的类型 1:入库
        log.setType(Storeoper.TYPE_IN);
        storeoperDao.add(log);
       
        // 4. 订单表returnorders, 订单进入持久态
        Returnorders returnorders = rd.getReturnOrders();
        // 4.1 判断订单下的所有明细是否都完成入库
        // 构建查询条件
         Returnorderdetail returnorderdetail = new Returnorderdetail();
        //Orderdetail queryParam = new Orderdetail();
        // 查询条件: 订单编号 明细获取, 明细的状态0
         returnorderdetail.setReturnOrders(returnorders);
        returnorderdetail.setState(Returnorderdetail.STATE_NOT_IN);
        // 查询订单下的未入库的明细的个数getCount
        Long count = returnorderdetailDao.getCount(returnorderdetail, null, null);
        // 4.2 如果还有明细没有入库count > 0
        // 不需要操作
        // 4.3 如果不存在未入库的明细count = 0
        if (count == 0) {
            // 更新订单:
            // 4.3.1 入库日期： 系统时间
        	returnorders.setEndtime(rd.getEndtime());
            // 4.3.2 库管员 登陆用户
        	returnorders.setEnder(uuid);
            // 4.3.3 状态 3: 已入库
        	returnorders.setState(Returnorders.STATE_OUT);
	}
	}
    
    
}
