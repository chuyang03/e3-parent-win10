package cn.e3mall.search.mapper;

import cn.e3mall.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {

    List<SearchItem> getItemList();

    //要将商品信息添加到索引库，只需要将新添加的商品id拿到，到数据库中查找就可以了
    SearchItem getItemById(long itemId);
}
