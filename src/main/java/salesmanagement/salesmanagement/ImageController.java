package salesmanagement.salesmanagement;

import javafx.scene.image.Image;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ImageController {
    public static Image orderIcon = getImage("order_icon.png");
    public static Image blueOrderIcon = getImage("blue_order_icon.png");
    public static Image employeeIcon = getImage("employee_icon.png");
    public static Image blueEmployeeIcon = getImage("blue_employee_icon.png");
    public static Image newsIcon = getImage("news_icon.png");
    public static Image blueNewsIcon = getImage("blue_news_icon.png");
    public static Image settingsIcon = getImage("settings_icon.png");
    public static Image blueSettingsIcon =getImage("blue_settings_icon.png");
    public static Image productIcon = getImage("product_icon.png");
    public static Image blueProductIcon =getImage("blue_product_icon.png");
    public static Image customerIcon = getImage("customer_icon.png");
    public static Image blueCustomerIcon = getImage("blue_customer_icon.png");

    public static Image getImage(String imageName) {
        return new Image(Objects.requireNonNull(SalesManagement.class.getResourceAsStream("/salesmanagement/salesmanagement/img/" + imageName)));
    }
    private final static String server = "files.000webhost.com";
    private final static int port = 21;
    private final static String user = "sama-support";
    private final static String pass = "sama-support";
    public static void uploadImage(String url, String remoteName) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File firstLocalFile = new File(url);

            String firstRemoteFile = "public_html/" + remoteName;
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static Image getImage(String remote_name, boolean fromFTP) {
        if(fromFTP) {
            return new Image("https://sama-support.000webhostapp.com/" + remote_name);
        }
        return null;
    }
    public static boolean isImageLoaded(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
