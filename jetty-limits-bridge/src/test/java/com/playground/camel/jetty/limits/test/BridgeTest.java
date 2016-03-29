package com.playground.camel.jetty.limits.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class BridgeTest {

    private static final Logger LOG = LoggerFactory.getLogger(BridgeTest.class);

    private static Properties properties;

    private static final String LARGE_HEADER_VALUE = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. "
            + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley "
            + "of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap "
            + "into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of "
            + "Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus "
            + "PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting "
            + "industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer "
            + "took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, "
            + "but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s "
            + "with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
            + "software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing "
            + "and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an "
            + "unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five "
            + "centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the "
            + "1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
            + "software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and "
            + "typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown "
            + "printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, "
            + "but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the "
            + "release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus "
            + "PageMaker including versions of Lorem Ipsum.";

    @BeforeClass
    public static void beforeClass() throws IOException {
        properties = new Properties();
        InputStream is = BridgeTest.class.getClassLoader().getResourceAsStream("bridge-default.properties");
        try {
            properties.load(is);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            is.close();
        }
    }

    @Test
    public void jettyBridgeTest() throws IOException {
        String endpoint = "http://localhost:" + properties.getProperty("jetty.bridge.port") + "/"
                + properties.getProperty("jetty.bridge.contextPath");

        largePostRequest(endpoint);
    }

    @Test
    public void http4BridgeTest() throws IOException {
        String endpoint = "http://localhost:" + properties.getProperty("http4.bridge.port") + "/"
                + properties.getProperty("http4.bridge.contextPath");

        largePostRequest(endpoint);
    }

    private void largePostRequest(String endpoint) throws IOException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("param1", LARGE_HEADER_VALUE));
        nvps.add(new BasicNameValuePair("param2", LARGE_HEADER_VALUE));
        nvps.add(new BasicNameValuePair("param3", LARGE_HEADER_VALUE));

        HttpEntity entity = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setEntity(entity);

        //Set HTTP proxy to capture the request
        HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpPost.setConfig(config);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            LOG.info("" + response.getStatusLine());
            Assert.isTrue(response.getStatusLine().getStatusCode() == 200, "Expected 200 OK!");
            response.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            httpClient.close();
        }
    }
}
