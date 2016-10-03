package hydrograph.ui.help.aboutDialog;



import hydrograph.ui.datastructure.property.InstallationWindowDetails;
import hydrograph.ui.datastructure.property.JarInformationDetails;

import java.io.File;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

public class ObjectToXMLGeneration {

	public static final ObjectToXMLGeneration INSTANCE = new ObjectToXMLGeneration();
	
	public void objectToXMlConverter(File file){
		
		XStream xstream = new XStream();
		xstream.alias("InstallationWindowDetails", InstallationWindowDetails.class);
		xstream.alias("JarInformationDetail", JarInformationDetails.class);
	try {
		JarInformationDetails jarInformationDetails = new JarInformationDetails();
		JarInformationDetails jarInformationDetails1 = new JarInformationDetails();

		InstallationWindowDetails installationWindowDetails= new InstallationWindowDetails();
		installationWindowDetails.getJarInfromationDetails().add(jarInformationDetails1);
		installationWindowDetails.getJarInfromationDetails().add(jarInformationDetails);

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		xstream.toXML(installationWindowDetails, fileOutputStream);
		System.out.println(" Sucessfully xml file Converted ....");
		} catch (Exception e) {
		System.out.println("Exception :" +e);
		e.printStackTrace();
		}
	}
}
