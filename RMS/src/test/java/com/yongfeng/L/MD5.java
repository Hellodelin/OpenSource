package com.yongfeng.L;

import SystemManage.Common.until.DateUtil;
import SystemManage.Common.until.GlobalUtil;
import SystemManage.Common.until.MyUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SocketHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MD5 {

    @Test
    public void index1(){
        Md5Hash md5_1 = new Md5Hash("123");
        System.out.println(md5_1);
        Md5Hash md5_2 = new Md5Hash("123", "admin");
        System.out.println(md5_2);
        Md5Hash md5_3 = new Md5Hash("123", "admin", 1024);
        System.out.println(md5_3);
    }


    // 花里胡哨
    @Test
    public void index2(){
        System.out.println("得到的UUID是: " + MyUtil.getStrUUID());
    }

    // 获取当前时间
    @Test
    public void index3(){
        System.out.println( "当前时间为: " + MyUtil.getNowDateStr2() ) ;
    }

    // 10天前的时间为
    @Test
    public void index4(){
        int logDays = Integer.valueOf(GlobalUtil.getValue("log.days"));
        Date date = DateUtil.getDate(DateUtil.getDate(), -logDays);
        System.out.println("10天前的时间为 : " + date );
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str=sdf.format(date);
        System.out.println("转换之后的时间为 : " + str );

    }
}
