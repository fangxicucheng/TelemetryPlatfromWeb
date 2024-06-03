package com.fang.utils;

import lombok.Data;

@Data
public class BaseCri {
    private int curPageNum;
    private int totalPageNum;
    private int maxResult;
    private int resultCount;
}
