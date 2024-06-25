package com.fang.service.exportService.exportResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportResult {
   private Map<String,ExportFrameTotalResult>exportResult;
   public void addResult(ExportFrameSingleFrameResult singleFrameResult){
      if(exportResult==null){
         this.exportResult=new HashMap<>();
      }
      if(!exportResult.containsKey(singleFrameResult.frameName)){
         ExportFrameTotalResult totalResult = new ExportFrameTotalResult();
         totalResult.setFrameName(singleFrameResult.frameName);
         exportResult.put(singleFrameResult.frameName,totalResult);
      }
      exportResult.get(singleFrameResult.frameName).addFrame(singleFrameResult);
   }
}
