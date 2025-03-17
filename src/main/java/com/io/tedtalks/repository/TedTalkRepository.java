package com.io.tedtalks.repository;

import com.io.tedtalks.model.TopSpeaker;
import com.io.tedtalks.model.TedTalk;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TedTalkRepository extends CrudRepository<TedTalk, Long> {

    List<TedTalk> findByTitle(String title);
    TedTalk findByUrl(String url);

    @Query("SELECT new com.io.tedtalks.model.TopSpeaker(t.author, SUM(t.views), SUM(t.likes)) FROM TedTalk t GROUP BY t.author ORDER BY SUM(t.views) DESC, SUM(t.likes) DESC LIMIT :limit")
    List<TopSpeaker> topTedTalksSpeakers(@Param("limit") int limit);

}
