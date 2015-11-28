package ztppro.util.filefilter;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

/**
 *
 * @author Damian Terlecki
 */
public class DefaultFileView extends FileView {

    @Override
    public Icon getIcon(File f) {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        return fileSystemView.getSystemIcon(f);
    }
}
