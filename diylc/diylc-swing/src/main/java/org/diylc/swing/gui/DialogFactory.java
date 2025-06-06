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
package org.diylc.swing.gui;

import java.awt.Window;
import java.io.File;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import org.diylc.appframework.miscutils.ConfigurationManager;
import org.diylc.swingframework.AboutDialog;
import org.diylc.swingframework.IFileChooserAccessory;
import org.diylc.swingframework.ProgressDialog;

import org.diylc.common.IPlugInPort;
import org.diylc.common.PropertyWrapper;
import org.diylc.netlist.ParsedNetlistEntry;
import org.diylc.plugins.cloud.model.UserEntity;
import org.diylc.swing.gui.components.OverwritePromptFileChooser;
import org.diylc.swing.gui.editor.PropertyEditorDialog;
import org.diylc.swing.plugins.cloud.view.ChangePasswordDialog;
import org.diylc.swing.plugins.cloud.view.LoginDialog;
import org.diylc.swing.plugins.cloud.view.UploadDialog;
import org.diylc.swing.plugins.cloud.view.UserEditDialog;
import org.diylc.swing.plugins.edit.FindDialog;
import org.diylc.swing.plugins.file.BomDialog;
import org.diylc.swing.plugins.file.NetlistImportDialog;
import org.diylc.utils.BomEntry;

public class DialogFactory {

  private static DialogFactory instance;

  private static final String PATH_KEY = "lastPath";

  public static DialogFactory getInstance() {
    if (instance == null) {
      instance = new DialogFactory();
    }
    return instance;
  }

  private JFrame mainFrame;
  private File lastDirectory;

  private DialogFactory() {}

  /**
   * Sets the frame to be used as dialog parent. This should be called prior to any other methods in
   * this class.
   * 
   * @param mainFrame
   */
  public void initialize(JFrame mainFrame) {
    this.mainFrame = mainFrame;
    String lastDirectoryPath = (String) ConfigurationManager.getInstance().readString(PATH_KEY, null);
    if (lastDirectoryPath != null) {
      lastDirectory = new File(lastDirectoryPath);
    }
  }

  public PropertyEditorDialog createPropertyEditorDialog(List<PropertyWrapper> properties, String title,
      boolean saveDefaults) {
    return createPropertyEditorDialog(mainFrame, properties, title, saveDefaults);
  }

  public PropertyEditorDialog createPropertyEditorDialog(JFrame owner, List<PropertyWrapper> properties, String title,
      boolean saveDefaults) {
    PropertyEditorDialog editor = new PropertyEditorDialog(owner, properties, title, saveDefaults);
    return editor;
  }

  public InfoDialog createInfoDialog(String tipKey) {
    return new InfoDialog(mainFrame, tipKey);
  }

  public BomDialog createBomDialog(List<BomEntry> bom, String initialFileName) {
    BomDialog dialog = new BomDialog(mainFrame, bom, initialFileName);
    return dialog;
  }

  public File showOpenDialog(FileFilter fileFilter, File initialFile, String defaultExtension,
      IFileChooserAccessory accessory) {
    JFileChooser openFileChooser = new JFileChooser();
    initializeFileChooser(openFileChooser, fileFilter, initialFile, defaultExtension, accessory, false);

    int result = openFileChooser.showOpenDialog(mainFrame);

    return processFileChooserResult(result, openFileChooser, defaultExtension);
  }
  
  public File showOpenDialog(Window owner, FileFilter fileFilter, File initialFile, String defaultExtension,
      IFileChooserAccessory accessory) {
    JFileChooser openFileChooser = new JFileChooser();
    initializeFileChooser(openFileChooser, fileFilter, initialFile, defaultExtension, accessory, false);

    int result = openFileChooser.showOpenDialog(owner);

    return processFileChooserResult(result, openFileChooser, defaultExtension);
  }

  public File showOpenDialog(FileFilter fileFilter, File initialFile, String defaultExtension,
      IFileChooserAccessory accessory, JFrame ownerFrame) {
    JFileChooser openFileChooser = new JFileChooser();
    initializeFileChooser(openFileChooser, fileFilter, initialFile, defaultExtension, accessory, false);

    int result = openFileChooser.showOpenDialog(ownerFrame);

    return processFileChooserResult(result, openFileChooser, defaultExtension);
  }

  public File[] showOpenMultiDialog(FileFilter fileFilter, File initialFile, String defaultExtension,
      IFileChooserAccessory accessory, JFrame ownerFrame) {
    JFileChooser openFileChooser = new JFileChooser();
    initializeFileChooser(openFileChooser, fileFilter, initialFile, defaultExtension, accessory, true);

    int result = openFileChooser.showOpenDialog(ownerFrame);

    return processFileMultiChooserResult(result, openFileChooser, defaultExtension);
  }

  public File showSaveDialog(Window owner, FileFilter fileFilter, File initialFile, String defaultExtension,
      IFileChooserAccessory accessory) {
    JFileChooser saveFileChooser = new OverwritePromptFileChooser();
    initializeFileChooser(saveFileChooser, fileFilter, initialFile, defaultExtension, accessory, false);

    int result = saveFileChooser.showSaveDialog(owner);

    return processFileChooserResult(result, saveFileChooser, defaultExtension);
  }

  private void initializeFileChooser(JFileChooser fileChooser, FileFilter fileFilter, File initialFile,
      String defaultExtension, IFileChooserAccessory accessory, boolean allowMultiSelect) {
    if (accessory != null) {
      accessory.install(fileChooser);
    }
    for (FileFilter filter : fileChooser.getChoosableFileFilters()) {
      fileChooser.removeChoosableFileFilter(filter);
    }
    if (fileChooser instanceof OverwritePromptFileChooser) {
      ((OverwritePromptFileChooser) fileChooser).setFileFilter(fileFilter, defaultExtension);
    } else {
      fileChooser.setFileFilter(fileFilter);
    }
    if (lastDirectory != null) {
      fileChooser.setCurrentDirectory(lastDirectory);
    }
    fileChooser.setSelectedFile(initialFile);
    if (allowMultiSelect)
      fileChooser.setMultiSelectionEnabled(true);
  }

  private File processFileChooserResult(int result, JFileChooser fileChooser, String defaultExtension) {
    fileChooser.setAccessory(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      lastDirectory = fileChooser.getCurrentDirectory();
      ConfigurationManager.getInstance().writeValue(PATH_KEY, lastDirectory.getAbsolutePath());
      if (fileChooser.getSelectedFile().getName().contains(".")) {
        return fileChooser.getSelectedFile();
      } else {
        return new File(fileChooser.getSelectedFile().getAbsoluteFile() + "." + defaultExtension);
      }
    } else {
      return null;
    }
  }

  private File[] processFileMultiChooserResult(int result, JFileChooser fileChooser, String defaultExtension) {
    fileChooser.setAccessory(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      lastDirectory = fileChooser.getCurrentDirectory();
      ConfigurationManager.getInstance().writeValue(PATH_KEY, lastDirectory.getAbsolutePath());
      return fileChooser.getSelectedFiles();
    } else {
      return null;
    }
  }

  public AboutDialog createAboutDialog(String appName, Icon icon, String version, String author, String url,
      String mail, String htmlContent) {
    AboutDialog dialog = new AboutDialog(mainFrame, appName, icon, version, author, url, mail, htmlContent);
    return dialog;
  }

  public UserEditDialog createUserEditDialog(UserEntity existingUser) {
    UserEditDialog dialog = new UserEditDialog(mainFrame, existingUser);
    return dialog;
  }

  public LoginDialog createLoginDialog() {
    LoginDialog dialog = new LoginDialog(mainFrame);
    return dialog;
  }
  
  public FindDialog createFindDialog() {
    FindDialog dialog = new FindDialog(mainFrame);
    return dialog;
  }

  public UploadDialog createUploadDialog(JFrame ownerFrame, IPlugInPort plugInPort, String[] categories,
      boolean isUpdate) {
    UploadDialog dialog = new UploadDialog(ownerFrame, plugInPort, categories, isUpdate);
    return dialog;
  }

  public ChangePasswordDialog createChangePasswordDialog() {
    ChangePasswordDialog dialog = new ChangePasswordDialog(mainFrame);
    return dialog;
  }

  public ProgressDialog createProgressDialog(String title, String[] buttonCaptions, String description,
      boolean useProgress) {
    ProgressDialog dialog = new ProgressDialog(mainFrame, title, buttonCaptions, description, useProgress);
    return dialog;
  }
  
  public NetlistImportDialog createNetlistImportDialog(IPlugInPort plugInPort, List<ParsedNetlistEntry> entries) {
    NetlistImportDialog dialog = new NetlistImportDialog(mainFrame, plugInPort, entries);
    return dialog;
  }
}
