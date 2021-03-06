package com.zw.api.ds.controller;

import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.bankcard.IBankcardServer;
import com.api.service.ds.IDSMoneyBusiness;
import com.api.service.ds.IDSMoneyServer;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 借款人及放款账户数据同步而业务层实现
 * @author 陈清玉
 */
@Component(IDSMoneyBusiness.BEAN_KEY)
public class DSMoneyBusinessImpl  implements IDSMoneyBusiness{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppBasicInfoService appBasicInfoService;

    @Autowired
    private IDSMoneyServer idsMoneyServer ;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private IBankcardServer bankcardServer;

    @Override
    public BYXResponse syncData(String orderId) {
        try {
            final Map customer = appBasicInfoService.findByOrderId(orderId);
            if (customer != null) {
                if(isSync(customer)){
                    LOGGER.info("------借款人及放款账户数据已经同步过了------");
                    return BYXResponse.ok();
                }
                String custId = customer.get("custId").toString();
                DSMoneyRequest request = new DSMoneyRequest();
                request.setBorrowerType(0);
                request.setBorrowerCardType("1");
                request.setBorrowerName(customer.get("custName").toString());
                request.setBorrowerMobilePhone(customer.get("custPhone").toString());
                request.setBorrowerThirdId(custId);
                request.setBorrowerCardNo(customer.get("custCard").toString());
                request.setAddress(customer.get("nowaddress").toString());
                request.setAccountType("0");
                request.setAccountName(customer.get("custName").toString());
                request.setAccountIdCard(customer.get("custCard").toString());
                request.setAccountThirdId(custId);
                return doSyncData(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
           return BYXResponse.error(e.getMessage());
        }
        return BYXResponse.error();
    }


    @Override
    public BYXResponse syncData(DSMoneyRequest request){
        List<Map> customerMap = appBasicInfoService.getCustomerIdByid(request.getAppUserId());
        if (!CollectionUtils.isEmpty(customerMap)) {
            Map map = customerMap.get(0);
            if (isSync(map)) {
                LOGGER.info("------借款人及放款账户数据已经同步过了------");
                return BYXResponse.ok();
            }
            try {
                request.setBorrowerThirdId(map.get("id").toString());
                return doSyncData(request);
            } catch (Exception e) {
                e.printStackTrace();
                return BYXResponse.error(e.getMessage());
            }
        }
        return BYXResponse.error("找不到个人用户信息");
    }

    /**
     * 同步数据
     * @param request 借款人账户信息实体
     * @return 返回结果
     * @throws Exception http 异常
     */
    private BYXResponse  doSyncData(DSMoneyRequest request) throws Exception {
        //app登录手机号码
        request.setBorrowerUserName(appUserService.getPhoneById(request.getAppUserId()));
        //获取银行卡信息
        boolean flag = setBankInfo(request);
        if(!flag){
            return BYXResponse.error("找不到对应银行卡信息");
        }
        BYXResponse byxResponse = idsMoneyServer.saveBorrowerAndAccountCard(request);
        //数据同步接口一个相同的实名只会同步一次，所以不用判断是否成功
        if(byxResponse != null) {
            final Map resData = (Map) byxResponse.getRes_data();
            if(resData != null) {
                if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                    //数据同步更新个人信息到数据库
                    appBasicInfoService.updateSynById(
                            resData.get("userId").toString(),
                            resData.get("accountId").toString(),
                            request.getBorrowerThirdId()
                    );
                    LOGGER.info("------借款人及放款账户数据同步更新个人信息到数据库成功------");
                    return BYXResponse.ok(byxResponse.getRes_msg());
                }
            }
            return byxResponse;
        }else{
            LOGGER.info("------借款人及放款账户数据同步更新个人信息到数据库失败------");
        }
        return BYXResponse.error();
    }
    /**
     * 获取银行卡信息 并返回数据
     * @param request 请求数据实体
     */
    private boolean setBankInfo(DSMoneyRequest request) {
        List<Map> bankcardInfoList = bankcardServer.findSysBankcardInfoByCustId(request.getBorrowerThirdId());
        if(CollectionUtils.isEmpty(bankcardInfoList)){
            LOGGER.info("----银行卡信息为空---");
            return false;
        }
        Map bankInfo = bankcardInfoList.get(0);
        //借款人名称
        request.setBorrowerName(bankInfo.get("cust_name").toString());
        //借款人手机号
        request.setBorrowerMobilePhone(bankInfo.get("tel").toString());
        request.setBorrowerThirdId(request.getBorrowerThirdId());
        //借款人证件号
        request.setBorrowerCardNo(bankInfo.get("card").toString());
        request.setAccountName(bankInfo.get("cust_name").toString());
        request.setAccountIdCard(bankInfo.get("card").toString());
        //放款人证件号
        request.setAccountThirdId(request.getBorrowerThirdId());
        //个人
        request.setAccountType(bankInfo.get("bank_type").toString());
        //个人
        request.setBorrowerType(Integer.valueOf(bankInfo.get("bank_type").toString()));
        //银行编号
        request.setBankCode(String.valueOf(bankInfo.get("bank_number")));
        //银行名称
        request.setBankName(bankInfo.get("bank_name").toString());
        //银行卡号
        request.setBankCardNo(bankInfo.get("card_number").toString());
        request.setProvinceCode(bankInfo.get("prov_id") == null ? "" : String.valueOf(bankInfo.get("prov_id")));
        request.setProvinceName(bankInfo.get("prov_name") == null ? "" : bankInfo.get("prov_name").toString());
        request.setCityCode(bankInfo.get("city_id") == null ? "" : String.valueOf(bankInfo.get("city_id")));
        request.setCityName(bankInfo.get("city_name") == null ? "" : bankInfo.get("city_name").toString());
        request.setBankBranchName(bankInfo.get("bank_subbranch") == null ? "" : bankInfo.get("bank_subbranch").toString());
        request.setCnapsCode(bankInfo.get("bank_subbranch_id") == null ? "" : bankInfo.get("bank_subbranch_id").toString());
        return true;
    }

    /**
     *  是否同步过数据
     * @param customer
     * @return
     */
    private boolean isSync(Map customer){
       return(customer.get("sync_account_id") != null  &&  customer.get("sync_user_id") != null);
    }

}
