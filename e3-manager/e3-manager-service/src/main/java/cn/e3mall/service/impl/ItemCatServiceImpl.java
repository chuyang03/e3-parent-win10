package cn.e3mall.service.impl;

import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {

        //设置查询条件，执行查询
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);

        //设置返回结果的list
        List<EasyUITreeNode> resultList = new ArrayList<>();

        //将查询结果列表转化成EasyUITreeNode节点列表
        for (TbItemCat tbItemCat : tbItemCats){
            EasyUITreeNode node = new EasyUITreeNode();

            //设置属性
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");

            //添加到返回结果列表
            resultList.add(node);
        }

        return resultList;
    }
}
