package com.sej.escape.entity.constants;

import com.sej.escape.exception.FunctionWithException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum ThemeQType {
    LOCK(0), DEVICE(1);

    // 2진수에서 각 자릿수의 제곱
    // ex) 2^1에서 1, 2^2에서 2
    private int binaryDigitPower;

    private static ThemeQType valueOf(int binaryDigitPower) throws Exception {
        switch (binaryDigitPower){
            case 0 : return LOCK;
            case 1 : return DEVICE;
            default: throw new Exception("정의된 enum이 없습니다.");
        }
    }

    private static IntFunction<ThemeQType> valueOfWithException(FunctionWithException<Integer, ThemeQType, Exception> intToObj){
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
    @SneakyThrows
    public static List<ThemeQType> getThemeQTypes(int typeSum){

        int typeCnt = ThemeQType.values().length;
        BitSet bitset = new BitSet(typeCnt);

        int digit = 0;
        while(digit >= typeCnt){
            int remain = typeSum % 2;
            if(remain == 1){
                bitset.set(digit);
                digit++;
            }
            int quotient = typeSum / 2;
            typeSum = quotient;
            if(quotient == 1) {
                bitset.set(digit);
                break;
            }
        }

        List<ThemeQType> themeQTypes = bitset.stream()
                .mapToObj(valueOfWithException(bit -> valueOf(bit)))
                .collect(Collectors.toList());

        return themeQTypes;
    }


    public static int getThemeQTypesValSum(List<ThemeQType> themeQTypes){
        int sum = themeQTypes.stream()
                .mapToInt(themeQType -> themeQType.binaryDigitPower)
                .sum();
        return sum;
    }

}
