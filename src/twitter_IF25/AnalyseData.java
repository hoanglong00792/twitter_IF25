package twitter_IF25;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import static java.util.concurrent.TimeUnit.SECONDS;

public class AnalyseData {
	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");

		DBCollection coll_tweets = db.getCollection("tweets");
		// BasicDBObject query = new BasicDBObject("user.screen_name",
		// "JackiePollaert");
		DBCollection coll_users = db.getCollection("users");
		BasicDBObject notQuery = new BasicDBObject();
		notQuery.put("read", new BasicDBObject("$exists", false));
		DBCursor cursor = coll_tweets.find(notQuery);

		try {
			while (cursor.hasNext()) {
				BasicDBObject tweet = (BasicDBObject) cursor.next();

				BasicDBObject user = (BasicDBObject) tweet.get("user");

				// =============================================================================
				// ==============================Agressiveness==================================
				// =============================================================================

				// String tweet_created_at = (String)
				// tweet.get("created_at");
				String user_created_at = (String) user.get("created_at");
				double friends_count = (double) user.get("friends_count");
				// Double followers_count = (Double)
				// user.get("followers_count");
				double tweets_count = (double) user.get("statuses_count");

				DateFormat inputFormat = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
				inputFormat.setLenient(true);
				double diff = 1;
				try {
					Date user_date = inputFormat.parse(user_created_at);
					Date now_date = new Date();
					// System.out.println(now_date + "-" + user_created_at +
					// ":");
					diff = (double) (now_date.getTime() - user_date.getTime());
					diff = diff / 3600000;

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Double agressiveness = ((tweets_count + friends_count) / diff) / 350;
				// System.out.println(tweets_count);
				// System.out.println(friends_count);
				// System.out.println(diff);
				// System.out.println(agressiveness);

				// ============================================================================
				// ==============================VISIBILITY====================================
				// ============================================================================

				String text = (String) tweet.get("text");
				int count_hashtag = StringUtils.countMatches(text, "#");
				int count_mention = StringUtils.countMatches(text, "@");
				int count_tweet_analysed = 1;

				String id_user = (String) user.get("id_str");
				DBCursor cursor_user = coll_users.find(new BasicDBObject(
						"id_str", id_user));
				try {
					while (cursor_user.hasNext()) {
						BasicDBObject user_old = (BasicDBObject) cursor_user
								.next();
						count_tweet_analysed = count_tweet_analysed
								+ (int) user_old.get("count_tweet_analysed");
						count_mention = count_mention
								+ (int) user_old.get("count_mention");
						count_hashtag = count_hashtag
								+ (int) user_old.get("count_hashtag");
						System.out.println("+++++++++++++++++" + user_old);
						coll_users.remove(user_old);
					}
				} finally {
					cursor_user.close();
				}
				// String screen_name = (String) user.get("screen_name");

				double visibility = (count_hashtag / count_tweet_analysed
						* 11.6 + count_mention / count_tweet_analysed * 11.4) / 140;
				System.out.println(visibility + "**" + count_hashtag + "-"
						+ count_mention + ":	" + text);

				double danger = 0;
				BasicDBObject doc = user.append("agressiveness", agressiveness)
						.append("visibility", visibility)
						.append("danger", danger)
						.append("agressiveness", agressiveness)
						.append("count_hashtag", count_hashtag)
						.append("count_mention", count_mention)
						.append("count_tweet_analysed", count_tweet_analysed);
				coll_users.insert(doc);
				//String id_tweet = (String) tweet.get("_id");
//				BasicDBObject searchQuery = new BasicDBObject().append(
//						"_id", id_tweet);
				tweet.append("read", 1);
				coll_tweets.save( tweet);

			}
		} finally {
			cursor.close();
		}

	}
}