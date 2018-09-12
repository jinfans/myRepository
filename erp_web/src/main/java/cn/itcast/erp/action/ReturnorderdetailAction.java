package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IReturnorderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("returnorderdetailAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("returnorderdetail")
public class ReturnorderdetailAction extends BaseAction<Returnorderdetail> {

    private IReturnorderdetailBiz returnorderdetailBiz;

    @Resource(name="returnorderdetailBiz")
    public void setReturnorderdetailBiz(IReturnorderdetailBiz returnorderdetailBiz) {
        this.returnorderdetailBiz = returnorderdetailBiz;
        super.setBaseBiz(this.returnorderdetailBiz);
    }
    
    private Long storeuuid;

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
    
	/**
	 * 采购退货订单出库
	 */
	public void doOurStore(){
		Emp loginUser = WebUtil.getLoginUser();
		if(null == loginUser){
			WebUtil.ajaxReturn(false, "没有登录");
			return;
		}
		try {
			returnorderdetailBiz.doOutStore(getId(), storeuuid, loginUser.getUuid());
			WebUtil.ajaxReturn(true, "出库成功");
		} catch (ErpException e) {
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "出库失败");
		}
	}
	
	public void doInStore() {
    	// 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
			returnorderdetailBiz.doInStore(loginUser.getUuid(), storeuuid, getId());
			WebUtil.ajaxReturn(true, "入库成功");
        } catch (ErpException e) {
			WebUtil.ajaxReturn(false,e.getMessage());
		} catch (Exception e) {
			WebUtil.ajaxReturn(false, "入库失败");
			
			e.printStackTrace();
		}
    }
}
