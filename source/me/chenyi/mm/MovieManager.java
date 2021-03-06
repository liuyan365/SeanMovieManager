package me.chenyi.mm;

import me.chenyi.jython.Script;
import me.chenyi.jython.ScriptTriggerType;
import me.chenyi.jython.ScriptUtilities;
import me.chenyi.mm.model.DatabaseUtil;
import me.chenyi.mm.service.ServiceUtilities;
import me.chenyi.mm.util.SysUtil;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * Class description goes here
 *
 * @author $Author:$
 * @version $Revision:$
 */
public class MovieManager extends ThreadGroup
{
    private static MovieManagerConfig config;
    private static MovieManagerFrame frame;

    public MovieManager()
    {
        super("Sean's Movie Manager");
    }

    public static MovieManagerFrame getFrame()
    {
        return frame;
    }

    public static MovieManagerConfig getConfig()
    {
        return config;
    }

    public static void main(String[] args)
    {
        Runnable appStarter = new Runnable()
        {
            public void run()
            {
                config = new MovieManagerConfig();

                //if it is mac, make the JMenuBar show properly.
                if (SysUtil.isMac()) {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");

                    // set the name of the application menu item
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sean's Movie Manager");
                }

                List<String> serviceErrors = ServiceUtilities.initMovieService();

                if (serviceErrors != null && serviceErrors.size() > 0)
                {
                    JOptionPane.showMessageDialog(null, serviceErrors.toString());
                }
                List<String> databaseErrors = DatabaseUtil.initDatabase();
                if (databaseErrors != null && databaseErrors.size() > 0)
                {
                    JOptionPane.showMessageDialog(null, databaseErrors.toString());
                    return;
                }

                Map<ScriptTriggerType,Map<String,Script>> scripts = ScriptUtilities.getScripts();
                int scriptCount = 0;
                for (Map<String, Script> map : scripts.values()) {
                    scriptCount += map.size();
                }
                if (scriptCount == 0)
                {
                    ScriptUtilities.initPluginList();
                }

                frame = new MovieManagerFrame();
                frame.setVisible(true);
                ScriptUtilities.executeScripts(ScriptTriggerType.OnAppStart);
            }
        };

        new Thread(new MovieManager(), appStarter, "SeanMovieManager").start();
    }

    public void uncaughtException(Thread t, Throwable ex)
    {
        ex.printStackTrace();
    }
}


