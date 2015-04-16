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

public class Visualisation {
	public double[] agressiveness, visibility, danger, count_mention,count_hashtag,count_malware_link,friends_count,followers_count;
	public int count_user;

	public void getVisualisation() {
		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");
		DBCollection coll_users = db.getCollection("users");
		DBCursor cursor_user = coll_users.find();
		int count = 0;
		count_user = (int) coll_users.getCount();
		agressiveness = new double[count_user];
		visibility = new double[count_user];
		danger = new double[count_user];
		count_mention = new double[count_user];
		count_hashtag = new double[count_user];
		count_malware_link = new double[count_user];
		friends_count = new double[count_user];
		followers_count = new double[count_user];
		System.out.println(count_user);
		try {
			while (cursor_user.hasNext()) {
				BasicDBObject user = (BasicDBObject) cursor_user.next();
				// System.out.println(count+"a"+user.toString());
				agressiveness[count] = (double) user.get("agressiveness");
				visibility[count] = (double) user.get("visibility");
				danger[count] = (double) user.get("danger");
				count_mention[count] = (double) ((int)user.get("count_mention"));
				count_hashtag[count] = (double) ((int)user.get("count_hashtag"));
				count_malware_link[count] = (double) ((int)user.get("count_malware_link"));
				friends_count[count] = (double) ((int)user.get("friends_count"));
				followers_count[count] = (double) ((int)user.get("followers_count"));
				//System.out.println(followers_count[count]);
				count++;
			}
		} finally {
			cursor_user.close();
		}
	}

	public static void main(String[] args) {
		Visualisation visualisation = new Visualisation();
		visualisation.getVisualisation();
	}
}