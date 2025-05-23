// FinancialReportGenerator.java
package invest_wise;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Provides functionality for generating PDF financial reports.
 * Creates formatted reports with financial goals and user information.
 */
public class FinancialReportGenerator {

    /**
     * Generates a PDF financial report for a user.
     * Creates a formatted document with financial goals and user details.
     *
     * @param goals The list of financial goals to include in the report
     * @param username The username of the user to generate the report for
     */
    public static void generatePDFReport(ArrayList<FinancialGoals.Goal> goals, String username) {
        if (goals == null || goals.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No goals found to generate a report.");
            return;
        }

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(username + "_Financial_Report.pdf"));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Title
            document.add(new Paragraph("📊 InvestWise Financial Report", titleFont));
            document.add(new Paragraph("Prepared for: " + username, bodyFont));
            document.add(new Paragraph("Date: " + new java.util.Date(), bodyFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("📌 Financial Goals Summary", headingFont));

            // Goals List
            for (FinancialGoals.Goal g : goals) {
                document.add(new Paragraph("- " + g.toString(), bodyFont));
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("💡 This is an automated financial report generated by InvestWise.", bodyFont));

            document.close();

            JOptionPane.showMessageDialog(null, "✅ Financial Report saved as '" + username + "_Financial_Report.pdf'");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "❌ Error generating financial report.");
            ex.printStackTrace();
        }
    }
}
