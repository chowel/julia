package com.julia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;
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
@TableName("guji_player_weapon")
public class GujiPlayerWeaponEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "weapon_id", type = IdType.AUTO)
    private Long weaponId;

    /**
     * 玩家id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 武器id
     */
    @TableField("id")
    private Integer id;

    /**
     * 武器等级
     */
    @TableField("level")
    private Integer level;

    /**
     * 武器碎片
     */
    @TableField("fragment_cnt")
    private Integer fragmentCnt;
}
