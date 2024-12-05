package labo.smtp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
        System.out.println("Connexion au serveur SMTP...");
        socket = new Socket(smtpServer, smtpPort);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        System.out.println("Connecté au serveur SMTP.");
        readResponse(); // Lire la réponse initiale du serveur
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            sendCommand("QUIT");
            System.out.println("Déconnexion du serveur SMTP...");
            socket.close();
            System.out.println("Déconnecté du serveur SMTP.");
        }
    }

    private String readResponse() throws IOException {
        String response = reader.readLine();
        System.out.println("SERVER: " + response);
        if (response.startsWith("4") || response.startsWith("5")) {
            throw new IOException("Erreur SMTP : " + response);
        }
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

        // Construire et envoyer l'e-mail
        writer.println("Subject: " + email.getSubject());
        writer.println();
        writer.println(email.getBody());
        writer.println(".");
        writer.flush();
        System.out.println("E-mail envoyé au serveur SMTP.");

        // Ajouter un délai ici
        try {
            Thread.sleep(1000); // Délai d'attente de 1 seconde
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Lire la réponse après l'envoi des données
        String response = readResponse();
        if (!response.startsWith("250")) {
            throw new IOException("Erreur lors de l'envoi du message : " + response);
        }

        sendCommand("QUIT");
    }
}
