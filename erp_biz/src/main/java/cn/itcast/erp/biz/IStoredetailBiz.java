package cn.itcast.erp.biz;

import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

/**
 * 仓库库存业务逻辑层接口
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{

    /**
     * 获取库存预警列表
     * @return
     */
    List<Storealert> getStorealertList();

    /**
     * 发送预警邮件
     */
    void sendStorealertMail() throws MessagingException;

    /**
     * 获取仓库里所有商品
     * @param storeuuid 仓库id
     * @return
     */
    List<Map<String, Object>> getGoodsByStore(Long storeuuid);
}

