package com.example.bastin.application_vendeurs_4.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import server.Message;

/**
 * Created by bastin on 12-11-16.
 */

public class Client {
    protected String addr;
    protected int port;
    protected Socket socket;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;

    public void connect() throws IOException {

        InetAddress inetAddr = InetAddress.getByName(addr);

        socket = new Socket(inetAddr, port);
        ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        oos.flush();
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) ois.readObject();
    }

    public void sendMessage(Message msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
