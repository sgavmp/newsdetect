package com.ikip.newsdetect.main.statistics.impl;

public class PolyTrendLine extends OLSTrendLine {
    final int degree;
    public PolyTrendLine(int degree) {
//        if (degree < 0) throw new IllegalArgumentException(LanguageLoad.getinstance().find("internal/trend/error/negative"));
        this.degree = degree;
    }
    protected double[] xVector(double x) { // {1, x, x*x, x*x*x, ...}
        double[] poly = new double[degree+1];
        double xi=1;
        for(int i=0; i<=degree; i++) {
            poly[i]=xi;
            xi*=x;
        }
        return poly;
    }
    @Override
    protected boolean logY() {return false;}
}
