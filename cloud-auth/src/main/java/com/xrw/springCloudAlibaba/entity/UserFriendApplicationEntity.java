package com.xrw.springCloudAlibaba.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @program: LightFileManagement
 * @description: 好友表
 * @author: 谢荣旺 1429382875@qq.com
 * @create: 2021-11-12 09:43
 **/
@Data
@Accessors(chain = true)
@TableName(value = "user_friend")
public class UserFriendApplicationEntity {
    @TableId
    private Long id;
    private Integer revision;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Long createUserId;
    private Long updateUserId;
    @TableLogic
    private Integer isDeleted;

    /**
     * 申请者
     */
    private Long applyFromId;
    /**
     * 被申请者
     */
    private Long applyToId;
    /**
     * 通过时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date agreeTime;
    /**
     * 处置时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date disposeTime;
    /**
     * 申请状态，关联字典apply_status
     */
    private String applyStatus;
}