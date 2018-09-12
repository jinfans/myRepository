package cn.itcast.erp.entity;

import java.util.List;

import javax.persistence.*;

import cn.itcast.erp.entity.Returnorderdetail;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 退货订单实体类
 */
@Entity
@Table(name = "returnorders")
public class Returnorders {

    /**
     * 订单类型：采购退货
     */
    public static final String TYPE_OUT = "1";

    /**
     * 订单类型：销售退货
     */
    public static final String TYPE_IN = "2";

    /**
     * 采购退货订单状态：未审核
     */
    public static final String STATE_CREATE = "0";

    /**
     * 采购退货订单状态：已审核
     */
    public static final String STATE_CHECK = "1";

    /**
     * 采购退货订单状态：已出库
     */
    public static final String STATE_OUT = "2";

    /**
     * 销售退货订单状态：未审核
     */
    public static final String _STATE_CREATE = "0";

    /**
     * 销售退货订单状态：已审核
     */
    public static final String _STATE_CHECK = "1";
    /**
     * 销售退货订单状态：已入库
     */
    public static final String _STATE_IN = "2";


    @Id
    @GeneratedValue(generator = "returnordersKeyGenerator", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "returnordersKeyGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@Parameter(name = "sequence_name", value = "returnorders_seq")}
    )
    private Long uuid;//编号
    private java.util.Date createtime;//生成日期
    private java.util.Date checktime;//检查日期
    private java.util.Date endtime;//结束日期
    private String type;//订单类型
    private Long creater;//下单员
    private Long checker;//审核员工编号
    private Long ender;//库管员
    private Long supplieruuid;//供应商及客户编号
    private Double totalmoney;//合计金额
    private String state;//订单状态
    private Long waybillsn;//运单号
    private Long ordersuuid;//原订单编号

    /**
     * 下单员名称
     */
    @Transient
    private String createrName;
    /**
     * 审核员名称
     */
    @Transient
    private String checkerName;
    /**
     * 库管员名称
     */
    @Transient
    private String enderName;
    /**
     * 供应商名称
     */
    @Transient
    private String supplierName;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public java.util.Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(java.util.Date createtime) {
        this.createtime = createtime;
    }

    public java.util.Date getChecktime() {
        return checktime;
    }

    public void setChecktime(java.util.Date checktime) {
        this.checktime = checktime;
    }

    public java.util.Date getEndtime() {
        return endtime;
    }

    public void setEndtime(java.util.Date endtime) {
        this.endtime = endtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getChecker() {
        return checker;
    }

    public void setChecker(Long checker) {
        this.checker = checker;
    }

    public Long getEnder() {
        return ender;
    }

    public void setEnder(Long ender) {
        this.ender = ender;
    }

    public Long getSupplieruuid() {
        return supplieruuid;
    }

    public void setSupplieruuid(Long supplieruuid) {
        this.supplieruuid = supplieruuid;
    }

    public Double getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(Double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getWaybillsn() {
        return waybillsn;
    }

    public void setWaybillsn(Long waybillsn) {
        this.waybillsn = waybillsn;
    }

    public Long getOrdersuuid() {
        return ordersuuid;
    }

    public void setOrdersuuid(Long ordersuuid) {
        this.ordersuuid = ordersuuid;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getEnderName() {
        return enderName;
    }

    public void setEnderName(String enderName) {
        this.enderName = enderName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    //CascadeType.PERSIST  相对于save
    @OneToMany(targetEntity = Returnorderdetail.class, cascade = {CascadeType.ALL}, mappedBy = "returnOrders")
    private List<Returnorderdetail> returnOrderDetails; // 销售订单明细

    public List<Returnorderdetail> getReturnOrderDetails() {
        return returnOrderDetails;
    }

    public void setReturnOrderDetails(List<Returnorderdetail> returnOrderDetails) {
        this.returnOrderDetails = returnOrderDetails;
    }
}