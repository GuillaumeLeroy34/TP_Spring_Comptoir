package comptoirs.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
 // Ce test est basé sur le jeu de données dans "test_data.sql"
class CommandeServiceTest {
    private static final String ID_PETIT_CLIENT = "0COM";
    private static final String ID_GROS_CLIENT = "2COM";
    private static final String VILLE_PETIT_CLIENT = "Berlin";
    private static final BigDecimal REMISE_POUR_GROS_CLIENT = new BigDecimal("0.15");
    private static final Integer ID_COMMANDE_LIVREE = 99999;
    private static final Integer ID_COMMANDE_PAS_LIVREE = 99998;
    private static final Integer ID_COMMANDE_NON_EXISTANTE = 10;

    @Autowired
    private CommandeService service;
    @Test
    void testCreerCommandePourGrosClient() {
        var commande = service.creerCommande(ID_GROS_CLIENT);
        assertNotNull(commande.getNumero(), "On doit avoir la clé de la commande");
        assertEquals(REMISE_POUR_GROS_CLIENT, commande.getRemise(),
            "Une remise de 15% doit être appliquée pour les gros clients");
    }

    @Test
    void testCreerCommandePourPetitClient() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertNotNull(commande.getNumero());
        assertEquals(BigDecimal.ZERO, commande.getRemise(),
            "Aucune remise ne doit être appliquée pour les petits clients");
    }

    @Test
    void testCreerCommandeInitialiseAdresseLivraison() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertEquals(VILLE_PETIT_CLIENT, commande.getAdresseLivraison().getVille(),
            "On doit recopier l'adresse du client dans l'adresse de livraison");
    }   

    @Test 
    void commandeEnregistreeExiste(){
        assertThrows(Exception.class, () -> service.enregistreExpédition(ID_COMMANDE_NON_EXISTANTE),
        "l'id de commande n'existe pas");

    }

@Test
void commandeDejaEnvoyee(){
    assertThrows(Exception.class, () -> service.enregistreExpédition(ID_COMMANDE_LIVREE),
    "la commande est déjà enregistrée");
}



    @Test void miseAJourDateEnvoi(){
       var commandeEnvoyee = service.enregistreExpédition(ID_COMMANDE_PAS_LIVREE);
        assertEquals(LocalDate.now(), commandeEnvoyee.getEnvoyeele(),
        "la date d'envoi n'est pas celle du jour actuel");
    }
    }



