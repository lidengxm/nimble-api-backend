package com.lmeng.api.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmeng.apicommon.model.entity.User;
import com.lmeng.apicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 26816
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo();

}




