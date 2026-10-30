import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

abstract class libraryItem {
    protected String category;
    protected int id;
    protected String title;
    protected String author;
    protected boolean isBorrowed;

    public libraryItem(String category, int id, String title, String author, Boolean isBorrowed) {
        this.category = category;
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setIsBorrowed(boolean isBorrowed) { this.isBorrowed = isBorrowed; }

    public void borrowBook() {
        if (isBorrowed) {
            System.out.println(title + " is already borrowed");
        } else {
            isBorrowed = true;
            System.out.println("You borrowed: " + title);
        }
    }

    public void returnBook() {
        if (!isBorrowed) {
            System.out.println(title + " is already in the library");
        } else {
            isBorrowed = false;
            System.out.println("You returned: " + title);
        }
    }

    public abstract void getDetails();
}

class Book extends libraryItem {
    public boolean isBorrowed() {
        return isBorrowed;
    }

    public Book() {
        super("None", 0, "Untitled", "Unknown", false);
    }

    public Book(String category, int id, String title, String author, Boolean isBorrowed) {
        super(category, id, title, author, isBorrowed);
    }

    @Override
    public void getDetails() {
        String status = isBorrowed ? "Borrowed" : "Available";
        System.out.println(id + " - " + title + " by " + author + " [" + category + "] status = " + status);
    }

    public static ArrayList<Book> loadBook(String filename) {
        ArrayList<Book> libro = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String category = parts[0].trim();
                    int id = Integer.parseInt(parts[1].trim());
                    String title = parts[2].trim();
                    String author = parts[3].trim();
                    boolean isBorrowed = Boolean.parseBoolean(parts[4].trim());
                    libro.add(new Book(category, id, title, author, isBorrowed));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading book.txt");
        }
        return libro;
    }
}

// Custom Animated Panel featuring a live Pulsing Neon Glow & Scanning HUD Line
class SystemWindowPanel extends JPanel {
    private float glowPhase = 0.0f;
    private Timer animationTimer;

    public SystemWindowPanel() {
        // Animation loop running at ~30 FPS for smooth pulsing glow effects
        animationTimer = new Timer(35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                glowPhase += 0.08f;
                if (glowPhase > Math.PI * 2) {
                    glowPhase = 0;
                }
                repaint();
            }
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Deep Obsidian Hunter Background
        g2d.setColor(new Color(5, 8, 14));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Dynamic Pulsing Neon Cyan Glow (Calculated via Sine Wave)
        int alpha = (int)(130 + 125 * Math.sin(glowPhase));
        g2d.setColor(new Color(0, 240, 255, alpha));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(6, 6, getWidth() - 12, getHeight() - 12);
        
        // Inner Tech Border
        g2d.setColor(new Color(0, 160, 220, 100));
        g2d.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
        
        // Animated Sci-Fi Scanline moving up and down
        g2d.setColor(new Color(0, 240, 255, 20));
        int scanY = (int)((Math.sin(glowPhase * 1.5) + 1) * 0.5 * (getHeight() - 20)) + 10;
        g2d.fillRect(12, scanY, getWidth() - 24, 3);

        // Solo Leveling Corner Brackets
        g2d.setColor(new Color(0, 240, 255));
        int cSize = 16;
        // Top-Left
        g2d.fillRect(3, 3, cSize, 4); g2d.fillRect(3, 3, 4, cSize);
        // Top-Right
        g2d.fillRect(getWidth() - 3 - cSize, 3, cSize, 4); g2d.fillRect(getWidth() - 7, 3, 4, cSize);
        // Bottom-Left
        g2d.fillRect(3, getHeight() - 7, cSize, 4); g2d.fillRect(3, getHeight() - 3 - cSize, 4, cSize);
        // Bottom-Right
        g2d.fillRect(getWidth() - 3 - cSize, getHeight() - 7, cSize, 4); g2d.fillRect(getWidth() - 7, getHeight() - 3 - cSize, 4, cSize);
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<Book> libro = Book.loadBook("C:\\Users\\User\\Desktop\\Calmada_Comprog_Term2_Activity\\Book.txt");
        
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // Removes standard OS border for true floating game-popup feel
        frame.setSize(720, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        SystemWindowPanel mainPanel = new SystemWindowPanel();
        mainPanel.setLayout(null);
        frame.add(mainPanel);

        // System Prompt Notification Label
        JLabel label = new JLabel("[Please specify a command for the Library System.]");
        label.setBounds(25, 20, 670, 30);
        label.setFont(new Font("Consolas", Font.BOLD, 13));
        label.setForeground(new Color(0, 240, 255));
        mainPanel.add(label);

        // System Inventory Display Box
        JTextArea catalogArea = new JTextArea(" [!] Database on standby. Click 'VIEW CATALOG' to extract inventory.");
        catalogArea.setBounds(25, 60, 670, 140);
        catalogArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        catalogArea.setBackground(new Color(11, 18, 32));
        catalogArea.setForeground(new Color(224, 242, 254));
        catalogArea.setEditable(false);
        catalogArea.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 180)));
        mainPanel.add(catalogArea);

        // Target Input Field (ID or Title)
        JTextField textField = new JTextField();
        textField.setBounds(25, 220, 360, 42);
        textField.setFont(new Font("Consolas", Font.PLAIN, 14));
        textField.setBackground(new Color(11, 18, 32));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(new Color(0, 240, 255));
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 240, 255)));
        mainPanel.add(textField);

        // System Action Buttons with Glow Effects
        JButton borrowbtn = new JButton("BORROW");
        borrowbtn.setBounds(395, 220, 145, 42);
        styleSystemButton(borrowbtn);
        mainPanel.add(borrowbtn);

        JButton returnbtn = new JButton("RETURN");
        returnbtn.setBounds(550, 220, 145, 42);
        styleSystemButton(returnbtn);
        mainPanel.add(returnbtn);

        JButton catalogbtn = new JButton("VIEW CATALOG");
        catalogbtn.setBounds(25, 280, 330, 45);
        styleSystemButton(catalogbtn);
        mainPanel.add(catalogbtn);

        JButton quitbtn = new JButton("CLOSE SYSTEM");
        quitbtn.setBounds(365, 280, 330, 45);
        styleSystemButton(quitbtn);
        mainPanel.add(quitbtn);

        // ==========================================
        // ACTION LISTENERS (Fully Validated Logic)
        // ==========================================

        borrowbtn.addActionListener(e -> {
            new Thread(() -> {
                label.setText("[SYSTEM]: Executing borrow command...");
                try { Thread.sleep(400); } catch (InterruptedException ex) {}
                String input = textField.getText().trim();
                boolean found = false;
                for (Book book : libro) {
                    if (String.valueOf(book.getId()).equals(input) || book.getTitle().equalsIgnoreCase(input)) {
                        found = true;
                        if (!book.isBorrowed()) {
                            book.borrowBook();
                            label.setText("[SUCCESS]: Acquired item -> " + book.getTitle());
                        } else {
                            label.setText("[WARNING]: Item is already borrowed!");
                        }
                        break;
                    }
                }
                if (!found) { label.setText("[ERROR]: Target item not found in database."); }
            }).start();
        });

        returnbtn.addActionListener(e -> {
            new Thread(() -> {
                label.setText("[SYSTEM]: Executing return command...");
                try { Thread.sleep(400); } catch (InterruptedException ex) {}
                String input = textField.getText().trim();
                boolean found = false;
                for (Book book : libro) {
                    if (String.valueOf(book.getId()).equals(input) || book.getTitle().equalsIgnoreCase(input)) {
                        found = true;
                        if (book.isBorrowed()) {
                            book.returnBook();
                            label.setText("[SUCCESS]: Stored item -> " + book.getTitle());
                        } else {
                            label.setText("[WARNING]: Item is not currently borrowed!");
                        }
                        break;
                    }
                }
                if (!found) { label.setText("[ERROR]: Target item not found in database."); }
            }).start();
        });

        catalogbtn.addActionListener(e -> {
            new Thread(() -> {
                StringBuilder info = new StringBuilder("=== [ SHADOW DATABASE INVENTORY ] ===\n");
                for (Book book : libro) {
                    info.append(" ID: ").append(book.getId())
                        .append(" | ").append(book.getTitle())
                        .append(" [").append(book.isBorrowed() ? "BORROWED" : "AVAILABLE")
                        .append("]\n");
                }
                catalogArea.setText(info.toString());
                label.setText("[SYSTEM]: Inventory extracted successfully.");
            }).start();
        });

        quitbtn.addActionListener(e -> {
            label.setText("[SYSTEM]: Disconnecting interface...");
            try { Thread.sleep(400); } catch (InterruptedException ex) {}
            System.exit(0);
        });

        frame.setVisible(true);
    }

    private static void styleSystemButton(JButton btn) {
        btn.setBackground(new Color(11, 22, 40));
        btn.setForeground(new Color(0, 240, 255));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Consolas", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 240, 255), 1));
        
        // Interactive Hover Glow
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 240, 255));
                btn.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(11, 22, 40));
                btn.setForeground(new Color(0, 240, 255));
            }
        });
    }
}