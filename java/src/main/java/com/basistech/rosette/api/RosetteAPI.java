package com.basistech.rosette.api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.basistech.rosette.model.CategoryOptions;
import com.basistech.rosette.model.CategoryResponse;
import com.basistech.rosette.model.EntityResponse;
import com.basistech.rosette.model.ErrorResponse;
import com.basistech.rosette.model.LanguageResponse;
import com.basistech.rosette.model.LinkedEntityResponse;
import com.basistech.rosette.model.MorphologyResponse;
import com.basistech.rosette.model.NameMatcherRequest;
import com.basistech.rosette.model.NameMatcherResponse;
import com.basistech.rosette.model.NameTranslationRequest;
import com.basistech.rosette.model.NameTranslationResponse;
import com.basistech.rosette.model.SentimentOptions;
import com.basistech.rosette.model.SentimentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.net.HttpURLConnection.HTTP_OK;

public class RosetteAPI {
    private static String key;
    private static String urlBase = "https://api.rosette.com/rest/v1/";
    private final String debugOutput = "?debug=true";
    private final static String language = "language";
    private final static String morphology = "morphology/complete";
    private final static String entities = "entities";
    private final static String entitiesLinked = "entities/linked";
    private final static String categories = "categories";
    private final static String sentiment = "sentiment";
    private final static String translatedName = "translated-name";
    private final static String matchedName = "matched-name";
    private final ObjectMapper mapper;

    public RosetteAPI(String filename) throws IOException {
        loadAPIKey(filename);
        mapper = new ObjectMapper();
    }

    public RosetteAPI() {
        mapper = new ObjectMapper();
    }

    public void setServiceUrl(String url) {
        this.urlBase = url;
    }

    public void setAPIKey(String key) {
        this.key = key;
    }

    private void loadAPIKey(String filename) throws IOException, IllegalArgumentException {
        if (null == filename || 0 == filename.length())
            throw new IllegalArgumentException("Empty API key file specified.");

        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Empty API key file specified.");
        }
        this.key = line.replaceAll("\\n", "").replaceAll("\\r", "");
        fis.close();
        reader.close();
    }

    public NameMatcherResponse matchName(NameMatcherRequest request) throws RosetteAPIException {
        String jsonStr = sendRequestMatchName(request, urlBase + matchedName);
        try {
            return mapper.readValue(jsonStr, NameMatcherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String sendRequestMatchName(NameMatcherRequest request, String urlStr) throws RosetteAPIException {
        String payloadStr = null;
        try {
            payloadStr = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sendRequest(payloadStr, urlStr);
    }

    public NameTranslationResponse translateName(NameTranslationRequest request) throws RosetteAPIException {
        String jsonStr = sendRequestTranslateName(request, urlBase + translatedName);
        try {
            return mapper.readValue(jsonStr, NameTranslationResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String sendRequestTranslateName(NameTranslationRequest request, String urlStr) throws RosetteAPIException {
        String payloadStr = null;
        try {
            payloadStr = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sendRequest(payloadStr, urlStr);
    }

    public LanguageResponse getLanguage(InputStream inputStream) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestFromFileWorker(inputStream, null, urlBase + language);
        return mapper.readValue(jsonStr, LanguageResponse.class);
    }

    public LanguageResponse getLanguage(URL url) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(url, urlBase + language);
        return mapper.readValue(jsonStr, LanguageResponse.class);
    }

    public LanguageResponse getLanguage(String content) throws RosetteAPIException, IOException {
        String jsonStr =  sendRequestWorker(content, urlBase + language);
        return mapper.readValue(jsonStr, LanguageResponse.class);
    }

    public MorphologyResponse getMorphology(InputStream inputStream) throws RosetteAPIException, IOException {
        String jsonStr =  sendRequestFromFileWorker(inputStream, null, urlBase + morphology);
        return mapper.readValue(jsonStr, MorphologyResponse.class);
    }

    public MorphologyResponse getMorphology(URL url) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(url, urlBase + morphology);
        return mapper.readValue(jsonStr, MorphologyResponse.class);
    }

    public MorphologyResponse getMorphology(String content) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(content, urlBase + morphology);
        return mapper.readValue(jsonStr, MorphologyResponse.class);
    }

    public EntityResponse getEntity(InputStream inputStream) throws RosetteAPIException, IOException {
        String jsonStr =  sendRequestFromFileWorker(inputStream, null, urlBase + entities);
        return mapper.readValue(jsonStr, EntityResponse.class);
    }

    public EntityResponse getEntity(URL url) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(url, urlBase + entities);
        return mapper.readValue(jsonStr, EntityResponse.class);
    }

    public EntityResponse getEntity(String content) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(content, urlBase + entities);
        return mapper.readValue(jsonStr, EntityResponse.class);
    }

    public LinkedEntityResponse getLinkedEntity(InputStream inputStream) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestFromFileWorker(inputStream, null, urlBase + entitiesLinked);
        return mapper.readValue(jsonStr, LinkedEntityResponse.class);
    }

    public LinkedEntityResponse getLinkedEntity(URL url) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(url, urlBase + entitiesLinked);
        return mapper.readValue(jsonStr, LinkedEntityResponse.class);
    }

    public LinkedEntityResponse getLinkedEntity(String content) throws RosetteAPIException, IOException {
        String jsonStr = sendRequestWorker(content, urlBase + entitiesLinked);
        return mapper.readValue(jsonStr, LinkedEntityResponse.class);
    }

    public CategoryResponse getCategories(InputStream inputStream, CategoryOptions options)  throws RosetteAPIException, IOException {
        String optionsStr = mapper.writeValueAsString(options);
        String jsonStr = sendRequestFromFileWorker(inputStream, optionsStr, urlBase + categories);
        return mapper.readValue(jsonStr, CategoryResponse.class);
    }

    public CategoryResponse getCategories(URL url, CategoryOptions options)  throws RosetteAPIException, IOException {
        String optionsStr = mapper.writeValueAsString(options);
        String jsonStr = sendRequestWorker(url, optionsStr, urlBase + categories);
        return mapper.readValue(jsonStr, CategoryResponse.class);
    }

    public CategoryResponse getCategories(String content, CategoryOptions options)  throws RosetteAPIException, IOException {
        String optionsStr = mapper.writeValueAsString(options);
        String jsonStr = sendRequestWorker(content, optionsStr, urlBase + categories);
        return mapper.readValue(jsonStr, CategoryResponse.class);
    }

    public SentimentResponse getSentiment(InputStream inputStream, SentimentOptions options) throws RosetteAPIException, IOException {
        String optionsStr = mapper.writeValueAsString(options);
        String jsonStr = sendRequestFromFileWorker(inputStream, optionsStr, urlBase + sentiment);
        return mapper.readValue(jsonStr, SentimentResponse.class);
    }

    public SentimentResponse getSentiment(URL url, SentimentOptions options) throws RosetteAPIException, IOException {
        String optionsStr = mapper.writeValueAsString(options);
        String jsonStr = sendRequestWorker(url, optionsStr, urlBase + sentiment);
        return mapper.readValue(jsonStr, SentimentResponse.class);
    }

    public SentimentResponse getSentiment(String content, SentimentOptions options) throws RosetteAPIException, IOException {
        String optionsStr = mapper.writeValueAsString(options);
        String jsonStr = sendRequestWorker(content, optionsStr, urlBase + sentiment);
        return mapper.readValue(jsonStr, SentimentResponse.class);
    }

    private String sendRequestFromFileWorker(InputStream inputStream, String options, String url) throws RosetteAPIException, IOException {
        MultipartUtility multipartUtility = new MultipartUtility(url + debugOutput, "UTF-8", key);
        multipartUtility.addFilePart("content", inputStream);
        if (options != null && options.length() > 0) {
            String optionsStr = "{\"options\":" + options + "}";
            InputStream optionStream = new ByteArrayInputStream(optionsStr.getBytes(StandardCharsets.UTF_8));
            multipartUtility.addFilePart("options", optionStream, "application/json");
        }

        List<String> response = multipartUtility.finish();

        if (response.size() > 0) {
            return response.get(0);
        } else {
            return "";
        }
    }

    private String sendRequestWorker(URL url, String serviceUrl) throws RosetteAPIException {
        return sendRequestWorker(url, null, serviceUrl);
    }

    private String sendRequestWorker(URL url, String options, String serviceUrl) throws RosetteAPIException {
        return sendRequest(payloadWorker("contentUri", url.toString(), options), serviceUrl);
    }

    private String payloadWorker(String key, String value, String options) {
        String payloadStr = "{";
        payloadStr += "\"" + key + "\":\"" + value + "\"";
        if (options != null && options.length() != 0) {
            payloadStr += ", \"options\":" + options;
        }
        payloadStr += "}";
        return payloadStr;
    }

    private String sendRequestWorker(String content, String serviceUrl) throws RosetteAPIException {
        return sendRequestWorker(content, null, serviceUrl);
    }

    private String sendRequestWorker(String content, String options, String serviceUrl) throws RosetteAPIException {
        return sendRequest(payloadWorker("content", content, options), serviceUrl);
    }

    private String sendRequest(String payloadStr, String urlStr) throws RosetteAPIException {
        String jsonStr = "";
        try {
            URL url = new URL(urlStr + debugOutput);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("user_key", key);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("Accept", "application/json");
            httpUrlConnection.connect();

            byte[] payload = payloadStr.getBytes();
            OutputStream os  = httpUrlConnection.getOutputStream();
            os.write(payload);
            os.close();

            InputStream inputStream = httpUrlConnection.getResponseCode() != HTTP_OK ?
                    httpUrlConnection.getErrorStream() : httpUrlConnection.getInputStream();

            DataInputStream istream = new DataInputStream(inputStream);
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    response.append(line);

                jsonStr = response.toString();
                int code = httpUrlConnection.getResponseCode();
                if (HTTP_OK != code) {
                    try {
                        ErrorResponse errorResponse = mapper.readValue(jsonStr, ErrorResponse.class);
                        throw new RosetteAPIException(code, errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

}
