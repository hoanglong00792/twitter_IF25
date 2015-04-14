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
	public double[] agressiveness, visibility, danger;

	public void getVisualisation() {
		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");
		DBCollection coll_users = db.getCollection("users");
		DBCursor cursor_user = coll_users.find();
		int count = 0;
		int count_user=(int)coll_users.getCount();
		agressiveness=new double[count_user];
		visibility=new double[count_user];
		danger=new double[count_user];
		try {
			while (cursor_user.hasNext()) {
				count++;
				BasicDBObject user = (BasicDBObject) cursor_user.next();
				System.out.println(count+"a"+user.toString());
				if (user.get("agressiveness") == null) {
					agressiveness[count] = 0;
				} else {					
					agressiveness[count] = (double) user.get("agressiveness");
				}
				if (user.get("visibility") == null) {
					visibility[count] = 0;
				} else {
					visibility[count] = (double) user.get("visibility");
				}
				if (user.get("danger") == null) {
					danger[count] = 0;
				} else {
					danger[count] = (double) user.get("danger");
				}
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