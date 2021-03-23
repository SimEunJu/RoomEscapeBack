package com.sej.escape.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AreaSection{

    NATIONWIDE("전국", 0),

    SEOUL("서울", 1),
    INCHEON("인천",1),
    DAEJEON("대전",1),
    DAEGU("대구",1),
    BUSAN("부산",1),
    GWANGJU("광주",1),
    SEJONG("세종",1),
    GYEONGGIDO("경기도",1),
    JEJU("제주",1),

    GANGNAM("강남", 2), HONGDAE("홍대", 2), KONDAE("건대", 2),
    BUPYEONG("부평", 2), GUWOL("구월", 2)
    ;

    static {
        NATIONWIDE.subSections = Arrays.asList(SEOUL, INCHEON, DAEJEON, DAEGU, BUSAN, GWANGJU, SEJONG, GYEONGGIDO, JEJU);

        SEOUL.subSections = Arrays.asList(GANGNAM, HONGDAE, KONDAE);
        INCHEON.subSections = Arrays.asList(BUPYEONG, GUWOL);
    }

    AreaSection(String title, int depth){
        this.title = title;
        this.depth = depth;
    }

    private String title;
    private int depth;
    private List<AreaSection> subSections;

}
