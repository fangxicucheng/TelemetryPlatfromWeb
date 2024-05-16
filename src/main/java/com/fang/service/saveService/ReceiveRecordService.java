package com.fang.service.saveService;

import com.fang.database.postgresql.repository.ReceiveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiveRecordService {
    @Autowired
    private ReceiveRecordRepository receiveRecordDao;



}
