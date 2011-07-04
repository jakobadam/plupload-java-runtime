package plupload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUploader {

	private int retries;
	private String cookie;
	private HttpClient httpclient;

	public HttpUploader(int chunk_retries, String cookie){
		this.httpclient = HttpUtil.getHttpClient();
		this.retries = chunk_retries;
		this.cookie = cookie;
	}

	public int sendChunk(byte[] data, int len, int chunk, long chunks,
			String name, URI uri) throws NoSuchAlgorithmException, URISyntaxException,
			ClientProtocolException, IOException {
		InputStreamEntity entity = new InputStreamEntity(
				new ByteArrayInputStream(data), len);
		entity.setContentType("application/octet-stream");

		HttpPost httppost = new HttpPost(uri);
		httppost.setEntity(entity);
		httppost.addHeader("Cookie", cookie);
		HttpResponse response = httpclient.execute(httppost);

		HttpEntity resEntity = response.getEntity();
		int status_code = response.getStatusLine().getStatusCode();

		String body = EntityUtils.toString(resEntity);

		if (status_code != 200) {
			if (retries > 0) {
				retries--;
				sendChunk(data, len, chunk, chunks, name, uri);
			} else {
				throw new IOException("Exception uploading to server: " + body);
			}
		}
		return status_code;
	}

	public Map<String, String> probe(URI uri) throws ClientProtocolException, IOException, ParseException{
		HttpGet get = new HttpGet(uri);
		get.addHeader("Cookie", cookie);

		HttpResponse response = httpclient.execute(get);
		String body = HttpUtil.toString(response.getEntity());
		if(response.getStatusLine().getStatusCode() != 200){
			System.err.println("Exception probing server: " + body);
			throw new IOException("Exception probing server: see java console log");
		}
		return HttpUtil.parse_qs(body);
	}

	public static String getQueryParams(long chunk, long chunks, int chunk_size, String md5hex_total,
			String md5hex_chunk, String name) {
		List<NameValuePair> q = new ArrayList<NameValuePair>();
		q.add(new BasicNameValuePair("chunk", Long.toString(chunk)));
		q.add(new BasicNameValuePair("chunks", Long.toString(chunks)));
		q.add(new BasicNameValuePair("chunk_size", Integer.toString(chunk_size)));
		q.add(new BasicNameValuePair("md5chunk", md5hex_chunk));
		q.add(new BasicNameValuePair("md5total", md5hex_total));
		q.add(new BasicNameValuePair("name", name));
		return URLEncodedUtils.format(q, "UTF-8");
	}
}
