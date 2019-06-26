package com.yuanwj.poetry.rest;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.yuanwj.poetry.entity.Poetry;
import com.yuanwj.poetry.entity.PoetryLocation;
import com.yuanwj.poetry.model.SpiderRequest;
import com.yuanwj.poetry.model.SpiderResult;
import com.yuanwj.poetry.repository.PoetryLocationRepository;
import com.yuanwj.poetry.repository.PoetryRepository;
import com.yuanwj.poetry.service.service.PoetryServiceImpl;
import com.yuanwj.poetry.service.spider.ParseService;
import com.yuanwj.poetry.service.spider.impl.LocationParseService;
import com.yuanwj.poetry.service.spider.impl.PoetryParseService;
import com.yuanwj.poetry.utils.ExecutorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/api/v1/poetry/")
public class PoetrySpiderRest {

    @Resource
    private PoetryLocationRepository poetryLocationRepository;

    @Resource
    private PoetryRepository poetryRepository;

    @RequestMapping(value = "download")
    public String download() {

        SpiderRequest spiderRequest = new SpiderRequest();
        spiderRequest.setUrl("https://www.gushiwen.org/guwen/shijing.aspx");
        spiderRequest.setTYPE(SpiderRequest.TYPE.LOCATION);
        ParseService parseService = getParse(spiderRequest);
        SpiderResult result = parseService.parse(spiderRequest);
        List<PoetryLocation> poetryLocations = result.getEntity();
        poetryLocationRepository.deleteAll();
        poetryLocationRepository.saveAll(poetryLocations);
        List<SpiderRequest> requestList = result.getRequests();

        for (SpiderRequest request : requestList) {
            SpiderResult spiderResult = getParse(request).parse(request);
            if (spiderResult.getEntity() != null) {
                System.out.println("保存单首古诗");
                poetryRepository.saveAll(spiderResult.getEntity());
            }
        }
        return "success";
    }

    public ParseService getParse(SpiderRequest request) {
        if (request.getTYPE() == null) {
            throw new RuntimeException("request's type mustn't empty");
        }
        Map<SpiderRequest.TYPE, ParseService> parseServiceMap = ImmutableMap.of(
                SpiderRequest.TYPE.LOCATION, new LocationParseService(),
                SpiderRequest.TYPE.POETRY, new PoetryParseService());
        return parseServiceMap.get(request.getTYPE());
    }

    @RequestMapping(value = "/createName")
    public String creteName() {
        List<Poetry> poetries = poetryRepository.findAll();
        StringBuffer buffer = new StringBuffer("");
        for (Poetry poetry : poetries) {
            buffer.append(poetry.getContent());
        }
        System.out.println(buffer);
        String result = buffer.toString();
        result = result.replace("，", "");
        result = result.replace("。", "");
        result = result.replace("？", "");
        result = result.replace("；", "");
        result = result.replace("！", "");
        result = result.replace("：", "");
        result = result.replace(" ", "");

        System.out.println(result);
        Random random = new Random();
        int resultSize = result.length();
        for (int i = 0; i < 1000; i++) {
            StringBuffer name = new StringBuffer("");
            int firstIndex = random.nextInt(resultSize);
            int secondIndex = random.nextInt(resultSize);
            name.append(result.substring(firstIndex, firstIndex + 1));
            name.append(result.substring(secondIndex, secondIndex + 1));
            System.out.println(name);
        }
        return "success";
    }

    @RequestMapping(value = "testThread")
    public String testThread() {
        int size = poetryRepository.findAll().size();
        int totalPage = (size - 1) % 2 + 1;
        ExecutorService executorService = ExecutorUtils.getExecutorService();
        for (int page = 1; page <= totalPage; page++) {
            PoetryServiceImpl poetryService = new PoetryServiceImpl(page, 2);
            Future<List<Poetry>> future = executorService.submit(poetryService);
            try {
                List<Poetry> poetries = future.get();
                System.out.println(poetries.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

}
