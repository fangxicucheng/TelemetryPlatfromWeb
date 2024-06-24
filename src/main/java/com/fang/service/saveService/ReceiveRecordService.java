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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ReceiveRecordService {
    @Autowired
    private ReceiveRecordRepository receiveRecordDao;
    @Autowired
    private TeleReceiveRecordMqRepository recordMqRepository;

    public void reSave(){
        this.receiveRecordDao.deleteAll();

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

    public ReceiveRecord getReceiveRecordById(Integer id){
        return this.receiveRecordDao.findById(id).get();
    }


    public List<ReceiveRecord>getReceiveRecordList(String satelliteName, List<String> stationNameList, Date startTime,Date endTime)
    {
       return receiveRecordDao.findAll(getSpecification(new ArrayList<>(Arrays.asList(satelliteName)) , stationNameList, startTime, endTime));
    }

    public Page<ReceiveRecord> getTelemetryReplayList(ReceiveRecordRequestInfo requestInfo) {
        Pageable pageable = PageRequest.of(requestInfo.getPageNum(), requestInfo.getPageSize(), Sort.by("startTime").descending());

        Page<ReceiveRecord> all = receiveRecordDao.findAll(getSpecification(requestInfo.getSatelliteNameList(),requestInfo.getStationNameList(),requestInfo.getStartTime(),requestInfo.getEndTime()), pageable);
        return all;
    }
    Specification<ReceiveRecord> getSpecification(List<String> satelliteNameList,List<String>stationNameList,Date starTime,Date endTime){
        Specification<ReceiveRecord> spec=new Specification<ReceiveRecord>() {
            @Override
            public Predicate toPredicate(Root<ReceiveRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List <Predicate> predicateList=new ArrayList<>();
                if(starTime!=null){
                    predicateList.add(builder.greaterThan(root.get("startTime"),starTime));
                }
                if(endTime!=null){
                    predicateList.add(builder.lessThan(root.get("startTime"), endTime));
                }
                if(satelliteNameList!=null&&satelliteNameList.size()>0){
                    predicateList.add(root.get("satelliteName").in(satelliteNameList)) ;
                }
                if(stationNameList!=null&&stationNameList.size()>0){

                    List<Predicate>preList=new ArrayList<>();

                    for (String stationName : stationNameList) {

                        preList.add(builder.like(root.get("filePath"),"%"+stationName+"%"));

                    }
                    predicateList.add(builder.or(preList.toArray(new Predicate[preList.size()])));
                }
                return query.where(predicateList.toArray(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        };
        return spec;
    }



}
