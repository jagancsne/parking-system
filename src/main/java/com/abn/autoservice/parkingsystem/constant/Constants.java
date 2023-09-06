package com.abn.autoservice.parkingsystem.constant;

public class Constants {
    public static final String INVALID_INPUT_CODE = "4550";
    public static final String NOT_FOUND_CODE = "4004";
    public static final String SYSTEM_ERROR_CODE = "5000";
    public static final String INVALID_REQUEST = "INVALID REQUEST";
    public static final String NOT_FOUND = "NOT FOUND";
    public static final String SYSTEM_ERROR = "SYSTEM ERROR";
    public static final String LICENSE_PLATE_NO_REGEX = "^[\\dA-Z]{1,2}-[\\dA-Z]{2,3}-[\\dA-Z]{2}$";
    public static final String HUMAN_NAME_REGEX = "^[A-Za-z]+([A-Za-z ,'.]+)$";
    public static final String STREET_NAME_REGEX = "Java|Jakarta|Spring|Azure";
    public static final String MONITOR_SCHEDULER_CRON_EX = "0 0/10 8-21 ? * MON-SAT";
    public static final String REPORTING_SCHEDULER_CRON_EX = "0 0 22 ? * MON-SAT";

}
