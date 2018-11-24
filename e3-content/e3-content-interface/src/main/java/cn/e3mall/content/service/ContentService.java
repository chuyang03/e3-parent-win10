package cn.e3mall.content.service;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.utils.E3Result;

import java.util.List;

public interface ContentService {
    E3Result addContent(TbContent tbContent);

    //根据分类id查询内容列表
    List<TbContent> getContentListByCid(Long cid);
}
