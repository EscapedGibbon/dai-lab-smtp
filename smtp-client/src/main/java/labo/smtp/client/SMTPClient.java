package labo.smtp.client;

import java.io.*;
import java.net.Socket;

public class SMTPClient {
    private String smtpServer;
    private int smtpPort;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public SMTPClient(String smtpServer, int smtpPort) {
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
    }

    public void connect() throws IOException {
        socket = new Socket(smtpServer, smtpPort);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        readResponse(); // Read server response
    }

    private String readResponse() throws IOException {
        String response = reader.readLine();
        System.out.println("SERVER: " + response);
        return response;
    }

    private void sendCommand(String command) throws IOException {
        writer.println(command);
        System.out.println("CLIENT: " + command);
        readResponse();
    }

    public void sendEmail(Email email) throws IOException {
        sendCommand("HELO prank-client");
        sendCommand("MAIL FROM:<" + email.getSender() + ">");

        for (String recipient : email.getRecipients()) {
            sendCommand("RCPT TO:<" + recipient + ">");
        }

        sendCommand("DATA");
        writer.println("Subject: " + email.getSubject());
        writer.println();
        writer.println(email.getBody());
        writer.println(".");
        writer.flush();
        readResponse(); // End of DATA

        sendCommand("QUIT");
    }

    public void disconnect() throws IOException {
        socket.close();
    }
}
