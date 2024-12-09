package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.IndividualWorkPlan;
import com.deanery.app.model.WorkPlan;
import com.deanery.app.service.EducationPlanService;
import com.deanery.app.service.IndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/education")
@Slf4j
public class EducationPlanRestController {
    private final EducationPlanService educationPlanService;
    private final IndividualService individualService;

    @GetMapping("/eduPlan/{id}")
    @ActiveUserCheck
    public String EduPlans(@PathVariable UUID id, @RequestParam(value ="search", required = false) String search) {

        List<EducationPlan> ep = educationPlanService.findEduPlansActive(search);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ep.size(); i++) {
            sb.append(
                    "<option value="
                            + ep.get(i).getId()
                            + ">" + ep.get(i).getNum()
                            + " | " + ep.get(i).getFullName()
                            + " | " + ep.get(i).getEdu_form()
                            + " | " + ep.get(i).getYear_to()
                            + "</option>");
        }
        return sb.toString();
    }
    @GetMapping("/workPlan/{id}")
    @ActiveUserCheck
    public String WorkPlans(@PathVariable UUID id) {
        List<WorkPlan> wp = educationPlanService.findWorkPlans(id);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wp.size(); i++) {
            sb.append(
                    "<option value="
                            + wp.get(i).getId()
                            + ">"+ wp.get(i).getName()
                            + "-" + wp.get(i).getCourse().toString()
                            + "</option>");
        }
        return sb.toString();
    }
}
