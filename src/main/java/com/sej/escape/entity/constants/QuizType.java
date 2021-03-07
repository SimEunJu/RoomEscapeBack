package com.sej.escape.entity.constants;

import com.sej.escape.exception.FunctionWithException;
import lombok.AllArgsConstructor;

import java.util.BitSet;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum QuizType {
    LOCK(0), DEVICE(1);

    // 2진수에서 각 자릿수의 제곱
    // ex) 2^0에서 0, 2^2에서 2
    private int binaryDigitPower;
    
    // TODO: 정의되지 않은 값이 있을 때 무엇을 어떻게 하고 싶은지 더 고민
    // 1. 인서트하지 않고, 사용자에게 정의되지 않은 타입이라고 알린다.
    // 2. 정의된 타입까지만 인서트하고, 사용자에게 알린다.
    private static QuizType valueOf(int binaryDigitPower) throws Exception {
        switch (binaryDigitPower){
            case 0 : return LOCK;
            case 1 : return DEVICE;
            default: return null;
        }
    }

    private static IntFunction<QuizType> valueOfWithException(FunctionWithException<Integer, QuizType, Exception> intToObj){
        return binaryDigitPower -> {
            try {
                return intToObj.apply(binaryDigitPower);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    // @param int typeSum ex) LOCK = 2^1 = 2, DEVICE = 2^2 = 4, ...
    //                  LOCK + DEVICE + ... = 2 + 4 + ... = typeSum
    public static List<QuizType> getThemeQTypes(int typeSum){

        int typeCnt = QuizType.values().length;
        BitSet bitset = new BitSet(typeCnt);

        int digit = 0;
        while(typeSum != 0){

            int remain = typeSum % 2;
            if(remain == 1){
                bitset.set(digit);
            }

            int quotient = typeSum / 2;
            typeSum = quotient;
            digit++;
        }

        List<QuizType> quizTypes = bitset.stream()
                .mapToObj(valueOfWithException(QuizType::valueOf))
                .collect(Collectors.toList());

        return quizTypes;
    }


    public static int getThemeQTypesValSum(List<QuizType> quizTypes){
        int sum = quizTypes.stream()
                .mapToInt(quizType -> (int) Math.pow(2, quizType.binaryDigitPower))
                .sum();
        return sum;
    }

}
