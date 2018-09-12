package cn.itcast.erp.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.util.MailUtil;

@Component("mailJob2")
public class MailJob2 {
	@Autowired
    private MailUtil mailUtil;

    @Autowired
    private IOrdersBiz ordersBiz;

    @Value("${mail.orders_title}")
    private String title;
    @Value("${mail.orders_to}")
    private String to;
    @Value("${mail.orders_content}")
    private String content;

    public void doJob() {
    	Orders orders=new Orders();
    	orders.setType(Orders.TYPE_OUT);
    	orders.setState(Orders.STATE_NOT_OUT);
        List<Orders> list = ordersBiz.getList(orders, null, null);
        System.out.println(list.size());
        if(null != list && list.size() > 0) {
            // 有预警的商品

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String _title = title.replace("[time]",sdf.format(new Date()));
            String _content = content.replace("[count]", list.size() + "");
            // 存在库存预警
            try {
                mailUtil.sendMail(_title, to, _content);
                System.out.println("邮件发送成功");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
