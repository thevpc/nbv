/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.nb.plugins.nvb;

/**
 *
 * @author vpc
 */
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;
@ServiceProvider(service = ProjectFactory.class)
public class CustomFolderProjectFactory implements ProjectFactory {
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return DataFolder.findFolder(projectDirectory) != null;
    }
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new FolderProject(dir) : null;
    }
    @Override
    public void saveProject(Project prjct) throws IOException, ClassCastException {
        // leave unimplemented for the moment
    }
    private class FolderProject implements Project {
        private final FileObject projectDir;
        private Lookup lkp;
        private FolderProject(FileObject dir) {
            this.projectDir = dir;
        }
        @Override
        public FileObject getProjectDirectory() {
            return projectDir;
        }
        @Override
        public Lookup getLookup() {
            if (lkp == null) {
                lkp = Lookups.fixed(new Object[]{
                    new Info(),
                });
            }
            return lkp;
        }
        private final class Info implements ProjectInformation {
            @Override
            public Icon getIcon() {
                Icon icon = null;
                try {
                    icon = ImageUtilities.image2Icon(
                            new FilterNode(DataFolder.find(
                            getProjectDirectory()).getNodeDelegate()).getIcon(1));
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return icon;
            }
            @Override
            public String getName() {
                return getProjectDirectory().getName();
            }
            @Override
            public String getDisplayName() {
                return getName();
            }
            @Override
            public void addPropertyChangeListener(PropertyChangeListener pcl) {
                //do nothing, won't change
            }
            @Override
            public void removePropertyChangeListener(PropertyChangeListener pcl) {
                //do nothing, won't change
            }
            @Override
            public Project getProject() {
                return FolderProject.this;
            }
        }
    }
}