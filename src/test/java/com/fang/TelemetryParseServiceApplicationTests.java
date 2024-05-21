package com.fang;

import com.fang.config.exception.ExceptionManager;
import com.fang.database.mysql.entity.*;
import com.fang.database.mysql.repository.*;
import com.fang.database.postgresql.entity.*;
import com.fang.database.postgresql.repository.*;
import com.fang.service.commandCountService.CommandCountService;
import com.fang.service.resourceReader.ResourceReaderService;
import com.fang.service.setExcpetionJuge.SubSystemService;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.service.setExcpetionJuge.ThresholdService;
import com.fang.service.setSatelliteConfig.SatelliteConfigService;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModelInterface;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import com.fang.utils.ExcelUtils;
import com.fang.utils.NetWorkInfoUtils;
import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SubSystemService subSystemService;
    @Autowired
    private ThresholdService thresholdService;
    @Autowired
    private ResourceReaderService resourceReaderService;
    @Autowired
    private StationInfoRepository stationInfoRepository;

    @Autowired
    private CommandCountService commandCountService;
    @Autowired
    private TeleCommandCountMqRepository commandCountMqRepository;
    @Autowired
    private TeleCountparaMqRepository teleCountparaMqRepository;
    @Autowired
    private DecryptionConfigRepository decryptionConfigRepository;
    @Autowired
    private TeleDecryptionCofnigMqRepository teleDecryptionCofnigMqRepository;

    @Autowired
    private GpsParaConfigRepository gpsParaConfigRepository;
    @Autowired
    private TeleGpsParaConfigMqRepository gpsParaConfigMqRepository;
    @Autowired
    private TeleSatelliteNameConfigMqRepository satelliteNameConfigMqRepository;
    @Autowired
    private SatelliteNameConfigRepository satelliteNameConfigRepository;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;




    @Test
    void testUpdateDataBase() {
        updateGpsList();
        updateCommandCountList();
        updateSatelliteNameConfig();
        updateDecryptionConfig();

    }

    void updateGpsList() {
        this.gpsParaConfigRepository.deleteAll();
        List<TeleGpsParaConfigMq> gpsParaConfigMqList = this.gpsParaConfigMqRepository.findAll();
        List<GpsParaConfig> gpsParaConfigList = new ArrayList<>();
        for (TeleGpsParaConfigMq teleGpsParaConfigMq : gpsParaConfigMqList) {
            gpsParaConfigList.add(new GpsParaConfig(teleGpsParaConfigMq));
        }
        this.gpsParaConfigRepository.saveAll(gpsParaConfigList);
    }

    void updateCommandCountList() {
        this.commandCountMqRepository.deleteAll();
        List<TeleCommandCountMq> commandCountMqList = this.commandCountMqRepository.findAll();
        List<CommandCount> commandCountList = new ArrayList<>();
        for (TeleCommandCountMq teleCommandCountMq : commandCountMqList) {
            commandCountList.add(new CommandCount(teleCommandCountMq));
        }
        this.commandCountService.saveOrUpdateCommandCountList(commandCountList);
    }

    void updateSatelliteNameConfig() {
        this.satelliteNameConfigRepository.deleteAll();
        List<TeleSatelliteNameConfigMq> satelliteNameConfigMqList = this.satelliteNameConfigMqRepository.findAll();
        List<SatelliteNameConfig> satelliteNameConfigList = new ArrayList<>();
        for (TeleSatelliteNameConfigMq teleSatelliteNameConfigMq : satelliteNameConfigMqList) {
            satelliteNameConfigList.add(new SatelliteNameConfig(teleSatelliteNameConfigMq));
        }
        this.satelliteNameConfigRepository.saveAll(satelliteNameConfigList);
    }

    void updateDecryptionConfig() {
        this.decryptionConfigRepository.deleteAll();
        List<TeleDecryptionCofnigMq> teleDecryptionCofnigMqList = this.teleDecryptionCofnigMqRepository.findAll();
        List<DecryptionConfig> decryptionConfigList = new ArrayList<>();
        for (TeleDecryptionCofnigMq teleDecryptionCofnigMq : teleDecryptionCofnigMqList) {
            decryptionConfigList.add(new DecryptionConfig(teleDecryptionCofnigMq));
        }
        this.decryptionConfigRepository.saveAll(decryptionConfigList);
    }


    @Test
    void testGetIpAddress() {
        List<InetAddress> machineIpAddress = NetWorkInfoUtils.getMachineIpAddress();
        for (InetAddress ipAddress : machineIpAddress) {
            System.out.println(ipAddress.getHostAddress());
        }
    }

    @Test
    void updateStationInfo() throws IOException {
        List<StationInfo> stationInfoDbList = this.stationInfoRepository.findAll();
        Map<String, StationInfo> stationInfoMap = new HashMap<>();
        List<StationInfo> stationInfoList = new ArrayList<>();
        if (stationInfoDbList != null && stationInfoDbList.size() > 0) {
            for (StationInfo stationInfo : stationInfoDbList) {
                if (!stationInfoMap.containsKey(stationInfo.getStationName())) {
                    stationInfoMap.put(stationInfo.getStationName(), stationInfo);
                    stationInfoList.add(stationInfo);
                }
            }
        }
        List<String> stationNameInfoList = this.resourceReaderService.readResourceFile("stationName.txt");
        for (String stationNameInfo : stationNameInfoList) {
            String[] stationNameInfoArray = stationNameInfo.split(",");
            if (!stationInfoMap.containsKey(stationNameInfoArray[1])) {
                StationInfo stationInfo = new StationInfo();
                stationInfo.setStationName(stationNameInfoArray[1]);
                stationInfoMap.put(stationInfo.getStationName(), stationInfo);
                stationInfoList.add(stationInfo);
            }
            stationInfoMap.get(stationNameInfoArray[1]).setStationId(stationNameInfoArray[0]);
        }
        List<String> stationConfigList = this.resourceReaderService.readResourceFile("StationConfig.txt");
        StationInfo stationInfo = null;
        for (String stationConfigInfo : stationConfigList) {
            if (stationConfigInfo.contains("$")) {
                String stationName = stationConfigInfo.replaceAll("\\$", "");
                if (stationInfoMap.containsKey(stationName)) {
                    stationInfo = stationInfoMap.get(stationName);
                }
            } else if (stationConfigInfo.contains("#")) {
                String[] waveNameInfoArray = stationConfigInfo.replaceAll("#", "").split(":");
                String wavename = waveNameInfoArray[0];
                String[] ipAddressInfo = waveNameInfoArray[1].split(";");
                if (ipAddressInfo.length == 3) {
                    String ip = ipAddressInfo[1].split(",")[1];
                    String port = ipAddressInfo[2].split(",")[1];
                    String waveInfo = wavename + ":" + ip + "," + port;
                    if (stationInfo != null) {
                        if (stationInfo.getWaveInfo() != null && !stationInfo.getWaveInfo().equals("")) {
                            stationInfo.setWaveInfo(stationInfo.getWaveInfo() + ";" + waveInfo);
                        } else {
                            stationInfo.setWaveInfo(waveInfo);
                        }
                    }
                }
            }
        }
        List<String> stationAndServerInfoList = this.resourceReaderService.readResourceFile("stationAndServer.txt");
        for (String stationAndServerInfo : stationAndServerInfoList) {
            String[] stationAndServerInfoArray = stationAndServerInfo.split(":");
            String ip = stationAndServerInfoArray[0];
            String[] stationNameList = stationAndServerInfoArray[1].split(",");
            for (String stationName : stationNameList) {
                if (stationInfoMap.containsKey(stationName)) {
                    stationInfoMap.get(stationName).setServerIp(ip);
                }
            }
        }
        this.stationInfoRepository.saveAll(stationInfoList);
    }

    @Test
    void updateSatelliteInfo() throws IOException {
        List<TeleSatelliteDbModel> teleSatelliteDbModels = this.satelliteDbRepository.querySatelliteName();
        Map<String, TeleSatelliteDbModel> satelliteDbModelMap = new HashMap<>();
        for (TeleSatelliteDbModel teleSatelliteDbModel : teleSatelliteDbModels) {
            satelliteDbModelMap.put(teleSatelliteDbModel.getSatelliteName(), teleSatelliteDbModel);
        }
        List<String> satelliteNameInfoList = this.resourceReaderService.readResourceFile("satelliteName.txt");
        for (String satelliteNameInfo : satelliteNameInfoList) {
            String[] nameInfoArray = satelliteNameInfo.split(",");

            if (satelliteDbModelMap.containsKey(nameInfoArray[1])) {
                satelliteDbModelMap.get(nameInfoArray[1]).setSatelliteId(nameInfoArray[0]);
            }

        }

        List<String> satelliteNmaeByteInfoList = this.resourceReaderService.readResourceFile("satelliteNameByte.txt");
        //System.out.println(satelliteNmaeByteInfoList);
        for (String nameByteInfo : satelliteNmaeByteInfoList) {
            String[] nameByteInfoArray = nameByteInfo.split(",");
            if (nameByteInfoArray.length == 3) {
                if (satelliteDbModelMap.containsKey(nameByteInfoArray[0])) {
                    satelliteDbModelMap.get(nameByteInfoArray[0]).setSatelliteBytesStr(nameByteInfoArray[1] + "," + nameByteInfoArray[2]);
                }
            }
        }
        //  satelliteNmaeByteInfoList.forEach(t->System.out.println(t));
        List<String> satellliteICardInfoList = this.resourceReaderService.readResourceFile("BDSatelliteICard.txt");
        //   satellliteICardInfoList.forEach(t->System.out.println(t));
        //System.out.println(satellliteICardInfoList);

        for (String iCardInfo : satellliteICardInfoList) {

            String[] iCardInfoArray = iCardInfo.split(":");

            if (satelliteDbModelMap.containsKey(iCardInfoArray[0])) {
                satelliteDbModelMap.get(iCardInfoArray[0]).setBdICCardsStr(iCardInfoArray[1]);
            }


        }


        for (String satelliteName : satelliteDbModelMap.keySet()) {

            System.out.println(satelliteDbModelMap.get(satelliteName));
        }


        for (TeleSatelliteDbModel satelliteDbModel : satelliteDbModelMap.values()) {

            try {
//                if (satelliteDbModel.getBdICCardsStr() == null) {
//                    satelliteDbModel.setBdICCardsStr("");
//                }
//                if (satelliteDbModel.getSatelliteBytesStr() == null) {
//                    satelliteDbModel.setSatelliteBytesStr("");
//                }
//                if (satelliteDbModel.getSatelliteId() == null) {
//                    satelliteDbModel.setSatelliteId("");
//                }
                //  System.out.println(satelliteDbModel.getSatelliteName() + " id:" + satelliteDbModel.getSatelliteId().length() + ";bytes:" + satelliteDbModel.getSatelliteBytesStr().length() + ";iCards:" + satelliteDbModel.getBdICCardsStr().length());
                this.satelliteConfigService.updateTeleSatelliteDbInfo(satelliteDbModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //


        }


    }


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
    void readThresholdFile() throws IOException {
        List<ThresholdInfo> thresholdInfoList = thresholdService.readThresholdFile("宽幅02A星");
        for (ThresholdInfo thresholdInfo : thresholdInfoList) {
            System.out.println(thresholdInfo);
        }


    }

    @Test
    void testReadExcel() throws FileNotFoundException {


        File file = new File("F:\\0323\\0323\\test\\test\\5.2.34. 中心机内部参数应答帧一0_34_2_ZT-C002.xlsx");
        InputStream inputStream = new FileInputStream(file);
        List<Object[]> objects = ExcelUtils.importExcel(inputStream).get(0);
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
        this.subSystemService.copySatelliteSubsystemFromMysql();

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
