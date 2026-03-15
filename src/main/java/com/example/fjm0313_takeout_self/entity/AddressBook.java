package com.example.fjm0313_takeout_self.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AddressBook implements Serializable {
    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private long id;

    private String userId;

    private String consignee;

    private String phone;

    private Integer sex;

    private String ProvinceName;

    private String cityName;

    private String districtName;

    private Integer IsDefault;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime currentTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
