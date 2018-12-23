package cn.e3mall.content.service.impl;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        TbContentCategoryExample example=new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(example);

        List<EasyUITreeNode> nodeList=new ArrayList<>();
        for (TbContentCategory tbContentCategory:tbContentCategories){
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");

            nodeList.add(node);
        }
        return nodeList;
    }

    @Override
    public E3Result addContentCatefory(long parentId, String name) {
        //创建ContentCategory表的pojo对象
        TbContentCategory tbContentCategory=new TbContentCategory();

        //补全pojo对象的属性
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setSortOrder(1);
        //1(正常),2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //插入表中
        tbContentCategoryMapper.insert(tbContentCategory);
        //插入数据后判断父节点isParent属性是否为true，如果不是true设置为true
        //先根据parentId查询这条数据
        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!category.getIsParent()){
            category.setIsParent(true);
            //更新数据库
            tbContentCategoryMapper.updateByPrimaryKey(category);
        }
        //返回结果，返回E3Result，包含pojo
        return E3Result.ok(tbContentCategory);
    }

    @Override
    public E3Result updateContentCategory(Long id, String name) {

        //根据id查询ContentCategory表记录
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        //修改name
        tbContentCategory.setName(name);
        //更新数据库
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        //返回状态

        return E3Result.ok();
    }

    @Override
    public E3Result deleteContentCategory(Long id) {
        //根据id查询contentCategory这条数据
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        Long parentId = tbContentCategory.getParentId();
        //判断该节点是否为父节点，如果为子节点直接删除，并且判断其父节点是否还有其他子节点，
        // 如果没有则设置isParent属性为false，
        if (tbContentCategory.getIsParent()) {
            return E3Result.build(1, "删除失败");
        } else {
            tbContentCategoryMapper.deleteByPrimaryKey(id);
            //查询条件，parentId相同的有几个，返回一个parentId相同的list列表
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            List<TbContentCategory> childs = tbContentCategoryMapper.selectByExample(example);

            if (childs.size() == 0) {
                TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);

                if (parent.getIsParent()) {
                    parent.setIsParent(false);

                    tbContentCategoryMapper.updateByPrimaryKey(parent);
                }
            }

        }
        return E3Result.ok(tbContentCategory);
    }
}
