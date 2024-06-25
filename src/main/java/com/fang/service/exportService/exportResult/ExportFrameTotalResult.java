package com.fang.service.exportService.exportResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportFrameTotalResult {
   private String frameName;
   private  List<ExportFrameSingleFrameResult>reusltList;
   public void addFrame(ExportFrameSingleFrameResult singleFrameResult){
       if(reusltList==null){
          this.reusltList=new ArrayList<>();
       }
       this.reusltList.add(singleFrameResult);
   }
}
