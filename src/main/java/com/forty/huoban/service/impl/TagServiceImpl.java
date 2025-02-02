package com.forty.huoban.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.huoban.service.TagService;
import com.forty.huoban.model.domain.Tag;
import com.forty.huoban.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author fortyfour
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-11-20 16:10:09
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

}




