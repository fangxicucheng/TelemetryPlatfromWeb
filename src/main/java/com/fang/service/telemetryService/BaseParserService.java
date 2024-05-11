package com.fang.service.telemetryService;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.utils.ParseUtils;
import org.springframework.stereotype.Service;

@Service
public class BaseParserService {

    public void validateSatelliteDb(SatelliteDb satelliteDb, CheckConfigResult result) {

        if (!satelliteDb.getFrameCatalogDbList().isEmpty()) {
            for (FrameCatalogDb catalogDb : satelliteDb.getFrameCatalogDbList()) {
                if (satelliteDb.getFrameCatalogDbList().stream().filter(t -> t.getCatalogCode().equals(catalogDb.getCatalogCode())).count() > 2) {
                    result.setErrorMsg("重复帧类型码" + catalogDb.getCatalogCode());
                }
                validateFrameCatalog(catalogDb, result);
            }
        }
    }

    public void validateFrameCatalog(FrameCatalogDb catalogDb, CheckConfigResult result) {
        if (catalogDb.getCatalogName() == null && catalogDb.getCatalogName().isEmpty()) {
            result.setErrorMsg("未设置帧类型码为:" + catalogDb.getCatalogCode() + "帧类型名");
        }
        if (!catalogDb.getFrameDbList().isEmpty()) {
            for (FrameDb frameDb : catalogDb.getFrameDbList()) {
                if (catalogDb.getFrameDbList().stream().filter(t -> t.getFrameCode() == frameDb.getFrameCode() && t.getReuseChannel() == frameDb.getReuseChannel()).count() > 1) {
                    result.setErrorMsg(frameDb.getFrameName() + "帧编号重复");
                }
                if (catalogDb.getFrameDbList().stream().filter(t -> t.getFrameCode() == frameDb.getFrameCode()).count() > 1) {
                    result.setErrorMsg(frameDb.getFrameName() + "帧名称重复");
                }
                validateFrame(frameDb, result);

            }
        }

    }

    public void validateFrame(FrameDb frame, CheckConfigResult result) {

        if (frame.getFrameName() == null || frame.getFrameName().isEmpty()) {
            result.setErrorMsg("帧编号为" + frame.getFrameCode() + "没有帧名称");
        }
        if (frame.getFrameCode() < 0) {
            result.setErrorMsg(frame.getFrameName() + "帧序号为负");
        }
        if (frame.getReuseChannel() < 0) {
            result.setErrorMsg(frame.getFrameName() + "复播道为负");
        }
        if (!frame.getParaConfigLineDbList().isEmpty()) {
            for (ParaConfigLineDb paraConfigLineDb : frame.getParaConfigLineDbList()) {
                validateParaConfigLine(paraConfigLineDb, frame.getFrameName(), result);
            }
        }
    }
    public void validateParaConfigLine(ParaConfigLineDb paraConfigLineDb, String frameName, CheckConfigResult result) {
        if (paraConfigLineDb.getBitStart() < 0) {
            result.setErrorMsg(frameName + "     " + paraConfigLineDb.getParaCode() + "  的起始位为负");
        }
        if (paraConfigLineDb.getBitNum() < 0) {
            result.setErrorMsg(frameName + "   " + paraConfigLineDb.getParaCode() + "  的位宽为负");
        }
        if (paraConfigLineDb.getParseType() == 10) {
            switch (paraConfigLineDb.getSourceCodeSaveType()) {
                case "有符号":
                case "补码": {
                    if (!"十进制显示".equals(paraConfigLineDb.getHandleType())) {
                        result.setErrorMsg(frameName + "    " + paraConfigLineDb.getParaCode() + "的handleType错误");
                    }
                }
                break;
                case "无符号": {
                    switch (paraConfigLineDb.getHandleType()) {
                        case "状态码(二进制)": {
                            if (judgeHandleParam(2, paraConfigLineDb.getHandleParam())) {
                                result.setErrorMsg(frameName + "    " + paraConfigLineDb.getParaCode() + "的handleParam错误");
                            }
                        }
                        break;
                        case "状态码(十进制)": {
                            if (judgeHandleParam(10, paraConfigLineDb.getHandleParam())) {
                                result.setErrorMsg(frameName + "    " + paraConfigLineDb.getParaCode() + "的handleParam错误");
                            }
                        }
                        break;
                        case "状态码(十六进制)": {
                            if (judgeHandleParam(16, paraConfigLineDb.getHandleParam())) {
                                result.setErrorMsg(frameName + "    " + paraConfigLineDb.getParaCode() + "的handleParam错误");
                            }
                        }
                        break;
                        case "十进制显示":
                        case "十六进制":
                        case "公式":
                        case "时间":
                        case "原码显示":
                        case "32位单精度浮点数": {
                        }
                        break;
                        default: {
                            result.setErrorMsg(frameName + "    " + paraConfigLineDb.getParaCode() + "的handleType错误");
                        }
                        break;
                    }
                }
                break;
            }
        }
        if (judgeDimension(paraConfigLineDb.getDimension())) {
            result.setErrorMsg(frameName + "   " + paraConfigLineDb.getParaCode() + "   的量纲错误");
        }
    }
    public boolean judgeHandleParam(int number, String handleParam) {
        String[] handleParamArray = handleParam.replaceAll("\n", "").split("\r");
        if (handleParamArray != null && handleParamArray.length > 0) {
            for (String param : handleParamArray) {
                if (param.contains("=")) {
                    try {
                        Integer.parseInt(param.split("=")[0], number);
                    } catch (Exception e) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean judgeDimension(String dimension) {
        try {
           double dimensionValue= ParseUtils.getDimension(dimension);
            if(Double.isInfinite(dimensionValue)||Double.isNaN(dimensionValue)){
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

//    public double getDimension(String dimension) {
//        double result = 1;
//        if (dimension.contains("/")) {
//            String[] divArray = dimension.split("/");
//            result = parseStrToValue(divArray[0]) / parseStrToValue(divArray[1]);
//        } else {
//            result = parseStrToValue(dimension);
//        }
//        return result;
//    }
//
//    public double parseStrToValue(String str) {
//        double result = 1;
//        if (str.contains("0x")) {
//            result = Long.parseLong(str.replaceAll("0x", ""), 16);
//        } else if (str.contains("^")) {
//            String[] powArray = str.split("\\^");
//            result = Math.pow(Double.parseDouble(powArray[0]), Double.parseDouble(powArray[1]));
//        } else {
//            result = Double.parseDouble(str);
//        }
//        return result;
//    }
}




