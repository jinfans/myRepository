package cn.itcast.erp.action;


import cn.itcast.erp.biz.IInventoryBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Inventory;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

@Controller("inventoryAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("inventory")
public class InventoryAction extends BaseAction<Inventory> {

    private IInventoryBiz inventoryBiz;

    @Resource(name="inventoryBiz")
    public void setInventoryBiz(IInventoryBiz inventoryBiz) {
        this.inventoryBiz = inventoryBiz;
        super.setBaseBiz(this.inventoryBiz);
    }

    /**
     * 盈利盈亏登记
     */
    public void doCreate() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }

        Inventory inventory = getT1();
        // 登记人
        inventory.setCreater(loginUser.getUuid());
        // 登记时间
        inventory.setCreatetime(new Date());
        // 状态
        inventory.setState(Inventory.STATE_CREATE);
        try {
            inventoryBiz.add(inventory);
            WebUtil.ajaxReturn(true, "登记成功");
        } catch (Exception e) {
            WebUtil.ajaxReturn(false, "登记失败");
        }
    }
    
    public void doCheck() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            inventoryBiz.doCheck(loginUser.getUuid(), getId());
            WebUtil.ajaxReturn(true, "审核成功");
        } catch (ErpException e) {
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            WebUtil.ajaxReturn(false, "审核失败");
        }
    }
}
