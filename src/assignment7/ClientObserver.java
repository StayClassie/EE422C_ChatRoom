/* CHAT ROOM <ClientObserver.java>
 * EE422C Project 7 submission by
 * Replace <...> with your actual data
 * Chris Classie
 * CSC2859
 * 16355
 * Slip days used: <0>
 * Fall 2018
 * */




package assignment7;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

class ClientObserver extends PrintWriter implements Observer {

    public ClientObserver(OutputStream out) {
        super(out);
    }

    @Override
    public void update(Observable arg, Object arg1) {
        this.println(arg1);
        this.flush();
    }
}