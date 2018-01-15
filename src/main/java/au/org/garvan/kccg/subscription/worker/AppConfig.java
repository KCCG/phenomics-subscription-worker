package au.org.garvan.kccg.subscription.worker;
import lombok.Data;

import java.util.Map;

@Data
public class AppConfig
{

    public  Map<String, String> connections;


    @Override
    public String toString() {
        return "LogConfiguration [connections=" + connections + "]";
    }
}