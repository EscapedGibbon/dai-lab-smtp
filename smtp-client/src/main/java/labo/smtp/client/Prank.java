package labo.smtp.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Prank {

    private List<String> victims;
    private List<String> messages;
    private int groupCount;

    public Prank(List<String> victims, List<String> messages, int groupCount) {
        this.victims = victims;
        this.messages = messages;
        this.groupCount = groupCount;
    }

    public List<Email> createPranks() {
        List<Email> emails = new ArrayList<>();

        if (victims.size() < 2) {
            throw new IllegalArgumentException("Pas assez de victimes pour créer des groupes.");
        }

        // Mélanger les victimes
        Collections.shuffle(victims);

        List<String> availableVictims = new ArrayList<>(victims);

        for (int i = 0; i < groupCount; i++) {
            // Assurez-vous qu'il reste suffisamment de victimes pour former un groupe
            if (availableVictims.size() < 2) {
                System.out.println("Nombre insuffisant de victimes restantes pour créer un groupe.");
                break;
            }

            // Taille du groupe : entre 2 et 5, ou le nombre restant de victimes
            int groupSize = Math.min(new Random().nextInt(4) + 2, availableVictims.size());

            List<String> group = new ArrayList<>(availableVictims.subList(0, groupSize));
            availableVictims.subList(0, groupSize).clear(); // Retirer les victimes utilisées

            String sender = group.get(0);
            List<String> recipients = group.subList(1, group.size());

            // Vérifier si des messages sont disponibles
            if (messages.isEmpty()) {
                throw new IllegalStateException("Aucun message disponible pour envoyer les e-mails.");
            }

            // Sélectionner un message au hasard
            String message = messages.get(new Random().nextInt(messages.size()));
            String subject = message.split("\n")[0];
            String body = message.substring(subject.length()).trim();

            emails.add(new Email(sender, recipients, subject, body));
        }

        return emails;
    }
}
