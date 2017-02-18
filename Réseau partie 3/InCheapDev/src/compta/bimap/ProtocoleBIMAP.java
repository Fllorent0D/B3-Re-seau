/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.bimap;

/**
 *
 * @author bastin
 */
public interface ProtocoleBIMAP {
    public static final int REQUEST_INTERRUPT = 0;
    public static final int REQUEST_LOGIN = 1;
    public static final int REQUEST_MAKE_BILL = 2;
    public static final int REQUEST_GET_NEXT_BILL = 3;
    public static final int REQUEST_VALIDATE_BILL = 4;
    public static final int REQUEST_LIST_BILLS = 5;
    public static final int REQUEST_SEND_BILLS = 6;
    public static final int REQUEST_HANDSHAKE = 7;
}
