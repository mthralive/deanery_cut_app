package com.deanery.app.service;

import com.deanery.app.dto.EducationPlanDto;
import com.deanery.app.error.exception.EduPlanNotFoundException;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.error.exception.WorkPlanException;
import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.WorkPlan;
import com.deanery.app.repository.EducationPlanRepository;
import com.deanery.app.repository.WorkPlanRepository;
import groovy.util.logging.Slf4j;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationPlanService {
    private final EducationPlanRepository educationPlanRepository;
    private final WorkPlanRepository workPlanRepository;

    @Transactional(readOnly = true)
    public EducationPlan findEducationPlan(UUID id) {
        final Optional<EducationPlan> base = educationPlanRepository.findById(id);
        return base.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public EducationPlan findEducationPlanByNum(Integer num) {
        final Optional<EducationPlan> base = Optional.ofNullable(educationPlanRepository.findByNum(num));
        return base.orElseThrow(() -> new EduPlanNotFoundException(num));
    }

    @Transactional(readOnly = true)
    public List<EducationPlan> findAllEduPlansList() {
        return educationPlanRepository.findAll();
    }

    @Transactional
    public EducationPlan create(EducationPlanDto educationPlanDto) {
        final EducationPlan eduPlan = educationPlanRepository.findByNum(educationPlanDto.hashCode());

        if(eduPlan != null) {
            throw new ValidationException(String.format("Education with num '%s' already exists", educationPlanDto.hashCode()));
        }

        final EducationPlan newEduPlan = new EducationPlan(null, educationPlanDto.hashCode(), LocalDate.now(), educationPlanDto.getYear_from(), educationPlanDto.getYear_to(), educationPlanDto.getYear_to() - educationPlanDto.getYear_from(), educationPlanDto.getFullName(), getShortName(educationPlanDto.getFullName()), educationPlanDto.getEdu_type(), educationPlanDto.getEdu_form(), educationPlanDto.getEdu_qual());
        return educationPlanRepository.save(newEduPlan);
    }

    @Transactional
    public EducationPlan update(UUID id,EducationPlanDto educationPlanDto) {
        final EducationPlan eduPlan = findEducationPlan(id);
        eduPlan.setEdu_years(educationPlanDto.getYear_to() - educationPlanDto.getYear_from());
        eduPlan.setFullName(educationPlanDto.getFullName());
        eduPlan.setShortName(getShortName(educationPlanDto.getFullName()));
        eduPlan.setEdu_type(educationPlanDto.getEdu_type());
        eduPlan.setEdu_form(educationPlanDto.getEdu_form());
        eduPlan.setEdu_qual(educationPlanDto.getEdu_qual());
        eduPlan.setYear_to(educationPlanDto.getYear_to());
        eduPlan.setYear_from(educationPlanDto.getYear_from());
        return educationPlanRepository.save(eduPlan);
    }

    @Transactional
    public EducationPlan delete(UUID id) {
        final EducationPlan eduPlan = findEducationPlan(id);
        educationPlanRepository.deleteById(id);
        return eduPlan;
    }

    @Transactional(readOnly = true)
    public List<WorkPlan> findWorkPlans(UUID id) {

        final Optional<List<WorkPlan>> base = Optional.ofNullable(workPlanRepository.findByEducationPlan_Id(id));
        return base.orElseThrow(() -> new WorkPlanException(id));
    }

    @Transactional
    public EducationPlan createWorkPlans(UUID id){
        final EducationPlan eduPlan = findEducationPlan(id);
        List<WorkPlan> wps = workPlanRepository.findByEducationPlan_Id(id);
        if(!wps.isEmpty()){
            throw new ValidationException("Рабочие планы уже созданы");
        }
        for(int i = 0; i < eduPlan.getEdu_years(); i++){
            WorkPlan wp = new WorkPlan(null,
                    eduPlan.getShortName()+eduPlan.getEdu_type().toShortType()+eduPlan.getEdu_form().toShortType(),
                    i+1,
                    eduPlan);
            workPlanRepository.save(wp);
        }
        return eduPlan;
    }

    public String getShortName(String name){
        String[] words = name.split(" ");

        // Создаем StringBuilder для хранения результата
        StringBuilder firstLetters = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) { // Проверяем, что слово не пустое
                firstLetters.append(word.charAt(0)); // Берем первый символ слова
            }
        }
        return firstLetters.toString().toUpperCase();
    }
}