package org.diylc.components.guitar;

import org.diylc.common.PropertyWrapper;
import org.diylc.core.AbstractDynamicPropertyValueSource;
import org.diylc.core.IDIYComponent;
import org.diylc.core.IDynamicPropertySource;
import org.diylc.core.ISwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SchallerMegaSwitchPositionPropertyValueSource extends
    AbstractDynamicPropertyValueSource {

  @Override
  public List<Object> getAvailableValues() {
    if (properties == null) {
      return List.of();
    }
    Optional<PropertyWrapper> optionalPropertyWrapper = this.properties.stream().filter(p -> {
      try {
        return p.getGetter().getName().equals("getType");
      } catch (NoSuchMethodException e) {
        return false;
      }
    }).findFirst();
    if (optionalPropertyWrapper.isEmpty()) {
      return List.of();
    }
    SchallerMegaSwitch.MegaSwitchType
        switchType = (SchallerMegaSwitch.MegaSwitchType) optionalPropertyWrapper.get().getValue();
    int positionCount = switchType.getPositionCount();
    List<Object> res = new ArrayList<>();
    res.add(null);
    for (int i = 0; i < positionCount; i++) {
      res.add(i);
    }
    return res;
  }
}
