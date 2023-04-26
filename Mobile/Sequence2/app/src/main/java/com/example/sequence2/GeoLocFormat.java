package com.example.sequence2;

/**
 * Une simple classe contenant des primitives pour l'affichage correct des coordonnées GPS
 */
public class GeoLocFormat {

    /**
     * Fournit la latitude sous un format DD.DDDDD
     * @param latitude la valeur de la latitude
     * @return une chaîne contenant la valeur au format DD.DDDDD
     */
    public static String latitudeDDD(double latitude)
    {
        return strDDD(latitude);
    }

    /**
     * Fournit la longitude sous un format DD.DDDDD
     * @param longitude la valeur de la longitude
     * @return une chaîne contenant la valeur au format DD.DDDDD
     */
    public static String longitudeDDD(double longitude)
    {
        return strDDD(longitude);
    }
	
	private static String strDDD(double value)
	{
		return String.format("%.6f",value);
	}

    /**
     * Fournit la latitude sous le format DD°MM'SS"
     * @param latitude la valeur de la latitude
     * @return une chaîne contenant la latitude, par exemple N 47°25'15"
     */
    public static String latitudeDMS(double latitude)
    {
        // calcule Nord/Sud
        String ns;
        if(latitude<0) {
            ns = "S";
            latitude = -latitude;
        }
        else{
            ns = "N";
        }
        // calcule les 3 composantes degrés, minutes, secondes
        int degres = (int)(latitude);
        double reste = latitude-degres;
        int minutes = (int)(60*reste);
        reste = latitude-degres-minutes/60.0;
        int secondes = (int)(3600*reste);

        return String.format("%s %d°%02d'%02d\"",ns,degres,minutes,secondes);
    }

    /**
     * Fournit la longitude sous le format DD°MM'SS"
     * @param longitude la valeur de la longitude
     * @return une chaîne contenant la longitude, par exemple W 47°25'15"
     */
    public static String longitudeDMS(double longitude)
    {
        // calcule est/ouest
        String ew;
        if(longitude<0) {
            ew = "W";
            longitude = -longitude;
        }
        else{
            ew = "E";
        }
        // calcule les 3 composantes degrés, minutes, secondes
        int degres = (int)(longitude);
        double reste = longitude-degres;
        int minutes = (int)(60*reste);
        reste = longitude-degres-minutes/60.0;
        int secondes = (int)(3600*reste);

        return String.format("%s %d°%02d'%02d\"",ew,degres,minutes,secondes);
    }
}
