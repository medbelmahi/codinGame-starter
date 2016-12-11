package makeOneFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class OneFileMaker {

    private static final String PLAYER_FILE_PATH = "Player.java";
    private static final String PACKAGE_PATH = "starterpackege";
    private static final String REQUIRED_PACKAGE_FILE = "requiredPackage.cfg";

    private static final int UPDATE_RANGE_IN_SECOND = 10; //seconds

    public static void main(final String[] args) {
        while (true) {
            System.out.println("Starting --> " + new Date());
            final File projectPackage = new File(PACKAGE_PATH);

            String content = "";

            content += getRequiredPackageFileContent();

            content += filesContent(projectPackage);

            makePlayerFile(content);

            try {
                Thread.sleep(UPDATE_RANGE_IN_SECOND * 1000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void makePlayerFile(final String content) {
        final File file = new File(PLAYER_FILE_PATH);

        try (FileOutputStream fop = new FileOutputStream(file)) {

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            final byte[] contentInBytes = content.getBytes(StandardCharsets.UTF_8);

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static String filesContent(final File file) {

        String filesContent = "";

        if (file.isDirectory()) {

            for (final File f : file.listFiles()) {
                filesContent += "\n" + filesContent(f);
            }

        } else {
            BufferedReader br = null;

            try {

                String sCurrentLine;

                br = new BufferedReader(new FileReader(file));

                while ((sCurrentLine = br.readLine()) != null) {
                    if (sCurrentLine.startsWith("package") || sCurrentLine.startsWith("import")) {
                        continue;
                    } else if (sCurrentLine.startsWith("public ")) {
                        sCurrentLine = sCurrentLine.replaceFirst("public ", "");
                    }
                    filesContent += "\n" + sCurrentLine;
                }

            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return filesContent;

    }

    private static String getRequiredPackageFileContent() {

        String requiredPackageFileContent = "";

        BufferedReader br = null;

        try {
            String sCurrentLine;

            FileInputStream fis = new FileInputStream(REQUIRED_PACKAGE_FILE);

            br = new BufferedReader(new InputStreamReader(fis));

            while ((sCurrentLine = br.readLine()) != null) {
                requiredPackageFileContent += "\n" + sCurrentLine;
            }

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        return requiredPackageFileContent;
    }

}
