package com.sej.escape.utils.geolocation;

import lombok.AllArgsConstructor;

public class Harversine {

    private final int EARTH_RADIUS = 6378; // 지구 반지름(기준 km)

    @AllArgsConstructor
    private class Position{
        double lat; // 위도
        double lon; // 경도
    }
    /*
    @reference https://pyxispub.uzuki.live/?p=1006
    */
    public Position getPoisitionFromOrig(Position orig, double distance, double bearing){

        double radLatitude = mapDegreeToRadian(orig.lat);
        double radLongitude = mapDegreeToRadian(orig.lon);
        double radBearing = mapDegreeToRadian(bearing);
        double distRadius = distance / EARTH_RADIUS;

        double latitude = Math.asin( Math.sin(radLatitude) * Math.cos(distRadius) +
                Math.cos(radLatitude) * Math.sin(distRadius) * Math.cos(radBearing));
        var longitude = radLongitude + Math.atan2(
                Math.sin(radBearing) * Math.sin(distRadius) * Math.cos(radLatitude),
                Math.cos(distRadius) - Math.sin(radLatitude) * Math.sin(latitude));
        // normalize
        longitude = (longitude + 540) % 360 - 180;
        
        return new Position(mapRadianToDegree(latitude), mapRadianToDegree(longitude));
    }

    /*
    @reference  http://www.arubin.org/files/geo_search.pdf 8p
                https://en.wikipedia.org/wiki/Haversine_formula
    */
    public double getDistanceBetween(Position orig, Position dest){
        return EARTH_RADIUS * 2 * Math.asin( Math.sqrt(
                        haversin( mapDegreeToRadian(orig.lat - Math.abs(dest.lat) ) )
                        + Math.cos( mapDegreeToRadian(orig.lat) )
                        * Math.cos( mapDegreeToRadian( Math.abs(dest.lat) ) )
                        * haversin( mapDegreeToRadian(orig.lon - dest.lon) )
        ) );
    }

    private double haversin(double val){
        return Math.pow( Math.sin(val / 2), 2 );
    }

    public double mapDegreeToRadian(double degree){
        return degree * Math.PI / 180;
    }

    public double mapRadianToDegree(double radian){
        return radian * 180 / Math.PI;
    }


}
