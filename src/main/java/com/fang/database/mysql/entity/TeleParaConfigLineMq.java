package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tele_para_configline")
public class TeleParaConfigLineMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private int id;
    @Column(name="para_code")
    private String paraCode;
    @Column(name="para_name")
    private String paraName;
    @Column(name="bit_start")
    private Integer bitStart;
    @Column(name="bit_num")
    private Integer bitNum;
    @Column(name="dimension")
    private String dimension;
    @Column(name="parse_type")
    private Integer parseType;
    @Column(name="source_code_save_type")
    private String sourceCodeSaveType;
    @Column(name="handle_type")
    private String handleType;
    @Column(name="handle_param",length = 2048)
    private String handleParam;
    @Column(name="unit")
    private String unit;
    @Column(name="round")
    private Integer round;
    @Column(name="comment")
    private String comment;
    @Column(name="visual")
    private Integer visual;
    @Column(name="num")
    private Integer num;
    @Column(name="exp")
    private String exp;
    @Column(name="subsystem")
    private String subSystem;

}
