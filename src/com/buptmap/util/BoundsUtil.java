package com.buptmap.util;

import java.lang.Math;

public class BoundsUtil {
	private static final Double EARTH_RADIUS = 6371004.00;  //earth radius (meter)
	private Double perDegree = 111000.00;  //meter per degree
	
    private static final double EPS = 1e-8; //number that small enough, used for accuracy control
    
    /**
     * simplify the result got from method dot
     * @param  d  double  result from method dot
     * @return  double
     * */
    private static double fabs(double d){  
        if(Math.abs(d)<EPS) return 0;  
        else return d > 0 ? 1 : -1; 
    }
    
    /**
     * calculate cross product
     * @param  x0  double  start point's coordinate x
     * @param  y0  double  start point's coordinate y
     * @param  x1  double  target point's coordinate x
     * @param  y1  double  target point's coordinate y
     * @param  x  double  checking point's coordinate x
     * @param  y  double  checking point's coordinate y
     * @return  double
     * */
    private static double x_multi(double x0, double y0, double x1, double y1, double x, double y){  //求叉积
        return (x-x0)*(y1-y0)-(x1-x0)*(y-y0); 
    }
    
    /**
     * calculate side length
     * @param  x0  double  start coordinate x
     * @param  y0  double  start coordinate y
     * @param  x1  double  target coordinate x
     * @param  y1  double  target coordinate y
     * @return  double
     * */
    private static double Len_ab(double x0, double y0, double x1, double y1){
        return Math.sqrt((x0-x1)*(x0-x1)+(y0-y1)*(y0-y1)); 
    }
    
    /**
     * calculate for checking whether the 3 points is upon on line 
     * @param  x0  double  start point's coordinate x
     * @param  y0  double  start point's coordinate y
     * @param  x1  double  target point's coordinate x
     * @param  y1  double  target point's coordinate y
     * @param  x  double  checking point's coordinate x
     * @param  y  double  checking point's coordinate y
     * @return  double
     * */
    private static double dot(double x0, double y0, double x1, double y1, double x, double y){  
        return (x-x0)*(x1-x0)+(y-y0)*(y1-y0); 
    }
    
    /**
     * check whether the point is upon edge of the polygon
     * @param  x0  double  start point's coordinate x
     * @param  y0  double  start point's coordinate y
     * @param  x1  double  target point's coordinate x
     * @param  y1  double  target point's coordinate y
     * @param  x  double  checking point's coordinate x
     * @param  y  double  checking point's coordinate y
     * @return  boolean
     * */
    @SuppressWarnings("unused")
	private static boolean pointOnSegment(double x0, double y0, double x1, double y1, double x, double y){
        return fabs(x_multi(x0,y0,x1,y1,x,y))<=0 && fabs(dot(x0,y0,x1,y1,x,y))>=0; 
    }
    
    /**
     * angle sum decision method, check whether point(x,y) is inside polygon;
     * this method is useful for both convex polygon and reentrant polygon;
     * key point: accuracy control, must set an number small enough, such as 0.00000001
     * @param  x  double  coordinate x
     * @param  y  double  coordinate y
     * @param  frame  Double[]  unit polygon's frame, consisted of many point(x,y) one by one anticlockwise or clockwise 
     * @return  boolean
     * */
    public static boolean inPolygon1(double x, double y, Double[] frame){
        double sum=0.0; 
        System.out.println("frame.length:"+frame.length);
        System.out.println("y:"+x+"y:"+y);
        for(int i = 0; i < frame.length-2; i+=2){
        	System.out.println("i:"+i);
        	double x0 = frame[i], y0 = frame[i+1];  //current start point
            double x1 = frame[i+2], y1 = frame[i+3];  //current target point
            System.out.println("x0:"+x0+" y0:"+y0+" x1:"+x1+" y1:"+y1);
            if((x==x0 && y==y0) || (x==x1 && y==y1)) //check whether the point is same with the point in frame    
            	return true; 
            //if(pointOnSegment(x0,y0,x1,y1,x,y))  //check whether the point on edge of the polygon
                //return true; 
            double a=Len_ab(x,y,x0,y0);  //get 3 side length of the triangle formed by current 3 points
            double b=Len_ab(x,y,x1,y1);   
            double c=Len_ab(x0,y0,x1,y1); 
            
            //calculate angle sum, add if cross product > 0, minus else
            sum+=fabs(x_multi(x0,y0,x1,y1,x,y))*Math.acos((a*a+b*b-c*c)/(2.0*a*b));
        }
        sum=Math.abs(sum);  //if angle sum equals 360, the point is inside polygon, else outside or upon
        if(Math.abs(sum-2.0*Math.PI)<=EPS)  //compare the difference with a number small enough
            return true;  
        return false; 
    }
	
    /**
     * 水平/垂直交叉点数判别法（适用于任意多边形）
     * @param  x  double  待判断点的x坐标
     * @param  y  double  待判断点的y坐标
     * @param  frame  Double[]  unit的边框点，按顺时针或逆时针方向排列 
     * @return  boolean	是否在该unit内部
     * */
    public static boolean inPolygon (double x, double y, Double[] frame) 
    {
    	int nCross = 0;
    	
    	for (int i = 0; i < frame.length-2; i+=2) { 
    		double x0 = frame[i], y0 = frame[i+1];  //current start point
    		double x1 = frame[i+2], y1 = frame[i+3];  //current target point
    		System.out.println("x0:"+x0+" y0:"+y0+" x1:"+x1+" y1:"+y1);
    		// 求解 y=p.y 与 p1p2 的交点
    		if ( y0 == y1 ) // p1p2 与 y=p0.y平行 
    			continue;
    		if ( y < Math.min(y0, y1) ) // 交点在p1p2延长线上 
    			continue; 
    		if ( y >= Math.max(y0, y1) ) // 交点在p1p2延长线上 
    			continue;
    		// 求交点的 X 坐标 -------------------------------------------------------------- 
    		double xx = (double)(y - y0) * (double)(x1 - x0) / (double)(y1 - y0) + x0;
    		if ( xx > x ) 
    			nCross++; // 只统计单边交点 
    	}
    	// 单边交点为偶数，点在多边形之外 --- 
    	return (nCross % 2 == 1); 
    }
    
	/**
	 * get the bounds of circumscribing square centered by (lat,lon)
	 * @param  lat  double  graphical latitude
	 * @param  lon  double  graphical longitude
	 * @param  radius  double  radius looking in
	 * @return  double[]
	 * */
	public double[] getBounds(double lat, double lon, double radius){
		
		perDegree = 2*Math.PI*EARTH_RADIUS/360.00;
		System.out.println("perDegree:"+perDegree);
		
		double dLat = radius / perDegree;
		double dLon = Math.abs(radius / perDegree /Math.cos(lat));
		
		double nLat = lat + dLat;
		double sLat = lat - dLat;
		double wLon = lon - dLon;
		double eLon = lon + dLon;
		System.out.println("nLat:"+nLat+" sLat:"+sLat+" wLon:"+wLon+" eLon:"+eLon);
		return new double[] {nLat,sLat,wLon,eLon};
	}
	
	/**
	 * get the distance between two point by format Haversine
	 * haversine(d/r)=haversine(a2-a1)+cos(a1)cos(a2)haversine(|b1-b2|)
	 * haversine(0)=sin(0/2)*sin(0/2)
	 * @param  centerLat  double  source latitude
	 * @param  centerLon  double  source longitude
	 * @param  targetLat  double  target latitude
	 * @param  targetLon  double  target longitude
	 * @param  radius  double  source latitude
	 * @return  boolean
	 * */
	public boolean checkInBounds(double centerLat, double centerLon, double targetLat, double targetLon, double radius){
		double radCenterLat = Math.toRadians(centerLat);
		double radCenterLon = Math.toRadians(centerLon);
		double radTargetLat = Math.toRadians(targetLat);
		double radTargetLon = Math.toRadians(targetLon);
		
		double dLat = Math.abs(radCenterLat - radTargetLat);
		double dLon = Math.abs(radCenterLon - radTargetLon);
		
		double h = Haversine(dLat) + Math.cos(radCenterLat)*Math.cos(radTargetLat)*Haversine(dLon);
		double distance = 2*EARTH_RADIUS*Math.asin(Math.sqrt(h));
		System.out.println(" distance:"+distance);
		if(distance <= radius) return true;
		return false;
	}
	
	public double Haversine(double theta){
		double s = Math.sin(theta/2);
		return s*s;
	}
	
}
