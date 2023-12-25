import static java.lang.System.*;
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.net.*;

public class Launcher extends JPanel{
    static HashMap<String, String> config = new HashMap<String, String>();
    static ArrayList<String> log = new ArrayList<String>();

    public static void main(String args[]) { // args[0] is sublauncher version

        config.put("local-sublauncher_version", "0.0.1");
        config.put("local-launcher_version", "0.0.1");

        config.put("cached_update_file", System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "update.dat");

        loadConfig("config.dat");               // loading configs
        loadConfig(config.get("cached_update_file"));

        JFrame frame = new JFrame("LAUNCHER version " + config.get("local-launcher_version"));
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(String item : config.keySet())
            System.out.println(item + " -> " + config.get(item));

        if(Updater.update() == 1){
            try {
                writeconfig("config.dat");
                //frame.setTitle("Running SubLauncher");
                Runtime.getRuntime().exec(new String[] { "java", "-jar", "SubLauncher.jar"});
                return;
//                frame.setTitle("SubLauncher Executed");
            } catch (IOException err) {                         // TODO: add a UI that tells the user that UH OH error happened that stoped the execution of the code!
                System.out.println("error occured updating launcher, exiting code");
            }
            return;
        }

        try {
            writeconfig("config.dat");
            log.add("Starting Page Assembler, version " + config.get("local-pageasm_version\n\n"));
            frame.setTitle("Running PageASM");
            Process process = Runtime.getRuntime().exec(new String[] { "java", "-jar", "PageAssembler.jar", new File("config.dat").getAbsolutePath() });

            InputStream program_output = process.getInputStream();
            int Character = 0;
            while (process.isAlive()) {
                Character = program_output.read();
                if (Character == -1)
                    continue;
                System.out.print((char) Character);
            }
        } catch (IOException err) {
            log.add("Fatal Error could not open Page Assembler.jar");
        }
        try {
        FileWriter output = new FileWriter(new File("Launcher" + config.get("local-launcher_version") + ".log"));
        for(int i = 0; i < log.size(); i++)
            output.write(log.get(i) + "\n");
        output.close();
        } catch (IOException err){}
    }

    public static boolean loadConfig(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));

            while (file.hasNextLine()) {
                String line = file.nextLine();

                if (line.contains(" "))
                    config.put(line.split(" ")[0].toLowerCase(), line.split(" ")[1]);
            }

            file.close();
            System.out.println("Loaded file " + filename + " to memory");
            log.add("Loaded file " + filename + " to memory");
            return true;
        } catch (IOException err) {
            System.out.println("error loading file " + filename + " to memory");
            log.add("error loading file " + filename + " to memory");
            return false;
        }

    }

    private static boolean writeconfig(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));
            int count = 0;
            while (file.hasNextLine()) {
                count++;
                file.nextLine();
            }
            file = new Scanner(new File(filename));

            String[] new_config = new String[count], Tconfig = new String[count];
            for (int i = 0; i < count; i++) {
                Tconfig[i] = file.nextLine();
                if (Tconfig[i].equals("")) {
                    new_config[i] = "";
                    continue;
                }

                new_config[i] = Tconfig[i].split(" ")[0] + " " + config.get(Tconfig[i].split(" ")[0]);

            }
            file = new Scanner(new File(filename));
            count = 0;

            file.close();

            FileWriter outfile = new FileWriter(new File(filename));

            for (String item : new_config) {

                if (item == null) {
                    outfile.write("\n");
                    continue;
                }

                outfile.write(item + "\n");
            }

            outfile.close();
            return true;
        } catch (IOException err) {

        }
        return false;
    }
}
