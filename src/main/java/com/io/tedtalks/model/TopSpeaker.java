package com.io.tedtalks.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSpeaker {

    private String author;
    private Long totalViews;
    private Long totalLikes;
}
