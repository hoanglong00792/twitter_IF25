package twitter_IF25;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

public class AnalyseData extends Observable implements Runnable {
	public boolean is_train;
	public DBCollection coll_tweets;
	public DBCollection coll_users;
	public int group_id;

	public AnalyseData(int group_id) {
		this.group_id = group_id;
	}

	@Override
	public void run() {
		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");
		if (group_id == 1) {
			coll_tweets = db.getCollection("tweets_train_verified");
		} else if (group_id == 2) {
			coll_tweets = db.getCollection("tweets_train_spam");
		} else {
			coll_tweets = db.getCollection("tweets");
		} // BasicDBObject query = new BasicDBObject("user.screen_name",
		// "JackiePollaert");
		if (group_id == 1) {
			coll_users = db.getCollection("users_train_verified");
		} else if (group_id == 2) {
			coll_users = db.getCollection("users_train_spam");
		} else {
			coll_users = db.getCollection("users");
		}
		System.out.println(group_id);
		BasicDBObject notQuery = new BasicDBObject();
		notQuery.put("read", new BasicDBObject("$exists", false));
		DBCursor cursor = coll_tweets.find(notQuery);

		try {
			while (cursor.hasNext()) {
				BasicDBObject tweet = (BasicDBObject) cursor.next();

				BasicDBObject user = (BasicDBObject) tweet.get("user");

				User new_user = new User();
				new_user = AnalyseData.calculAgressiveness(user, tweet,
						new_user);
				new_user = AnalyseData.calculVisibility(user, tweet,
						coll_users, new_user);
				try {
					new_user = AnalyseData.calculDanger(user, tweet, new_user);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BasicDBObject doc = user
						.append("agressiveness", new_user.getAgressiveness())
						.append("visibility", new_user.getVisibility())
						.append("danger", new_user.getDanger())
						.append("count_hashtag", new_user.getCount_hashtag())
						.append("count_mention", new_user.getCount_mention())
						.append("count_malware_link",
								new_user.getCount_malware_link())
						.append("count_tweet_analysed",
								new_user.getCount_tweet_analysed())
						.append("user_class",group_id);
				coll_users.insert(doc);
				tweet.append("read", 1);
				coll_tweets.save(tweet);

			}
		} finally {
			cursor.close();
		}

	}

	public static User calculVisibility(BasicDBObject user,
			BasicDBObject tweet, DBCollection coll_users, User new_user) {
		// ============================================================================
		// ==============================VISIBILITY====================================
		// ============================================================================

		String text = (String) tweet.get("text");
		int count_hashtag = StringUtils.countMatches(text, "#");
		int count_mention = StringUtils.countMatches(text, "@");
		int count_tweet_analysed = 1;
		int count_malware_link = 0;

		String id_user = (String) user.get("id_str");
		DBCursor cursor_user = coll_users.find(new BasicDBObject("id_str",
				id_user));
		try {
			while (cursor_user.hasNext()) {
				BasicDBObject user_old = (BasicDBObject) cursor_user.next();
				count_tweet_analysed = count_tweet_analysed
						+ (int) user_old.get("count_tweet_analysed");
				count_mention = count_mention
						+ (int) user_old.get("count_mention");
				count_hashtag = count_hashtag
						+ (int) user_old.get("count_hashtag");
				count_malware_link = (int) user_old.get("count_malware_link");
				// System.out.println("+++++++++++++++++" + user_old);
				coll_users.remove(user_old);
			}
		} finally {
			cursor_user.close();
		}
		// String screen_name = (String) user.get("screen_name");

		double visibility = (count_hashtag / count_tweet_analysed * 11.6 + count_mention
				/ count_tweet_analysed * 11.4) / 140;
		// System.out.println(visibility + "**" + count_hashtag + "-"
		// + count_mention + ":	" + text);

		new_user.setVisibility(visibility);
		new_user.setCount_hashtag(count_hashtag);
		new_user.setCount_mention(count_mention);
		new_user.setCount_tweet_analysed(count_tweet_analysed);
		new_user.setCount_malware_link(count_malware_link);

		return new_user;

	}

	public static User calculDanger(BasicDBObject user, BasicDBObject tweet,
			User new_user) throws IOException {
		double danger = 0;
		boolean is_malware = false;
		int count_malware_link = new_user.getCount_malware_link();
		/*
		 * BasicDBObject entities = (BasicDBObject) tweet.get("entities");
		 * BasicDBList list_urls = (BasicDBList) entities.get("urls"); for
		 * (Object object : list_urls) { DBObject embedded = (DBObject) object;
		 * System.out.println(embedded.get("expanded_url")); String link =
		 * (String) embedded.get("expanded_url"); // link =
		 * "http://ianfette.org"; if (AnalyseData.check_link(link)) { is_malware
		 * = true; } } if (is_malware) { count_malware_link++; }
		 */
		int count_tweet_analysed = new_user.getCount_tweet_analysed();
		danger = count_malware_link / count_tweet_analysed;
		new_user.setDanger(danger);
		return new_user;
	}

	public static User calculAgressiveness(BasicDBObject user,
			BasicDBObject tweet, User new_user) {
		// =============================================================================
		// ==============================Agressiveness==================================
		// =============================================================================

		String user_created_at = (String) user.get("created_at");
		int friends_count = (int) user.get("friends_count");
		int followers_count = (int) user.get("followers_count");
		int tweets_count = (int) user.get("statuses_count");

		DateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
		inputFormat.setLenient(true);
		double diff = 1;
		try {
			Date user_date = inputFormat.parse(user_created_at);
			Date now_date = new Date();
			diff = (double) (now_date.getTime() - user_date.getTime());
			diff = diff / 3600000;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Double agressiveness = ((tweets_count + friends_count) / diff) / 350;
		new_user.setAgressiveness(agressiveness);
		new_user.setFollowers_count(followers_count);
		new_user.setFriends_count(friends_count);
		return new_user;
	}

	public static String[] increaseArray(String[] theArray, String string) {
		int i = theArray.length;
		int n = ++i;
		String[] newArray = new String[n];
		for (int cnt = 0; cnt < theArray.length; cnt++) {
			newArray[cnt] = theArray[cnt];
		}
		return newArray;
	}

	public static boolean check_link(String link) throws IOException {

		String baseURL = "https://sb-ssl.google.com/safebrowsing/api/lookup";

		String arguments = "";
		arguments += URLEncoder.encode("client", "UTF-8") + "="
				+ URLEncoder.encode("demo-app", "UTF-8") + "&";
		arguments += URLEncoder.encode("key", "UTF-8")
				+ "="
				+ URLEncoder.encode("AIzaSyADggeR7F772IOfbo06hHce3Z8ZqiX4bZY",
						"UTF-8") + "&";
		arguments += URLEncoder.encode("appver", "UTF-8") + "="
				+ URLEncoder.encode("1.5.2", "UTF-8") + "&";
		arguments += URLEncoder.encode("pver", "UTF-8") + "="
				+ URLEncoder.encode("3.1", "UTF-8") + "&";
		arguments += URLEncoder.encode("url", "UTF-8") + "="
				+ URLEncoder.encode(link, "UTF-8");

		// Construct the url object representing cgi script
		URL url = new URL(baseURL + "?" + arguments);

		// Get a URLConnection object, to write to POST method
		URLConnection connect = url.openConnection();
		// String code = (String) connect.getContentEncoding();
		InputStreamReader in = new InputStreamReader(
				(InputStream) connect.getContent());
		BufferedReader buff = new BufferedReader(in);
		String line;
		StringBuffer text = new StringBuffer();
		boolean is_malware = false;
		do {
			line = buff.readLine();
			text.append(line + "\n");
			if (line != null) {
				if (line.equals("malware")) {
					is_malware = true;
				}
			}

		} while (line != null);
		if (is_malware) {
			System.out
					.println("===========================MALWARE==============================");
		}
		return is_malware;
	}


	public static void main(String[] args) throws IOException {
		AnalyseData analyse = new AnalyseData(0);
		analyse.run();
	}
}