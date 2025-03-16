package com.io.tedtalks.controller;

import com.io.tedtalks.model.Response;
import com.io.tedtalks.model.TedTalk;
import com.io.tedtalks.service.TedTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tedtalks")
@RequiredArgsConstructor
public class TedTalkManagerController {

    private final TedTalkService tedTalkService;

    @GetMapping
    public Response<List<TedTalk>> getTedTalks(@RequestParam("title") String title) {
        return new Response(tedTalkService.findTedTalksWithTitle(title));
    }

    @PutMapping
    public Response<Long> saveTedTalk(@RequestBody TedTalk tedTalk) {
        return new Response(tedTalkService.saveTedTalk(tedTalk));
    }

    @PatchMapping
    public void updateTedTalk(@RequestBody TedTalk tedTalk) {
        tedTalkService.updateTedTalk(tedTalk);
    }

    @DeleteMapping("/{id}")
    public void deleteTedTalk(@PathVariable("id") Long id) {
        tedTalkService.deleteTedTalk(id);
    }

    @ExceptionHandler
    public Response<String> handleException(IllegalArgumentException e) {
        return new Response(null, e.getMessage());
    }

}

