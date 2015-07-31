package com.metis.base.framework;

/**
 * Created by Beak on 2015/7/3.
 */
public class NetProperty {

    private static final boolean TESTING = true;

    public static final String FORMAL = "https://metisapi.azure-mobile.cn";
    public static final String FORMAL_KEY = "JhSUSARkPDywIlrCKJKQzOJIttIYWU24";
    public static final String TEST = "https://mobiletest.azure-mobile.cn/";
    public static final String TEST_KEY = "TsBWBGVGZmkrUYGvBArWkmJrhHSsnr52";

    public static final String USE = TESTING ? TEST : FORMAL;
    public static final String USE_KEY = TESTING ? TEST_KEY : FORMAL_KEY;
}
