package cn.itcast.erp.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Store;

/**
 * 仓库业务逻辑类
 *
 */
@Service("storeBiz")
public class StoreBiz extends BaseBiz<Store> implements IStoreBiz {

    private IStoreDao storeDao;

    @Resource(name="storeDao")
    public void setStoreDao(IStoreDao storeDao) {
        this.storeDao = storeDao;
        super.setBaseDao(this.storeDao);
    }
    
    
    
    //仓库添加库管员姓名字段
    @Autowired
    private IEmpDao empdao;
    @Override
    public java.util.List<Store> getList(Store t1, Store t2, Object obj) {
    	// TODO Auto-generated method stub
    	List<Store> list = super.getList(t1, t2, obj);
    	for (Store store : list) {
			store.setEmpname(getEmpName(store.getEmpuuid()));
		}
    	return  list;
    }
    private String getEmpName(Long empuuid) {
    	Emp emp = empdao.get(empuuid);
    	if (empuuid == null || emp == null) {
			return null;
		}
    	return emp.getName();
    }
    
}
