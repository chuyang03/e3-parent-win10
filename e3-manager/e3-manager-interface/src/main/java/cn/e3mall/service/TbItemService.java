package cn.e3mall.service;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemParamItem;
import cn.e3mall.utils.E3Result;

public interface TbItemService {

    //根据商品id查询商品
    TbItem getItemById(long ItemId);

    //获取商品列表
    EasyUIDataGridResult getItemList(int page,int rows);

    //新增商品
    E3Result addItem(TbItem tbItem, String desc);

    //编辑商品
    TbItemDesc editItem(long id);

    //批量删除
    E3Result deteteBatch(String ids);

    //下架商品。可以批量
    E3Result instockItem(String ids);

    //上架商品
    E3Result reshelfItem(String ids);

}
