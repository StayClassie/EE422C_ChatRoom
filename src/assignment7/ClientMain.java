
/* CHAT ROOM <ClientMain.java>
* EE422C Project 7 submission by
* Replace <...> with your actual data
* Chris Classie
* CSC2859
* 16355
* Slip days used: <0>
* Fall 2018
* */

package assignment7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ListIterator;

import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientMain extends Application {

    private String name;
    private Socket sock;
    private BufferedReader reader;
    private PrintWriter writer;
    private TextFlow incomingMessage;
    private TextField outgoingMessage;

    private Stage emojiStage = new Stage();
    private boolean toggleEmoji = false;

    private Stage gifStage = new Stage();
    private boolean toggleGif = false;

    ArrayList<String> blockedList;

    private static final String separator = Character.toString((char) 31);
    private static final String nameSeparator = Character.toString((char) 29);

    private Stage home = null;
    private HashMap<Integer, ChatWindow> chatWindows = new HashMap<Integer, ChatWindow>();
    private VBox open;


    private Color tempColor = Color.BLACK;
    private Color baseColor = Color.BLACK;
    private Color mine = Color.BLACK;
    private Color theirs = Color.BLACK;
    private String font = "Times New Roman";
    private String buttonColor = "#0000FF";



    @Override
    public void start(Stage primaryStage) throws Exception {

        home = primaryStage;
        open = new VBox();
        open.setPadding(new Insets(10, 40, 40, 40));
        open.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 10;" +
                "-fx-border-insets: 0;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        open.setSpacing(10);
        primaryStage.setTitle("AIM");

        //Creating an image
        Image image = new Image(new FileInputStream("AOL_Instant_Messenger_logo,_1999.png"));

        //Setting the image view
        ImageView imageView = new ImageView(image);

        //Setting the position of the image
        imageView.setX(100);
        imageView.setY(100);

        //setting the fit height and width of the image view
        imageView.setFitHeight(200);
        imageView.setFitWidth(260);

        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        open.getChildren().add(imageView);

        Label ipInstruction = new Label("Enter IP Address:");
        TextField ip = new TextField();
        ip.setPromptText("IP Address");

        Label howTo = new Label("How to Find IP Address:");
        Label space = new Label (" ");

        Label mac = new Label ("Mac:");
        mac.setStyle("-fx-underline: true");
        Label mac1 = new Label ("Go to System Preferences then Network");
        Label mac2 = new Label ("IP Address will be listed there");

        Label space2 = new Label (" ");

        Label windows = new Label("Windows:");
        windows.setStyle("-fx-underline: true");
        Label windows1 = new Label("Click Start -> Control Panel");
        Label windows2 = new Label("Then 'View Network Status And Tasks'' ");

        Label error = new Label();

        // user inputs IP address of server trying to connect to
        Button enterIP = new Button("Connect");
        enterIP.setStyle("-fx-base: " + buttonColor + ";");
        enterIP.setOnAction(new EventHandler<ActionEvent>() {

            // after IP address entered, set up network and then prompt log in screen
            @Override
            public void handle(ActionEvent arg0) {

                if(!ip.getText().equals("")) {

                    try {

                        setUpNetworking(ip.getText());
                        loginScreen();

                    }
                    catch (UnknownHostException e) {
                        error.setText("Invalid IP, reenter.");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ip.setOnKeyPressed(new EventHandler<KeyEvent>() {

            int w = 0;
            int v = 0;
            boolean IPFound = false;

            // enter key
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER){

                    if(!ip.getText().equals("")) {

                        try {

                            setUpNetworking(ip.getText());
                            loginScreen();
                        }

                        catch (UnknownHostException e) {
                            error.setText("Invalid IP, reenter.");
                        }

                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        open.getChildren().addAll(ipInstruction, ip, enterIP, error, howTo, space, mac, mac1, mac2, space2, windows,windows1, windows2);
        Scene scene = new Scene(open, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   //log-in screen
    public void loginScreen() throws FileNotFoundException {

        open.getChildren().clear();

        open = new VBox();
        open.setPadding(new Insets(10, 40, 40, 40));

        open.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 10;" +
                "-fx-border-insets: 0;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        open.setSpacing(10);


        //Creating an image
        Image image1 = new Image(new FileInputStream("AOL_Instant_Messenger_logo,_1999.png"));

        //Setting the image view
        ImageView imageView2 = new ImageView(image1);

        //Setting the position of the image
        imageView2.setX(100);
        imageView2.setY(100);

        //setting the fit height and width of the image view
        imageView2.setFitHeight(250);
        imageView2.setFitWidth(250);

        //Setting the preserve ratio of the image view
        imageView2.setPreserveRatio(true);

        open.getChildren().add(imageView2);


        Label instruction = new Label("ScreenName:");
        instruction.setTextFill(Color.BLUE);
        instruction.setStyle("-fx-font-size: 24");
        instruction.setStyle("-fx-font-weight: bold");
        TextField username = new TextField();
        username.setPromptText("ScreenName");

        Label instruction2 = new Label("Password:");
        instruction2.setTextFill(Color.BLACK);
        instruction.setStyle("-fx-font-weight: bold");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label instruction3 = new Label("Forgot Password?");
        instruction3.setTextFill(Color.BLUE);
        instruction3.setStyle("-fx-underline: true");

        Label space3 = new Label(" ");

        Button createAcc = new Button("Get A Screen Name");
        createAcc.setStyle("-fx-base: " + buttonColor + ";");
        createAcc.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                String user = username.getText();
                String pwd = password.getText();

                if (!user.equals("") && !pwd.equals("")) {
                    name = user;

                    // send the new user info to the server
                    writer.println("NEWUSER" + separator + user + separator + pwd);
                    writer.flush();

                    // get a list of current users who are online
                    writer.println("GETONLINE" + separator + name);
                    writer.flush();
                }
            }

        });

        Button login = new Button("Sign On");
        login.setStyle("-fx-base: " + buttonColor + ";");
        login.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {

                String user = username.getText();
                String pwd = password.getText();

                if (!user.equals("") && !pwd.equals("")) {

                    name = user;

                    //users logged in
                    writer.println("LOGIN" + separator + user + separator + pwd);
                    writer.flush();

                    // update chat screen when new user is logged in
                    writer.println("GETONLINE" + separator + name);
                    writer.flush();

                }
            }

        });

        Button Help = new Button ("Help");
        Help.setStyle("-fx-base: " + buttonColor + ";");
        Help.setOnAction(new EventHandler<ActionEvent>() {

                              @Override
                              public void handle(ActionEvent arg0) {
                                  // Figure out a way to go to a help screen to provide insight
                              }
                          });

        Button Setup = new Button ("Setup");
        Setup.setStyle("-fx-base: " + buttonColor + ";");
        Setup.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                //  This Button would probably be for show as there is not much to setup for this project
                //only considering adding because it was part of the layout I am mimicing
            }
        });

        int length = 2;
        Label savePassword = new Label("Save password");
        CheckBox[] savedPassword = new CheckBox[length];
        boolean saveClicked = false;

        int length2 = 2;
        Label autoLogin = new Label("Auto-Login");
        CheckBox[] autoLoginBox = new CheckBox[length2];
        boolean autoLoginClick = false;

      //  for (int i = 0; i < savedPassword.length; i++) {

       //     if(!(savedPassword[i].equals(2))) {

          //      saveClicked = true;
         //   }
    //    }

        //find a way to enable click buttons to save password and to Auto-Login
        //auto-login one seems near impossible
        //save password would just store the password to the corresponding username and if clicked dislay the password
        //not sure how to implement it though




        open.getChildren().addAll(instruction,  username  ,createAcc, instruction2, password,instruction3,space3, login);
        Scene scene = new Scene(open, 300, 600);
        home.setScene(scene);
        home.show();
    }

   //networking the IP address
    private void setUpNetworking(String IP) throws Exception {

        this.sock = new Socket(IP, 4242);
        InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
        reader = new BufferedReader(streamReader);

        try
        {
            writer = new PrintWriter(sock.getOutputStream());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    //when creating account let the user know if a username has been taken already
    public void userExists(String[] message) {


        for(ListIterator<Node> iterator = open.getChildren().listIterator(); iterator.hasNext();) {
            Node currentNode = iterator.next();
            if (currentNode instanceof Label && ((Label)currentNode).getText().contains("ERROR")) {
                iterator.remove();
            }
        }

        String error = "ERROR: Username already exists. Enter another username.";
        Label notif = new Label();
        notif.setText(error);
        notif.setTextFill(Color.RED);
        notif.setWrapText(true);
        open.getChildren().addAll(notif);


    }


     //check if user is already logged in
    public void alreadyLogged(String[] message) {

        for(ListIterator<Node> iterator = open.getChildren().listIterator(); iterator.hasNext();) {
            Node currentNode = iterator.next();
            if (currentNode instanceof Label && ((Label)currentNode).getText().contains("ERROR")) {
                iterator.remove();
            }
        }

        String error = "ERROR: User already logged in. Enter another username.";
        Label notif = new Label();
        notif.setTextFill(Color.RED);
        notif.setText(error);
        notif.setWrapText(true);
        open.getChildren().addAll(notif);

    }

   //does the username match the password given
    public void wrongPass(String[] message) {

        for(ListIterator<Node> iterator = open.getChildren().listIterator(); iterator.hasNext();) {

            Node currentNode = iterator.next();

            if (currentNode instanceof Label && ((Label)currentNode).getText().contains("ERROR")) {

                iterator.remove();
            }
        }

        String error = "ERROR: Invalid Password. Please Retry.";
        Label notif = new Label();
        notif.setText(error);
        notif.setTextFill(Color.RED);
        notif.setWrapText(true);
        open.getChildren().addAll(notif);


    }


    public void loggedIn(String[] available) {

        //close old stage
        double x = home.getX();
        double y = home.getY();
        home.close();

        home = new Stage();
        home.setTitle("Instant Message");
        home.setX(x);
        home.setY(y);

        Label space1 = new Label("");
        Label space2 = new Label("");
        Label space3 = new Label("");

        VBox chat = new VBox();
        chat.setPadding(new Insets(10, 40, 40, 40));
        chat.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 10;" +
                "-fx-border-insets: 0;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        chat.setSpacing(5);

        //Creating an image
        Image image3 = null;
        try {
            image3 = new Image(new FileInputStream("AOL_Instant_Messenger_logo,_1999.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Setting the image view
        ImageView imageView3 = new ImageView(image3);

        //Setting the position of the image
        imageView3.setX(100);
        imageView3.setY(100);

        //setting the fit height and width of the image view
        imageView3.setFitHeight(200);
        imageView3.setFitWidth(250);

        //Setting the preserve ratio of the image view
        imageView3.setPreserveRatio(true);

        chat.getChildren().add(imageView3);

        Label people = new Label("Select User(s) to Chat With?");

        chat.getChildren().addAll( space1, people);

        // CheckBox of all users currently online (available is the array of online users)
        CheckBox[] online = new CheckBox[available.length-1];
        int j = 0;
        int a = 0;
        int b = 0;

        for (int i = 0; i < available.length; i++) {

            if(!(available[i].equals(name))) {

                online[j] = new CheckBox(available[i]);
                chat.getChildren().add(online[j]);
                j++;
            }
        }

        // start chat with other online user(s)
        Button done = new Button("Start Chat");
        done.setStyle("-fx-base: " + buttonColor + ";");
        done.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {

                String names = "";
                for(int i = 0; i < online.length; i++) {
                    if (online[i].isSelected())
                        names += online[i].getText() + nameSeparator;
                }

                if (names.equals("")) {
                    return;
                }

                String message = "NEWCHAT" + separator + name + separator + names;
                writer.println(message);
                writer.flush();

            }});

        Label myColor = new Label("Choose your chat color:");


        ColorPicker colorPickerMine = new ColorPicker(Color.BLACK);
        colorPickerMine.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                mine = colorPickerMine.getValue();
            }
        });

        Label theirColor = new Label("Choose their char color:");

        ColorPicker colorPickerTheirs = new ColorPicker(Color.BLACK);
        colorPickerTheirs.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                theirs = colorPickerTheirs.getValue();
            }
        });

        // log out of the system
        Button logOut = new Button("Log Out");
        logOut.setStyle("-fx-base: " + buttonColor + ";");
        logOut.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {


                for (ChatWindow chats : chatWindows.values()) {
                    chats.chat.close();
                }

                writer.println("LOGOUT" + separator + name);
                writer.flush();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                writer.println("GETONLINE" + separator + name);
                writer.flush();

                try {
                    sock.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                home.close();
                exit();
            }
        });

        chat.getChildren().addAll(done, space2, myColor, colorPickerMine, theirColor, colorPickerTheirs, space3, logOut);
        Scene realScene = new Scene(chat, 300, 600);
        home.setScene(realScene);

        // set up so that if the user closes the home window, it effectively logs them out of the system
        home.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {

                for (ChatWindow chats : chatWindows.values()) {
                    chats.chat.close();
                }

                writer.println("LOGOUT" + separator + name);
                writer.flush();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                writer.println("GETONLINE" + separator + name);
                writer.flush();

                try {
                    sock.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                home.close();
                exit();
            }


        });
        home.show();
    }

    // closes the socket
    public void exit() {
        System.exit(0);
    }

   //chat window
    public void startChat(String[] message) {
        int ID = Integer.parseInt(message[0]);
        ChatWindow newChat = new ChatWindow(ID);
        if(chatWindows.containsKey(ID)) {
            newChat.setTitle(chatWindows.get(ID).getTitle());
            chatWindows.get(ID).close();
        }
        else
            newChat.setTitle(message);
        newChat.updateChat(message);
        chatWindows.put(ID, newChat);
    }

    class ChatWindow extends Application {

        private int ID;
        private Stage chat;

        private ScrollPane sPane;
        private GridPane convo;

        private int messageNo = 0;
        private int chatroomnm = 0;
        private int i = 0;
        private int j = 0;


        public ChatWindow(int num) {

            ID = num;
            this.chat = new Stage();
            chat.setTitle("Chat Window");

            convo = new GridPane();

            sPane = new ScrollPane();
            sPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            sPane.setHbarPolicy(ScrollBarPolicy.NEVER);

            TextField text = new TextField();
            text.setPromptText("Enter message");

            // send a message to another user
            Button send = new Button("Send");
            send.setStyle("-fx-base: " + buttonColor + ";");
            send.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    String s = text.getText();
                    if (s != null) {

                        sendMessage(s);
                        text.clear();
                        text.setPromptText("Enter message");
                    }
                }

            });

            // send message on pressing
            text.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ENTER) {

                        String s = text.getText();
                        if (s != null) {

                            sendMessage(s);
                            System.out.println("message sent: " + s);
                            text.clear();
                            text.setPromptText("Enter message");
                        }
                    }
                }
            });

            Button emoji = new Button("Emojis");
            emoji.setStyle("-fx-base: " + buttonColor + ";");
            emoji.setFont(Font.font("Times New Roman"));
            emoji.setTextFill(Color.DARKBLUE);


            emoji.setOnAction((ActionEvent event) -> {
                if (!toggleEmoji) {
                    GridPane emojiPane = new GridPane();
                    emojiPane.setAlignment(Pos.BOTTOM_RIGHT);

                    Scene emojiScene = new Scene(emojiPane, 102, 102);

                    Button smiley = new Button();
                    Image okImage = null;
                    try {
                        okImage = new Image(new FileInputStream("Slightly_Smiling_Face_Emoji.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp = new ImageView(okImage);
                    temp.setFitHeight(16.0);
                    temp.setFitHeight(16.0);
                    temp.setPreserveRatio(true);
                    smiley.setGraphic(temp);

                    smiley.setOnAction(e -> {
                        text.appendText("0x0FF00");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF00");
                    });

                    Button smiley1 = new Button();
                    Image okImage1 = null;
                    try {
                        okImage1 = new Image(new FileInputStream("Smiling_Face_Emoji_with_Blushed_Cheeks.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp1 = new ImageView(okImage1);
                    temp1.setFitHeight(16.0);
                    temp1.setFitHeight(16.0);
                    temp1.setPreserveRatio(true);
                    smiley1.setGraphic(temp1);

                    smiley1.setOnAction(e -> {
                        text.appendText("0x0FF01");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF01");

                    });

                    Button smiley2= new Button();
                    Image okImage2 = null;
                    try {
                        okImage2 = new Image(new FileInputStream("laughing.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp2 = new ImageView(okImage2);
                    temp2.setFitHeight(16.0);
                    temp2.setFitHeight(16.0);
                    temp2.setPreserveRatio(true);
                    smiley2.setGraphic(temp2);

                    smiley2.setOnAction(e -> {
                        text.appendText("0x0FF02");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF02");

                    });

                    Button smiley3 = new Button();
                    Image okImage3 = null;
                    try {
                        okImage3 = new Image(new FileInputStream("thinking.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp3 = new ImageView(okImage3);
                    temp3.setFitHeight(16.0);
                    temp3.setFitHeight(16.0);
                    temp3.setPreserveRatio(true);
                    smiley3.setGraphic(temp3);

                    smiley3.setOnAction(e -> {
                        text.appendText("0x0FF03");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF03");

                    });

                    Button smiley4 = new Button();
                    Image okImage4 = null;
                    try {
                        okImage4 = new Image(new FileInputStream("wink.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp4 = new ImageView(okImage4);
                    temp4.setFitHeight(16.0);
                    temp4.setFitHeight(16.0);
                    temp4.setPreserveRatio(true);
                    smiley4.setGraphic(temp4);

                    smiley4.setOnAction(e -> {
                        text.appendText("0x0FF04");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF04");

                    });

                    Button smiley5 = new Button();
                    Image okImage5 = null;
                    try {
                        okImage5 = new Image(new FileInputStream("kiss.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp5 = new ImageView(okImage5);
                    temp5.setFitHeight(16.0);
                    temp5.setFitHeight(16.0);
                    temp5.setPreserveRatio(true);
                    smiley5.setGraphic(temp5);

                    smiley5.setOnAction(e -> {
                        text.appendText("0x0FF05");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF05");

                    });

                    Button smiley6 = new Button();
                    Image okImage6 = null;
                    try {
                        okImage6 = new Image(new FileInputStream("thumbs.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp6 = new ImageView(okImage6);
                    temp6.setFitHeight(16.0);
                    temp6.setFitHeight(16.0);
                    temp6.setPreserveRatio(true);
                    smiley6.setGraphic(temp6);

                    smiley6.setOnAction(e -> {
                        text.appendText("0x0FF06");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF06");

                    });

                    Button smiley7 = new Button();
                    Image okImage7 = null;
                    try {
                        okImage7 = new Image(new FileInputStream("hookem.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp7 = new ImageView(okImage7);
                    temp7.setFitHeight(16.0);
                    temp7.setFitHeight(16.0);
                    temp7.setPreserveRatio(true);
                    smiley7.setGraphic(temp7);

                    smiley7.setOnAction(e -> {
                        text.appendText("0x0FF07");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF07");

                    });

                    Button smiley8 = new Button();
                    Image okImage8 = null;
                    try {
                        okImage8 = new Image(new FileInputStream("ok.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp8 = new ImageView(okImage8);
                    temp8.setFitHeight(16.0);
                    temp8.setFitHeight(16.0);
                    temp8.setPreserveRatio(true);
                    smiley8.setGraphic(temp8);

                    smiley8.setOnAction(e -> {
                        text.appendText("0x0FF08");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF08");

                    });

                    Button smiley9 = new Button();
                    Image okImage9 = null;
                    try {
                        okImage9 = new Image(new FileInputStream("heart.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView temp9 = new ImageView(okImage9);
                    temp9.setFitHeight(16.0);
                    temp9.setFitHeight(16.0);
                    temp9.setPreserveRatio(true);
                    smiley9.setGraphic(temp9);

                    smiley9.setOnAction(e -> {
                        text.appendText("0x0FF09");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF09");

                    });

                    Button smileyA = new Button();
                    Image okImageA = null;
                    try {
                        okImageA = new Image(new FileInputStream("poop.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView tempA = new ImageView(okImageA);
                    tempA.setFitHeight(16.0);
                    tempA.setFitHeight(16.0);
                    tempA.setPreserveRatio(true);
                    smileyA.setGraphic(tempA);

                    smileyA.setOnAction(e -> {
                        text.appendText("0x0FF0A");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF0A");

                    });

                    Button smileyB = new Button();
                    Image okImageB = null;
                    try {
                        okImageB = new Image(new FileInputStream("eggplant.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView tempB = new ImageView(okImageB);
                    tempB.setFitHeight(16.0);
                    tempB.setFitHeight(16.0);
                    tempB.setPreserveRatio(true);
                    smileyB.setGraphic(tempB);

                    smileyB.setOnAction(e -> {
                        text.appendText("0x0FF0B");
                        send.fire();
                        System.out.println("emoji sent: 0x0FF0B");

                    });




                    emojiPane.add(smiley, 0, 0);
                    emojiPane.add(smiley1, 1, 0);
                    emojiPane.add(smiley2, 2, 0);
                    emojiPane.add(smiley3, 0, 1);
                    emojiPane.add(smiley4, 1,1);
                    emojiPane.add(smiley5, 2, 1);
                    emojiPane.add(smiley6, 0, 2);
                    emojiPane.add(smiley7, 1, 2);
                    emojiPane.add(smiley8, 2,2);
                    emojiPane.add(smiley9, 0,3);
                    emojiPane.add(smileyA, 1, 3);
                    emojiPane.add(smileyB, 2, 3);
                    emojiStage.setScene(emojiScene);
                    emojiStage.setX(495);
                    emojiStage.setY(503);
                    toggleEmoji = true;
                    emojiStage.show();


                } else {
                    toggleEmoji = false;
                    emojiStage.close();
                }

            });

            Button gif = new Button("GIFS");
            gif.setStyle("-fx-base: " + buttonColor + ";");
            gif.setFont(Font.font("Times New Roman"));
            gif.setTextFill(Color.DARKBLUE);


            gif.setOnAction((ActionEvent event) -> {
                        if (!toggleGif) {
                            GridPane gifPane = new GridPane();
                            gifPane.setAlignment(Pos.BOTTOM_LEFT);

                            Scene gifScene = new Scene(gifPane, 100, 102);

                            Button gifBtn = new Button();
                            Image gifImage = null;
                            try {
                                gifImage = new Image(new FileInputStream("EmGif.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempC = new ImageView(gifImage);
                            tempC.setFitHeight(16.0);
                            tempC.setFitHeight(16.0);
                            tempC.setPreserveRatio(true);
                            gifBtn.setGraphic(tempC);

                            gifBtn.setOnAction(e -> {
                                text.appendText("0x0FF0C");
                                send.fire();
                                System.out.println("gif sent: 0x0FF0C");
                            });


                            Button gifBtn1 = new Button();
                            Image gifImage1 = null;
                            try {
                                gifImage1 = new Image(new FileInputStream("ms1.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempD = new ImageView(gifImage1);
                            tempD.setFitHeight(16.0);
                            tempD.setFitHeight(16.0);
                            tempD.setPreserveRatio(true);
                            gifBtn1.setGraphic(tempD);

                            gifBtn1.setOnAction(e -> {
                                text.appendText("0x0FF0D");
                                send.fire();
                                System.out.println("gif sent: 0x0FF0D");
                            });

                            Button gifBtn2 = new Button();
                            Image gifImage2 = null;
                            try {
                                gifImage2 = new Image(new FileInputStream("gif3.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempE = new ImageView(gifImage2);
                            tempE.setFitHeight(16.0);
                            tempE.setFitHeight(16.0);
                            tempE.setPreserveRatio(true);
                            gifBtn2.setGraphic(tempE);

                            gifBtn2.setOnAction(e -> {
                                text.appendText("0x0FF0E");
                                send.fire();
                                System.out.println("gif sent: 0x0FF0E");
                            });

                            Button gifBtn3 = new Button();
                            Image gifImage3 = null;
                            try {
                                gifImage3 = new Image(new FileInputStream("gif4.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempF = new ImageView(gifImage3);
                            tempF.setFitHeight(16.0);
                            tempF.setFitHeight(16.0);
                            tempF.setPreserveRatio(true);
                            gifBtn3.setGraphic(tempF);

                            gifBtn3.setOnAction(e -> {
                                text.appendText("0x0FF0F");
                                send.fire();
                                System.out.println("gif sent: 0x0FF0F");
                            });

                            Button gifBtn4 = new Button();
                            Image gifImage4 = null;
                            try {
                                gifImage4 = new Image(new FileInputStream("himym.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempG = new ImageView(gifImage4);
                            tempG.setFitHeight(16.0);
                            tempG.setFitHeight(16.0);
                            tempG.setPreserveRatio(true);
                            gifBtn4.setGraphic(tempG);

                            gifBtn4.setOnAction(e -> {
                                text.appendText("0x0FF10");
                                send.fire();
                                System.out.println("gif sent: 0x0FF10");
                            });

                            Button gifBtn5 = new Button();
                            Image gifImage5 = null;
                            try {
                                gifImage5 = new Image(new FileInputStream("friends.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempH = new ImageView(gifImage5);
                            tempH.setFitHeight(16.0);
                            tempH.setFitHeight(16.0);
                            tempH.setPreserveRatio(true);
                            gifBtn5.setGraphic(tempH);

                            gifBtn5.setOnAction(e -> {
                                text.appendText("0x0FF11");
                                send.fire();
                                System.out.println("gif sent: 0x0FF11");
                            });

                            Button gifBtn6 = new Button();
                            Image gifImage6 = null;
                            try {
                                gifImage6 = new Image(new FileInputStream("homer.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempI = new ImageView(gifImage6);
                            tempI.setFitHeight(16.0);
                            tempI.setFitHeight(16.0);
                            tempI.setPreserveRatio(true);
                            gifBtn6.setGraphic(tempI);

                            gifBtn6.setOnAction(e -> {
                                text.appendText("0x0FF12");
                                send.fire();
                                System.out.println("gif sent: 0x0FF12");
                            });

                            Button gifBtn7 = new Button();
                            Image gifImage7 = null;
                            try {
                                gifImage7 = new Image(new FileInputStream("bigsexy.gif"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ImageView tempJ = new ImageView(gifImage7);
                            tempJ.setFitHeight(16.0);
                            tempJ.setFitHeight(16.0);
                            tempJ.setPreserveRatio(true);
                            gifBtn7.setGraphic(tempJ);

                            gifBtn7.setOnAction(e -> {
                                text.appendText("0x0FF13");
                                send.fire();
                                System.out.println("gif sent: 0x0FF13");
                            });




                            gifPane.add(gifBtn, 0, 0);
                            gifPane.add(gifBtn1, 1,0 );
                            gifPane.add(gifBtn2, 0, 1);
                            gifPane.add(gifBtn3, 1, 1);
                            gifPane.add(gifBtn4, 0, 2);
                            gifPane.add(gifBtn5, 1, 2);
                            gifPane.add(gifBtn6, 0, 3);
                            gifPane.add(gifBtn7, 1, 3);
                            gifStage.setScene(gifScene);
                            gifStage.setX(495);
                            gifStage.setY(503);
                            toggleGif = true;
                            gifStage.show();


                        } else {
                            toggleGif = false;
                            gifStage.close();
                        }

            });


            VBox box = new VBox();
            GridPane screen = new GridPane();

            screen.getRowConstraints().add(new RowConstraints(270));
            screen.getRowConstraints().add(new RowConstraints(30));

            screen.getColumnConstraints().add(new ColumnConstraints(250));
            screen.getColumnConstraints().add(new ColumnConstraints(50));

            screen.add(sPane, 0, 0);
            screen.add(text, 0, 1);
            screen.add(send, 1, 1);
            screen.add(emoji, 2, 1);
            screen.add(gif, 3, 1);

            sPane.setContent(convo);
            box.getChildren().addAll(screen);
            Scene scene = new Scene(box, 405, 300);
            sPane.setMinViewportWidth(scene.getWidth() - 15);
            chat.setScene(scene);
            chat.show();
        }

        public void close() {
            chat.close();
        }

        //set title
        public void setTitle(String[] message) {
            String sentMessage = message[2];
            sentMessage = sentMessage.substring(28, sentMessage.length());
            chat.setTitle(sentMessage);
        }

        public void setTitle(String title) {
            chat.setTitle(title);
        }

        public String getTitle() {
            return chat.getTitle();
        }

        @Override
        public void start(Stage arg0) throws Exception {

        }
        int tempOnline;
        int tempOffline;

        public int listUsersOnline(){
            //FIGURE OUT HOW TO LIST THE USERS WHEN NOT ONLINE
            return tempOnline;
        }

        public int listUsersOffline(){
            //FIGURE OUT HOW TO LIST THE USERS WHEN NOT ONLINE
            return tempOffline;
        }

        public int getID() {
            return ID;
        }

        int blocked = 1;
        boolean block = false;
        public int block(){

            //figure out a way to block users
            //this would cause them to not show up even when online
            //there is no way to unblock them unfortunately
            block = true;
            return blocked;
        }

        //update chat window on user side
        public void updateChat(String[] message) {


            TextFlow text = new TextFlow();
            Text text3 = new Text(message[2]);
            Text text1 = new Text(message[1] + ": ");
            text1.setStyle("-fx-font-weight: bold");
            Text text2 = new Text(message[2]);

            text3 = new Text("\n" + message[2]);

            if (message[2].contains("0x0FF00")) {
                Image okImage1 = null;
                try {
                    okImage1 = new Image(new FileInputStream("Slightly_Smiling_Face_Emoji.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage1);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF00", ""));

                text.getChildren().addAll(text1, text3, imageView);

            } else if (message[2].contains("0x0FF01")) {
                Image okImage1 = null;
                try {
                    okImage1 = new Image(new FileInputStream("Smiling_Face_Emoji_with_Blushed_Cheeks.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage1);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF01", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF02")) {
                Image okImage2 = null;
                try {
                    okImage2 = new Image(new FileInputStream("Laughing.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage2);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF02", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF03")) {
                Image okImage3 = null;
                try {
                    okImage3 = new Image(new FileInputStream("thinking.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage3);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF03", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF04")) {
                Image okImage4 = null;
                try {
                    okImage4 = new Image(new FileInputStream("wink.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage4);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF04", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF05")) {
                Image okImage5 = null;
                try {
                    okImage5 = new Image(new FileInputStream("kiss.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage5);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF05", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF06")) {
                Image okImage6 = null;
                try {
                    okImage6 = new Image(new FileInputStream("thumbs.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage6);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF06", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF07")) {
                Image okImage7 = null;
                try {
                    okImage7 = new Image(new FileInputStream("hookem.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage7);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF07", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF08")) {
                Image okImage8 = null;
                try {
                    okImage8 = new Image(new FileInputStream("ok.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage8);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF08", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF09")) {
                Image okImage9 = null;
                try {
                    okImage9 = new Image(new FileInputStream("heart.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImage9);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF09", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF0A")) {
                Image okImageA = null;
                try {
                    okImageA = new Image(new FileInputStream("poop.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageA);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF0A", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF0B")) {
                Image okImageB = null;
                try {
                    okImageB = new Image(new FileInputStream("eggplant.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageB);
                imageView.setFitHeight(16.0);
                imageView.setFitHeight(16.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF0B", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF0C")) {
                Image okImageC = null;
                try {
                    okImageC = new Image(new FileInputStream("EmGif.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageC);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF0C", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF0D")) {
                Image okImageD = null;
                try {
                    okImageD = new Image(new FileInputStream("ms1.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageD);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF0D", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF0E")) {
                Image okImageE = null;
                try {
                    okImageE = new Image(new FileInputStream("gif3.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageE);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF0E", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF0F")) {
                Image okImageF = null;
                try {
                    okImageF = new Image(new FileInputStream("gif4.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageF);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF0F", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF10")) {
                Image okImageG = null;
                try {
                    okImageG = new Image(new FileInputStream("himym.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageG);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF10", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF11")) {
                Image okImageH = null;
                try {
                    okImageH = new Image(new FileInputStream("friends.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageH);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF11", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF12")) {
                Image okImageI = null;
                try {
                    okImageI = new Image(new FileInputStream("homer.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageI);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF12", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else if (message[2].contains("0x0FF13")) {
                Image okImageJ = null;
                try {
                    okImageJ = new Image(new FileInputStream("bigsexy.gif"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(okImageJ);
                imageView.setFitHeight(75.0);
                imageView.setFitHeight(75.0);
                imageView.setPreserveRatio(true);
                text3.setText(text3.getText().replace("0x0FF13", ""));

                text.getChildren().addAll(text1, text3, imageView);
            }
            else {
                text.getChildren().addAll(text1, text2);
            }

            // start message of each chat is always in black
            if (message[1].equals("CONSOLE")) {
                text1.setFill(Color.BLACK);
                text2.setFill(Color.BLACK);
                text3.setFill(Color.BLACK);;
            }

            // if you are the user that sent the message, text should appear in color selected by the user
            else if(message[1].equals(name)) {
                text1.setFill(mine);
                text2.setFill(mine);
                text3.setFill(mine);
            }

            // else, message is from someone else, text should appear in the color chosen
            else {
                text1.setFill(theirs);
                text2.setFill(theirs);
                text3.setFill(theirs);
            }

            text.setMaxWidth(sPane.getWidth());
            text.setBorder(null);
            convo.add(text, 1, messageNo);
            messageNo++;

            sPane.setVvalue(1.0);
        }

       //sends the message the user wants to send into the server
        public void sendMessage(String message) {
            writer.println(ID + separator + name + separator + message);
            writer.flush();
        }
    }

    //poll the server
    // see if there are any updates to the Observable
    class IncomingReader implements Runnable {

        @Override
        public void run() {

            String incoming;

            try {
                while ((incoming = reader.readLine()) != null) {

                    // message received from the server
                    String[] message = incoming.split(separator);

                    // instruction from server is to update
                    if (message[0].equals("GETONLINE")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                loggedIn(message[2].split(nameSeparator));
                            }
                        });
                    }

                  /*  Text text3 = new Text(incoming);

                    if (incoming.contains("0x0FF00")) {
                        Image okImage1 = new Image(new FileInputStream("Slightly_Smiling_Face_Emoji.png"));
                        ImageView imageView = new ImageView(okImage1);
                        imageView.setFitHeight(16.0);
                        imageView.setFitHeight(16.0);
                        imageView.setPreserveRatio(true);
                        text3.setText(text3.getText().replace("0x0FF00", "" +okImage1));
                }
*/
                    // server indicates another user wants to either start messaging or send a message to an existing chat
                    else if (isNumeric(message[0])) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                int ID = Integer.parseInt(message[0]);

                                // chat window ID is already contained within this user's list of open chat windows
                                if (chatWindows.containsKey(ID)) {
                                    if(message[1].equals("CONSOLE") && message[2].equals("Chat refresh requested."))
                                        startChat(message);
                                    else
                                        chatWindows.get(ID).updateChat(message);
                                }

                                // new chat being initiated
                                else {
                                    startChat(message);
                                }
                            }
                        });
                    }

                    // username is taken
                    else if (message[0].equals("USEREXISTS")) {
                        boolean userexists = true;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                userExists(message);
                            }
                        });
                    }

                    // user is already logged in
                    else if(message[0].equals("ALREADYLOGGEDIN")) {
                        boolean alreadyloggedin = true;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                alreadyLogged(message);
                            }
                        });
                    }

                    // server telling client the entered password is incorrect
                    else if (message[0].equals("WRONGPASS")) {
                        boolean wrongpassword = true;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                wrongPass(message);
                            }
                        });
                    }
                }
            } catch (IOException ex) {
                if(ex instanceof SocketException) {}
                else ex.printStackTrace(); 	}
        }
    }


    //could not figure out how to send or accept friend requests
    public int sendFriendRequest(){
        return friend;
    }


    int friend;
    boolean addedfriend = false;
    public int addFriends(){
        return friend;
    }


    private static boolean isNumeric(String str) {
        try {

            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {

            launch(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }



}