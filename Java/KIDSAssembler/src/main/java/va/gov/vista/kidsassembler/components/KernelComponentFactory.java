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
package va.gov.vista.kidsassembler.components;

public class KernelComponentFactory {
	public KernelComponentFactory() {
		
	}

	public static KernelComponent createKernelComponent(String fileNumber) {
		KernelComponent result = null;

		switch (fileNumber) {
		case ".4":
			result = new PrintTemplate();
			break;
		case ".401":
			result = new SortTemplate();
			break;
		case ".402":
			result = new InputTemplate();
			break;
		case ".403":
			result = new Form();
			break;
		case ".5":
			result = new Function();
			break;
		case ".84":
			result = new Dialog();
			break;
		case "3.6":
			result = new  Bulletin();
			break;
		case "3.8":
			result = new MailGroup();
			break;
		case "9.2":
			result = new HelpFrame();
			break;
		case "9.8":
			result = new Routine();
			break;
		case "19":
			result = new MenuOption();
			break;
		case "19.1":
			result = new SecurityKey();
			break;
		case "101":
			result = new Protocol();
			break;
		case "409.61":
			result = new TemplateList();
			break;
		case "771":
			result = new Hl7Application();
			break;
		case "870":
			result = new Hl7LogicalLink();
			break;
		case "8989.51":
			result = new ParameterDefinition();
			break;
		case "8989.52":
			result = new ParameterTemplate();
			break;
		case "8994":
			result = new RemoteProcedureCall();
			break;
		}
		return result;
	}
}
