package com.fang.service.saveService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.database.postgresql.repository.ReceiveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiveRecordService {
    @Autowired
    private ReceiveRecordRepository receiveRecordDao;


    public void save(ReceiveRecord receiveRecord){
        this.receiveRecordDao.save(receiveRecord);
    }


}
