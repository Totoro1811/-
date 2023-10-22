package com.sky.controller.user;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrdersService;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: PayNotifyController
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : C端-微信支付回调表现层接口
 */
@Slf4j
@RestController
@RequestMapping("/notify")
public class PayNotifyController {

    //注入订单服务层接口实现Bean与微信支付参数Bean
    @Resource
    private OrdersService ordersService;
    @Resource
    private WeChatProperties weChatProperties;

    /**
     * 微信支付成功回调接口
     *
     * @param httpServletRequest  请求对象(支付成功相关信息)
     * @param httpServletResponse 响应对象(响应给微信支付)
     */
    @PostMapping("/paySuccess")
    public void paySuccessNotify(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, GeneralSecurityException {
        //读取本次微信回调的发送数据
        String payInfo = readRequestData(httpServletRequest);
        log.info("支付成功回调数据 : {}", payInfo);
        //对回调解析数据进行解密
        String decryptData = decryptData(payInfo);
        log.info("支付回调数据解密后数据 : {}", decryptData);
        //从解析数据中获取对应信息
        JSONObject payInfoJsonObject = JSON.parseObject(decryptData);
        String outTradeNo = payInfoJsonObject.getString("out_trade_no"); //获取商户平台订单编号
        String transactionId = payInfoJsonObject.getString("transaction_id"); //获取微信支付交易号
        log.info("商户平台订单号 : {} , 微信支付交易号 : {}", outTradeNo, transactionId);
        //业务处理(修改订单状态,来电提醒)后给微信响应
        ordersService.paySuccess(outTradeNo);
        response2WeChat(httpServletResponse);
    }

    /**
     * 读取本次请求支付数据信息
     *
     * @param httpServletRequest 本次请求支付数据封装请求对象
     * @return 支付信息
     */
    private String readRequestData(HttpServletRequest httpServletRequest) throws IOException {
        BufferedReader bufferedReader = httpServletRequest.getReader();
        StringBuilder result = new StringBuilder();
        String info = null;
        while ((info = bufferedReader.readLine()) != null) {
            if (info.length() > 0)
                result.append("\n");
            result.append(info);
        }
        return result.toString();
    }

    /**
     * 密文数据解密功能
     *
     * @param data 解密数据
     * @return 解密后数据
     */
    private String decryptData(String data) throws GeneralSecurityException {
        JSONObject resultJsonObject = JSON.parseObject(data);
        JSONObject resourceJsonObject = resultJsonObject.getJSONObject("resource");
        String ciphertext = resourceJsonObject.getString("ciphertext");
        String nonce = resourceJsonObject.getString("nonce");
        String associatedData = resourceJsonObject.getString("associated_data");
        AesUtil aesUtil = new AesUtil(weChatProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        return aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
    }

    /**
     * 根据支付结果响应微信
     *
     * @param httpServletResponse 用于响应结果的响应对象
     */
    private void response2WeChat(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(200);
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("code", "SUCCESS");
        resultMap.put("message", "SUCCESS");
        httpServletResponse.setContentType(ContentType.APPLICATION_JSON.toString());
        httpServletResponse.getOutputStream().write(JSONUtils.toJSONString(resultMap).getBytes(StandardCharsets.UTF_8));
        httpServletResponse.flushBuffer();
    }
}
