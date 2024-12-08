package com.deanery.app.model.Enums;

public enum EducationQual {
    HIGH,
    MID;

    public static final class AsString {
        public static final String HIGH = "Высшее";
        public static final String MID = "Среднее-профессиональное";
    }

    @Override
    public String toString() {
        switch (this) {
            case HIGH:
                return EducationQual.AsString.HIGH;
            case MID:
                return EducationQual.AsString.MID;
            default:
                throw new IllegalArgumentException("Unknown EducationQual: " + this);
        }
    }
}
