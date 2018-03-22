package com.bookishsentimentanalysis;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;


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
import org.jsoup.Jsoup;

import java.sql.SQLOutput;


@Path("/reviews")
public class BookReviewsResource {

        private static final Logger log = LoggerFactory.getLogger(com.bookishsentimentanalysis.BookSearchResource.class);

        private final OkHttpClient okHttpClient;
        private SentimentAnalyzer sentimentAnalyzer;

        public BookReviewsResource(SentimentAnalyzer sentimentAnalyzer) {
            this.okHttpClient = new OkHttpClient();
            this.sentimentAnalyzer = sentimentAnalyzer;
        }

        @GET
        public String getReviews(@QueryParam("q") String bookId) {
            String requestedUrl = buildUrl(bookId);

            Request request = new Request.Builder()
                    .url(requestedUrl)
                    .build();

            byte[] htmlResponse = execute(request);
            Document doc = Jsoup.parse(new String(htmlResponse));
            Elements reviews = doc.body().select("span[id~=freeText[\\d]+]");
            System.out.println(reviews.size());
            for (Element review : reviews) {
                System.out.println("**");
                System.out.println(review.text());
                int dominantSentiment = sentimentAnalyzer.getDominantSentiment(review.text());
                System.out.println(dominantSentiment);
                System.out.println("**");
            }
            return new String(htmlResponse);
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