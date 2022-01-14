import com.formdev.flatlaf.FlatDarkLaf;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class TurkishCoffee {
    static Timer myTimer;
    static TimerTask timerJob;

    public static void createGui() throws IOException {
        FlatDarkLaf.setup();

        Desktop desktop = Desktop.getDesktop();
        String imagePath = "assets/turkish.png";
        BufferedImage myPicture = ImageIO.read(Objects.requireNonNull(TurkishCoffee.class.getResource(imagePath)));
        Icon appIcon = new ImageIcon(myPicture);
        final Taskbar taskbar = Taskbar.getTaskbar();
        try {
            taskbar.setIconImage(myPicture);
        } catch (final UnsupportedOperationException | SecurityException e) {
            e.printStackTrace();
        }
        final JFrame[] frame = new JFrame[1];
        //System.out.println(UIManager.getLookAndFeel());
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> frame[0] = new JFrame());
        JPanel panel = new JPanel();
        JPanel imagePanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JLabel lblImage = new JLabel(new ImageIcon(myPicture));
        lblImage.setHorizontalAlignment(SwingConstants.LEFT);
        frame[0].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame[0].setResizable(false);
        frame[0].setSize(400, 200);
        frame[0].setName("Turkish Coffee");
        frame[0].setTitle("Turkish Coffee");
        frame[0].setIconImage(Toolkit
                .getDefaultToolkit()
                .getImage(String
                        .valueOf(ImageIO.read(Objects
                                .requireNonNull(TurkishCoffee.class.getResource("/assets/turkish.png"))))));
        imagePanel.add(lblImage);
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(150));
        panel.add(stopButton);
        frame[0].getContentPane().add(BorderLayout.LINE_START, imagePanel);
        frame[0].getContentPane().add(BorderLayout.CENTER, panel);
        centerWindow(frame[0]);
        desktop.setAboutHandler(e -> {
            JLabel label = new JLabel();
            Font font = label.getFont();

            String style = "font-family:" +
                    font.getFamily() + ";" + "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" +
                    "font-size:" + font.getSize() + "pt;";

            JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"
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
            JOptionPane
                    .showMessageDialog(null, ep, "About", JOptionPane.INFORMATION_MESSAGE, appIcon);
        });

        stopButton.setEnabled(false);
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            try {
                createTimer();
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        });
        stopButton.addActionListener(e -> {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            stopTimer();
        });
    }

    private static void centerWindow(@NotNull JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
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

    public static String createDate() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static void main(String[] args) throws IOException {
        createGui();
    }

}
