package com.fang.service.setExcpetionJuge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyFrameExceptionInfo {
    private Integer satelliteId;
    private Integer keyFrameId;
    private Integer keyFrameCode;
    private String keyFrameName;
    private List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList;
}
