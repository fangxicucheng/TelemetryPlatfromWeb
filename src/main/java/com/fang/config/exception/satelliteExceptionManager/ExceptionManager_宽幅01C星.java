package com.fang.config.exception.satelliteExceptionManager;

import com.fang.config.exception.ExceptionManager;
import lombok.Data;

import java.util.Map;

@Data

public class ExceptionManager_宽幅01C星 implements ExceptionManager {
    private ThreadLocal<Integer> zt150 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt112 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt113 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt114 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt122 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt123 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt124 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt138 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt139 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt140 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt141 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt142 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt143 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt144 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt145 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt146 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt147 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt148 = new ThreadLocal<>();
    private ThreadLocal<Integer> zt149 = new ThreadLocal<>();
    private ThreadLocal<Integer> zg144 = new ThreadLocal<>();
    private ThreadLocal<Integer> zg163 = new ThreadLocal<>();
    private ThreadLocal<Integer> zg182 = new ThreadLocal<>();


    @Override
    public void initThread() {
        zt150.set(0);
        zt112.set(0);
        zt113.set(0);
        zt114.set(0);
        zt122.set(0);
        zt123.set(0);
        zt124.set(0);
        zt138.set(0);
        zt139.set(0);
        zt140.set(0);
        zt141.set(0);
        zt142.set(0);
        zt143.set(0);
        zt144.set(0);
        zt145.set(0);
        zt146.set(0);
        zt147.set(0);
        zt148.set(0);
        zt149.set(0);
        zg144.set(0);
        zg163.set(0);
        zg182.set(0);
    }

    @Override
    public void detroyThread() {
        zt150.remove();
        zt112.remove();
        zt113.remove();
        zt114.remove();
        zt122.remove();
        zt123.remove();
        zt124.remove();
        zt138.remove();
        zt139.remove();
        zt140.remove();
        zt141.remove();
        zt142.remove();
        zt143.remove();
        zt144.remove();
        zt145.remove();
        zt146.remove();
        zt147.remove();
        zt148.remove();
        zt149.remove();
        zg144.remove();
        zg163.remove();
        zg182.remove();
    }

    @Override
    public boolean judgeMatch(Map<String, Double> real, String paraCode, Double paraValue) {
        boolean matchResult = true;
        switch (paraCode) {
            case "ZT-C150": {
                Integer zt150Value = this.zt150.get();
                if (paraValue == 1) {
                    zt150Value += 1;
                } else {
                    zt150Value = 0;
                }

                if (zt150Value > 60) {
                    matchResult = false;
                }
                this.zt150.set(zt150Value);
            }
            break;
            case "ZT-C112": {
                Integer zt112Value = this.zt112.get();
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13 && real.containsKey("ZT-C120") &&
                        real.get("ZT-C120") > 1.2 && real.get("ZT-C120") < 4) {

                    if (paraValue == 1) {
                        zt112Value += 1;
                    } else {
                        zt112Value = 0;
                    }
                } else {
                    zt112Value = 0;
                }

                if (zt112Value > 60) {
                    matchResult = false;
                }
                this.zt112.set(zt112Value);
            }
            break;
            case "ZT-C113": {
                Integer zt113Value = this.zt113.get();
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13 && real.containsKey("ZT-C120") &&
                        real.get("ZT-C120") > 1.2 && real.get("ZT-C120") < 4) {
                    if (paraValue == 1) {
                        zt113Value += 1;
                    } else {
                        zt113Value = 0;
                    }
                } else {
                    zt113Value = 0;
                }

                if (zt113Value > 60) {
                    matchResult = false;
                }
                this.zt113.set(zt113Value);
            }
            break;
            case "ZT-C114": {
                Integer zt114Value = this.zt114.get();
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13 && real.containsKey("ZT-C120") &&
                        real.get("ZT-C120") > 1.2 && real.get("ZT-C120") < 4) {
                    if (paraValue == 1) {
                        zt114Value += 1;
                    } else {
                        zt114Value = 0;
                    }
                } else {
                    zt114Value = 0;
                }

                if (zt114Value > 60) {
                    matchResult = false;
                }
                this.zt114.set(zt114Value);
            }
            break;
            case "ZT-C120": {
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13) {
                    if (paraValue <= 0.9) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZT-C122": {
                Integer zt122Value = this.zt122.get();
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13 && real.containsKey("ZT-C120") &&
                        real.get("ZT-C130") > 1.2 && real.get("ZT-C130") < 4) {
                    if (paraValue == 1) {
                        zt122Value += 1;
                    } else {
                        zt122Value = 0;
                    }
                } else {
                    zt122Value = 0;
                }

                if (zt122Value > 60) {
                    matchResult = false;
                }
                this.zt122.set(zt122Value);
            }
            break;
            case "ZT-C123": {
                Integer zt123Value = this.zt123.get();
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13 && real.containsKey("ZT-C120") &&
                        real.get("ZT-C130") > 1.2 && real.get("ZT-C130") < 4) {
                    if (paraValue == 1) {
                        zt123Value += 1;
                    } else {
                        zt123Value = 0;
                    }
                } else {
                    zt123Value = 0;
                }

                if (zt123Value > 60) {
                    matchResult = false;
                }
                this.zt123.set(zt123Value);
            }
            break;
            case "ZT-C124": {
                Integer zt124Value = this.zt124.get();
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13 && real.containsKey("ZT-C120") &&
                        real.get("ZT-C130") > 1.2 && real.get("ZT-C130") < 4) {
                    if (paraValue == 1) {
                        zt124Value += 1;
                    } else {
                        zt124Value = 0;
                    }
                } else {
                    zt124Value = 0;
                }

                if (zt124Value > 60) {
                    matchResult = false;
                }
                this.zt124.set(zt124Value);
            }
            break;
            case "ZT-C130": {
                if (real.containsKey("ZT-C021") && real.get("ZT-C021") != 6 && real.get("ZT-C021") != 7 &&
                        real.get("ZT-C021") != 0x13) {
                    if (paraValue <= 0.9) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "RK-C091": {
                if (real.containsKey("RK-C092") && Math.abs(real.get("RK-C092")) <= 1 &&
                        real.containsKey("RK-C093") && Math.abs(real.get("RK-C093")) <= 1) {
                    if ((paraValue + real.get("RK-C092") + real.get("RK-C093")) != 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "RK-C092": {
                if (real.containsKey("RK-C091") && Math.abs(real.get("RK-C091")) <= 1 &&
                        real.containsKey("RK-C093") && Math.abs(real.get("RK-C093")) <= 1) {
                    if ((paraValue + real.get("RK-C091") + real.get("RK-C093")) != 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "RK-C093": {
                if (real.containsKey("RK-C091") && Math.abs(real.get("RK-C091")) <= 1 &&
                        real.containsKey("RK-C092") && Math.abs(real.get("RK-C092")) <= 1) {
                    if ((paraValue + real.get("RK-C091") + real.get("RK-C092")) != 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "RK-C094": {
                if (real.containsKey("RK-C095") && Math.abs(real.get("RK-C095")) <= 1 &&
                        real.containsKey("RK-C096") && Math.abs(real.get("RK-C096")) <= 1) {
                    if ((paraValue + real.get("RK-C095") + real.get("RK-C096")) != 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "RK-C095": {
                if (real.containsKey("RK-C094") && Math.abs(real.get("RK-C094")) <= 1 &&
                        real.containsKey("RK-C096") && Math.abs(real.get("RK-C096")) <= 1) {
                    if ((paraValue + real.get("RK-C094") + real.get("RK-C096")) != 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "RK-C096": {
                if (real.containsKey("RK-C094") && Math.abs(real.get("RK-C094")) <= 1 &&
                        real.containsKey("RK-C095") && Math.abs(real.get("RK-C095")) <= 1) {
                    if ((paraValue + real.get("RK-C094") + real.get("RK-C095")) != 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZK-C010":
            case "ZK-C011":
            case "ZK-C012": {
                if (real.containsKey("ZT-C020") && real.get("ZT-C020") == 3 &&
                        real.containsKey("ZK-C001") && real.get("ZK-C001") == 4
                        && real.containsKey("ZK-C032") && real.get("ZK-C032") > 550) {
                    if (paraValue >= 0.01) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZK-C096":
            case "ZK-C097": {
                if (real.containsKey("ZT-C138") &&
                        real.containsKey("ZT-C139") &&
                        real.containsKey("ZT-C140") &&
                        real.containsKey("ZT-C141") &&
                        real.containsKey("ZT-C142") &&
                        real.containsKey("ZT-C143") &&
                        real.containsKey("ZT-C144") &&
                        real.containsKey("ZT-C145") &&
                        real.containsKey("ZT-C146") &&
                        real.containsKey("ZT-C147") &&
                        real.containsKey("ZT-C148") &&
                        real.containsKey("ZT-C149")) {
                    if (real.get("ZT-C138") != 0 &&
                            real.get("ZT-C139") != 0 &&
                            real.get("ZT-C140") != 0 &&
                            real.get("ZT-C141") != 0 &&
                            real.get("ZT-C142") != 0 &&
                            real.get("ZT-C143") != 0 &&
                            real.get("ZT-C144") != 0 &&
                            real.get("ZT-C145") != 0 &&
                            real.get("ZT-C146") != 0 &&
                            real.get("ZT-C147") != 0 &&
                            real.get("ZT-C148") != 0 &&
                            real.get("ZT-C149") != 0) {
                        if (real.containsKey("ZK-C001") && real.containsKey("ZK-C032") &&
                                real.get("ZK-C001") == 0x04 && real.get("ZK-C032") > 550) {
                            if (paraValue >= 6) {
                                matchResult = false;
                            }
                        }
                    }
                }
            }
            break;
            case "ST-C027":
            case "ST-C028":
            case "ST-C029": {
                if (real.containsKey("ZT-C020") && real.get("ZT-C020") == 6 &&
                        real.containsKey("ZT-C075") && real.get("ZT-C075") == 1
                ) {
                    if (Math.abs(paraValue) > 0.86) {
                        matchResult = false;
                    }
                } else {
                    if (Math.abs(paraValue) > 0.5) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZG-C042": {
                if (real.containsKey("ZK-C057") &&
                        real.get("ZK-C057") == 1 &&
                        real.containsKey("ZT-C138") &&
                        real.containsKey("ZT-C139") &&
                        (real.get("ZT-C138") == 1 ||
                                real.get("ZT-C139") == 1) && real.containsKey("ZG-C043")
                ) {
                    if (real.get("ZG-C043") == 511 && paraValue == 511) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZG-C043": {
                if (real.containsKey("ZK-C057") &&
                        real.get("ZK-C057") == 1 &&
                        real.containsKey("ZT-C138") &&
                        real.containsKey("ZT-C139") &&
                        (real.get("ZT-C138") == 1 ||
                                real.get("ZT-C139") == 1) && real.containsKey("ZG-C042")
                ) {
                    if (real.get("ZG-C042") == 511 && paraValue == 511) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZG-C040": {
                if (real.containsKey("RK-C216") && real.get("RK-C216") == 1 &&
                        real.containsKey("ZG-C039") && real.get("ZG-C039") == 2) {
                    if (paraValue == 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZG-C041": {
                if (real.containsKey("RK-C216") && real.get("RK-C216") == 1 &&
                        real.containsKey("ZG-C039") && real.get("ZG-C039") == 1) {
                    if (paraValue == 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "FL-C028": {
                if (real.containsKey("RK-C243") && real.get("RK-C243") == 1 &&
                        real.containsKey("FL-C027") && real.get("FL-C027") == 2) {
                    if (paraValue == 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "FL-C029": {
                if (real.containsKey("RK-C243") && real.get("RK-C243") == 1 &&
                        real.containsKey("FL-C027") && real.get("FL-C027") == 1) {
                    if (paraValue == 1) {
                        matchResult = false;
                    }
                }
            }
            break;
            case "ZG-C144": {
                if (real.containsKey("ZG-C137") && real.get("ZG-C137") == 4) {
                    Integer zg144Value = this.zg144.get();

                    if (paraValue == 0) {
                        zg144Value++;
                    } else {
                        zg144Value = 0;
                    }

                    if (zg144Value >= 60) {
                        matchResult = false;
                    }
                    this.zg144.set(zg144Value);
                }
            }
            break;
            case "ZG-C163": {
                if (real.containsKey("ZG-C156") && real.get("ZG-C156") == 4) {
                    Integer zg163Value = this.zg163.get();
                    if (paraValue == 0) {
                        zg163Value++;
                    } else {
                        zg163Value = 0;
                    }

                    if (zg163Value >= 60) {
                        matchResult = false;
                    }
                    this.zg163.set(zg163Value);
                }
            }
            break;
            case "ZG-C182": {
                if (real.containsKey("ZG-C175") && real.get("ZG-C175") == 4) {
                    Integer zg182Value = this.zg182.get();
                    if (paraValue == 0) {
                        zg182Value++;
                    } else {
                        zg182Value = 0;
                    }

                    if (zg182Value >= 60) {
                        matchResult = false;
                    }
                    this.zg182.set(zg182Value);
                }
            }
            break;
        }


        return matchResult;
    }
}
