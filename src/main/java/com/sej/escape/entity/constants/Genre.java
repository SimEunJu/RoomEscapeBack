package com.sej.escape.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Genre {

    DRAMAS(0), ROMANTIC(1), ANIMATION(2),
    HISTORICAL(3), SCI_FI(4), THRILLERS(5),
    CRIME(6), MYSTERIES(7), HORROR(8);

    private int binaryDigitPower;

    public static Map<Integer, Genre> lookup = new HashMap<>();

    static {
        for (Genre type : Genre.values()) {
            lookup.put(type.getBinaryDigitPower(), type);
        }
    }

    public static List<Genre> getEnumList(int typeSum){

        int typeCnt = Genre.values().length;

        BitSet bitset = BinaryUtil.convertToBitset(typeSum, typeCnt);

        List<Genre> genres = BinaryUtil.convertToEnumList(bitset,
                binaryDigitPower -> lookup.get(binaryDigitPower));

        return genres;
    }

    public static int getEnumValSum(List<Genre> genres){

        int sum = BinaryUtil.convertToValSum(genres,
                genre -> (int) Math.pow(2, genre.binaryDigitPower));

        return sum;
    }

}
