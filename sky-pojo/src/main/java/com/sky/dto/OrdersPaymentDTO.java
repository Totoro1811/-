package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单付款方式数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersPaymentDTO implements Serializable {
    //订单号
    private String orderNumber;
    //付款方式
    private Integer payMethod;
}
