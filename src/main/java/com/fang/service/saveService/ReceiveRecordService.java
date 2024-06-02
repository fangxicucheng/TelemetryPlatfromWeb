package com.fang.service.saveService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.database.postgresql.repository.ReceiveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveRecordService {
    @Autowired
    private ReceiveRecordRepository receiveRecordDao;


    public void save(ReceiveRecord receiveRecord){
        this.receiveRecordDao.save(receiveRecord);
    }


    public List<ReceiveRecord> getTelemetryReplayList(ReceiveRecordRequestInfo requestInfo) {
        Pageable pageable = PageRequest.of(requestInfo.getPageNum(), requestInfo.getPageSize(), Sort.by("id").descending());
//        Specification<ReceiveRecord> specification=new Specification<ReceiveRecord>() {
//            @Override
//            public Predicate toPredicate(Root<ReceiveRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//
//                List <Predicate> predicateList=new ArrayList<>();
//                if(requestInfo.getStartTime()!=null){
//                    predicateList.add(builder.greaterThan(root.get("startTime"), requestInfo.getStartTime()));
//                }
//
//
//
//                builder.in(root.get("satelliteName"),requestInfo.getSatelliteNameList());
//
//
//
//                return query.where().getRestriction();
//            }
//        };
        return null;

    }

}
