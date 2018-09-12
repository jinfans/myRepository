package cn.itcast.erp.biz.impl;

import javax.annotation.Resource;

import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.exception.ErpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IReturnordersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IReturnordersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.entity.Returnorders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 退货订单业务逻辑类
 *
 * @author Liberty
 * @date 07/25/2018
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
@Service("returnordersBiz")
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

    private IReturnordersDao returnordersDao;

    @Resource(name = "returnordersDao")
    public void setReturnordersDao(IReturnordersDao returnordersDao) {
        this.returnordersDao = returnordersDao;
        super.setBaseDao(this.returnordersDao);
    }

    @Autowired
    private IEmpDao empDao;

    @Autowired
    private ISupplierDao supplierDao;

    /**
     * 获取员工名称
     *
     * @param uuid
     * @return String
     */
    private String getEmpName(Long uuid) {
        if (null == uuid) {
            return null;
        }
        return empDao.get(uuid).getName();
    }

    /**
     * 循环ReturnOrders为ReturnOrders中的姓名赋值
     *
     * @param t1
     * @param t2
     * @param obj
     * @param startRow
     * @param maxResults
     * @return List<Returnorders>
     * @author Liberty
     * @date 07/25/2018
     */
    @Override
    public List<Returnorders> getListByPage(Returnorders t1, Returnorders t2, Object obj, int startRow, int maxResults) {
        List<Returnorders> list = super.getListByPage(t1, t2, obj, startRow, maxResults);
        // 循环赋值名称
        for (Returnorders returnOrders : list) {
            returnOrders.setCreaterName(getEmpName(returnOrders.getCreater()));
            returnOrders.setCheckerName(getEmpName(returnOrders.getChecker()));
            returnOrders.setEnderName(getEmpName(returnOrders.getEnder()));
            returnOrders.setSupplierName(supplierDao.get(returnOrders.getSupplieruuid()).getName());
        }
        return list;
    }

    /**
     * 判断退货数是否大于历史订单中的数量method，大于就会抛出异常
     * @param returnorders
     */
    private void isBeyond(Returnorders returnorders) {
        Long[] checkedGoodsId = new Long[returnorders.getReturnOrderDetails().size()];
        for (int i = 0; i < returnorders.getReturnOrderDetails().size(); i++) {
            Long goodsCount = 0L;
            Long goodsUuid = returnorders.getReturnOrderDetails().get(i).getGoodsuuid();
            Long maximum = returnordersDao.getMaximumBySupplierIdAndGoodsId(returnorders.getSupplieruuid(), goodsUuid);
            if (null == maximum) {
                throw new ErpException("数据库拉肚子了...");
            }
            for (Returnorderdetail ord : returnorders.getReturnOrderDetails()) {
                boolean flag = false;
                for (int j = 0; j < checkedGoodsId.length; j++) {
                    if (goodsUuid.equals(checkedGoodsId[j])) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }

                if (goodsUuid.equals(ord.getGoodsuuid())) {
                    goodsCount += ord.getNum();
                    if (goodsCount > maximum) {
                        throw new ErpException("你给我好好想想，你有进这么多货吗？");
                    }
                }
            }
            checkedGoodsId[i] = goodsUuid;
        }
    }

    /**
     * 这是一个添加“销售退货”订单与“采购退货”订单的method
     *
     * @author Liberty
     * @date 07/25/2018
     * @param returnorders
     */
    @Override
    @Transactional
    public void add(Returnorders returnorders) {
    	if (Returnorders.TYPE_OUT.equals(returnorders.getType())) {
    		// 判断退货数是否大于历史订单中的数量
    		isBeyond(returnorders);
			
		}
 
        // 总金额
        double totalmoney = 0;
        // 预先定义后面“销售退货”订单中明细状态的变量为“未入库”
        String returnOrderDetailState = Returnorderdetail.STATE_NOT_IN;
        // 生成日期
        returnorders.setCreatetime(new Date());
        // 订单类型 采购退货: TYPE_OUT = "1", 销售退货: TYPE_IN = "2";

        // 判断传来的订单类型，如果这是一个“采购退货”订单，就在接下来的循环中设置订单中明细状态为“未出库”，反之上面定义了为“未入库”；
        if (Returnorders.TYPE_OUT.equals(returnorders.getType())) {
            returnOrderDetailState = Returnorderdetail.STATE_NOT_OUT;
        }

        // 用循环为这个订单中的所有明细设置初始值
        for (Returnorderdetail returnorderdetail : returnorders.getReturnOrderDetails()) {

            // 把此订单中每条明细的金额加入totalmoney，得到这笔退货订单的总金额；
            totalmoney += returnorderdetail.getMoney();
            // 设置每条明细的状态
            returnorderdetail.setState(returnOrderDetailState);
            // 设置每条明细与订单的关系，让每条明细记录自身对应的退货订单
            returnorderdetail.setReturnOrders(returnorders);
        }

        // 给这个退货订单设置总金额
        returnorders.setTotalmoney(totalmoney);
        // 调用持久层，存入数据库
        returnordersDao.add(returnorders);
    }

    /**
     * 用传入的供应商ID，然后调用持久层查询历史订单中对应供应商存在的商品名称和商品ID，返回List
     * @author Liberty
     * @date 07/26/2018
     * @param supplieruuid
     * @return List
     */
    @Override
    public List<Map<String, Object>> getGoodsDetailListBySupplierId(Long supplieruuid) {
        return returnordersDao.getGoodsDetailListBySupplierId(supplieruuid);
    }


	@Override
	@Transactional
	public void doCheck(Long empuuid, Long uuid) {//订单状态
		// TODO Auto-generated method stub
		 Returnorders orders = returnordersDao.get(uuid);
		if (Returnorders.STATE_CHECK.equals(orders.getState())) {
			throw new ErpException("订单已审核,不能重复审核");
		}

		orders.setState(Returnorders.STATE_CHECK);
		orders.setChecktime(new Date());
		orders.setChecker(empuuid);
	}

	
}
