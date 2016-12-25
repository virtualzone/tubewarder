package net.weweave.tubewarder.outputhandler;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TwitterOauth {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String getOautHeaderString(Credentials credentials, String method, String baseUrl, Map<String, String> params) {
        String nonce = UUID.randomUUID().toString();
        int time = Math.round(System.currentTimeMillis()/1000L);
        return getOautHeaderString(credentials, method, baseUrl, params, nonce, time);
    }

    public static String getOautHeaderString(Credentials credentials, String method, String baseUrl, Map<String, String> params, String nonce, int time) {
        String collectedParams = collectOauthParams(params, credentials.getConsumerKey(), nonce, time, credentials.getAccessToken());
        String signatureBaseString = getSignatureBaseString(method, baseUrl, collectedParams);
        String signingKey = getSigningKey(credentials.getConsumerSecret(), credentials.getTokenSecret());
        String signature = getHmacSha1(signatureBaseString, signingKey);
        return getOautHeaderString(credentials.getConsumerKey(), signature, credentials.getAccessToken(), nonce, time);
    }

    public static String getOautHeaderString(String consumerKey, String signature, String accessToken, String nonce, int time) {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");
        sb.append("oauth_consumer_key=\""+percentEncode(consumerKey)+"\", ");
        sb.append("oauth_nonce=\""+percentEncode(nonce)+"\", ");
        sb.append("oauth_signature=\""+percentEncode(signature)+"\", ");
        sb.append("oauth_signature_method=\"HMAC-SHA1\", ");
        sb.append("oauth_timestamp=\""+time+"\", ");
        sb.append("oauth_token=\""+percentEncode(accessToken)+"\", ");
        sb.append("oauth_version=\"1.0\"");
        return sb.toString();
    }

    public static String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getSignatureBaseString(String method, String baseUrl, String collectedParams) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.toUpperCase());
        sb.append("&");
        sb.append(percentEncode(baseUrl));
        sb.append("&");
        sb.append(percentEncode(collectedParams));
        return sb.toString();
    }

    public static String getSigningKey(String consumerSecret, String tokenSecret) {
        StringBuilder sb = new StringBuilder();
        sb.append(percentEncode(consumerSecret));
        sb.append("&");
        sb.append(percentEncode(tokenSecret));
        return sb.toString();
    }

    public static String getHmacSha1(String data, String key) {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return toBase64(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String collectOauthParams(Map<String, String> params,
                                            String consumerKey,
                                            String nonce,
                                            int timestamp,
                                            String token) {
        Map<String, String> input = new HashMap<>(params);
        input.put("oauth_consumer_key", consumerKey);
        input.put("oauth_nonce", nonce);
        input.put("oauth_signature_method", "HMAC-SHA1");
        input.put("oauth_timestamp", new Integer(timestamp).toString());
        input.put("oauth_token", token);
        input.put("oauth_version", "1.0");
        SortedSet<String> keys = new TreeSet<>(input.keySet());
        StringBuilder sb = new StringBuilder();
        boolean empty = true;
        for (String key : keys) {
            if (!empty) {
                sb.append("&");
            }
            sb.append(percentEncode(key) + "=" + percentEncode(input.get(key)));
            empty = false;
        }
        return sb.toString();
    }

    private static String toBase64(byte[] bytes) {
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes);
    }

    public static class Credentials {
        private String consumerKey;
        private String consumerSecret;
        private String accessToken;
        private String tokenSecret;

        public String getConsumerKey() {
            return consumerKey;
        }

        public void setConsumerKey(String consumerKey) {
            this.consumerKey = consumerKey;
        }

        public String getConsumerSecret() {
            return consumerSecret;
        }

        public void setConsumerSecret(String consumerSecret) {
            this.consumerSecret = consumerSecret;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }
    }
}
