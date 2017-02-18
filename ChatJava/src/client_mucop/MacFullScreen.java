/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_mucop;

import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author florentcardoen
 */
public class MacFullScreen {
    public static void setupDisplay(Window win, boolean toggle)
    {
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
            MacFullScreen.enableOSXFullscreen(win);
            if(toggle)
                MacFullScreen.toggleOSXFullscreen(win);
            MacFullScreen.enableOSXQuitStrategy();
        }
    }
    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException | NoSuchMethodException |
                SecurityException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException exp) {
            exp.printStackTrace(System.err);
        }
    }

    //Application.getApplication().requestToggleFullScreen(window);
    public static void toggleOSXFullscreen(Window window) {
        try {
            Class application = Class.forName("com.apple.eawt.Application");
            Method getApplication = application.getMethod("getApplication");
            Object instance = getApplication.invoke(application);
            Method method = application.getMethod("requestToggleFullScreen", Window.class);
            method.invoke(instance, window);
        } catch (ClassNotFoundException | NoSuchMethodException |
                SecurityException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException exp) {
            exp.printStackTrace(System.err);
        }
    }

    //Application.getApplication().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
    public static void enableOSXQuitStrategy() {
        try {
            Class application = Class.forName("com.apple.eawt.Application");
            Method getApplication = application.getMethod("getApplication");
            Object instance = getApplication.invoke(application);
            Class strategy = Class.forName("com.apple.eawt.QuitStrategy");
            Enum closeAllWindows = Enum.valueOf(strategy, "CLOSE_ALL_WINDOWS");
            Method method = application.getMethod("setQuitStrategy", strategy);
            method.invoke(instance, closeAllWindows);
        } catch (ClassNotFoundException | NoSuchMethodException |
                SecurityException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException exp) {
            exp.printStackTrace(System.err);
        }
    }
}
