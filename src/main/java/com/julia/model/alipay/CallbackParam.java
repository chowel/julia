package com.julia.model.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: julia
 * @description: 支付宝回调
 * @author: Chowel.Master https://www.merchant.com/receive_notify.htm?notify_type=trade_status_sync&
 **/

@Getter
@Setter
public class CallbackParam {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notify_time;

    private String notify_type;

    private String notify_id;

    private String app_id;

    private String charset;

    private String version;

    private String sign_type;

    private String sign;

    private String trade_no;

    private String out_trade_no;

    private String out_biz_no;

    private String buyer_id;

    private String buyer_logon_id;

    private String seller_id;

    private String seller_email;

    private String trade_status;

    private Double total_amount;

    private Double receipt_amount;

    private Double invoice_amount;

    private Double buyer_pay_amount;

    private Double point_amount;

    private Double refund_fee;

    private String subject;

    private String body;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_create;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_payment;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_refund;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_close;

    private List<Map<String, String>> fund_bill_list;

    private String passback_params;

    private List<Map<String, String>> voucher_detail_list;

}
