package com.fang.telemetry.satelliteConfigModel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CheckConfigResult {
    boolean hasWrong;
    List<String> errorMsgList;

    public CheckConfigResult() {
        this.hasWrong=false;
        this.errorMsgList=new ArrayList<>();
    }
    public void setErrorMsg(String errorMsg){
        this.hasWrong=true;
        this.errorMsgList.add(errorMsg);

    }
}
