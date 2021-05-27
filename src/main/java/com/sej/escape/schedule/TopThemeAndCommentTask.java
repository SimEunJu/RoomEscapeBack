package com.sej.escape.schedule;

import com.sej.escape.entity.trending.TopTrendingTheme;
import com.sej.escape.entity.trending.TopTrendingThemeComment;
import com.sej.escape.repository.trending.TopTrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TopThemeAndCommentTask {

    private final EntityManager em;
    private final TopTrendingRepository topTrendingRepository;

    // 2시간 마다
    @Scheduled(cron="0 15 0/2 * * *")
    public void gatherTopThemes(){
        // ( comment_cnt + zim_cnt ) * comment_star_avg -> 별점 높을수록 높게
        List<Object[]> results = em.createNamedQuery("Theme.findTopThemes").getResultList();
        topTrendingRepository.updatePrevThemeTrendingInactive();
        List<TopTrendingTheme> topTrendingThemes = results.stream().map(result -> {
            return TopTrendingTheme.themeBuilder()
                    .referId((Long) result[0])
                    .isActive(true)
                    .build();
        }).collect(Collectors.toList());
        topTrendingRepository.saveAll(topTrendingThemes);
    }

    // 2시간 마다
    @Scheduled(cron="0 15 0/2 * * *")
    public void gatherTopThemeComments(){
        // count(good.good_id) + datediff(now(), theme_comment.reg_date) * -0.05
        // 좋아요 수가 많을수록 + 등록된지 오래될 수록 순위 밀려나도록
        List<Object[]> results = em.createNamedQuery("ThemeComment.findTopThemeComments").getResultList();
        topTrendingRepository.updatePrevThemeTrendingInactive();
        List<TopTrendingThemeComment> topTrendingThemes = results.stream().map(result -> {
            return TopTrendingThemeComment.themeCommentBuilder()
                    .referId((Long) result[0])
                    .isActive(true)
                    .build();
        }).collect(Collectors.toList());
        topTrendingRepository.saveAll(topTrendingThemes);
    }
}
