package com.example.helloworld;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) {
        environment.jersey().register(new HelloWorldResource(configuration.buildTemplate()));
    }
}
