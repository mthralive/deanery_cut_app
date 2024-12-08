package com.deanery.app.error.exception;

import java.util.UUID;

public class WorkPlanException extends RuntimeException {
    public WorkPlanException(UUID id) {
        super(String.format("Work plan for Education plan [%s] is already created", id));
    }

}
