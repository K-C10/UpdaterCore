import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import static java.lang.System.*;

public class web {
    public static boolean getfile(String WebUrl, String filename, boolean IsTemporary) {
        try {
            URL url = new URL(WebUrl);

            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fos;
            if(IsTemporary)
                fos = new FileOutputStream(new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + filename));
            else 
                fos = new FileOutputStream(new File(filename));

            byte[] buffer = new byte[1024];

            int count = 0, chunks_used = 0;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                System.out.print(Arrays.toString(buffer));
                fos.write(buffer, 0, count);
                chunks_used++;
            }

            System.out.print("Done! chunks used " + chunks_used);

            bis.close();
            fos.close();
            return true;

        } catch (MalformedURLException err) {
            out.println("Malformed URL Exception raised on " + WebUrl);
        } catch (FileNotFoundException err) {
            out.println("Error on " + filename + ", file was not found!");
        } catch (UnknownHostException err) {
            out.println("NameServer was not able to be contacted");
        } catch (IOException err) {
            out.println("Local file could not be written, the root file might not have write permissions");
        }
        return false;
    }
}
