package com.sej.escape.entity.constants;

import lombok.experimental.UtilityClass;

import java.util.BitSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@UtilityClass
public class BinaryUtil {

    public BitSet convertToBitset(int valSum, int valMaxCnt){

        BitSet bitset = new BitSet(valMaxCnt);

        int digit = 0;
        while(valSum != 0){

            int remain = valSum % 2;
            if(remain == 1){
                bitset.set(digit);
            }

            int quotient = valSum / 2;
            valSum = quotient;
            digit++;
        }

        return bitset;
    }

    public <T> List<T> convertToEnumList(BitSet bitset, IntFunction<T> function){

        List<T> enumList = bitset.stream()
                .mapToObj(function)
                .collect(Collectors.toList());

        return enumList;
    }

    public <T> int convertToValSum(List<T> enumList, ToIntFunction<T> function){

        int sum = enumList.stream()
                .mapToInt(function)
                .sum();

        return sum;
    }



}
