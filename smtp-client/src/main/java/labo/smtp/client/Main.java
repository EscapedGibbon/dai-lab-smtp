package labo.smtp.client;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            ConfigManager config = new ConfigManager("config.properties", "victims.txt", "messages.txt");

            // Valide les fichiers
            if (config.getVictims().isEmpty()) {
                throw new IllegalArgumentException("Le fichier des victimes est vide ou invalide.");
            }
            if (config.getMessages().isEmpty()) {
                throw new IllegalArgumentException("Le fichier des messages est vide ou invalide.");
            }
            if (config.getVictims().size() < config.getGroupCount() * 2) {
                throw new IllegalArgumentException("Pas assez de victimes pour former " + config.getGroupCount() + " groupes.");
            }

            SMTPClient smtpClient = new SMTPClient(config.getSmtpServer(), config.getSmtpPort());
            smtpClient.connect();

            Prank prank = new Prank(config.getVictims(), config.getMessages(), config.getGroupCount());
            List<Email> emails = prank.createPranks();

            for (Email email : emails) {
                smtpClient.sendEmail(email);
            }

            smtpClient.disconnect();
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
