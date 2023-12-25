import static java.lang.System.*;
import java.util.*;
import java.io.*;

public class Updater {
    public static int update() {
        try {
            if(checkversion(Launcher.config.get("local-sublauncher_version"), Launcher.config.get("sublauncher_version"))) {
                Launcher.log.add("Downloading new SubLauncher");
                web.getfile(Launcher.config.get("sublauncher_webfile"), "SubLauncher.jar", false);
                System.out.println(" - DONE! restarting Launch process");
                return 1;
            }   

            if(checkversion(Launcher.config.get("local-pageasm_version"), Launcher.config.get("pageasm_version")) || !new File("PageAssembler.jar").exists()) {
                Launcher.log.add("Downloading new Page Assembler code");
                System.out.println("Downloading new Page Assembler code");
                web.getfile(Launcher.config.get("pageasm_webfile"), "PageAssembler.jar", false);
                System.out.println(" - DONE!");
            }

            return 0;
        } catch( NullPointerException err) {
            System.out.println("Could not update : Null Pointer Exception -+> " + err);
            Launcher.log.add("Could not update : Null Pointer Exception -+> " + err);
            err.printStackTrace();
            return -1;
        }
    }

    private static boolean checkversion(String OLD, String NEW) {
        
        int O_vn = 0, N_vn = 0;
        System.out.println("Comparing version " + OLD + " to " + NEW);
        Launcher.log.add("Comparing version " + OLD + " to " + NEW);

        for (int i = 0; i < 3; i++) {
            O_vn += Integer.parseInt(OLD.split("[.]")[i]) * (i + 1);
            N_vn += Integer.parseInt(NEW.split("[.]")[i]) * (i + 1);
        }
        return O_vn < N_vn;
    }
}
