package twitter_IF25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class test_api {

	public static void main(String[] args) throws IOException {
		String baseURL="https://sb-ssl.google.com/safebrowsing/api/lookup";

		String arguments = "";
		arguments+=URLEncoder.encode("client", "UTF-8") + "=" + URLEncoder.encode("demo-app", "UTF-8") + "&";
		arguments+=URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("AIzaSyADggeR7F772IOfbo06hHce3Z8ZqiX4bZY", "UTF-8") + "&";
		arguments+=URLEncoder.encode("appver", "UTF-8") + "=" + URLEncoder.encode("1.5.2", "UTF-8") + "&";
		arguments+=URLEncoder.encode("pver", "UTF-8") + "=" + URLEncoder.encode("3.1", "UTF-8")+ "&";
		arguments+=URLEncoder.encode("url", "UTF-8")+"="+URLEncoder.encode("http://t.co/7qwq4Q568f", "UTF-8");

		// Construct the url object representing cgi script
		URL url = new URL(baseURL + "?" + arguments);

		// Get a URLConnection object, to write to POST method
		URLConnection connect = url.openConnection();
		//String code = (String) connect.getContentEncoding();
		InputStreamReader in = new InputStreamReader((InputStream) connect.getContent());
	    BufferedReader buff = new BufferedReader(in);
	    String line;
	    StringBuffer text = new StringBuffer();
	    boolean is_malware=false;
	    do {
	      line = buff.readLine();
	      text.append(line + "\n");
	      if (line=="malware"){
	    	  is_malware=true;
	      }
	    	  
	    } while (line != null);
	    if (is_malware){
	    	System.out.println("hehe");
	    }
	}

}
