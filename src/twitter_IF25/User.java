package twitter_IF25;

public class User {
	double visibility;
	double danger;
	double agressiveness;
	int count_hashtag;
	int count_mention;
	int count_tweet_analysed;
	int count_malware_link;
	
	public int getCount_malware_link() {
		return count_malware_link;
	}
	public void setCount_malware_link(int count_malware_link) {
		this.count_malware_link = count_malware_link;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public int getCount_hashtag() {
		return count_hashtag;
	}
	public void setCount_hashtag(int count_hashtag) {
		this.count_hashtag = count_hashtag;
	}
	public int getCount_mention() {
		return count_mention;
	}
	public void setCount_mention(int count_mention) {
		this.count_mention = count_mention;
	}
	public int getCount_tweet_analysed() {
		return count_tweet_analysed;
	}
	public void setCount_tweet_analysed(int count_tweet_analysed) {
		this.count_tweet_analysed = count_tweet_analysed;
	}
	public double getDanger() {
		return danger;
	}
	public void setDanger(double danger) {
		this.danger = danger;
	}
	public double getAgressiveness() {
		return agressiveness;
	}
	public void setAgressiveness(double agressiveness) {
		this.agressiveness = agressiveness;
	}
	
}
