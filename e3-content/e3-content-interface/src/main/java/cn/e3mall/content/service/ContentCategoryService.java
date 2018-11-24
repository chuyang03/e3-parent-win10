package cn.e3mall.content.service;

import cn.e3mall.pojo.EasyUITreeNode;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCategoryList(long parentId);
}
