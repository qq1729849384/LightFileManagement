package com.xrw.springCloudAlibaba.controller;

import com.xrw.springCloudAlibaba.entity.SysUserEntity;
import com.xrw.springCloudAlibaba.service.SysUserServiceImpl;
import com.xrw.springCloudAlibaba.vo.ResponseJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: LightFileManagement
 * @description: 系统用户控制器层
 * @author: 谢荣旺 1429382875@qq.com
 * @create: 2021-11-15 10:03
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class SysUserController {
    @Autowired
    private SysUserServiceImpl userService;

    /**
     * @param search: 关键字
     * @Description: 通过关键字全匹配用户名、昵称、手机号、邮箱查找用户
     * @return: com.xrw.springCloudAlibaba.vo.ResponseJSON
     * @Author: xearin 1429382875@qq.com
     * @Date: 2021/11/17
     */
    @RequestMapping("/select/perfectMatching")
    public ResponseJSON perfectMatching(@RequestParam(value = "search", required = false) String search) {
        if (StringUtils.isEmpty(search)) {
            return new ResponseJSON(userService.list());
        }
        List<SysUserEntity> sysUserEntities = userService.perfectMatching(search);
        return new ResponseJSON(sysUserEntities);
    }

    // todo 根据用户id查询详细信息
}
