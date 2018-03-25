package com.bookishsentimentanalysis;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class LangDetector {

    private static final Logger log = LoggerFactory.getLogger(LangDetector.class);
    private final LanguageDetector langDetector;

    private LangDetector(LanguageDetector langDetector) {
        this.langDetector = langDetector;
    }

    public static LangDetector create() {
        try {
            LanguageDetector langDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(new LanguageProfileReader().readAllBuiltIn())
                    .build();
            return new LangDetector(langDetector);
        } catch (Exception e) {
            log.error("Could not initialize language detector.", e);
            throw new RuntimeException(new IOException("Uninitialized language detector"));
        }
    }

    public String detectLanguage(String text) {
        try {
            TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
            TextObject textObject = textObjectFactory.forText(text);
            return langDetector.detect(textObject).get().getLanguage();
        } catch (Exception e) {
            log.error("Exception detecting language", e);
            throw new RuntimeException("Unable to detect language");
        }
    }

}
