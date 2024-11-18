package labo.smtp.client;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigManager config = new ConfigManager("config.properties", "victims.txt", "messages.txt");
            SMTPClient smtpClient = new SMTPClient(config.getSmtpServer(), config.getSmtpPort());
            smtpClient.connect();

            Prank prank = new Prank(config.getVictims(), config.getMessages(), config.getGroupCount());
            List<Email> emails = prank.createPranks();

            for (Email email : emails) {
                smtpClient.sendEmail(email);
            }

            smtpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
