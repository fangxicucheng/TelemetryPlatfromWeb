package com.fang.service.parseTelemetry;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.database.postgresql.entity.SatelliteDb;

public class BaseParserService {

    public String validateSatelliteDb(SatelliteDb satelliteDb) {
      String result=null;
        if (!satelliteDb.getFrameCatalogDbList().isEmpty()) {
            for (FrameCatalogDb catalogDb : satelliteDb.getFrameCatalogDbList()) {
                if (satelliteDb.getFrameCatalogDbList().stream().filter(t->t.getCatalogCode().equals(catalogDb.getCatalogCode())).count()>2) {
                    String catalogResult="重复帧类型码"+catalogDb.getCatalogCode();
                    result=result==null?catalogResult:result+catalogResult;
                }
                String resultBuffer = validateFrameCatalog(catalogDb);
                result=result==null? resultBuffer :result+resultBuffer;
            }

        }

        return result;
    }

    public String validateFrameCatalog(FrameCatalogDb catalogDb) {
        String result=null;
        if(catalogDb.getCatalogName().isEmpty()){
            result = "未设置帧类型码为:"+catalogDb.getCatalogCode()+"帧类型名\r\n";
        }
        if(!catalogDb.getFrameDbList().isEmpty()){
            for (FrameDb frameDb : catalogDb.getFrameDbList()) {
                if (catalogDb.getFrameDbList().stream().filter(t->t.getFrameCode()==frameDb.getFrameCode()&&t.getReuseChannel()==frameDb.getReuseChannel()).count()>1) {
                    result=result==null?frameDb.getFrameName()+"帧编号重复\r\n":result+frameDb.getFrameName()+"帧编号重复\r\n";
                }
                if(catalogDb.getFrameDbList().stream().filter(t->t.getFrameCode()==frameDb.getFrameCode()).count()>1){
                    result=result==null?frameDb.getFrameName()+"帧名称重复\r\n":result+frameDb.getFrameName()+"帧名称重复\r\n";
                }
                String frameResult = validateFrame(frameDb);
                if(frameResult!=null){
                    result=result==null?frameResult:result+frameResult;
                }
            }
        }
        return result;
    }
    public String validateFrame(FrameDb frame) {
        String result=null;

        if(frame.getFrameName()==null||frame.getFrameName().isEmpty()){

            result="帧编号为"+frame.getFrameCode()+"没有帧名称\r\n";
        }
        if (frame.getFrameCode()<0) {

            result=result==null:frame.get



        }




        return result;
    }
    public String validateParaConfigLine(ParaConfigLineDb  paraConfigLineDb,String frameName){
        String result=null;


        return result;
    }



}
