package au.org.garvan.kccg.subscription.worker;

//import com.google.common.base.Strings;
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

    public static JSONArray getSubscriptions(){
        JSONArray jsonResponseArray = new JSONArray();
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


            jsonResponseArray = (JSONArray) JSONValue.parse( response.body().string().trim()) ;

        } catch (SocketException e) {
            slf4jLogger.error(String.format("Socket exception while fetching subscriptions.\n Exception: %s", e.toString()));
        } catch (IOException ex) {
            slf4jLogger.error(String.format("IO exception while fetching subscriptions. \n Exception: %s", ex.toString()));
        }

        return jsonResponseArray;
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


    public static JSONArray getArticles(JSONObject jsonQuery){
        JSONArray jsonResponseArray = new JSONArray();
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


            jsonResponseArray = (JSONArray) JSONValue.parse( response.body().string().trim()) ;

        } catch (SocketException e) {
            slf4jLogger.error(String.format("Socket exception while fetching subscriptions.\n Exception: %s", e.toString()));
        } catch (IOException ex) {
            slf4jLogger.error(String.format("IO exception while fetching subscriptions. \n Exception: %s", ex.toString()));
        }

        return jsonResponseArray;
    }





    private static void init(){
        AppConfig config = null;
        Yaml yaml = new Yaml();

        String configFile="";
        String ENV = System.getenv("ENV");
        if (ENV==null || ENV.isEmpty())
            ENV = "Local";

        switch(ENV) {
            case "Local":
                configFile =  "application.yml";
                break;
            case "DEV":
                configFile =  "application-dev.yml";
                break;
            case "PROD":
                configFile =  "application-prod.yml";
                break;
        }
        try(InputStream in = ClassLoader.getSystemResourceAsStream("config/" + configFile)) {
            config = yaml.loadAs(in, AppConfig.class);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        pipelineEndpoint = config.getPipeline().get("endpoint");

    }


}
