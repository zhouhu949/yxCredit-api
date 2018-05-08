package com.zw.miaofuspd.login.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.ILoginService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController extends AbsBaseController {
    @Autowired
    ILoginService loginService;
    /**
     * 秒付登录
     * @param phone
     * @param password
     * @param registration_id
     * @param deviceCode
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResultVO login(@RequestParam String phone, @RequestParam String password,
                          @RequestParam String registration_id, @RequestParam String black_box, @RequestParam String type, @RequestParam String deviceCode) throws Exception {
        //获取客户端的Ip地址
        String ip_address = getRemortIP();
        ResultVO resultVO = this.createResultVO(null);
        Map map = loginService.login(phone, password, registration_id,black_box,type,ip_address,deviceCode);
        boolean flag = (boolean) map.get("success");
        String msg = (String) map.get("msg");
        Map returnmap = new HashMap();
        if (flag) {
            AppUserInfo user = new AppUserInfo();
            user.setId(map.get("id") + "");
            user.setTel(map.get("tel") + "");
            user.setImg_url(map.get("img_url") + "");
            user.setRegistration_id(registration_id);
            if((Boolean) map.get("isIdentity")){
                user.setName(map.get("person_name") + "");
                user.setBg_cust_info_id(map.get("bg_cust_info_id") + "");
                user.setBg_customer_id(map.get("bg_customer_id") + "");
                user.setCrm_cust_info_id(map.get("crm_cust_info_id") + "");
                user.setCustomer_id(map.get("customer_id") + "");
                user.setCardTypeId(map.get("card_type_id") + "");
                user.setCardType(map.get("CARD_TYPE") + "");
                user.setCard(map.get("CARD") + "");
                user.setSex(map.get("sex") + "");
                user.setSexName(map.get("sex_name") + "");
                user.setEmp_id(map.get("emp_id") + "");
                user.setPerson_id(map.get("person_id") + "");
                user.setOccupation_type(map.get("occupation_type") + "");
                user.setIsBlack(map.get("is_black") + "");
            }
            Map map2 = new HashMap();
            String token = map.get("token")+"";
            map2.put("token",token);
            map2.put("tel",phone);
            map2.put("token_time",map.get("token_time"));
            map2.put(AppConstant.APP_USER_INFO,user);
            getRequest().getSession().setAttribute(AppConstant.APP_USER_INFO, user);
            returnmap.put("userId",map.get("id"));
            returnmap.put("isBlack",map.get("is_black"));
            returnmap.put("token",token);
            returnmap.put("referenceId",map.get("id"));
            returnmap.put("sessionId",getRequest().getSession().getId());
            resultVO.setRetData(returnmap);
        } else {
            resultVO.setErrorMsg(VOConst.FAIL, msg);
            resultVO.setRetData(map);
        }
        return resultVO;
    }
    public String getRemortIP() {
        if (getRequest().getHeader("x-forwarded-for") == null) {
            return getRequest().getRemoteAddr();
        }
        return getRequest().getHeader("x-forwarded-for");
    }
}