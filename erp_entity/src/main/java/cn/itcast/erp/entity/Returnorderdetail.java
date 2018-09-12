package cn.itcast.erp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.itcast.erp.entity.Returnorders;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 退货订单明细实体类
 */
@Entity
@Table(name = "returnorderdetail")
public class Returnorderdetail {

    /**
     * 采购退货明细的状态：未出库
     */
    public static final String STATE_NOT_OUT = "0";

    /**
     * 采购退货明细的状态：已出库
     */
    public static final String STATE_OUT = "1";

    /**
     * 销售退货明细的状态：未入库
     */
    public static final String STATE_NOT_IN = "0";

    /**
     * 销售退货明细的状态：已入库
     */
    public static final String STATE_IN = "1";
    @Id
    @GeneratedValue(generator = "returnorderdetailKeyGenerator", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "returnorderdetailKeyGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@Parameter(name = "sequence_name", value = "returnorderdetail_seq")}
    )
    /**
     * 编号
     */
    private Long uuid;
    /**
     * 商品编号
     */
    private Long goodsuuid;
    /**
     * 商品名称
     */
    private String goodsname;
    /**
     * 价格
     */
    private Double price;
    /**
     * 数量
     */
    private Long num;
    /**
     * 金额
     */
    private Double money;
    /**
     * 结束日期
     */
    private java.util.Date endtime;
    /**
     * 库管员
     */
    private Long ender;
    /**
     * 仓库编号
     */
    private Long storeuuid;
    /**
     * 状态
     */
    private String state;

    /**
     * 明细对应的订单
     */
    /*@ManyToOne(targetEntity = Returnorders.class)
    @JoinColumn(name = "ORDERSUUID")
    @JSONField(serialize = false)
    private Returnorders returnorders;*/

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getGoodsuuid() {
        return goodsuuid;
    }

    public void setGoodsuuid(Long goodsuuid) {
        this.goodsuuid = goodsuuid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public java.util.Date getEndtime() {
        return endtime;
    }

    public void setEndtime(java.util.Date endtime) {
        this.endtime = endtime;
    }

    public Long getEnder() {
        return ender;
    }

    public void setEnder(Long ender) {
        this.ender = ender;
    }

    public Long getStoreuuid() {
        return storeuuid;
    }

    public void setStoreuuid(Long storeuuid) {
        this.storeuuid = storeuuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @ManyToOne(targetEntity = Returnorders.class)
    @JoinColumn(name = "ORDERSUUID")
    @JSONField(serialize = false)    // 解决持续循环
    private Returnorders returnOrders;

    public Returnorders getReturnOrders() {
        return returnOrders;
    }

    public void setReturnOrders(Returnorders returnOrders) {
        this.returnOrders = returnOrders;
    }
}