package com.fang.service.restartTimeService;

import com.fang.database.postgresql.repository.RestartTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestartTimeService {

    @Autowired
    private RestartTimeRepository restartTimeDao;




}
