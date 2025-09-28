package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.PlayersEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author chowel
 * @since 2025-09-28
 */
@Getter
@Setter
@ApiModel(value = "PlayersEntityVO", description = "用户表VO")
public class PlayersEntityVO extends BaseEntity {

    @ApiModelProperty("用户ID")
    private Long playId;

    @ApiModelProperty("用户标识")
    private String secure;

    @ApiModelProperty("web_URL")
    private String webUrl;

    @ApiModelProperty("访问IP")
    private String webIp;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("数据验证 0 没通过 1 通过")
    private Integer verify;

    @Override
    public String toString() {
        return "{" +
            "playId = " + playId +
            ", secure = " + secure +
            ", webUrl = " + webUrl +
            ", webIp = " + webIp +
            ", loginName = " + loginName +
            ", password = " + password +
            ", verify = " + verify +
        "}";
    }
}
