package app.ui.pages;

import app.ui.FXMLElement;

/**
 * The base page that all pages will be built of.
 * @author Brett Taylor
 */
public abstract class BasePage extends FXMLElement {
    /**
     * Whether the side bar menu should show on this page.
     */
    private boolean showSideBarMenu;

    /**
     * Constructor
     * @param fxmlAddress The fxml page the base page is based on.
     * @param showSideBarMenu Whether the side bar menu should show on this page.;
     */
    protected BasePage(String fxmlAddress, boolean showSideBarMenu) {
        super(fxmlAddress);
        this.showSideBarMenu = showSideBarMenu;
    }

    /**
     * Called when the page has been shown
     */
    public abstract void onLoaded();

    /**
     * Called when the basepage is about to be removed
     */
    public abstract void onRemoved();

    /**
     * Whether the side bar menu should show on this page.
     * @return True if we want the side bar menu to show on this page.
     */
    public boolean shouldShowSideBarMenu() {
        return showSideBarMenu;
    }
}
