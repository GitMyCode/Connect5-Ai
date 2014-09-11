/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Représente un problème chargé d'un fichier test sokoban??.txt.
 */
public class Probleme {
    public Grille grille;
    public EtatSokoban etatInitial;
    public But but;
    
    private Probleme(){
    }
    
    public static Probleme charger(BufferedReader br) throws IOException{
        // Lire les lignes dans fichiers
        String line = "";
        ArrayList<String> lignes = new ArrayList<String>();
        while((line = br.readLine())!=null && !line.isEmpty()){
            lignes.add(line);
        }
        
        Probleme probleme = new Probleme();
        // Traiter les lignes lue. La grille a lignes.size() lignes.
        List<Case> obstacles = char_to_list(lignes,'#');
        List<Case> les_buts = char_to_list(lignes,'.');
        List<Case> bonhomme = char_to_list(lignes,'@');
        List<Case> blocks = char_to_list(lignes,'$');


        probleme.grille = new Grille(lignes);
        probleme.but = new But(les_buts);
        probleme.etatInitial = new EtatSokoban(bonhomme.get(0),blocks);




        // À compléter...
        
        // Un espace ' ' est une case libre.
        // Un dièse '#' est une case obstacle.
        // Un dollar '$' représente la position initiale d'un bloc. ==> etatInitial.
        // Un point '.' représente la position finale d'un bloc. ==> but.
        // Les blocs sont indistinguables.
        
        // Certains grilles pourraient contenir des astérisques '*' et plus '+'. 
        // Ces symboles peuvent être ignorés et traités comme des espaces ' '.
        
        return probleme;
    }

    private static List<Case> char_to_list(List<String> lecture, char symbole){
        List<Case> result = new ArrayList<Case>();
        for(int i=0; i< lecture.size(); i++){
            for(int j=0; j< lecture.get(i).length(); j++){
                if(lecture.get(i).charAt(j) == symbole){
                    Case c = new Case(i,j, symbole);
                    result.add(c);
                }
            }
        }

        return result;
    }
}
