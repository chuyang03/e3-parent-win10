package cn.e3mall.search.message;

import cn.e3mall.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * 监听商品添加消息，当有商品添加时，监听容器接收到商品id的消息
 * 根据商品id从数据库中查找出来，将商品信息添加到索引库中
 */
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取出商品id（发送的消息就是商品的id）
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);

            //拿到消息时有可能出现插入数据库的操作事物还没提交，导致数据库中没有数据，出现空指针
            //解决办法,在查询数据库之前等一会
            Thread.sleep(1000);
            //根据商品id查询消息
            SearchItem searchItem= itemMapper.getItemById(itemId);

            //创建一个文档对象，将来写入索引库
            SolrInputDocument document = new SolrInputDocument();
            //向文档对象中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            //把文档写入索引库
            solrServer.add(document);
            //提交
            solrServer.commit();


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
