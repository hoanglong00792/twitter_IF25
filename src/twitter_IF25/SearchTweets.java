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
import com.mongodb.util.JSON;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.util.List;
import java.util.Map;
import java.util.Observable;

public class SearchTweets extends Observable implements Runnable {

	public String search_key;

	public static void main(String[] args) {
		SearchTweets search_tweet = new SearchTweets(args[0]);
		search_tweet.run();
	}

	public SearchTweets(String search_key) {
		this.search_key = search_key;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String search_key = this.search_key;
		if (search_key == null || search_key == "") {
			System.exit(0);
		} else {

			MongoClient mongoClient = new MongoClient("localhost");

			DB db = mongoClient.getDB("tweetdata");

			final DBCollection coll_tweets = db.getCollection("tweets");

			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setJSONStoreEnabled(true);

			Twitter twitter = new TwitterFactory(cb.build()).getInstance();

			try {
				Query query = new Query(search_key);
				QueryResult result;
				do {
					/*
					 * if (twitter.getRateLimitStatus() != null) { try {
					 * Thread.sleep(twitter.getRateLimitStatus()
					 * .getSecondsUntilReset()); } catch (InterruptedException
					 * e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); }
					 * 
					 * }
					 */
					Map<String, RateLimitStatus> rateLimitStatus = twitter
							.getRateLimitStatus();
					RateLimitStatus status = rateLimitStatus
							.get("/search/tweets");
					System.out.println(" Limit: " + status.getLimit());
					System.out.println(" Remaining: " + status.getRemaining());
					System.out.println(" ResetTimeInSeconds: "
							+ status.getResetTimeInSeconds());
					System.out.println(" SecondsUntilReset: "
							+ status.getSecondsUntilReset());
					if (status.getRemaining() < 10) {
						try {
							Thread.sleep(status.getSecondsUntilReset() * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					result = twitter.search(query);
					List<Status> tweets = result.getTweets();
					for (Status tweet : tweets) {
						// System.out.println("@" +
						// tweet.getUser().getScreenName()
						// + " - " + tweet.getText());

						System.out.println(TwitterObjectFactory
								.getRawJSON(tweet));
						// try {
						// SearchTweets.write(TERM+".json",
						// TwitterObjectFactory.getRawJSON(tweet));
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						DBObject dbObject = (DBObject) JSON
								.parse(TwitterObjectFactory.getRawJSON(tweet));

						coll_tweets.insert(dbObject);

					}
				} while ((query = result.nextQuery()) != null);
				//System.exit(0);
			} catch (TwitterException te) {
				try {
					Thread.sleep(1000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
