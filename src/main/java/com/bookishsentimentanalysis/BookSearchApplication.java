package com.bookishsentimentanalysis;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class BookSearchApplication extends Application<BookSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new BookSearchApplication().run(args);
    }

    @Override
    public void run(BookSearchConfiguration configuration, Environment environment) {
        environment.jersey().register(new BookSearchResource());

    }
}
