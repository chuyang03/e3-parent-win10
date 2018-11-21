package cn.e3mall.service;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemParamItem;
import cn.e3mall.utils.E3Result;

public interface TbItemService {

    TbItem getItemById(long ItemId);

    EasyUIDataGridResult getItemList(int page,int rows);

    E3Result addItem(TbItem tbItem, String desc);

    TbItemDesc editItem(long id);

    E3Result deteteBatch(String ids);
}
