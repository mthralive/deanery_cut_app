package com.deanery.app.model.Enums;

public enum EducationType {
    BACALAVR("б"),
    MAGISTR("м"),
    ASPIR("а");

    private final String shortType;

    EducationType(String shortType) {
        this.shortType = shortType;
    }

    public String toShortType() {
        return shortType;
    }

    public static final class AsString {
        public static final String BACALAVR = "Бакалавриат";
        public static final String MAGISTR = "Магистратура";
        public static final String ASPIR = "Аспирантура";
    }

    @Override
    public String toString() {
        switch (this) {
            case BACALAVR:
                return AsString.BACALAVR;
            case MAGISTR:
                return AsString.MAGISTR;
            case ASPIR:
                return AsString.ASPIR;
            default:
                throw new IllegalArgumentException("Unknown EducationType: " + this);
        }
    }
}
