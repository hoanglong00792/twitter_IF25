package twitter_IF25;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringEscapeUtils;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Sql_connect {
	static int stop = 0;
	static int connect = 0;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/IF25";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";

	public static void main(String[] args) {
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		StatusListener listener = new StatusListener() {

			@Override
			public void onStatus(Status status) {

				Connection conn = null;
				Statement stmt = null;

				try {
					if (stop != 1) {
						stop = 1;

						// STEP 2: Register JDBC driver
						if (connect != 1) {
							Class.forName("com.mysql.jdbc.Driver");

							// STEP 3: Open a connection
							System.out.println("Connecting to    database...");
							conn = DriverManager.getConnection(DB_URL, USER,
									PASS);
							// no need to display the fields... directly insert
							// them and work on it
							// System.out.println(status);
							// EDIT THIS
							System.out
									.println("Inserting records into the table...");
							stmt = conn.createStatement();
							connect = 1;
						}
						/*
						 * String sql1 =
						 * "INSERT INTO tweet(TweetID,Product,sub_product,TweetDate,"
						 * +
						 * "Tweetmsg,tweetsource,isTruncated,inReplyToStatusId,"
						 * +
						 * "inReplyToUserId,isFavorited,isRetweeted,favoriteCount,"
						 * +
						 * "inReplyToScreenName,geoLocation, place,retweetCount,isPossiblySensitive,"
						 * +
						 * "isoLanguageCode,lang ,retweetedStatus,hashtagEntities,currentUserRetweetId,"
						 * +
						 * "userid,username,screenName,userlocation,userdescription,isContributorsEnabled,"
						 * +
						 * "profileImageUrl,profileImageUrlHttp, url,isProtected,followersCount,"
						 * +
						 * "twitter_status,profileUseBackgroundImage,friendsCount,createdAt,"
						 * +
						 * "favouritesCount,utcOffset,timeZone,statusesCount,isGeoEnabled ,"
						 * +
						 * "isVerified,translator,listedCount,isFollowRequestSent)"
						 * + " VALUES ( '" + status.getId() + ",'Pepsi'," +
						 * ",‘Pepsi’," + status.getCreatedAt() + "," +
						 * status.getText() + "," + status.getSource() + "," +
						 * ' ' + "," + status.getInReplyToStatusId() + "," +
						 * status.getInReplyToUserId() + "," + ' ' + "," + ' ' +
						 * "," + ' ' + "," + status.getInReplyToScreenName() +
						 * "," + status.getGeoLocation() + "," +
						 * status.getPlace() + "," + status.getRetweetCount() +
						 * "," + ' ' + "," + ' ' + "," + status.getLang() + ","
						 * + status.getRetweetedStatus() + "," +
						 * status.getHashtagEntities() + "," +
						 * status.getCurrentUserRetweetId() + "," +
						 * status.getUser().getId() + "," +
						 * status.getUser().getName() + "," +
						 * status.getUser().getScreenName() + "," +
						 * status.getUser().getLocation() + "," +
						 * status.getUser().getDescription() + "," +
						 * status.getUser().isContributorsEnabled() + "," +
						 * status.getUser().getProfileImageURL() + "," +
						 * status.getUser().getProfileImageURLHttps() + "," +
						 * status.getUser().getURL() + "," +
						 * status.getUser().isProtected() + "," +
						 * status.getUser().getFollowersCount() + "," +
						 * status.getText() + "," + status.getUser()
						 * .getProfileBackgroundImageURL() + "," +
						 * status.getUser().getFriendsCount() + "," +
						 * status.getUser().getCreatedAt() + "," +
						 * status.getUser().getFavouritesCount() + "," +
						 * status.getUser().getUtcOffset() + "," +
						 * status.getUser().getTimeZone() + "," +
						 * status.getUser().getStatusesCount() + "," +
						 * status.getUser().isGeoEnabled() + "," +
						 * status.getUser().isVerified() + "," +
						 * status.getUser().isTranslator() + "," +
						 * status.getUser().getListedCount() + "," +
						 * status.getUser().isFollowRequestSent() + ")";
						 * 
						 * String sql1 =
						 * "INSERT INTO Status(Id,PersonId2,Created_at,Text) values("
						 * + "1" + ",54" + ",'2015-01-01 12:12:12'" + ",'Pepsi'"
						 * + ");";
						 */
						String sql1 = "INSERT INTO Status(Id,PersonId2,Created_at, Text, Geo, Place)"
								+ " VALUES ( "
								+ status.getId()
								+ ","
								+ status.getUser().getId()
								+ ","
								+ "'"
								+ status.getCreatedAt()
								+ "',"
								+ "'"
								+ StringEscapeUtils.escapeSql(status.getText())
								+ "',"
								+ status.getGeoLocation()
								+ ","
								+ status.getPlace() + ");";
						if (status.getRateLimitStatus() != null) {
							System.out
									.println("status.getRateLimitStatus().getLimit() = "
											+ status.getRateLimitStatus()
													.getLimit());
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
						System.out.println(sql1);
						stmt.executeUpdate(sql1);
						System.out
								.println("Inserted records into the tableaaa...");

						stop = 0;
					}
				} catch (SQLException se) {
					// Handle errors for JDBC
					se.printStackTrace();
				} catch (Exception e) {
					// Handle errors for Class.forName
					e.printStackTrace();
				} finally {
					// finally block used to close resources
					try {
						if (stmt != null)
							stmt.close();
					} catch (SQLException se2) {
					}// nothing we can do
					try {
						if (conn != null)
							conn.close();
					} catch (SQLException se) {
						se.printStackTrace();
					}// end finally try
				}// end try
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
