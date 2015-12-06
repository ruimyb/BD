import java.sql.*;
import java.io.*;
import java.util.*;
import java.lang.Math;

public class tp {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@ensioracle1.imag.fr:"
                    + "1521:ensioracle1";

            String user = "halbg";
            String passwd = "Coucou";

            connection = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            System.err.println("FAILED");
            e.printStackTrace();
        }

// on s'occupe du menu du selection
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir votre requete: \n");
        System.out.println("(a) Assembler une ruche et y placer un essaim  \n");
        System.out.println("(b) Transfert d'un cadre de couvain\n");
        System.out.println("(c) Calculer le poids de la récolte totale \n");
        System.out.println("(d) Calculer le poids moyen des cadres de hausse en fonction de la race des abeilles\n");
        System.out.println("(e) Calculer le nombre de cadres de couvain pour une ruche\n");
        String str1 = sc.nextLine();
        while (!(str1.equals("a") ||
                str1.equals("b") ||
                str1.equals("c") ||
                str1.equals("d") ||
                str1.equals("e"))) {
            System.out.println("Veuillez saisir à nouveau votre requete: \n");
            System.out.println("(a) Assembler une ruche et y placer un essaim  \n");
            System.out.println("(b) Transfert d'un cadre de couvain\n");
            System.out.println("(c) Calculer le poids de la récolte totale \n");
            System.out.println("(d) Calculer le poids moyen des cadres de hausse en fonction de la race des abeilles\n");
            System.out.println("(e) Calculer le nombre de cadres de couvain pour une ruche\n");
            str1 = sc.nextLine();
        }

        try {
            Statement stmt = connection.createStatement();
            switch (str1) {

                case "a":
                    System.out.println("Pour créer une ruche il faut : un toit, un plancher, un couvercle, plusieurs hausses\n");
                    //System.out.println("Veuillez choisir un toit, un plancher et un couvercle parmi ceux disponibles:");
                    
                    //On s'occupe de la saisie du toit disponible que l'on veut utiliser
                    String S_toit = "(SELECT NumToit from Toit) MINUS (SELECT NumToit FROM Ruche)";
                    ResultSet rs_toit = stmt.executeQuery(S_toit);
                    LinkedList<Integer> listeToitDispo = new LinkedList<Integer>();
                    while (rs_toit.next()) {
                        System.out.println(rs_toit.getInt("NumToit"));
                        listeToitDispo.add(rs_toit.getInt("NumToit"));
                    }
                    System.out.println("Veuillez choisir un toit parmi ceux disponibles");
                    Scanner sc_toit = new Scanner(System.in);
                    String str_toit = sc_toit.nextLine();
                    // on vérifie que la saisie est correcte
                    while (!listeToitDispo.contains(Integer.parseInt(str_toit))){
                        System.out.println("Veuillez choisir un toit parmi ceux disponibles");
                        str_toit = sc_toit.nextLine();
                    }

                    
                    //On s'occupe de la saisie du plancher disponible
                    String S_plancher = "(SELECT NumPlancher from PLancher) MINUS (SELECT NumPlancher FROM Ruche)";
                    ResultSet rs_plancher = stmt.executeQuery(S_plancher);
                    LinkedList<Integer> listePlancherDispo = new LinkedList<Integer>();
                    while (rs_plancher.next()) {
                        System.out.println(rs_plancher.getString("NumPlancher"));
                        listePlancherDispo.add(rs_plancher.getInt("NumPlancher"));
                    }
                    System.out.println("Veuillez choisir un plancher parmi ceux disponibles");
                    Scanner sc_plancher = new Scanner(System.in);
                    String str_plancher = sc_plancher.nextLine();
                    while (!listePlancherDispo.contains(Integer.parseInt(str_plancher))){
                        System.out.println("Veuillez choisir un plancher parmi ceux disponibles");
                        str_plancher = sc_plancher.nextLine();
                    }

                    
                    //On s'occupe de la saisie du couvercle
                    String S_couvercle = "(SELECT NumCouvercle from Couvercle) MINUS (SELECT NumCouvercle FROM Ruche)";
                    ResultSet rs_couvercle = stmt.executeQuery(S_couvercle);
                    LinkedList<Integer> listeCouvercleDispo = new LinkedList<Integer>();
                    while (rs_couvercle.next()) {
                        System.out.println(rs_couvercle.getString("NumCouvercle"));
                        listeCouvercleDispo.add(rs_couvercle.getInt("NumCouvercle"));
                    }
                    System.out.println("Veuillez choisir un couvercle parmi ceux disponibles");
                    Scanner sc_couvercle = new Scanner(System.in);
                    String str_couvercle = sc_couvercle.nextLine();
                    while (!listeCouvercleDispo.contains(Integer.parseInt(str_couvercle))){
                        System.out.println("Veuillez choisir un plancher parmi ceux disponibles");
                        str_couvercle = sc_couvercle.nextLine();
                    }
                    
                    //on indique le poids souhaité pour la ruche
                    System.out.println("Veuillez indiquer le poids de votre ruche");
                    Scanner sc_poids = new Scanner(System.in);
                    String str_poids = sc_poids.nextLine();

                    //Récupération de CodeRuche, 1 s'il n'y pas de ruche
                    //+1 au Max(CodeRuche) s'il y a au moins une ruche
                   //on vérifie d'abord s'il y a une ruche qui existe ou non
                    String str_count = "SELECT Count(CodeRuche) FROM Ruche";
                    ResultSet rs_count = stmt.executeQuery(str_count);
                    int max = 0;
                    if (rs_count.next()){
                        max = rs_count.getInt("Count(CodeRuche)");
                    }
                    if (max == 0){
                        //s'il n'y a aucune ruche on choisit un CodeRuche qui vaut 1
                        max = 1;
                    }else{
                        String str_max = "SELECT Max(CodeRuche) From Ruche";
                        ResultSet rs_max = stmt.executeQuery(str_max);
                        if (rs_max.next()) {
                            max = rs_max.getInt("Max(CodeRuche)") + 1;
                        }
                    }
                    String S_Ruche = "INSERT INTO Ruche VALUES (" + max + "," + str_plancher + "," + str_couvercle + "," + str_toit + "," + str_poids + ")";
                    ResultSet rs_ruche = stmt.executeQuery(S_Ruche);

                    //recuperation du nombre de hausses disponible
                    String str_nbHaussesTotal = "SELECT count(NumHausse) FROM Hausse";
                    String str_nbHausseUtilise = "SELECT count(NumHausse) FROM EstCorpsRuche";
                    ResultSet rs_nbHausseTotal = stmt.executeQuery(str_nbHaussesTotal);

                    int nbHaussesTotal = 0;
                    if (rs_nbHausseTotal.next()) {
                        nbHaussesTotal = rs_nbHausseTotal.getInt("count(NumHausse)");
                    }
                    int nbHaussesUtilise = 0;
                    ResultSet rs_nbHausseUtilise = stmt.executeQuery(str_nbHausseUtilise);
                    if (rs_nbHausseUtilise.next()) {
                        nbHaussesUtilise = rs_nbHausseUtilise.getInt("count(Numhausse)");
                    }
                    int nbHaussesDispo = nbHaussesTotal - nbHaussesUtilise;
                    //insertion des hausses, vérifier que nb_hausse<nombre de hausses disponibles
                    System.out.println("Combien de hausses souhaitez vous ?");
                    Scanner sc_nb_hausses = new Scanner(System.in);
                    int nb_hausses = Integer.parseInt(sc_nb_hausses.nextLine());

                    while (nb_hausses > nbHaussesDispo) {
                        String Kwan = "Veuillez saisir un bon nombre de hausses, il y en a " + nbHaussesDispo;
                        System.out.println(Kwan);
                        nb_hausses = Integer.parseInt(sc_nb_hausses.nextLine());
                    }
                    //choix des hausses
                    System.out.println("Veuillez choisir vos hausses parmi celles disponibles");
                    String S_hausses = "select NumHausse, Couleur from Hausse where NumHausse IN((SELECT NumHausse from Hausse) MINUS (SELECT NumHausse FROM EstCorpsRuche))";
                    ResultSet rs_hausses = stmt.executeQuery(S_hausses);
                    LinkedList<Integer> listeHausseDispo = new LinkedList<Integer>();
                    while (rs_hausses.next()) {
                        System.out.println(rs_hausses.getString("NumHausse"));
                        listeHausseDispo.add(rs_hausses.getInt("NumHausse"));
                    }

   
                    String S_EstCorpsRuche;
                    ResultSet rs_EstCorpsRuche;
                    Scanner sc_choix_hausse = new Scanner(System.in);
                    int choix_hausse;
                    for (int i = 0; i < nb_hausses; i++) {

                        choix_hausse = Integer.parseInt(sc_choix_hausse.nextLine());
                        //on vérifie qu'on un bon choix de hausse
                        while (!listeHausseDispo.contains(choix_hausse)){
                            System.out.println("Veuillez choisir une hausse parmi celles disponibles");
                            choix_hausse = Integer.parseInt(sc_choix_hausse.nextLine());
                        }
                        int contenu;
                        String type;
                        if (i < 2) {
                            type = "CorpsDeLaRuche";
                            contenu = (int) (Math.random() * 5);
                        } else {
                            type = "Supplementaire";
                            contenu = (int) (Math.random() * 3);
                        }
                        S_EstCorpsRuche = "insert into EstCorpsRuche values (" + max + "," + choix_hausse + ",\'" + type + "\')";
                        rs_EstCorpsRuche = stmt.executeQuery(S_EstCorpsRuche);
                        for (int j = 0; j < 10; j++) {
                            int etat = (int) (Math.random() * 3);
                            String str_etat;
                            String str_contenu;
                            switch (etat) {
                                case 0:
                                    str_etat = "bon";
                                    break;
                                case 1:
                                    str_etat = "moyen";
                                    break;
                                case 2:
                                    str_etat = "mauvais";
                                    break;
                                default:
                                    str_etat = "moyen";
                                    break;
                            }
                            switch (contenu) {
                                case 0:
                                    str_contenu = "cire";
                                    break;
                                case 1:
                                    str_contenu = "construit";
                                    break;
                                case 2:
                                    str_contenu = "miel";
                                    break;
                                case 3:
                                    str_contenu = "couvain";
                                case 4:
                                    str_contenu = "pollen";
                                    break;
                                default:
                                    str_contenu = "miel";
                                    break;
                            }

                       
                            int poids_cadre = 0;
                            if (str_contenu.equals("miel")) {
                                poids_cadre = (int)(Math.random() * 1000) + 1 ;
                            }
                            

                            //on choisit le materiel
                            /*String S_matiere = "SELECT DISTINCT MatiereMateriel FROM Materiel";
                            ResultSet rs_matiere = stmt.executeQuery(S_matiere);
                            LinkedList<String> listeMatiereDispo = new LinkedList<String>();
                            while(rs_matiere.next()){
                                System.out.println(rs_matiere.getString("MatiereMateriel"));
                                listeMatiereDispo.add(rs_matiere.getString("MatiereMateriel"));
                            }
                            Scanner sc_matiere = new Scanner(System.in);
                            String str_matiere = sc_matiere.nextLine();
                            while (!listeMatiereDispo.contains(str_matiere)){
                                System.out.println("Veuillez choisir une matiere parmi celles disponibles");
                                str_matiere = sc_matiere.nextLine();
                            }
                            System.out.println(str_matiere);
                            //on choisit le numero du materiel
                            String S_materiel = "SELECT NumMateriel FROM Materiel WHERE MatiereMateriel = '" + str_matiere+ "'" ;
                            ResultSet rs_materiel = stmt.executeQuery(S_materiel);
                            LinkedList<Integer> listeMaterielDispo = new LinkedList<Integer>();
                            while(rs_materiel.next()){
                                System.out.println(rs_materiel.getInt("NumMateriel"));
                                listeMaterielDispo.add(rs_materiel.getInt("NumMateriel"));
                            }
                            Scanner sc_materiel = new Scanner(System.in);
                            String str_materiel = sc_materiel.nextLine();
                            while (!listeMaterielDispo.contains(Integer.parseInt(str_materiel))){
                                System.out.println("Veuillez choisir un materiel parmi ceux disponibles");
                                str_materiel = sc_materiel.nextLine();
                            }*/
                            
                            /*System.out.println("Veuillez choisir un NumMateriel disponible");
                            String cadre = "INSERT INTO Cadre VALUES ('" + str_etat + "'," + poids_cadre + ",'"+str_contenu +"'," + str_materiel + "," + choix_hausse + ")";
                            ResultSet rs_création_cadre = stmt.executeQuery(cadre);*/
                        }

                    }
                    //on place maintenant l'essaim 

                    String S_essaim = "SELECT CodeEssaim from Essaim WHERE CodeRuche IS NULL";
                    ResultSet rs_essaim = stmt.executeQuery(S_essaim);
                    LinkedList<Integer> listeEssaimDispo = new LinkedList<Integer>();
                    while (rs_essaim.next()) {
                        System.out.println(rs_essaim.getString("CodeEssaim"));
                        listeEssaimDispo.add(rs_essaim.getInt("CodeEssaim"));
                    }
                    System.out.println("Veuillez choisir l'essaim que vous souhaitez placer dans la ruche");
                    Scanner sc_essaim = new Scanner(System.in);
                    String str_essaim = sc_essaim.nextLine();
                    while (!listeEssaimDispo.contains(Integer.parseInt(str_essaim))){
                        System.out.println("Veuillez choisir un essaim parmi ceux disponibles");
                        str_essaim = sc_essaim.nextLine();
                    }
                    String S_insertion_essaim = "UPDATE Essaim set CodeRuche = " + max + "WHERE CodeEssaim = " + str_essaim;
                    ResultSet rs_insertion_essaim = stmt.executeQuery(S_insertion_essaim);
                    
                    
                    
                    break;


                case "b":
                    System.out.println("Vous avez choisi d'effectuer un transfert de cadre de couvain");
                    //Stockage des deux bons numéros de cadres attendus


                    int CadreRuche5, CadreRuche3, HausseRuche5, HausseRuche3, CadreCire;

                    //AFFICHAGE De ce qui nous interesse à l'état initial


                    String s_b = "Select distinct c.NumCadre, c.NumHausse"
                            + " from Ruche r, Cadre c, EstCorpsRuche ECR"
                            + " where c.Contenu = 'couvain' "
                            + "and c.NumHausse = ECR.NumHausse "
                            + "and ECR.CodeRuche = r.CodeRuche "
                            + "and r.CodeRuche = 5 "
                            + "and ECR.Type = 'CorpsDeLaRuche'";

                    ResultSet resSet = stmt.executeQuery(s_b);
                    if (resSet.next()) {
                        System.out.println("Voici la liste des cadres de la ruche 5 ayant pour contenu du couvain : \n");
                        CadreRuche5 = resSet.getInt(1);
                        HausseRuche5 = resSet.getInt(2);
                        System.out.println(CadreRuche5);
                        while (resSet.next()) {
                            System.out.println(resSet.getInt(1));
                        }
                        System.out.println("\n");
                    } else {
                        System.out.println("Il n'y a pas de cadre en couvain dans le corps de la ruche 5 :");
                        System.out.println("Abandons du transfert");
                        break;
                    }


                    s_b = "Select distinct c.NumCadre, c.NumHausse"
                            + " from Ruche r, Cadre c, EstCorpsRuche ECR"
                            + " where c.Contenu = 'cire' "
                            + "and c.NumHausse = ECR.NumHausse "
                            + "and ECR.CodeRuche = r.CodeRuche "
                            + "and r.CodeRuche = 5 "
                            + "and ECR.Type = 'CorpsDeLaRuche'";

                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadre cire de la Ruche 5 :");
                    while (resSet.next()) {
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");

                    s_b = "Select distinct c.NumCadre, c.NumHausse " +
                            "from RUCHE r, Cadre c, EstCorpsRuche ECR " +
                            "where c.Contenu = 'construit' " +
                            "and c.NumHausse = ECR.NumHausse " +
                            "and ECR.CodeRuche = r.CodeRuche " +
                            "and r.CodeRuche = 3 " +
                            "and ECR.Type = 'CorpsDeLaRuche'";
                    resSet = stmt.executeQuery(s_b);
                    if (resSet.next()) {
                        System.out.println("Voici la liste des cadres dont le contenu est Construit de la Ruche 3 :");
                        CadreRuche3 = resSet.getInt(1);
                        HausseRuche3 = resSet.getInt(2);
                        System.out.println(CadreRuche3);
                        while (resSet.next()) {
                            System.out.println(resSet.getInt(1));
                        }
                        System.out.println("\n");
                    } else {
                        System.out.println("Il n'y a pas de cadre construit dans la Ruche 3 \n");
                        System.out.println("Abandons du transfert");
                        break;
                    }

                    s_b = "Select distinct c.NumCadre, c.NumHausse"
                            + " from Ruche r, Cadre c, EstCorpsRuche ECR"
                            + " where c.Contenu = 'couvain' "
                            + "and c.NumHausse = ECR.NumHausse "
                            + "and ECR.CodeRuche = r.CodeRuche "
                            + "and r.CodeRuche = 3 "
                            + "and ECR.Type = 'CorpsDeLaRuche'";

                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadres couvain de la Ruche 3 :");
                    while (resSet.next()) {
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");

                    //On cherche à cadre ciré potentiellement disponible pour le corps de la ruche 5
                    s_b = "Select distinct c.NumCadre from Cadre c where c.Contenu = 'cire' and c.NumHausse is NULL";
                    resSet = stmt.executeQuery(s_b);
                    if (resSet.next()) {
                        System.out.println("Voici la liste des cadres cire dispo :");
                        CadreCire = resSet.getInt(1);
                        System.out.println(CadreCire);
                        while(resSet.next()){
                            System.out.println(resSet.getInt(1));
                        }
                        System.out.println("\n");
                    } else {
                        System.out.println("Il n'y a aucun cadre ciré disponible.\n");
                        System.out.println("Abandons du transfert");
                        break;
                    }


                    //Mise à jour du numéro de hausse du cadre à changer.
                    System.out.println("*****************************");
                    System.out.println("*****************************");
                    System.out.println("***********ECHANGE***********");
                    System.out.println("*****************************");
                    System.out.println("*****************************");
                    System.out.println("\n");

                    s_b = "UPDATE Cadre SET NumHausse =" + HausseRuche3
                            + "WHERE NumCadre =" + CadreRuche5;
                    stmt.executeQuery(s_b);
                    //Eventuellement faire un affichage du corps de la Hausse de la Ruche 5 pour vérifier"
                    //On rend le cadre qui était dans la ruche 3 disponible
                    s_b = "UPDATE Cadre SET NumHausse =NULL "
                            + "WHERE NumCadre =" + CadreRuche3;
                    stmt.executeQuery(s_b);
                    //On met le cadre ciré dans la bonne hausse.
                    s_b = "UPDATE Cadre SET NumHausse = "
                            + HausseRuche5
                            + "WHERE NumCadre ="
                            + CadreCire;
                    stmt.executeQuery(s_b);



                    s_b = "Select distinct c.NumCadre, c.NumHausse"
                            + " from Ruche r, Cadre c, EstCorpsRuche ECR"
                            + " where c.Contenu = 'couvain' "
                            + "and c.NumHausse = ECR.NumHausse "
                            + "and ECR.CodeRuche = r.CodeRuche "
                            + "and r.CodeRuche = 5 "
                            + "and ECR.Type = 'CorpsDeLaRuche'";

                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadres de la ruche 5 ayant pour contenu du couvain : \n");
                    while (resSet.next()) {
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");

                    s_b = "Select distinct c.NumCadre, c.NumHausse"
                            + " from Ruche r, Cadre c, EstCorpsRuche ECR"
                            + " where c.Contenu = 'cire' "
                            + "and c.NumHausse = ECR.NumHausse "
                            + "and ECR.CodeRuche = r.CodeRuche "
                            + "and r.CodeRuche = 5 "
                            + "and ECR.Type = 'CorpsDeLaRuche'";

                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadre cire de la Ruche 5 :");
                    while (resSet.next()) {
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");


                    s_b = "Select distinct c.NumCadre, c.NumHausse " +
                            "from RUCHE r, Cadre c, EstCorpsRuche ECR " +
                            "where c.Contenu = 'construit' " +
                            "and c.NumHausse = ECR.NumHausse " +
                            "and ECR.CodeRuche = r.CodeRuche " +
                            "and r.CodeRuche = 3 " +
                            "and ECR.Type = 'CorpsDeLaRuche'";
                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadre dont le contenu est Construit de la Ruche 3 :");
                    while (resSet.next()) {
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");


                    s_b = "Select distinct c.NumCadre, c.NumHausse"
                            + " from Ruche r, Cadre c, EstCorpsRuche ECR"
                            + " where c.Contenu = 'couvain' "
                            + "and c.NumHausse = ECR.NumHausse "
                            + "and ECR.CodeRuche = r.CodeRuche "
                            + "and r.CodeRuche = 3 "
                            + "and ECR.Type = 'CorpsDeLaRuche'";

                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadre dont le contenu est couvain de la Ruche 3 :");
                    while (resSet.next()) {
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");

                    s_b = "Select distinct c.NumCadre from Cadre c where c.Contenu = 'cire' and c.NumHausse is NULL";
                    resSet = stmt.executeQuery(s_b);
                    System.out.println("Voici la liste des cadres cire dispo :");
                    while(resSet.next()){
                        System.out.println(resSet.getInt(1));
                    }
                    System.out.println("\n");
                    break;
                case "c":
                    System.out.println("Vous avez choisi de calculer le poids de la récolte totale \n");
                    String s_c = "SELECT Sum(PoidsCadre) FROM Cadre c, Hausse h, EstCorpsRuche ecr, Ruche r WHERE (h.NumHausse = c.NumHausse) AND (c.contenu = 'miel') AND (ecr.Type = 'Supplementaire') AND (r.CodeRuche = ecr.CodeRuche) AND (h.NumHausse = ecr.NumHausse) ";
                    ResultSet rs_c = stmt.executeQuery(s_c);
                    while (rs_c.next()) {
                        System.out.println(rs_c.getString("Sum(PoidsCadre)"));
                    }
                    break;
                case "d":
                    System.out.println("Vous avez choisi de calculer le poids moyen des cadres de hausses en fonction de la race des abeilles \n");
                    System.out.println("Veuillez choisir la race de la reine parmi celles disponibles \n");
                    String str_afficher_race = "(SELECT Race FROM Essaim) MINUS (SELECT Race FROM Essaim e, Ruche r WHERE r.CodeRuche = e.CodeRuche)";
                    ResultSet rs_afficher_race = stmt.executeQuery(str_afficher_race);
                    LinkedList<String> listeRace = new LinkedList<String>();
                    while (rs_afficher_race.next()){
                        System.out.println(rs_afficher_race.getString("Race"));
                        listeRace.add(rs_afficher_race.getString("Race"));
                    }
                    String choixClient_d = sc.nextLine();
                    while (!listeRace.contains(choixClient_d)){
                        System.out.println("Veuillez choisir une race parmi celles proposées");
                        choixClient_d = sc.nextLine();
                    }
                    String s_d = "SELECT AVG(PoidsCadre) FROM Cadre c, Ruche r, Essaim e, EstCorpsRuche ecr, Hausse h WHERE (e.Race = '" + choixClient_d + "') AND (e.CodeRuche = r.CodeRuche) AND (h.NumHausse = c.NumHausse) AND (c.Contenu = 'miel') AND (r.CodeRuche = ecr.CodeRuche) and (h.NumHausse = ecr.NumHausse)";
                    ResultSet rs_d = stmt.executeQuery(s_d);
                    while (rs_d.next())
                        System.out.println(rs_d.getString("AVG(PoidsCadre)"));
                    break;
                case "e":
                    System.out.println("Vous avez choisi de calculer le nombre de cadre de couvain pour une ruche donnée \n");
                    System.out.println("Veuillez choisir le numero de la ruche \n");
                    String str_afficher_ruche = "SELECT CodeRuche FROM Ruche";
                    ResultSet rs_afficher_ruche = stmt.executeQuery(str_afficher_ruche);
                    LinkedList<Integer> listeRuche = new LinkedList<Integer>();
                    while (rs_afficher_ruche.next()){
                        System.out.println(rs_afficher_ruche.getString("CodeRuche"));
                        listeRuche.add(rs_afficher_ruche.getInt("CodeRuche"));
                    }
                    String choixClient_e = sc.nextLine();
                    while (!listeRuche.contains(Integer.parseInt(choixClient_e))){
                        System.out.println("Veuillez choisir une ruche parmi celles proposées");
                        choixClient_e = sc.nextLine();
                    }
                    String s_e = "SELECT COUNT(NumCadre) FROM Cadre c, Ruche r, EstCorpsRuche ecr WHERE (r.CodeRuche = '" + choixClient_e + "') AND (c.Contenu = 'couvain') AND (r.CodeRuche = ecr.CodeRuche) AND (c.NumHausse = ecr.NumHausse)";
                    ResultSet rs_e = stmt.executeQuery(s_e);
                    while (rs_e.next())
                        System.out.println(rs_e.getString("Count(NumCadre)"));
                    break;
                default:
                    System.out.println("Mauvais choix, recommencez");
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) {
                /* ignore close errors */
                }
            }
        }
    }
}

