package com.deanery.app.model.Enums;

public enum WorkPlanStatus {
    STUDY,
    ACADEM,
    EXPELLED,
    ENDED,
    TRANSFERRED;


    public static final class AsString {
        public static final String STUDY = "Обучается";
        public static final String ACADEM = "Академ";
        public static final String EXPELLED = "Отчислен";
        public static final String ENDED = "Закончил";
        public static final String TRANSFERRED = "Переведен на курс старше";
    }
}
