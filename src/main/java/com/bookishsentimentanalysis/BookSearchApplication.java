package com.bookishsentimentanalysis;

import com.squareup.okhttp.OkHttpClient;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class BookSearchApplication extends Application<BookSearchConfiguration> {
    OkHttpClient okHttpClient = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        new BookSearchApplication().run(args);
    }

    @Override
    public void run(BookSearchConfiguration configuration, Environment environment) {
        environment.jersey().register(bookSearchResource());
        environment.jersey().register(bookReviewsResource());
    }

    private BookSearchResource bookSearchResource() {
        return new BookSearchResource(okHttpClient);
    }

    private BookReviewsResource bookReviewsResource() {
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        LangDetector langDetector = LangDetector.create();
        return new BookReviewsResource(okHttpClient, sentimentAnalyzer, langDetector);
    }
}
