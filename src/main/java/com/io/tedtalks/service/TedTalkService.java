package com.io.tedtalks.service;

import com.io.tedtalks.model.TopSpeaker;
import com.io.tedtalks.model.TedTalk;
import com.io.tedtalks.repository.TedTalkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TedTalkService {

    private final TedTalkRepository tedTalkRepository;

    public Long saveTedTalk(TedTalk tedTalk) {
        if(tedTalkRepository.findByUrl(tedTalk.getUrl()) != null) {
            throw new IllegalArgumentException("TedTalk with url " + tedTalk.getUrl() + " already exists");
        }
        return tedTalkRepository.save(tedTalk).getId();
    }

    public Long updateTedTalk(TedTalk tedTalk) {
        if(tedTalk.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }
        return tedTalkRepository.save(tedTalk).getId();
    }

    public List<TedTalk> findTedTalksWithTitle(String title) {
        return tedTalkRepository.findByTitle(title);
    }

    public void deleteTedTalk(Long id) {
        if(tedTalkRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("TedTalk with id " + id + " not found");
        }
        tedTalkRepository.deleteById(id);
    }

    public List<TopSpeaker> getTopTedTalkSpeakers(int limit) {
        return tedTalkRepository.topTedTalksSpeakers(limit);
    }

    public List<TopSpeaker> getTopTedTalkSpeakersPerYear(int year, int limit) {
//        return tedTalkRepository.topTedTalksSpeakersPerYear(year, limit);
        return null;
    }
}
