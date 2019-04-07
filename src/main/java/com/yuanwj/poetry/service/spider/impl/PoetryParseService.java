package com.yuanwj.poetry.service.spider.impl;

import com.yuanwj.poetry.entity.Poetry;
import com.yuanwj.poetry.model.SpiderRequest;
import com.yuanwj.poetry.model.SpiderResult;
import com.yuanwj.poetry.service.spider.ParseService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class PoetryParseService implements ParseService {

    @Override
    public SpiderResult parse(SpiderRequest request) {

        SpiderResult spiderResult = new SpiderResult();
        try {

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(request.getUrl());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String html = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(html);
            Elements elements = document.select(".main3 .left .sons .cont");
            Element element = elements.get(0);
//            Elements elements = document.select(".cont .contson");
            System.out.println(request.getUrl());
            String name = element.select("h1").text();
            System.out.println(name);
            Elements titleDiv = element.select(".source a");
            String categories = titleDiv.get(0).text();
            String author = titleDiv.get(1).text();
            String content = element.select(".contson").text();
            Poetry poetry = new Poetry();
            poetry.setAuthor(author);
            poetry.setCategories(categories);
            poetry.setContent(content);
            poetry.setName(name);
            poetry.setCreateDate(new Date());
            spiderResult.setEntity(Arrays.asList(poetry));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return spiderResult;
    }
}
