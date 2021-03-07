package com.sej.escape.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum QuizType implements BinaryConvertOp{
    LOCK(0), DEVICE(1);

    // 2진수에서 각 자릿수의 제곱
    // ex) 2^0에서 0, 2^2에서 2
    private int binaryDigitPower;

    public static Map<Integer, QuizType> lookup = new HashMap<>();

    static {
        for (QuizType type : QuizType.values()) {
            lookup.put(type.getBinaryDigitPower(), type);
        }
    }

    // @param int typeSum ex) LOCK = 2^1 = 2, DEVICE = 2^2 = 4, ...
    //                  LOCK + DEVICE + ... = 2 + 4 + ... = typeSum
    public static List<QuizType> getEnumList(int typeSum){

        int typeCnt = QuizType.values().length;

        BitSet bitset = BinaryUtil.convertToBitset(typeSum, typeCnt);

        List<QuizType> quizTypes = BinaryUtil.convertToEnumList(bitset,
                binaryDigitPower -> lookup.get(binaryDigitPower));

        return quizTypes;
    }

    public static int getEnumValSum(List<QuizType> quizTypes){

        int sum = BinaryUtil.convertToValSum(quizTypes,
                quizType -> (int) Math.pow(2, quizType.binaryDigitPower));

        return sum;
    }

}
