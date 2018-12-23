package cn.e3mall.service.impl;

import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbItemParamItemMapper;
import cn.e3mall.pojo.*;
import cn.e3mall.service.TbItemService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    //根据id属性注入
    @Resource
    private Destination topicDestination;

    @Override
    public TbItem getItemById(long ItemId) {

        TbItem tbItem = tbItemMapper.selectByPrimaryKey(ItemId);
/**
        TbItemExample example = new TbItemExample();
        //设置查询条件
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ItemId);

        //执行查询
        List<TbItem> list = tbItemMapper.selectByExample(example);

        if (list!=null&&list.size()>0){

            return list.get(0);
        }

        return null;
 */

        return tbItem;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {

        //设置分页信息
        PageHelper.startPage(page,rows);

        //设置查询条件
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);

        //显示分页结果
        PageInfo<TbItem> pageInfo=new PageInfo<>(list);

        //创建返回结果对象
        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);

        return result;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //生成商品id
        final long id = IDUtils.genItemId();
        //补全商品信息
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //插入商品表
        tbItemMapper.insert(tbItem);

        //创建商品描述信息类
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        tbItemDescMapper.insert(tbItemDesc);

        //商品添加完成后，向activemq发送一个添加商品的id的消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //创建发送的消息
                TextMessage textMessage = session.createTextMessage(id + "");
                return textMessage;
            }
        });

        return E3Result.ok();
    }

    @Override
    public TbItemDesc editItem(long itemId) {

        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        return tbItemDesc;
    }

    @Override
    public E3Result deteteBatch(String ids) {
        //判断ids不为空
        if(StringUtils.isNoneBlank(ids)){
            //分割ids
            String[] split = ids.split(",");
            for ( String id : split ) {
                //商品表删除
                tbItemMapper.deleteByPrimaryKey(Long.valueOf(id));
                //商品描述表删除
                tbItemDescMapper.deleteByPrimaryKey(Long.valueOf(id));
            }
            return E3Result.ok();
        }
        return null;
    }

    @Override
    public E3Result instockItem(String ids) {
        //判断id不为空
        if (StringUtils.isNoneBlank(ids)){
            String[] strings=ids.split(",");

            for (String id:strings){
                TbItem item = tbItemMapper.selectByPrimaryKey(Long.valueOf(id));
                //商品状态，1-正常，2-下架，3-删除
                item.setStatus((byte) 2);
                //保存商品，修改过后要保存
                tbItemMapper.updateByPrimaryKey(item);
            }
            return E3Result.ok();
        }

        return null;
    }

    @Override
    public E3Result reshelfItem(String ids) {
        if (StringUtils.isNoneBlank(ids)){
            String[] strings=ids.split(",");

            for (String id:strings){
                TbItem item = tbItemMapper.selectByPrimaryKey(Long.valueOf(id));
                //商品状态，1-正常，2-下架，3-删除
                item.setStatus((byte) 1);
                //保存商品，修改过后要保存
                tbItemMapper.updateByPrimaryKey(item);
            }
            return E3Result.ok();
        }

        return null;
    }

}
