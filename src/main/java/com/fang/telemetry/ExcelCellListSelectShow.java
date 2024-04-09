package com.fang.telemetry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCellListSelectShow {
    String[] showDatas;
    int columnIndex;
}
