package com.qiushop.qiushopbackend.dutychain;

import com.qiushop.qiushopbackend.pojo.BusinessLaunch;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CityHandler extends AbstractBusinessHandler{
    @Override
    public List<BusinessLaunch> processHandler(List<BusinessLaunch> launchList, String targetCity, String targetSex, String targetProduct) {
        //如果 launchList 中没有数据，直接返回
        if (launchList.isEmpty()) {
            return launchList;
        }
        //按 target 进行筛选，只保留符合条件的投放信息
        launchList = launchList.stream().filter(launch -> {
            String city = launch.getTargetCity();
            if (StringUtils.isEmpty(city)) {
                return true;
            }
            List<String> cityList = Arrays.asList(city.split(","));
            return cityList.contains(targetCity);
        }).collect(Collectors.toList());
        //如果还有下一个责任类，则继续进行筛选
        if (hasNextHandler()) {
            return nextHandler.processHandler(launchList, targetCity, targetSex, targetProduct);
        }
        return launchList;
    }
}
