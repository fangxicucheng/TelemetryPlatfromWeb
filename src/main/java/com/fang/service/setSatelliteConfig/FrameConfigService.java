package com.fang.service.setSatelliteConfig;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.FrameCatalogDbRepository;
import com.fang.database.postgresql.repository.FrameDbRepository;
import com.fang.service.parseTelemetry.BaseParserService;
import com.fang.service.setSatelliteConfig.readFile.ManageSatelliteConfigFileService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModelInterface;
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
public class FrameConfigService {
    @Autowired
    private FrameDbRepository frameDbRepository;
    @Autowired
    private FrameCatalogDbRepository frameCatalogDbRepository;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
    @Autowired
    private BaseParserService baseParserService;
    @Autowired
    ManageSatelliteConfigFileService manageSatelliteConfigFileService;

    public List<TeleFrameDbModel> getFrameDbModelList(Integer catalogId) {

        List<TeleFrameDbModel> result = new ArrayList<>();
        List<TeleFrameDbModelInterface> searchResult = this.frameDbRepository.getFrameByCatalogId(catalogId);
        if (searchResult != null && searchResult.size() > 0) {
            for (TeleFrameDbModelInterface teleFrameDbModelInterface : searchResult) {
                result.add(new TeleFrameDbModel(teleFrameDbModelInterface));
            }

        }
        return result;


    }

    public FrameDb uploadFrameConfigFile(MultipartFile file) throws IOException {
        FrameDb frameDb;
        String format = sdf.format(new Date());
        Path directoryPath = Paths.get(baseDirectoryPath + "上传文件\\" + format + "\\");
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        File dest = new File(directoryPath + "\\" + fileName);
        System.out.println(fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        frameDb = manageSatelliteConfigFileService.readFrameDbFromFile(dest);
        System.out.println("文件保存完成");
        FileUtils.deleteDirectory(new File(baseDirectoryPath + "上传文件\\" + format + "\\"));
        return frameDb;
    }

    public void deleteFrmeById(Integer frameId) {
        this.frameDbRepository.deleteById(frameId);

    }

    public CheckConfigResult insertFrameCatalog(int catalogId, FrameDb frameDb) {
        CheckConfigResult result = new CheckConfigResult();
        if (frameDb.getFrameName() == null || frameDb.getFrameName().isEmpty()) {
            result.setErrorMsg("未设置帧名称");
        }
        if (frameDb.getFrameCode() < 0) {
            result.setErrorMsg("帧编号为负值");
        }
        FrameCatalogDb catalogDb = frameCatalogDbRepository.findById(catalogId).get();
        List<FrameDb> frameDbList = catalogDb.getFrameDbList();
        if (frameDbList != null && frameDbList.size() > 0) {

            for (FrameDb frame : frameDbList) {

                if (frame.getFrameName().equals(frameDb.getFrameName())) {
                    result.setErrorMsg("重复的帧名称");
                }
                if (frame.getFrameCode().equals(frameDb.getFrameCode()) && frame.getReuseChannel().equals(frameDb.getReuseChannel())) {
                    result.setErrorMsg("重复的帧编号和复播道");
                }
            }
        } else {
            catalogDb.setFrameDbList(new ArrayList<>());
        }
        catalogDb.getFrameDbList().add(frameDb);
        if (catalogDb.getFrameDbList() != null && catalogDb.getFrameDbList().size() > 0) {
            catalogDb.getFrameDbList().sort((o1, o2) -> {
                return o1.getFrameCode() == o2.getFrameCode() ? o1.getReuseChannel() - o2.getReuseChannel() : o1.getFrameCode() - o2.getFrameCode();
            });
            for (int i = 0; i < catalogDb.getFrameDbList().size(); i++) {
                catalogDb.getFrameDbList().get(i).setNum(i + 1);
            }
        }
        baseParserService.validateFrame(frameDb, result);
        if (!result.isHasWrong()) {
            this.frameCatalogDbRepository.save(catalogDb);
        }
        return result;
    }

    public void updateFrameByFrameInfo(TeleFrameDbModel teleFrameDbModel) {
        frameDbRepository.updateFrameByFrameInfo(teleFrameDbModel.getFrameCode(), teleFrameDbModel.getFrameName(), teleFrameDbModel.getReuseChannel(), teleFrameDbModel.getNum(), teleFrameDbModel.getId());
    }
}
