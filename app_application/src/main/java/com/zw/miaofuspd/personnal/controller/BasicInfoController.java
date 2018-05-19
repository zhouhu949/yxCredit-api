package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IDSMoneyServer;
import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by 韩梅生 on 2018/05/09 0028.
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION+AppRouterSettings.BASIC_MODUAL)
public class BasicInfoController extends AbsBaseController {
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    private IUserService userService;

    @Autowired
    private AppBasicInfoService appBasicInfoServiceImpl;

    @Autowired
    private IDSMoneyServer idsMoneyServer;

    /**
     * @author 韩梅生
     * 保存用户申请的基本信息
     * @param data
     * @return
     */
    @RequestMapping(value = "/addApplyInfo")
    public ResultVO addApplyInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addApplyInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * 获取用户申请的基本信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getApplyInfo")
    public ResultVO getApplyInfo(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getApplyInfo(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  保存用户的个人信息
     * @Date 14:07 2018/5/12
     * @param
     * @return
     */
    @RequestMapping("/addBasicInfo")
    public ResultVO addBasicInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addBasicInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }




    /**
     * @author:韩梅生
     * @Description 获取用户的个人信息
     * @Date 14:06 2018/5/12
     * @param
     * @return
     */
    @RequestMapping("/getBasicInfo")
    public ResultVO getBasicInfo(String customerId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getBasicInfo(customerId);
        if(!(Boolean)(map.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));
            return resultVO;
        }
        resultVO.setRetData(map.get("data"));
        return resultVO;
    }




    /**
     * @author:韩梅生
     * @Description  获取省份信息
     * @Date 13:40 2018/5/12
     * @param
     * @return com.zw.web.base.vo.ResultVO
     */
    @RequestMapping("/getProvinceList")
    public ResultVO getProvinceList() throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getProvinceList();
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  获取市的信息
     * @Date 13:54 2018/5/12
     * @param  provinceId 省id
     * @return
     */
    @RequestMapping("/getCityList")
    public ResultVO getCityList(String provinceId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getCityList(provinceId);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  获取区的信息
     * @Date 13:54 2018/5/12
     * @param  cityId 市的id
     * @return
     */
    @RequestMapping("/getDistrictList")
    public ResultVO getDistrictList(String cityId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getDistrictList(cityId);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description 获取实名认证信息
     * @Date 17:58 2018/5/16
     * @param
     */
    @RequestMapping("/getRealName")
    public ResultVO getRealName(String customerId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getRealName(customerId);
        resultVO.setRetData(map);
        return resultVO;
    }


    /**
     * @author:韩梅生
     * @Description 获取实名认证信息
     * @Date 17:58 2018/5/16
     * @param
     */
    @RequestMapping("/savaRealName")
    public ResultVO savaRealName(String data) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = JSONObject.parseObject(data);
        DSMoneyRequest request = new DSMoneyRequest();
        try {
            final Map customer = appBasicInfoServiceImpl.findById((String) map.get("customerId"));
            if (customer != null) {
                request.setBorrowerType(0);
                request.setBorrowerCardType("1");
                //app登录手机号码
                request.setBorrowerUserName((String) map.get("phone"));
                request.setBorrowerName(customer.get("PERSON_NAME").toString());
                request.setBorrowerMobilePhone(customer.get("TEL").toString());
                request.setBorrowerChannel("yxd");
                request.setBorrowerCardNo(customer.get("CARD").toString());
                request.setAddress(customer.get("company_address").toString());
                request.setAccountType("0");
                request.setAccountName(customer.get("PERSON_NAME").toString());
                request.setAccountIdCard(customer.get("CARD").toString());
                request.setOtherFlag("1");
                request.setAccountChannel("yxd");
                request.setAccountThirdId(request.getBorrowerThirdId());
                request.setProvinceCode((String) map.get("prov_id"));
                request.setProvinceName((String)map.get("prov_name"));
                request.setCityCode((String) map.get("city_id"));
                request.setCityName((String)map.get("city_name"));
                request.setBankCode((String)map.get("bank_number"));
                request.setBankName((String)map.get("bank_name"));
                BYXResponse byxResponse = idsMoneyServer.saveBorrowerAndAccountCard(request);
                if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                    final Map resData = (Map) byxResponse.getRes_data();
                    //数据同步更新个人信息到数据库
                    appBasicInfoService.updateSynById(
                            (String)map.get("customerId"),
                            (String)resData.get("userId"),
                            (String)resData.get("accountId")
                            );

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map resMap = appBasicInfoService.saveRealName(map);
        resultVO.setRetData(resMap);
        return resultVO;
    }


}
