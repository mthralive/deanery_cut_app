package com.deanery.app.service;

import com.deanery.app.dto.IndividualDto;
import com.deanery.app.dto.IndividualPlanDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.model.*;
import com.deanery.app.model.Enums.Status;
import com.deanery.app.model.Enums.WorkPlanStatus;
import com.deanery.app.repository.*;
import groovy.util.logging.Slf4j;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualService {
    private final IndividualRepository individualRepository;
    private final IndividualWorkPlanRepository individualWorkPlanRepository;
    private final WorkPlanRepository workPlanRepository;
    private final LogInfoRepository logInfoRepository;
    private final EducationPlanRepository educationPlanRepository;

    @Transactional
    public Individual findBySnils(String snils) {
        return individualRepository.findBySnils(snils);
    }

    @Transactional
    public Individual create(IndividualDto individualDto) {
        final Individual individual = findBySnils(individualDto.getSnils());

        if(individual != null) {
            throw new ValidationException(String.format("Individual with snils num '%s' already exists", individualDto.getSnils()));
        }

        final Individual newIndividual =
                new Individual(null ,
                        individualDto.getIndividual_code(),
                        individualDto.getFirst_name(),
                        individualDto.getSecond_name(),
                        individualDto.getPatronymic(),
                        individualDto.getBirthday(),
                        individualDto.getGender(),
                        individualDto.getBirthplace(),
                        individualDto.getSnils(),
                        individualDto.getInn(),
                        individualDto.getRegistration(),
                        individualDto.getActualAddress(),
                        individualDto.getEmail());
        return individualRepository.save(newIndividual);
    }

    @Transactional(readOnly = true)
    public Individual findIndividual(UUID id) {
        final Optional<Individual> base = individualRepository.findById(id);
        return base.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public Individual updateIndividual(UUID id, IndividualDto individualDto) {
        final Individual currentIndividual = findIndividual(id);
        currentIndividual.setFirst_name(individualDto.getFirst_name());
        currentIndividual.setSecond_name(individualDto.getSecond_name());
        currentIndividual.setGender(individualDto.getGender());
        currentIndividual.setBirthday(individualDto.getBirthday());
        currentIndividual.setPatronymic(individualDto.getPatronymic());
        currentIndividual.setBirthplace(individualDto.getBirthplace());
        currentIndividual.setInn(individualDto.getInn());
        currentIndividual.setRegistration(individualDto.getRegistration());
        currentIndividual.setActualAddress(individualDto.getActualAddress());
        currentIndividual.setEmail(individualDto.getEmail());
        currentIndividual.setSnils(individualDto.getSnils());
        return individualRepository.save(currentIndividual);
    }

    @Transactional
    public Individual deleteIndividual(UUID id) {
        final Individual individual = findIndividual(id);
        individualRepository.deleteById(id);
        return individual;
    }

    @Transactional
    public IndividualWorkPlan findBind(Individual ind, EducationPlan ep){
        return individualWorkPlanRepository.findAllWP(ind.getId(), ep.getId());
    }

    @Transactional
    public IndividualWorkPlan createBind(IndividualPlanDto individualPlanDto){
        final IndividualWorkPlan individual = findBind(individualPlanDto.getIndividual(), individualPlanDto.getWorkPlan().getEducationPlan());

        if(individual != null) {
            throw new ValidationException("Связь с учебным планом уже существует");
        }

        final IndividualWorkPlan newIndividual =
                new IndividualWorkPlan(null ,
                        individualPlanDto.getIndividual(), individualPlanDto.getWorkPlan(), WorkPlanStatus.STUDY);
        return individualWorkPlanRepository.save(newIndividual);
    }

    @Transactional(readOnly = true)
    public IndividualWorkPlan findBind(UUID id) {
        final Optional<IndividualWorkPlan> base = individualWorkPlanRepository.findById(id);
        return base.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public IndividualWorkPlan findBindByIndId(UUID id) {
        final Optional<IndividualWorkPlan> base = individualWorkPlanRepository.findById(id);
        return base.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public IndividualWorkPlan updateStatusBind(UUID id, WorkPlanStatus stat){
        final IndividualWorkPlan iwp = findBind(id);
        iwp.setStatus(stat);
        return individualWorkPlanRepository.save(iwp);
    }

    @Transactional
    public List<IndividualWorkPlan> setNextCourse(UUID id, User user, EducationPlan ep){
        List<IndividualWorkPlan> iwp = individualWorkPlanRepository.findAllByWorkPlan_EducationPlan_Id(id);
        List<WorkPlan> wps = workPlanRepository.findByEducationPlan_IdOrderByCourse(id);
        Integer count = 0;
        for(IndividualWorkPlan wp : iwp){
            if(wp.getStatus() != WorkPlanStatus.EXPELLED && wp.getStatus() != WorkPlanStatus.ACADEM && wp.getStatus() != WorkPlanStatus.TRANSFERREDOUT){
                List<WorkPlan> twp = wps.stream().filter(x -> x.getCourse() == (wp.getWorkPlan().getCourse() + 1))
                        .collect(Collectors.toList());
                IndividualWorkPlan newiwp;
                if(!twp.isEmpty()) {
                    newiwp =
                            new IndividualWorkPlan(
                                    wp.getId(),
                                    wp.getIndividual(),
                                    twp.get(0),
                                    wp.getStatus());
                    logInfoRepository.save(new LogInfo(null, wp.getIndividual().getId(), "Перевод с " + wp.getWorkPlan().getCourse() + " на " + twp.get(0).getCourse(), LocalDateTime.now(), user));
                    if(count == 0){
                        ep.setStatus(Status.INWORK);
                        educationPlanRepository.save(ep);
                        count++;
                    }
                }
                else {
                    newiwp =
                            new IndividualWorkPlan(
                                    wp.getId(),
                                    wp.getIndividual(),
                                    wp.getWorkPlan(),
                                    WorkPlanStatus.ENDED);
                    logInfoRepository.save(new LogInfo(null, wp.getIndividual().getId(), "Закончил обучение", LocalDateTime.now(), user));
                    if(count == 0){
                        ep.setStatus(Status.INACTIVE);
                        educationPlanRepository.save(ep);
                        count++;
                    }
                }
            individualWorkPlanRepository.save(newiwp);
            }
        }

        logInfoRepository.save(new LogInfo(null, iwp.get(0).getWorkPlan().getEducationPlan().getId(), "Перевод на следующий курс", LocalDateTime.now(), user));
        return iwp;
    }

    @Transactional(readOnly = true)
    public List<Individual> findAllIndividualsList() {
        return individualRepository.findAll();
    }

    @Transactional
    public Individual createPersonalCode(UUID id) {
        final Individual individual = findIndividual(id);
        individual.setIndividualCode(individual.hashCode());
        return individualRepository.save(individual);
    }

    @Transactional(readOnly = true)
    public List<IndividualWorkPlan> findAllIndividualWP(UUID id) {
        return individualWorkPlanRepository.findAllByIndividual_Id(id);
    }

}
