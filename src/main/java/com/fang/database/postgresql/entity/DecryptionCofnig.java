package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleDecryptionCofnigMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cg_telemetry_decryption_cofnig")
public class DecryptionCofnig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="decryption_status_para_code")
    private String decryptionStatusParaCode;
    @Column(name="secret_key_para_code")
    private String secretKeyParaCode;
    @Column(name="satellite_code")
    private Integer satelliteCode;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="encrypted_status_value")
    private Integer encryptedStatusValue;
    @Column(name="non_encrypted_status_value")
    private Integer noneEncryptedStatusValue;

    public DecryptionCofnig(TeleDecryptionCofnigMq decryptionConfigMq) {
        this.satelliteName=decryptionConfigMq.getSatelliteName();
        this.decryptionStatusParaCode=decryptionConfigMq.getDecryptionStatusParaCode();
        this.secretKeyParaCode=decryptionConfigMq.getSecretKeyParaCode();
        this.satelliteCode=decryptionConfigMq.getSatelliteCode();
        this.frameName=decryptionConfigMq.getFrameName();
        this.encryptedStatusValue=decryptionConfigMq.getEncryptedStatusValue();
        this.noneEncryptedStatusValue=decryptionConfigMq.getNoneEncryptedStatusValue();
    }
}
