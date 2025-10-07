package com.julia.model.HuiYuan;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-09-17 18:02
 **/
@Data
public class NewDataSet {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Table")
    private List<BillTable> table;
}
