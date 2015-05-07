package com.basistech.rosette.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.basistech.rosette.api.RosetteAPI;
import com.basistech.rosette.api.RosetteAPIException;
import com.basistech.rosette.api.RosetteAPIParameterException;
import com.basistech.rosette.model.CategorizationModel;
import com.basistech.rosette.model.Category;
import com.basistech.rosette.model.CategoryOptions;
import com.basistech.rosette.model.CategoryResponse;
import com.basistech.rosette.model.Decompounding;
import com.basistech.rosette.model.EntityResponse;
import com.basistech.rosette.model.ExtractedEntity;
import com.basistech.rosette.model.HanReadings;
import com.basistech.rosette.model.LanguageCode;
import com.basistech.rosette.model.LanguageDetectionResult;
import com.basistech.rosette.model.LanguageResponse;
import com.basistech.rosette.model.Lemma;
import com.basistech.rosette.model.LinkedEntity;
import com.basistech.rosette.model.LinkedEntityResponse;
import com.basistech.rosette.model.MorphologyResponse;
import com.basistech.rosette.model.Name;
import com.basistech.rosette.model.NameMatcherRequest;
import com.basistech.rosette.model.NameMatcherResponse;
import com.basistech.rosette.model.NameMatcherResult;
import com.basistech.rosette.model.NameTranslationRequest;
import com.basistech.rosette.model.NameTranslationResponse;
import com.basistech.rosette.model.PartOfSpeech;
import com.basistech.rosette.model.Sentiment;
import com.basistech.rosette.model.SentimentModel;
import com.basistech.rosette.model.SentimentOptions;
import com.basistech.rosette.model.SentimentResponse;
import com.basistech.rosette.model.TranslatedNameResult;

public class APIExample {

    private static final String key = "Rosette API key"; // Your Rosette API key
    private static RosetteAPI rosetteAPI;

    public static void main(String[] args) throws URISyntaxException, IOException, RosetteAPIParameterException {
        String website = "http://www.basistech.com";
        URL url = null;
        try {
            url = new URL(website);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String text = "I live in Boston, Massachusetts.";

        if (args.length != 1) {
            usage();
            return;
        }
        String apiFilename = args[0];
        rosetteAPI = new RosetteAPI(apiFilename);
//        rosetteAPI.setServiceUrl("http://localhost:8755/rest/v1/");
        ClassLoader cl = APIExample.class.getClassLoader();

        testNameMatcherRequest("John Doe", "John Doe");
        testNameMatcherRequest("John Doe", "Jon Doe");
        testNameMatcherRequest("习近平", "Xi Jinping");

        testNameTranslationRequest("John Doe", LanguageCode.KOREAN);
        testNameTranslationRequest("习近平", LanguageCode.ENGLISH);

        testLanguageRequest(url);
        testLanguageRequest(text);
        testLanguageRequest(cl.getResourceAsStream("Chinese.txt"));

        testMorphologyRequest(url);
        testMorphologyRequest(text);
        testMorphologyRequest(cl.getResourceAsStream("Chinese.txt"));

        testEntityRequest(url);
        testEntityRequest(text);
        testEntityRequest(cl.getResourceAsStream("English.txt"));

        testResolvedEntity(url);
        testResolvedEntity(text);
        testResolvedEntity(cl.getResourceAsStream("English.txt"));

        testCategories(url);
        testCategories(url, new CategoryOptions(CategorizationModel.QAG));
        testCategories(url, new CategoryOptions(CategorizationModel.QAG, true));
        testCategories(url, new CategoryOptions(CategorizationModel.QAG, true, 10));
        testCategories(text);
        testCategories(cl.getResourceAsStream("English.txt"));

        testSentiment(url);
        testSentiment(url, new SentimentOptions(SentimentModel.REVIEW));
        testSentiment(url, new SentimentOptions(SentimentModel.REVIEW, true));
        testSentiment(text);
        testSentiment(text, new SentimentOptions(SentimentModel.SHORT_STRING, true));
        testSentiment(cl.getResourceAsStream("English.txt"));
        testSentiment(cl.getResourceAsStream("English.txt"), new SentimentOptions(SentimentModel.REVIEW, true));
    }

    private static void usage() {
        System.out.println("Usage: java -jar Rosette-API.jar api-key.txt");
    }

    private static void testNameMatcherRequest(String name1, String name2) {
        try {
            NameMatcherRequest request = new NameMatcherRequest(new Name(name1), new Name(name2));
            NameMatcherResponse response = rosetteAPI.matchName(request);
            print(request, response);
        } catch (RosetteAPIException e) {
            e.printStackTrace();
        } catch (RosetteAPIParameterException e) {
            e.printStackTrace();
        }
    }

    private static void print(NameMatcherRequest request, NameMatcherResponse response) {
        System.out.println(response.getRequestId());
        NameMatcherResult result = response.getResult();
        System.out.printf("%s\t%s\t%f\n", request.getName1().getText(), request.getName2().getText(), result.getScore());
        System.out.println();
    }

    private static void testCategories(URL url) {
        testCategories(url, null);
    }

    private static void testCategories(InputStream file) {
        testCategories(file, null);
    }

    private static void testCategories(String text) {
        testCategories(text, null);
    }

    private static void testCategories(URL url, CategoryOptions options) {
        try {
            CategoryResponse categoryResponse = rosetteAPI.getCategories(url, options);
            print(categoryResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testCategories(InputStream inputStream, CategoryOptions options) {
        try {
            CategoryResponse categoryResponse = rosetteAPI.getCategories(inputStream, options);
            print(categoryResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testCategories(String text, CategoryOptions options) {
        try {
            CategoryResponse categoryResponse = rosetteAPI.getCategories(text, options);
            print(categoryResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(CategoryResponse categoryResponse) {
        System.out.println(categoryResponse.getRequestId());
        for (Category category : categoryResponse.getCategories()) {
            System.out.printf("%s\t%f\t%s\n",
                    category.getLabel(),
                    category.getConfidence(),
                    category.getExplanations() == null ? "" : category.getExplanations());
        }
        System.out.println();
    }

    private static void testSentiment(InputStream file) {
        testSentiment(file, null);
    }

    private static void testSentiment(InputStream file, SentimentOptions options) {
        try {
            SentimentResponse sentimentResponse = rosetteAPI.getSentiment(file, options);
            print(sentimentResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testSentiment(String text) {
        testSentiment(text, null);
    }

    private static void testSentiment(String text, SentimentOptions options) {
        try {
            SentimentResponse sentimentResponse = rosetteAPI.getSentiment(text, options);
            print(sentimentResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testSentiment(URL url) {
        testSentiment(url, null);
    }

    private static void testSentiment(URL url, SentimentOptions options) {
        try {
            SentimentResponse sentimentResponse = rosetteAPI.getSentiment(url, options);
            print(sentimentResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(SentimentResponse sentimentResponse) {
        System.out.println(sentimentResponse.getRequestId());
        for (Sentiment sentiment : sentimentResponse.getSentiment()) {
            System.out.printf("%s\t%f\n", sentiment.getLabel(), sentiment.getConfidence());
            if (sentiment.getExplanations() != null) {
                for (String explanation : sentiment.getExplanations()) {
                    System.out.printf("  explanation: %s\n", explanation);
                }
            }
        }
        System.out.println();
    }

    private static void testResolvedEntity(InputStream file) {
        try {
            LinkedEntityResponse entityResponse = rosetteAPI.getLinkedEntity(file);
            print(entityResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(LinkedEntityResponse entityResponse) {
        System.out.println(entityResponse.getRequestId());
        for (LinkedEntity entity : entityResponse.getEntities()) {
            System.out.printf("%s\t%d\t%s\t%f\n",
                    entity.getEntityId(),
                    entity.getIndocChainId(),
                    entity.getMention(),
                    entity.getConfidence());
        }
        System.out.println();
    }

    private static void testResolvedEntity(String text) {
        try {
            LinkedEntityResponse entityResponse = rosetteAPI.getLinkedEntity(text);
            print(entityResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testResolvedEntity(URL url) {
        try {
            LinkedEntityResponse entityResponse = rosetteAPI.getLinkedEntity(url);
            print(entityResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testEntityRequest(InputStream file) {
        try {
            EntityResponse entityResponse = rosetteAPI.getEntity(file);
            print(entityResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(EntityResponse entityResponse) {
        System.out.println(entityResponse.getRequestId());
        for (ExtractedEntity entity : entityResponse.getEntities()) {
            System.out.printf("%s\t%s\t%d\t%s\t%f\t%d\n",
                    entity.getNormalized(),
                    entity.getType(),
                    entity.getCount(),
                    entity.getMention(),
                    entity.getConfidence(),
                    entity.getIndocChainId());
        }
    }

    private static void testEntityRequest(String text) {
        try {
            EntityResponse entityResponse = rosetteAPI.getEntity(text);
            print(entityResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testEntityRequest(URL url) {
        try {
            EntityResponse entityResponse = rosetteAPI.getEntity(url);
            print(entityResponse);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testLanguageRequest(InputStream file) {
        try {
            LanguageResponse response = rosetteAPI.getLanguage(file);
            print(response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(LanguageResponse response) {
        System.out.println(response.getRequestId());
        for (LanguageDetectionResult ldr : response.getLanguageDetections()) {
            System.out.printf("%s\t%f\n", ldr.getLanguage(), ldr.getConfidence());
        }
        System.out.println();
    }

    private static void testLanguageRequest(String text) {
        try {
            LanguageResponse response = rosetteAPI.getLanguage(text);
            print(response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testLanguageRequest(URL url) {
        try {
            LanguageResponse response = rosetteAPI.getLanguage(url);
            print(response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testMorphologyRequest(InputStream file) {
        try {
            MorphologyResponse response = rosetteAPI.getMorphology(file);
            print(response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testMorphologyRequest(String text) {
        try {
            MorphologyResponse response = rosetteAPI.getMorphology(text);
            print(response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void testMorphologyRequest(URL url) {
        try {
            MorphologyResponse response = rosetteAPI.getMorphology(url);
            print(response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(MorphologyResponse response) {
        System.out.println(response.getRequestId());
        String result = "===== posTags =====\n";
        for (PartOfSpeech partOfSpeech : response.getPosTags()) {
            result += partOfSpeech.getText() + "\t" + partOfSpeech.getPos() + "\n";
        }
        result += "===== lemmas =====\n";
        for (Lemma lemma : response.getLemmas()) {
            result += lemma.getText() + "\t" + lemma.getLemma() + "\n";
        }
        result += "===== compounds =====\n";
        for (Decompounding compoud : response.getCompounds()) {
            result += compoud.getText() + "\t" + compoud.getCompoundComponents() + "\n";
        }
        result += "===== hanReadings =====\n";
        for (HanReadings reading : response.getHanReadings()) {
            result += reading.getText() + "\t" + reading.getHanReadings() + "\n";
        }
        result += "\n";
        System.out.println(result);
    }

    private static void testNameTranslationRequest(String name, String targetLanguage) {
        try {
            NameTranslationRequest nameTranslationRequest = new NameTranslationRequest(name, null, null, null, null, targetLanguage, null, null);
            NameTranslationResponse response = rosetteAPI.translateName(nameTranslationRequest);
            print(name, response);
        } catch (RosetteAPIException e) {
            System.err.println(e.toString());
        } catch (RosetteAPIParameterException e) {
            System.err.println(e.toString());
        }
    }

    private static void print(String name, NameTranslationResponse response) {
        System.out.println(response.getRequestId());
        TranslatedNameResult result = response.getResult();
        System.out.println("name: " + name + ", "
                + "sourceScript: " + result.getSourceScript() + ", "
                + "sourceLanguageOfOrigin: " + result.getSourceLanguageOfOrigin() + ", "
                + "sourceLanguageOfUse: " + result.getSourceLanguageOfUse() + ", "
                + "translation: " + result.getTranslation() + ", "
                + "targetLanguage: " + result.getTargetLanguage() + ", "
                + "targetScript: " + result.getTargetScript() + ", "
                + "targetScheme: " + result.getTargetScheme() + ", "
                + "confidence: " + result.getConfidence());
        System.out.println();
    }
}
