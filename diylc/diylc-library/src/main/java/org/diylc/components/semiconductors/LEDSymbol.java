/*

    DIY Layout Creator (DIYLC).
    Copyright (c) 2009-2025 held jointly by the individual authors.

    This file is part of DIYLC.

    DIYLC is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    DIYLC is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with DIYLC.  If not, see <http://www.gnu.org/licenses/>.

*/
package org.diylc.components.semiconductors;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import org.diylc.common.ObjectCache;
import org.diylc.components.transform.SimpleComponentTransformer;
import org.diylc.core.CreationMethod;
import org.diylc.core.IDIYComponent;
import org.diylc.core.annotations.ComponentDescriptor;
import org.diylc.core.annotations.KeywordPolicy;

@ComponentDescriptor(name = "LED", author = "Branislav Stojkovic", category = "Schematic Symbols",
    creationMethod = CreationMethod.POINT_BY_POINT, instanceNamePrefix = "D", description = "Diode schematic symbol",
    zOrder = IDIYComponent.COMPONENT, keywordPolicy = KeywordPolicy.SHOW_TAG, keywordTag = "Schematic",
    transformer = SimpleComponentTransformer.class)
public class LEDSymbol extends DiodeSymbol {

  private static final long serialVersionUID = 1L;

  @Override
  public void drawIcon(Graphics2D g2d, int width, int height) {
    AffineTransform tx = g2d.getTransform();
    super.drawIcon(g2d, width, height);
    int arrowSize = 3 * width / 32;
    g2d.setStroke(ObjectCache.getInstance().fetchBasicStroke(1));
    // Go back to original transform
    g2d.setTransform(tx);
    // g2d.drawLine((width - size) / 2 + size / 8, (height + size) / 2,
    // (width - size) / 2 + size
    // / 8 + arrowSize, (height + size) / 2 + arrowSize);
    g2d.drawLine(width * 9 / 16, height * 10 / 16, width * 9 / 16 + arrowSize, height * 10 / 16);
    g2d.drawLine(width * 9 / 16, height * 11 / 16, width * 9 / 16 + arrowSize, height * 11 / 16);
  }

  @Override
  protected void decorateComponentBody(Graphics2D g2d, boolean outlineMode) {
    super.decorateComponentBody(g2d, outlineMode);

    // Draw arrows
    g2d.setStroke(ObjectCache.getInstance().fetchBasicStroke(1));
    double width = getWidth().convertToPixels();
    double arrowLength = width / 3;
    double arrowSize = width / 6;
    int d = (int) (width / 3);

    int x2 = (int) (d / 2 + Math.cos(Math.PI / 4) * arrowLength);
    int y2 = (int) (width + Math.sin(Math.PI / 4) * arrowLength);
    g2d.drawLine(d / 2, (int) width, x2 - 2, y2 - 2);
    g2d.fillPolygon(new Polygon(new int[] {x2, x2, (int) (x2 - arrowSize)}, new int[] {y2, (int) (y2 - arrowSize), y2},
        3));

    x2 = (int) (3 * d / 2 + Math.cos(Math.PI / 4) * arrowLength);
    y2 = (int) (width + Math.sin(Math.PI / 4) * arrowLength);
    g2d.drawLine(3 * d / 2, (int) width, x2 - 2, y2 - 2);
    g2d.fillPolygon(new Polygon(new int[] {x2, x2, (int) (x2 - arrowSize)}, new int[] {y2, (int) (y2 - arrowSize), y2},
        3));
  }
}
