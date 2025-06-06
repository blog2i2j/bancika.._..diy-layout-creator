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
 */
package org.diylc.components.connectivity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Path2D;

import org.diylc.common.ObjectCache;
import org.diylc.components.AbstractCurvedComponent;
import org.diylc.components.transform.SimpleComponentTransformer;
import org.diylc.core.ComponentState;
import org.diylc.core.IContinuity;
import org.diylc.core.IDIYComponent;
import org.diylc.core.IDrawingObserver;
import org.diylc.core.annotations.BomPolicy;
import org.diylc.core.annotations.ComponentDescriptor;
import org.diylc.core.annotations.EditableProperty;
import org.diylc.utils.Constants;

@ComponentDescriptor(name = "Hookup Wire", author = "Branislav Stojkovic", category = "Connectivity",
    instanceNamePrefix = "W", description = "Flexible wire with two control points", zOrder = IDIYComponent.WIRING,
    flexibleZOrder = true, bomPolicy = BomPolicy.NEVER_SHOW,
    transformer = SimpleComponentTransformer.class, enableCache = false)
public class HookupWire extends AbstractCurvedComponent<AWG> implements IContinuity {

  private static final long serialVersionUID = 1L;

  public static Color COLOR = Color.green;
  public static Color STRIPE_COLOR = Color.yellow;
  public static double INSULATION_THICKNESS_PCT = 0.3;

  protected AWG gauge = AWG._22;
  
  protected boolean striped = false;
  protected Color stripeColor = STRIPE_COLOR;

  @Override
  protected Color getDefaultColor() {
    return COLOR;
  }

  @Override
  protected void drawCurve(Path2D curve, Graphics2D g2d, ComponentState componentState, IDrawingObserver drawingObserver) {
    int thickness = (int) (gauge.diameterIn() * Constants.PIXELS_PER_INCH * (1 + 2 * INSULATION_THICKNESS_PCT)) - 1;
    Color curveColor =
        componentState == ComponentState.SELECTED || componentState == ComponentState.DRAGGING ? SELECTION_COLOR
            : color;
    g2d.setColor(curveColor);
    Stroke stroke = null;
    Stroke tracingStroke = null;
    switch (getStyle()) {
      case SOLID:
        stroke = ObjectCache.getInstance().fetchBasicStroke(thickness);
        tracingStroke = stroke;
        break;
      case DASHED:
        stroke =
            ObjectCache.getInstance().fetchStroke(thickness, new float[] {thickness * 2, thickness * 3}, thickness * 4, BasicStroke.CAP_SQUARE);
        tracingStroke = ObjectCache.getInstance().fetchBasicStroke(thickness);
        break;
      case DOTTED:
        stroke = ObjectCache.getInstance().fetchStroke(thickness, new float[] {thickness / 4, thickness * 3}, 0, BasicStroke.CAP_ROUND);
        tracingStroke = ObjectCache.getInstance().fetchBasicStroke(thickness);
        break;
    }
    Shape s = stroke.createStrokedShape(curve);    
        
    if (stroke == tracingStroke) {
      g2d.fill(s);
      drawingObserver.stopTracking();
    } else {
      // draw without tracking
      drawingObserver.stopTracking();
      g2d.fill(s);
      // track with a simplified stroke without the dashes and dots to improve performance;
      Shape traceShape = tracingStroke.createStrokedShape(curve);
      drawingObserver.startTracking();
      Composite oldComposite = g2d.getComposite();
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST, 0f));
      g2d.fill(traceShape);
      g2d.setComposite(oldComposite);
      drawingObserver.stopTracking();
    }
    
    if (getStriped()) {
      stroke = ObjectCache.getInstance().fetchStroke(thickness, new float[] { thickness / 2, thickness * 2 }, thickness * 10, BasicStroke.CAP_BUTT);
      Shape stripe = stroke.createStrokedShape(curve);
      g2d.setColor(getStripeColor());      
      g2d.fill(stripe);      
    }    
    
    if (componentState == ComponentState.NORMAL) {
      g2d.setColor(color.darker());
      g2d.setStroke(ObjectCache.getInstance().fetchBasicStroke(1f));
      g2d.draw(s);
    }
  }

  @EditableProperty(name = "AWG")
  public AWG getValue() {
    return gauge;
  }

  public void setValue(AWG gauge) {
    this.gauge = gauge;
  }

  @EditableProperty(name = "Stripe")
  public boolean getStriped() {
    return striped;
  }

  public void setStriped(boolean striped) {
    this.striped = striped;
  }

  @EditableProperty(name = "Stripe Color")
  public Color getStripeColor() {
    if (stripeColor == null)
      stripeColor = STRIPE_COLOR;
    return stripeColor;
  }

  public void setStripeColor(Color stripeColor) {
    this.stripeColor = stripeColor;
  }

  @Override
  public boolean arePointsConnected(int index1, int index2) {
    return Math.abs(index1 - index2) == getControlPointCount() - 1;
  }
}
