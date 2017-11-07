import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class UserView implements ActionListener{
    private UserUser user;
    private AdminMan manager;

    private JTextField followInput, tweetInput;
    private JButton followButton, tweetButton;
    private JList newsfeed;
    private JList currentFollowing;

    public UserView( UserUser user, AdminMan manager ){
        this.user = user;
        this.manager = manager;
        JFrame userFrame = new JFrame( user.GetId() );
        followInput = new JTextField();
        tweetInput = new JTextField();
        followInput.setBounds( 10, 10, 260, 20 );
        followInput.setEditable( true );
        tweetInput.setBounds( 10, 235, 260, 20 );
        tweetInput.setEditable( true );
        userFrame.add( followInput );
        userFrame.add( tweetInput );
        followButton = new JButton( "Follow" );
        tweetButton = new JButton( "Tweet" );
        followButton.setBounds( 280, 10, 100, 20 );
        tweetButton.setBounds( 280, 235, 100, 20 );
        userFrame.add( followButton );
        userFrame.add( tweetButton );
        followButton.addActionListener( this );
        tweetButton.addActionListener( this );
        currentFollowing = new JList( user.GetFollowing() );
        newsfeed = new JList( user.GetTweets() );
        currentFollowing.setBounds( 10, 40, 370, 185 );
        newsfeed.setBounds( 10, 265, 370, 205 );
        userFrame.add( currentFollowing );
        userFrame.add( newsfeed );
        userFrame.setSize( 405, 520 );
        userFrame.setLayout( null );
        userFrame.setVisible( true );
    }

    public void actionPerformed( ActionEvent e ){
        if( e.getSource() == followButton ){
            String followId = followInput.getText();
            UserUser followee = manager.GetUser( followId );
            if( followee != null ){
                user.AddFollowing( followee );
                followee.AddFollower( user );
            }
        }
        if( e.getSource() == tweetButton ){
            String tweet = user.GetId() + ": " + tweetInput.getText();
            user.Tweet( tweet );
        }
    }
}
