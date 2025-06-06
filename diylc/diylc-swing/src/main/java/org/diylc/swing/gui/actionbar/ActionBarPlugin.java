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
package org.diylc.swing.gui.actionbar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.diylc.appframework.miscutils.Utils;

import org.diylc.common.*;
import org.diylc.lang.LangUtil;
import org.diylc.swing.ActionFactory;
import org.diylc.swing.ISwingUI;
import org.diylc.utils.IconLoader;
import org.diylc.swing.plugins.help.HelpMenuPlugin;

/**
 * Mini toolbar with common actions
 * 
 * @author Branislav Stojkovic
 */
public class ActionBarPlugin implements IPlugIn {
  
  private static final Logger LOG = Logger.getLogger(ActionBarPlugin.class);

  private static final SupportOption[] SUPPORT_OPTIONS = new SupportOption[] {
    new SupportOption(
      "Enjoying DIYLC? Click here to buy me a coffee :)",
      "Donate",
      IconLoader.Donate,
      HelpMenuPlugin.DONATE_URL
    ),
    new SupportOption(
      "Become a Patreon and enjoy perks",
      "Join Patreon",
      IconLoader.Patreon,
      HelpMenuPlugin.PATREON_URL
    )
  };

  private IPlugInPort plugInPort;
  private ISwingUI swingUI;

  private JPanel actionPanel;
  private ActionToolbar contextActionToolbar;
  private ConfigToolbar configToolbar;
  private JLabel donateLabel;
  private SupportOption selectedOption;
  private Consumer<Boolean> highlightConnectedAreasUpdateAction;

  public ActionBarPlugin(ISwingUI swingUI) {
    this.swingUI = swingUI;
    this.selectedOption = SUPPORT_OPTIONS[(int)(Math.random() * SUPPORT_OPTIONS.length)];
  }

  public JPanel getActionPanel() {
    if (actionPanel == null) {
      actionPanel = new JPanel();
      actionPanel.setOpaque(false);
      actionPanel.setLayout(new GridBagLayout());
      
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1;
      
      actionPanel.add(getDonateLabel(), gbc);
      
      gbc.gridx = 1;
      gbc.weightx = 0;      
      actionPanel.add(getConfigToolbar(), gbc);
      
      gbc.gridx = 2;
      // add some space to separate the two toolbars
      actionPanel.add(new JLabel() {
        
        private static final long serialVersionUID = 1L;

        @Override
        public Dimension getPreferredSize() {
          return new Dimension(16, 16);
        }
      }, gbc);
      
      gbc.gridx = 3;
      actionPanel.add(getContextActionToolbar(), gbc);
      
      actionPanel.setBorder(BorderFactory.createEmptyBorder());          
    }
    return actionPanel;
  }
  
  public JLabel getDonateLabel() {
    if (donateLabel == null) {
      donateLabel = new JLabel(selectedOption.longText);
      donateLabel.setForeground(Color.blue);
      donateLabel.setIcon(selectedOption.icon.getIcon());
      donateLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      this.swingUI.getOwnerFrame().addComponentListener(new ComponentAdapter() {
        
        @Override
        public void componentShown(ComponentEvent e) {
          JRootPane rootPane = SwingUtilities.getRootPane(donateLabel);
          rootPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
              if (rootPane.getWidth() < 940) {
                if (!selectedOption.shortText.equals(donateLabel.getText())) {
                  donateLabel.setText(selectedOption.shortText);
                  donateLabel.setIcon(selectedOption.icon.getIcon());
                }
              } else {
                if (!selectedOption.longText.equals(donateLabel.getText())) {
                  donateLabel.setText(selectedOption.longText);
                  donateLabel.setIcon(selectedOption.icon.getIcon());
                }
              }
            }
          });
        }        
      });
      donateLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          try {
            Utils.openURL(selectedOption.url);
          } catch (Exception e1) {
            swingUI.showMessage("Web browser launching failed. Please visit " + 
                selectedOption.url + 
                " to support DIYLC. Thank you!", "Error", ISwingUI.INFORMATION_MESSAGE);
          }
        }
      });
    }
    return donateLabel;
  }
  
  public ConfigToolbar getConfigToolbar() {
    if (configToolbar == null) {
      configToolbar = new ConfigToolbar();

      List<ToggleItem> toggleItems = new ArrayList<ToggleItem>();
      toggleItems.add(new ToggleItem(IPlugInPort.SNAP_TO_NONE, IPlugInPort.SNAP_TO_NONE, IconLoader.SnapToNone.getIcon()));
      toggleItems.add(new ToggleItem(IPlugInPort.SNAP_TO_GRID, IPlugInPort.SNAP_TO_GRID, IconLoader.SnapToGrid.getIcon()));
      toggleItems.add(new ToggleItem(IPlugInPort.SNAP_TO_COMPONENTS, IPlugInPort.SNAP_TO_COMPONENTS, IconLoader.SnapToComponents.getIcon()));
      configToolbar.addToggleLabel(LangUtil.translate("Snap To"), IPlugInPort.SNAP_TO_KEY, IPlugInPort.SNAP_TO_DEFAULT, toggleItems);
      
      configToolbar.add(new JLabel(" "));
      
      configToolbar.add(LangUtil.translate("Continuous Creation"), IPlugInPort.CONTINUOUS_CREATION_KEY, IconLoader.Elements.getIcon(), false);
      highlightConnectedAreasUpdateAction =
          configToolbar.add(LangUtil.translate("Highlight Connected Areas") + " (Alt)",
              IconLoader.LaserPointer.getIcon(),
              plugInPort.getOperationMode() == OperationMode.HIGHLIGHT_CONNECTED_AREAS,
              () -> plugInPort.setOperationMode(
                  plugInPort.getOperationMode() == OperationMode.EDIT ?
                      OperationMode.HIGHLIGHT_CONNECTED_AREAS :
                      OperationMode.EDIT));
      configToolbar.add(LangUtil.translate("Sticky Points") + " (Ctrl)", IPlugInPort.STICKY_POINTS_KEY, IconLoader.GraphNodes.getIcon(), true);
    }
    return configToolbar;
  }
  
  public ActionToolbar getContextActionToolbar() {
    if (contextActionToolbar == null) {
      contextActionToolbar = new ActionToolbar();
      contextActionToolbar.add(ActionFactory.getInstance().createRotateSelectionAction(plugInPort, 1));
      contextActionToolbar.add(ActionFactory.getInstance().createRotateSelectionAction(plugInPort, -1));
      contextActionToolbar.addSpacer();
      contextActionToolbar.add(ActionFactory.getInstance().createMirrorSelectionAction(plugInPort, IComponentTransformer.HORIZONTAL));
      contextActionToolbar.add(ActionFactory.getInstance().createMirrorSelectionAction(plugInPort, IComponentTransformer.VERTICAL));
      contextActionToolbar.addSpacer();
      contextActionToolbar.add(ActionFactory.getInstance().createNudgeAction(plugInPort));
      contextActionToolbar.addSpacer();
      contextActionToolbar.add(ActionFactory.getInstance().createSendToBackAction(plugInPort));
      contextActionToolbar.add(ActionFactory.getInstance().createBringToFrontAction(plugInPort));
      contextActionToolbar.addSpacer();
      contextActionToolbar.add(ActionFactory.getInstance().createGroupAction(plugInPort));
      contextActionToolbar.add(ActionFactory.getInstance().createUngroupAction(plugInPort));
    }
    return contextActionToolbar;
  }

  @Override
  public void connect(IPlugInPort plugInPort) {
    this.plugInPort = plugInPort;
    if (Utils.isMac()) {
      try {
        swingUI.injectGUIComponent(getActionPanel(), SwingConstants.CENTER, false, null);
      } catch (BadPositionException e) {
        LOG.error("Could not inject action bar plugin", e);
      }
    }
    else
      swingUI.injectMenuComponent(getActionPanel());
  }

  @Override
  public EnumSet<EventType> getSubscribedEventTypes() {
    return EnumSet.of(EventType.SELECTION_CHANGED, EventType.STATUS_MESSAGE_CHANGED);
  }

  @Override
  public void processMessage(EventType eventType, Object... params) {
    if (eventType == EventType.SELECTION_CHANGED) {
      boolean enabled = !plugInPort.getSelectedComponents().isEmpty();
      getContextActionToolbar().setEnabled(enabled);
    } else if (eventType == EventType.STATUS_MESSAGE_CHANGED) {
      highlightConnectedAreasUpdateAction.accept(plugInPort.getOperationMode() ==
          OperationMode.HIGHLIGHT_CONNECTED_AREAS);
    }
  }

  private record SupportOption(
    String longText,
    String shortText,
    IconLoader icon,
    String url
  ) {
    public SupportOption {
      longText = LangUtil.translate(longText);
      shortText = LangUtil.translate(shortText);
    }
  }
}
