import java.sql.*;
import java.io.*;
import java.util.*;

public class tp {
    public static void main(String[] args) {
	Connection connection = null;
	try {
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	    String url = "jdbc:oracle:thin:@ensioracle1.imag.fr:"
		+ "1521:ensioracle1";

	    String user = "halbg";
	    String passwd = "Coucou";

	    connection = DriverManager.getConnection(url, user,	passwd);
	    Statement stmt = connection.createStatement();

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
		     str1.equals("d") )) {
	    	System.out.println("Veuillez saisir à nouveau votre requete: \n");
		System.out.println("(a) Assembler une ruche et y placer un essaim  \n");
		System.out.println("(b) Transfert d'un cadre de couvain\n");
		System.out.println("(c) Calculer le poids de la récolte totale \n");
		System.out.println("(d) Calculer le poids moyen des cadres de hausse en fonction de la race des abeilles\n");
		System.out.println("(e) Calculer le nombre de cadres de couvain pour une ruche\n");
		str1 = sc.nextLine();
	    }
	    switch (str1) {
	    case "a": System.out.println("Vous avez choisi d'assembler une ruchet et d'y placer un essaim");
		break;
		case "b": System.out.println("Vous avez choisi d'effectuer un transfert de cadre de couvain");
				//Stockage des deux bons numéros de cadres attendus
				String s_b = "Select c.NumCadre "+ "from RUCHE r, Cadre c, Hausse h, EstCorpsRuche ECR" + 
										"where c.Contenu = 'couvain'" + 
										"where c.NumHausse = ECR.NumHausse"+
										"where ECR.CodeRuche = r.CodeRuche" +
										"where r.CodeRuche = 5" +
										"where ECR.CHECK= 'CorpsDeLaRuche'";
				ResultSet numCadreRuche5 = stmt.executeQuery(s_b); 
				if(numCadreRuche5 == null){
					System.out.println("Il n'y a pas de cadre en couvain dans le corps de la ruche 5\n");
					System.out.println("Abandons du transfert");
					break; 
				}
				s_b = "Select c.NumCadre" +
										"from RUCHE r, Cadre c, Hausse h, EstCorpsRuche ECR" + 
										"where c.Contenu = 'construit'"+
										"where c.NumHausse = ECR.NumHausse"+ 
										"where ECR.CodeRuche = r.CodeRuche"+
										"where r.CodeRuche = 3"+
										"where ECR.CHECK= 'CorpsDeLaRuche'";
				ResultSet numCadreRuche3 = stmt.executeQuery(s_b);
				if(numCadreRuche3 == null){
					System.out.println("Il n'y a pas de cadre construit dans la Ruche 3 \n"); 
					System.out.println("Abandons du transfert");
					break;
				} 
				//Retenir les numéros de hausse correspondants
				s_b = "Select c.NumHausse from Cadre c where c.NumCadre =" + numCadreRuche5.getString(1); 
				ResultSet numHausseCadre5 = stmt.executeQuery(s_b);
				s_b = "Select c.NumHausse from Cadre c where c.NumCadre =" + numCadreRuche3.getString(1); 
				ResultSet numHausseCadre3 = stmt.executeQuery(s_b);
				//On cherche à cadre ciré potentiellement disponible pour le corps de la ruche 5
				s_b = "Select c.NumCadre from Cadre c where c.Contenu = 'cire' where c.NumHausse = NULL";
				ResultSet numCadreCireDispo = stmt.executeQuery(s_b);
				if(numCadreCireDispo == null){
					System.out.println("Il n'y a aucun cadre ciré disponible.\n"); 			
					System.out.println("Abandons du transfert");
					break; 
				}
				//Mise à jour du numéro de hausse du cadre à changer. 
				s_b = "UPDATE Cadre SET NumHausse =" + numHausseCadre3.getString(1) + 
							 "WHERE NumCadre =" + numCadreRuche5.getString(1);
				stmt.executeQuery(s_b);
				//Eventuellement faire un affichage du corps de la Hausse de la Ruche 5 pour vérifier"
				//On rend le cadre qui était dans la ruche 3 disponible
				s_b = "UPDATE Cadre SET NumHausse = NULL WHERE NumCadre =" + numCadreRuche3.getString(1);
				stmt.executeQuery(s_b); 
				
				s_b = "Select UPDATE Cadre SET NumHausse = LeBonNumeroDeHausse1 WHERE NumCadre = LeNouveauNumeroDeCadre";


		break;
		case "c": 
				System.out.println("Vous avez choisi de calculer le poids de la récolte totale \n");
	    		String s_c = "SELECT Sum(PoidsCadre) FROM Cadre c, Hausse h, EstCorpsRuche ecr, ruche r WHERE (h.NumHausse = c.NumHausse) AND (c.contenu = 'miel') AND (ecr.Type = Supplementaire) AND (r.CodeRuche = ecr.CodeRuche) AND (h.NumHausse = ecr.NumHausse) ";
	    		ResultSet rs_c = stmt.executeQuery(s_c);
	    		//	while (rs_c.next()) {
				System.out.println(rs_c.getString("Sum(PoidsCadre)")) ;
	    		//}
		break;
		case "d": 
				System.out.println("Vous avez choisi de calculer le poids moyen des cadres de hausses en fonction de la race des abeilles \n");
				System.out.println("Veuillez choisir la race de la reine\n");
				String  choixClient_d = sc.nextLine();
				String s_d = "SELECT AVG(PoidsCadre) FROM Cadre c, Ruche r, Essaim e, EstCorpsRuche ecr WHERE (e.Race = '" + choixClient_d + "'') AND (e.CodeRuche = r.CodeRuche) AND (h.NumHausse = c.NumHausse) AND (c.Contenu = 'miel') AND (r.CodeRuche = ecr.CodeRuche) and (h.NumHausse = ecr.NumHausse)";
				ResultSet rs_d = stmt.executeQuery(s_d);
				System.out.println(rs_d.getString("AVG(PoidsCadre)")) ;
		break;
		case "e": 
				System.out.println("Vous avez choisi de calculer le nombre de cadre de couvain pour une ruche donnée \n");
				System.out.println("Veuillez choisir le numero de la ruche \n");
				String  choixClient_e = sc.nextLine();
				String s_e = "SELECT COUNT(NumCadre) FROM Cadre c, Ruche r, EstCorpsRuche ecr WHERE (r.CodeRuche = '" + choixClient_e + "') AND (c.Contenu = 'couvain') AND (r.CodeRuche = ecr.CodeRuche) AND (c.NumHausse = ecr.NumHausse)";
				ResultSet rs_e = stmt.executeQuery(s_e);
				System.out.println(rs_e.getString("AVG(PoidsCadre)")) ;
		break;
	    default: System.out.println("Mauvais choix, recommencez");
		break;
	    }

	    /*String str2 = sc.nextLine();
	    switch (str2) {
	    case "Materiel": System.out.println("Veuillez saisir sur quel composant vous intéresse (NumMateriel ou MatiereMateriel)");
		break;
	    case "Hausse": System.out.println("Veuillez saisir sur quel composant vous intéresse (Code, Couleur, NumMateriel)");  
		break;
	    case "Cadre":  System.out.println("Veuillez saisir sur quel composant vous intéresse (Code, Etat, PoidsCadre, Contenu, NumMateriel)");
		break;
	    case "Couvercle": System.out.println("Veuillez saisir sur quel composant vous intéresse (Code, NumMateriel)");
		break;
	    case "Toit": System.out.println("Veuillez saisir sur quel composant vous intéresse (Code, NumMateriel)"); 
		break;
	    case "Plancher": System.out.println("Veuillez saisir sur quel composant vous intéresse (Code, NumMateriel)"); 
		break;
	    case "Ruche": System.out.println("Veuillez saisir sur quel composant vous intéresse (CodeRuche, CodePlancher, CodeCadre, CodeCouvercle, CodeToit)"); 
		break;
	    case "Essaim": System.out.println("Veuillez saisir sur quel composant vous intéresse (Code, Couleur, Race, Age, CodeRuche)"); 
		break;
	    case "EstCorpsRuche": System.out.println("Veuillez saisir sur quel composant vous intéresse (CodeRuche, CodeHausse, Type)"); 
		break;
	    default: System.out.println("Saisie invalide");
		break;
	    }
	    String str3 = sc.nextLine();
	    String S = str1 + " " + str3 + " FROM " + str2;
	    ResultSet rs = stmt.executeQuery(S);
	    while (rs.next()) {
		System.out.println(rs.getString(str3)) ;
	    }*/
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    if (connection != null){
		try {
		    connection.close ();
		    System.out.println ("Database connection terminated");
		}
		catch (Exception e) { 
		    /* ignore close errors */
		}
	    }
	}
    }
}

