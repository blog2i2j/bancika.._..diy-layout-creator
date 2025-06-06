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
package org.diylc.swing.gui.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.diylc.common.PropertyWrapper;
import org.diylc.core.Angle;
import org.diylc.lang.LangUtil;
import org.diylc.utils.Constants;

public class AngleEditor extends JComboBox<Integer> {

  private static final long serialVersionUID = 1L;

  private Color oldBg = getBackground();

  private final PropertyWrapper property;

  public AngleEditor(final PropertyWrapper property) {
    this.property = property;
    if (property.isReadOnly())
      setEnabled(false);

    setEditable(true);

    setModel(new DefaultComboBoxModel<Integer>(new Integer[] {30, 45, 90, 135, 180, 270}));
    setRenderer(new DefaultListCellRenderer() {

      private static final long serialVersionUID = 1L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        // TODO Auto-generated method stub
        return super.getListCellRendererComponent(list,
            value == null ? null : LangUtil.translate(value.toString()), index, isSelected,
            cellHasFocus);
      }

    });
    setSelectedItem(((Angle) (property.getValue())).getValue());
    addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          property.setChanged(true);
          setBackground(oldBg);
          AngleEditor.this.property.setValue(Angle.of((Integer) e.getItem()));
        }
      }
    });
    if (!property.isUnique()) {
      setBackground(Constants.MULTI_VALUE_COLOR);
    }
  }
}
