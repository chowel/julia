package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GujiPlayerWeaponEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 玩家武器
 * </p>
 *
 * @author chowel
 * @since 2025-12-21
 */
@Getter
@Setter
@ApiModel(value = "GujiPlayerWeaponEntityVO", description = "玩家武器VO")
public class GujiPlayerWeaponEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Long weaponId;

    @ApiModelProperty("玩家id")
    private Long userId;

    @ApiModelProperty("武器id")
    private Integer id;

    @ApiModelProperty("武器等级")
    private Integer level;

    @ApiModelProperty("武器碎片")
    private Integer fragmentCnt;

    @Override
    public String toString() {
        return "{" +
            "weaponId = " + weaponId +
            ", userId = " + userId +
            ", id = " + id +
            ", level = " + level +
            ", fragmentCnt = " + fragmentCnt +
        "}";
    }
}
