package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.dto.ArticleDto;
import au.org.garvan.kccg.subscription.worker.dto.PaginationDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ahmed on 11/1/18.
 */
public class PipelineHandlerTest {

    @Before
    public void init(){
        Runner.init();
    }
//    @Test
//    public void getSubscriptions() throws Exception {
//        JSONArray respone = PipelineHandler.getSubscriptions();
//        ObjectMapper mapper = new ObjectMapper();
//        SubscriptionDto dto = mapper.readValue(respone.get(0).toString(), SubscriptionDto.class);
//    }
//
//    @Test
//    public void getArticles() throws Exception {
//        JSONArray response = PipelineHandler.getSubscriptions();
//        ObjectMapper mapper = new ObjectMapper();
//        SubscriptionDto dto = mapper.readValue(response.get(0).toString(), SubscriptionDto.class);
//        JSONObject response2 = PipelineHandler.getArticles(dto.getQuery());
//
//        ObjectMapper mapper2 = new ObjectMapper();
//        PaginationDto page = mapper2.readValue(response2.get("pagination").toString(),PaginationDto.class);
//        ArticleDto article = mapper2.readValue(((JSONArray)response2.get("articles")).get(0).toString(), ArticleDto.class);
//    }


}