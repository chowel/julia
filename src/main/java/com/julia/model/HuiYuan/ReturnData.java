package com.julia.model.HuiYuan;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-09-17 18:03
 **/
@Data
@JacksonXmlRootElement(localName = "Return")
public class ReturnData {
    @JacksonXmlProperty(localName = "RetCode")
    private Integer retCode;

    @JacksonXmlProperty(localName = "RetMsg")
    private String retMsg;

    @JacksonXmlProperty(localName = "AgentID")
    private String agentID;

    @JacksonXmlProperty(localName = "DownLoadTime")
    private String downLoadTime;

    @JacksonXmlProperty(localName = "NewDataSet")
    private NewDataSet newDataSet;
}
