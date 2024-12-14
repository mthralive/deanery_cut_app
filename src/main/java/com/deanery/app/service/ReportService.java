package com.deanery.app.service;

import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import static com.opencsv.ICSVWriter.DEFAULT_ESCAPE_CHARACTER;
import static com.opencsv.ICSVWriter.DEFAULT_LINE_END;
import static com.opencsv.ICSVWriter.DEFAULT_QUOTE_CHARACTER;
@Service
@RequiredArgsConstructor
public class ReportService {
    private final IndividualService individualService;
    private final EducationPlanService educationPlanService;
    public void exportComputerReport(HttpServletResponse response, UUID eId) {
        exportToCsv(
                response,
                "educationPlan_report.csv",
                new String[]{
                        "Идентификатор", "Персональный код", "Имя", "Фамилия", "Отчество", "День рождения",
                        "Место рождения", "Снилс", "ИНН", "Адрес регистрации",
                        "Актуальный адрес", "Почта"
                },
                educationPlanService.findAllIndividualOnEP(eId),
                individual -> new String[]{
                        String.valueOf(individual.getId()),String.valueOf(individual.getIndividualCode()),
                        individual.getFirst_name(), individual.getSecond_name(),
                        individual.getPatronymic(), String.valueOf(individual.getBirthday()),
                        individual.getBirthplace(),
                        individual.getSnils(), individual.getInn(),
                        individual.getRegistration(), individual.getActualAddress(),
                        individual.getEmail()
                }
        );
    }
    private <T> void exportToCsv(
            HttpServletResponse response,
            String fileName,
            String[] headers,
            List<T> items,
            Function<T, String[]> mapper
    ) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(response.getOutputStream(), Charset.forName("Windows-1251"))) {
            CSVWriter writer = new CSVWriter(outputStreamWriter, ';', DEFAULT_QUOTE_CHARACTER,
                    DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
            writer.writeNext(headers);
            for (T item : items) {
                writer.writeNext(mapper.apply(item));
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при экспорте CSV-отчета", e);
        }
    }
}