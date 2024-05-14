package com.fang.utils;

import com.fang.config.satellite.configStruct.HandleType;
import com.fang.config.satellite.configStruct.ParaConfigLineConfigClass;
import com.fang.config.satellite.configStruct.SourceCodeSaveType;
import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.database.postgresql.entity.ParaConfigLineDb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParseUtils {

    public static Double getDimension(String dimension) {
        double result = 1;
        if (dimension == null) {
            return result;
        }
        if (dimension.contains("/")) {
            String[] divArray = dimension.split("/");
            result = parseStrToValue(divArray[0]) / parseStrToValue(divArray[1]);
        } else {
            result = parseStrToValue(dimension);
        }
        return result;
    }

    //宽带节点的校验
    public static void validateKDJDBytes(FrameInfo frameInfo) {
        boolean result = false;
        byte[] dataBytes = frameInfo.getDataBytes();
        if (dataBytes != null&&dataBytes.length==224) {
            if (dataBytes[0] != 0xFA || dataBytes[1] != 0xF2 || dataBytes[2] != 0x20) {
                result = false;
            } else {
                int crcSum = dataBytes[dataBytes.length - 2] * 256 + dataBytes[dataBytes.length - 1];

                byte[] needCheckBytes = Arrays.copyOfRange(dataBytes, 8, 221);
                result= CrcUtils.calculateCrcSum(needCheckBytes)==crcSum;
            }

        }
        frameInfo.setValid(result);

    }


    public static Double parseStrToValue(String str) {
        double result = 1;
        if (StringConvertUtils.testStringEmpty(str)) {
            return 1.0;
        }
        if (str.contains("0x")) {
            result = Long.parseLong(str.replaceAll("0x", ""), 16);
        } else if (str.contains("^")) {
            String[] powArray = str.split("\\^");
            result = Math.pow(Double.parseDouble(powArray[0]), Double.parseDouble(powArray[1]));
        } else {
            result = Double.parseDouble(str);
        }
        return result;
    }


    public static void initParaConfigClass(ParaConfigLineDb paraConfigLineDb, ParaConfigLineConfigClass paraConfigLineConfigClass) {

        switch (paraConfigLineDb.getParseType()) {
            case 3:
            case 4: {
                setSourceCodeShow(paraConfigLineConfigClass);

            }
            break;
            case 1: {
                setSingedDecimalShow(paraConfigLineConfigClass);
            }
            break;
            case 2: {
                setComplement(paraConfigLineConfigClass);
            }
            break;
            case 5: {
                setIEE754Float(paraConfigLineConfigClass);
            }
            break;
            case 10: {
                String sourceCodeSaveType = paraConfigLineDb.getSourceCodeSaveType() == null ? "" : StringConvertUtils.removeInvisibleCharacters(paraConfigLineDb.getSourceCodeSaveType()).replaceAll(" ", "");
                switch (sourceCodeSaveType) {
                    case "补码":
                    case "有符号": {
                        setComplement(paraConfigLineConfigClass);
                    }
                    break;
                    case "有符号位": {
                        setSingedDecimalShow(paraConfigLineConfigClass);
                    }
                    break;
                    case "无符号": {
                        String handleType = paraConfigLineDb.getHandleType() == null ? "" : StringConvertUtils.removeInvisibleCharacters(paraConfigLineDb.getHandleType()).replaceAll(" ", "");
                        switch (handleType) {
                            case "状态码(二进制)": {
                                Map<Double, String> stateMap = initStateMap(2, paraConfigLineDb.getHandleParam());
                                setStateCodeStrShow(paraConfigLineConfigClass);
                                paraConfigLineConfigClass.setStateParseMap(stateMap);

                            }
                            break;
                            case "状态码(十进制)": {
                                Map<Double, String> stateMap = initStateMap(10, paraConfigLineDb.getHandleParam());
                                setStateCodeStrShow(paraConfigLineConfigClass);
                                paraConfigLineConfigClass.setStateParseMap(stateMap);
                            }
                            break;
                            case "状态码(十六制)": {
                                Map<Double, String> stateMap = initStateMap(16, paraConfigLineDb.getHandleParam());
                                setStateCodeStrShow(paraConfigLineConfigClass);
                                paraConfigLineConfigClass.setStateParseMap(stateMap);
                            }
                            break;
                            case "时间": {
                                setTimeShow(paraConfigLineConfigClass);
                            }
                            break;
                            case "原码":
                            case "原码显示":
                            case "源码":
                            case "源码显示":
                            case "十六进制显示":
                            case "十六进制": {
                                setSourceCodeShow(paraConfigLineConfigClass);
                            }
                            break;
                            case "32位单精度浮点数": {
                                setIEE754Float(paraConfigLineConfigClass);
                            }
                            break;
                        }

                    }
                    break;

                }
            }
            break;
            default: {
                setDecimalShow(paraConfigLineConfigClass);
            }
            break;
        }


    }


    public static void setDecimalShow(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.无符号);
        paraConfigLineConfigClass.setHandleType(HandleType.十进制显示);
    }

    public static void setSourceCodeShow(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.无符号);
        paraConfigLineConfigClass.setHandleType(HandleType.源码);
    }

    public static void setIEE754Float(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setHandleType(HandleType.单精度浮点数);
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.无符号);
    }

    public static void setComplement(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.补码);
        paraConfigLineConfigClass.setHandleType(HandleType.补码);
    }

    public static void setSingedDecimalShow(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.有符号位);
        paraConfigLineConfigClass.setHandleType(HandleType.有符号位);
    }

    public static void setTimeShow(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.无符号);
        paraConfigLineConfigClass.setHandleType(HandleType.时间);
    }

    public static void setStateCodeStrShow(ParaConfigLineConfigClass paraConfigLineConfigClass) {
        paraConfigLineConfigClass.setHandleType(HandleType.状态码);
        paraConfigLineConfigClass.setSourceCodeSaveType(SourceCodeSaveType.无符号);
    }

    public static Map<Double, String> initStateMap(Integer index, String handleParam) {
        Map<Double, String> result = new HashMap<>();
        String[] handleParamArray = handleParam.replaceAll("\n", "").split("\r");
        if (handleParamArray != null && handleParamArray.length > 0) {
            for (String paramInfo : handleParamArray) {
                if (paramInfo.contains("=")) {
                    String[] paramInfoArray = paramInfo.split("=");
                    if (paramInfoArray.length == 2) {
                        Double parseValue = Double.valueOf(Long.parseLong(StringConvertUtils.removeUnableNumberCharacters(paramInfoArray[0], index), index));
                        result.put(parseValue, paramInfoArray[1]);
                    }
                }
            }
        }
        return result;
    }
}

