package com.io.tedtalks.service;

import com.io.tedtalks.exception.CSVFileFormatException;
import com.io.tedtalks.model.TedTalk;
import com.io.tedtalks.repository.TedTalkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TedTalkImportServiceTest {

    @Mock
    private TedTalkRepository tedTalkRepository;

    @InjectMocks
    private TedTalkImportService tedTalkImportService;

    @Test
    void shouldImportTedTalksFromValidCsv() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv",
                "title,speaker,date,views,likes,url\nTitle1,Speaker1,February 2023,100,50,url1".getBytes(StandardCharsets.UTF_8));
        Set<TedTalk> expectedTedTalks = Set.of(new TedTalk(null, "Title1", "Speaker1", YearMonth.of(2023, 2), 100L, 50L, "url1"));
        when(tedTalkRepository.findByUrl("url1")).thenReturn(null);

        tedTalkImportService.importTedTalksFromCsv(file);

        verify(tedTalkRepository).saveAll(expectedTedTalks);
    }

    @Test
    void shouldThrowExceptionForInvalidCsvFormat() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv",
                "header\ninvalid,csv,format".getBytes(StandardCharsets.UTF_8));

        assertThrows(CSVFileFormatException.class, () -> tedTalkImportService.importTedTalksFromCsv(file));
    }

    @Test
    void shouldThrowExceptionForEmptyCsvFile() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv", "".getBytes(StandardCharsets.UTF_8));

        assertThrows(CSVFileFormatException.class, () -> tedTalkImportService.importTedTalksFromCsv(file));
    }

    @Test
    void shouldThrowExceptionForCsvWithMissingFields() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv",
                "title,speaker,date,views,likes,url\nTitle1,Speaker1,2023-02,100".getBytes(StandardCharsets.UTF_8));

        assertThrows(CSVFileFormatException.class, () -> tedTalkImportService.importTedTalksFromCsv(file));
    }

    @Test
    void shouldThrowExceptionForInvalidDateFormat() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv",
                "title,speaker,date,views,likes,url\nTitle1,Speaker1,invalid-date,100,50,url1".getBytes(StandardCharsets.UTF_8));

        assertThrows(CSVFileFormatException.class, () -> tedTalkImportService.importTedTalksFromCsv(file));
    }

    @Test
    void shouldThrowExceptionForInvalidViewsFormat() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv",
                "title,speaker,date,views,likes,url\nTitle1,Speaker1,2023-02,invalid-views,50,url1".getBytes(StandardCharsets.UTF_8));

        assertThrows(CSVFileFormatException.class, () -> tedTalkImportService.importTedTalksFromCsv(file));
    }

    @Test
    void shouldThrowExceptionForInvalidLikesFormat() {
        MultipartFile file = new MockMultipartFile("file", "tedtalks.csv", "text/csv",
                "title,speaker,date,views,likes,url\nTitle1,Speaker1,2023-02,100,invalid-likes,url1".getBytes(StandardCharsets.UTF_8));

        assertThrows(CSVFileFormatException.class, () -> tedTalkImportService.importTedTalksFromCsv(file));
    }
}
