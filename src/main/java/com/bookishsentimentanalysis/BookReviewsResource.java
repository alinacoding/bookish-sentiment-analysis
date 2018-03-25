package com.bookishsentimentanalysis;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;

import java.io.IOException;

@Path("/reviews")
public class BookReviewsResource {

    private static final Logger log = LoggerFactory.getLogger(BookReviewsResource.class);

    private final OkHttpClient okHttpClient;
    private final SentimentAnalyzer sentimentAnalyzer;
    private final LangDetector langDetector;

    public BookReviewsResource(OkHttpClient okHttpClient, SentimentAnalyzer sentimentAnalyzer, LangDetector langDetector) {
        this.okHttpClient = okHttpClient;
        this.sentimentAnalyzer = sentimentAnalyzer;
        this.langDetector = langDetector;
    }

    @GET
    public String getReviews(@QueryParam("q") String bookId) throws IOException {
        String requestedUrl = buildUrl(bookId);

        Request request = new Request.Builder()
                .url(requestedUrl)
                .build();

        byte[] htmlResponse = execute(request);
        Document doc = Jsoup.parse(new String(htmlResponse));
        Elements reviews = doc.body().select("span[id~=freeText[\\d]+]");
        computeReviewsSentiment(reviews);
        return new String(htmlResponse);
    }

    private void computeReviewsSentiment(Elements reviews) {
        reviews.parallelStream().forEach(review -> {
            String language = langDetector.detectLanguage(review.text());
            if (language.equals("en")) {
                int dominantSentiment = sentimentAnalyzer.getDominantSentiment(review.text());
                System.out.println(dominantSentiment);
            }
        });
    }

    private String buildUrl(@QueryParam("q") String bookId) {
        StringBuilder url = new StringBuilder();
        url.append("https://www.goodreads.com/book/show/");
        url.append(bookId);
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