
package scheduler;

import javax.swing.*;

public class Main {

    public static void main(String[] args) { 
        try {
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", String.valueOf(true));
            boolean lookAndFeel = false;
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if (laf.getName().equalsIgnoreCase("GTK+") || laf.getName().equalsIgnoreCase("Windows Vista")) {
                    UIManager.setLookAndFeel(laf.getClassName());
                    lookAndFeel = true;
                    break;
                }
            }
            UIDefaults uiDefaults = UIManager.getDefaults();
            uiDefaults.put("TextArea.font", uiDefaults.get("TextPane.font"));
            if (!lookAndFeel) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }catch(Exception e){}
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ScheduleVisualiser visualS = new ScheduleVisualiser();
            }
        });
    }

}
