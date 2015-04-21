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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchTweets extends Observable implements Runnable {

	public String search_key;
	public boolean is_train;
	public int count_users = 0;

	public static void main(String[] args) {
		SearchTweets search_tweet = new SearchTweets(args[0], false);
		search_tweet.run();
	}

	public SearchTweets(String search_key, boolean is_train) {
		this.search_key = search_key;
		this.is_train = is_train;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String search_key = this.search_key;
		if (search_key == null || search_key == "") {
			System.exit(0);
		} else {

			// MongoClient mongoClient = new MongoClient("localhost");
			//
			// DB db = mongoClient.getDB("tweetdata");
			//
			// final DBCollection coll_tweets = db.getCollection("tweets");

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
					
					RateLimitStatus rate = rateLimitStatus
							.get("/application/rate_limit_status");
					System.out.println(" Limit for application/rate_limit_status: " + rate.getLimit());
					System.out.println(" Remaining: " + rate.getRemaining());
					System.out.println(" ResetTimeInSeconds: "
							+ rate.getResetTimeInSeconds());
					System.out.println(" SecondsUntilReset: "
							+ rate.getSecondsUntilReset());
					if (rate.getRemaining() < 10) {
						try {
							Thread.sleep(rate.getSecondsUntilReset() * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
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

						// System.out.println(TwitterObjectFactory
						// .getRawJSON(tweet));
						// try {
						// SearchTweets.write(TERM+".json",
						// TwitterObjectFactory.getRawJSON(tweet));
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						// DBObject dbObject = (DBObject) JSON
						// .parse(TwitterObjectFactory
						// .getRawJSON(tweet));
						// System.out.println(dbObject.toString());
						// coll_tweets.insert(dbObject);
						if (is_train) {
							// DBCollection coll_tweets_train =
							// db.getCollection("tweets_train");
							// int
							// count_users=(int)coll_tweets_train.getCount();
							if (count_users > 500) {
								if (tweet.getUser().isVerified()) {
									AnalyseData analyse_spam = new AnalyseData(
											2);
									Thread thread_analyse_spam = new Thread(
											analyse_spam);
									thread_analyse_spam.start();
								} else {
									AnalyseData analyse_verified = new AnalyseData(
											1);
									Thread thread_analyse_verified = new Thread(
											analyse_verified);
									thread_analyse_verified.start();
								}
								Thread.currentThread().stop();
							}
							if (tweet.getUser().isVerified()) {
								GetUserTimeline user_timeline_train = new GetUserTimeline(
										tweet.getUser().getScreenName(), 1);
								user_timeline_train.run();
								GetUserTimeline user_timeline_main = new GetUserTimeline(
										tweet.getUser().getScreenName(), 0);
								user_timeline_main.run();
								count_users++;
							} else {
								Pattern pattern = Pattern
										.compile("@spam ([a-zA-Z0-9]*)$");
								Matcher matcher = pattern.matcher(tweet
										.getText());
								if (matcher.find()) {
									String screen_name = matcher.group(1);
									if (screen_name != null) {
										System.out.println(screen_name);
										GetUserTimeline user_timeline = new GetUserTimeline(
												screen_name, 2);
										user_timeline.run();
										GetUserTimeline user_timeline_main = new GetUserTimeline(
												screen_name, 0);
										user_timeline_main.run();
										count_users++;
									}
								}
							}
						} else {
							GetUserTimeline user_timeline = new GetUserTimeline(
									tweet.getUser().getScreenName(), 0);
							user_timeline.run();
						}
					}
				} while ((query = result.nextQuery()) != null);
				// System.exit(0);
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
