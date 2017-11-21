import java.util.*;
import javax.swing.*;

public class UserUser extends User{
    private long lastUpdateTime;
    private ArrayList<String> myTweets;
    private ArrayList<TweetObserverVisitable> followers;
    private DefaultListModel<UserUser> following;
    private DefaultListModel<String> tweetFeed;

    public UserUser( String id ){
        super( id );
        lastUpdateTime = GetCreationTime();
        following = new DefaultListModel<UserUser>();
        followers = new ArrayList<TweetObserverVisitable>();
        myTweets = new ArrayList<String>();
        tweetFeed = new DefaultListModel<String>();
        followers.add( this );
    }

    public void AddFollowing( UserUser user ){
        following.addElement( user );
    }

    public void AddFollower( TweetObserverVisitable obs ){
        followers.add( obs );
    }

    public void Tweet( String tweet ){
        myTweets.add( tweet );
        NotifyFollowers( tweet );
    }

    public void NotifyFollowers( String tweet ){
        for( TweetObserverVisitable user: followers ){
            user.Update( tweet );
        }
    }

    public void Update( String tweet ){
        lastUpdateTime = System.currentTimeMillis();
        tweetFeed.addElement( tweet );
    }

    public DefaultListModel GetFollowing(){
        return following;
    }

    public DefaultListModel GetTweets(){
        return tweetFeed;
    }

    public ArrayList<String> GetMyTweets(){
        return myTweets;
    }

    public long GetLastUpdate(){
        return lastUpdateTime;
    }

    public void AcceptMessages( AdminVisitor visitor ){
        visitor.VisitMessages( this );
    }

    public void AcceptPositive( AdminVisitor visitor ){
        visitor.VisitPositive( this );
    }

    public void AcceptLastUpdate( AdminVisitor visitor ){
        visitor.VisitLastUpdate( this );
    }
}
