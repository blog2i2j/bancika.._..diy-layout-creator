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
package org.diylc.swing.plugins.cloud.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.UIManager;

import org.diylc.swingframework.ButtonDialog;

import org.diylc.plugins.cloud.model.UserEntity;
import org.diylc.swing.gui.components.HTMLTextArea;

public class UserEditDialog extends ButtonDialog {

  private static final long serialVersionUID = 1L;

  private static final String BULLET = "&nbsp;&nbsp;&nbsp;&#8226;&nbsp;";
  private static final String PASSWORD_RULES_HTML = "<html>" + BULLET
      + "Password must be at least 8 characters long<br>" + BULLET
      + "Password must contain at least one letter<br>" + BULLET
      + "Password must contain at least one number<br>" + BULLET
      + "Passwords must match exactly<br>"
      + "</html>";

  private JPanel mainPanel;

  private JTextField userNameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JTextField emailField;
  private JTextField websiteField;
  private JTextArea bioArea;
  private JScrollPane bioPane;

  private String userName;
  private String password;
  private String email;
  private String website;
  private String bio;

  private UserEntity existingEntity;

  private JLabel passwordRulesLabel;

  public UserEditDialog(JFrame owner, UserEntity existingEntity) {
    super(owner, existingEntity == null ? "New Account" : "Manage Account", new String[] {OK, CANCEL});

    this.existingEntity = existingEntity;

    setMinimumSize(new Dimension(240, 32));
    layoutGui();
    refreshState();
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getWebsite() {
    return website;
  }

  public String getBio() {
    return bio;
  }

  @Override
  protected JComponent getMainComponent() {
    if (mainPanel == null) {
      mainPanel = new JPanel(new GridBagLayout());
      mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.NONE;
      gbc.insets = new Insets(4, 2, 2, 2);

      gbc.gridx = 0;

      gbc.gridy = 0;
      mainPanel.add(new JLabel("User Name:"), gbc);

      if (existingEntity == null) {
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridy = 2;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        
        // Add password rules hint pane
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 2, 4, 2);  // Add more vertical padding
        mainPanel.add(getPasswordRulesLabel(), gbc);
        
        // Reset gridwidth and gridx for remaining fields
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(2, 2, 2, 2);  // Reset insets
        gbc.gridy = 4;
      }

      gbc.gridy = existingEntity == null ? 4 : 3;
      gbc.gridx = 0;
      mainPanel.add(new JLabel("eMail:"), gbc);

      gbc.gridy = existingEntity == null ? 5 : 4;
      gbc.gridx = 0;
      mainPanel.add(new JLabel("Website (optional):"), gbc);

      gbc.gridy = existingEntity == null ? 6 : 5;
      gbc.gridx = 0;
      mainPanel.add(new JLabel("Short Bio (optional):"), gbc);

      gbc.gridx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(2, 2, 2, 2);
      gbc.weightx = 1;

      gbc.gridy = 0;
      mainPanel.add(getUserNameField(), gbc);

      if (existingEntity == null) {
        gbc.gridy = 1;
        mainPanel.add(getPasswordField(), gbc);

        gbc.gridy = 2;
        mainPanel.add(getConfirmPasswordField(), gbc);
      }

      gbc.gridy = existingEntity == null ? 4 : 3;
      mainPanel.add(getEmailField(), gbc);

      gbc.gridy = existingEntity == null ? 5 : 4;
      mainPanel.add(getWebsiteField(), gbc);

      gbc.gridy = existingEntity == null ? 6 : 5;
      gbc.fill = GridBagConstraints.BOTH;
      mainPanel.add(getBioPane(), gbc);
    }
    return mainPanel;
  }

  private void refreshState() {
    this.userName = getUserNameField().getText();
    String password = new String(getPasswordField().getPassword());
    String confirmPassword = new String(getConfirmPasswordField().getPassword());
    
    // Password validation
    boolean hasValidPassword = true;
    if (existingEntity == null) {
      hasValidPassword = password.length() >= 8 && 
                        password.matches(".*[a-zA-Z].*") && // at least one letter
                        password.matches(".*\\d.*");        // at least one number
    }
    
    if (password.equals(confirmPassword) && hasValidPassword) {
      this.password = password;
    } else {
      this.password = null;
    }
    this.email = getEmailField().getText();
    this.website = getWebsiteField().getText();
    this.bio = getBioArea().getText();
    JButton okButton = getButton(OK);
    okButton.setEnabled(!this.userName.isEmpty()
        && !this.email.isEmpty() && this.email.contains("@")
        && (this.existingEntity != null || ((this.password != null) && (this.password.length() > 0))));
  }

  private JTextField getUserNameField() {
    if (userNameField == null) {
      userNameField = new JTextField();
      if (existingEntity != null) {
        userNameField.setText(existingEntity.getUsername());
        userNameField.setEditable(false);
      }
      userNameField.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          refreshState();
        }
      });
    }
    return userNameField;
  }

  private JPasswordField getPasswordField() {
    if (passwordField == null) {
      passwordField = new JPasswordField();
      passwordField.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          refreshState();
        }
      });
    }
    return passwordField;
  }

  private JPasswordField getConfirmPasswordField() {
    if (confirmPasswordField == null) {
      confirmPasswordField = new JPasswordField();
      confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          refreshState();
        }
      });
    }
    return confirmPasswordField;
  }

  private JTextField getEmailField() {
    if (emailField == null) {
      emailField = new JTextField();
      if (existingEntity != null) {
        emailField.setText(existingEntity.getEmail());
        emailField.setEditable(false);
      }
      emailField.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          refreshState();
        }
      });
    }
    return emailField;
  }

  private JTextField getWebsiteField() {
    if (websiteField == null) {
      websiteField = new JTextField();
      if (existingEntity != null)
        websiteField.setText(existingEntity.getWebsite());
      websiteField.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          refreshState();
        }
      });
    }
    return websiteField;
  }

  private JScrollPane getBioPane() {
    if (bioPane == null) {
      bioPane = new JScrollPane(getBioArea());
      bioPane.setPreferredSize(new Dimension(192, 64));
      bioPane.setBorder(getBioArea().getBorder());
      getBioArea().setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }
    return bioPane;
  }

  private JTextArea getBioArea() {
    if (bioArea == null) {
      bioArea = new HTMLTextArea();
      if (existingEntity != null)
        bioArea.setText(existingEntity.getBio());
      bioArea.setFont(getUserNameField().getFont());
      bioArea.setBorder(getUserNameField().getBorder());
      bioArea.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          refreshState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          refreshState();
        }
      });
    }
    return bioArea;
  }

  private JLabel getPasswordRulesLabel() {
    if (passwordRulesLabel == null) {
      passwordRulesLabel = new JLabel();
      passwordRulesLabel.setOpaque(true);
      passwordRulesLabel.setBackground(UIManager.getColor("ToolTip.background"));
      passwordRulesLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
          BorderFactory.createEmptyBorder(2, 2, 2, 2)));
      passwordRulesLabel.setText(PASSWORD_RULES_HTML);
    }
    return passwordRulesLabel;
  }
}
