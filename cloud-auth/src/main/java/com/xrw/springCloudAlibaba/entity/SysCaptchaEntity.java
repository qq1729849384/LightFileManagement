
package com.xrw.springCloudAlibaba.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 系统验证码
 *
 * @author xearin
 */
@Data
@TableName("captcha")
public class SysCaptchaEntity extends Model<SysCaptchaEntity> {
    @TableId(type = IdType.INPUT)
    private String uuid;
    /**
     * 验证码
     */
    @TableField(value = "code")
    private String errorcode;
    /**
     * 过期时间
     */
    private Date expireTime;

}
