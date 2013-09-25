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
package va.gov.vista.kidsassembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import va.gov.vista.kidsassembler.components.Bulletin;
import va.gov.vista.kidsassembler.components.DataDictionary;
import va.gov.vista.kidsassembler.components.Dialog;
import va.gov.vista.kidsassembler.components.Form;
import va.gov.vista.kidsassembler.components.Function;
import va.gov.vista.kidsassembler.components.HelpFrame;
import va.gov.vista.kidsassembler.components.Hl7Application;
import va.gov.vista.kidsassembler.components.Hl7LogicalLink;
import va.gov.vista.kidsassembler.components.InputTemplate;
import va.gov.vista.kidsassembler.components.KernelComponent;
import va.gov.vista.kidsassembler.components.KernelComponentList;
import va.gov.vista.kidsassembler.components.KidsComponent;
import va.gov.vista.kidsassembler.components.KidsComponentList;
import va.gov.vista.kidsassembler.components.MailGroup;
import va.gov.vista.kidsassembler.components.MenuOption;
import va.gov.vista.kidsassembler.components.Namespace;
import va.gov.vista.kidsassembler.components.ParameterDefinition;
import va.gov.vista.kidsassembler.components.ParameterTemplate;
import va.gov.vista.kidsassembler.components.PrintTemplate;
import va.gov.vista.kidsassembler.components.Protocol;
import va.gov.vista.kidsassembler.components.Routine;
import va.gov.vista.kidsassembler.components.RemoteProcedureCall;
import va.gov.vista.kidsassembler.components.SecurityKey;
import va.gov.vista.kidsassembler.components.SortTemplate;
import va.gov.vista.kidsassembler.components.TemplateList;
import va.gov.vista.kidsassembler.manifest.RequiredBuild;
import va.gov.vista.kidsassembler.parsers.DelimitedString;

public class KidsPackage {
	private final Hashtable<String, KidsComponentList<? extends KidsComponent>> componentCollections;
	private final Collection<KernelComponentList<? extends KernelComponent>> kernelComponentCollections;
	private final KidsComponentList<DataDictionary> dataDictionaryCollection;
	private final KernelComponentList<PrintTemplate> printTemplateCollection;
	private final KernelComponentList<SortTemplate> sortTemplateCollection;
	private final KernelComponentList<InputTemplate> inputTemplateCollection;
	private final KernelComponentList<Form> formCollection;
	private final KernelComponentList<Function> functionCollection;
	private final KernelComponentList<Dialog> dialogCollection;
	private final KernelComponentList<Bulletin> bulletinCollection;
	private final KernelComponentList<MailGroup> mailGroupCollection;
	private final KernelComponentList<HelpFrame> helpFrameCollection;
	private final KernelComponentList<Routine> routineCollection;
	private final KernelComponentList<MenuOption> menuOptionCollection;
	private final KernelComponentList<SecurityKey> securityKeyCollection;
	private final KernelComponentList<Protocol> protocolCollection;
	private final KernelComponentList<TemplateList> templateListCollection;
	private final KernelComponentList<Hl7Application> hl7ApplicationCollection;
	private final KernelComponentList<Hl7LogicalLink> hl7LogicalLinkCollection;
	private final KernelComponentList<ParameterDefinition> parameterDefinitionCollection;
	private final KernelComponentList<ParameterTemplate> parameterTemplateCollection;
	private final KernelComponentList<RemoteProcedureCall> remoteProcedureCallCollection;

	private final List<Namespace> namespaceCollection;

	protected final List<RequiredBuild> requiredBuilds;
	protected String name;
	protected short patch;
	protected String patchName;
	protected String alphaBetaTesting;
	protected String installationMessage;
	protected String addressForUsageReporting;
	protected String packageName;
	protected int packageNumber;
	protected String packagePrefix;
	protected String packageShortDescription;
	protected String packageVersion;
	protected String packageDateDistributed; // TODO packageDateDistributed
												// Change to date format ?
	protected String packageDateInstalled; // TODO packageDateInstalled Change
											// to date format ?
	protected String packageInstalledBy;

	protected String envCheckRoutine;
	protected String deleteEnvCheckRoutine;
	protected String preInstallRoutine;
	protected String deletePreInstallRoutine;
	protected String postInstallRoutine;
	protected String deletePostInstallRoutine;
	protected int testVersion;
	protected int buildIEN;
	private String buildLabel;

	public KidsPackage() {
		this.dataDictionaryCollection = new KidsComponentList<DataDictionary>(
				"Data Dictionary");
		this.printTemplateCollection = new KernelComponentList<PrintTemplate>(
				"Print Template", .4f, 5);
		this.sortTemplateCollection = new KernelComponentList<SortTemplate>(
				"Sort Template", .401f, 6);
		this.inputTemplateCollection = new KernelComponentList<InputTemplate>(
				"Input Template", .402f, 7);
		this.formCollection = new KernelComponentList<Form>("Form", .403f, 8);
		this.functionCollection = new KernelComponentList<Function>("Function",
				.5f, 4);
		this.dialogCollection = new KernelComponentList<Dialog>("Dialog", .84f,
				9);
		this.bulletinCollection = new KernelComponentList<Bulletin>("Bulletin",
				3.6f, 2);
		this.mailGroupCollection = new KernelComponentList<MailGroup>(
				"Mail Group", 3.8f, 11);
		this.helpFrameCollection = new KernelComponentList<HelpFrame>(
				"Help Frame", 9.2f, 1);
		this.routineCollection = new KernelComponentList<Routine>("Routine",
				9.8f, 0);
		this.menuOptionCollection = new KernelComponentList<MenuOption>(
				"Menu Option", 19f, 18);
		this.securityKeyCollection = new KernelComponentList<SecurityKey>(
				"Security Key", 19.1f, 3);
		this.protocolCollection = new KernelComponentList<Protocol>("Protocol",
				101f, 15);
		this.templateListCollection = new KernelComponentList<TemplateList>(
				"Template List", 409.61f, 17);
		this.hl7ApplicationCollection = new KernelComponentList<Hl7Application>(
				"Hl7 Application", 771f, 14);
		this.hl7LogicalLinkCollection = new KernelComponentList<Hl7LogicalLink>(
				"Hl7 Logical Link", 870f, 13);
		this.parameterDefinitionCollection = new KernelComponentList<ParameterDefinition>(
				"Parameter Definition", 8989.51f, 20);
		this.parameterTemplateCollection = new KernelComponentList<ParameterTemplate>(
				"Parameter Template", 8989.52f, 21);
		this.remoteProcedureCallCollection = new KernelComponentList<RemoteProcedureCall>(
				"Rpc", 8994f, 16);

		kernelComponentCollections = new ArrayList<KernelComponentList<? extends KernelComponent>>() {
			private static final long serialVersionUID = 1595595449055661252L;
			{
				add(printTemplateCollection);
				add(sortTemplateCollection);
				add(inputTemplateCollection);
				add(formCollection);
				add(functionCollection);
				add(dialogCollection);
				add(bulletinCollection);
				add(mailGroupCollection);
				add(helpFrameCollection);
				add(routineCollection);
				add(menuOptionCollection);
				add(securityKeyCollection);
				add(protocolCollection);
				add(templateListCollection);
				add(hl7ApplicationCollection);
				add(hl7LogicalLinkCollection);
				add(parameterDefinitionCollection);
				add(parameterTemplateCollection);
				add(remoteProcedureCallCollection);
			}
		};

		componentCollections = new Hashtable<String, KidsComponentList<? extends KidsComponent>>();
		componentCollections.put(dataDictionaryCollection.getName(),
				dataDictionaryCollection);
		for (KernelComponentList<? extends KernelComponent> kComponentList : kernelComponentCollections) {
			componentCollections.put(kComponentList.getName(), kComponentList);
		}

		namespaceCollection = new LinkedList<Namespace>();

		requiredBuilds = new LinkedList<RequiredBuild>();
	}

	public void add(KidsComponent component) {
		if (component instanceof DataDictionary) {
			dataDictionaryCollection.add((DataDictionary) component);
		} else if (component instanceof PrintTemplate) {
			printTemplateCollection.add((PrintTemplate) component);
		} else if (component instanceof SortTemplate) {
			sortTemplateCollection.add((SortTemplate) component);
		} else if (component instanceof InputTemplate) {
			inputTemplateCollection.add((InputTemplate) component);
		} else if (component instanceof Function) {
			functionCollection.add((Function) component);
		} else if (component instanceof Dialog) {
			dialogCollection.add((Dialog) component);
		} else if (component instanceof Bulletin) {
			bulletinCollection.add((Bulletin) component);
		} else if (component instanceof MailGroup) {
			mailGroupCollection.add((MailGroup) component);
		} else if (component instanceof HelpFrame) {
			helpFrameCollection.add((HelpFrame) component);
		} else if (component instanceof Routine) {
			routineCollection.add((Routine) component);
		} else if (component instanceof MenuOption) {
			menuOptionCollection.add((MenuOption) component);
		} else if (component instanceof SecurityKey) {
			securityKeyCollection.add((SecurityKey) component);
		} else if (component instanceof Protocol) {
			protocolCollection.add((Protocol) component);
		} else if (component instanceof TemplateList) {
			templateListCollection.add((TemplateList) component);
		} else if (component instanceof Hl7Application) {
			hl7ApplicationCollection.add((Hl7Application) component);
		} else if (component instanceof Hl7LogicalLink) {
			hl7LogicalLinkCollection.add((Hl7LogicalLink) component);
		} else if (component instanceof ParameterDefinition) {
			parameterDefinitionCollection.add((ParameterDefinition) component);
		} else if (component instanceof ParameterTemplate) {
			parameterTemplateCollection.add((ParameterTemplate) component);
		} else if (component instanceof RemoteProcedureCall) {
			remoteProcedureCallCollection.add((RemoteProcedureCall) component);
		} else if (component instanceof Namespace) {
			namespaceCollection.add((Namespace) component);
		}
	}

	public void addAll(Collection<KidsComponent> t) {
		for (KidsComponent component : t) {
			add(component);
		}
	}

	public String getAddressForUsageReporting() {
		return addressForUsageReporting;
	}

	public Collection<KidsComponentList<? extends KidsComponent>> getAllComponentCollections() {
		return componentCollections.values();
	}

	public Collection<KidsComponent> getAllComponents() {
		ArrayList<KidsComponent> allComponents = new ArrayList<KidsComponent>();
		allComponents.addAll(dataDictionaryCollection);
		allComponents.addAll(getAllKernelComponents());
		return allComponents;
	}

	public Collection<KernelComponentList<? extends KernelComponent>> getAllKernelComponentCollections() {
		LinkedList<KernelComponentList<? extends KernelComponent>> collections = new LinkedList<KernelComponentList<? extends KernelComponent>>();
		collections.addAll(kernelComponentCollections);
		Collections.sort(collections);
		return collections;
	}

	public Collection<? extends KernelComponent> getAllKernelComponents() {
		ArrayList<KernelComponent> kernelComponents = new ArrayList<KernelComponent>();
		for (KernelComponentList<? extends KernelComponent> collection : this.kernelComponentCollections) {
			kernelComponents.addAll(collection);
		}
		return kernelComponents;
	}

	public Collection<KernelComponentList<? extends KernelComponent>> getAllSpecialInstructionCollections() {
		LinkedList<KernelComponentList<? extends KernelComponent>> collections = new LinkedList<KernelComponentList<? extends KernelComponent>>();
		for (KernelComponentList<? extends KernelComponent> kc : kernelComponentCollections) {
			if (kc.getSpecialInstructionIndex() > 0)
				collections.add(kc);
		}
		Collections
				.sort(collections,
						new Comparator<KernelComponentList<? extends KernelComponent>>() {
							@Override
							public int compare(
									KernelComponentList<? extends KernelComponent> o1,
									KernelComponentList<? extends KernelComponent> o2) {
								return Integer.compare(
										o1.getSpecialInstructionIndex(),
										o2.getSpecialInstructionIndex());
							}
						});
		return collections;
	}

	public String getAlphaBetaTesting() {
		return alphaBetaTesting;
	}

	public int getBuildIEN() {
		return buildIEN;
	}

	public String getBuildLabel() {
		return buildLabel;
	}

	public KernelComponentList<Bulletin> getBulletinCollection() {
		return bulletinCollection;
	}

	public KidsComponentList<DataDictionary> getDataDictionaryCollection() {
		return dataDictionaryCollection;
	}

	public String getDeleteEnvCheckRoutine() {
		return deleteEnvCheckRoutine;
	}

	public String getDeletePostInstallRoutine() {
		return deletePostInstallRoutine;
	}

	public String getDeletePreInstallRoutine() {
		return deletePreInstallRoutine;
	}

	public KernelComponentList<Dialog> getDialogCollection() {
		return dialogCollection;
	}

	public String getEnvCheckRoutine() {
		return envCheckRoutine;
	}

	public KernelComponentList<Form> getFormCollection() {
		return formCollection;
	}

	public KernelComponentList<Function> getFunctionCollection() {
		return functionCollection;
	}

	public KernelComponentList<HelpFrame> getHelpFrameCollection() {
		return helpFrameCollection;
	}

	public KernelComponentList<Hl7Application> getHl7ApplicationCollection() {
		return hl7ApplicationCollection;
	}

	public KernelComponentList<Hl7LogicalLink> getHl7LogicalLinkCollection() {
		return hl7LogicalLinkCollection;
	}

	public KernelComponentList<InputTemplate> getInputTemplateCollection() {
		return inputTemplateCollection;
	}

	public String getInstallationMessage() {
		return installationMessage;
	}

	public Collection<KernelComponentList<? extends KernelComponent>> getKernelComponentCollections() {
		return kernelComponentCollections;
	}

	public KernelComponentList<MailGroup> getMailGroupCollection() {
		return mailGroupCollection;
	}

	public KernelComponentList<MenuOption> getMenuOptionCollection() {
		return menuOptionCollection;
	}

	public String getName() {
		return name;
	}

	public List<Namespace> getNamespaceCollection() {
		return namespaceCollection;
	}

	public String getPackageDateDistributed() {
		return packageDateDistributed;
	}

	public String getPackageDateInstalled() {
		return packageDateInstalled;
	}

	public String getPackageInstalledBy() {
		return packageInstalledBy;
	}

	public String getPackageName() {
		return packageName;
	}

	public int getPackageNumber() {
		return packageNumber;
	}

	public String getPackagePrefix() {
		return packagePrefix;
	}

	public String getPackageShortDescription() {
		return packageShortDescription;
	}

	public String getPackageVersion() {
		return packageVersion;
	}

	public KernelComponentList<ParameterDefinition> getParameterDefinitionCollection() {
		return parameterDefinitionCollection;
	}

	public KernelComponentList<ParameterTemplate> getParameterTemplateCollection() {
		return parameterTemplateCollection;
	}

	public short getPatch() {
		return patch;
	}

	public String getPatchName() {
		return patchName;
	}

	public String getPostInstallRoutine() {
		return postInstallRoutine;
	}

	public String getPreInstallRoutine() {
		return preInstallRoutine;
	}

	public KernelComponentList<PrintTemplate> getPrintTemplateCollection() {
		return printTemplateCollection;
	}

	public KernelComponentList<Protocol> getProtocolCollection() {
		return protocolCollection;
	}

	public KernelComponentList<RemoteProcedureCall> getRemoteProcedureCallCollection() {
		return remoteProcedureCallCollection;
	}

	public List<RequiredBuild> getRequiredBuilds() {
		return requiredBuilds;
	}

	public KernelComponentList<Routine> getRoutineCollection() {
		return routineCollection;
	}

	public KernelComponentList<SecurityKey> getSecurityKeyCollection() {
		return securityKeyCollection;
	}

	public KernelComponentList<SortTemplate> getSortTemplateCollection() {
		return sortTemplateCollection;
	}

	public KernelComponentList<TemplateList> getTemplateListCollection() {
		return templateListCollection;
	}

	public int getTestVersion() {
		return testVersion;
	}

	public void setAddressForUsageReporting(String addressForUsageReporting) {
		this.addressForUsageReporting = addressForUsageReporting;
	}

	public void setAlphaBetaTesting(String alphaBetaTesting) {
		this.alphaBetaTesting = alphaBetaTesting;
	}

	public void setBuildIEN(int buildIEN) {
		this.buildIEN = buildIEN;
	}

	public void setBuildLabel(String buildLabel) {
		this.buildLabel = buildLabel;
	}

	public void setDeleteEnvCheckRoutine(String deleteEnvCheckRoutine) {
		this.deleteEnvCheckRoutine = deleteEnvCheckRoutine;
	}

	public void setDeletePostInstallRoutine(String deletePostInstallRoutine) {
		this.deletePostInstallRoutine = deletePostInstallRoutine;
	}

	public void setDeletePreInstallRoutine(String deletePreInstallRoutine) {
		this.deletePreInstallRoutine = deletePreInstallRoutine;
	}

	public void setEnvCheckRoutine(String envCheckRoutine) {
		this.envCheckRoutine = envCheckRoutine;
	}

	public void setInstallationMessage(String installationMessage) {
		this.installationMessage = installationMessage;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPackageDateDistributed(String packageDateDistributed) {
		this.packageDateDistributed = packageDateDistributed;
	}

	public void setPackageDateInstalled(String packageDateInstalled) {
		this.packageDateInstalled = packageDateInstalled;
	}

	public void setPackageInstalledBy(String packageInstalledBy) {
		this.packageInstalledBy = packageInstalledBy;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setPackageNumber(int packageNumber) {
		this.packageNumber = packageNumber;
	}

	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}

	public void setPackageShortDescription(String packageShortDescription) {
		this.packageShortDescription = packageShortDescription;
	}

	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
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

	public void setTestVersion(int testVersion) {
		this.testVersion = testVersion;
	}

	public void updateBuildLabel() {

		if (this.buildLabel == null || this.buildLabel.equals("")) { // V3.0p118Build169_T33
			String result;
			DelimitedString nameParts = new DelimitedString(this.name, "[*]",
					true);
			result = "V" + nameParts.getPart(2);
			result += "p" + this.patch;
			result += "Build" + Integer.toString(this.buildIEN);

			if (this.testVersion > 0) {
				result += "_T" + Integer.toString(this.testVersion);
			}

			this.setBuildLabel(result);
		}

	}
}
