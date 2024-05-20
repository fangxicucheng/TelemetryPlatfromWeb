package com.fang.controller;

import com.fang.database.postgresql.entity.DecryptionConfig;
import com.fang.service.decryptionCofnigService.DecryptionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/decryptionConfig")
public class DecryptionConfigController {
    @Autowired
    private DecryptionConfigService decryptionConfigService;


    @GetMapping("/getAllData")
    public List<DecryptionConfig> getDecryptionList() {
        return this.decryptionConfigService.getDecryptionConfigList();
    }

    @PostMapping("/saveOrUpdate")
    public List<DecryptionConfig> saveOrUpdateDecryption(@RequestBody DecryptionConfig decryptionConfig) {
        this.decryptionConfigService.saveOrUpdateDecryptionConfig(decryptionConfig);
        return this.decryptionConfigService.getDecryptionConfigList();
    }
    @PostMapping("/saveOrUpdateList")
    public List<DecryptionConfig> saveOrUpdateDecryptionList(@RequestBody List<DecryptionConfig> decryptionConfigList){
        this.decryptionConfigService.saveOrUpdateDecryptionConfigList(decryptionConfigList);
        return this.decryptionConfigService.getDecryptionConfigList();
    }
    @DeleteMapping("/delete/{id}")
    public List<DecryptionConfig> decryptionConfigList(@PathVariable Integer id){
        this.decryptionConfigService.deleteById(id);
        return this.decryptionConfigService.getDecryptionConfigList();
    }

}
