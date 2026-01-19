package UniPi.MealLab.App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import UniPi.MealLab.API.IMealService;
import UniPi.MealLab.API.MealApiException;
import UniPi.MealLab.API.MealService;
import UniPi.MealLab.Model.Recipe;

/**
 * Main application window for the Meal Lab project.
 * Handles the graphical user interface, event management, and asynchronous
 * communication with the MealService.
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private IMealService service;
    private DataManager dataManager = new DataManager();
    private DefaultListModel<Recipe> searchModel = new DefaultListModel<>();
    private DefaultListModel<Recipe> favoritesModel = new DefaultListModel<>();
    private DefaultListModel<Recipe> cookedModel = new DefaultListModel<>();
    private java.util.Map<String, ImageIcon> imageCache = new java.util.HashMap<>();

    // UI Design Constants
    private final Color ACCENT = new Color(37, 99, 235);
    private final Color BG_MAIN = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    public MainFrame(IMealService service) {
        this.service = service;
        setTitle("Meal Lab Manager");
        setSize(1280, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadSavedData();
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_MAIN);
        setContentPane(content);

        // Header section with logo/title
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        JLabel title = new JLabel("  Meal Lab");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ACCENT);
        title.setBorder(new EmptyBorder(12, 20, 12, 20));
        header.add(title, BorderLayout.WEST);
        content.add(header, BorderLayout.NORTH);

        // Main navigation tabs: Search, Favorites, and Cooked lists
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(FONT_BOLD);
        tabs.setBorder(new EmptyBorder(10, 10, 0, 10));
        tabs.addTab("  Search  ", createSplitView(searchModel, true));
        tabs.addTab("  Favorites  ", createSplitView(favoritesModel, false));
        tabs.addTab("  Cooked  ", createSplitView(cookedModel, false));
        content.add(tabs, BorderLayout.CENTER);
    }

    /**
     * Creates a split view containing a list on the left and detail panel on the right.
     * @param model The data model for the list
     * @param isSearchTab Whether to include the search input controls
     */
    private JPanel createSplitView(DefaultListModel<Recipe> model, boolean isSearchTab) {
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(0, 0, 0, 15));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        if (isSearchTab) {
            // Search UI controls using GridBagLayout for alignment
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setOpaque(false);
            
            JTextField field = new RoundedTextField(20);
            field.setPreferredSize(new Dimension(400, 45));
            field.setMinimumSize(new Dimension(200, 45));
            
            JComboBox<String> searchType = new JComboBox<>(new String[]{"Name", "Ingredient"});
            searchType.setFont(FONT_PLAIN);
            searchType.setPreferredSize(new Dimension(100, 45));
            
            ModernButton sBtn = new ModernButton("Search", ACCENT, Color.WHITE);
            ModernButton rBtn = new ModernButton("Random", new Color(16, 185, 129), Color.WHITE);
            Dimension btnSize = new Dimension(110, 45);
            sBtn.setPreferredSize(btnSize);
            rBtn.setPreferredSize(btnSize);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 5, 0, 5);
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.weightx = 1.0; 
            gbc.fill = GridBagConstraints.HORIZONTAL;
            centerPanel.add(field, gbc);

            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 1; 
            centerPanel.add(searchType, gbc);
            
            gbc.gridx = 2; 
            centerPanel.add(sBtn, gbc);
            
            gbc.gridx = 3; 
            centerPanel.add(rBtn, gbc);
            
            top.add(centerPanel, BorderLayout.CENTER);
            
            JProgressBar bar = new JProgressBar();
            loadingStyle(bar);
            
            JPanel botPanel = new JPanel(new BorderLayout());
            botPanel.setOpaque(false);
            botPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            botPanel.add(bar, BorderLayout.CENTER);
            top.add(botPanel, BorderLayout.SOUTH);
            
            // Search and Random button listeners
            ActionListener searchAction = e -> {
                String q = field.getText().trim();
                if(!q.isEmpty()) {
                    if (searchType.getSelectedItem().equals("Name")) {
                        runWorker(() -> service.searchRecipes(q), searchModel, sBtn, bar);
                    } else {
                        runWorker(() -> service.getRecipesByIngredient(q), searchModel, sBtn, bar);
                    }
                }
            };
            sBtn.addActionListener(searchAction);
            field.addActionListener(searchAction); 
            
            rBtn.addActionListener(e -> runWorker(() -> { 
                Recipe r = service.getRandomRecipe(); 
                return r != null ? List.of(r) : new ArrayList<>(); 
            }, searchModel, rBtn, bar));
        } else {
            JLabel l = new JLabel("Saved Recipes", SwingConstants.CENTER); 
            l.setFont(new Font("Segoe UI", Font.BOLD, 16)); l.setForeground(TEXT_DARK);
            top.add(l, BorderLayout.CENTER);
        }

        left.add(top, BorderLayout.NORTH);

        JList<Recipe> list = new JList<>(model);
        list.setCellRenderer(new ModernListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(55);
        left.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);
        right.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        JLabel placeholder = new JLabel("Select a recipe to view details", SwingConstants.CENTER);
        placeholder.setForeground(Color.GRAY);
        right.add(placeholder, BorderLayout.CENTER);

        // Update details when a user clicks a recipe in the list
        list.addListSelectionListener(e -> { 
            if (!e.getValueIsAdjusting()) { 
                Recipe r = list.getSelectedValue(); 
                if (r != null) {
                    // Check if we need to fetch full details (e.g., if instructions are missing)
                    if (r.getStrInstructions() == null || r.getStrInstructions().isEmpty()) {
                        new SwingWorker<Recipe, Void>() {
                            @Override
                            protected Recipe doInBackground() throws Exception {
                                return service.getRecipeById(r.getIdMeal());
                            }
                            @Override
                            protected void done() {
                                try {
                                    Recipe full = get();
                                    if (full != null) {
                                        // Update the model object if possible or just use the full one
                                        updateDetailPanel(full, right, model);
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(MainFrame.this, "Error fetching recipe details.");
                                }
                            }
                        }.execute();
                    } else {
                        updateDetailPanel(r, right, model); 
                    }
                }
            } 
        });

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(450); split.setDividerSize(0);
        
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_MAIN); p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.add(split, BorderLayout.CENTER);
        return p;
    }

    /**
     * Renders full recipe details including high-quality image, ingredients, and instructions.
     * Uses HTML rendering for the content and a background worker for the image.
     */
    private void updateDetailPanel(Recipe r, JPanel panel, DefaultListModel<Recipe> activeModel) {
        panel.removeAll(); panel.setLayout(new BorderLayout());
        JLabel imgL = new JLabel("Loading image...", SwingConstants.CENTER);
        imgL.setPreferredSize(new Dimension(0, 350)); imgL.setOpaque(true); imgL.setBackground(new Color(241, 245, 249));
        
        // Asynchronous image loading and scaling to keep UI responsive
        String thumbUrl = r.getStrMealThumb();
        if (thumbUrl != null && imageCache.containsKey(thumbUrl)) {
            imgL.setText("");
            imgL.setIcon(imageCache.get(thumbUrl));
        } else {
            new SwingWorker<ImageIcon, Void>() {
                protected ImageIcon doInBackground() throws Exception {
                    if(thumbUrl == null) return null;
                    BufferedImage o = javax.imageio.ImageIO.read(java.net.URI.create(thumbUrl).toURL());
                    int targetW = 800;
                    int targetH = 350;
                    double ratio = (double)o.getWidth() / o.getHeight();
                    int newW = targetW;
                    int newH = (int)(targetW / ratio);
                    if(newH > targetH) {
                        newH = targetH;
                        newW = (int)(targetH * ratio);
                    }
                    Image scaled = o.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
                protected void done() { 
                    try { 
                        ImageIcon i = get(); 
                        if(i!=null) { 
                            imageCache.put(thumbUrl, i);
                            imgL.setText(""); 
                            imgL.setIcon(i); 
                        } else {
                            imgL.setText("Image unavailable");
                        }
                    } catch(Exception e){ 
                        imgL.setText("Image unavailable"); 
                    } 
                }
            }.execute();
        }
        panel.add(imgL, BorderLayout.NORTH);

        JEditorPane text = new JEditorPane(); text.setEditable(false); text.setContentType("text/html"); text.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        String category = r.getStrCategory() != null ? r.getStrCategory() : "Unknown Category";
        String area = r.getStrArea() != null ? r.getStrArea() : "Unknown Area";
        String instructions = r.getStrInstructions() != null ? r.getStrInstructions() : "No instructions available.";

        // Generate HTML content for the recipe details
        String h = "<html><body style='font-family: Segoe UI; color: #334155; margin:0; padding:0;'>" +
                   "<h2 style='color: #1e293b; margin-bottom:2px; margin-top:0; font-size: 18px;'>" + r.getStrMeal() + "</h2>" +
                   "<p style='color: #64748b; font-size:11px; margin-top:0; margin-bottom: 8px;'>" + category + " &bull; " + area + "</p>" +
                   "<div style='background-color: #f8fafc; padding: 6px; border-radius: 6px; margin: 5px 0;'>" +
                   "<h4 style='color:#2563eb; margin-top:0; margin-bottom:4px; font-size:13px;'>Ingredients</h4>" +
                   "<table width='100%' style='font-size:12px; color:#475569; border-collapse: collapse;'>";
        java.util.List<String> ings = r.getIngredientsList();
        for(int i=0; i<ings.size(); i++) {
            if(i % 2 == 0) h += "<tr>";
            h += "<td width='50%' style='padding: 1px 0;'>" + "&bull; " + ings.get(i) + "</td>";
            if(i % 2 != 0) h += "</tr>";
        }
        if(ings.size() % 2 != 0) h += "</tr>";
        h += "</table></div><h4 style='color:#2563eb; margin-top: 10px; margin-bottom:4px; font-size:13px;'>Instructions</h4>" +
             "<div style='font-size: 13px; line-height: 1.3; color:#1e293b; text-align: justify;'>" + 
             instructions.replace("\n", "<br>").replace(". ", ".<br>") + "</div></body></html>";
        
        text.setText(h); text.setCaretPosition(0);
        panel.add(new JScrollPane(text), BorderLayout.CENTER);

        // Action buttons: Favorite, Cooked, and Delete
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15)); btns.setBackground(Color.WHITE);
        btns.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        JButton fav = new ModernButton("Favorite", new Color(236, 72, 153), Color.WHITE);
        JButton cook = new ModernButton("Cooked", new Color(16, 185, 129), Color.WHITE);
        JButton del = new ModernButton("Delete", Color.WHITE, Color.GRAY);
        
        fav.addActionListener(e -> { if(addUnique(favoritesModel, r)) JOptionPane.showMessageDialog(this, "Saved!"); });
        cook.addActionListener(e -> { if(addUnique(cookedModel, r)) JOptionPane.showMessageDialog(this, "Marked!"); });
        del.addActionListener(e -> { 
            boolean removed = false;
            if (activeModel == favoritesModel) {
                if(favoritesModel.contains(r)) { favoritesModel.removeElement(r); removed = true; }
            } else if (activeModel == cookedModel) {
                if(cookedModel.contains(r)) { cookedModel.removeElement(r); removed = true; }
            } else {
                if(favoritesModel.contains(r)) { favoritesModel.removeElement(r); removed = true; }
                if(cookedModel.contains(r)) { cookedModel.removeElement(r); removed = true; }
            }
            
            if(removed) { 
                saveCurrentData(); 
                panel.removeAll(); 
                panel.add(new JLabel("Removed.", SwingConstants.CENTER)); 
                panel.revalidate(); 
                panel.repaint(); 
            }
            else JOptionPane.showMessageDialog(this, "Recipe is not in this list.");
        });
        
        btns.add(fav); btns.add(cook); btns.add(del);
        panel.add(btns, BorderLayout.SOUTH); panel.revalidate(); panel.repaint();
    }

    private boolean addUnique(DefaultListModel<Recipe> m, Recipe r) { 
        if(m.contains(r)) return false; 
        m.addElement(r); 
        saveCurrentData(); 
        return true; 
    }
    
    /**
     * Executes a network-related task on a separate thread using SwingWorker.
     * Prevents the UI from freezing during API calls.
     */
    private void runWorker(TaskSupplier t, DefaultListModel<Recipe> m, JButton b, JProgressBar bar) {
        new SwingWorker<List<Recipe>, Void>() {
            protected void done() { 
                bar.setVisible(false); b.setEnabled(true); 
                try { 
                    List<Recipe> l = get(); 
                    m.clear(); 
                    if(l!=null && !l.isEmpty()) {
                        for(Recipe r:l) m.addElement(r); 
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "No recipes found matching your criteria.");
                    }
                } catch(InterruptedException | ExecutionException e) { 
                    String msg = "Search failed.";
                    Throwable cause = e.getCause();
                    if (cause instanceof MealApiException) {
                        msg = cause.getMessage();
                    }
                    JOptionPane.showMessageDialog(MainFrame.this, msg); 
                } 
            }
            protected List<Recipe> doInBackground() throws Exception { SwingUtilities.invokeLater(() -> { bar.setVisible(true); b.setEnabled(false); }); return t.get(); }
        }.execute();
    }
    interface TaskSupplier { List<Recipe> get() throws Exception; }
    private void loadingStyle(JProgressBar b) { b.setIndeterminate(true); b.setVisible(false); b.setPreferredSize(new Dimension(100, 3)); b.setForeground(ACCENT); b.setBorderPainted(false); }
    private void loadSavedData() { DataManager.UserData d = dataManager.load(); if(d.favorites!=null) for(Recipe r:d.favorites) favoritesModel.addElement(r); if(d.cooked!=null) for(Recipe r:d.cooked) cookedModel.addElement(r); }
    private void saveCurrentData() { List<Recipe> f = new ArrayList<>(); for(int i=0; i<favoritesModel.size(); i++) f.add(favoritesModel.get(i)); List<Recipe> c = new ArrayList<>(); for(int i=0; i<cookedModel.size(); i++) c.add(cookedModel.get(i)); dataManager.save(f, c); }

    class ModernButton extends JButton {
        private Color bg;
        public ModernButton(String text, Color bg, Color fg) {
            super(text); this.bg = bg;
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setForeground(fg); setFont(FONT_BOLD); setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 20, 8, 20));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g); g2.dispose();
        }
    }

    class RoundedTextField extends JTextField {
        private static final long serialVersionUID = 1L;
        public RoundedTextField(int cols) { super(cols); setOpaque(false); setBorder(new EmptyBorder(8, 15, 8, 15)); setFont(FONT_PLAIN); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            g2.setColor(new Color(203, 213, 225));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            super.paintComponent(g); g2.dispose();
        }
    }

    class ModernListRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Recipe r = (Recipe) value;
            String cat = r.getStrCategory();
            if (cat == null || cat.isEmpty()) cat = "Click to view details";
            l.setText("<html><div style='padding:10px 10px 10px 5px;'><b>" + r.getStrMeal() + "</b><br><span style='color:#64748b; font-size:10px;'>" + cat + "</span></div></html>");
            l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)));
            l.setBackground(isSelected ? new Color(219, 234, 254) : Color.WHITE);
            l.setForeground(isSelected ? ACCENT : TEXT_DARK);
            return l;
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e){}
        SwingUtilities.invokeLater(() -> new MainFrame(new MealService()).setVisible(true));
    }
}

