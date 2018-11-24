package cn.e3mall.content.service;

import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCategoryList(long parentId);

    E3Result addContentCatefory(long parentId,String name);

    E3Result updateContentCategory(Long id, String name);

    E3Result deleteContentCategory(Long id);
}
