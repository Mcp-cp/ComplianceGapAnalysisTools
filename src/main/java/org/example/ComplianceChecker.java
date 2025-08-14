package org.example;

import java.util.HashMap;
import java.util.Map;

public class ComplianceChecker {

    public static String checkCompliance(Map<String, String> answers) {
        // Expected compliant answers
        Map<String, String> expected = new HashMap<>();
        expected.put("q1", "Yes");
        expected.put("q2", "Yes");
        expected.put("q3", "Yes");
        expected.put("q4", "Yes");
        expected.put("q5", "Yes");
        expected.put("q6", "Yes");
        expected.put("q7", "Yes");
        expected.put("q8", "Yes");
        expected.put("q9", "Yes");
        expected.put("q10", "Yes");

        // Count compliance
        int total = expected.size();
        int compliant = 0;
        StringBuilder gaps = new StringBuilder();

        for (String key : expected.keySet()) {
            String userAnswer = answers.get(key);
            if (userAnswer != null && userAnswer.equalsIgnoreCase(expected.get(key))) {
                compliant++;
            } else {
                gaps.append("<li>").append(getRequirementText(key)).append("</li>");
            }
        }

        int compliancePercent = (compliant * 100) / total;

        // Build HTML report
        StringBuilder report = new StringBuilder();
        report.append("<html><head><title>Compliance Report</title></head><body>");
        report.append("<h1>Compliance Report</h1>");
        report.append("<p><strong>Compliance Score: </strong>").append(compliancePercent).append("%</p>");

        if (gaps.length() > 0) {
            report.append("<h3>Gaps Found:</h3><ul>").append(gaps).append("</ul>");
        } else {
            report.append("<p>All controls are compliant! ✅</p>");
        }

        report.append("<br><a href='/'>Go Back</a>");
        report.append("</body></html>");

        return report.toString();
    }

    private static String getRequirementText(String questionKey) {
        switch (questionKey) {
            case "q1": return "Passwords must be ≥ 12 characters.";
            case "q2": return "Multi-factor authentication must be enabled.";
            case "q3": return "Backups must be done daily.";
            case "q4": return "Security awareness training must be given yearly.";
            case "q5": return "All devices must have antivirus installed.";
            case "q6": return "Firewalls must be enabled.";
            case "q7": return "Incident response plan must exist.";
            case "q8": return "Access rights must be reviewed quarterly.";
            case "q9": return "Data must be encrypted at rest.";
            case "q10": return "Vendor security must be assessed yearly.";
            default: return "Unknown requirement.";
        }
    }
}
