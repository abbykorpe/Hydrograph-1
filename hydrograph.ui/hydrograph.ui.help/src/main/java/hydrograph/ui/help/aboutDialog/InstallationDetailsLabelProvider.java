package hydrograph.ui.help.aboutDialog;

import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.JarInformationDetails;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class InstallationDetailsLabelProvider implements ITableLabelProvider, ITableColorProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		JarInformationDetails jarInformationDetails = (JarInformationDetails) element;
		switch (columnIndex) {
		case 0:
			return jarInformationDetails.getName();
		case 1:
			return jarInformationDetails.getVersionNo();
		case 2:
			return jarInformationDetails.getGroupId();
		case 3:
			return jarInformationDetails.getArtifactNo();
		case 4:
			return jarInformationDetails.getLicenseInfo();
		}
		return null;
	}

	
}

	

