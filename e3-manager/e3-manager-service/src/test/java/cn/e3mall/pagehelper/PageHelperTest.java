package cn.e3mall.pagehelper;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class PageHelperTest {

    @Test
    public void testPageHeper() throws Exception{

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");

        //从容器中获得代理对象
        TbItemMapper tbItemMapper= (TbItemMapper) applicationContext.getBean(TbItemMapper.class);

        //执行sql语句之前设置分页信息，使用PageHelper的startPage方法
        PageHelper.startPage(1,10);
        //设置查询条件
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);

        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);

        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
        System.out.println(list.size());



    }
}
