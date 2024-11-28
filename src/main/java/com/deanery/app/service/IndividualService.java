package com.deanery.app.service;

import com.deanery.app.dto.IndividualDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.model.Individual;
import com.deanery.app.repository.IndividualRepository;
import groovy.util.logging.Slf4j;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualService {
    private final IndividualRepository individualRepository;

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

    @Transactional(readOnly = true)
    public List<Individual> findAllIndividualsList() {
        return individualRepository.findAll();
    }
}
