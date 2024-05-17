package com.fang.service.saveService;

import com.fang.service.dataBaseManager.DataBaseManagerService;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FileSaver {
    private List<byte[]>fileLines;
    private Date startTime;
    private DataBaseManagerService dataBaseManagerService;

    public FileSaver() {
        this.fileLines=new ArrayList<>();
    }

    public void save(){

    }





}
