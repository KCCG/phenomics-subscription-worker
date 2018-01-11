package au.org.garvan.kccg.subscription.worker;

//import com.google.common.base.Strings;
import au.org.garvan.kccg.subscription.worker.dto.ArticleDto;
import au.org.garvan.kccg.subscription.worker.dto.PaginationDto;
import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.InputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by ahmed on 20/12/17.
 */

public class PipelineHandler {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(PipelineHandler.class);

    private static String pipelineEndpoint;
    private static String port = ":9080";
    private static String subscriptionsAll = "/subscription/";
    private static String query = "/query/";

    static {
        init();
    }
    private static void init(){
        pipelineEndpoint = Runner.getConfig().pipeline.get("endpoint");
    }

    public static List<SubscriptionDto> getSubscriptions(){
        List<SubscriptionDto> lstSubscriptions = new ArrayList<>();

        OkHttpClient subscriptionClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(300L, TimeUnit.SECONDS)
                .writeTimeout(300L, TimeUnit.SECONDS)
                .readTimeout(300L, TimeUnit.SECONDS)
                .build();

        try {

            HttpUrl.Builder httpBuilder = HttpUrl.parse(pipelineEndpoint + port + subscriptionsAll).newBuilder();
            Request request = new Request.Builder()
                    .get()
                    .url(httpBuilder.build().url())
                    .build();

            slf4jLogger.info(String.format("Dispatching get request for subscriptions. URL:%s", request.url()));
            Response response = subscriptionClient.newCall(request).execute();
            slf4jLogger.info(String.format("Subscriptions request completed with code: %d", response.code()));


            JSONArray jsonSubscriptions = (JSONArray) JSONValue.parse( response.body().string().trim()) ;
            if(jsonSubscriptions.size()>0)
            {
                ObjectMapper mapper = new ObjectMapper();
                jsonSubscriptions.stream().forEach(x -> {
                    try {
                        lstSubscriptions.add(mapper.readValue(x.toString(), SubscriptionDto.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


            }

        } catch (SocketException e) {
            slf4jLogger.error(String.format("Socket exception while fetching subscriptions.\n Exception: %s", e.toString()));
        } catch (IOException ex) {
            slf4jLogger.error(String.format("IO exception while fetching subscriptions. \n Exception: %s", ex.toString()));
        }

        return lstSubscriptions;
    }

    public static void updateSubscription(String sId, long runDate, String timeStamp){

        OkHttpClient subscriptionClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(300L, TimeUnit.SECONDS)
                .writeTimeout(300L, TimeUnit.SECONDS)
                .readTimeout(300L, TimeUnit.SECONDS)
                .build();

        try {

            HttpUrl.Builder httpBuilder = HttpUrl.parse(pipelineEndpoint + port + subscriptionsAll).newBuilder();
            httpBuilder.addPathSegment(sId);
            httpBuilder.addPathSegment(String.valueOf(runDate));
            httpBuilder.addPathSegment(timeStamp);


            Request request = new Request.Builder()
                    .put(RequestBody.create(null, new byte[0]))
                    .url(httpBuilder.build().url())
                    .build();

            slf4jLogger.info(String.format("Dispatching request for subscription update. URL:%s", request.url()));
            Response response = subscriptionClient.newCall(request).execute();
            slf4jLogger.info(String.format("Subscription update request completed with code: %d", response.code()));


        } catch (SocketException e) {
            slf4jLogger.error(String.format("Socket exception while updating subscription.\n Exception: %s", e.toString()));
        } catch (IOException ex) {
            slf4jLogger.error(String.format("IO exception while updating subscription. \n Exception: %s", ex.toString()));
        }

    }


    public static SearchResponseDto getArticles(JSONObject jsonQuery){
        SearchResponseDto searchResponseDto = new SearchResponseDto();

        OkHttpClient subscriptionClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(300L, TimeUnit.SECONDS)
                .writeTimeout(300L, TimeUnit.SECONDS)
                .readTimeout(300L, TimeUnit.SECONDS)
                .build();

        try {

            HttpUrl.Builder httpBuilder = HttpUrl.parse(pipelineEndpoint + port + query).newBuilder();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonQuery.toString());
            Request request = new Request.Builder()
                    .post(body)
                    .url(httpBuilder.build().url())
                    .build();

            slf4jLogger.info(String.format("Dispatching query for search. Query:%s", jsonQuery.toJSONString()));
            Response response = subscriptionClient.newCall(request).execute();
            slf4jLogger.info(String.format("Query request completed with code: %d", response.code()));

            JSONObject jsonResponse = (JSONObject) JSONValue.parse( response.body().string().trim());
            ObjectMapper mapper = new ObjectMapper();
            PaginationDto page = mapper.readValue(jsonResponse.get("pagination").toString(),PaginationDto.class);
            searchResponseDto.setPagination(page);
            JSONArray jsonArticles =  (JSONArray)jsonResponse.get("articles");
            if(jsonArticles.size()>0)
            {
                List<ArticleDto> lstArtciles = new ArrayList<>();
                jsonArticles.stream().forEach(x -> {
                    try {
                        lstArtciles.add(mapper.readValue(x.toString(), ArticleDto.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                searchResponseDto.setArticles(lstArtciles);
            }


        } catch (SocketException e) {
            slf4jLogger.error(String.format("Socket exception while fetching subscriptions.\n Exception: %s", e.toString()));
        } catch (IOException ex) {
            slf4jLogger.error(String.format("IO exception while fetching subscriptions. \n Exception: %s", ex.toString()));
        }

        return searchResponseDto;
    }







}
