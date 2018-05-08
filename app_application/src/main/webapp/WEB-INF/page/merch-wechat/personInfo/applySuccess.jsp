<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>商品分期</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <style>
        .titleImgContainer{text-align: center;margin-top: 44px;padding: 20px;}
        .titleImgContainer img{width: 35%}
        .titleImgContainer p:first-child{font-size: 20px}
        .titleImgContainer p:nth-child(2){font-size: 20px}
        .titleImgContainer p:last-child{font-size: 12px;color:#CACACA}
        .orderApplyMsgContainer{margin: 20px 15px;font-size: 14px;}
        .orderApplyMsgContainer p:first-child{color:#7C7C7C;font-weight: bold;margin: 4px auto;}
        .btnContainer{text-align: center}
        .bingCard{font-weight: bold;width: 80%;height: 40px;line-height: 40px;border: none;outline: none;margin: 10px auto;border-radius: 30px;background: #FFDA44}
        .checkOrder{font-weight: bold;width: 80%;height: 40px;line-height: 40px;border: 1px solid #FEE888; background: transparent;outline: none;margin: 10px auto;border-radius: 30px;}
        .commBtn .commBtn_w{background: url(${ctx}/resources/merch-wechat/images/commBtn_w.png) no-repeat center center;
            background-size: cover;
            color:#937729;
            outline: none;}
    </style>
</head>
<body style="background:#FAFAFA">
<input type="hidden" value="${param.orderId}">
    <div class="toolBar">
        <p><span>商品分期</span></p>
    </div>
    <div class="titleImgContainer" style="border-bottom: none">
        <p>商品分期信息</p>
        <p>已发送至办单员系统</p>
        <p>请尽快完成信用认证,完成申请流程。</p>
    </div>
    <div class="titleImgContainer" style="margin-top: 0">
        <img src="${ctx}/resources/merch-wechat/images/applySuccess.png" alt="">
    </div>
    <div class="commBtn">
        <button class="" style="margin: 30px auto;">查看分期订单</button>
        <button class="commBtn_w" style="margin: 0 auto;">返回首页</button>
    </div>
</body>
<script>
    $(".backHome").on("click",function () {
        window.location=_ctx+"/wechat/login/home?orderId=${param.orderId}"
    })
    $(".checkOrder").on("click",function () {
        window.location=_ctx+"/wechat/order/toForLendingView?orderId=${param.orderId}"
    })
</script>
</html>