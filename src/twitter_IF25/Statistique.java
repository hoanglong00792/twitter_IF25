package twitter_IF25;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

import org.apache.commons.lang.StringUtils;


public class Statistique  {
	public int count,count_user,count_analysed,count_spam,count_verified;
	public void getStatistique() {
		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");

		DBCollection coll_tweets = db.getCollection("tweets");
		// BasicDBObject query = new BasicDBObject("user.screen_name",
		// "JackiePollaert");
		DBCollection coll_users = db.getCollection("users");
		BasicDBObject notQuery = new BasicDBObject();
		notQuery.put("read", new BasicDBObject("$exists", true));
		count=(int)coll_tweets.getCount();
		count_user=(int)coll_users.getCount();
		count_analysed=(int)coll_tweets.getCount(notQuery);
		BasicDBObject spamQuery = new BasicDBObject();
		spamQuery.append("user_class",-1);
		count_spam=(int)coll_users.getCount(spamQuery);
		BasicDBObject verifiedQuery = new BasicDBObject();
		verifiedQuery.append("user_class",1);
		count_verified=(int)coll_users.getCount(verifiedQuery);
		System.out.println(count+"-"+count_analysed+"-"+count_user+"-"+count_spam+"-"+count_verified);
		
	}	
	public static void main(String[] args) throws IOException {
		Statistique analyse = new Statistique();
		analyse.getStatistique();
	}
}