package com.fang.service.saveService;

import com.fang.database.mysql.entity.TeleReceiveRecordMq;
import com.fang.database.mysql.repository.TeleReceiveRecordMqRepository;
import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.database.postgresql.repository.ReceiveRecordRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiveRecordService {
    @Autowired
    private ReceiveRecordRepository receiveRecordDao;
    @Autowired
    private TeleReceiveRecordMqRepository recordMqRepository;

    public void reSave(){
        List<TeleReceiveRecordMq> receiveRecordMqList = recordMqRepository.findAll();
        List<ReceiveRecord>receiveRecordList=new ArrayList<>();
        for (TeleReceiveRecordMq teleReceiveRecordMq : receiveRecordMqList) {
            receiveRecordList.add(new ReceiveRecord(teleReceiveRecordMq));
        }
        this.receiveRecordDao.saveAll(receiveRecordList);
    }


    public void save(ReceiveRecord receiveRecord){
        this.receiveRecordDao.save(receiveRecord);
    }


    public Page<ReceiveRecord> getTelemetryReplayList(ReceiveRecordRequestInfo requestInfo) {
        Pageable pageable = PageRequest.of(requestInfo.getPageNum(), requestInfo.getPageSize(), Sort.by("id").descending());
        Specification<ReceiveRecord> spec=new Specification<ReceiveRecord>() {
            @Override
            public Predicate toPredicate(Root<ReceiveRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List <Predicate> predicateList=new ArrayList<>();
                if(requestInfo.getStartTime()!=null){
                    predicateList.add(builder.greaterThan(root.get("startTime"), requestInfo.getStartTime()));
                }
                if(requestInfo.getEndTime()!=null){
                    predicateList.add(builder.lessThan(root.get("startTime"), requestInfo.getStartTime()));
                }
                if(requestInfo.getSatelliteNameList()!=null&&requestInfo.getSatelliteNameList().size()>0){
                    predicateList.add(root.get("satelliteName").in(requestInfo.getSatelliteNameList())) ;
                }
                if(requestInfo.getStationNameList()!=null&&requestInfo.getStationNameList().size()>0){

                    List<Predicate>preList=new ArrayList<>();

                    for (String stationName : requestInfo.getStationNameList()) {

                        preList.add(builder.like(root.get("filePath"),"%"+stationName+"%"));

                    }
                    predicateList.add(builder.or(preList.toArray(new Predicate[preList.size()])));
                }
                return query.where(predicateList.toArray(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        };
        return receiveRecordDao.findAll(spec,pageable);

    }

}
