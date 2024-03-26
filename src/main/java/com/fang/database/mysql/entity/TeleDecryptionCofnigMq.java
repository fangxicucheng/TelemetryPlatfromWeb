package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tele_decryption_cofnig")
public class TeleDecryptionCofnigMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="decryption_status_para_code")
    private String decryptionStatusParaCode;
    @Column(name="sercet_key_para_code")
    private String secretKeyParaCode;
    @Column(name="satellite_code")
    private Integer satelliteCode;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="encrypted_status_value")
    private Integer encryptedStatusValue;
    @Column(name="non_encrypted_status_value")
    private Integer noneEncryptedStatusValue;
}
