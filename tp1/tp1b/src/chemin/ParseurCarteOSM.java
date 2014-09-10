/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package chemin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * Chargeur de carte à partir d'un fichier en format OSM (XML).
 * @author Christophe Gigax
 * @author Éric Beaudry
 * @author Nicolas Sola
 */
public class ParseurCarteOSM extends DefaultHandler2{
    public enum OSMEnum {
        NODE,
        WAY,
        RELATION,
        ND,
        MEMBER,
        TAG,
        INVALID,
        OSM,
        BOUNDS,
        BOUND;
    }
    
    private final TreeMap<Long, Noeud> nodes = new TreeMap<Long, Noeud>();
    
    private final TreeMap<Long, String> namesTmp = new TreeMap<Long, String>();
    private final TreeSet<Long> correctNodeTmp = new TreeSet<Long>();
    
    private long id;
    private double lat;
    private double lon;
    private String nom;
    
    private Route wayTemp;
    
    private static Carte map;
    
    //Variable recovered from the XML
    private boolean wayElement,
            nodeElement,
            quarterElement,
            routeElement,
            inverseElement;
    
    boolean prendreEnCompteSensUnique = false;//Choisir si l'on prend en compte ou non les sens uniques
    
    final static Set<String> OnewayValues = new TreeSet<String>(Arrays.asList(new String[]{"yes", "true", "1", "-1", "reverse"}));
    final static Set<String> TypeHighWays = new TreeSet<String>();
    
    static{
        TypeHighWays.addAll(Arrays.asList(new String[]{"residential", "primary", "primary_link", "secondary", "secondary_link", "tertiary", "tertiary_link"/*, "motorway", "motorway_link"*/, "unclassified"}));
        //TypeHighWays.addAll(Arrays.asList(new String[]{"motorway", "motorway_link"}));
        TypeHighWays.add("cycleway");
        TypeHighWays.add("footway");
        TypeHighWays.add("service");
    }
    
    //Class can't be instanciate from the outside
    private ParseurCarteOSM(){
        map = new Carte();
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {   
               
        switch(OSMEnum.valueOf(qName.toUpperCase())){
            case NODE :
                id = Long.parseLong(attributes.getValue("id"));
                lon = Double.parseDouble(attributes.getValue("lon"));
                lat = Double.parseDouble(attributes.getValue("lat"));
                nodeElement = true;
                nodes.put(id, new Noeud(id, lat, lon));
                break;
            case WAY :
                wayElement = true;
                id = Long.parseLong(attributes.getValue("id"));
                nom = "";
                wayTemp = new Route(id);
                break;
            case ND :
                if (wayElement) {
                    long ref = Long.parseLong(attributes.getValue("ref"));
                    if (nodes.get(ref) != null) {
                        wayTemp.noeuds.add(nodes.get(ref));
                    }
                }
                break;
            case TAG :
                if (wayElement) {
                    if (attributes.getValue("k").equalsIgnoreCase("highway"))
                        routeElement = TypeHighWays.contains(attributes.getValue("v"));

                    if (attributes.getValue("k").equalsIgnoreCase("oneway")) {
                        if(prendreEnCompteSensUnique){
                            if (OnewayValues.contains(attributes.getValue("v")))
                                wayTemp.sensunique = true;

                            if (    attributes.getValue("v").equalsIgnoreCase("-1") 
                                ||  attributes.getValue("v").equalsIgnoreCase("reverse"))
                                inverseElement = true;
                        }
                    }                        

                    if (    attributes.getValue("k").equalsIgnoreCase("admin_level") 
                         && attributes.getValue("v").equalsIgnoreCase("8")) {
                        quarterElement = true;
                    }
                    if (attributes.getValue("k").equalsIgnoreCase("name")) {
                        nom = attributes.getValue("v");
                        wayTemp.setName(nom);
                    }
                }
                else if (nodeElement) {
                    if (attributes.getIndex("k") != -1 && attributes.getValue("k").toLowerCase().contains("name")){
                        nom = attributes.getValue("k");
                        namesTmp.put(id, nom);
                    }
                }
                break;
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(OSMEnum.valueOf(qName.toUpperCase())){
            case WAY :
                if (wayElement) {
                    if (quarterElement){
                    }
                    if (routeElement) {
                        for (Noeud nodeTmp : wayTemp.noeuds)
                            correctNodeTmp.add(nodeTmp.id);

                        if (inverseElement) {
                            ArrayList<Noeud> reverseNodes = new ArrayList<Noeud>();
                            for (int k = wayTemp.noeuds.size() - 1; k >= 0; k--)
                                reverseNodes.add(wayTemp.noeuds.get(k));

                            wayTemp.noeuds = reverseNodes;
                            inverseElement = false;
                        }

                        map.addWay(wayTemp);
                        routeElement = false;
                    }
                }

                wayElement = false;
                break;
            case NODE :
                nodeElement = false;
                break;
        } 
    }
    
    @Override
    public void endDocument() throws SAXException {
        TreeSet<Long> nodeToRemove = new TreeSet<Long>(nodes.keySet());
        nodeToRemove.removeAll(correctNodeTmp);
        nodes.keySet().removeAll(nodeToRemove);
        
        for(Noeud n : nodes.values())
            map.addNode(n);

        //Fermer les quartiers ouverts
        if (quarterElement){
        }
    }
    
    /**
     * Single entry point for creating a MapOSM according to the file in parameter
     * @param file The stream to parse
     * @return The new MapOSM generated
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException 
     */
    public static Carte parse(File file) throws IOException, SAXException, ParserConfigurationException {         
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        saxParser.parse(file, new ParseurCarteOSM());
        
        return map;
    }
}
