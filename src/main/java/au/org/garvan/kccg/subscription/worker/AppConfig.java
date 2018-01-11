package au.org.garvan.kccg.subscription.worker;
import lombok.Data;

import java.util.Map;

@Data
public class AppConfig
{

    public  Map<String, String> pipeline;


    @Override
    public String toString() {
        return "LogConfiguration [pipeline=" + pipeline + "]";
    }
}