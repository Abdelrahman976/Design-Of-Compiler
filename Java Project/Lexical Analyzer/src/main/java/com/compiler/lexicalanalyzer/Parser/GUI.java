package   com.compiler.lexicalanalyzer.Parser;
import com.compiler.lexicalanalyzer.CompilerController;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GUI {
    private Map<String, Color> palette;
    private File grammarFile;
    private File sourceFile;
    private AnalysisTable analysisTable;
    private JFrame frame;
    private Map<String, JButton> buttons;
    private JPanel bodyPanel;
    private JLabel tableTitle;
    private JLabel tableSummary;
    private JTable table;
    private DefaultTableCellRenderer headerRenderer;
    private DefaultTableCellRenderer rightCellRenderer;

    public GUI() {
        var headingFontName = "Bank Gothic Light BT";
        palette = new HashMap<>();
        palette.put("teal", new Color(0x054148));
        palette.put("strongTeal", new Color(0x012B31));
        palette.put("subtleTeal", new Color(0xD9EBEE));
        palette.put("orange", new Color(0x765206));
        palette.put("red", new Color(0x760E06));
        palette.put("green", new Color(0x055A14));
        palette.put("blue", new Color(0x0D1E4F));
        palette.put("subtleGray", new Color(0xCCCCCC));

        frame = new JFrame();
        frame.setIconImage(new ImageIcon("..\\assets\\logo.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 600));

        var appNameLabel = new JLabel("Top-Down Parser");
        appNameLabel.setHorizontalAlignment(JLabel.CENTER);
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setBackground(palette.get("strongTeal"));
        appNameLabel.setPreferredSize(new Dimension(0, 50));
        appNameLabel.setFont(new Font(headingFontName, Font.PLAIN, 20));

        var actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(1,3, 1, 0));
        actionsPanel.setPreferredSize(new Dimension(0, 80));
        actionsPanel.setBackground(palette.get("strongTeal"));

        buttons = new HashMap<>();
        buttons.put("selectGrammar", new JButton("Select Grammar File..."));
        buttons.get("selectGrammar").addActionListener(e -> handleGrammarSelection());
        buttons.put("selectSource", new JButton("Select Source File..."));
        buttons.get("selectSource").addActionListener(e -> handleSourceSelection());
        buttons.put("checkSyntax", new JButton("Check Syntax"));
        buttons.get("checkSyntax").addActionListener(e -> displayTable());
        buttons.get("checkSyntax").setEnabled(false);

        for (var button : buttons.values()) {
            button.setFocusable(false);
            button.setForeground(palette.get("strongTeal"));
            button.setBackground(palette.get("subtleTeal"));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (button.isEnabled()) {
                        button.setForeground(Color.WHITE);
                        button.setBackground(palette.get("orange"));
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (button.isEnabled()) {
                        button.setForeground(palette.get("strongTeal"));
                        button.setBackground(palette.get("subtleTeal"));
                    }
                }
            });
            buttons.get("checkSyntax").setBackground(palette.get("subtleGray"));
            actionsPanel.add(button);
        }

        var headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 135));
        headerPanel.setBackground(palette.get("strongTeal"));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        headerPanel.add(appNameLabel, BorderLayout.NORTH);
        headerPanel.add(actionsPanel, BorderLayout.CENTER);

        tableTitle = new JLabel();
        tableTitle.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, palette.get("strongTeal")),
                new EmptyBorder(10,10,10,10)));
        tableTitle.setForeground(palette.get("strongTeal"));
        tableTitle.setBackground(Color.WHITE);
        tableTitle.setOpaque(true);
        tableTitle.setHorizontalAlignment(JLabel.CENTER);

        table = new JTable() {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return true;
            }
        };
        table.setGridColor(palette.get("strongTeal"));
        table.setIntercellSpacing(new Dimension(20, 0));
        table.setRowSelectionAllowed(false);
        table.setFocusable(false);
        table.setRowHeight(28);

        var tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(new MatteBorder(0,1,0,1, palette.get("strongTeal")));
        tableScrollPane.setBackground(palette.get("strongTeal"));

        tableSummary = new JLabel();
        tableSummary.setBorder(new EmptyBorder(10,10,10,10));
        tableSummary.setHorizontalAlignment(JLabel.CENTER);
        tableSummary.setForeground(Color.WHITE);
        tableSummary.setOpaque(true);

        bodyPanel = new JPanel();
        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.add(tableTitle, BorderLayout.NORTH);
        bodyPanel.add(tableScrollPane, BorderLayout.CENTER);
        bodyPanel.add(tableSummary, BorderLayout.SOUTH);
        bodyPanel.setBorder(new EmptyBorder(45,45,45,45));
        bodyPanel.setVisible(false);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.getContentPane().setBackground(palette.get("subtleGray"));
        frame.setVisible(true);

        headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setBackground(palette.get("strongTeal"));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        rightCellRenderer = new DefaultTableCellRenderer();
        rightCellRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightCellRenderer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    }

    private void displayTable() {
        analysisTable = new AnalysisTable(grammarFile, sourceFile);

        tableTitle.setText("Analysis Table for Expression: \"" + CompilerController.file.getAbsolutePath() + "\"");

        var tableModel = new DefaultTableModel(tableData(analysisTable), analysisTable.getColumnNames());
        table.setModel(tableModel);

        var tableHeader = table.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(0, 28));
        var columnModel = tableHeader.getColumnModel();
        var columns = new TableColumn[]{
                columnModel.getColumn(0),
                columnModel.getColumn(1),
                columnModel.getColumn(2),
                columnModel.getColumn(3)
        };
        columns[0].setCellRenderer(rightCellRenderer);
        for (var i = 0; i < columns.length; i++)
            columns[i].setHeaderRenderer(headerRenderer);
        columns[0].setPreferredWidth(100);
        columns[1].setPreferredWidth(300);
        columns[2].setPreferredWidth(20);
        columns[3].setPreferredWidth(30);

        tableSummary.setText(new Reporter(analysisTable).fileStatement());
        var feedbackColor = analysisTable.isComplete() ? palette.get("green") : palette.get("red");
        tableSummary.setBackground(feedbackColor);

        bodyPanel.setVisible(true);
    }

    private String[][] tableData(AnalysisTable analysisTable) {
        String[][] data = new String[analysisTable.getTable().size()][4];
        int r = 0;
        for (var row : analysisTable.getTable()) {
            int c = 0;
            for (var cell : row) {
                data[r][c] = cell;
                c++;
            }
            r++;
        }
        return data;
    }

    private void handleGrammarSelection() {
        var fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            grammarFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
            buttons.get("selectGrammar").setText("<html><div text-align:center>Grammar File: \"" +
                    fileChooser.getSelectedFile().getName() +
                    "\"<br><font color=gray>(Click to Reselect)</font></div></html>");
            updateCheckSyntaxButton();
        }
    }

    private void handleSourceSelection() {
        var fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            sourceFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
            buttons.get("selectSource").setText("<html><div text-align:center>Source File: \"" +
                    fileChooser.getSelectedFile().getName() +
                    "\"<br><font color=gray>(Click to Reselect)</font></div></html>");
            updateCheckSyntaxButton();
        }
    }

    private void updateCheckSyntaxButton() {
        if (grammarFile != null && sourceFile != null) {
            buttons.get("checkSyntax").setBackground(palette.get("subtleTeal"));
            buttons.get("checkSyntax").setEnabled(true);
        }
    }
}
