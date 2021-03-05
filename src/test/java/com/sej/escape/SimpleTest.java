package com.sej.escape;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class SimpleTest {

    int field1 = 1;
    int field2 = this.field1;

    @Test
    public void simple(){
        System.out.println(field2);
    }
}
