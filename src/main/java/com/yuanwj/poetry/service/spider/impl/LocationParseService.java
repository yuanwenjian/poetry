package com.yuanwj.poetry.service.spider.impl;

import com.yuanwj.poetry.entity.PoetryLocation;
import com.yuanwj.poetry.model.SpiderRequest;
import com.yuanwj.poetry.model.SpiderResult;
import com.yuanwj.poetry.repository.PoetryLocationRepository;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LocationParseService implements ParseService {

    @Override
    public SpiderResult<PoetryLocation> parse(SpiderRequest request) {
        SpiderResult<PoetryLocation> spiderResult = new SpiderResult<>();

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(request.getUrl());
            CloseableHttpResponse response = client.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(result);
            Elements nameDiv = document.select(".typecont span");
            List<PoetryLocation> poetryLocations = new ArrayList<>();
            List<SpiderRequest> requests = new ArrayList<>();
            for (Element element : nameDiv) {
                Elements elements = element.select("[href]");
                String name = elements.text();
                String url = elements.attr("href");
                PoetryLocation poetryLocation = new PoetryLocation();
                poetryLocation.setName(name);
                poetryLocation.setUrl(url);
                poetryLocation.setCreateDate(new Date());
                poetryLocations.add(poetryLocation);
                SpiderRequest spiderRequest = new SpiderRequest();
                spiderRequest.setUrl(url);
                spiderRequest.setTYPE(SpiderRequest.TYPE.POETRY);
                requests.add(spiderRequest);
            }
            spiderResult.setEntity(poetryLocations);
            spiderResult.setRequests(requests);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return spiderResult;
    }
}
