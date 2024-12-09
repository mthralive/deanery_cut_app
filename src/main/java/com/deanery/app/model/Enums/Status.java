package com.deanery.app.model.Enums;

public enum Status {
    ACTIVE,
    INWORK,
    INACTIVE;

    public static final class AsString {
        public static final String ACTIVE = "ACTIVE";
        public static final String INWORK = "INWORK";
        public static final String INACTIVE = "INACTIVE";
    }

    @Override
    public String toString() {
        switch (this) {
            case ACTIVE:
                return Status.AsString.ACTIVE;
            case INACTIVE:
                return Status.AsString.INACTIVE;
            case INWORK:
                return Status.AsString.INWORK;
            default:
                throw new IllegalArgumentException("Unknown EducationForms: " + this);
        }
    }
}
