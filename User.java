import javax.swing.tree.DefaultMutableTreeNode;

public class User extends DefaultMutableTreeNode implements TweetObserverVisitable{
    protected String userId;
    protected long creationTime;

    public User( String id ){
        setUserObject( id );
        this.userId = id;
        this.creationTime = System.currentTimeMillis();
    }

    public String GetId(){
        return userId;
    }

    public void AcceptPositive( AdminVisitor visitor ){
        //Default functions, to be overridden when needed.
    }

    public void AddFollower( TweetObserverVisitable obs ){
        //Default functions, to be overridden when needed.
    }

    public void NotifyFollowers( String tweet ){
        //Default functions, to be overridden when needed.
    }

    public void Update( String tweet ){
        //Default functions, to be overridden when needed.
    }

    public void AcceptMessages( AdminVisitor visitor ){
        //Default functions, to be overridden when needed.
    }

    public void AcceptLastUpdate( AdminVisitor visitor ){
        //Default function, to be overridden when needed
    }

    public long GetCreationTime(){
        return creationTime;
    }
}
