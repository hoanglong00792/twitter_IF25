package twitter_IF25;

import java.util.Observable;

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

public class StreamTweets extends Observable implements Runnable {
	public static void main(String[] args) {
		StreamTweets stream_tweet = new StreamTweets(true);
		stream_tweet.run();
	}
	public boolean is_running=true;
	public int count=0;
	public StreamTweets(boolean is_running) {
		this.is_running = is_running;
	}
	@Override
	public void run() {

		MongoClient mongoClient = new MongoClient("localhost");

		DB db = mongoClient.getDB("tweetdata");

		final DBCollection coll_tweets = db.getCollection("tweets");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setJSONStoreEnabled(true);

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status tweet) {
				System.out.println(TwitterObjectFactory.getRawJSON(tweet));
				// StreamTweets.write(TERM + ".json",
				// TwitterObjectFactory.getRawJSON(status));
				DBObject dbObject = (DBObject) JSON.parse(TwitterObjectFactory
						.getRawJSON(tweet));

				coll_tweets.insert(dbObject);
			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				// System.out.println("Got a status deletion notice id:" +
				// statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// System.out.println("Got track limitation notice:" +
				// numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				// System.out.println("Got scrub_geo event userId:" + userId +
				// " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				// System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		twitterStream.addListener(listener);
		twitterStream.sample();
		if (is_running==false){
			System.out.println("STOPPPPPPPPP");
			twitterStream.cleanUp(); 
			twitterStream.shutdown();
		}
	}
}
