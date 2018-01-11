package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ahmed on 11/1/18.
 */
public class EmailGeneratorTest {
    @Test
    public void prepapreAndSendEmail() throws Exception {
        List<SubscriptionDto> subs = PipelineHandler.getSubscriptions();
        SearchResponseDto articles = PipelineHandler.getArticles(subs.get(0).getQuery());
        EmailGenerator.prepapreAndSendEmail(subs.get(0), articles);

    }

}