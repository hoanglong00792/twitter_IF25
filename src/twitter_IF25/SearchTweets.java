package twitter_IF25;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SearchTweets {
	BufferedWriter OutFileWriter;

	static final String TERM = "free";

	public static void write(String name, String text) throws IOException {
		BufferedWriter out = null;

		FileWriter fstream = new FileWriter(name, true); // true tells
															// to append
															// data.
		out = new BufferedWriter(fstream);
		try {
			out.write(text + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
	}

	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setJSONStoreEnabled(true);

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();

		try {
			Query query = new Query(TERM);
			QueryResult result;
			do {
				/*
				 * if (twitter.getRateLimitStatus() != null) { try {
				 * Thread.sleep(twitter.getRateLimitStatus()
				 * .getSecondsUntilReset()); } catch (InterruptedException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 * 
				 * }
				 */
				Map<String, RateLimitStatus> rateLimitStatus = twitter
						.getRateLimitStatus();
				RateLimitStatus status = rateLimitStatus.get("/search/tweets");
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
					// System.out.println("@" + tweet.getUser().getScreenName()
					// + " - " + tweet.getText());

					System.out.println(TwitterObjectFactory.getRawJSON(tweet));
					try {
						SearchTweets.write(TERM+".json",
								TwitterObjectFactory.getRawJSON(tweet));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} while ((query = result.nextQuery()) != null);
			System.exit(0);
		} catch (TwitterException te) {
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//te.printStackTrace();
			//System.out.println("Failed to search tweets: " + te.getMessage());

		}
	}
}
