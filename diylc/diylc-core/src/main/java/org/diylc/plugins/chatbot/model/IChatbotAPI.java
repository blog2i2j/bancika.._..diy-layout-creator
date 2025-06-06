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
package org.diylc.plugins.chatbot.model;

import com.diyfever.httpproxy.ParamName;

import java.io.File;
import java.util.List;

public interface IChatbotAPI {

  /**
   * Prompts the chatbot with a question
   *
   * @param username
   * @param token
   * @param machineId
   * @param project
   * @param netlist
   * @param prompt
   * @return
   */
  String promptChatbot(@ParamName("username") String username, @ParamName("token") String token,
      @ParamName("machineId") String machineId, @ParamName("project") String project,
      @ParamName("netlist") File netlist, @ParamName("prompt") String prompt);

  /**
   * Analyzes the provided circuit
   *
   * @param username
   * @param token
   * @param machineId
   * @param project
   * @param netlist
   * @return
   */
  String analyzeCircuit(@ParamName("username") String username, @ParamName("token") String token,
      @ParamName("machineId") String machineId, @ParamName("project") String project,
      @ParamName("netlist") File netlist);

  SubscriptionEntity getSubscriptionInfo(@ParamName("username") String username, @ParamName("token") String token,
      @ParamName("machineId") String machineId);

  String deleteChatHistory(@ParamName("username") String username, @ParamName("token") String token,
      @ParamName("machineId") String machineId, @ParamName("project") String project);

  List<ChatMessageEntity> getChatHistory(@ParamName("username") String username, @ParamName("token") String token,
      @ParamName("machineId") String machineId, @ParamName("project") String project);

  String updateChatProject(@ParamName("username") String username, @ParamName("token") String token,
      @ParamName("machineId") String machineId, @ParamName("project") String project,
      @ParamName("newProject") String newProject);
}
