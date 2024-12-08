package com.deanery.app.model.Enums;

public enum EducationPayType {
    PAYED,
    FREE,
    WORKED;

    public static final class AsString {
        public static final String PAYED = "Платное";
        public static final String FREE = "Бюджетное";
        public static final String WORKED = "Целевое";
    }

    @Override
    public String toString() {
        switch (this) {
            case PAYED:
                return EducationPayType.AsString.PAYED;
            case FREE:
                return EducationPayType.AsString.FREE;
            case WORKED:
                return EducationPayType.AsString.WORKED;
            default:
                throw new IllegalArgumentException("Unknown EducationPayType: " + this);
        }
    }
}
