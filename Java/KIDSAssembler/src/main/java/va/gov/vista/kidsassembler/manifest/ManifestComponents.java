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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "components")
@XmlAccessorType(XmlAccessType.FIELD)
public class ManifestComponents {
	@XmlElementWrapper(name = "dataDictionaries")
	@XmlElement(name = "dataDictionary")
	protected List<ManifestComponent> dataDictionaries;

	@XmlElementWrapper(name = "routines")
	@XmlElement(name = "routine")
	protected List<ManifestComponent> routines;

	@XmlElementWrapper(name = "rpcs")
	@XmlElement(name = "rpc")
	protected List<ManifestComponent> rpcs;

	@XmlElementWrapper(name = "options")
	@XmlElement(name = "option")
	protected List<ManifestComponent> menuOptions;

	@XmlElementWrapper(name = "securityKeys")
	@XmlElement(name = "securityKey")
	protected List<ManifestComponent> securityKeys;

	@XmlElementWrapper(name = "protocols")
	@XmlElement(name = "protocol")
	protected List<ManifestComponent> protocols;

	@XmlElementWrapper(name = "templateLists")
	@XmlElement(name = "templateList")
	protected List<ManifestComponent> templateLists;

	@XmlElementWrapper(name = "hl7Applications")
	@XmlElement(name = "hl7Application")
	protected List<ManifestComponent> hl7Applications;

	@XmlElementWrapper(name = "hl7LogicalLinks")
	@XmlElement(name = "hl7LogicalLink")
	protected List<ManifestComponent> hl7LogicalLinks;

	@XmlElementWrapper(name = "parameterDefinitions")
	@XmlElement(name = "parameterDefinition")
	protected List<ManifestComponent> parameterDefinitions;

	@XmlElementWrapper(name = "parameterTemplates")
	@XmlElement(name = "parameterTemplate")
	protected List<ManifestComponent> parameterTemplates;

	@XmlElementWrapper(name = "printTemplates")
	@XmlElement(name = "printTemplate")
	protected List<ManifestComponent> printTemplates;

	@XmlElementWrapper(name = "sortTemplates")
	@XmlElement(name = "sortTemplate")
	protected List<ManifestComponent> sortTemplates;

	@XmlElementWrapper(name = "inputTemplates")
	@XmlElement(name = "inputTemplate")
	protected List<ManifestComponent> inputTemplates;

	@XmlElementWrapper(name = "forms")
	@XmlElement(name = "form")
	protected List<ManifestComponent> forms;

	@XmlElementWrapper(name = "functions")
	@XmlElement(name = "function")
	protected List<ManifestComponent> functions;

	@XmlElementWrapper(name = "dialogs")
	@XmlElement(name = "dialog")
	protected List<ManifestComponent> dialogs;

	@XmlElementWrapper(name = "bulletins")
	@XmlElement(name = "bulletin")
	protected List<ManifestComponent> bulletins;

	@XmlElementWrapper(name = "mailGroups")
	@XmlElement(name = "mailGroup")
	protected List<ManifestComponent> mailGroups;

	@XmlElementWrapper(name = "helpFrames")
	@XmlElement(name = "helpFrame")
	protected List<ManifestComponent> helpFrames;

	public List<ManifestComponent> getAllComponents() {
		ArrayList<ManifestComponent> all = new ArrayList<ManifestComponent>();
		if (this.bulletins != null)
			all.addAll(this.bulletins);
		if (this.dataDictionaries != null)
			all.addAll(this.dataDictionaries);
		if (this.dialogs != null)
			all.addAll(this.dialogs);
		if (this.forms != null)
			all.addAll(this.forms);
		if (this.functions != null)
			all.addAll(this.functions);
		if (this.helpFrames != null)
			all.addAll(this.helpFrames);
		if (this.hl7Applications != null)
			all.addAll(this.hl7Applications);
		if (this.hl7LogicalLinks != null)
			all.addAll(this.hl7LogicalLinks);
		if (this.inputTemplates != null)
			all.addAll(this.inputTemplates);
		if (this.mailGroups != null)
			all.addAll(this.mailGroups);
		if (this.menuOptions != null)
			all.addAll(this.menuOptions);
		if (this.parameterDefinitions != null)
			all.addAll(this.parameterDefinitions);
		if (this.parameterTemplates != null)
			all.addAll(this.parameterTemplates);
		if (this.printTemplates != null)
			all.addAll(this.printTemplates);
		if (this.protocols != null)
			all.addAll(this.protocols);
		if (this.routines != null)
			all.addAll(this.routines);
		if (this.rpcs != null)
			all.addAll(this.rpcs);
		if (this.securityKeys != null)
			all.addAll(this.securityKeys);
		if (this.sortTemplates != null)
			all.addAll(this.sortTemplates);
		if (this.templateLists != null)
			all.addAll(this.templateLists);

		return all;
	}

	public List<ManifestComponent> getBulletins() {
		return bulletins;
	}

	public List<ManifestComponent> getDataDictionaries() {
		return dataDictionaries;
	}

	public List<ManifestComponent> getDialogs() {
		return dialogs;
	}

	public List<ManifestComponent> getForms() {
		return forms;
	}

	public List<ManifestComponent> getFunctions() {
		return functions;
	}

	public List<ManifestComponent> getHelpFrames() {
		return helpFrames;
	}

	public List<ManifestComponent> getHl7Applications() {
		return hl7Applications;
	}

	public List<ManifestComponent> getHl7LogicalLinks() {
		return hl7LogicalLinks;
	}

	public List<ManifestComponent> getInputTemplates() {
		return inputTemplates;
	}

	public List<ManifestComponent> getMailGroups() {
		return mailGroups;
	}

	public List<ManifestComponent> getMenuOptions() {
		return menuOptions;
	}

	public List<ManifestComponent> getParameterDefinitions() {
		return parameterDefinitions;
	}

	public List<ManifestComponent> getParameterTemplates() {
		return parameterTemplates;
	}

	public List<ManifestComponent> getPrintTemplates() {
		return printTemplates;
	}

	public List<ManifestComponent> getProtocols() {
		return protocols;
	}

	public List<ManifestComponent> getRoutines() {
		return routines;
	}

	public List<ManifestComponent> getRpcs() {
		return rpcs;
	}

	public List<ManifestComponent> getSecurityKeys() {
		return securityKeys;
	}

	public List<ManifestComponent> getSortTemplates() {
		return sortTemplates;
	}

	public List<ManifestComponent> getTemplateLists() {
		return templateLists;
	}

	public void setBulletins(List<ManifestComponent> bulletins) {
		this.bulletins = bulletins;
	}

	public void setDataDictionaries(List<ManifestComponent> dataDictionaries) {
		this.dataDictionaries = dataDictionaries;
	}

	public void setDialogs(List<ManifestComponent> dialogs) {
		this.dialogs = dialogs;
	}

	public void setForms(List<ManifestComponent> forms) {
		this.forms = forms;
	}

	public void setFunctions(List<ManifestComponent> functions) {
		this.functions = functions;
	}

	public void setHelpFrames(List<ManifestComponent> helpFrames) {
		this.helpFrames = helpFrames;
	}

	public void setHl7Applications(List<ManifestComponent> hl7Applications) {
		this.hl7Applications = hl7Applications;
	}

	public void setHl7LogicalLinks(List<ManifestComponent> hl7LogicalLinks) {
		this.hl7LogicalLinks = hl7LogicalLinks;
	}

	public void setInputTemplates(List<ManifestComponent> inputTemplates) {
		this.inputTemplates = inputTemplates;
	}

	public void setMailGroups(List<ManifestComponent> mailGroups) {
		this.mailGroups = mailGroups;
	}

	public void setOptions(List<ManifestComponent> menuOptions) {
		this.menuOptions = menuOptions;
	}

	public void setParameterDefinitions(
			List<ManifestComponent> parameterDefinitions) {
		this.parameterDefinitions = parameterDefinitions;
	}

	public void setParameterTemplates(List<ManifestComponent> parameterTemplates) {
		this.parameterTemplates = parameterTemplates;
	}

	public void setPrintTemplates(List<ManifestComponent> printTemplates) {
		this.printTemplates = printTemplates;
	}

	public void setProtocols(List<ManifestComponent> protocols) {
		this.protocols = protocols;
	}

	public void setRoutines(List<ManifestComponent> routines) {
		this.routines = routines;
	}

	public void setRpcs(List<ManifestComponent> rpcs) {
		this.rpcs = rpcs;
	}

	public void setSecurityKeys(List<ManifestComponent> securityKeys) {
		this.securityKeys = securityKeys;
	}

	public void setSortTemplates(List<ManifestComponent> sortTemplates) {
		this.sortTemplates = sortTemplates;
	}

	public void setTemplateLists(List<ManifestComponent> templateLists) {
		this.templateLists = templateLists;
	}
}
