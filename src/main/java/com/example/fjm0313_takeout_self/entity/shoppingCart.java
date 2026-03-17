package com.example.fjm0313_takeout_self.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class shoppingCart implements Serializable {
    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long dishId;

    private String name;

    private String image;

    private Integer number;

    private BigDecimal amount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
