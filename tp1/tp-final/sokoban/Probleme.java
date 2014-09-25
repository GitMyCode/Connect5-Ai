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


    public static double  ratio_space_wall =0;

    private Probleme(){
    }
    
    public static Probleme charger(BufferedReader br) throws IOException{
        // Lire les lignes dans fichiers
        String line = "";
        ArrayList<String> lignes = new ArrayList<String>();

        int max_x=0;
        int max_y=0;
        while((line = br.readLine())!=null && !line.isEmpty()){

            lignes.add(line);
            if(line.length() > max_y) {
                max_y = line.length();
            }
        }
        max_x = lignes.size();

        
        Probleme probleme = new Probleme();
        // Traiter les lignes lue. La grille a lignes.size() lignes.
        List<Case> obstacles = char_to_list(lignes,'#');
        List<Case> les_buts = char_to_list(lignes,'.');
        List<Case> bonhomme = char_to_list(lignes,'@');
        List<Case> blocks = char_to_list(lignes,'$');

        Case[][] grid =make_grid(lignes,max_x,max_y);


        List<Case> test = char_to_list(lignes,'.');

        les_buts.equals(test);

        probleme.grille = new Grille(lignes);
        probleme.grille.setLes_buts(les_buts);
        probleme.grille.setObstacles(obstacles);
        probleme.grille.array_grid = grid;

        probleme.but = new But(les_buts);
        probleme.but.mures = obstacles;
        probleme.but.grid = grid;
        probleme.but.ratio_space_wall = ratio_space_wall;

        probleme.etatInitial = new EtatSokoban(bonhomme.get(0),blocks);


        /*
        matrixdistance

              gloal | goal | goal
        block
        block
        block

         */

        for(int i=0; i<blocks.size();i++){
            for(int j=0; j< blocks.size(); j++){
                Case block = blocks.get(i);
                Case goal  = les_buts.get(j);

                probleme.etatInitial.matrix_distance_goal[i][j] = CheckPath.canGo(block,goal,grid);
            }
        }




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

    private static Case[][] make_grid(List<String> lecture, int max_x, int max_y){

        Case[][] grid = new Case[max_x][max_y];

        int count_space =0;
        int count_wall =0;

        for(int i=0; i< max_x; i++){
            for(int j=0; j< lecture.get(i).length() ; j++){
                char current_char = lecture.get(i).charAt(j);
                if(current_char != '#')
                    current_char = ' ';


                count_space = (current_char == ' ')? count_space+1 : count_space;
                count_wall = (current_char == '#')? count_wall+1 : count_wall;

                Case new_case = new Case(i,j,current_char);
                grid[i][j] = new_case;
            }
        }

        ratio_space_wall = (Double.valueOf(count_space)/Double.valueOf(count_wall));
/*
        System.out.println("Space : "+count_space);
        System.out.println("Wall : "+count_wall);
        System.out.println("Ratio :"+ ratio_space_wall);
*/

        return grid;
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
