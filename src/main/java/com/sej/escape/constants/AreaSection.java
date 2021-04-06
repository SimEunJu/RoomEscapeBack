package com.sej.escape.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AreaSection{

    NATIONWIDE("전국", 0, new AreaCode(Double.MIN_VALUE, Double.MAX_VALUE)),

    SEOUL("서울", 1, new AreaCode(0.1, 0.9)),
    INCHEON("인천",1, new AreaCode(2.1,2.3)),
    DAEJEON("대전",1, new AreaCode(3.4,3.5)),
    DAEGU("대구",1, new AreaCode(4.1, 4.3)),
    BUSAN("부산",1, new AreaCode(4.6, 4.9)),
    GWANGJU("광주",1, new AreaCode(6.1,6.2)),
    SEJONG("세종",1, new AreaCode(3.0, 3.0)),
    GYEONGGIDO("경기도",1, new AreaCode(1.0,2.0)),
    JEJU("제주",1, new AreaCode(6.3, 6.3)),

    GANGNAM("강남", 2, new AreaCode(0.60,0.64)),
    HONGDAE("홍대", 2, new AreaCode(0.39,0.42)),
    KONDAE("건대", 2, new AreaCode(0.49,0.51)),
    BUPYEONG("부평", 2, new AreaCode(2.13,2.14)),
    GUWOL("구월", 2, new AreaCode(2.15,2.18))
    ;

    // TODO: 계층형을 표현하기 위한 최선의 방법인가
    static {
        NATIONWIDE.subSections = Arrays.asList(SEOUL, INCHEON, DAEJEON, DAEGU, BUSAN, GWANGJU, SEJONG, GYEONGGIDO, JEJU);

        SEOUL.subSections = Arrays.asList(GANGNAM, HONGDAE, KONDAE);
        INCHEON.subSections = Arrays.asList(BUPYEONG, GUWOL);
    }

    AreaSection(String title, int depth, AreaCode areaCodeByPostcode){
        this.title = title;
        this.depth = depth;
        this.areaCodeByPostcode = areaCodeByPostcode;
    }

    private String title;
    private int depth;
    private AreaCode areaCodeByPostcode;
    private List<AreaSection> subSections;

    // public으로 설정하는게 맞을까
    @AllArgsConstructor
    @Getter
    public static class AreaCode{
        // [ lower, upper ]
        private double lower;
        private double upper;
    }

}
