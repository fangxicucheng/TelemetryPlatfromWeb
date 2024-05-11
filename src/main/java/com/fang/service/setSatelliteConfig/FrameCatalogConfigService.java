package com.fang.service.setSatelliteConfig;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.FrameCatalogDbRepository;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.service.telemetryService.BaseParserService;
import com.fang.service.setSatelliteConfig.readFile.ManageSatelliteConfigFileService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModelInterface;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FrameCatalogConfigService {

    @Autowired
    private FrameCatalogDbRepository frameCatalogDbRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Autowired
    ManageSatelliteConfigFileService manageSatelliteConfigFileService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
    @Autowired
    private BaseParserService baseParserService;

    public FrameCatalogDb uploadFrameCatalogConfigFiles(List<MultipartFile> files) throws IOException {
        FrameCatalogDb frameCatalogDb;
        String format = sdf.format(new Date());
        Path directoryPath = Paths.get(baseDirectoryPath + "上传文件\\" + format + "\\");
        List<File> destFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            File dest = new File(directoryPath + "\\" + fileName);
            System.out.println(fileName);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);
            destFiles.add(dest);
        }
        frameCatalogDb = manageSatelliteConfigFileService.readFrameCatalogDbConfigFile(destFiles);

        System.out.println("文件保存完成");
        FileUtils.deleteDirectory(new File(baseDirectoryPath + "上传文件\\" + format + "\\"));
        return frameCatalogDb;
    }


       public CheckConfigResult insertFrameCatalog(int satelliteId,FrameCatalogDb frameCatalogDb) {
        CheckConfigResult result = new CheckConfigResult();
        if (frameCatalogDb.getCatalogName() == null || frameCatalogDb.getCatalogName().isEmpty()) {
            result.setErrorMsg("未设置帧类型名");
        }
           if (frameCatalogDb.getCatalogCode() <0) {
               result.setErrorMsg("帧类型编号为负值");
           }
        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();


           List<FrameCatalogDb> frameCatalogDbList = satelliteDb.getFrameCatalogDbList();
           if(frameCatalogDbList!=null&&frameCatalogDbList.size()>0){

               for (FrameCatalogDb catalogDb : frameCatalogDbList) {

                   if(catalogDb.getCatalogName().equals(frameCatalogDb.getCatalogName())){
                       result.setErrorMsg("重复的帧类型名");
                   }
                   if(catalogDb.getCatalogCode().equals(frameCatalogDb.getCatalogCode())){
                       result.setErrorMsg("重复的帧类型码");
                   }
               }
           }else{

               satelliteDb.setFrameCatalogDbList(new ArrayList<>());
              //
           }
           satelliteDb.getFrameCatalogDbList().add(frameCatalogDb);
           //frameCatalogDb.setNum(satelliteDb.getFrameCatalogDbList().size());
           if (satelliteDb.getFrameCatalogDbList()!=null&&satelliteDb.getFrameCatalogDbList().size()>0) {

               satelliteDb.getFrameCatalogDbList().sort((o1,o2)->{
                   return o1.getCatalogCode()-o2.getCatalogCode();
               });
               for (int i = 0; i < satelliteDb.getFrameCatalogDbList().size(); i++) {

                   satelliteDb.getFrameCatalogDbList().get(i).setNum(i+1);
               }

           }

           baseParserService.validateFrameCatalog(frameCatalogDb, result);
        if(!result.isHasWrong()){
            this.satelliteDbRepository.save(satelliteDb);
        }
        return result;
    }


    public List<TeleFrameCatalogDbModel> getFrameCatalogInfo(Integer satelliteId){

       List<TeleFrameCatalogDbModel>result=new ArrayList<>();
        List<TeleFrameCatalogDbModelInterface> searchResult = this.frameCatalogDbRepository.getFrameCatalogDbModelListByBId(satelliteId);

        if(searchResult!=null&&searchResult.size()>0){
            for (TeleFrameCatalogDbModelInterface teleFrameCatalogDbModelInterface : searchResult) {
                result.add(new TeleFrameCatalogDbModel(teleFrameCatalogDbModelInterface));

            }
        }
        return result;

    }
    public List<TeleFrameCatalogDbModel> updateFrameCatalogById(Integer satelliteId,TeleFrameCatalogDbModel teleFrameCatalogDbModel){
        this.frameCatalogDbRepository.updateFrameCatalogById(teleFrameCatalogDbModel.getId(),teleFrameCatalogDbModel.getCatalogName(),teleFrameCatalogDbModel.getCatalogCode(),teleFrameCatalogDbModel.getNum());
        return getFrameCatalogInfo(satelliteId);
    }

    public List<TeleFrameCatalogDbModel> deleteFrameCatlogById(Integer satelliteId,int id){
        this.frameCatalogDbRepository.deleteById(id);
        return getFrameCatalogInfo(satelliteId);
    }


}
