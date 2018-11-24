package cn.e3mall.content.service.impl;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Override
    public E3Result addContent(TbContent tbContent) {

        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());

        tbContentMapper.insertSelective(tbContent);
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentListByCid(Long cid) {

        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        return list;
    }
}
