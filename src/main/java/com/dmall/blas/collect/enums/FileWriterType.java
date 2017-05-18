package com.dmall.blas.collect.enums;


public enum FileWriterType {
    EVTS_RAW_PARSER(1, "evt_data"),
    EVTS_RAW_RECEIVER(2, "evt_raw_data"),
    EVT_DATA_REBUILD_FROM_EVT_RAW_DATA(3, "evt_data_from_evt_raw_data"),
    //	OXEYE_2_EVT_DATA(4, "evt_data_from_hive_history"),
    OXEYE_2_EVT_RAW_DATA(5, "evt_raw_data_from_hive_history");

    private int code;
    private String fileNamePrefix;

    private FileWriterType(int code, String fileNamePrefix) {
        this.code = code;
        this.fileNamePrefix = fileNamePrefix;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public void setFileNamePrefix(String fileNamePrefix) {
        this.fileNamePrefix = fileNamePrefix;
    }

}
