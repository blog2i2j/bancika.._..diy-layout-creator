/*
 * 
 * DIY Layout Creator (DIYLC). Copyright (c) 2009-2018 held jointly by the individual authors.
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
package org.diylc.components.transform;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.diylc.common.IComponentTransformer;
import org.diylc.common.Orientation;
import org.diylc.components.smd.PassiveSMDComponent;
import org.diylc.core.IDIYComponent;

public class PassiveSMDTransformer implements IComponentTransformer {

  @Override
  public boolean canRotate(IDIYComponent<?> component) {
    return PassiveSMDComponent.class.isAssignableFrom(component.getClass());
  }

  @Override
  public boolean canMirror(IDIYComponent<?> component) {
    return true;
  }

  @Override
  public boolean mirroringChangesCircuit() {
    return false;
  }

  @Override
  public void rotate(IDIYComponent<?> component, Point2D center, int direction) {
    AffineTransform rotate =
        AffineTransform.getRotateInstance(Math.PI / 2 * direction, center.getX(), center.getY());
    for (int index = 0; index < component.getControlPointCount(); index++) {
      Point2D p = new Point2D.Double();
      rotate.transform(component.getControlPoint(index), p);
      component.setControlPoint(p, index);
    }

    PassiveSMDComponent<?> smd = (PassiveSMDComponent<?>) component;
    Orientation o = smd.getOrientation();
    int oValue = o.ordinal();
    oValue += direction;
    if (oValue < 0)
      oValue = Orientation.values().length - 1;
    if (oValue >= Orientation.values().length)
      oValue = 0;
    o = Orientation.values()[oValue];
    smd.setOrientation(o);
  }

  @Override
  public void mirror(IDIYComponent<?> component, Point2D center, int direction) {
    PassiveSMDComponent<?> smd = (PassiveSMDComponent<?>) component;
    for (int i = 0; i < smd.getControlPointCount(); i++) {
      Point2D point = smd.getControlPoint(i);      
      double dx = 0;
      double dy = 0;
      if (direction == IComponentTransformer.HORIZONTAL)
        dx = (center.getX() - point.getX()) * 2;
      else
        dy = (center.getY() - point.getY()) * 2;
      Point2D newPoint = new Point2D.Double(point.getX() + dx, point.getY() + dy);
      smd.setControlPoint(newPoint, i);
    }
  }
}
