package com.deanery.app.model.Enums;

public enum RequestStatus {
    SENDED,
    CONSIDERATION,
    IN_WORK,
    COMPLETED,
    CANCELED;

    public static final class AsString {
        public static final String SENDED = "SENDED";
        public static final String CONSIDERATION = "CONSIDERATION";
        public static final String IN_WORK = "IN_WORK";
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELED = "CANCELED";
    }
}
