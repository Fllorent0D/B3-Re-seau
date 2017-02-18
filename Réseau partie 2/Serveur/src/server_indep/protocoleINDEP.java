/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_indep;

/**
 *
 * @author bastin
 */
public interface protocoleINDEP {
    public final static int REQUEST_LOGIN = 1;
    public final static int GET_STAT_DESCR_CONT= 2;
    public final static int GET_GR_VENTES_REP = 3;
    public final static int GET_GR_VENTES_COMP = 4;
    public final static int GET_STAT_INFER_TEST_CONF = 5;
    public final static int GET_STAT_INFER_TEST_ANOVA = 6;
    public final static int REQUEST_SALE_INFO = 7;
}
