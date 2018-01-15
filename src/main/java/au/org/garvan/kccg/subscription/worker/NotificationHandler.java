package au.org.garvan.kccg.subscription.worker;

//import com.google.common.base.Strings;

import au.org.garvan.kccg.subscription.worker.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by ahmed on 20/12/17.
 */

public class NotificationHandler {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(NotificationHandler.class);

    private static String notificationEndpoint;
    private static String port = ":9090";
    private static String email = "/email";

    static {
        init();
    }

    private static void init() {
        notificationEndpoint = Runner.getConfig().connections.get("notification-endpoint");
    }

    public static void sendEmail(EmailNotificationRequestDto emailNotificationRequestDto) {
        OkHttpClient notificationClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(300L, TimeUnit.SECONDS)
                .writeTimeout(300L, TimeUnit.SECONDS)
                .readTimeout(300L, TimeUnit.SECONDS)
                .build();

        try {

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(emailNotificationRequestDto);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            HttpUrl.Builder httpBuilder = HttpUrl.parse(notificationEndpoint + port + email).newBuilder();

            Request request = new Request.Builder()
                    .post(body)
                    .url(httpBuilder.build().url())
                    .build();

            slf4jLogger.info(String.format("Dispatching email to notification service. ID:%s", emailNotificationRequestDto.getUniqueID() ));
            Response response = notificationClient.newCall(request).execute();
            slf4jLogger.info(String.format("Email dispatched with code: %d", response.code()));

        } catch (SocketException e) {
            System.out.println(String.format("Socket exception when posting email request. \n Exception: %s", e.toString()));
        } catch (IOException ex) {
            System.out.println(String.format("IO exception when posting email request.\n Exception: %s", ex.toString()));
        } catch (Exception eg) {
            System.out.println(String.format("Generic when posting email request.\n Exception: %s", eg.toString()));
        }


    }


}
