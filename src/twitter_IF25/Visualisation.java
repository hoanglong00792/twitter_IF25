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
	public double[] agressiveness, visibility, danger, mention_per_tweet,
			hashtag_per_tweet, malware_link_per_tweet, friends_count,
			followers_count, user_class;
	public int count_user;
	public int group_id;
	public String[] user_id;
	public DBCollection coll_users;

	public Visualisation(int group_id) {
		this.group_id = group_id;
	}

	public void getVisualisation() {
		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");
		BasicDBObject query = new BasicDBObject();
		if (group_id == 1) {
			coll_users = db.getCollection("users_train_verified");
		} else if (group_id == 2) {
			coll_users = db.getCollection("users_train_spam");
		} else if (group_id == 3) {
			coll_users = db.getCollection("users");
			query.append("user_class", 1);
		} else if (group_id == 4) {
			coll_users = db.getCollection("users");
			query.append("user_class", -1);
		} else {
			coll_users = db.getCollection("users");
		}
		DBCursor cursor_user = coll_users.find(query);

		int count = 0;
		count_user = (int) coll_users.getCount();
		agressiveness = new double[count_user];
		visibility = new double[count_user];
		danger = new double[count_user];
		mention_per_tweet = new double[count_user];
		hashtag_per_tweet = new double[count_user];
		malware_link_per_tweet = new double[count_user];
		friends_count = new double[count_user];
		followers_count = new double[count_user];
		user_class = new double[count_user];
		user_id = new String[count_user];

		System.out.println(count_user);
		try {
			while (cursor_user.hasNext()) {
				BasicDBObject user = (BasicDBObject) cursor_user.next();
				// System.out.println(count+"a"+user.toString());
				agressiveness[count] = (double) user.get("agressiveness");
				visibility[count] = (double) user.get("visibility");
				danger[count] = (double) user.get("danger");
				mention_per_tweet[count] = (double) ((int) user
						.get("count_mention"))
						/ ((int) user.get("count_tweet_analysed"));
				hashtag_per_tweet[count] = (double) ((int) user
						.get("count_hashtag"))
						/ ((int) user.get("count_tweet_analysed"));
				malware_link_per_tweet[count] = (double) ((int) user
						.get("count_malware_link"))
						/ ((int) user.get("count_tweet_analysed"));
				friends_count[count] = (double) ((int) user
						.get("friends_count"));
				followers_count[count] = (double) ((int) user
						.get("followers_count"));
				user_class[count] = (double) ((int) user.get("user_class"));
				user_id[count] = (String) user.get("id_str");
				// System.out.println(followers_count[count]);
				count++;
			}
		} finally {
			cursor_user.close();
		}
	}

	public static void main(String[] args) {
		Visualisation visualisation = new Visualisation(0);
		visualisation.getVisualisation();
	}
}