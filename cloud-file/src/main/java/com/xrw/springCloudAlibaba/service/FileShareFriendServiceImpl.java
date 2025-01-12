package com.xrw.springCloudAlibaba.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xrw.springCloudAlibaba.dao.FileShareFriendDao;
import com.xrw.springCloudAlibaba.entity.FileEntity;
import com.xrw.springCloudAlibaba.entity.FileShareFriendEntity;
import com.xrw.springCloudAlibaba.enums.SysDictItemEnum;
import com.xrw.springCloudAlibaba.exception.ApiError;
import com.xrw.springCloudAlibaba.exception.ApiException;
import com.xrw.springCloudAlibaba.utils.login.LoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @Description: 文件共享服务层
 * @Author: xearin
 * @Date: 2021-11-12
 */
@Slf4j
@Service("FileShareFriendServiceImpl")
public class FileShareFriendServiceImpl extends ServiceImpl<FileShareFriendDao, FileShareFriendEntity> {
    @Autowired
    private FileServiceImpl fileService;

    /**
     * @param fileId:     文件id
     * @param friendId:   共享文件的目标用户
     * @param permission: 共享权限,关联数据字典 SysDictEnum.file_share_permission
     * @param userId:     用户id
     * @Description: 新增或者修改文件共享权限
     * @return: void
     * @Author: xearin 1429382875@qq.com
     * @Date: 2021/11/29
     */
    public void save(Long fileId, Long friendId, String permission, Long userId) {
        FileEntity file = fileService.getById(fileId);
        if (file == null) {
            throw new ApiException(ApiError.DATA_NOT_EXISTS);
        }
        FileShareFriendEntity fileShareFriendEntity = baseMapper.selectOne(new QueryWrapper<FileShareFriendEntity>()
                .eq("share_from", userId)
                .eq("share_to", friendId)
                .eq("file_id", fileId));
        if (fileShareFriendEntity != null) {
            fileShareFriendEntity.setFileSharePermission(permission);
            baseMapper.updateById(fileShareFriendEntity);
            return;
        }
        fileShareFriendEntity = new FileShareFriendEntity().setFileSharePermission(permission)
                .setShareFrom(userId)
                .setShareTo(friendId)
                .setFileId(fileId);
        baseMapper.insert(fileShareFriendEntity);
    }

    /**
     * @param page:   页码，从1开始
     * @param size:   每页数量
     * @param select: 模糊查询关键字，按文件名称查询
     * @param userId: 用户id
     * @Description: 分页查询共享文件列表
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.xrw.springCloudAlibaba.entity.FileEntity>
     * @Author: xearin 1429382875@qq.com
     * @Date: 2021/11/29
     */
    public Page<FileEntity> getSelectPage(Long page, Long size, String select, Long userId) {
        Long offset = 0L;
        if (page != null && size != null) {
            offset = (page - 1) * size;
        }
        ArrayList<FileEntity> applicationEntities = baseMapper.getSelectPage(offset, size, select, userId);
        Long pageCount = baseMapper.getSelectPageCount(offset, size, select, userId);
        Page<FileEntity> entityPage = new Page<>(Optional.ofNullable(page).orElse(0L),
                Optional.ofNullable(size).orElse(0L),
                Optional.ofNullable(pageCount).orElse(0L));
        entityPage.setRecords(applicationEntities);
        return entityPage;
    }

    /**
     * @param id:     文件id
     * @param userId: 用户id
     * @Description: delete 删除文件
     * @return: void
     * @Author: xearin 1429382875@qq.com
     * @Date: 2021/11/29
     */
    public void delete(Long id, Long userId) {
        FileShareFriendEntity selectOne = baseMapper.selectOne(new QueryWrapper<FileShareFriendEntity>().eq("file_id", id)
                .eq("share_to", userId));
        if (selectOne == null) {
            throw new ApiException(ApiError.INTERFACE_UNPREMITTED);
        }
        if (!selectOne.getFileSharePermission().equals(SysDictItemEnum.file_share_permission_0.getKey())
                && !selectOne.getFileSharePermission().equals(SysDictItemEnum.file_share_permission_3.getKey())) {
            throw new ApiException(ApiError.INTERFACE_UNPREMITTED);
        }
        baseMapper.deleteById(selectOne.getId());
        fileService.delete(id, selectOne.getShareFrom());
    }

    /**
     * @param id:     文件id
     * @param userId: 用户id
     * @Description: deleteShare 删除共享关系（共享给他人或者收到的共享文件）
     * @return: void
     * @Author: xearin 1429382875@qq.com
     * @Date: 2021/11/29
     */
    public void deleteShare(Long id, Long userId) {
        FileShareFriendEntity selectOne = baseMapper.selectOne(new QueryWrapper<FileShareFriendEntity>().eq("file_id", id)
                .eq("share_to", userId));
        if (selectOne == null) {
            selectOne = baseMapper.selectOne(new QueryWrapper<FileShareFriendEntity>().eq("file_id", id)
                    .eq("share_from", userId));
            if (selectOne == null) {
                throw new ApiException(ApiError.DATA_NOT_EXISTS);
            }
        }
        baseMapper.deleteById(selectOne.getId());
    }
}