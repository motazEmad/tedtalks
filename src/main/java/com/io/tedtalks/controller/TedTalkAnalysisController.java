package com.io.tedtalks.controller;

import com.io.tedtalks.entity.TopSpeaker;
import com.io.tedtalks.service.TedTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tedtalks/analysis")
@RequiredArgsConstructor
public class TedTalkAnalysisController {

    private final TedTalkService tedTalkService;

    @GetMapping("/top-speakers")
    public List<TopSpeaker> getTopTedTalkSpeakers(@RequestParam("limit") int limit,
                                                  @RequestParam(value = "year", required = false) Integer year) {
        if(year == null) {
            return tedTalkService.getTopTedTalkSpeakers(limit);
        }
        return tedTalkService.getTopTedTalkSpeakersPerYear(year, limit);
    }

}
