/* CHAT ROOM <ServerMain.java>
 * EE422C Project 7 submission by
 * Replace <...> with your actual data
 * Chris Classie
 * CSC2859
 * 16355
 * Slip days used: <0>
 * Fall 2018
 * */



package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerMain {

    private static List<ChatRoom> newChats;
    private static List<ChatRoom> openChats;

    private static Map<String, ClientObserver> userObservers;

    private static final String separator = Character.toString((char) 31);
    private static final String nameSeparator = Character.toString((char) 29);
    private HashMap<String, ArrayList<String>> groupChats;
    private HashMap<String, ArrayList<String>> groupMessages;
    Set<String> onlineClients = new HashSet<>();
    Map<String, Set<String>> liveGroups = new HashMap<>();

    private static String fileName = "C:\\ChatRoom\\users.txt";


    private boolean noUsers = false;
    private boolean addedNewUsers = false;
    private ServerSocket serverSock;




    public ServerMain() throws FileNotFoundException, IOException {

        openChats = new ArrayList<ChatRoom>();
        userObservers = new HashMap<String, ClientObserver>();


        new File("C:\\ChatRoom").mkdir();  //this creates one directory

        File yourFile = new File(fileName);

        // create new file if it doesn't exist
        try {

            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

  //setting up the network sockets for the server

    public void setUpNetworking() throws Exception {
        this.serverSock = new ServerSocket(4242);

        groupChats = new HashMap<String, ArrayList<String>>();
        groupMessages = new HashMap<String, ArrayList<String>>();
        groupMessages.put("Global", new ArrayList<String>());

        int socketOn = 0;

        while (true) {

            Socket clientSocket = serverSock.accept();
            System.out.println("Received connection " + clientSocket);

            ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());

            Thread t = new Thread(new ClientHandler(clientSocket, writer));
            t.start();
            System.out.println("Connection Established");
            socketOn = 1;
        }
    }



     //this checks for messages from the client

    class ClientHandler implements Runnable {
        private ClientObserver writer;
        private BufferedReader reader;

        private Socket sock;
        private Socket tempSock;

        public ClientHandler(Socket clientSocket, ClientObserver writer) {
            this.sock = clientSocket;


            try {

                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                this.writer = writer;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            String message;
            String messageHolder;
            boolean isMessage = false;
            boolean roomCreated = false;

            try {
                while ((message = reader.readLine()) != null) {
                    String[] array = message.split(separator);

                   //message with chat ID

                    if(isNumeric(array[0])) {
                        openChats.get(Integer.parseInt(array[0])).sendMessage(message);
                        isMessage = true;
                    }



                    //creates a chat room when requested

                    else if(array[0].equals("NEWCHAT")) {

                        String[] users = array[2].split(nameSeparator);

                        // check to see if chat with users already exists
                        String[] names = new String[users.length + 1];

                        names[0] = array[1];

                        for(int i = 0; i< users.length; i++) {
                            names[i+1] = users[i];
                            roomCreated = true;


                        }

                        boolean exist = false;
                        int ID = -1;

                        for(ChatRoom room: openChats) {
                            if(room.users.containsAll(Arrays.asList(names))
                                    && room.users.size() == names.length) {
                                roomCreated = false;
                                exist = true;
                                ID = room.getID();
                                break;
                            }
                        }


                        if(!exist) {

                            roomCreated = true;
                            ChatRoom newChat = new ChatRoom();
                            openChats.add(newChat);
                            newChat.setID(openChats.indexOf(newChat));
                            newChat.addUsers(array);

                            String userString = "";


                            for(int i = 0; i < users.length; i++) {
                                userString += users[i] + ", ";
                            }
                            userString += array[1];

                            newChat.sendMessage("" + Integer.toString(newChat.getID()) + separator + "CONSOLE"
                                    + separator + "This is a new chat between: " + userString);
                        }
                        else {
                            ChatRoom chat = openChats.get(ID);
                            chat.sendMessage("" + Integer.toString(chat.getID()) + separator + "CONSOLE"
                                    + separator + "Chat refresh requested.");
                        }
                    }

                    // input username when first creating account
                    else if(array[0].equals("NEWUSER")) {
                        String user = array[1];
                        String pwd = array[2];

                        // check to see if user has already been created in text file

                        boolean userExists = false;
                        Scanner inFile = new Scanner(new FileReader(fileName));
                        while(inFile.hasNext()) {

                            String line = inFile.nextLine();

                            if (line.toUpperCase().contains(("***" + array[1]).toUpperCase())) {
                                userExists = true;
                            }
                        }

                        inFile.close();

                        //user is already online

                        if(userObservers.containsKey(user)
                                || userExists) {

                            userObservers.put("ERRORNAME", this.writer);

                            String error = "USEREXISTS" + separator
                                    + "ERRORNAME" + separator +  "ERRORNAME";

                            String[] tempArray = error.split(separator);

                            ChatRoom temp = new ChatRoom();
                            temp.addUsers(tempArray);
                            temp.sendMessage(error);

                            userObservers.remove("ERRORNAME");
                        }
                        else {
                            userObservers.put(user, this.writer);
                            addedNewUsers = true;

                            PrintWriter out = new PrintWriter(new FileWriter(fileName, true));
                            out.println("***" + user + "###" + pwd);
                            out.close();
                        }


                    }

                    //login attempt
                    else if(array[0].equals("LOGIN")) {


                        String user = array[1];
                        String pwd = array[2];

                        if(userObservers.containsKey(user)) {

                            userObservers.put("ERRORNAME", this.writer);

                            String error = "ALREADYLOGGEDIN" + separator
                                    + "ERRORNAME" + separator +  "ERRORNAME";

                            String[] tempArray = error.split(separator);

                            ChatRoom temp = new ChatRoom();

                            temp.addUsers(tempArray);
                            temp.sendMessage(error);

                            userObservers.remove("ERRORNAME");

                        }
                        else {
                            boolean userExists = false;

                            Scanner inFile = new Scanner(new FileReader(fileName));

                            while(inFile.hasNext()) {

                                String line = inFile.nextLine();

                                if (line.toUpperCase().contains((("***") + array[1]).toUpperCase())
                                        && line.contains("###" + pwd)) {

                                    userExists = true;
                                }
                            }

                            if(userExists) {

                                userObservers.put(user, this.writer);
                                addedNewUsers = true;
                            }
                            else {

                                userObservers.put("ERRORNAME", this.writer);

                                String error = "WRONGPASS" + separator + "ERRORNAME"
                                        + separator +  "ERRORNAME";

                                String[] tempArray = error.split(separator);

                                ChatRoom temp = new ChatRoom();
                                temp.addUsers(tempArray);
                                temp.sendMessage(error);

                                userObservers.remove("ERRORNAME");
                            }



                            inFile.close();
                        }
                    }

                    //gets all online people and prints out # of users on server

                    else if(array[0].equals("GETONLINE")) {

                        if(addedNewUsers) {

                            addedNewUsers = false;
                            Set<String> keys = 	userObservers.keySet();
                            String names = "";

                            // create String with all online user names
                            for(String user: keys) {
                                names += user + nameSeparator;
                            }

                            // return back GETONLINE string with user names separated by nameSeparator
                            names = ("GETONLINE" + separator + array[1] + separator + names);

                            // use chat room observer pattern to send message
                            String[] tempArray = names.split(separator);
                            ChatRoom temp = new ChatRoom();
                            temp.addUsers(tempArray);
                            temp.sendMessage(names);
                        }
                    }

                    // LOGOUT removes user from our HashMap
                    else if(array[0].equals("LOGOUT")) {
                        userObservers.remove(array[1]);
                        addedNewUsers = true;
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();

            }
        }


        public boolean isNumeric(String str) {
            try {
                double d = Double.parseDouble(str);
            }
            catch(NumberFormatException nfe) {
                return false;
            }
            return true;
        }
    }

    //holder of info
    //implements the observable
    class ChatRoom extends Observable {

        public List<String> users = new ArrayList<String>();
        private int ID;

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getID() {
            return ID;
        }

        //array[1] has the username
        //array[2] has the list of suers

        public void addUsers(String[] array) {

            boolean existingUser = false;
            int newUser;
            ArrayList<Integer> userList;

            if(userObservers.containsKey(array[1])) {
                addObserver(userObservers.get(array[1]));
                users.add(array[1]);
                existingUser = false;
            }

            if(array.length > 2){
                String[] otherUsers = array[2].split(nameSeparator);

                for(int i = 0; i < otherUsers.length; i++) {
                    addObserver(userObservers.get(otherUsers[i]));
                    users.add(otherUsers[i]);
                }
            }
        }

       //pushes message to all observers
        public void sendMessage(String message) {

            setChanged();
            notifyObservers(message);
        }
    }

    //cannot get to work properly
    //work on a way to remove people from current groups without having to end chat
    public void removeFromGroup(String userName, String groupName){

        if(groupChats.containsKey(groupName)){

            Set<String> members =liveGroups.get(groupName);

            if(members.contains(userName)){

                members.remove(userName);
            }
            if (members.size() <= 0){

                groupChats.remove(groupName);
            }
        }
    }

    //cannot get to work properly
    //basis for a way to display which members are offline and which are currently online
    //found a way to just display those that are online
    public String[] getCurrentMembers(String groupName){

        if(liveGroups.containsKey(groupName)){

            Set<String> members = liveGroups.get(groupName);
            String[] returnValue = members.toArray(new String[members.size()]);

            return returnValue;
        } else {
            return null;
        }
    }



    public static void main(String[] args) {
        try {
            new ServerMain().setUpNetworking();
        } catch (Exception e) { e.printStackTrace(); }


    }
}