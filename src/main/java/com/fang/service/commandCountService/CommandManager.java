package com.fang.service.commandCountService;

import com.fang.database.postgresql.entity.CommandCount;
import com.fang.telemetry.TelemetryFrameModel;
import com.fang.telemetry.TelemetryParameterModel;
import com.fang.utils.ConfigUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CommandManager {
    private String satelliteName;
    private boolean hasCommandManager;
    private Map<String, CommandCount> commandCountMap;
    private int totalCommandCountInit;
    private int errorCommandCountInit;
    private int totalCommandCountLatest;
    private int errorCommandCountLatest;
    private boolean hasReceived;
    private int needInsertCommandCount;
    private int hasInsertCommandCount;

    public CommandManager(String satelliteName) {
        this.satelliteName = satelliteName;
        this.hasCommandManager = false;

    }

    public void refresh() {
        this.hasReceived = false;
        this.totalCommandCountLatest = 0;
        this.totalCommandCountInit = 0;
        this.errorCommandCountInit = 0;
        this.errorCommandCountLatest = 0;
        List<CommandCount> commandCountList = ConfigUtils.getCommandCountList(this.satelliteName);
        if (commandCountList != null) {
            for (CommandCount commandCount : commandCountList) {
                if (commandCountMap == null) {
                    commandCountMap = new HashMap<>();
                }
                commandCountMap.put(commandCount.getFrameName(), commandCount);
            }
        }
    }

    public void setNeedInsertCommandInfo(int needInsertCommandCount) {
        this.needInsertCommandCount = needInsertCommandCount;
        this.hasInsertCommandCount = 0;
    }

    public void setCommandCount(TelemetryFrameModel frame) {
        if (commandCountMap.containsKey(frame.getFrameName())) {

        }


    }

    private void setCommandParaValue(TelemetryFrameModel frame, CommandCount commandCount) {
        String errorCommandParaCode = commandCount.getErrorCommandParaCode();
        String totalCommandParaCode = commandCount.getTotalCommandParaCode();
        int totalCommandCountBuffer = 0;
        int errorCommandCountBuffer = 0;
        for (TelemetryParameterModel telemetryParameterModel : frame.getParameterList()) {
            String paraCode = telemetryParameterModel.getParaCode();
            double paraValue = telemetryParameterModel.getParaDouble();
            if (totalCommandParaCode.equals(paraCode)) {
                totalCommandCountBuffer = (int) paraValue;
            } else if (errorCommandParaCode.equals(paraCode)) {
                if (paraValue < 0) {
                    return;
                }
                errorCommandCountBuffer = (int) paraValue;
                break;
            }
        }
        if (errorCommandCountBuffer < 0 || totalCommandCountBuffer < 0) {
            return;
        }
        if (!this.hasReceived) {
            this.totalCommandCountInit = totalCommandCountBuffer;
            this.errorCommandCountInit = errorCommandCountBuffer;
            this.hasReceived = true;
        }
        this.errorCommandCountLatest = errorCommandCountBuffer;
        this.totalCommandCountLatest = totalCommandCountBuffer;

        this.hasInsertCommandCount = calculateTotalCommandCountChange(commandCount) - calculateErrorCommandCountChange(commandCount);

    }

    private void finish(){
        this.needInsertCommandCount=0;
        this.hasInsertCommandCount=0;
        this.commandCountMap.clear();
        this.hasReceived=false;
        this.totalCommandCountInit=0;
        this.errorCommandCountInit=0;
        this.totalCommandCountLatest=0;
        this.errorCommandCountLatest=0;
    }

    /// <summary>
    /// 计算总计数
    /// </summary>
    /// <param name="commandManager"></param>
    /// <returns></returns>
    private int calculateTotalCommandCountChange(CommandCount commandCount) {
        int result = 0;
        result = this.totalCommandCountLatest - this.totalCommandCountInit;
        if (result < 0) {
            result += commandCount.getTotalCommandCountMax();
        }
        return result;
    }

    /// <summary>
    /// 计算异常差值
    /// </summary>
    /// <param name="commandManager"></param>
    /// <returns></returns>
    private int calculateErrorCommandCountChange(CommandCount commandCount) {
        int result = 0;
        result = this.errorCommandCountLatest - this.errorCommandCountInit;
        if (result < 0) {
            result += commandCount.getErrorCommandCountMax();
        }
        return result;
    }


}
