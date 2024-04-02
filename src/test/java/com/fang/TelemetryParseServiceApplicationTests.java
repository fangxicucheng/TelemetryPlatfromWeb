package com.fang;

import com.fang.database.mysql.entity.TeleReceiveRecordMq;
import com.fang.database.mysql.repository.TeleReceiveRecordMqRepository;
import com.fang.database.mysql.repository.TeleSatelliteNameMqRepository;
import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.database.postgresql.repository.FrameCatalogDbRepository;
import com.fang.database.postgresql.repository.ReceiveRecordRepository;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.service.setExcpetionJuge.SatelliteSubsystemService;
import com.fang.service.setSatelliteConfig.FrameCatalogConfigService;
import com.fang.service.setSatelliteConfig.SatelliteConfigService;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModelInterface;
import com.fang.utils.ExcelReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TelemetryParseServiceApplicationTests {
    @Autowired
    private TeleSatelliteNameMqRepository teleSatelliteNameMqRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Autowired
    private TeleReceiveRecordMqRepository teleReceiveRecordMqRepository;
    @Autowired
    private ReceiveRecordRepository receiveRecordRepository;
    @Autowired
    private FrameCatalogDbRepository frameCatalogDbRepository;
    @Autowired
    private SatelliteConfigService satelliteConfigService;
    @Autowired
    private SatelliteSubsystemService satelliteSubsystemService;

    @Test
    void deleteSatellites() {

        try {
            satelliteDbRepository.deleteAll();
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    void testReadExcel() throws FileNotFoundException {


        File file = new File("F:\\0323\\0323\\test\\test\\5.2.34. 中心机内部参数应答帧一0_34_2_ZT-C002.xlsx");
        InputStream inputStream = new FileInputStream(file);
        List<Object[]> objects = ExcelReader.importExcel(inputStream);
        assert objects != null;
        for (Object[] object : objects) {
            // System.out.println(object[0] + "\t" + object[1] + "\t" + object[2]);
            String printContent = "";
            for (Object o : object) {
                //System.out.println(o);
                printContent += o + "\t";

            }
            System.out.println(printContent);
        }


    }

    @Test
    void contextLoads() {

//        List<TeleSatelliteNameMq> all = this.teleSatelliteNameMqRepository.findAll();
        // System.out.println(all);

    }

    @Test
    void testGetQuery() {
        List<TeleFrameCatalogDbModelInterface> frameCatalogDbModelListByBId = this.frameCatalogDbRepository.getFrameCatalogDbModelListByBId(3);
        List<TeleFrameCatalogDbModel> modelList = new ArrayList<>();
        for (TeleFrameCatalogDbModelInterface teleFrameCatalogDbModelInterface : frameCatalogDbModelListByBId) {

            TeleFrameCatalogDbModel teleFrameCatalogDbModel = new TeleFrameCatalogDbModel();
            teleFrameCatalogDbModel.setCatalogCode(teleFrameCatalogDbModelInterface.getCatalogCode());
            teleFrameCatalogDbModel.setCatalogName(teleFrameCatalogDbModelInterface.getCatalogName());
            teleFrameCatalogDbModel.setId(teleFrameCatalogDbModelInterface.getId());
            teleFrameCatalogDbModel.setSatelliteId(teleFrameCatalogDbModelInterface.getSatelliteId());
            teleFrameCatalogDbModel.setNum(teleFrameCatalogDbModelInterface.getNum());
            System.out.println(teleFrameCatalogDbModel);
        }

    }

    @Test
    void testGetQuery2() {
        List<Object[]> list = this.frameCatalogDbRepository.getFrameCatalogDbModelListByBId2(3);
        List<TeleFrameCatalogDbModel> modelList = new ArrayList<>();
        for (Object[] objects : list) {
            TeleFrameCatalogDbModel teleFrameCatalogDbModel = new TeleFrameCatalogDbModel();
            teleFrameCatalogDbModel.setId((Integer) objects[0]);
            teleFrameCatalogDbModel.setCatalogName(((String) objects[1]));
            teleFrameCatalogDbModel.setCatalogCode(((Integer) objects[2]));
            teleFrameCatalogDbModel.setNum(((Integer) objects[3]));
            teleFrameCatalogDbModel.setSatelliteId(((Integer) objects[4]));
            System.out.println(teleFrameCatalogDbModel);
        }
//        for (TeleFrameCatalogDbModelInterface teleFrameCatalogDbModelInterface : frameCatalogDbModelListByBId) {
//
//            TeleFrameCatalogDbModel teleFrameCatalogDbModel = new TeleFrameCatalogDbModel();
//            teleFrameCatalogDbModel.setCatalogCode(teleFrameCatalogDbModelInterface.getCatalogCode());
//            teleFrameCatalogDbModel.setCatalogName(teleFrameCatalogDbModelInterface.getCatalogName());
//            teleFrameCatalogDbModel.setId(teleFrameCatalogDbModelInterface.getId());
//            teleFrameCatalogDbModel.setSatelliteId(teleFrameCatalogDbModelInterface.getSatelliteId());
//            teleFrameCatalogDbModel.setNum(teleFrameCatalogDbModelInterface.getNum());
//            System.out.println(teleFrameCatalogDbModel);
//        }


    }


    @Test
    void copySatelliteName() {

        this.satelliteConfigService.copySatelliteData();
        System.out.println("复制完成");

//           List<TeleSatelliteNameMq> all = teleSatelliteNameMqRepository.findAll();
//
//        List<SatelliteDb> satelliteDbList=new ArrayList<>();
//
//        for (TeleSatelliteNameMq teleSatelliteNameMq : teleSatelliteNameMqList) {
//            SatelliteDb satelliteDb=new SatelliteDb(teleSatelliteNameMq);
//            satelliteDbList.add(satelliteDb);
//        }
//        satelliteDbRepository.saveAllAndFlush(satelliteDbList);
//          SatelliteDb all = satelliteDbRepository.findById(3);
//    List<SatelliteDb> all = satelliteDbRepository.F();
//        long currentTimeNanosOrMillis = TimeUtil.getCurrentTimeNanosOrMillis();
//
//
//        List<SatelliteDb> satelliteDbList = satelliteDbRepository.queryListAll();
//        System.out.println(TimeUtil.getCurrentTimeNanosOrMillis() - currentTimeNanosOrMillis);
//
//
//        System.out.println(satelliteDbList);


    }

    @Test
    public void copySatelliteSubsystem() {
        this.satelliteSubsystemService.copySatelliteSubsystemFromMysql();

    }

    @Test
    public void selectSatelliteName() {
        System.out.println(this.satelliteDbRepository.querySatelliteName());

    }

    @Test
    void copyRecord() {
        List<TeleReceiveRecordMq> all = this.teleReceiveRecordMqRepository.findAll();

        for (TeleReceiveRecordMq teleReceiveRecordMq : all) {

            this.receiveRecordRepository.save(new ReceiveRecord(teleReceiveRecordMq));
        }
        List<ReceiveRecord> all1 = this.receiveRecordRepository.findAll();
        System.out.println(all1);

    }

}
