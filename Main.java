import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;

class TalhaFrame extends JFrame {}
class TalhaPanel extends JPanel {
    public TalhaPanel() { super(); }
    public TalhaPanel(LayoutManager layout) { super(layout); }
}
class TalhaButton extends JButton {
    public TalhaButton(String text) { super(text); }
}
class TalhaLabel extends JLabel {
    public TalhaLabel(String text, int horizontalAlignment) { super(text, horizontalAlignment); }
}
class TalhaTextField extends JTextField {}
class TalhaPasswordField extends JPasswordField {}

public class Main extends TalhaFrame {

    private boolean isSignIn = true;
    public boolean isTurkish = false;
    public boolean isDarkMode = false;
    private float animProgress = 0.0f;
    private Timer timer;

    private final Color colorPrimary = new Color(60, 175, 209);
    private final Color colorDark = new Color(68, 151, 191);
    private final Color colorNavyLight = new Color(30, 88, 140);
    private final Color colorNavyDark = new Color(20, 50, 90);
    
    public Color currentBg = Color.WHITE;
    public Color currentText = new Color(27, 31, 36);

    private TalhaPanel activeBar;
    private TalhaPanel heroInner;
    private TalhaPanel formsInner;
    
    private TalhaButton btnSignIn, btnSignUp;
    private TalhaLabel lblHeroTitle1, lblHeroSub1, lblHeroTitle2, lblHeroSub2;
    private TalhaLabel lblFormTitle1, lblFormTitle2;
    private TalhaTextField txtEmail1, txtEmail2;
    private TalhaPasswordField txtPass1, txtPass2;
    private TalhaButton btnAction1, btnAction2;
    private TalhaPanel bgPanel;
    
    private ModernToastPanel toastPanel;

    public Main() {
        setUndecorated(true);
        setSize(700, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        bgPanel = new TalhaPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(currentBg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        TalhaButton closeBtn = new TalhaButton("X");
        closeBtn.setBounds(650, 10, 40, 30);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setForeground(Color.GRAY);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        bgPanel.add(closeBtn);

        TalhaPanel navPanel = new TalhaPanel(null);
        navPanel.setBounds(0, 0, 100, 460);
        navPanel.setOpaque(false);
        bgPanel.add(navPanel);

        activeBar = new TalhaPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isDarkMode ? colorNavyLight : colorPrimary);
                g2.fillRoundRect(0, 0, 6, 60, 8, 8);
            }
        };
        activeBar.setBounds(0, (460/3) + (460/3 - 60)/2, 6, 60);
        activeBar.setOpaque(false);
        navPanel.add(activeBar);

        btnSignIn = createNavButton("Sign In", 460/3, e -> toggleView(true));
        btnSignUp = createNavButton("Sign Up", (460/3)*2, e -> toggleView(false));
        navPanel.add(btnSignIn);
        navPanel.add(btnSignUp);

        TalhaPanel heroPanel = new TalhaPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int bgOffset = (int)(animProgress * -30);
                Color topColor = isDarkMode ? colorNavyLight : colorPrimary;
                Color bottomColor = isDarkMode ? colorNavyDark : colorDark;
                
                GradientPaint gp = new GradientPaint(0, bgOffset, topColor, 0, bgOffset+460, bottomColor);
                g2.setPaint(gp);
                g2.fillRect(0, 0, 300, 460);
                g2.dispose();
            }
        };
        heroPanel.setBounds(100, 0, 300, 460);
        heroPanel.setOpaque(false);
        bgPanel.add(heroPanel);

        // Şık, modern siyah toast bildirim paneli (Dil seçme barının hemen altı - Y: 48)
        toastPanel = new ModernToastPanel();
        heroPanel.add(toastPanel);
        heroPanel.setComponentZOrder(toastPanel, 0);

        LangToggle langToggle = new LangToggle();
        langToggle.setBounds(105, 10, 90, 26);
        heroPanel.add(langToggle);
        heroPanel.setComponentZOrder(langToggle, 1);

        ThemeToggle themeToggle = new ThemeToggle();
        themeToggle.setBounds(100, 390, 100, 60); 
        heroPanel.add(themeToggle);

        heroInner = new TalhaPanel(null);
        heroInner.setBounds(0, 0, 300, 920);
        heroInner.setOpaque(false);
        heroPanel.add(heroInner);

        lblHeroTitle1 = createLabel("Welcome back", 22, Color.WHITE, 0, 210, 300, 30);
        lblHeroSub1 = createLabel("Please enter your credentials", 13, new Color(235, 235, 235), 0, 240, 300, 20);
        heroInner.add(lblHeroTitle1);
        heroInner.add(lblHeroSub1);

        lblHeroTitle2 = createLabel("Join us today", 22, Color.WHITE, 0, 670, 300, 30);
        lblHeroSub2 = createLabel("Creating an account is quick", 13, new Color(235, 235, 235), 0, 700, 300, 20);
        heroInner.add(lblHeroTitle2);
        heroInner.add(lblHeroSub2);

        TalhaPanel formPanel = new TalhaPanel(null);
        formPanel.setBounds(400, 0, 300, 460);
        formPanel.setOpaque(false);
        bgPanel.add(formPanel);

        formsInner = new TalhaPanel(null);
        formsInner.setBounds(0, 0, 300, 920);
        formsInner.setOpaque(false);
        formPanel.add(formsInner);

        lblFormTitle1 = createLabel("Sign In", 24, currentText, 0, 45, 300, 40);
        formsInner.add(lblFormTitle1);
        txtEmail1 = createTextField();
        txtPass1 = createPasswordField();
        txtEmail1.setBounds(50, 110, 200, 45);
        txtPass1.setBounds(50, 170, 200, 45);
        formsInner.add(txtEmail1);
        formsInner.add(txtPass1);
        
        btnAction1 = createActionButton(50, 245);
        btnAction1.addActionListener(e -> {
            if(txtEmail1.getText().trim().isEmpty() || new String(txtPass1.getPassword()).trim().isEmpty()) {
                showToast(true, "Lütfen tüm alanları doldurun!", "Please fill in all fields!");
            } else {
                showToast(false, "Başarıyla giriş yapıldı!", "Login successful!");
            }
        });
        formsInner.add(btnAction1);

        lblFormTitle2 = createLabel("Sign Up", 24, currentText, 0, 505, 300, 40);
        formsInner.add(lblFormTitle2);
        txtEmail2 = createTextField();
        txtPass2 = createPasswordField();
        txtEmail2.setBounds(50, 570, 200, 45);
        txtPass2.setBounds(50, 630, 200, 45);
        formsInner.add(txtEmail2);
        formsInner.add(txtPass2);
        
        btnAction2 = createActionButton(50, 705);
        btnAction2.addActionListener(e -> {
            if(txtEmail2.getText().trim().isEmpty() || new String(txtPass2.getPassword()).trim().isEmpty()) {
                showToast(true, "Lütfen tüm alanları doldurun!", "Please fill in all fields!");
            } else {
                showToast(false, "Kayıt işlemi başarılı!", "Registration successful!");
            }
        });
        formsInner.add(btnAction2);

        timer = new Timer(15, e -> {
            float target = isSignIn ? 0.0f : 1.0f;
            animProgress += (target - animProgress) * 0.15f; 
            
            if (Math.abs(target - animProgress) < 0.005f) {
                animProgress = target;
                timer.stop();
            }

            int navH = 460/3;
            activeBar.setLocation(0, navH + (int)(animProgress * navH) + (navH - 60)/2);
            heroInner.setLocation(0, (int)(animProgress * -460));
            formsInner.setLocation(0, (int)(animProgress * -460));
            
            updateNavColors();
            bgPanel.repaint();
        });
        
        updateTextsAndStyles();
    }

    private void toggleView(boolean signIn) {
        if (this.isSignIn != signIn) {
            this.isSignIn = signIn;
            timer.start();
        }
    }

    public void showToast(boolean error, String msgTr, String msgEn) {
        if (toastPanel != null) {
            toastPanel.triggerToast(error, msgTr, msgEn);
        }
    }

    public void updateTextsAndStyles() {
        if (isTurkish) {
            btnSignIn.setText("Giriş Yap");
            btnSignUp.setText("Kayıt Ol");
            lblHeroTitle1.setText("Tekrar Hoşgeldiniz");
            lblHeroSub1.setText("Lütfen bilgilerinizi giriniz");
            lblHeroTitle2.setText("Bugün Bize Katılın");
            lblHeroSub2.setText("Hesap oluşturmak çok hızlı");
            lblFormTitle1.setText("Giriş Yap");
            lblFormTitle2.setText("Kayıt Ol");
            btnAction1.setText("Giriş Yap");
            btnAction2.setText("Kayıt Ol");
        } else {
            btnSignIn.setText("Sign In");
            btnSignUp.setText("Sign Up");
            lblHeroTitle1.setText("Welcome back");
            lblHeroSub1.setText("Please enter your credentials");
            lblHeroTitle2.setText("Join us today");
            lblHeroSub2.setText("Creating an account is quick");
            lblFormTitle1.setText("Sign In");
            lblFormTitle2.setText("Sign Up");
            btnAction1.setText("Sign In");
            btnAction2.setText("Sign Up");
        }

        lblFormTitle1.setForeground(currentText);
        lblFormTitle2.setForeground(currentText);

        String emailStr = isTurkish ? "E-posta" : "Email";
        String passStr = isTurkish ? "Şifre" : "Password";
        
        applyFieldStyle(txtEmail1, emailStr);
        applyFieldStyle(txtEmail2, emailStr);
        applyFieldStyle(txtPass1, passStr);
        applyFieldStyle(txtPass2, passStr);

        Color btnBg = isDarkMode ? colorNavyLight : colorPrimary;
        btnAction1.setBackground(btnBg);
        btnAction2.setBackground(btnBg);

        updateNavColors();
        if (toastPanel != null) toastPanel.updateText();
        bgPanel.repaint();
    }

    private void applyFieldStyle(JTextField field, String title) {
        field.setBackground(isDarkMode ? new Color(22, 26, 31) : Color.WHITE);
        field.setForeground(currentText);
        field.setCaretColor(currentText);
        Color borderColor = isDarkMode ? new Color(80, 85, 95) : Color.GRAY;
        field.setBorder(BorderFactory.createTitledBorder(
            new EmptyBorder(0,0,0,0), title, 0, 0, 
            new Font("SansSerif", Font.PLAIN, 11), borderColor));
    }

    private void updateNavColors() {
        Color highlight = isDarkMode ? colorNavyLight : colorDark;
        if (animProgress < 0.5f) {
            btnSignIn.setForeground(highlight);
            btnSignUp.setForeground(isDarkMode ? Color.GRAY : currentText);
        } else {
            btnSignIn.setForeground(isDarkMode ? Color.GRAY : currentText);
            btnSignUp.setForeground(highlight);
        }
    }

    private TalhaButton createNavButton(String text, int y, ActionListener action) {
        TalhaButton btn = new TalhaButton(text);
        btn.setBounds(0, y, 100, 460/3);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private TalhaLabel createLabel(String text, int size, Color color, int x, int y, int w, int h) {
        TalhaLabel lbl = new TalhaLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", size > 20 ? Font.BOLD : Font.PLAIN, size));
        lbl.setForeground(color);
        lbl.setBounds(x, y, w, h);
        return lbl;
    }

    private TalhaTextField createTextField() {
        TalhaTextField txt = new TalhaTextField();
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return txt;
    }

    private TalhaPasswordField createPasswordField() {
        TalhaPasswordField txt = new TalhaPasswordField();
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return txt;
    }

    private TalhaButton createActionButton(int x, int y) {
        TalhaButton btn = new TalhaButton("");
        btn.setBounds(x, y, 200, 40);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true); 
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Modern, siyah, yumuşak köşeli, animasyonlu simgeli Toast Bildirim Paneli
    class ModernToastPanel extends JPanel {
        private String msgTr = "", msgEn = "", currentText = "";
        private boolean isError = false;
        private float animProgress = 0f; // 0.0 (kapalı) ile 1.0 (tam açık) arası animasyon değeri
        private Timer animTimer;
        private int state = 0; // 0: Gizli, 1: Yukarıdan iniyor, 2: Bekliyor, 3: Yukarı çıkıp kapanıyor
        private int waitTicks = 0;

        public ModernToastPanel() {
            setOpaque(false);
            setBounds(15, 48, 270, 45); // Dil seçme çubuğunun hemen altı
        }

        public void triggerToast(boolean err, String tr, String en) {
            this.isError = err;
            this.msgTr = tr;
            this.msgEn = en;
            updateText();

            if (animTimer != null) animTimer.stop();
            state = 1;
            waitTicks = 0;
            
            animTimer = new Timer(12, e -> {
                if (state == 1) {
                    animProgress += 0.12f; // İçeri süzülme hızı
                    if (animProgress >= 1f) {
                        animProgress = 1f;
                        state = 2;
                    }
                } else if (state == 2) {
                    waitTicks++;
                    if (waitTicks > 110) state = 3; // Ekranda kalma süresi
                } else if (state == 3) {
                    animProgress -= 0.08f; // Yukarı çıkıp kaybolma hızı
                    if (animProgress <= 0f) {
                        animProgress = 0f;
                        state = 0;
                        animTimer.stop();
                    }
                }
                repaint();
            });
            animTimer.start();
        }

        public void updateText() {
            this.currentText = Main.this.isTurkish ? msgTr : msgEn;
            if (state > 0) repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (animProgress <= 0.01f || currentText.isEmpty()) return;
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Yumuşak açılış için yukarıdan aşağıya kayma (Slide-down) piksel hesaplaması
            int targetY = 0;
            int currentY = (int)(-35 * (1 - animProgress));
            g2.translate(0, currentY);

            // Opaklık (Fade in/out) efekti
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, animProgress * 1.5f)));

            // Modern Koyu/Siyah Şık Arka Plan Kutusu
            g2.setColor(new Color(25, 30, 38, 245));
            g2.fillRoundRect(0, 0, getWidth(), 38, 14, 14);

            // Sol tarafta minik uyarı/başarı simge alanı (Tik veya X)
            int iconX = 14;
            int iconY = 19;
            if (isError) {
                // Animasyonlu Kırmızı Çarpı (X) Çizimi
                g2.setColor(new Color(255, 90, 90));
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(iconX - 4, iconY - 4, iconX + 4, iconY + 4);
                g2.drawLine(iconX + 4, iconY - 4, iconX - 4, iconY + 4);
            } else {
                // Animasyonlu Yeşil Onay Tik İşareti Çizimi
                g2.setColor(new Color(60, 220, 120));
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(iconX - 5, iconY, iconX - 1, iconY + 4);
                g2.drawLine(iconX - 1, iconY + 4, iconX + 5, iconY - 3);
            }

            // Metin Yazımı
            g2.setColor(new Color(240, 245, 250));
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            int strX = 35; // Simgeden sonraki boşluk
            int strY = (38 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(currentText, strX, strY);

            g2.dispose();
        }
    }

    class LangToggle extends JPanel {
        private float progress = 0f;
        private boolean isTR = false;
        private Timer toggleTimer;

        public LangToggle() {
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isTR = !isTR;
                    if(toggleTimer != null) toggleTimer.stop();
                    toggleTimer = new Timer(12, ev -> {
                        progress += isTR ? 0.1f : -0.1f;
                        if(progress <= 0f) { progress = 0f; toggleTimer.stop(); }
                        if(progress >= 1f) { progress = 1f; toggleTimer.stop(); }
                        repaint();
                    });
                    toggleTimer.start();
                    Main.this.isTurkish = isTR;
                    Main.this.updateTextsAndStyles();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

            int thumbW = 44;
            int maxShift = getWidth() - thumbW - 4;
            int x = (int)(2 + progress * maxShift);

            g2.setColor(new Color(255, 255, 255, 230));
            g2.fillRoundRect(x, 2, thumbW, getHeight()-4, getHeight()-4, getHeight()-4);

            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            
            g2.setColor(progress < 0.5f ? new Color(30, 34, 40) : Color.WHITE);
            g2.drawString("EN", 16, 17);
            
            g2.setColor(progress > 0.5f ? new Color(30, 34, 40) : Color.WHITE);
            g2.drawString("TR", 58, 17);

            g2.dispose();
        }
    }

    class ThemeToggle extends JPanel {
        private float progress = 0f;
        private boolean isNight = false;
        private Timer toggleTimer;

        public ThemeToggle() {
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isNight = !isNight;
                    if(toggleTimer != null) toggleTimer.stop();
                    toggleTimer = new Timer(15, ev -> {
                        progress += isNight ? 0.1f : -0.1f;
                        if(progress <= 0f) { progress = 0f; toggleTimer.stop(); }
                        if(progress >= 1f) { progress = 1f; toggleTimer.stop(); }
                        repaint();
                    });
                    toggleTimer.start();
                    Main.this.isDarkMode = isNight;
                    Main.this.currentBg = isNight ? new Color(30, 34, 40) : Color.WHITE;
                    Main.this.currentText = isNight ? new Color(230, 237, 243) : new Color(27, 31, 36);
                    Main.this.updateTextsAndStyles();
                }
            });
        }

        private Color blend(Color c1, Color c2, float ratio) {
            float r = (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
            float g = (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
            float b = (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
            return new Color((int)r, (int)g, (int)b);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = 32; 
            
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gImg = img.createGraphics();
            gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color dayBg = new Color(110, 190, 240);
            Color nightBg = new Color(25, 35, 60);
            gImg.setColor(blend(dayBg, nightBg, progress));
            gImg.fillRoundRect(0, 0, w, h, h, h);

            gImg.setColor(new Color(255, 255, 255, (int)((1 - progress) * 200)));
            gImg.fillOval(30, 10, 22, 10);
            gImg.fillOval(42, 5, 22, 16);

            gImg.setColor(new Color(255, 255, 255, (int)(progress * 200)));
            gImg.fillOval(35, 8, 2, 2);
            gImg.fillOval(55, 16, 3, 3);
            gImg.fillOval(75, 10, 2, 2);

            int thumbSize = 24;
            int maxShift = w - thumbSize - 8;
            int x = (int)(4 + progress * maxShift);
            
            Color sunColor = new Color(255, 220, 50);
            Color moonColor = new Color(230, 235, 240);
            gImg.setColor(blend(sunColor, moonColor, progress));
            gImg.fillOval(x, 4, thumbSize, thumbSize);

            if (progress > 0) {
                gImg.setColor(new Color(180, 190, 200, (int)(progress * 120)));
                gImg.fillOval(x + 6, 10, 4, 4);
                gImg.fillOval(x + 14, 15, 5, 5);
            }

            gImg.dispose();
            g2.drawImage(img, 0, 0, null);

            BufferedImage refImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gRef = refImg.createGraphics();
            gRef.translate(0, h);
            gRef.scale(1, -1);
            gRef.drawImage(img, 0, 0, null);
            gRef.setComposite(AlphaComposite.DstIn);
            gRef.setPaint(new GradientPaint(0, 0, new Color(0,0,0,0), 0, h, new Color(0,0,0,180)));
            gRef.fillRect(0, 0, w, h);
            gRef.dispose();

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g2.drawImage(refImg, 0, h, null);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
