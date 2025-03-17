package com.io.tedtalks.service;

import com.io.tedtalks.exception.CSVFileFormatException;
import com.io.tedtalks.model.Response;
import com.io.tedtalks.model.TedTalk;
import com.io.tedtalks.repository.TedTalkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TedTalkImportService {

    private final TedTalkRepository tedTalkRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private final String SPLIT_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    public Set<String> importTedTalksFromCsv(MultipartFile file) {
        Set<String> errors = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            Set<TedTalk> tedTalks = reader.lines()
                    .skip(1) // Skip header line
                    .filter(line -> {
                        String[] fields = line.split(SPLIT_REGEX);
                        if(fields.length != 6) {
                            log.info("Line %s: Invalid CSV format, not correct count of fields".formatted(line));
                            errors.add("Line %s: Invalid CSV format, not correct count of fields".formatted(line));
                        }
                        return fields.length == 6;
                    })
                    .map(line -> {
                        try {
                            return this.mapToTedTalk(line);
                        } catch (CSVFileFormatException e) {
                            errors.add(e.getMessage());
                            return null;
                        }
                    })
                    .filter(tedTalk -> tedTalk != null)
                    .filter(tedTalk -> tedTalkRepository.findByUrl(tedTalk.getUrl()) == null)
                    .collect(Collectors.toSet());
            if(tedTalks.isEmpty()) {
                log.info("No ted talks found in CSV file or file is already imported");
                throw new CSVFileFormatException("No ted talks found in CSV file or file is already imported");
            }
            tedTalkRepository.saveAll(tedTalks);
        } catch (IOException e) {
            throw new CSVFileFormatException("Failed to import CSV file", e);
        }
        return errors;
    }

    private TedTalk mapToTedTalk(String line) {
        String[] fields = line.split(SPLIT_REGEX);
        YearMonth date;
        Long views, likes;
        try {
            date = YearMonth.parse(fields[2], dateTimeFormatter);
        } catch (DateTimeParseException e) {
            log.info("Line %s: Invalid date format".formatted(line));
            throw new CSVFileFormatException("Line %s: Invalid date format".formatted(line));
        }
        try {
            views = Long.parseLong(fields[3]);
            if(views < 0) {
                log.info("Line %s: Views cannot be negative".formatted(line));
                throw new CSVFileFormatException("Line %s: Views cannot be negative".formatted(line));
            }
        } catch (NumberFormatException e) {
            log.info("Line %s: Invalid views format".formatted(line));
            throw new CSVFileFormatException("Line %s: Invalid views format".formatted(line));
        }
        try {
            likes = Long.parseLong(fields[4]);
            if(likes < 0) {
                log.info("Line %s: Likes cannot be negative".formatted(line));
                throw new CSVFileFormatException("Line %s: Likes cannot be negative".formatted(line));
            }
        } catch (NumberFormatException e) {
            log.info("Line %s: Invalid likes format".formatted(line));
            throw new CSVFileFormatException("Line %s: Invalid likes format".formatted(line));
        }
        return new TedTalk(
                null,
                fields[0],
                fields[1],
                date,
                views,
                likes,
                fields[5]
        );
    }
}
