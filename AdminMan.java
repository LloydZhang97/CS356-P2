import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.*;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeModel;
import java.util.*;

public class AdminMan implements ActionListener,AdminVisitor{

    private static final String[] POSITIVEWORDS = { "great", "good", "excellent", "awesome", "amazing", "stupendous", "supercalifragilisticexpialidocious"};

    private static AdminMan adminManager;
    private GroupUser root;
    private HashMap<String,TweetObserverVisitable> users;
    private User selectedUser;
    private int totalMessages, userNum, groupNum, positiveTotal;
    private long lastUpdateTime;
    private String lastUpdateId;

    private JFrame mainFrame;
    private JTextArea consoleOutput;
    private JTextField userIdInput, groupIdInput;
    private JButton userIdButton, groupIdButton, openUserButton, userTotalButton, groupTotalButton, messageTotalButton, positivePerButton, idVerificationButton, lastUpdateButton;
    private JTree userList;
    private DefaultTreeModel userListModel;

    private AdminMan(){
        users = new HashMap<String,TweetObserverVisitable>();
        userNum = 0;
        groupNum = 1;

        mainFrame = new JFrame( "Admin Manager" );
        consoleOutput = new JTextArea( "Welcome to the Admin Manager for Mini-Twitter!\n" );
        consoleOutput.setBounds( 405, 100, 370, 260 );
        mainFrame.add( consoleOutput );
        userIdInput = new JTextField( "UserId" );
        groupIdInput = new JTextField( "GroupId" );
        userIdInput.setBounds( 405, 10, 260, 20 );
        userIdInput.setEditable(true);
        groupIdInput.setBounds( 405, 40, 260, 20 );
        groupIdInput.setEditable( true );
        mainFrame.add( userIdInput );
        mainFrame.add( groupIdInput );
        userIdButton = new JButton( "Add User" );
        groupIdButton = new JButton( "Add Group" );
        openUserButton = new JButton( "Open User View" );
        userTotalButton = new JButton( "Show User Total" );
        groupTotalButton = new JButton( "Show Group Total" );
        messageTotalButton = new JButton( "Show Messages Total" );
        positivePerButton = new JButton( "Show Positive Percentage" );
        idVerificationButton = new JButton( "Validate All Id's" );
        lastUpdateButton = new JButton( "Get Last User Update" );
        userIdButton.setBounds( 675, 10, 100, 20 );
        groupIdButton.setBounds( 675, 40, 100, 20 );
        openUserButton.setBounds( 405, 70, 370, 20 );
        userTotalButton.setBounds( 405, 370, 180, 20 );
        groupTotalButton.setBounds( 595, 370, 180, 20 );
        messageTotalButton.setBounds( 405, 400, 180, 20 );
        positivePerButton.setBounds( 595, 400, 180, 20 );
        idVerificationButton.setBounds( 405, 430, 180, 20 );
        lastUpdateButton.setBounds( 595, 430, 180, 20 );
        userIdButton.addActionListener( this );
        groupIdButton.addActionListener( this );
        openUserButton.addActionListener( this );
        userTotalButton.addActionListener( this );
        groupTotalButton.addActionListener( this );
        messageTotalButton.addActionListener( this );
        positivePerButton.addActionListener( this );
        idVerificationButton.addActionListener( this );
        lastUpdateButton.addActionListener( this );
        mainFrame.add( userIdButton );
        mainFrame.add( groupIdButton );
        mainFrame.add( openUserButton );
        mainFrame.add( userTotalButton );
        mainFrame.add( groupTotalButton );
        mainFrame.add( messageTotalButton );
        mainFrame.add( positivePerButton );
        mainFrame.add( idVerificationButton );
        mainFrame.add( lastUpdateButton );
        root = new GroupUser( "root" );
        selectedUser = root;
        userList = new JTree( root );
        userListModel = (DefaultTreeModel)userList.getModel();
        userList.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        userList.setBounds( 10, 10, 385, 440 );
        mainFrame.add( userList );
        mainFrame.setSize( 800,500 );
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }

    public static AdminMan Start(){
        if( adminManager == null ){
            adminManager = new AdminMan();
        }
        return adminManager;
    }

    public UserUser GetUser( String id ){
        return (UserUser)users.get( id );
    }

    public void GetUserTotal(){
        consoleOutput.append( "There are " + userNum + " users!\n" );
        consoleOutput.update( consoleOutput.getGraphics() );
    }

    public void GetGroupTotal(){
        consoleOutput.append( "There are " + groupNum + " groups!\n" );
        consoleOutput.update( consoleOutput.getGraphics() );
    }

    public void GetMessagesTotal(){
        totalMessages = 0;
        for( TweetObserverVisitable user: users.values() ){
            user.AcceptMessages( this );
        }
        consoleOutput.append( "There are " + totalMessages + " messages!\n" );
        consoleOutput.update( consoleOutput.getGraphics() );
    }

    public void GetPositiveTotal(){
        positiveTotal = 0;
        totalMessages = 0;
        for( TweetObserverVisitable user: users.values() ){
            user.AcceptPositive( this );
        }
        if( totalMessages == 0 ){
            totalMessages = 1; //To prevent divide by 0 error
        }
        consoleOutput.append( ( (double)positiveTotal/totalMessages * 100 ) + " of the messages are positive!\n" );
        consoleOutput.update( consoleOutput.getGraphics() );
    }

    public void GetLastUserUpdate(){
        lastUpdateId = "";
        lastUpdateTime = Long.MAX_VALUE;
        for( TweetObserverVisitable user: users.values() ){
            user.AcceptLastUpdate( this );
        }
        consoleOutput.append( "Last Update User: " + lastUpdateId );
        consoleOutput.update( consoleOutput.getGraphics() );
    }

    public void VisitMessages( UserUser user ){
        totalMessages += user.GetMyTweets().size();
    }

    public void VisitPositive( UserUser user ){
        ArrayList<String> tweets = user.GetMyTweets();
        totalMessages += tweets.size();
        for( String tweet: tweets ){
            for( String positive: POSITIVEWORDS ){
                if( tweet.toLowerCase().contains( positive ) ){
                    positiveTotal++;
                    break;
                }
            }
        }
    }

    public void VisitLastUpdate( UserUser user ){
        if( user.GetLastUpdate() < lastUpdateTime ){
            lastUpdateId = user.GetId();
        }
    }

    public boolean validateUsers(){
        Set<String> names = users.keySet();
        for( String name: names ){
            if( name.indexOf( " " ) != -1 ){
                return false;
            }
        }
        return true;
    }

    public void actionPerformed( ActionEvent e ){
        if( e.getSource() == userIdButton ){
            String userId = userIdInput.getText();
            if( users.containsKey( userId ) )
                return;
            UserUser user = new UserUser( userId );
            try{
                GroupUser selectedUser = (GroupUser)userList.getLastSelectedPathComponent();
                if( selectedUser == null )
                    return;
                selectedUser.add( user );
                users.put( user.GetId(), user );
                userListModel.reload( selectedUser );
                ++userNum;
            } catch ( Exception ex ){}
        }
        else if( e.getSource() == groupIdButton ){
            GroupUser user = new GroupUser( groupIdInput.getText() );
            try{
                GroupUser selectedUser = (GroupUser)userList.getLastSelectedPathComponent();
                if( selectedUser == null )
                    return;
                selectedUser.add( user );
                users.put( user.GetId(), user );
                userListModel.reload( selectedUser );
                ++groupNum;
            } catch ( Exception ex ){}
        }
        else if( e.getSource() == openUserButton ){
            try{
                UserUser user = (UserUser)userList.getLastSelectedPathComponent();
                System.out.println( user.GetId() + " created at " + user.GetCreationTime() + "\n" );
                UserView newUserview = new UserView( user, this );
            } catch ( Exception ex ){}
        }
        else if( e.getSource() == userTotalButton ){
            GetUserTotal();
        }
        else if( e.getSource() == groupTotalButton ){
            GetGroupTotal();
        }
        else if( e.getSource() == messageTotalButton ){
            GetMessagesTotal();
        }
        else if( e.getSource() == positivePerButton ){
            GetPositiveTotal();
        }
        else if( e.getSource() == idVerificationButton ){
            if( validateUsers() ){
                consoleOutput.append( "All users are valid!\n" );
                consoleOutput.update( consoleOutput.getGraphics() );
            }
            else{
                consoleOutput.append( "One or more users are not valid!\n" );
                consoleOutput.update( consoleOutput.getGraphics() );
            }
        }
        else if( e.getSource() == lastUpdateButton ){
            GetLastUserUpdate();
        }
    }
}
