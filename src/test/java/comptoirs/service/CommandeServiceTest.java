package comptoirs.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import javax.management.openmbean.TabularData;

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
    private static final Integer ID_COMMANDE_EXISTANTE = 11000;
    private static final Integer ID_COMMANDE_NON_EXISTANTE = 11078;

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
void commandePasEnvoyee(){
    service.enregistreExpédition(ID_COMMANDE_EXISTANTE);
    assertThrows(Exception.class, () -> service.enregistreExpédition(ID_COMMANDE_EXISTANTE));
}

}



 /**
     * Service métier : Enregistre l'expédition d'une commande connue par sa clé
     * Règles métier :
     * - la commande doit exister
     * - la commande ne doit pas être déjà envoyée (le champ 'envoyeele' doit être null)
     * - On met à jour la date d'expédition (envoyeele) avec la date du jour
     * - Pour chaque produit commandé, décrémente la quantité en stock (Produit.unitesEnStock)
     *   de la quantité commandée
     * @param commandeNum la clé de la commande
     * @return la commande mise à jour
     */
    // @Transactional
    // public Commande enregistreExpédition(Integer commandeNum) {
    //     var commande = commandeDao.findById(commandeNum).orElseThrow();

    //     if (commande.getEnvoyeele() != null) {
    //         throw new IllegalStateException("la commande existe déjà");
    //     }
    //     commande.setEnvoyeele(LocalDate.now());

    //     for( Ligne l : commande.getLignes()){
    //         var produit = l.getProduit();
    //         var stock = produit.getUnitesEnStock();
    //         produit.setUnitesEnStock(stock - l.getQuantite());
    //     }

    //     return commande;
    // 