/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

#include <string>
#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

struct Position{
    Position(int l=0, int c=0) : ligne(0), colonne(0){}
    int ligne, colonne;
};

class Grille{
    private:
      vector<string> lignes;
      vector<Position> buts;
      Position positionBonhomme;

    public:
      bool deplacer(char direction);
      bool butSatisfait() const;
      char operator[](const Position& p) const;
      char& operator[](const Position& p);
    
    friend istream& operator >> (istream&, Grille& g);
    friend ostream& operator << (ostream&, const Grille& g);
};

istream& operator >> (istream& is, Grille& g){
    g.lignes.clear();
    g.buts.clear();
    Position p;
    while(is && !is.fail())
    {
        string ligne;
        getline(is, ligne);
        if(ligne.empty())
            break;
        for(p.colonne=0;p.colonne<ligne.size();p.colonne++)
            switch(ligne[p.colonne]){
                case '*':
                case '+':
                    ligne[p.colonne]=' ';
                case ' ':
                case '#':
                case '$':
                    break;
                case '@':
                    g.positionBonhomme = p;
                    ligne[p.colonne]='@';
                    break;
                case '.':
                    ligne[p.colonne]=' ';
                    g.buts.push_back(p);
                    break;
            }
        g.lignes.push_back(ligne);
        p.ligne++;
    }
    return is;
}

ostream& operator << (ostream& os, const Grille& g){
    for(int l=0;l<g.lignes.size();l++)
        os << g.lignes[l] << endl;
    return os;
}

char Grille::operator[](const Position& p) const{
    return lignes[p.ligne][p.colonne];
}
char& Grille::operator[](const Position& p){
    return lignes[p.ligne][p.colonne];
}

bool Grille::deplacer(char direction){
    Position p2 = positionBonhomme;
    Position p3 = positionBonhomme;
    switch(direction){
        case 'N':
            p2.ligne--; p3.ligne-=2;
            break;
        case 'S':
            p2.ligne++; p3.ligne+=2;
            break;
        case 'W':
            p2.colonne--; p3.colonne-=2;
            break;
        case 'E':
            p2.colonne++; p3.colonne+=2;
            break;
        default:
            return false;
    }
    if(operator[](p2)=='#') return false;
    if(operator[](p2)=='$'){
        if(operator[](p3)!=' ') return false;
        operator[](p3) = '$';
    }
    operator[](positionBonhomme) = ' ';
    operator[](p2) = '@';
    positionBonhomme = p2;
    return true;
}

bool Grille::butSatisfait() const{
    for(int b=0;b<buts.size();b++)
        if(operator[](buts[b])!='$')
            return false;
    return true;
}

vector<char> chargerPlan(istream& in){
    vector<char> plan;
    while(in && !in.fail()){
        if(in.peek()=='#'){
            string ligne;
            getline(in, ligne);
        }else{
            char c;
            in >> c >> std::ws;
            if(c=='<'){
                string aucune;
                plan.push_back(c);
                in >> aucune;
                // Robustesse à écrire...
                break; // aucune solution
            }
            if(!in.fail())
                plan.push_back(c);
        }
    }
    return plan;
}

int main(int argc, const char** argv)
{
    const char* nomfichier_grille=NULL;
    const char* nomfichier_resultat=NULL;
    const char* nomfichier_solution=NULL;
    bool verbose=false;
    
    for(int i=1,j=0;i<argc;i++){
        if(argv[i][0]=='-')
            switch(argv[i][1]){
                case 'v':
                    verbose=true;
                    break;
                default:
                    cout << "Option " << argv[i] << " non reconnue!" << endl;
                    return 1;
            }
        else
            switch(j++){
                case 0:
                    nomfichier_grille = argv[i];
                    break;
                case 1:
                    nomfichier_resultat = argv[i];
                    break;
                case 2:
                    nomfichier_solution = argv[i];
                    break;
                default:
                    cout << "Argument en trop : " << argv[i] << endl;
                    return 2;                
            }
    }
    if(nomfichier_resultat==NULL){
        cout << "./valideur-sokoban grille.txt resultat [solution]" << endl;
        return 3;
    }

    Grille g;
    {
        ifstream ingrille(nomfichier_grille);
        if(ingrille.fail()){
            cout << "Impossible d'ouvrir la grille " << nomfichier_grille << endl;
            return 4;
        }
        ingrille >> g;
    }
    vector<char> resultat, solution;
    int longueurSolution=-1;
    {
        ifstream inresultat(nomfichier_resultat);
        if(inresultat.fail()){
            cout << "Impossible d'ouvrir le résultat " << nomfichier_resultat << endl;
            return 5;
        }
        resultat = chargerPlan(inresultat);
        if(verbose)
            cout << "|resultat|=" << resultat.size() << endl;
    }
    if(nomfichier_solution!=NULL){
        ifstream insolution(nomfichier_solution);
        if(insolution.fail()){
            cerr << "Attention : Impossible d'ouvrir la solution!"
                 << nomfichier_solution << endl;
        }else{
            solution = chargerPlan(insolution);
            longueurSolution = solution.size();
            if(verbose)
                cout << "|solution|=" << longueurSolution << endl;
        }
    }
    
    bool resultatcorrect = true;
    if(verbose)
        cout << "Grille lu:\n" << g << endl;
        
    if(resultat.size()>0 && resultat[0]=='<'){
        cout << "AucuneSolution";
        if(longueurSolution==-1) cout << "_?";
        else if(solution.size()>0 && solution[0]=='<') cout << "_OK";
        else cout << "_Echec";
        cout << endl;
        return 0;
    }
    
    
    for(int i=0;i<resultat.size();i++){
        if(verbose)
            cout << "Move #" << (i+1)<< " : " << resultat[i] << endl;
        if(!g.deplacer(resultat[i])){
            resultatcorrect = false;
            cout << "EchecMove" << endl;
            return 0;
        }
        if(verbose)
            cout << g << endl;
    }
    resultatcorrect &= g.butSatisfait();
    
    if(resultatcorrect){
        if(longueurSolution==-1)
            cout << "AuMoinsCorrect";
        else
            if(resultat.size()<longueurSolution) cout << "Meilleur";
            else if(resultat.size()==longueurSolution) cout << "Optimal";
            else cout << "CorrectNonOptimal";
    }else
        cout << "Echec";
    cout << endl;
    
    return 0;
}
