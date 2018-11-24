package cn.e3mall.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContent(TbContent tbContent){

        E3Result e3Result = contentService.addContent(tbContent);

        return e3Result;
    }
}
