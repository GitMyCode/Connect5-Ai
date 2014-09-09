/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package chemin;

import java.util.StringTokenizer;

public class Coordonnees {
    private double longitude, latitude;
    public final double RAYONTERRE = 6371000.0;

    public Coordonnees(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double lat) {
        latitude = lat;
    }

    public double getLon() {
        return longitude;
    }

    public void setLon(double lon) {
        longitude = lon;
    }
    
    public double distanceTerre(Coordonnees coor)
    {
    	double lon1=Math.PI*longitude/180.0,
                lon2=Math.PI*coor.longitude/180.0,
                lat1=Math.PI*latitude/180.0,
                lat2=Math.PI*coor.latitude/180.0;
        double s1 = Math.sin((lat2-lat1)/2);
        double s2 = Math.sin((lon2-lon1)/2);
        return 2*RAYONTERRE * Math.asin(Math.sqrt(s1*s1 + Math.cos(lat1)*Math.cos(lat2)*s2*s2));
    }

    @Override
    public String toString() {
        return "(" + latitude + "," + longitude + ")";
    }
    
    public static Coordonnees parseCoordonnees(String sequence){
        StringTokenizer st = new StringTokenizer(sequence, " (),");
        return new Coordonnees(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
    }
}
