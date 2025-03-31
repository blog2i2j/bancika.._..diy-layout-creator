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
package org.diylc.swing.plugins.chatbot;

import org.apache.log4j.Logger;
import org.diylc.common.BadPositionException;
import org.diylc.common.EventType;
import org.diylc.common.IPlugIn;
import org.diylc.common.IPlugInPort;
import org.diylc.core.IDIYComponent;
import org.diylc.core.Project;
import org.diylc.swing.ISwingUI;
import org.diylc.swing.plugins.config.ConfigPlugin;

import javax.swing.*;
import java.util.EnumSet;
import java.util.Set;

public class ChatbotPlugin implements IPlugIn {

  private static final Logger LOG = Logger.getLogger(ChatbotPlugin.class);

  private IPlugInPort plugInPort;
  private ISwingUI swingUI;
  private ChatbotPane chatbotPane;

  public ChatbotPlugin(ISwingUI swingUI) {
    super();
    this.swingUI = swingUI;
  }

  @Override
  public EnumSet<EventType> getSubscribedEventTypes() {
    return EnumSet.of(EventType.FILE_STATUS_CHANGED, EventType.PROJECT_LOADED,
        EventType.PROJECT_MODIFIED, EventType.SELECTION_CHANGED);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void processMessage(EventType eventType, Object... params) {
//    switch (eventType) {
//      case PROJECT_MODIFIED:
//        getChatbotPane().setComponents(((Project) params[1]).getComponents(),
//            plugInPort.getSelectedComponents());
//        break;
//      case PROJECT_LOADED:
//        getChatbotPane().setComponents(((Project) params[0]).getComponents(),
//            plugInPort.getSelectedComponents());
//        break;
//      case SELECTION_CHANGED:
//        getChatbotPane().setSelection((Set<IDIYComponent<?>>) params[0]);
//      default:
//        break;
//    }
  }

  @Override
  public void connect(IPlugInPort plugInPort) {
    this.plugInPort = plugInPort;
    try {
      this.swingUI.injectGUIComponent(getChatbotPane(), SwingConstants.RIGHT, true,
          ConfigPlugin.CHATBOT);
    } catch (BadPositionException e) {
      LOG.error("Could not install project explorer", e);
    }
  }

  public ChatbotPane getChatbotPane() {
    if (chatbotPane == null) {
      chatbotPane = new ChatbotPane(swingUI, plugInPort);
    }
    return chatbotPane;
  }
}
