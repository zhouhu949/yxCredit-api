package com.api.service.ds;

import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;

/**
 * 借款人及放款账户数据同步业务层
 * @author 陈清玉
 */
public interface IDSMoneyBusiness {

    String BEAN_KEY = "dSMoneyBusinessImpl";
    /**
     * 放款账户数据同步
     * @param response 借款实体
     * @return 数据同步
     */
    BYXResponse syncData(DSMoneyRequest response);
    /**
     * 放款账户数据同步
     * @param orderId 订单ID
     * @return 数据同步
     */
    BYXResponse syncData(String orderId);
}
