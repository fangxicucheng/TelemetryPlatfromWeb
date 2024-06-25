package com.fang.config.satellite.paraParser.satelliteParaParser;

import com.fang.config.satellite.paraParser.BaseParaParser;
import com.fang.service.telemetryService.SatelliteTimeManager;

import java.util.HashMap;
import java.util.Map;

public class ParaParser_宽幅01C星 extends BaseParaParser {
    private ThreadLocal<Double> v5_1 = new ThreadLocal<>();
    private ThreadLocal<Double> v5_2 = new ThreadLocal<>();
    private ThreadLocal<Double> v5 = new ThreadLocal<>();// 4.8;
    private ThreadLocal<Double> sampleTime = new ThreadLocal<>();//1;
    private ThreadLocal<Double> tmc011 = new ThreadLocal<>();//0;

    private ThreadLocal<Double> tmc012 = new ThreadLocal<>();// 0;

    @Override
    public void initThread() {


        this.v5_1.set(0.0);
        this.v5_2.set(0.0);
        this.v5.set(4.8);
        this.sampleTime.set(1.0);
        this.tmc011.set(0.0);
        this.tmc012.set(0.0);
        super.initThread();
    }

    @Override
    public void destroyThread() {
        this.v5_1.remove();
        this.v5_2.remove();
        this.v5.remove();
        this.sampleTime.remove();
        this.tmc011.remove();
        this.tmc012.remove();
        super.destroyThread();
    }

    @Override
    public double getSpecialFormulaValue(String paraCode, double paraValue) {

        double calValue = paraValue;

        switch (paraCode) {
            case "RK-C260": {
                double k = 0.22;
                calValue = (paraValue * 5 / 255 + k) * 12;
            }
            break;
            case "RK-C266": {
                double A = 0.714556537;
                double B = -0.076525602;
                calValue = paraValue * A * 10 / 4095 + B;
            }
            break;
            /*母线电压*/
            case "DY-C001": {
                calValue = dy(paraValue, 7.725, 0);
            }
            break;
            /*负载电流*/
            case "DY-C002": {
                calValue = dy(paraValue, 15.969, 0.2309);
            }
            break;
            /*蓄电池A电压*/
            case "DY-C003": {
                calValue = dy(paraValue, 7.7494, -0.012);
            }
            break;
            /*蓄电池B电压*/
            case "DY-C004": {
                calValue = dy(paraValue, 7.8125, -0.104);
            }
            break;
            /*（+X阵，-X阵）输入电流*/
            case "DY-C005": {
                calValue = dy(paraValue, 12.014, 0.0977);
            }
            break;
            /*（+Y阵，-Y阵）输入电流*/
            case "DY-C006": {
                calValue = dy(paraValue, 11.976, 0.02);
            }
            break;
            /*主误差放大器电压*/
            case "DY-C007": {
                calValue = dy(paraValue, 3.0596, -0.058);
            }
            break;
            /*A-BEA电压（充电器恒流恒压分界点）*/
            case "DY-C008": {
                calValue = dy(paraValue, 3.0627, 0.0005);
            }
            break;
            /*蓄电池A充电电流1*/
            case "DY-C009": {
                calValue = dy(paraValue, 4.043, -0.1317);
            }
            break;
            /*蓄电池A充电电流2*/
            case "DY-C010": {
                calValue = dy(paraValue, 4.0154, 0.0941);
            }
            break;
            /*蓄电池A充电器1输入电流*/
            case "DY-C011": {
                calValue = dy(paraValue, 5.9241, -0.4289);
            }
            break;
            /*蓄电池A充电器2输入电流*/
            case "DY-C012": {
                calValue = dy(paraValue, 5.9202, -0.4258);
            }
            break;
            /*蓄电池A充电器3输入电流*/
            case "DY-C013": {
                calValue = dy(paraValue, 5.9417, -0.4405);
            }
            break;
            /*蓄电池A充电器4输入电流*/
            case "DY-C014": {
                calValue = dy(paraValue, 5.9697, -0.5207);
            }
            break;
            /*蓄电池A单体1电压*/
            case "DY-C015":
                /*蓄电池A单体2电压*/
            case "DY-C016":
                /*蓄电池A单体3电压*/
            case "DY-C017":
                /*蓄电池A单体4电压*/
            case "DY-C018":
                /*蓄电池A单体5电压*/
            case "DY-C019":
                /*蓄电池A单体6电压*/
            case "DY-C020":
                /*蓄电池A单体7电压*/
            case "DY-C021": {
                calValue = dy(paraValue, 1, 0);
            }
            break;
            /*B-BEA电压（充电器恒流恒压分界点）*/
            case "DY-C022": {
                calValue = dy(paraValue, 3.0386, 0.0449);
            }
            break;
            /*蓄电池B充电电流1*/
            case "DY-C023": {
                calValue = dy(paraValue, 4.0304, -0.1928);
            }
            break;
            /*蓄电池B充电电流2*/
            case "DY-C024": {
                calValue = dy(paraValue, 4.0211, -0.0998);
            }
            break;
            /*蓄电池B充电器1输入电流*/
            case "DY-C025": {
                calValue = dy(paraValue, 5.9135, -0.426);
            }
            break;
            /*蓄电池B充电器2输入电流*/
            case "DY-C026": {
                calValue = dy(paraValue, 5.9801, -0.4757);
            }
            break;
            /*蓄电池B充电器3输入电流*/
            case "DY-C027": {
                calValue = dy(paraValue, 5.9066, -0.4114);
            }
            break;
            /*蓄电池B充电器4输入电流*/
            case "DY-C028": {
                calValue = dy(paraValue, 5.9415, -0.4316);
            }
            break;
            /*蓄电池B单体1电压*/
            case "DY-C029":
                /*蓄电池B单体2电压*/
            case "DY-C030":
                /*蓄电池B单体3电压*/
            case "DY-C031":
                /*蓄电池B单体4电压*/
            case "DY-C032":
                /*蓄电池B单体5电压*/
            case "DY-C033":
                /*蓄电池B单体6电压*/
            case "DY-C034":
                /*蓄电池B单体7电压*/
            case "DY-C035": {
                calValue = dy(paraValue, 1, 0);
            }
            break;
            /*蓄电池A放电电流*/
            case "DY-C036": {
                calValue = dy(paraValue, 15.954, 0.0433);
            }
            break;
            /*蓄电池B放电电流*/
            case "DY-C037": {
                calValue = dy(paraValue, 15.862, 0.0135);
            }
            break;
            /*控制+12V电压遥测*/
            case "DY-C038": {
                calValue = dy255(paraValue, 4.1072, 0);
            }
            break;
            /*下位机-12V电压*/
            case "DY-C039": {
                calValue = dy255(paraValue, -3.9294, 0);
            }
            break;
            /*下位机+12V电压*/
            case "DY-C040": {
                calValue = dy255(paraValue, 4.0048, 0);
            }
            break;
            /*下位机5V电压*/
            case "DY-C041": {
                calValue = dy255(paraValue, 2.0008, 0);
            }
            break;
            case "GP-C021":
            case "GP-C022": {
                calValue = paraValue * 2 + 30;
            }
            break;
            case "TM-C011": {
                double value = ((long) paraValue & 0xFFFFFF00);
                this.tmc011.set(value);
            }
            break;

            case "ZG-C060": {
                calValue = paraValue + this.tmc011.get();
            }
            break;
            case "TM-C012": {
                double value = ((long) paraValue) & 0xFFFFFF00;
                this.tmc012.set(value);
            }
            break;
            case "ZG-C090": {
                calValue = paraValue + this.tmc012.get();
            }
            break;
            case "ZG-C100": {
                calValue = paraValue + this.tmc012.get();
            }
            break;


            case "ZG-C042": {
                calValue = dy(paraValue, 2.0008, 0);
            }
            break;

            case "ZG-C115":
            case "ZG-C117":
            case "ZG-C119": {
                double R = calculateR(paraValue);
                double a = -5.7;
                double b = 4474.95;
                double c = -69934.65;
                calValue = calculateT(R, a, b, c);
            }
            break;

            case "RK-C287":
            case "RK-C288":
            case "RK-C289":
            case "RK-C290":
            case "RK-C291":
            case "RK-C292":
            case "RK-C293":
            case "RK-C294":
            case "RK-C295":
            case "RK-C296":
            case "RK-C297":
            case "RK-C298":
            case "RK-C299":
            case "RK-C300":
            case "RK-C301":
            case "RK-C302":
            case "RK-C303":
            case "RK-C304":
            case "RK-C305":
            case "RK-C306":
            case "RK-C307":
            case "RK-C308":
            case "RK-C309":
            case "RK-C310":
            case "RK-C311":
            case "RK-C312":
            case "RK-C313": {
                double R = calculateR12(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;

            case "RK-C315":
            case "RK-C316":
            case "RK-C317":
            case "RK-C318":
            case "RK-C319":
            case "RK-C320":
            case "RK-C321":
            case "RK-C322":
            case "RK-C323":
            case "RK-C324":
            case "RK-C325":
            case "RK-C326":
            case "RK-C327": {
                double R = calculateR12(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;

            case "RK-C328":
            case "RK-C329":
            case "RK-C330":
            case "RK-C331":
            case "RK-C332":
            case "RK-C333":
            case "RK-C334":
            case "RK-C335":
            case "RK-C336":
            case "RK-C337":
            case "RK-C338":
            case "RK-C339":
            case "RK-C340":
            case "RK-C341":
            case "RK-C342":
            case "RK-C343":
            case "RK-C344":
            case "RK-C345":
            case "RK-C346":
            case "RK-C347":
            case "RK-C348":
            case "RK-C349":
            case "RK-C350":
            case "RK-C351":
            case "RK-C352":
            case "RK-C353":
            case "RK-C354": {
                double R = calculateR8(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "RK-C357":
            case "RK-C358":
            case "RK-C359":
            case "RK-C360":
            case "RK-C361":
            case "RK-C362":
            case "RK-C363":
            case "RK-C364":
            case "RK-C365":
            case "RK-C366":
            case "RK-C367":
            case "RK-C368":
            case "RK-C369":
            case "RK-C370":
            case "RK-C371":
            case "RK-C372":
            case "RK-C373":
            case "RK-C374":
            case "RK-C375":
            case "RK-C376":
            case "RK-C377":
            case "RK-C378":
            case "RK-C379": {
                double R = calculateR8(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "RK-C386":
            case "RK-C387":
            case "RK-C388":
            case "RK-C389":
            case "RK-C390":
            case "RK-C391":
            case "RK-C392":
            case "RK-C393":
            case "RK-C394":
            case "RK-C395":
            case "RK-C396":
            case "RK-C397":
            case "RK-C398":
            case "RK-C399":
            case "RK-C400":
            case "RK-C401":
            case "RK-C402":
            case "RK-C403":
            case "RK-C404":
            case "RK-C405":
            case "RK-C406":
            case "RK-C407":
            case "RK-C408":
            case "RK-C409": {
                double R = calculateR8(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "RK-C412":
            case "RK-C413":
            case "RK-C414":
            case "RK-C415":
            case "RK-C416":
            case "RK-C417":
            case "RK-C418":
            case "RK-C419":
            case "RK-C420":
            case "RK-C421":
            case "RK-C422":
            case "RK-C423":
            case "RK-C424":
            case "RK-C425":
            case "RK-C426":
            case "RK-C427":
            case "RK-C428":
            case "RK-C429":
            case "RK-C430":
            case "RK-C431":
            case "RK-C432":
            case "RK-C433":
            case "RK-C434":
            case "RK-C435":
            case "RK-C436":
            case "RK-C437": {
                double R = calculateR8(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "RK-C438":
            case "RK-C439":
            case "RK-C440":
            case "RK-C441": {
                double R = calculateR8(paraValue);
                double a = -2.30134;
                double b = 2737.27;
                double c = 121842.04;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "RK-C442": {
                v5_1.set(paraValue * 5 / 255);
                ;
                calValue = v5_1.get();
            }
            break;
            case "RK-C443": {
                v5_2.set(paraValue * 5 / 255);
                calValue = v5_2.get();
            }
            break;
            /*F1推力器头部温度*/
            case "RK-C380": {
                double R = calculateR8(paraValue);
                double a = -1.55514;
                double b = 4198.9898;
                double c = -191780.6207;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            /*F2推力器头部温度*/
            case "RK-C381": {
                double R = calculateR8(paraValue);
                double a = -1.57640;
                double b = 4208.0506;
                double c = -192016.7885;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            /*F3推力器头部温度*/
            case "RK-C382": {
                double R = calculateR8(paraValue);
                double a = -1.65099;
                double b = 4208.0506;
                double c = -188884.1835;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            /*F4推力器头部温度*/
            case "RK-C383": {
                double R = calculateR8(paraValue);
                double a = -1.45753;
                double b = 4162.0182;
                double c = -182943.0829;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            /*F5推力器头部温度*/
            case "RK-C384": {
                double R = calculateR8(paraValue);
                double a = -1.42319;
                double b = 4116.4526;
                double c = -178823.3003;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            /*F5法兰盘*/
            case "RK-C385": {
                double R = calculateR8(paraValue);
                double a = -1.61219;
                double b = 4240.2913;
                double c = -191663.1638;
                calValue = calculateMF501(R, a, b, c);
            }
            break;

            case "XJ-C285":
            case "XJ-C287":
            case "XJ-C288":
            case "XJ-C289":
            case "XJ-C290":
            case "XJ-C326":
            case "XJ-C328":
            case "XJ-C329":
            case "XJ-C330":
            case "XJ-C331":
            case "XJ-C361":
            case "XJ-C363":
            case "XJ-C364":
            case "XJ-C365":
            case "XJ-C366":
            case "XJ-C396":
            case "XJ-C398":
            case "XJ-C400":
            case "XJ-C401":
            case "XJ-C402":
            case "XJ-C432":
            case "XJ-C434":
            case "XJ-C435":
            case "XJ-C436":
            case "XJ-C437":
            case "XJ-C467":
            case "XJ-C469":
            case "XJ-C470":
            case "XJ-C471":
            case "XJ-C472":
            case "XJ-C519":
            case "XJ-C521":
            case "XJ-C522":
            case "XJ-C523":
            case "XJ-C524":
            case "XJ-C554":
            case "XJ-C556":
            case "XJ-C557":
            case "XJ-C558":
            case "XJ-C559":
            case "XJ-C594":
            case "XJ-C596":
            case "XJ-C597":
            case "XJ-C598":
            case "XJ-C599":
            case "XJ-C629":
            case "XJ-C631":
            case "XJ-C632":
            case "XJ-C633":
            case "XJ-C634":
            case "XJ-C676":
            case "XJ-C678":
            case "XJ-C679":
            case "XJ-C680":
            case "XJ-C681":
            case "XJ-C711":
            case "XJ-C713":
            case "XJ-C714":
            case "XJ-C715":
            case "XJ-C716": {
                calValue = Math.pow(10, (0.0359 * paraValue + 5.76) / 20);
            }
            break;

            case "FL-C003": {
                calValue = 4050 / (4.98 + Math.log(7500 * paraValue / (256 - paraValue))) - 273;
            }
            break;
            case "ST-C004": {
                if (paraValue != 0)
                    this.sampleTime.set(paraValue / 1000000);
                calValue = paraValue / 1000000;
            }
            break;
            case "ST-C001":
            case "ST-C002":
            case "ST-C003": {
                calValue = paraValue / 450000 / this.sampleTime.get();
            }
            break;
            case "ST-C056":
            case "ST-C057":
            case "ST-C058":
            case "ST-C059": {
                calValue = tout(paraValue, 4300, 298.15, 12);
            }
            break;
            case "RK-C500":
            case "RK-C501":
            case "RK-C502":
            case "RK-C503":
            case "RK-C504":
            case "RK-C505":
            case "RK-C506":
            case "RK-C507":
            case "RK-C508":
            case "RK-C509":
            case "RK-C510":
            case "RK-C511":
            case "RK-C512":
            case "RK-C513":
            case "RK-C514":
            case "RK-C515":
            case "RK-C516":
            case "RK-C517":
            case "RK-C518":
            case "RK-C519":
            case "RK-C520":
            case "RK-C521":
            case "RK-C522":
            case "RK-C523":
            case "RK-C524":
            case "RK-C525":
            case "RK-C526":
            case "RK-C528":
            case "RK-C529":
            case "RK-C530":
            case "RK-C531":
            case "RK-C532":
            case "RK-C533":
            case "RK-C534":
            case "RK-C535":
            case "RK-C536":
            case "RK-C537":
            case "RK-C538":
            case "RK-C539":
            case "RK-C540":
            case "RK-C541":
            case "RK-C542":
            case "RK-C543":
            case "RK-C544":
            case "RK-C545":
            case "RK-C546":
            case "RK-C547":
            case "RK-C548":
            case "RK-C549":
            case "RK-C550":
            case "RK-C551":
            case "RK-C552":
            case "RK-C553":
            case "RK-C554":
            case "RK-C556":
            case "RK-C557":
            case "RK-C558":
            case "RK-C559":
            case "RK-C560":
            case "RK-C561":
            case "RK-C562":
            case "RK-C563":
            case "RK-C564":
            case "RK-C565":
            case "RK-C566":
            case "RK-C567":
            case "RK-C622":
            case "RK-C623":
            case "RK-C624":
            case "RK-C625":
            case "RK-C626":
            case "RK-C627":
            case "RK-C628":
            case "RK-C629": {
                double R = calculateR12(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "RK-C568":
            case "RK-C569":
            case "RK-C570":
            case "RK-C571":
            case "RK-C572":
            case "RK-C573":
            case "RK-C574":
            case "RK-C575":
            case "RK-C576":
            case "RK-C577":
            case "RK-C578":
            case "RK-C579":
            case "RK-C580":
            case "RK-C581":
            case "RK-C582":
            case "RK-C583":
            case "RK-C584":
            case "RK-C585":
            case "RK-C586":
            case "RK-C587":
            case "RK-C588":
            case "RK-C589":
            case "RK-C590":
            case "RK-C591":
            case "RK-C592":
            case "RK-C593":
            case "RK-C594":
            case "RK-C595":
            case "RK-C596":
            case "RK-C597":
            case "RK-C598":
            case "RK-C599":
            case "RK-C600":
            case "RK-C601":
            case "RK-C602":
            case "RK-C603":
            case "RK-C604":
            case "RK-C605":
            case "RK-C606":
            case "RK-C607":
            case "RK-C608":
            case "RK-C609":
            case "RK-C610":
            case "RK-C611":
            case "RK-C612":
            case "RK-C613":
            case "RK-C614":
            case "RK-C615":
            case "RK-C616":
            case "RK-C617":
            case "RK-C618":
            case "RK-C619":
            case "RK-C620":
            case "RK-C621":
            case "RK-C630":
            case "RK-C631":
            case "RK-C632":
            case "RK-C633":
            case "RK-C634":
            case "RK-C635":
            case "RK-C636":
            case "RK-C637":
            case "RK-C638":
            case "RK-C639":
            case "RK-C640":
            case "RK-C641":
            case "RK-C642":
            case "RK-C643":
            case "RK-C644":
            case "RK-C645":
            case "RK-C646":
            case "RK-C647":
            case "RK-C648":
            case "RK-C649":
            case "RK-C650":
            case "RK-C651":
            case "RK-C652":
            case "RK-C653":
            case "RK-C654":
            case "RK-C655":
            case "RK-C656":
            case "RK-C657":
            case "RK-C658":
            case "RK-C659":
            case "RK-C660":
            case "RK-C661":
            case "RK-C662":
            case "RK-C663":
            case "RK-C664":
            case "RK-C665":
            case "RK-C666":
            case "RK-C667":
            case "RK-C668":
            case "RK-C669":
            case "RK-C670":
            case "RK-C671":
            case "RK-C672":
            case "RK-C673":
            case "RK-C674":
            case "RK-C675": {
                double R = calculateR8(paraValue);
                double a = -6.01188;
                double b = 4622.533;
                double c = -86421.7;
                calValue = calculateMF501(R, a, b, c);
            }
            break;
            case "ZY-C172": {
                calValue = paraValue * 4 + 6800000;
            }
            break;
            case "ZY-C174": {
                calValue = paraValue / Math.pow(2, 16) + 1.5;
            }
            break;
            case "ZY-C182": {
                calValue = paraValue / Math.pow(2, 29) + 1.06e-3;
            }
            break;
            case "ZY-C304": {
                calValue = paraValue * 4 + 6.8e6;
            }
            break;
            case "ZY-C306": {
                calValue = paraValue / 0x10000 + 1.5;
            }
            break;
            case "ZY-C314": {
                calValue = paraValue / 0x20000000 + 1.06e-3;
            }
            break;
            case "WL-C001":
            case "WL-C090": {
                calValue = paraValue * 8 + 1;
            }
            break;
            case "WL-C008":
            case "WL-C097": {
                calValue = paraValue * 8 + 2;
            }
            break;
            case "WL-C015":
            case "WL-C104": {
                calValue = paraValue * 8 + 3;
            }
            break;
            case "WL-C022":
            case "WL-C111": {
                calValue = paraValue * 8 + 4;
            }
            break;
            case "WL-C029":
            case "WL-118": {
                calValue = paraValue * 8 + 5;
            }
            break;
            case "WL-C036":
            case "WL-C125": {
                calValue = paraValue * 8 + 6;
            }
            break;
            case "WL-C043":
            case "WL-C051":
            case "WL-C132":
            case "WL-C140": {
                calValue = paraValue * 8 + 7;
            }
            break;
            case "CA-C003":
            case "CA-C004":
            case "CA-C005":
            case "CA-C006":
            case "CA-C007":
            case "CA-C008":
            case "CA-C010":
            case "CA-C011":
            case "CA-C012":
            case "CA-C013":
            case "CA-C014":
            case "CA-C015":
            case "CA-C017":
            case "CA-C018":
            case "CA-C019":
            case "CA-C020":
            case "CA-C023":
            case "CA-C024":
            case "CA-C025":
            case "CA-C026":
            case "CA-C027":
            case "CA-C028":
            case "CA-C030":
            case "CA-C031":
            case "CA-C032":
            case "CA-C033":
            case "CA-C034":
            case "CA-C035":
            case "CA-C037":
            case "CA-C038":
            case "CA-C039":
            case "CA-C040":
            case "CA-C103":
            case "CA-C104":
            case "CA-C105":
            case "CA-C106":
            case "CA-C107":
            case "CA-C108":
            case "CA-C110":
            case "CA-C111":
            case "CA-C112":
            case "CA-C113":
            case "CA-C114":
            case "CA-C115":
            case "CA-C117":
            case "CA-C118":
            case "CA-C119":
            case "CA-C120":
            case "CA-C123":
            case "CA-C124":
            case "CA-C125":
            case "CA-C126":
            case "CA-C127":
            case "CA-C128":
            case "CA-C130":
            case "CA-C131":
            case "CA-C132":
            case "CA-C133":
            case "CA-C134":
            case "CA-C135":
            case "CA-C137":
            case "CA-C138":
            case "CA-C139":
            case "CA-C140":
            case "CA-C203":
            case "CA-C204":
            case "CA-C205":
            case "CA-C206":
            case "CA-C207":
            case "CA-C208":
            case "CA-C210":
            case "CA-C211":
            case "CA-C212":
            case "CA-C213":
            case "CA-C214":
            case "CA-C215":
            case "CA-C217":
            case "CA-C218":
            case "CA-C219":
            case "CA-C220":
            case "CA-C223":
            case "CA-C224":
            case "CA-C225":
            case "CA-C226":
            case "CA-C227":
            case "CA-C228":
            case "CA-C230":
            case "CA-C231":
            case "CA-C232":
            case "CA-C233":
            case "CA-C234":
            case "CA-C235":
            case "CA-C237":
            case "CA-C238":
            case "CA-C239":
            case "CA-C240":
            case "CA-C303":
            case "CA-C304":
            case "CA-C305":
            case "CA-C306":
            case "CA-C307":
            case "CA-C308":
            case "CA-C310":
            case "CA-C311":
            case "CA-C312":
            case "CA-C313":
            case "CA-C314":
            case "CA-C315":
            case "CA-C317":
            case "CA-C318":
            case "CA-C319":
            case "CA-C320":
            case "CA-C323":
            case "CA-C324":
            case "CA-C325":
            case "CA-C326":
            case "CA-C327":
            case "CA-C328":
            case "CA-C330":
            case "CA-C331":
            case "CA-C332":
            case "CA-C333":
            case "CA-C334":
            case "CA-C335":
            case "CA-C337":
            case "CA-C338":
            case "CA-C339":
            case "CA-C340":
            case "CA-C403":
            case "CA-C404":
            case "CA-C405":
            case "CA-C406":
            case "CA-C407":
            case "CA-C408":
            case "CA-C410":
            case "CA-C411":
            case "CA-C412":
            case "CA-C413":
            case "CA-C414":
            case "CA-C415":
            case "CA-C417":
            case "CA-C418":
            case "CA-C419":
            case "CA-C420":
            case "CA-C423":
            case "CA-C424":
            case "CA-C425":
            case "CA-C426":
            case "CA-C427":
            case "CA-C428":
            case "CA-C430":
            case "CA-C431":
            case "CA-C432":
            case "CA-C433":
            case "CA-C434":
            case "CA-C435":
            case "CA-C437":
            case "CA-C438":
            case "CA-C439":
            case "CA-C440":
            case "CA-C503":
            case "CA-C504":
            case "CA-C505":
            case "CA-C506":
            case "CA-C507":
            case "CA-C508":
            case "CA-C510":
            case "CA-C511":
            case "CA-C512":
            case "CA-C513":
            case "CA-C514":
            case "CA-C515":
            case "CA-C517":
            case "CA-C518":
            case "CA-C519":
            case "CA-C520":
            case "CA-C523":
            case "CA-C524":
            case "CA-C525":
            case "CA-C526":
            case "CA-C527":
            case "CA-C528":
            case "CA-C530":
            case "CA-C531":
            case "CA-C532":
            case "CA-C533":
            case "CA-C534":
            case "CA-C535":
            case "CA-C537":
            case "CA-C538":
            case "CA-C539":
            case "CA-C540": {
                calValue = Math.pow(10, (paraValue * 0.0359 + 5.76) / 20);
            }
            break;
            case "ZN-C008": {
                calValue = paraValue + 2017;
            }
            break;
            case "ZN-C209":
            case "ZN-C210":
            case "ZN-C211":
            case "ZN-C212": {
                calValue = (paraValue * 5 / 255 + 0.22) * 12;
            }
            break;
        }

        return calValue;
    }

    @Override
    public Map<String, Double> getInitRealMap() {
        Map<String, Double> realMap = new HashMap<>();
        realMap.put("ZT-C150", -10000.0);
        realMap.put("ZT-C020", -10000.0);
        realMap.put("ZT-C021", -10000.0);
        realMap.put("ZT-C120", -10000.0);
        realMap.put("ZT-C130", -10000.0);
        realMap.put("RK-C091", -10000.0);
        realMap.put("RK-C092", -10000.0);
        realMap.put("RK-C093", -10000.0);
        realMap.put("RK-C094", -10000.0);
        realMap.put("RK-C095", -10000.0);
        realMap.put("RK-C096", -10000.0);
        realMap.put("ZK-C001", -10000.0);
        realMap.put("ZK-C032", -10000.0);
        realMap.put("ZK-C057", -10000.0);
        realMap.put("ZG-C137", -10000.0);
        realMap.put("ZG-C156", -10000.0);
        realMap.put("ZG-C175", -10000.0);
        realMap.put("ZT-C075", -10000.0);
        realMap.put("ZT-C138", -10000.0);
        realMap.put("ZT-C139", -10000.0);
        realMap.put("ZT-C140", -10000.0);
        realMap.put("ZT-C141", -10000.0);
        realMap.put("ZT-C142", -10000.0);
        realMap.put("ZT-C143", -10000.0);
        realMap.put("ZT-C144", -10000.0);
        realMap.put("ZT-C145", -10000.0);
        realMap.put("ZT-C146", -10000.0);
        realMap.put("ZT-C147", -10000.0);
        realMap.put("ZT-C148", -10000.0);
        realMap.put("ZT-C149", -10000.0);
        realMap.put("ZG-C042", -10000.0);
        realMap.put("ZG-C043", -10000.0);
        realMap.put("RK-C216", -10000.0);
        realMap.put("ZG-C039", -10000.0);
        realMap.put("RK-C243", -10000.0);
        realMap.put("FL-C027", -10000.0);

        return realMap;
    }


    private double dy(double V, double K, double B) {
        return V * 5 / 4095 * K + B;
    }

    private double calculateMF501(double R, double a, double b, double c) {
        double t = 2 * c / (-b + Math.sqrt(b * b - 4 * c * (a - Math.log(R)))) - 273.15;
        return t;
    }

    private double calculateT(double R, double a, double b, double c) {
        double t = 2 * c / (-b + Math.sqrt(b * b - 4 * c * (a - Math.log(1000 * R)))) - 273.15;
        return t;
    }

    private double dy255(double V, double K, double B) {
        return V * 5 / 255 * K + B;
    }

    private double calculateR12(double paraValue) {
        double R = paraValue * 10 / 4095 * 10000 / (v5.get() - paraValue * 10 / 4095);
        return R;
    }

    private double calculateR8(double paraValue) {
        double R = paraValue * 5 / 255 * 10000 / (v5.get() - paraValue * 5 / 255);
        return R;
    }

    private double calculateR(double paraValue) {
        double R = paraValue * 37.5 / (1280 - 12.5 / paraValue);
        return R;
    }

    private double tout(double V, double B, double T0, double R0) {
        double RT = V * 20 / (4096 - V);
        double T = 1 / (Math.log(RT / R0) / B + 1 / T0);
        return T - 273.15;
    }

    @Override
    public String getDisplayValue(String paraCode, Double paraValue, int frameFlag, SatelliteTimeManager satelliteTimeManager) {
        switch (paraCode) {
            case "ZT-C006": {
                if (frameFlag == 0) {
                    satelliteTimeManager.setRestartTime(paraValue);
                }
                if (frameFlag == 8) {
                    satelliteTimeManager.setResRestartDelayTime(paraValue);
                }
            }
            break;
            case "ZT-C003": {

                if (frameFlag == 0) {
                    satelliteTimeManager.setSatelliteTime(paraValue);
                    return satelliteTimeManager.getSatelliteTimeStr();
                }
                if (frameFlag == 8) {
                    satelliteTimeManager.setResRestartDelayTime(paraValue);
                    return satelliteTimeManager.getSatelliteTimeDelayStr();
                }

            }
            break;
        }
        return super.getDisplayValue(paraCode, paraValue, frameFlag, satelliteTimeManager);
    }


}
