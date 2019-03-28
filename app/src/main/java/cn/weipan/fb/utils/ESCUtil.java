package cn.weipan.fb.utils;

import java.io.UnsupportedEncodingException;

public class ESCUtil {

    public static final byte ESC = 27;// 换码
    public static final byte FS = 28;// 文本分隔符
    public static final byte GS = 29;// 组分隔符
    public static final byte DLE = 16;// 数据连接换码
    public static final byte EOT = 4;// 传输结束
    public static final byte ENQ = 5;// 询问字符
    public static final byte SP = 32;// 空格
    public static final byte HT = 9;// 横向列表
    public static final byte LF = 10;// 打印并换行（水平定位）
    public static final byte CR = 13;// 归位键
    public static final byte FF = 12;// 走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;// 作废（页模式下取消打印数据 ）

    // ------------------------打印机初始化-----------------------------

    /**
     * 打印机初始化
     *
     * @return
     */
    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
//        result[1] = 48;
        result[1] = 64;
        return result;
    }

    // ------------------------换行-----------------------------

    /**
     * 换行
     *
     * @return
     * @paramlineNum要换几行
     */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    // ------------------------下划线-----------------------------

    /**
     * 绘制下划线（1点宽）
     *
     * @return
     */
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    /**
     * 绘制下划线（2点宽）
     *
     * @return
     */
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    /**
     * 取消绘制下划线
     *
     * @return
     */
    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }

    // ------------------------加粗-----------------------------

    /**
     * 选择加粗模式
     *
     * @return
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消加粗模式
     *
     * @return
     */
    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    // ------------------------对齐-----------------------------

    /**
     * 左对齐
     *
     * @return
     */
    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }

    /**
     * 居中对齐
     *
     * @return
     */
    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    /**
     * 右对齐
     *
     * @return
     */
    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }

    /**
     * 水平方向向右移动col列
     *
     * @param col
     * @return
     */
    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }
    // ------------------------字体变大-----------------------------

    /**
     * 字体变大为标准的n倍
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetBig(int num) {
        byte realSize = 0;
        switch (num) {
            case 1:
                realSize = 0;
                break;
            case 2:
                realSize = 17;
                break;
            case 3:
                realSize = 34;
                break;
            case 4:
                realSize = 51;
                break;
            case 5:
                realSize = 68;
                break;
            case 6:
                realSize = 85;
                break;
            case 7:
                realSize = 102;
                break;
            case 8:
                realSize = 119;
                break;
        }
        byte[] result = new byte[3];
        result[0] = 29;
        result[1] = 33;
        result[2] = realSize;
//		result[0] = 26;
//		result[1] = 30;
//		result[2] = realSize;
        return result;
    }

    // ------------------------字体变小-----------------------------

    /**
     * 字体取消倍宽倍高
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetSmall(int num) {
        byte[] result = new byte[3];
//        result[0] = ESC;
//        result[1] = 30;
        result[0] = 24;
        result[1] = 27;
        return result;
    }

    // ------------------------切纸-----------------------------

    /**
     * 进纸并全部切割
     *
     * @return
     */
    public static byte[] feedPaperCutAll() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        return result;
    }

    /**
     * 进纸并切割（左边留一点不切）
     *
     * @return
     */
    public static byte[] feedPaperCutPartial() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        return result;
    }

    // ------------------------切纸-----------------------------
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] byteMerger(byte[][] byteList) {

        int length = 0;
        for (int i = 0; i < byteList.length; i++) {
            length += byteList[i].length;
        }
        byte[] result = new byte[length];

        int index = 0;
        for (int i = 0; i < byteList.length; i++) {
            byte[] nowByte = byteList[i];
            for (int k = 0; k < byteList[i].length; k++) {
                result[index] = nowByte[k];
                index++;
            }
        }
        for (int i = 0; i < index; i++) {
            // CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
        }
        return result;
    }

    // --------------------
    public static byte[] generateMockData(String RealName, String casherName, String payNumber, String time, String payType, String payMoney, boolean isSign) {

        byte[] bigSize = new byte[3];
        bigSize[0] = 29;
        bigSize[1] = 33;
        bigSize[2] = 0;

        byte[] smallSize = new byte[3];
        smallSize[0] = 27;
        smallSize[1] = 33;
        smallSize[2] = 0;

        byte[] NORMAL = {0x1d, 0x21, 0x00};

        try {
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] next4Line = ESCUtil.nextLine(4);

//			字体变大为标准的n倍
            byte[] fontSize0Big = ESCUtil.fontSizeSetBig(1);
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
//			进纸并切割（左边留一点不切）
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();
//			加粗
            byte[] boldOn = ESCUtil.boldOn();
            //			取消加粗
            byte[] boldOff = ESCUtil.boldOff();
//			中心
            byte[] center = ESCUtil.alignCenter();
//			左对齐
            byte[] left = ESCUtil.alignLeft();
//			右对齐
            byte[] right = ESCUtil.alignRight();

            byte[] title = "云支付收银小票".getBytes("gb2312");
            byte[] Focus = "商户存根: MERCHANT COPY".getBytes("gb2312");
            byte[] orderSerinum = ("商户名称：" + RealName).getBytes("gb2312");
            byte[] FocusOrderContent = ("收银员号：" + casherName).getBytes("gb2312");
            byte[] priceInfo = ("支付单号：" + payNumber).getBytes("gb2312");
            byte[] takeTime = ("交易时间: " + time).getBytes("gb2312");
            byte[] setOrderTime = ("交易详情：" + payType).getBytes("gb2312");
            byte[] money = ("金额：RMB " + payMoney).getBytes("gb2312");

//            byte[] tips_1 = "交易状态：收款成功".getBytes("gb2312");

            byte[] tips_2 = "备注（REFERENCE）".getBytes("gb2312");
            byte[] tips_3 = "技术支持：杭州微盘信息技术有限公司".getBytes("gb2312");
            byte[] tips_4 = "免费热线：400-8321-606".getBytes("gb2312");
            byte[] tips_5 = "持卡人签名：（CARDHOLDER SIGNATURE）".getBytes("gb2312");
            byte[] tips_6 = "本人确认以上交易，对交易无任何纠纷意见".getBytes("gb2312");
            byte[] tips_7 = "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICE".getBytes("gb2312");
            byte[] tips_8 = "----------------------------".getBytes("gb2312");

            if (isSign) {
                byte[][] cmdBytes = {title, center, next2Line, bigSize,
                        Focus, left, nextLine, smallSize,
                        orderSerinum, left, nextLine, smallSize,
                        FocusOrderContent, nextLine,
                        priceInfo, nextLine,
                        money, nextLine,
                        takeTime, nextLine,
                        setOrderTime, next2Line,
                        tips_2, nextLine, left, smallSize,
                        tips_3, nextLine, left, smallSize,
                        tips_4, nextLine, left, smallSize,
                        tips_5, next4Line, left, smallSize,
                        tips_6, nextLine, left, smallSize,
                        tips_7, next2Line, left, smallSize,
                        tips_8, next2Line, center, smallSize, breakPartial};
                return ESCUtil.byteMerger(cmdBytes);

            } else {
                byte[][] cmdBytes = {title, center, next2Line, bigSize,
                        Focus, left, nextLine, smallSize,
                        orderSerinum, left, nextLine, smallSize,
                        FocusOrderContent, nextLine,
                        priceInfo, nextLine,
                        money, nextLine,
                        takeTime, nextLine,
                        setOrderTime, next2Line,
                        tips_2, nextLine, left, smallSize,
                        tips_3, nextLine, left, smallSize,
                        tips_4, next4Line, breakPartial};
                return ESCUtil.byteMerger(cmdBytes);
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}