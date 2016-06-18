package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.configoption.*;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

@OutputHandler(id="WEBSERVICE", name="Webservice")
public class WebserviceOutputHandler implements IOutputHandler {
    @Override
    public void process(Config config, SendItem item) throws
            TemporaryProcessingException, PermanentProcessingException {
        CloseableHttpClient httpClient = createHttpClient(config);
        HttpRequestBase request = createRequest(config);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != 200) {
                    throw new TemporaryProcessingException("Received HTTP Status " + status.getStatusCode());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new TemporaryProcessingException("IOException: " + e.getMessage());
        }
    }

    private HttpRequestBase createRequest(Config config) throws PermanentProcessingException {
        String url = config.getString("url");
        String payload = config.getString("payload");
        HttpRequestBase request;

        try {
            if ("POST".equals(config.getString("method"))) {
                HttpPost post = new HttpPost(url);
                setContentType(config, post);
                setPayload(config, post, payload);
                request = post;
            } else {
                request = new HttpGet(url);
            }
        } catch (IllegalArgumentException e) {
            throw new PermanentProcessingException("Invalid argument: " + e.getMessage());
        }
        return request;
    }

    private void setContentType(Config config, HttpPost post) {
        String contenType = config.getString("contentType");
        if (!GenericValidator.isBlankOrNull(contenType)) {
            post.setHeader(HTTP.CONTENT_TYPE, contenType);
        }
    }

    private void setPayload(Config config, HttpPost post, String payload) throws PermanentProcessingException {
        try {
            HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
            post.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new PermanentProcessingException("UnsupportedEncodingException: " + e.getMessage());
        }
    }

    private CloseableHttpClient createHttpClient(Config config) {
        CloseableHttpClient httpClient;
        if ("BASIC".equals(config.getString("authType"))) {
            httpClient = createHttpClientWithBasicAuthentication(config);
        } else {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    private CloseableHttpClient createHttpClientWithBasicAuthentication(Config config) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(config.getString("username"), config.getString("password"));
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        List<OutputHandlerConfigOption> options = new ArrayList<>();
        StringConfigOption url = new StringConfigOption("url", "URL (encoded)", true, "");
        url.setRequiresUriEncoding(true);
        options.add(url);
        SelectConfigOption authType = new SelectConfigOption("authType", "Authentication", true, "NONE");
        authType.addOption("NONE", "None");
        authType.addOption("BASIC", "Basic");
        options.add(authType);
        options.add(new StringConfigOption("username", "Username", false, ""));
        options.add(new StringConfigOption("password", "Password", false, ""));
        SelectConfigOption method = new SelectConfigOption("method", "Method", true, "GET");
        method.addOption("GET", "GET");
        method.addOption("POST", "POST");
        options.add(method);
        options.add(new StringConfigOption("contentType", "Content Type (POST only)", false, ""));
        StringConfigOption payload = new StringConfigOption("payload", "Payload (POST only)", false, "", true);
        payload.setRequiresUriEncoding(true);
        options.add(payload);
        return options;
    }

    @Override
    public void checkConfig(Config config) throws InvalidConfigException {
        if (GenericValidator.isBlankOrNull(config.getString("url"))) {
            throw new FieldRequiredException("url");
        }
        if (!isValidUrl(config.getString("url"))) {
            throw new FieldInvalidException("url");
        }
        if (!("GET".equals(config.getString("method")) || "POST".equals(config.getString("method")))) {
            throw new FieldInvalidException("method");
        }
        if (!("NONE".equals(config.getString("authType")) || "BASIC".equals(config.getString("authType")))) {
            throw new FieldInvalidException("authType");
        }
    }

    @Override
    public void checkRecipientAddress(Address address) throws InvalidAddessException {
        // Nothing to do in webservice output handler
    }

    private boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"};
        long options = UrlValidator.ALLOW_LOCAL_URLS;
        UrlValidator urlValidator = new UrlValidator(schemes, options);
        return urlValidator.isValid(url);
    }
}
