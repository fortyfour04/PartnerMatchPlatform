package com.forty.huoban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forty.huoban.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fortyfour
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-11-20 16:17:11
* @Entity com.forty.huoban.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




