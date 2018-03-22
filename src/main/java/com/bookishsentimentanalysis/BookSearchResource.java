package com.bookishsentimentanalysis;

import org.hibernate.validator.constraints.NotEmpty;
import javax.ws.rs.*;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/book-search")
public class BookSearchResource {

    private static final Logger log = LoggerFactory.getLogger(BookSearchResource.class);

    private final OkHttpClient okHttpClient;

    public BookSearchResource() {
        this.okHttpClient = new OkHttpClient();
    }

    @GET
    public String requestBookByNameOrAuthor(@QueryParam("q") @NotEmpty String searchField) {
        String requestedUrl = buildUrl(searchField);

        Request request = new Request.Builder()
                .url(requestedUrl)
                .build();

        byte[] xmlResponse = execute(request);
        JSONObject json = getJson(xmlResponse);

        return json.toString();
    }

    private String buildUrl(@QueryParam("q") @NotEmpty String searchField) {
        StringBuilder url = new StringBuilder();
        url.append("https://www.goodreads.com/search?q=");
        url.append(searchField);
        url.append("&format=xml&key=");
        return url.toString();
    }

    private JSONObject getJson(byte[] xmlResponse) {
        try {
            return XML.toJSONObject(new String(xmlResponse));
        } catch (Exception e) {
            log.error("Exception converting XML to JSON", e);
            throw new JSONException(e);
        }
    }

    private byte[] execute(Request request) {
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().bytes();
        } catch (Exception e) {
            log.error("Exception executing request", e);
            throw new WebApplicationException(e);
        }
    }

}