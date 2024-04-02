package com.fang.service.setExcpetionJuge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParaConfigLineExceptionInfo {
    private Integer id;
    private String paraCode;
    private String paraName;
    private String paraNameRead;
    private String exp;
    private String subSystem;
}
