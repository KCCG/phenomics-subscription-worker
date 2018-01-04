package au.org.garvan.kccg.subscription.worker;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by ahmed on 4/1/18.
 */
public class RunnerTest {
    @Test
    public void test() {


        for (int x = 0;x<20;x++)
        {
            LocalDate t = LocalDate.now().minusDays(x);
            System.out.println(String.format("%s,%d",t.toString(),t.toEpochDay()));
        }


    }

}