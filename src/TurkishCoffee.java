import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.Timer;

import static java.awt.event.MouseEvent.BUTTON3;

public class TurkishCoffee {
    static ResourceBundle bundle = ResourceBundle.getBundle("i18n/AppUI");
    final static Taskbar taskbar = Taskbar.getTaskbar();
    private static final String DEFAULT_TITLE = "Turkish Coffee";
    private static final String VERSION = "0.5.0";
    private static final String VERSION_STRING = DEFAULT_TITLE + " v" + VERSION;
    private static final String URL = "https://alikarahisar.com";
    private static final String URL_STRING = "Ali Karahisar";
    public static String imagePath = "assets/turkish-coffee-passive.png";
    public static String imagePathActive = "assets/turkish-coffee-active.png";
    public static Boolean timerStatus = false;
    public static Desktop desktop = Desktop.getDesktop();
    public static PopupMenu trayPopupMenu = new PopupMenu();
    public static MenuItem timerStart = new MenuItem(bundle.getString("start"));
    static Timer myTimer;
    static TimerTask timerJob;
    static SystemTray tray = SystemTray.getSystemTray();
    static TrayIcon icon;

    public static void eventListenerForPopup() throws AWTException, IOException {
        if (!timerStatus) {
            createTimer();
        } else {
            stopTimer();
        }
    }

    public static PopupMenu popupMenu() {
        timerStart.addActionListener(e -> {
            try {
                eventListenerForPopup();
            } catch (AWTException | IOException ex) {
                ex.printStackTrace();
            }
        });
        trayPopupMenu.add(timerStart);

        MenuItem seperator = new MenuItem("-");
        trayPopupMenu.add(seperator);

        MenuItem about = new MenuItem(bundle.getString("about"));
        about.addActionListener(e -> createAbout());
        trayPopupMenu.add(about);

        MenuItem close = new MenuItem(bundle.getString("exit"));
        close.addActionListener(e -> System.exit(0));
        trayPopupMenu.add(close);
        return trayPopupMenu;
    }

    private static void createAbout() {
        JLabel label = new JLabel();
        Font font = label.getFont();

        String style = "font-family:" +
                font.getFamily() + ";" + "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" +
                "font-size:" + font.getSize() + "pt;";

        JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"
                + "<strong>" + VERSION_STRING + "</strong><br/>"
                + "Coded by, <a href=" + URL + ">" + URL_STRING + "</a>"
                + "<h6>icon by <a href=\"https://www.iconfinder.com/\">iconfinder</a></h6>"
                + "</body></html>");
        ep.addHyperlinkListener(e1 -> {
            if (e1.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    desktop.browse(URI.create(e1.getURL().toString()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        ep.setEditable(false);
        ep.setBackground(label.getBackground());
        JOptionPane.showMessageDialog(null, ep, bundle.getString("about"), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void createGui() throws IOException {

        BufferedImage myPicture = ImageIO.read(Objects.requireNonNull(TurkishCoffee.class.getResource(imagePath)));
        PopupMenu popupMenu = popupMenu();
        desktop.setAboutHandler(e ->
                createAbout()
        );

        if (SystemTray.isSupported()) {
            icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(
                    TurkishCoffee.class.getResource(imagePath)), DEFAULT_TITLE + " - "+bundle.getString("active"), popupMenu);
            icon.setImageAutoSize(true);

            try {
                tray.add(icon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
        try {
            taskbar.setIconImage(myPicture);
            taskbar.setMenu(popupMenu);
            icon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == BUTTON3) {
                        try {
                            eventListenerForPopup();
                        } catch (AWTException | IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        } catch (final UnsupportedOperationException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private static void createTimer() throws AWTException, IOException {
        myTimer = new Timer();
        isIconChanged(true);
        timerStatus = true;
        timerStart.setLabel(bundle.getString("stop"));
        timerJob = new TimerTask() {
            final Robot robot = new Robot();

            @Override
            public void run() {
                robot.keyPress(KeyEvent.VK_F24);
            }
        };
        myTimer.schedule(timerJob, 0, 10000);
    }

    private static void stopTimer() throws IOException {
        myTimer.cancel();
        timerJob.cancel();
        isIconChanged(false);
        timerStatus = false;
        timerStart.setLabel(bundle.getString("start"));
    }

    public static void isIconChanged(boolean iconChange) throws IOException {
        if (iconChange) {
            taskbar.setIconImage(ImageIO
                    .read(Objects.requireNonNull(TurkishCoffee.class.getResource(imagePathActive))));
            tray.getTrayIcons()[0].setImage(Toolkit.getDefaultToolkit()
                    .createImage(TurkishCoffee.class.getResource(imagePathActive)));
            icon.setToolTip(DEFAULT_TITLE + " - " + bundle.getString("active"));
        } else {
            taskbar.setIconImage(ImageIO
                    .read(Objects.requireNonNull(TurkishCoffee.class.getResource(imagePath))));
            tray.getTrayIcons()[0].setImage(Toolkit.getDefaultToolkit()
                    .createImage(TurkishCoffee.class.getResource(imagePath)));
            icon.setToolTip(DEFAULT_TITLE + " - " + bundle.getString("passive"));
        }
    }

    public static void main(String[] args) throws IOException {
        createGui();
    }
}