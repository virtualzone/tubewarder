package net.weweave.tubewarder.outputhandler;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestTwitterOauth {
    @Test
    public void testPercentEncode() {
        String input = "Hello Ladies + Gentlemen, a signed OAuth request!";
        String expected = "Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21";
        Assert.assertEquals(expected, TwitterOauth.percentEncode(input));
    }

    @Test
    public void testCalcSignature() {
        String signatureBaseString = "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
        String signingKey = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
        String expected = "tnnArxj06cWHq44gCs1OSKk/jLY=";
        Assert.assertEquals(expected, TwitterOauth.getHmacSha1(signatureBaseString, signingKey));
    }

    @Test
    public void testCollectOauthParams() {
        Map<String, String> params = new HashMap<>();
        params.put("status", "Hello Ladies + Gentlemen, a signed OAuth request!");
        params.put("include_entities", "true");
        String result = TwitterOauth.collectOauthParams(params,
                "xvz1evFS4wEEPTGEFPHBog",
                "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
                1318622958,
                "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb");
        String expected = "include_entities=true&oauth_consumer_key=xvz1evFS4wEEPTGEFPHBog&oauth_nonce=kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1318622958&oauth_token=370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb&oauth_version=1.0&status=Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOauthHeaderString() {
        String consumerKey = "xvz1evFS4wEEPTGEFPHBog";
        String nonce = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg";
        String signature = "tnnArxj06cWHq44gCs1OSKk/jLY=";
        int time = 1318622958;
        String token = "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb";
        String expected = "OAuth oauth_consumer_key=\"xvz1evFS4wEEPTGEFPHBog\", oauth_nonce=\"kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg\", oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1318622958\", oauth_token=\"370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb\", oauth_version=\"1.0\"";
        Assert.assertEquals(expected, TwitterOauth.getOautHeaderString(consumerKey, signature, token, nonce, time));
    }

    @Test
    public void testCreateSignatureBaseString() {
        String method = "post";
        String baseUrl =  "https://api.twitter.com/1/statuses/update.json";
        String collectedParams = "include_entities=true&oauth_consumer_key=xvz1evFS4wEEPTGEFPHBog&oauth_nonce=kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1318622958&oauth_token=370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb&oauth_version=1.0&status=Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21";
        String expected = "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
        Assert.assertEquals(expected, TwitterOauth.getSignatureBaseString(method, baseUrl, collectedParams));
    }

    @Test
    public void testCreateSigningKey() {
        String consumerSecret = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw";
        String tokenSecret = "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
        String expected = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
        Assert.assertEquals(expected, TwitterOauth.getSigningKey(consumerSecret, tokenSecret));
    }

    @Test
    public void testOauthHeaderStringComplete() {
        TwitterOauth.Credentials credentials = new TwitterOauth.Credentials();
        credentials.setConsumerKey("xvz1evFS4wEEPTGEFPHBog");
        credentials.setConsumerSecret("kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw");
        credentials.setAccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb");
        credentials.setTokenSecret("LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE");

        String method = "post";
        String baseUrl = "https://api.twitter.com/1/statuses/update.json";
        String nonce = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg";
        int time = 1318622958;

        Map<String, String> params = new HashMap<>();
        params.put("status", "Hello Ladies + Gentlemen, a signed OAuth request!");
        params.put("include_entities", "true");

        String header = TwitterOauth.getOautHeaderString(credentials, method, baseUrl, params, nonce, time);
        String expected = "OAuth oauth_consumer_key=\"xvz1evFS4wEEPTGEFPHBog\", oauth_nonce=\"kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg\", oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1318622958\", oauth_token=\"370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb\", oauth_version=\"1.0\"";
    }
}
