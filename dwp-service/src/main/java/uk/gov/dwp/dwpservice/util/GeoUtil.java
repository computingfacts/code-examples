package uk.gov.dwp.dwpservice.util;

/**
 * Utility class that computes the distance between two given Coordinates and
 * check if the distance between them is not more than the given radius in
 * miles.
 *
 * @author joseph
 */
public final class GeoUtil {

    private static final double EARTH_RADIUS = 3956;

    private GeoUtil() {
    }

    /**
     *
     * @param  latitude1
     * @param  latitude2
     * @param  longitude1
     * @param  longitude2
     * @param radius radius in miles
     * @return true is the distance is <= given radius in miles
     */
    public static boolean isWithinCoordinates(double latitude1, double latitude2, double longitude1, double longitude2, int radius) {

        double startLongitude = Math.toRadians(longitude1);
        double endLongitude = Math.toRadians(longitude2);
        double startLatitude = Math.toRadians(latitude1);
        double endLatitude = Math.toRadians(latitude2);

        // Haversine formula  
        double lon = endLongitude - startLongitude;
        double lat = endLatitude - startLatitude;
        double a = Math.pow(Math.sin(lat / 2), 2)
                + Math.cos(startLatitude) * Math.cos(endLatitude)
                * Math.pow(Math.sin(lon / 2), 2);

        double arc = 2 * Math.asin(Math.sqrt(a));
        double distanceInMiles = (arc * EARTH_RADIUS);
        return distanceInMiles <= radius;
    }

}
