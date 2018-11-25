package cn.e3mall.content.service.impl;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private JedisClient jedisClient;

    //从属性文件中取出key变量
    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent tbContent) {

        //将内容数据插入到内容表
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());

        //插入到数据库
        tbContentMapper.insertSelective(tbContent);

        //缓存同步，删除缓存中对应的缓存数据，其实增删改都要实现同步缓存，此处只实现了添加功能
        //原理就是：在数据库添加内容后，缓存没有更新，如果删除该field对应的字段，
        // 当刷新页面时会重新查找数据库添加缓存内容
        jedisClient.hdel(CONTENT_LIST,tbContent.getCategoryId().toString());

        return E3Result.ok();
    }

    //根据分类id查询内容列表
    @Override
    public List<TbContent> getContentListByCid(Long cid) {

        //查询缓存
        try {
            //如果缓存中有，则直接响应
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if (StringUtils.isNotBlank(json)){
                //TbContent.class表示的是list中的元素是什么类型
                List<TbContent> tbContentList = JsonUtils.jsonToList(json, TbContent.class);

                return tbContentList;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //如果缓存中没有查询数据库
        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list = tbContentMapper.selectByExample(example);

        //添加到缓存
        try {

            jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
