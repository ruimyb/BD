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

			connection = DriverManager.getConnection(url, user,	passwd);
		}catch(SQLException e){
			System.err.println("FAILED");
			e.printStackTrace();
		}
	

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
					str1.equals("e") )) {
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
			System.out.println("Veuillez choisir un toit, un plancher et un couvercle parmi ceux disponibles:");
			String S_toit = "(SELECT NumToit from Toit) MINUS (SELECT NumToit FROM Ruche)";
			ResultSet rs_toit = stmt.executeQuery(S_toit);
			while (rs_toit.next()){
				System.out.println(rs_toit.getInt("NumToit"));}
			
			Scanner sc_toit = new Scanner(System.in);
			String str_toit = sc_toit.nextLine();
	
			String S_plancher = "(SELECT NumPlancher from PLancher) MINUS (SELECT NumPlancher FROM Ruche)";
			ResultSet rs_plancher = stmt.executeQuery(S_plancher);
			while (rs_plancher.next()){
				System.out.println(rs_plancher.getString("NumPlancher"));}
			
			Scanner sc_plancher = new Scanner(System.in);
			String str_plancher = sc_plancher.nextLine();
	
			String S_couvercle = "(SELECT NumCouvercle from Couvercle) MINUS (SELECT NumCouvercle FROM Ruche)"; 
			ResultSet rs_couvercle = stmt.executeQuery(S_couvercle);
			while (rs_couvercle.next()){
			System.out.println(rs_couvercle.getString("NumCouvercle"));}
			
			Scanner sc_couvercle = new Scanner(System.in);
			String str_couvercle = sc_couvercle.nextLine();
	
			System.out.println("Veuillez indiquer le poids de votre ruche");
			Scanner sc_poids = new Scanner(System.in);
			String str_poids = sc_poids.nextLine();
	
			//Récupération de CodeRuche, 1 s'il n'y pas de ruche
			//+1 au Max(CodeRuche) s'il y a au moins une ruche
			String str_max = "SELECT Max(CodeRuche) From Ruche";
			ResultSet rs_max = stmt.executeQuery(str_max);
			int max = 0;
			if(rs_max.next()){
			max = rs_max.getInt("Max(CodeRuche)") + 1;
			System.out.println(max);}
	
			String S_Ruche = "INSERT INTO Ruche VALUES (" + max + "," + str_plancher + "," + str_couvercle + "," + str_toit + "," + str_poids + ")"; 
			ResultSet rs_ruche = stmt.executeQuery(S_Ruche);
	
			//recuperation du nombre de hausses disponible
			String str_nbHaussesTotal = "SELECT count(NumHausse) FROM Hausse";
			String str_nbHausseUtilise = "SELECT count(NumHausse) FROM EstCorpsRuche";
			ResultSet rs_nbHausseTotal = stmt.executeQuery(str_nbHaussesTotal);
		
			int nbHaussesTotal =0;
			if (rs_nbHausseTotal.next()){
				nbHaussesTotal = rs_nbHausseTotal.getInt("count(NumHausse)");}
			int nbHaussesUtilise = 0;
			ResultSet rs_nbHausseUtilise = stmt.executeQuery(str_nbHausseUtilise);
			if (rs_nbHausseUtilise.next()){
				nbHaussesUtilise = rs_nbHausseUtilise.getInt("count(Numhausse)");}
			int nbHaussesDispo = nbHaussesTotal - nbHaussesUtilise;
			//insertion des hausses, vérifier que nb_hausse<nombre de hausses disponibles
			System.out.println("Combien de hausses souhaitez vous ?");
			Scanner sc_nb_hausses = new Scanner(System.in);
			int nb_hausses = Integer.parseInt(sc_nb_hausses.nextLine());

			while( nb_hausses > nbHaussesDispo){
				String Kwan = "Veuillez saisir un bon nombre de hausses, il y en a " + nbHaussesDispo;
				System.out.println(Kwan);
				nb_hausses = Integer.parseInt(sc_nb_hausses.nextLine());
			}
			//choix des hausses
			System.out.println("Veuillez choisir vos hausses parmi celles disponibles");
			String S_hausses = "select NumHausse, Couleur from Hausse where NumHausse IN((SELECT NumHausse from Hausse) MINUS (SELECT NumHausse FROM EstCorpsRuche))";
			ResultSet rs_hausses = stmt.executeQuery(S_hausses);
			while (rs_hausses.next()){
			System.out.println(rs_hausses.getString("NumHausse"));}
	
			//à faire : vérification du NumHausse saisi, choix_hausse=101..120 et la hausse doit etre disponible
			String S_EstCorpsRuche ;
			ResultSet rs_EstCorpsRuche;
			Scanner sc_choix_hausse = new Scanner(System.in);
			int choix_hausse;
			for (int i=0; i<nb_hausses; i++){
				choix_hausse = Integer.parseInt(sc_choix_hausse.nextLine());
				int contenu;
				String type;
				if (i < 2) {
					type = "CorpsDeLaRuche"; 
					contenu = (int) (Math.random() * 5);
				}else{
					type = "Supplementaire";
					contenu = (int) (Math.random() * 3);
				}	
				S_EstCorpsRuche = "insert into EstCorpsRuche values (" + max + "," + choix_hausse + ",\'" + type + "\')";
				rs_EstCorpsRuche = stmt.executeQuery(S_EstCorpsRuche);
				for ( int j = 0 ; j < 10 ; j++) {
					int etat = (int) (Math.random() * 3);
					String str_etat;
					String str_contenu;
					switch (etat){
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
					switch (contenu){
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
	
					int poids_cadre = (int)(Math.random() * 1000) + 1 ;
	
					String str_cadre = "SELECT count(NumCadre) FROM Cadre";
					ResultSet rs_cadre = stmt.executeQuery(str_cadre);
					int num_cadre = 0;
					while (rs_cadre.next()){
					rs_cadre.getInt("count(NumCadre)");}
	
					String cadre = "INSERT INTO Cadre VALUES (" + str_etat + "," + poids_cadre + "," + num_cadre +"," +choix_hausse +")" ;
	
				}
	
			}
		break;


		case "b": System.out.println("Vous avez choisi d'effectuer un transfert de cadre de couvain");
		//Stockage des deux bons numéros de cadres attendus
		String s_b = "Select c.NumCadre"
				+ " from Ruche r, Cadre c, EstCorpsRuche ECR"
				+ " where c.Contenu = 'couvain' "
				+ "and c.NumHausse = ECR.NumHausse "
				+ "and ECR.CodeRuche = 5 "
				+ "and r.CodeRuche = 5 "
				+ "and ECR.Type = 'CorpsDeLaRuche'";

		ResultSet numCadreRuche5 = stmt.executeQuery(s_b);
		if(!numCadreRuche5.next()){
			System.out.println("Il n'y a pas de cadre en couvain dans le corps de la ruche 5\n");
			System.out.println("Abandons du transfert");
			break; 
		}
		s_b = "Select c.NumCadre " +		
				"from RUCHE r, Cadre c, EstCorpsRuche ECR " + 
				"where c.Contenu = 'construit' "+
				"and c.NumHausse = ECR.NumHausse "+ 
				"and ECR.CodeRuche = r.CodeRuche "+
				"and r.CodeRuche = 3 "+
				"and ECR.Type = 'CorpsDeLaRuche'";
		ResultSet numCadreRuche3 = stmt.executeQuery(s_b);
		if(!numCadreRuche3.next()){
			System.out.println("Il n'y a pas de cadre construit dans la Ruche 3 \n"); 
			System.out.println("Abandons du transfert");
			break;
		} 
		//Retenir les numéros de hausse correspondants
		numCadreRuche5.next();
		numCadreRuche3.next();
		s_b = "Select c.NumHausse from Cadre c where c.NumCadre = " + numCadreRuche5.getString(1);
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
		s_b = "UPDATE Cadre SET NumHausse =" + numHausseCadre3.getString("c.NumHausse") + 
				"WHERE NumCadre =" + numCadreRuche5.getString("c.NumCadre");
		stmt.executeQuery(s_b);
		//Eventuellement faire un affichage du corps de la Hausse de la Ruche 5 pour vérifier"
		//On rend le cadre qui était dans la ruche 3 disponible
		s_b = "UPDATE Cadre SET NumHausse = NULL "
				+ "WHERE NumCadre =" + numCadreRuche3.getString("c.NumCadre");
		stmt.executeQuery(s_b); 
		//On met le cadre ciré dans la bonne hausse. 
		s_b = "Select UPDATE Cadre SET NumHausse = "
				+ numHausseCadre5.getString("c.NumHausse")
				+ "WHERE NumCadre ="
				+ numCadreCireDispo.getString("c.NumCadre");
		break;
		case "c": 
			System.out.println("Vous avez choisi de calculer le poids de la récolte totale \n");
			String s_c = "SELECT Sum(PoidsCadre) FROM Cadre c, Hausse h, EstCorpsRuche ecr, Ruche r WHERE (h.NumHausse = c.NumHausse) AND (c.contenu = 'miel') AND (ecr.Type = 'Supplementaire') AND (r.CodeRuche = ecr.CodeRuche) AND (h.NumHausse = ecr.NumHausse) ";
			ResultSet rs_c = stmt.executeQuery(s_c);
			while (rs_c.next()) {
			System.out.println(rs_c.getString("Sum(PoidsCadre)")) ;
			}
			break;
		case "d": 
			System.out.println("Vous avez choisi de calculer le poids moyen des cadres de hausses en fonction de la race des abeilles \n");
			System.out.println("Veuillez choisir la race de la reine\n");
			String  choixClient_d = sc.nextLine();
			String s_d = "SELECT AVG(PoidsCadre) FROM Cadre c, Ruche r, Essaim e, EstCorpsRuche ecr WHERE (e.Race = '" + choixClient_d + "'') AND (e.CodeRuche = r.CodeRuche) AND (h.NumHausse = c.NumHausse) AND (c.Contenu = 'miel') AND (r.CodeRuche = ecr.CodeRuche) and (h.NumHausse = ecr.NumHausse)";
			ResultSet rs_d = stmt.executeQuery(s_d);
			while (rs_d.next())
			System.out.println(rs_d.getString("AVG(PoidsCadre)")) ;
			break;
		case "e": 
			System.out.println("Vous avez choisi de calculer le nombre de cadre de couvain pour une ruche donnée \n");
			System.out.println("Veuillez choisir le numero de la ruche \n");
			String  choixClient_e = sc.nextLine();
			String s_e = "SELECT COUNT(NumCadre) FROM Cadre c, Ruche r, EstCorpsRuche ecr WHERE (r.CodeRuche = '" + choixClient_e + "') AND (c.Contenu = 'couvain') AND (r.CodeRuche = ecr.CodeRuche) AND (c.NumHausse = ecr.NumHausse)";
			ResultSet rs_e = stmt.executeQuery(s_e);
			while (rs_e.next())
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

