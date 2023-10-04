import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MainProgram {
    
    static ArrayList<String> lireMotsDepuisFichier(String cheminFichier) {
        ArrayList<String> mots = new ArrayList<>();

        try {
            BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier));

            String ligne;

            while ((ligne = lecteur.readLine()) != null) {

                String[] motsLigne = ligne.split(" ");

                for (String mot : motsLigne) {
                    mots.add(mot);
                }
            }

            lecteur.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mots;
    }


    static ArrayList<String> Proposition(int min, String mot, ArrayList<String> Lista){

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i< Lista.size(); i++){
            // System.out.println("oui");
            String MotList = Lista.get(i); 
            if (mot.charAt(0) == MotList.charAt(0) && mot.charAt(1) == MotList.charAt(1) && compute_Levenshtein_distance(mot, MotList) <= min) {
                list.add(MotList);
                if(list.size() == 4)
                    return list;    
            }
        } 
        return list;
    }
    static int compute_Levenshtein_distance(String str1,
                                            String str2)
    {
      
        if (str1.isEmpty())
        {
            return str2.length();
        }
        if (str2.isEmpty())
        {
            return str1.length();
        }
 
         
        int replace = compute_Levenshtein_distance(
              str1.substring(1), str2.substring(1))
              + NumOfReplacement(str1.charAt(0),str2.charAt(0));
 
        
        int insert = compute_Levenshtein_distance(
                         str1, str2.substring(1))+ 1;
 
        
        int delete = compute_Levenshtein_distance(
                         str1.substring(1), str2)+ 1;
 
        
       
        return minm_edits(replace, insert, delete);
    }
 
    static int NumOfReplacement(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }
 
    static int minm_edits(int... nums)
    {
        return Arrays.stream(nums).min().orElse(
            Integer.MAX_VALUE);
    }   
 
    public static boolean TestMot(String MotRecherche, ArrayList<String> Lisitra){ 
        // System.out.println(MotRecherche);
        for(int i = 0; i< Lisitra.size(); i++){
            // System.out.println(Lisitra.get(i));
            String MotList = Lisitra.get(i);
            if (MotRecherche.equals(MotList))
                return true;    
        }       
        return false;
    }



    public static void main(String[] args) {
        String cheminFichier = "liste_francais.txt";
        String cheminPhrase = "texte.txt";
        String cheminGuten = "gutenberg.txt";
        ArrayList<String> mots = lireMotsDepuisFichier(cheminFichier);
        ArrayList<String> motsPhrase = lireMotsDepuisFichier(cheminPhrase);
        ArrayList<String> motGuten = lireMotsDepuisFichier(cheminGuten);

        
        // test
        String User_mot = "abaissee";
        if(TestMot(User_mot, mots))
            System.out.println("Le mot se trouve dans le dictionnaire");
        else
            System.out.println("Le mot ne se trouve pas dans le dictionnaire");
        

        ArrayList<String> lista = new ArrayList<>();

        lista = Proposition(2, User_mot, mots);

        for(int i=0; i<lista.size();i++){
            System.out.println("Les propositions proches du mots sont: "+lista.get(i));
        }

        ArrayList<String> motsErr = new ArrayList<>();
        for(int i = 0 ; i<motsPhrase.size(); i++){
            if(!TestMot(motsPhrase.get(i),mots))
                motsErr.add(motsPhrase.get(i));
        }
        
        Map<String, ArrayList<String>> listaErr = new HashMap<>();
        for(int i=0; i<motsErr.size();i++){
            listaErr.put(motsErr.get(i), Proposition(5,motsErr.get(i),mots));
        }

        for(int i=0; i<motsErr.size();i++){
            ArrayList<String> respList = new ArrayList<>();
            respList = listaErr.get(motsErr.get(i));
            for(int j=0; j<respList.size();j++){
                System.out.println("Les corrections des propositions proches du mot "+motsErr.get(i)+" sont :"+respList.get(j));
            }
        }
    }
}
