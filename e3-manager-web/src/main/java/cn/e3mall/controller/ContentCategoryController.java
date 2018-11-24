package cn.e3mall.controller;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(
            @RequestParam(name = "id",defaultValue = "0") long parentId){

        List<EasyUITreeNode> contentCategoryList = contentCategoryService.getContentCategoryList(parentId);

        return contentCategoryList;
    }

    //添加内容分类管理的子节点
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContentCatefory(long parentId, String name){
        E3Result e3Result = contentCategoryService.addContentCatefory(parentId, name);

        return e3Result;
    }

    //对分类节点重命名
    @RequestMapping(value = "/content/category/update")
    @ResponseBody
    public E3Result updateContentCategory(Long id,String name){
        E3Result e3Result = contentCategoryService.updateContentCategory(id, name);

        return e3Result;
    }

    //删除分类节点
    @RequestMapping(value = "/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public E3Result deleteContentCategory(Long id){
        E3Result e3Result = contentCategoryService.deleteContentCategory(id);
        return e3Result;
    }
}
