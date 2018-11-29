package cn.e3mall.search.dao;

import cn.e3mall.pojo.SearchItem;
import cn.e3mall.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据查询条件查询索引库
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query)throws Exception{
        //根据query查询索引库
        QueryResponse queryResponse = solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        //取查询结果记录数
        long numFound = solrDocumentList.getNumFound();
        SearchResult searchResult=new SearchResult();
        searchResult.setRecordCount(numFound);
        //取商品列表，需要取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        List<SearchItem> itemList=new ArrayList<>();
        for (SolrDocument solrDocument:solrDocumentList){
            SearchItem searchItem=new SearchItem();
            searchItem.setId((String) solrDocument.get("id"));

            //取高亮显示
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title="";
            if (list!=null&&list.size()>0){
                title=list.get(0);
            }else {
                title=(String) solrDocument.get("Item_title");
            }
            searchItem.setTitle(title);
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            //取出的是所有图片地址
            //类似这样的形式，是一个逗号分隔的字符串
            //   http://192.168.25.133/group1/M00/00/00/wKgZhVv_f1iACHhKAAAJMkMC8o8901.jpg,http://192.168.25.133/group1/M00/00/00/wKgZhVv_f1iAHCJtAAAHNKpP6hE066.jpg,http://192.168.25.133/group1/M00/00/00/wKgZhVv_f1iANIB_AAAH9yFyCJ8205.jpg
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));

            //把商品添加到商品列表
            itemList.add(searchItem);
        }
        searchResult.setItemList(itemList);
        //返回结果

        return searchResult;
    }
}
