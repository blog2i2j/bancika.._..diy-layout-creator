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
package org.diylc.components.transform;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import org.diylc.common.IComponentTransformer;
import org.diylc.components.AbstractAngledComponent;
import org.diylc.core.Angle;
import org.diylc.core.IDIYComponent;

public class AngledComponentTransformer implements IComponentTransformer {

  @Override
  public boolean canRotate(IDIYComponent<?> component) {
    return AbstractAngledComponent.class.isAssignableFrom(component.getClass());
  }

  @Override
  public boolean canMirror(IDIYComponent<?> component) {
    return false;
  }

  @Override
  public boolean mirroringChangesCircuit() {
    return false;
  }

  @Override
  public void rotate(IDIYComponent<?> component, Point2D center, int direction) {
    AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2 * direction, center.getX(), center.getY());
    for (int index = 0; index < component.getControlPointCount(); index++) {
      Point2D p = new Point2D.Double();
      rotate.transform(component.getControlPoint(index), p);
      component.setControlPoint(p, index);
    }

    AbstractAngledComponent<?> angledComponent = (AbstractAngledComponent<?>) component;
    Angle angle = angledComponent.getAngle();
    Angle newAngle = angle.rotate(direction);
    angledComponent.setAngle(newAngle);
  }

  @Override
  public void mirror(IDIYComponent<?> component, Point2D center, int direction) {    
  }
}
