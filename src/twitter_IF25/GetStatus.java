package twitter_IF25;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang.StringEscapeUtils;


import twitter4j.*;



public class GetStatus {
	public static void write(String text) throws IOException {
		BufferedWriter out = null;

		FileWriter fstream = new FileWriter("insert.sql", true); // true tells
																	// to append
																	// data.
		out = new BufferedWriter(fstream);
		try {
			out.write(text+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
	}

	public static void main(String[] args) throws TwitterException {
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		StatusListener listener = new StatusListener() {

			@Override
			public void onStatus(Status status) {
				String sql1 = "INSERT INTO Status(Id,PersonId2,Created_at, Text, Geo, Place)"
						+ " VALUES ( "
						+ status.getId()
						+ ","
						+ status.getUser().getId()
						+ ","
						+ "'"
						+ status.getCreatedAt()
						+ "',"
						+"'"+StringEscapeUtils.escapeSql(status.getText()) + "',"
						+ status.getGeoLocation()
						+ ","
						+ status.getPlace() + ");";
				System.out.println(sql1);
				try {
					GetStatus.write(sql1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (status.getRateLimitStatus() != null) {
					System.out
							.println("status.getRateLimitStatus().getLimit() = "
									+ status.getRateLimitStatus().getLimit());
					System.out
							.println("status.getRateLimitStatus().getRemaining() = "
									+ status.getRateLimitStatus()
											.getRemaining());
					System.out
							.println("status.getRateLimitStatus().getResetTimeInSeconds() = "
									+ status.getRateLimitStatus()
											.getResetTimeInSeconds());
					System.out
							.println("status.getRateLimitStatus().getSecondsUntilReset() = "
									+ status.getRateLimitStatus()
											.getSecondsUntilReset());
					// System.out.println("status.getRateLimitStatus().getRemainingHits() = "
					// +
					// status.getRateLimitStatus().getRemainingHits());
				}
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
	}
}
