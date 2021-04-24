package com.sej.escape;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SimpleTest {

    int field1 = 1;
    int field2 = this.field1;

    @Test
    public void simple(){
        String uuid = UUID.randomUUID().toString();

        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("H_m_s_N")).toString().substring(0, 12);
        System.out.println(uuid);
        System.out.println(time);
    }
}
