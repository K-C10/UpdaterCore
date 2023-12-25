import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class sub extends JPanel {
    private static HashMap<String, String> config = new HashMap<String, String>();
    static String[] default_config = { "updatefile https://raw.githubusercontent.com/K-C10/UpdaterCore/main/update.dat", "local-pageasm_version 0.0.0"};
    
    public static void main(String args[]) {

        config.put("local-sublauncher_version", "0.0.1");

        JFrame frame = new JFrame("SUB LAUNCHER " + config.get("local-sublauncher_version"));
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        System.out.println("Loading Config.dat to memory");

        if (!loadConfig("config.dat")) {
            System.out.println("Error loading Config.dat, rewritting default config.dat");
            try {
                FileWriter fi = new FileWriter(new File("config.dat"));
                for (String line : default_config)
                    fi.write(line + "\n");
                fi.close();
            } catch (IOException err) {
                System.out.println("!CRITICAL ERROR! Default config file could not be written, terminating process");
                return;
            }
            loadConfig("config.dat");
        }




        System.out.println("Downloading update file to > " + System.getProperty("java.io.tmpdir") + "update.dat from " + config.get("updatewebfile"));
        
        web.getfile(config.get("updatefile"), "update.dat", true);
        config.put("cached_update_file", System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "update.dat");

        System.out.println("Update file was downloaded to " + config.get("cached_update_file"));

        loadConfig(config.get("cached_update_file"));
        System.out.println("Sucessfully downloaded and loaded config.dat and update.dat, checking version of launcher");
        for(String item : config.keySet())
            System.out.println(item + " -> " + config.get(item));

        if(checkversion(config.get("local-sublauncher_version"), config.get("sublauncher_version")) || !new File("Launcher.jar").exists())
        {
            System.out.print("New Launcher version is available, downloading ");
            web.getfile(config.get("launcher_webfile"), "Launcher.jar", false);
            System.out.println(" Starting Launcher");
        }

        try {
            Runtime.getRuntime().exec(new String[] { "java", "-jar", "Launcher.jar"});
            frame.dispose();
        } catch (IOException err) {
            System.out.println("IOException on launching Launcher");
        }
    }

    public static boolean loadConfig(String filein) {
        try {
            Scanner file = new Scanner(new File(filein));

            while (file.hasNextLine()) {
                String line = file.nextLine();

                if (line.contains(" "))
                    config.put(line.split(" ")[0].toLowerCase(), line.split(" ")[1]);
            }

            file.close();
            return true;
        } catch (IOException err) {
            return false;
        }

    }

        private static boolean checkversion(String OLD, String NEW) {
        int O_vn = 0, N_vn = 0;
        for (int i = 0; i < 3; i++) {
            O_vn += Integer.parseInt(OLD.split("[.]")[i]) * (i + 1);
            N_vn += Integer.parseInt(NEW.split("[.]")[i]) * (i + 1);
        }
        return O_vn < N_vn;
    }
}
