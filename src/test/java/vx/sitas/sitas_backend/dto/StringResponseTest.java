package vx.sitas.sitas_backend.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vx.sitas.sitas_backend.dto.internal.StringResponse;

public class StringResponseTest {



    @Test
    void checkGetter(){
        StringResponse underTest = new StringResponse("TEST");
        Assertions.assertEquals("TEST", underTest.getVal());
    }

    @Test
    void checkSetter() {
        StringResponse underTest = new StringResponse("DUMMY");
        underTest.setVal("TEST2");
        Assertions.assertEquals("TEST2", underTest.getVal());
    }

}
