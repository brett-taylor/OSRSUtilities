package app.data;

import app.OSRSUtilities;
import app.data.DataManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Used to load a image. If the image is not present it will make an api call to the wiki page and download the image.
 * @author Brett Taylor
 */
public class ImageManager {
    /**
     * The path of the image cache location.
     */
    private final static String IMAGE_CACHE_LOCATION = DataManager.DATA_LOCATION + "Image_Cache/";

    /**
     * @return If the cache folder already exists.
     */
    private static boolean doesCacheFolderExist() {
        File directory = new File(IMAGE_CACHE_LOCATION);
        return directory.exists() && directory.isDirectory();
    }

    /**
     * Creates the directory where the data will be stored..
     */
    private static boolean createCacheFolder() {
        return new File(IMAGE_CACHE_LOCATION).mkdirs();
    }

    /**
     * Creates the full path to the image.
     * @param imageName The image name
     * @return The full image path.
     */
    private static String createImagePath(String imageName) {
        return IMAGE_CACHE_LOCATION + imageName + ".png";
    }

    /**
     * Checks whether the image associated with the name exists or not.
     * @param imageName The name of the image. NOTE: The image name should only be supplied it is automatically converted to a uri.
     * @return True if it exists in the image cache folder.
     */
    public static boolean doesImageAlreadyExist(String imageName) {
        if (!doesCacheFolderExist())
            createCacheFolder();

        File file = new File(createImagePath(imageName));
        return file.exists() && file.isFile();
    }

    /**
     * Downloads the image from the url and saves it.
     * @param imageUrl The url of the image to download.
     * @param imageName What the image will be saved as.
     * @param onDownloaded Method to called when download is completed.
     * @return True if it was successful.
     */
    public static boolean downloadAndSaveImage(String imageUrl, String imageName, Runnable onDownloaded) {
        Image image = new Image(imageUrl);
        if (image.isError())
            return false;

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        File file = new File(createImagePath(imageName));
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (onDownloaded != null)
            onDownloaded.run();
        return true;
    }

    /**
     * Gets an image from the image cache
     * @param imageName The name of the image
     * @return The image or null.
     */
    public static Image getImageOnDisk(String imageName) {
        if (!doesImageAlreadyExist(imageName)) {
            return null;
        }

        File file = new File(createImagePath(imageName));
        return new Image(file.toURI().toString());
    }

    /**
     * Gets the url of the image attached to the article.
     * @param articleURL the url of the article. In form on e.g. /wiki/Dragon_scimitar
     * @return The url of the article image.
     */
    public static String wikiArticleImageUrl(String articleURL) {
        String webSiteURL = OSRSUtilities.WIKI_ADDRESS + articleURL;
        Document doc;
        Elements imageElements = null;

        // Travel down the dom to get to the images in the infobox.
        try {
            doc = Jsoup.connect(webSiteURL).get();
            imageElements = doc.body()
                    .getElementById("WikiaMainContent")
                    .getElementById("WikiaArticle")
                    .getElementById("mw-content-text")
                    .getElementsByClass("infobox-wrapper")
                    .get(0).getElementsByClass("wikitable infobox")
                    .get(0).getElementsByTag("img");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Sometimes the wiki will have multiple images in the info box. We pick which one depending on what we want.
        // Remove the ones that have a weird url.
        // Not sure why the wiki does this sometimes but the image can sometimes have a url that is clearly not a url.
        for (int i = 0; i < imageElements.size(); i++) {
            if (imageElements.get(i).toString().contains("data:image")) {
                imageElements.remove(i);
            }
        }

        // The following code makes assumptions but we will get a picture just maybe not the one we exactly want - but we try our best.

        // If the remaining elements count two. There is a good chance that it is a fixed and broken version so remove the broken version.
        if (imageElements.size() == 2) {
            return imageElements.get(0).absUrl("src");
        }

        // If there are equal or more than 3 items we pick the last one.
        // For example bolts or coins we want to display the biggest stack.
        if (imageElements.size() >= 3) {
            return imageElements.last().absUrl("src");
        }

        return imageElements.get(0).absUrl("src");
    }
}
