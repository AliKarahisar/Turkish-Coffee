import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class TurkishCoffee {
    public static String imagePath = "assets/turkish.png";
    public static Boolean timerStatus = false;
    public static Desktop desktop = Desktop.getDesktop();
    static Timer myTimer;
    static TimerTask timerJob;

    public static PopupMenu popupMenu() {
        PopupMenu trayPopupMenu = new PopupMenu();
        MenuItem timerStart = new MenuItem("Start");
        timerStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!timerStatus) {
                    try {
                        createTimer();
                        timerStart.setLabel("Stop");
                        timerStatus = true;
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    timerStatus = false;
                    timerStart.setLabel("Start");
                    stopTimer();
                }
            }
        });
        trayPopupMenu.add(timerStart);

        MenuItem seperator = new MenuItem("-");
        trayPopupMenu.add(seperator);

        MenuItem about = new MenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAbout();
            }
        });
        trayPopupMenu.add(about);

        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
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
                + "<strong>Turkish Coffee v0.2</strong><br/>"
                + "Coded by, <a href=\"https://alikarahisar.com/\">Ali Karahisar</a>"
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
        JOptionPane.showMessageDialog(null, ep, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void createGui() throws IOException {

        BufferedImage myPicture = ImageIO.read(Objects.requireNonNull(TurkishCoffee.class.getResource(imagePath)));
        Icon appIcon = new ImageIcon(imagePath);
        PopupMenu popupMenu = popupMenu();
        desktop.setAboutHandler(e ->
                createAbout()
        );

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(
                    TurkishCoffee.class.getResource(
                            imagePath)),
                    "Turkish Coffee", popupMenu
            );
            icon.setImageAutoSize(true);

            try {
                tray.add(icon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
        final Taskbar taskbar = Taskbar.getTaskbar();
        try {
            taskbar.setIconImage(myPicture);
            taskbar.setMenu(popupMenu);
        } catch (final UnsupportedOperationException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private static void createTimer() throws AWTException {
        myTimer = new Timer();
        timerJob = new TimerTask() {
            final Robot robot = new Robot();

            @Override
            public void run() {
                robot.keyPress(KeyEvent.VK_F24);
            }
        };
        myTimer.schedule(timerJob, 0, 10000);
    }

    private static void stopTimer() {
        myTimer.cancel();
        timerJob.cancel();
    }

    public static void main(String[] args) throws IOException {
        createGui();
    }

}
