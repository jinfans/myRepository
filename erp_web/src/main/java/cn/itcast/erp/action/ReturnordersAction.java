package cn.itcast.erp.action;

import javax.annotation.Resource;

import cn.itcast.erp.entity.*;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;
import com.alibaba.fastjson.JSON;
import com.redsun.bos.ws.impl.IWaybillWs;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import java.util.List;

import javax.annotation.Resource;

import cn.itcast.erp.biz.IReturnordersBiz;

import java.util.List;
import java.util.Map;

/**
 * 退货订单Action
 *
 * @author Liberty
 * @date 07/26/2018
 * <p>
 * <p>
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
@Controller("returnordersAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("returnorders")
public class ReturnordersAction extends BaseAction<Returnorders> {

    private IReturnordersBiz returnordersBiz;

    public String getJson() {
        return json;
    }

    @Resource(name = "returnordersBiz")
    public void setReturnordersBiz(IReturnordersBiz returnordersBiz) {
        this.returnordersBiz = returnordersBiz;
        super.setBaseBiz(this.returnordersBiz);
    }

    @Autowired
    private IWaybillWs waybillWs;

    /**
     * 所有明细的json字符串
     */
    private String json;
    /**
     * 运单号
     */
    private Long waybillsn;

    /**
     * 接收客户端选择的供应商ID
     */
    private Long supplierId;

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setWaybillsn(Long waybillsn) {
        this.waybillsn = waybillsn;
    }

    /**
     * @author Liberty
     * @date 07/26/2018
     */
    @Override
    public void add() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if (null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            // 获取前端提交的订单,主要是得到供应商的编号
            Returnorders orders = getT();
            // 把明细的json字符串转成订单明细列表
            List<Returnorderdetail> returnOrderDetails = JSON.parseArray(json, Returnorderdetail.class);
            // 设置订单下的明细
            orders.setReturnOrderDetails(returnOrderDetails);
            // 设置下单人
            orders.setCreater(loginUser.getUuid());
            // 设置订单的类型
            orders.setType(Returnorders.TYPE_OUT);
            orders.setState(Returnorders.STATE_CREATE);
            returnordersBiz.add(orders);
            WebUtil.ajaxReturn(true, "添加订单成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "添加订单失败");
        }

    }

    /**
     * 通过传入的供应商编号，来查询历史订单中对应这个供应商存在的商品名称和商品ID，并用FirstJSON把对象转成json字符串输出给前端。
     *
     * @author Liberty
     * @date 07/26/2018
     */
    public void getGoodsDetailList() {
        List<Map<String, Object>> list = returnordersBiz.getGoodsDetailListBySupplierId(supplierId);
        WebUtil.write(list);
    }

    /**
     * 查询由我登陆用户发起订单
     */
    public void myListByPage() {
        Emp loginUser = WebUtil.getLoginUser();
        if (null != loginUser) {
            // 判断查询条件
            if (null == getT1()) {
                // 构建查询条件
                setT1(new Returnorders());
            }
            // 设置查询条件为登陆用户,根据下单员查询
            getT1().setCreater(loginUser.getUuid());
            super.listByPage();
        }
    }


  /*  public void myListByPage() {
		if(getT1()==null) {
			setT1(new Returnorders());
		}
		Emp emp=WebUtil.getLoginUser();
		if(emp != null) {
			getT1().setCreater(emp.getUuid());
			 super.listByPage();
		}
	}*/

    /**
     * 退货订单的审核方法，需要前端传过来id；
     */
    public void doCheck() {

        Emp loginUser = WebUtil.getLoginUser();
        if (null == loginUser) {
            WebUtil.ajaxReturn(false, "您还没有登陆");
            return;
        }
        try {
            returnordersBiz.doCheck(loginUser.getUuid(), getId());
            WebUtil.ajaxReturn(true, "订单已审核成功");
        } catch (ErpException e) {
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            WebUtil.ajaxReturn(false, "审核失败");
        }

    }


    /**
     * 销售订单录入salesEntry
     */
    public void salesEntry() {
        Emp loginUser = WebUtil.getLoginUser();
        //用户校验
        if (null == loginUser) {
            WebUtil.ajaxReturn(false, "亲^@^,你还没有登录哦");
            return;
        }
        try {
            //获取提交的订单
            Returnorders returnOrders = getT();
            //设置订单的下单人
            returnOrders.setCreater(loginUser.getUuid());
            //获取订单明细
            List<Returnorderdetail> returnOrderDetails = JSON.parseArray(json, Returnorderdetail.class);
            //设置订单明细
            returnOrders.setReturnOrderDetails(returnOrderDetails);
            //设置订单的类型
            returnOrders.setType(Returnorders.TYPE_IN);//销售
            //设置订单状态
            returnOrders.setState(Returnorders.STATE_CREATE);//未审核
            //保存订单
            returnordersBiz.add(returnOrders);
            WebUtil.ajaxReturn(true, "亲^@^,订单添加成功");
        } catch (ErpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "亲^@^,订单添加失败");
        }
    }

}