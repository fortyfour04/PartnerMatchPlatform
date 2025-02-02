package com.forty.huoban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forty.huoban.model.domain.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fortyfour
* @description 针对表【tag(标签)】的数据库操作Mapper
* @createDate 2024-11-20 16:10:09
* @Entity com.forty.huoban.domain.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




