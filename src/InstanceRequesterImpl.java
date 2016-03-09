import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt.eglin on 26/02/2016.
 */
public class InstanceRequesterImpl {

    CloseableHttpClient httpclient;
    String jiveURL;
    Credentials credentials = new Credentials();

    public InstanceRequesterImpl() {


    }


    private void test_request() {

        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://172.16.5.133:8080/welcome");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
// The underlying HTTP connection is still held by the response object
// to allow the response content to be streamed directly from the network socket.
// In order to ensure correct deallocation of system resources
// the user MUST call CloseableHttpResponse#close() from a finally clause.
// Please note that if response content is not fully consumed the underlying
// connection cannot be safely re-used and will be shut down and discarded
// by the connection manager.
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            }
            finally {
                response1.close();
            }

            HttpPost httpPost = new HttpPost("http://172.16.5.133:8080/login.jspa");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", "admin"));
            nvps.add(new BasicNameValuePair("password", "admin"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            }
            finally {
                response2.close();
            }

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public void buildRequest() {

        String requestURLbase = jiveURL + "/api/core/v3/contents"; //Events are created from the events API endpoint

       // requestURLbase + ""

        HttpPost postRequest = new HttpPost(requestURLbase);

    }

    private void sendPostRequest(HttpPost postRequest) {

        try {


            CloseableHttpClient httpclient = HttpClients.createDefault();

            CloseableHttpResponse postResponse = httpclient.execute(postRequest);

            try {
                System.out.println(postResponse.getStatusLine());
                if (postResponse.getStatusLine().getStatusCode() != 200) {
                    //failed
                }
            }
            finally {

                postResponse.close();
            }

        }
        catch (IOException ioe) {

            System.out.println("Error POST-ing request : " +ioe.getMessage());

        }
    }
}
