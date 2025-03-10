/*

    DIY Layout Creator (DIYLC).
    Copyright (c) 2009-2018 held jointly by the individual authors.

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
package org.diylc.components.connectivity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.diylc.common.ObjectCache;
import org.diylc.components.AbstractComponent;
import org.diylc.components.transform.SimpleComponentTransformer;
import org.diylc.core.ComponentState;
import org.diylc.core.IDIYComponent;
import org.diylc.core.IDrawingObserver;
import org.diylc.core.Project;
import org.diylc.core.VisibilityPolicy;
import org.diylc.core.annotations.BomPolicy;
import org.diylc.core.annotations.ComponentDescriptor;
import org.diylc.core.annotations.EditableProperty;
import org.diylc.core.annotations.KeywordPolicy;
import org.diylc.core.measures.Size;
import org.diylc.core.measures.SizeUnit;
import org.diylc.utils.Constants;

@ComponentDescriptor(name = "Turret Lug", category = "Connectivity", author = "Branislav Stojkovic",
    description = "Turret terminal lug", instanceNamePrefix = "Turret",
    zOrder = IDIYComponent.TRACE + 0.1, bomPolicy = BomPolicy.SHOW_ONLY_TYPE_NAME, autoEdit = false,
    keywordPolicy = KeywordPolicy.SHOW_TYPE_NAME, transformer = SimpleComponentTransformer.class,
    enableCache = true)
public class Turret extends AbstractComponent<String> {

  private static final long serialVersionUID = 1L;

  public static Size SIZE = new Size(0.16d, SizeUnit.in);
  public static Size HOLE_SIZE = new Size(0.0625d, SizeUnit.in);
  public static Color COLOR = Color.decode("#E0C04C");

  private Size size = SIZE;
  private Size holeSize = HOLE_SIZE;
  private Color color = COLOR;
  private Point2D.Double point = new Point2D.Double(0, 0);
  private String value = "";

  @Override
  public void draw(Graphics2D g2d, ComponentState componentState, boolean outlineMode, Project project,
      IDrawingObserver drawingObserver) {
    if (checkPointsClipped(g2d.getClip())) {
      return;
    }
    int diameter = getClosestOdd((int) size.convertToPixels());
    int holeDiameter = getClosestOdd((int) holeSize.convertToPixels());
    g2d.setColor(color);
    g2d.setStroke(ObjectCache.getInstance().fetchZoomableStroke(1f));
    drawingObserver.startTrackingContinuityArea(true);
    g2d.fillOval((int)(point.getX() - diameter / 2), (int)(point.getY() - diameter / 2), diameter, diameter);
    drawingObserver.stopTrackingContinuityArea();
    g2d.setColor(componentState == ComponentState.SELECTED || componentState == ComponentState.DRAGGING ? SELECTION_COLOR
        : color.darker());
    g2d.drawOval((int)(point.getX() - diameter / 2), (int)(point.getY() - diameter / 2), diameter, diameter);
    g2d.setColor(Constants.CANVAS_COLOR);
    g2d.fillOval((int)(point.getX() - holeDiameter / 2), (int)(point.getY() - holeDiameter / 2), holeDiameter, holeDiameter);
    g2d.setColor(componentState == ComponentState.SELECTED || componentState == ComponentState.DRAGGING ? SELECTION_COLOR
        : color.darker());
    g2d.drawOval((int)(point.getX() - holeDiameter / 2), (int)(point.getY() - holeDiameter / 2), holeDiameter, holeDiameter);
  }

  @Override
  public void drawIcon(Graphics2D g2d, int width, int height) {
    int diameter = getClosestOdd(width / 2);
    int holeDiameter = 5;
    g2d.setColor(COLOR);
    g2d.fillOval((width - diameter) / 2, (height - diameter) / 2, diameter, diameter);
    g2d.setColor(COLOR.darker());
    g2d.drawOval((width - diameter) / 2, (height - diameter) / 2, diameter, diameter);
    g2d.setColor(Constants.CANVAS_COLOR);
    g2d.fillOval((width - holeDiameter) / 2, (height - holeDiameter) / 2, holeDiameter, holeDiameter);
    g2d.setColor(COLOR.darker());
    g2d.drawOval((width - holeDiameter) / 2, (height - holeDiameter) / 2, holeDiameter, holeDiameter);
  }

  @EditableProperty
  public Size getSize() {
    return size;
  }

  public void setSize(Size size) {
    this.size = size;
  }

  @EditableProperty(name = "Hole Size")
  public Size getHoleSize() {
    return holeSize;
  }

  public void setHoleSize(Size holeSize) {
    this.holeSize = holeSize;
  }

  @Override
  public String getName() {
    return super.getName();
  }

  @Override
  public int getControlPointCount() {
    return 1;
  }

  @Override
  public boolean isControlPointSticky(int index) {
    return true;
  }

  @Override
  public VisibilityPolicy getControlPointVisibilityPolicy(int index) {
    return VisibilityPolicy.NEVER;
  }

  @Override
  public Point2D getControlPoint(int index) {
    return point;
  }

  @Override
  public void setControlPoint(Point2D point, int index) {
    this.point.setLocation(point);
  }

  @EditableProperty(name = "Color")
  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  @EditableProperty
  public String getValue() {
    return value;
  }

  @Override
  public void setValue(String value) {
    this.value = value;
  }
  
  @Override
  public String getControlPointNodeName(int index) {   
    return null;
  }
  
  @Override
  public Rectangle2D getCachingBounds() {
    double size = getSize().convertToPixels();
    return new Rectangle2D.Double(point.getX() - size, point.getY() - size, size * 2, size * 2);
  }
}
