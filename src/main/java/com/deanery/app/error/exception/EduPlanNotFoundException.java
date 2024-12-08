package com.deanery.app.error.exception;

import java.util.UUID;

public class EduPlanNotFoundException extends RuntimeException {
    public EduPlanNotFoundException(Integer num) {
        super(String.format("Education plan with num [%s] is not found", num));
    }

}
