package com.sej.escape.entity;

import com.sej.escape.entity.constants.QuizType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class QuizTypeTest {
    // TODO: println 말고 assert 사용해야
    @Test
    public void testThemeQType(){

        List<QuizType> lock = QuizType.getThemeQTypes(1);
        System.out.println(lock);

        List<QuizType> device = QuizType.getThemeQTypes(2);
        System.out.println(device);

        List<QuizType> lockAndDevice = QuizType.getThemeQTypes(3);
        System.out.println(lockAndDevice);
    }

    @Test
    public void testThemeQTypeSum(){

        List<QuizType> lock = Arrays.asList(new QuizType[]{QuizType.LOCK});
        int lockSum = QuizType.getThemeQTypesValSum(lock);
        System.out.println(lockSum);

        List<QuizType> device = Arrays.asList(new QuizType[]{QuizType.DEVICE});
        int deviceSum = QuizType.getThemeQTypesValSum(device);
        System.out.println(deviceSum);

        List<QuizType> lockAndDevice = Arrays.asList(new QuizType[]{QuizType.LOCK, QuizType.DEVICE});
        int lockAndDeviceSum = QuizType.getThemeQTypesValSum(lockAndDevice);
        System.out.println(lockAndDeviceSum);
    }
}
