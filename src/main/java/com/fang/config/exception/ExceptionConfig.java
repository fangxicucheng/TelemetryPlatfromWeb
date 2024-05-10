package com.fang.config.exception;

import com.fang.config.satellite.SatelliteConfig;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.service.setExcpetionJuge.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
public class ExceptionConfig {
    @Bean(name="threadInfoListMap")
    @Qualifier("threadInfoListMap")
    public Map<String, List<ThresholdInfo>> getSatelliteThreaInfoListMap(ThresholdService thresholdService) throws IOException {
        return thresholdService.getThreadholdInfoMap();
    }


}
