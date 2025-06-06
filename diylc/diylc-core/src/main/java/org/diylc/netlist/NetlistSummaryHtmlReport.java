/*
 *
 * DIY Layout Creator (DIYLC). Copyright (c) 2009-2025 held jointly by the individual authors.
 *
 * This file is part of DIYLC.
 *
 * DIYLC is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * DIYLC is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DIYLC. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package org.diylc.netlist;

import java.util.List;

public class NetlistSummaryHtmlReport {

    public static String generateHtml(List<Summary> summaries, String fontName) {
        if (summaries == null || summaries.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("<html><head><style>");
        sb.append("body { font-family: ").append(fontName)
            .append(", sans-serif; color: black; margin: 10px; }");
        sb.append("h2 { font-size: 12px; margin-top: 10px; margin-bottom: 0px; }");
        sb.append("p { margin: 0; }");
        sb.append("hr { margin-top: 10px; margin-bottom: 0; }");
        sb.append("</style></head><body>");

        for (Summary summary : summaries) {
            if (summaries.size() > 1) {
                sb.append("<h2>Switch configuration: ").append(summary.getNetlist().getSwitchSetup())
                    .append("</h2>");
            }

            sb.append(summary.getSummaryHtml());

            if (summaries.size() > 1) {
                sb.append("<br><hr>");
            }
        }
        sb.append("</body></html>");

        return sb.toString();
    }
}
