/**
 * 
  Package: MAG - VistA Imaging
  WARNING: Per VHA Directive 2004-038, this routine should not be modified.
  Date Created:
  Site Name:  Washington OI Field Office, Silver Spring, MD
  Developer:
  Description: 

        ;; +--------------------------------------------------------------------+
        ;; Property of the US Government.
        ;; No permission to copy or redistribute this software is given.
        ;; Use of unreleased versions of this software requires the user
        ;;  to execute a written test agreement with the VistA Imaging
        ;;  Development Office of the Department of Veterans Affairs,
        ;;  telephone (301) 734-0100.
        ;;
        ;; The Food and Drug Administration classifies this software as
        ;; a Class II medical device.  As such, it may not be changed
        ;; in any way.  Modifications to this software may result in an
        ;; adulterated medical device under 21CFR820, the use of which
        ;; is considered to be a violation of US Federal Statutes.
        ;; +--------------------------------------------------------------------+

 */
package va.gov.vista.kidsassembler.manifest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "build")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class KidsManifest {
	protected List<RequiredBuild> requiredBuilds;

	protected ManifestComponents components;

	protected String name;

	protected short patch;

	protected String patchName;

	protected short packageNumber;

	protected String packageName;
	
	protected String alphaBetaTesting;

	protected String installationMessage;
	
	protected String addressForUsageReporting;

	protected String envCheckRoutine;

	protected String deleteEnvCheckRoutine;

	protected String preInstallRoutine;

	protected String deletePreInstalltRoutine;
	
	protected String postInstallRoutine;

	protected String deletePostInstallRoutine;

	@XmlAttribute
	public String getAddressForUsageReporting() {
		return addressForUsageReporting;
	}

	@XmlAttribute
	public String getAlphaBetaTesting() {
		return alphaBetaTesting;
	}

	public ManifestComponents getComponents() {
		return components;
	}

	@XmlAttribute
	public String getDeleteEnvCheckRoutine() {
		return deleteEnvCheckRoutine;
	}

	@XmlAttribute
	public String getDeletePostInstallRoutine() {
		return deletePostInstallRoutine;
	}

	@XmlAttribute
	public String getDeletePreInstalltRoutine() {
		return deletePreInstalltRoutine;
	}

	@XmlAttribute
	public String getEnvCheckRoutine() {
		return envCheckRoutine;
	}

	@XmlAttribute
	public String getInstallationMessage() {
		return installationMessage;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	@XmlAttribute
	public String getPackageName() {
		return packageName;
	}

	@XmlAttribute
	public short getPackageNumber() {
		return packageNumber;
	}

	@XmlAttribute
	public short getPatch() {
		return patch;
	}

	@XmlAttribute
	public String getPatchName() {
		return patchName;
	}

	@XmlAttribute
	public String getPostInstallRoutine() {
		return postInstallRoutine;
	}

	@XmlAttribute
	public String getPreInstallRoutine() {
		return preInstallRoutine;
	}


	@XmlElementWrapper(name = "requiredBuilds")
	@XmlElement(name = "requiredBuild")
	public List<RequiredBuild> getRequiredBuilds() {
		return requiredBuilds;
	}

	public void setAddressForUsageReporting(String addressForUsageReporting) {
		this.addressForUsageReporting = addressForUsageReporting;
	}

	public void setAlphaBetaTesting(String alphaBetaTesting) {
		this.alphaBetaTesting = alphaBetaTesting.equalsIgnoreCase("YES") ? "y" : "n";
	}

	public void setComponents(ManifestComponents components) {
		this.components = components;
	}

	public void setDeleteEnvCheckRoutine(String deleteEnvCheckRoutine) {
		this.deleteEnvCheckRoutine = deleteEnvCheckRoutine;
	}

	public void setDeletePostInstallRoutine(String deletePostInstallRoutine) {
		this.deletePostInstallRoutine = deletePostInstallRoutine;
	}

	public void setDeletePreInstalltRoutine(String deletePreInstalltRoutine) {
		this.deletePreInstalltRoutine = deletePreInstalltRoutine;
	}

	public void setEnvCheckRoutine(String envCheckRoutine) {
		this.envCheckRoutine = envCheckRoutine;
	}

	public void setInstallationMessage(String installationMessage) {
		this.installationMessage = installationMessage.equalsIgnoreCase("YES") ? "y"
				: "n";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setPackageNumber(short packageNumber) {
		this.packageNumber = packageNumber;
	}

	public void setPatch(short patch) {
		this.patch = patch;
	}

	public void setPatchName(String patchName) {
		this.patchName = patchName;
	}

	public void setPostInstallRoutine(String postInstallRoutine) {
		this.postInstallRoutine = postInstallRoutine;
	}

	public void setPreInstallRoutine(String preInstallRoutine) {
		this.preInstallRoutine = preInstallRoutine;
	}

	public void setRequiredBuilds(List<RequiredBuild> reguiredBuilds) {
		this.requiredBuilds = reguiredBuilds;
	}
}
