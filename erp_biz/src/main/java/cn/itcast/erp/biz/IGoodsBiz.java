package cn.itcast.erp.biz;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import cn.itcast.erp.entity.Goods;
/**
 * 商品业务逻辑层接口
 *
 */
public interface IGoodsBiz extends IBaseBiz<Goods>{

	void export(ServletOutputStream outputStream, Goods t1) throws IOException;

	void doImport(FileInputStream fileInputStream) throws IOException;


}

