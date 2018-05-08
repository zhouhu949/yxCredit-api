package com.zw.miaofuspd.user.service;

import com.base.util.DateUtils;
import com.zw.api.SendSmsApi;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.user.service.ISmsService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SmsServiceImpl extends AbsServiceBase implements ISmsService {
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private IDictService iDictService;
    /**
     * 向数据库中插入验证码
     */
    @Override
    public boolean insertSms(Map inMap) throws Exception {
        String tel=(String)inMap.get("tel");
        String host ="";
        String dxtdType = iDictService.getDictInfo("短信通道","DXTD");//短信地址
        String varCode = ((int) ((Math.random() * 9 + 1) * 100000)) + "";
        Map map = new HashMap();
        if("0".equals(dxtdType)){//代表创蓝短信
            host = iSystemDictService.getInfo("sms.app_sms_url");//短信地址
            String account = iSystemDictService.getInfo("sms.app_sms_account");//短信模板。数据字典拿
            String password = iSystemDictService.getInfo("sms.app_sms_password");//短信模板。数据字典拿
            String type = inMap.get("type").toString();
            String title = getSmsContent(type);
            String msg = title.replace("$code$",":"+varCode+"");
            inMap.put("varCode",varCode);
            inMap.put("account",account);
            inMap.put("password",password);
            inMap.put("msg",msg);
            map = (Map) SendSmsApi.sendSms(host,inMap);
        }else if("1".equals(dxtdType)){//代表阿里云短信
            host = iSystemDictService.getInfo("aliyunsms.host");//短信地址
            String minute = iSystemDictService.getInfo("aliyunsms.minute");//短信时长
            String appcode = iSystemDictService.getInfo("aliyunsms.appcode");//短信code
            String tNum = iSystemDictService.getInfo("aliyunsms.tNum ");//短信模板
            inMap.put("host",host);
            inMap.put("minute",minute);
            inMap.put("appcode",appcode);
            inMap.put("tNum",tNum);
            inMap.put("varCode",varCode);
            inMap.put("phone",tel);
            map = (Map) SendSmsApi.sendAliyun(host,inMap);
        }
        boolean success = (boolean) map.get("flag");
       if (success==false) {//判断获取验证码是否成功
           return false;
       }
        String id = UUID.randomUUID().toString();
        String date = DateUtils.getDateString(new Date());
        StringBuffer sql = new StringBuffer("INSERT INTO app_sms (id,type,tel,var_code,creat_time,alter_time) VALUES('");
        sql.append(id + "','" + 0 + "','" + tel + "','" + varCode + "','" + date + "','" + date + "')");
        sunbmpDaoSupport.exeSql(sql.toString());
        return true;
    }
    /**
     * 检查验证码是否正确
     */
    @Override
    public Map checkSms(Map inMap) throws Exception {
        //先查询验证码和用户是否正确
        Map outMap=new HashMap();
        String tel=(String)inMap.get("tel");
        String smsCode=(String)inMap.get("smsCode");
        String errortime=(String)inMap.get("errortime");
        List smslist=sunbmpDaoSupport.findForList("select var_code,tel,creat_time from app_sms where tel = '" + tel +"' and state ='0' ORDER BY creat_time DESC LIMIT 1");
        if (smslist.size() <= 0) {//判断验证码和用户名是否同时存在
            outMap.put("flag",false);
            outMap.put("msg","验证码输入有误，请重新输入");
            return outMap;
        }
        Map smsMap=(Map)smslist.get(0);
        if (smsCode != null && ! smsCode.equals(smsMap.get("var_code"))) {//判断使用的验证码是否为最新获取的
            outMap.put("flag",false);
            outMap.put("msg","验证码输入有误，请重新输入");
            return outMap;
        }
        if (!(DateUtils.isDateBig((String) smsMap.get("creat_time"), errortime))) { //判断验证码是否失效
            outMap.put("flag",false);
            outMap.put("msg","验证码已失效，请重新获取");
            return outMap;
        }
        //验证完成修改验证码的状态
        String upsql = "update app_sms set state ='1' where var_code='" + smsCode + "' and tel ='" + tel + "'";
        sunbmpDaoSupport.exeSql(upsql);
        outMap.put("flag",true);
        return outMap;
    }
    public String getSmsContent(String type){
        String sql = "select sms_content from sms_manage where sms_rule = '"+type+"' and sms_state='1' and platform_type='1'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return map.get("sms_content").toString();
    }
}
