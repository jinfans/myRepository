package cn.itcast.erp.biz.impl;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IGoodsBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IGoodstypeDao;
import cn.itcast.erp.entity.Goods;
import cn.itcast.erp.entity.Goodstype;

/**
 * 商品业务逻辑类
 *
 */
@Service("goodsBiz")
public class GoodsBiz extends BaseBiz<Goods> implements IGoodsBiz {

    private IGoodsDao goodsDao;
    @Autowired
	private IGoodstypeDao goodstypeDao;

    @Resource(name="goodsDao")
    public void setGoodsDao(IGoodsDao goodsDao) {
        this.goodsDao = goodsDao;
        super.setBaseDao(this.goodsDao);
    }

	
	@Override
	public void export(ServletOutputStream outputStream, Goods t1) throws IOException {
		// TODO Auto-generated method stub
		// 得到符合条件的所有数据
        List<Goods> list = goodsDao.getList(t1, null, null);
        // 创建一个工作簿 HSSF xls
        Workbook wk = new HSSFWorkbook();
        try {
            String shtName = "商品";
            // 创建工作表
            Sheet sht = wk.createSheet(shtName);
            // 创建行   表头
            Row row = sht.createRow(0);// 行的下标从0开始
            // 表头内容
            String[] headers = {"名称","产地","厂家","计量单位","进货价格","销售价格","商品类型"};
            // 宽度
            int[] widths = {4000,4000,6000,2000,4000,4000,4000,5000};
            int i = 0;
            for (; i < headers.length; i++) {
                // 表头
                row.createCell(i).setCellValue(headers[i]);
                sht.setColumnWidth(i, widths[i]);
            }
            // 内容
            i = 1;
            for (Goods goods : list) {
                if(null == goods) {
                    continue;
                }
                row = sht.createRow(i);
                
                String goodstypeName = goodstypeDao.findGoodsTypeName(goods.getGoodstype());
                
                row.createCell(0).setCellValue(goods.getName());
                row.createCell(1).setCellValue(goods.getOrigin());
                row.createCell(2).setCellValue(goods.getProducer());
                row.createCell(3).setCellValue(goods.getUnit());
                row.createCell(4).setCellValue(goods.getInprice());
                row.createCell(5).setCellValue(goods.getOutprice());
                row.createCell(6).setCellValue(goodstypeName);
                
                i++;
            }
            // 保存到输出流
            wk.write(outputStream);
        } finally {
            try {
                wk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	@Transactional
	public void doImport(FileInputStream fileInputStream) throws IOException {
		// TODO Auto-generated method stub
		// 读取工作簿
        Workbook wk = new HSSFWorkbook(fileInputStream);
        try {
            // 读取第一个工作表
            Sheet sht = wk.getSheetAt(0);

            // 最后一行的下标
            Row row = null;
            String name = null;
            String goodstypeName = null;
            Goods goods = null;
            Goodstype goodstype = null;
            List<Goods> list = null;
            List<Goodstype> goodstypeList = null;
            // i=1是要忽略表头行
            for(int i = 1; i <= sht.getLastRowNum(); i++) {
                row = sht.getRow(i);
                name = row.getCell(0).getStringCellValue();
                goodstypeName = row.getCell(6).getStringCellValue();
                
                goods = new Goods();
                goods.setName(name);
                // 判断是否存在
                list = goodsDao.getList(null, goods, null);
                if(list.size() > 0) {
                    // 存在
                    goods = list.get(0); // 进入持久化状
                }
                
                goodstype = new Goodstype();
                goodstype.setName(goodstypeName);
                // 判断是否存在
                goodstypeList = goodstypeDao.getList(null, goodstype, null);
                if(goodstypeList.size() > 0) {
                    // 存在
                	goodstype = goodstypeList.get(0); // 进入持久化状
                } else {
                	goodstypeDao.add(goodstype);
                }
                
                goods.setOrigin(row.getCell(1).getStringCellValue());
                goods.setProducer(row.getCell(2).getStringCellValue());
                goods.setUnit(row.getCell(3).getStringCellValue());
                goods.setInprice(row.getCell(4).getNumericCellValue());
                goods.setOutprice(row.getCell(5).getNumericCellValue());
                
                goods.setGoodstype(goodstype);
                
                if(list.size() == 0) {
                    //不存在
                    goodsDao.add(goods);
                    
                }
            }
        } finally {
            try {
                wk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

    
}
