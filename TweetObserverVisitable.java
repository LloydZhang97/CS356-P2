public interface TweetObserverVisitable{

    public void AddFollower( TweetObserverVisitable obs );

    public void NotifyFollowers( String tweet );

    public void Update( String tweet );

    public void AcceptMessages( AdminVisitor visitor );

    public void AcceptPositive( AdminVisitor visitor );
}
