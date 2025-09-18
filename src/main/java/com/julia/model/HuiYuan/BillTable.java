package com.julia.model.HuiYuan;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-09-17 17:50
 **/

@Data
public class BillTable {
    @JacksonXmlProperty(localName = "Bill_No")
    private String billNo;

    @JacksonXmlProperty(localName = "Bill_Time")
    private String billTime;

    @JacksonXmlProperty(localName = "Charge_Type")
    private String chargeType;

    @JacksonXmlProperty(localName = "Supplier_ID")
    private String supplierID;

    @JacksonXmlProperty(localName = "Client_ID")
    private String clientID;

    @JacksonXmlProperty(localName = "Client_Name")
    private String clientName;

    @JacksonXmlProperty(localName = "Client_User")
    private String clientUser;

    @JacksonXmlProperty(localName = "Category_Code")
    private String categoryCode;

    @JacksonXmlProperty(localName = "Product_Code")
    private String productCode;

    @JacksonXmlProperty(localName = "Product_Name")
    private String productName;

    @JacksonXmlProperty(localName = "Product_Num")
    private Integer productNum;

    @JacksonXmlProperty(localName = "Par_Price")
    private String parPrice;

    @JacksonXmlProperty(localName = "Game_Name")
    private String gameName;

    @JacksonXmlProperty(localName = "Game_Value")
    private String gameValue;

    @JacksonXmlProperty(localName = "Region_Name")
    private String regionName;

    @JacksonXmlProperty(localName = "Region_Value")
    private String regionValue;

    @JacksonXmlProperty(localName = "Server_Name")
    private String serverName;

    @JacksonXmlProperty(localName = "Server_Value")
    private String serverValue;

    @JacksonXmlProperty(localName = "Charge_Account")
    private String chargeAccount;

    @JacksonXmlProperty(localName = "Charge_Email")
    private String chargeEmail;

    @JacksonXmlProperty(localName = "Charge_Ext1")
    private String chargeExt1;

    @JacksonXmlProperty(localName = "Charge_Ext2")
    private String chargeExt2;

    @JacksonXmlProperty(localName = "From_IP")
    private String fromIP;

    @JacksonXmlProperty(localName = "From_Province_ID")
    private String fromProvinceID;

    @JacksonXmlProperty(localName = "Bill_Status")
    private String billStatus;
}
