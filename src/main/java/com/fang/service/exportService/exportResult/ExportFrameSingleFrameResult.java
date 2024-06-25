package com.fang.service.exportService.exportResult;

import com.fang.service.exportService.SingleParaCodeResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportFrameSingleFrameResult {
    String frameName;
    private Map<String, SingleParaCodeResult> paraCodeResult;
    public void addParaCodeResult(String paraCode,String hexValue,String paraValueStr)
    {
        if(this.paraCodeResult==null){
            this.paraCodeResult=new HashMap<>();
        }
        this.paraCodeResult.put(paraCode,new SingleParaCodeResult(paraCode,hexValue,paraValueStr));
    }

    public SingleParaCodeResult getSingleParaCodeResult(String paraCode){
        if(this.paraCodeResult!=null&&this.paraCodeResult.containsKey(paraCode)){
            return this.paraCodeResult.get(paraCode);
        }
        return null;
    }
}
