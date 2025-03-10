package org.diylc.swing.actions.edit;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.diylc.clipboard.ComponentTransferableFactory;
import org.diylc.common.IPlugInPort;
import org.diylc.swing.ActionFactory;
import org.diylc.swing.images.IconLoader;

public class CutAction extends AbstractAction {

  private static final long serialVersionUID = 1L;

  private IPlugInPort plugInPort;
  private Clipboard clipboard;
  private ClipboardOwner clipboardOwner;

  public CutAction(IPlugInPort plugInPort, Clipboard clipboard, ClipboardOwner clipboardOwner) {
    super();
    this.plugInPort = plugInPort;
    this.clipboard = clipboard;
    this.clipboardOwner = clipboardOwner;
    putValue(AbstractAction.NAME, "Cut");
    putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    putValue(AbstractAction.SMALL_ICON, IconLoader.Cut.getIcon());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ActionFactory.LOG.info("Cut triggered");
    clipboard.setContents(ComponentTransferableFactory.getInstance()
        .build(plugInPort.getSelectedComponents(), plugInPort.getCurrentProject().getGroups()),
        clipboardOwner);
    plugInPort.deleteSelectedComponents();
  }
}