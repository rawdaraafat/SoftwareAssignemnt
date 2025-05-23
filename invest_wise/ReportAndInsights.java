// ReportAndInsightsPanel.java
package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Provides functionality for generating and displaying financial reports and insights.
 * Shows financial goals, connected stock accounts, and allows report export.
 */
public class ReportAndInsights extends styles {
    /** Text area for displaying the financial report */
    private JTextArea reportArea;
    /** Currently logged in user's username */
    private String currentUser;

    /**
     * Constructs the report and insights window.
     * Initializes the UI components and loads financial data.
     *
     * @param previousFrame The frame to return to when closing this window
     */
    public ReportAndInsights(JFrame previousFrame) {
        currentUser = login_signup.getCurrentUser();
        if (currentUser == null || currentUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must be logged in to view reports.");
            previousFrame.setVisible(true);
            dispose();
            return;
        }

        window();
        setTitle("Report & Insights");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.decode("#f5efe7"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // === Title Label ===
        JLabel titleLabel = new JLabel("📊 Report & Insights", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#3e5879"));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // === Report Area ===
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Financial Summary"));

        // === Buttons ===
        JButton exportBtn = styledButton("Export Report");
        JButton backBtn = styledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(exportBtn);
        buttonPanel.add(backBtn);

        // Actions
        exportBtn.addActionListener(this::generateReport);
        backBtn.addActionListener(e -> {
            previousFrame.setVisible(true);
            dispose();
        });

        // Load and display report
        loadAndDisplayReport();

        // Layout
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Loads and displays the financial report.
     * Includes financial goals and connected stock accounts.
     */
    private void loadAndDisplayReport() {
        StringBuilder sb = new StringBuilder();

        // Load Goals
        ArrayList<FinancialGoals.Goal> goals = FinancialGoals.loadGoals(null);
        sb.append("📌 FINANCIAL GOALS\n");
        sb.append("=======================\n");
        if (goals.isEmpty()) {
            sb.append("No goals found.\n\n");
        } else {
            for (FinancialGoals.Goal g : goals) {
                sb.append(g.toString()).append("\n\n");
            }
        }

        // Load Stock Accounts
        sb.append("🏦 CONNECTED STOCK ACCOUNTS\n");
        sb.append("===========================\n");
        try (BufferedReader reader = new BufferedReader(new FileReader("invest_wise/stock_accounts.txt"))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(currentUser)) {
                    sb.append("Platform: ").append(parts[1])
                            .append(", Email: ").append(parts[2]).append("\n\n");
                    found = true;
                }
            }
            if (!found) {
                sb.append("No stock accounts connected.\n\n");
            }
        } catch (Exception ex) {
            sb.append("⚠️ Error loading stock account data.\n\n");
        }

        reportArea.setText(sb.toString());
    }

    /**
     * Generates and exports the financial report.
     * Prompts user to save as PDF if confirmed.
     *
     * @param e The action event that triggered the report generation
     */
    private void generateReport(ActionEvent e) {
        ArrayList<FinancialGoals.Goal> goals = FinancialGoals.loadGoals(currentUser);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Do you want to export your report as PDF?",
                "Export Report",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            FinancialReportGenerator.generatePDFReport(goals, currentUser);
        }
    }

    /**
     * Creates a styled button with consistent formatting.
     *
     * @param text The text to display on the button
     * @return A configured JButton with standard styling
     */
}
