package com.goshop.solr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SolrServiceImpl implements SolrService {

    private static final Logger logger= Logger.getLogger(SolrServiceImpl.class.toString());

    private String urlPath;

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }


    @Override
    public HttpSolrClient getHttpSolrClient(String core) {
        return new HttpSolrClient(urlPath+core);
    }

    @Override
    public void add(String core, Object bean) throws IOException, SolrServerException {
        logger.info(urlPath);
        HttpSolrClient client =this.getHttpSolrClient(core);
        client.addBean(bean);
        client.commit();
    }

    @Override
    public void adds(String core, Collection<?> beans) throws IOException, SolrServerException {
        HttpSolrClient client =this.getHttpSolrClient(core);
        client.addBeans(beans);
        client.commit();
    }

    @Override
    public <T> List<T> query(String core, Class<T> clazz, SolrQuery query) throws IOException, SolrServerException {
        HttpSolrClient client =this.getHttpSolrClient(core);
        QueryResponse response = client.query(query);
        return response.getBeans(clazz);
    }

    @Override
    public QueryResponse query(String core, SolrQuery query) throws IOException, SolrServerException {
        HttpSolrClient client =this.getHttpSolrClient(core);
        QueryResponse response = client.query(query);
        return response;
    }

    @Override
    public <T> PageInfo query(String core, Class<T> clazz, SolrQuery query,RowBounds rowBounds) throws IOException, SolrServerException {
        HttpSolrClient client =this.getHttpSolrClient(core);
        if(rowBounds!=null){
            query.setStart(rowBounds.getOffset());
            query.setRows(rowBounds.getLimit());
        }else {
            Integer start = query.getStart();
            if(start==null){
                start=0;
            }
            Integer rows = query.getRows();
            Assert.notNull(start, "请设置分页条数rows");
            rowBounds = new RowBounds(start,rows);
        }
        QueryResponse response = client.query(query);
        SolrDocumentList documentList=response.getResults();
        List content = new DocumentObjectBinder().getBeans(clazz, documentList);
        //Page page=new Page(rowBounds.getOffset(),rowBounds.getLimit(), (int)documentList.getNumFound());
        Page page = (Page)content;
        page.setPageNum(rowBounds.getOffset());
        page.setPageSize(rowBounds.getLimit());
        page.setTotal(documentList.getNumFound());
        PageInfo pageInfo = new PageInfo(page);
        return pageInfo;
    }

    @Override
    public PageInfo query(String core, SolrQuery query, RowBounds rowBounds) throws IOException, SolrServerException {
        HttpSolrClient client =this.getHttpSolrClient(core);
        if(rowBounds!=null){
            query.setStart(rowBounds.getOffset()-1);
            query.setRows(rowBounds.getLimit());
        }else {
            Integer start = query.getStart();
            if(start==null){
                start=1;
            }
            Integer rows = query.getRows();
            Assert.notNull(start, "请设置分页条数rows");
            rowBounds = new RowBounds(start,rows);
        }
        QueryResponse response = client.query(query);
        SolrDocumentList documentList=response.getResults();
        List list =(List)documentList;
        Page page = new Page(rowBounds.getOffset(),rowBounds.getLimit());
        page.setTotal(documentList.getNumFound());
        page.addAll(list);
        PageInfo pageInfo = new PageInfo(page);
        return pageInfo;
    }

}