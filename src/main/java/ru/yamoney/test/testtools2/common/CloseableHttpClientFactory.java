package ru.yamoney.test.testtools2.common;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by onikolenko on 25.10.2014.
 */
public final class CloseableHttpClientFactory {

    public static final Logger LOG = Logger.getLogger(CloseableHttpClientFactory.class.getName());
    private static final CloseableHttpClientFactory INSTANCE = new CloseableHttpClientFactory();
    private HttpClientBuilder httpClientBuilder;
    private SSLContextBuilder sslContextBuilder;

    private CloseableHttpClientFactory() {
        httpClientBuilder = HttpClients.custom();
        sslContextBuilder = SSLContexts.custom();
        setConnectionManager();
    }

    public static CloseableHttpClientFactory getInstance() {
        return INSTANCE;
    }

    private void setConnectionManager() {
        Registry<ConnectionSocketFactory> registrySocketFactory = getRegistrySocketFactory();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(registrySocketFactory);
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(10);
        httpClientBuilder.setConnectionManager(connectionManager);
    }

    private SSLConnectionSocketFactory getSSLContext() {

        SSLContext sslContext;
        try {
            sslContext = sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Exception e) {
            LOG.error("Мы не должны получать исключения NoSuchAlgorithmException и KeyStoreException", e);
            throw new RuntimeException(e);
        }

        return new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    private Registry<ConnectionSocketFactory> getRegistrySocketFactory() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", getSSLContext())
                .build();
    }

    public CloseableHttpClient createHttpClient() {
        return httpClientBuilder.build();
    }

}
