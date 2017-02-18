/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dismap;

/**
 *
 * @author bastin
 */
public interface protocoleDISMAP {
    public final static int REQUEST_LOGIN = 1;
    public final static int REQUEST_LOGOUT = 2;
    public final static int REQUEST_SEARCH_GOODS = 3;
    public final static int REQUEST_TAKE_GOODS = 4;
    public final static int REQUEST_BUY_GOODS = 5;
    public final static int REQUEST_DELIVERY_GOODS = 6;
    public final static int REQUEST_LIST_SALES = 7;
    
    public final static int REQUEST_LIST_TYPES_PRECIS = 8;
    public final static int REQUEST_LIST_CLIENTS = 9;
    public final static int REQUEST_SEARCH_BASKET_CLIENT = 10;
    public final static int REQUEST_SUM_BASKET = 11;
    public final static int REQUEST_LIST_ITEMS_BASKET = 12;
    
    public final static int REQUEST_STATS_SOLD_APPAREILS = 13;
    public final static int REQUEST_STATS_TURNOVER = 14;
    public final static int REQUEST_LIST_VILLES = 15;
    
    //public final static int REQUEST_SALE_INFO = 13;

    public final static int REQUEST_INTERRUPT = 0;
}
