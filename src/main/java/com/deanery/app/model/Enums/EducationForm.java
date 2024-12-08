package com.deanery.app.model.Enums;

import lombok.Getter;

public enum EducationForm {
    EDU_IN("д"),
    EDU_OUT("з"),
    EDU_OUT_IN("в");

    private final String shortType;

    EducationForm(String shortType) {
        this.shortType = shortType;
    }

    public String toShortType() {
        return shortType;
    }

    public static final class AsString {
        public static final String EDU_IN = "Очно";
        public static final String EDU_OUT = "Заочно";
        public static final String EDU_OUT_IN = "Очно-заочно";
    }

    @Override
    public String toString() {
        switch (this) {
            case EDU_IN:
                return EducationForm.AsString.EDU_IN;
            case EDU_OUT:
                return EducationForm.AsString.EDU_OUT;
            case EDU_OUT_IN:
                return EducationForm.AsString.EDU_OUT_IN;
            default:
                throw new IllegalArgumentException("Unknown EducationForms: " + this);
        }
    }
}
