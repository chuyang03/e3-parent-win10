package cn.e3mall.controller;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemParamItem;
import cn.e3mall.service.TbItemService;
import cn.e3mall.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private TbItemService tbItemService;

    //这是一个测试，访问浏览器返回遗传json数据
    //{ItemId}是一个模版映射，可以从url中取出数据
    @RequestMapping("/item/{ItemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long ItemId){

        TbItem tbItem = tbItemService.getItemById(ItemId);
        return tbItem;
    }

    //注解ResponseBody表示返回json数据
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){

        //调用服务查询商品列表
        EasyUIDataGridResult result = tbItemService.getItemList(page, rows);

        return result;
    }

    //添加商品
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem tbItem,String desc){
        E3Result e3Result = tbItemService.addItem(tbItem, desc);
        return e3Result;
    }

    //编辑商品
    @RequestMapping("/rest/item/query/item/desc/{itemId}")
    @ResponseBody
    public TbItemDesc editItem(@PathVariable long itemId){
        TbItemDesc tbItemDesc = tbItemService.editItem(itemId);

        return tbItemDesc;
    }

    //异步重新加载商品信息   ？不懂什么意思
    @RequestMapping("/rest/item/param/item/query/{itemId}")
    @ResponseBody
    public TbItem queryById(@PathVariable long itemId){
        TbItem tbItem = tbItemService.getItemById(itemId);
        return tbItem;
    }


    /**
     * 批量删除功能实现
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result delete(String ids){
        E3Result result=tbItemService.deteteBatch(ids);
        return result;
    }

}
