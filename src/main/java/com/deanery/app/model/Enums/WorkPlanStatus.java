package com.deanery.app.model.Enums;

public enum WorkPlanStatus {
    DONTNO,
    INWORK,
    STUDY,
    ACADEM,
    EXPELLED,
    ENDED,
    TRANSFERRED,
    TRANSFERREDOUT,
    AGAIN;


    public static final class AsString {
        public static final String DONTNO = "ошибка";
        public static final String INWORK = "Зачислен";
        public static final String STUDY = "Обучается";
        public static final String ACADEM = "Академ";
        public static final String EXPELLED = "Отчислен";
        public static final String ENDED = "Закончил";
        public static final String TRANSFERRED = "Переведен на курс старше";
        public static final String TRANSFERREDOUT = "Переведен в другое учебное заведение";
        public static final String AGAIN = "ошибка";
    }
}
