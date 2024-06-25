package com.fang.service.exportService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleParaCodeResult {
    private String paraCode;
    private String hexValueStr;
    private String paraValueStr;
}
