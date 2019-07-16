package com.infy.esurio.esuriit.data.model.payTM;

public final class BuildConfig {
    public static final boolean DEBUG = Boolean.parseBoolean("true");
    public static final String APPLICATION_ID = "info.androidhive.paytmgateway";
    public static final String BUILD_TYPE = "debug";
    public static final String FLAVOR = "dev";
    public static final int VERSION_CODE = 1;
    public static final String VERSION_NAME = "1.0";
    // Fields from product flavor: dev
    public static final String BASE_URL = "https://demo.androidhive.info/paytm/public/api/";
    public static final boolean IS_PATM_STAGIN = true;
    public static final String PAYTM_CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=%s";
}
