package com.io.tedtalks.controller;

import com.io.tedtalks.exception.CSVFileFormatException;
import com.io.tedtalks.model.Response;
import com.io.tedtalks.service.TedTalkImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/tedtalks/import")
@RequiredArgsConstructor
public class TedTalkImportController {

    private final TedTalkImportService tedTalkImportService;

    @PostMapping("/csv")
    public Response<String> importTedTalksFromCsv(@RequestParam("file") MultipartFile file) {
        var errors = tedTalkImportService.importTedTalksFromCsv(file);
        return new Response<>("Imported ted talks from CSV", errors.toString());
    }

    @ExceptionHandler
    public Response<String> handleException(CSVFileFormatException e) {
        return new Response(null, e.getMessage());
    }
}
