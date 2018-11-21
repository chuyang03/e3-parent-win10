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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

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
        long id = IDUtils.genItemId();
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
        tbItemDescMapper.insert(tbItemDesc);

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

}
