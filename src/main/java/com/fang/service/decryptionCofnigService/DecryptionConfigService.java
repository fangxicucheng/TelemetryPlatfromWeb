package com.fang.service.decryptionCofnigService;

import com.fang.database.postgresql.entity.DecryptionConfig;
import com.fang.database.postgresql.repository.DecryptionConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecryptionConfigService {
    @Autowired
    private DecryptionConfigRepository decryptionConfigRepository;

    public List<DecryptionConfig> getDecryptionConfigList() {
        return this.decryptionConfigRepository.findAll();
    }

    public void saveOrUpdateDecryptionConfig(DecryptionConfig config) {
        this.decryptionConfigRepository.save(config);
    }

    public void saveOrUpdateDecryptionConfigList(List<DecryptionConfig> configList) {
        this.decryptionConfigRepository.saveAll(configList);
    }

    public void deleteById(Integer id) {
        this.decryptionConfigRepository.deleteById(id);
    }
}
