package com.deanery.app.model.Enums;

public enum RequestStatus {
    SENDED,
    CONSIDERATION,
    IN_WORK,
    COMPLETED,
    CANCELED;

    public static final class AsString {
        public static final String SENDED = "Отправлена";
        public static final String CONSIDERATION = "CONSIDERATION";
        public static final String IN_WORK = "В работе";
        public static final String COMPLETED = "Выполнена";
        public static final String CANCELED = "Отменена";
    }

    @Override
    public String toString() {
        switch (this) {
            case SENDED:
                return RequestStatus.AsString.SENDED;
            case IN_WORK:
                return RequestStatus.AsString.IN_WORK;
            case COMPLETED:
                return RequestStatus.AsString.COMPLETED;
            case CANCELED:
                return RequestStatus.AsString.CANCELED;
            default:
                throw new IllegalArgumentException("Unknown EducationQual: " + this);
        }
    }
}
