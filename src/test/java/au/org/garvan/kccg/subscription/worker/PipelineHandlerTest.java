package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.dto.ArticleDto;
import au.org.garvan.kccg.subscription.worker.dto.PaginationDto;
import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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
//        List<SubscriptionDto> respone = PipelineHandler.getSubscriptions();
//        List<String> genes = respone.get(0).getGenesInQuery();
//    }

//    @Test
//    public void getArticles() throws Exception {
//        List<SubscriptionDto> subs = PipelineHandler.getSubscriptions();
//        SearchResponseDto articles = PipelineHandler.getArticles(subs.get(0).getQuery());
//        Map x = articles.getArticles().get(0).getGenesWithCount();
//
//    }


}