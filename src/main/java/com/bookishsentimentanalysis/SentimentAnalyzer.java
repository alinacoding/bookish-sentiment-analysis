package com.bookishsentimentanalysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

public class SentimentAnalyzer {

    StanfordCoreNLP stanfordAnalyzer;

    public SentimentAnalyzer() {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        stanfordAnalyzer = new StanfordCoreNLP(properties);
    }

    public int getDominantSentiment(String review) {
        int dominantSentiment = 0;
        int longestSentence = 0;
        if (review != null && review.length() > 0) {
            Annotation annotation = stanfordAnalyzer.process(review);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                if (sentence.toString().length() > longestSentence) {
                    longestSentence = sentence.toString().length();
                    dominantSentiment = sentiment;
                }
            }
        }
        return dominantSentiment;

    }

}
