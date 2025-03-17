package com.io.tedtalks.service;

import com.io.tedtalks.model.TopSpeaker;
import com.io.tedtalks.model.TedTalk;
import com.io.tedtalks.repository.TedTalkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TedTalkServiceTest {

    @Mock
    private TedTalkRepository tedTalkRepository;

    @InjectMocks
    private TedTalkService tedTalkService;

    @Test
    void shouldReturnTedTalksByTitle() {
        List<TedTalk> expectedTedTalks = List.of(
                new TedTalk(1L, "Title1", "Speaker1", YearMonth.of(2023, 2), 100L, 50L, "url1"),
                new TedTalk(2L, "Title2", "Speaker2", YearMonth.of(2022, 3), 200L, 100L, "url2")
        );
        when(tedTalkRepository.findByTitle("Title1")).thenReturn(expectedTedTalks);

        List<TedTalk> actualTedTalks = tedTalkService.findTedTalksWithTitle("Title1");

        assertEquals(expectedTedTalks, actualTedTalks);
    }

    @Test
    void shouldReturnEmptyListWhenTitleDoesNotExist() {
        when(tedTalkRepository.findByTitle("NonExistentTitle")).thenReturn(List.of());

        List<TedTalk> actualTedTalks = tedTalkService.findTedTalksWithTitle("NonExistentTitle");

        assertTrue(actualTedTalks.isEmpty());
    }

    @Test
    void shouldSaveTedTalkSuccessfully() {
        TedTalk newTedTalk = new TedTalk(null, "Title", "Speaker", YearMonth.of(2021, 1), 100L, 50L, "url");
        TedTalk savedTedTalk = new TedTalk(1L, "Title", "Speaker", YearMonth.of(2021, 1), 100L, 50L, "url");
        when(tedTalkRepository.save(newTedTalk)).thenReturn(savedTedTalk);

        Long actualId = tedTalkService.saveTedTalk(newTedTalk);

        assertEquals(savedTedTalk.getId(), actualId);
    }

    @Test
    void shouldUpdateTedTalkSuccessfully() {
        TedTalk updatedTedTalk = new TedTalk(1L, "Updated Title", "Updated Speaker", YearMonth.of(2021, 1), 100L, 50L, "url");
        when(tedTalkRepository.save(updatedTedTalk)).thenReturn(updatedTedTalk);

        Long actualId = tedTalkService.updateTedTalk(updatedTedTalk);

        assertEquals(updatedTedTalk.getId(), actualId);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingTedTalkWithoutId() {
        TedTalk tedTalkWithoutId = new TedTalk(null, "Title", "Speaker", YearMonth.of(2021, 1), 100L, 50L, "url");

        assertThrows(IllegalArgumentException.class, () -> tedTalkService.updateTedTalk(tedTalkWithoutId));
    }

    @Test
    void shouldDeleteTedTalkSuccessfully() {
        when(tedTalkRepository.findById(1L)).thenReturn(Optional.of(new TedTalk(1L, "Title", "Speaker", YearMonth.of(2021, 1), 100L, 50L, "url")));

        assertDoesNotThrow(() -> tedTalkService.deleteTedTalk(1L));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTedTalk() {
        when(tedTalkRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tedTalkService.deleteTedTalk(1L));
    }

    @Test
    void shouldReturnTopTedTalkSpeakers() {
        List<TopSpeaker> expectedTopSpeakers = List.of(
                new TopSpeaker("Speaker1", 1000L, 500L),
                new TopSpeaker("Speaker2", 2000L, 1000L)
        );
        when(tedTalkRepository.topTedTalksSpeakers(2)).thenReturn(expectedTopSpeakers);

        List<TopSpeaker> actualTopSpeakers = tedTalkService.getTopTedTalkSpeakers(2);

        assertEquals(expectedTopSpeakers, actualTopSpeakers);
    }

}
