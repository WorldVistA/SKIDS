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
package va.gov.vista.kidsassembler.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import va.gov.vista.kidsassembler.components.DataDictionary;
import va.gov.vista.kidsassembler.components.GlobalNode;
import va.gov.vista.kidsassembler.components.Index;
import va.gov.vista.kidsassembler.components.KernelComponent;
import va.gov.vista.kidsassembler.components.KernelComponentFactory;
import va.gov.vista.kidsassembler.components.Key;
import va.gov.vista.kidsassembler.components.KeyPtr;
import va.gov.vista.kidsassembler.components.KidsComponent;
import va.gov.vista.kidsassembler.components.Namespace;
import va.gov.vista.kidsassembler.components.Routine;

public class KidsParser implements ComponentParser {
	private static final Logger logger = Logger.getLogger(ComponentParser.class);
	protected static final ArrayList<String> skippedRootNodes = new ArrayList<String>();
	protected static final ArrayList<String> skippedBldNodes = new ArrayList<String>();
	protected final Namespace namespaceComponent = new Namespace("");
	
	protected List<KidsComponent> components;

	static {
		skippedRootNodes.add("\"INI\")");
		skippedRootNodes.add("\"INIT\")");
		skippedRootNodes.add("\"MBREQ\")");
		skippedRootNodes.add("\"PKG\")");
		skippedRootNodes.add("\"PRE\")");
		skippedRootNodes.add("\"QUES\",");
		skippedRootNodes.add("\"ORD\",");
		// skippedRootNodes.add("\"RTN\")");
		skippedRootNodes.add("\"VER\")");

		skippedBldNodes.add("0)");
		skippedBldNodes.add("6.3)");
		skippedBldNodes.add("1)");
		skippedBldNodes.add("\"INI\")");
		skippedBldNodes.add("\"INIT\")");
		skippedBldNodes.add("\"INID\")");
		skippedBldNodes.add("\"ABPKG\")");
	}

	private GlobalNode createGlobalNode(DelimitedString nodeParts,
			String dataLine, int nodeStartIndex) {
		StringBuilder nodeBuilder = new StringBuilder();
		for (int i = nodeStartIndex; i <= nodeParts.getNumParts(); i++) {
			if (nodeBuilder.length() > 0)
				nodeBuilder.append(",");
			nodeBuilder.append(nodeParts.getPart(i));
		}
		GlobalNode node = new GlobalNode(nodeBuilder.toString(), dataLine);
		return node;
	}
	
	private DataDictionary getDataDictionary(float fileNumber) {
		DataDictionary component = null;
		for (KidsComponent c : components) {
			if (c instanceof DataDictionary
					&& ((DataDictionary) c).getFileNumber() == fileNumber) {
				component = ((DataDictionary) c);
				break;
			}
		}
		return component;
	}

	private Routine getRoutine(String routineName) {
		Routine component = null;
		for (KidsComponent c : components) {
			if (c instanceof Routine
					&& ((Routine) c).getName().equals(routineName)) {
				component = ((Routine) c);
				break;
			}
		}
		return component;
	}
	
	private KernelComponent getKernelComponent(int ien) {
		KernelComponent component = null;
		for (KidsComponent c : components) {
			if (((KernelComponent) c).getIen() == ien) {
				component = (KernelComponent) c;
				break;
			}
		}
		return component;
	}

	@Override
	public List<KidsComponent> parse(String name, File file) {
		components = new ArrayList<KidsComponent>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			logger.debug("Parsing file: " + file);
			
			String nodeLine, dataLine;
			DelimitedString nodeParts;

			// Skip first six lines
			for (int i = 1; i <= 6; i++) {
				if (reader.readLine() == null) {
					break;
				}
			}

			while ((nodeLine = reader.readLine()) != null) {
				if ((dataLine = reader.readLine()) == null) {
					break;
				}

				nodeParts = new DelimitedString(nodeLine, ",", true);
				if (skippedRootNodes.contains(nodeParts.getPartOrNull(1)))
					continue;
				switch (nodeParts.getPartOrNull(1)) {
				case "\"BLD\"":
					if (skippedBldNodes.contains(nodeParts.getPartOrNull(3)))
						continue;
					processBLDNode(nodeParts, dataLine);
					break;
				case "\"KRN\"":
					processKRNNode(nodeParts, dataLine);
					break;
				case "\"FIA\"":
					processFIANode(nodeParts, dataLine);
					break;
				case "\"IX\"":
					processIXNode(nodeParts, dataLine);
					break;
				case "\"KEY\"":
					processKEYNode(nodeParts, dataLine);
					break;
				case "\"KEYPTR\"":
					processKEYPTRNode(nodeParts, dataLine);
					break;
				case "\"^DD\"":
					processDDNode(nodeParts, dataLine);
					break;
				case "\"DATA\"":
					processDATANode(nodeParts, dataLine);
					break;
				case "\"^DIC\"":
					processDICNode(nodeParts, dataLine);
					break;
				case "\"SEC\"":
					processSECNode(nodeParts, dataLine);
					break;
				case "\"FRV1\"":
					processFileReferenceNode(nodeParts, dataLine);
					break;
				case "\"UP\"":
					processUpLinkNode(nodeParts, dataLine);
					break;
				case "\"PGL\"":
					processPointerToGlobalNode(nodeParts, dataLine);
					break;
				case "\"RTN\"":
					processRoutineNode(nodeParts, dataLine);
					break;

				}
				// if (nodeLineCommaPiece3.equals("10")) {
				// // p4 = GetPiece (s1, _T(","), 4, 4);
				// // if (!strcmp (p4, _T("0)")))
				// // {
				// // skip = 1;
				// // }
				// }
				//
				// if (nodeLineCommaPiece3.equals("\"ABNS\"")) {
				// // int d1, d2;
				// // use = 1;
				// // if (!strcmp (GetPiece (s1, _T(","), 4, 4), _T("0)"))
				// // && NonZero (s2))
				// // InsertString (s2, &namespacelist, &d1, &d2);
				// }
				//

			}

		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe);
		} catch (IOException ioe) {
			logger.error(ioe);
		}
		logger.debug(String.format("Found %s components", components.size()));
		return components;
	}

	private void processBLDNode(DelimitedString nodeParts, String dataLine) {

		switch (nodeParts.getPartOrNull(3)) {
		case "\"KRN\"":
			break;
		case "4":
			process4Node(nodeParts, dataLine);		// Parse 4 node data dictionary 
			break;
		case "\"ABNS\"":
			processABNSNode(nodeParts, dataLine);
			break;
		}
	}

	private void processABNSNode(DelimitedString nodeParts, String dataLine) {		// "BLD",3463,4,
		//	"BLD",7499,"ABNS",0)
		//	^9.66A^1^1
		//	"BLD",7499,"ABNS",1,0)
		//	MAG
		//	"BLD",7499,"ABNS","B","MAG",1)
		//
		
		switch (nodeParts.getPart(4)) {
		case "0)":		//	"BLD",7499,"ABNS",0)
			components.add(namespaceComponent);
			return;
		case "\"B\"":
			return;
		}
		
		GlobalNode node = new GlobalNode("", dataLine);
		namespaceComponent.getBody().add(node);
		
	}
	
	private void process4Node(DelimitedString nodeParts, String dataLine) {		// "BLD",3463,4,
		DataDictionary component;
		if (nodeParts.getNumParts() == 4) {		//	"BLD",3463,4,0)
												//	^9.64PA^2006.9422^40
			return;		
		}
		
		if (nodeParts.getPart(4).equals("\"APDD\"")) {		// "BLD",3463,4,"APDD",2005,2005)
			String fileNumberText = nodeParts.getPart(5);
			float fileNumber = Float.parseFloat(fileNumberText);
			
			component = getDataDictionary(fileNumber);
			if (component == null) {
				logger.error("Error finding component: " + fileNumber + " : " +nodeParts.getOriginal());
				return;
			}
			
			GlobalNode node = new GlobalNode(nodeParts.getParts(3, 99), dataLine);
			component.getPartialDefinitionBody().add(node);
			
			return;
		}
		
		if (nodeParts.getPart(4).equals("\"B\"")) {			// "BLD",3463,4,"B",2005,2005)
			String fileNumberText = nodeParts.getPart(5);
			float fileNumber = Float.parseFloat(fileNumberText);
			
			component = getDataDictionary(fileNumber);
			if (component == null) {
				logger.error("Error finding component: " + nodeParts.getOriginal());
				return;
			}
			
			GlobalNode node = new GlobalNode(nodeParts.getParts(3, 99), dataLine);
			component.getBCrossReferenceBody().add(node);
			
			return;
	}
		
		String fileNumberText = nodeParts.getPart(4);
		float fileNumber = Float.parseFloat(fileNumberText);
		if (nodeParts.getPart(5).equals("0)")) {	// 	"BLD",3463,4,2005,0)
				component = new DataDictionary(fileNumber, dataLine);		
				components.add(component);
		}
		else
		{
			component = getDataDictionary(fileNumber);
			if (component == null) {
				// something wrong print error or exception
			}
		}
		
		GlobalNode node = new GlobalNode(nodeParts.getParts(3, 99), dataLine);
		component.getDefinitionBody().add(node);
	}

	private void processDDNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getBody().add(node);
	}

	private void processDATANode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getDataBody().add(node);
	}
	
	private void processDICNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getDicBody().add(node);
	}
	
	private void processSECNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(3));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getSecBody().add(node);
	}
	
	private void processFIANode(DelimitedString nodeParts, String dataLine) {
		DataDictionary component;
		String fileNumberText = nodeParts.getPart(2);
		if (fileNumberText.endsWith(")")) {
			fileNumberText = fileNumberText.substring(0,
					fileNumberText.length() - 1);
		}
		float fileNumber = Float.parseFloat(fileNumberText);
		component = getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getFiaBody().add(node);
	}

	private void processIXNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = getDataDictionary(fileNumber);
		Index index = null;
		String name = nodeParts.getPartOrEmpty(4);
		if (nodeParts.getNumParts() == 5
				&& nodeParts.getPartOrEmpty(5).equals("0)")) {
			index = new Index(name);
			component.getIndeces().add(index);
		} else {
			for (Index i : component.getIndeces()) {
				if (i.getName().equals(name)) {
					index = i;
					break;
				}
			}
		}
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		index.getBody().add(node);
	}
	
	private void processKEYNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = getDataDictionary(fileNumber);
		Key key = null;
		String name = nodeParts.getPartOrEmpty(4);
		if (nodeParts.getNumParts() == 5
				&& nodeParts.getPartOrEmpty(5).equals("0)")) {
			key = new Key(name);
			component.getKeys().add(key);
		} else {
			for (Key i : component.getKeys()) {
				if (i.getName().equals(name)) {
					key = i;
					break;
				}
			}
		}
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		key.getBody().add(node);
	}

	private void processKEYPTRNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = getDataDictionary(fileNumber);
		String name = nodeParts.getPartOrEmpty(4);
		KeyPtr keyPtr = new KeyPtr(name);
		component.getKeyPtrs().add(keyPtr);
	
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		keyPtr.getBody().add(node);
	}

	private void processFileReferenceNode(DelimitedString nodeParts, String dataLine) {
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getFrv1Body().add(node);
	}

	private void processUpLinkNode(DelimitedString nodeParts, String dataLine) {
		// "UP",70,70.02,-1)
		//	70^DT
		if (nodeParts.getNumParts() != 4) { 
			return;
		}
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getUpBody().add(node);
	}
	
	private void processPointerToGlobalNode(DelimitedString nodeParts, String dataLine) {
	
		float fileNumber = Float.parseFloat(nodeParts.getPart(2));
		DataDictionary component = this.getDataDictionary(fileNumber);
		GlobalNode node = new GlobalNode(nodeParts.original, dataLine);
		component.getPglBody().add(node);
	}
	
	private void processRoutineNode(DelimitedString nodeParts, String dataLine) {
		//	"RTN","MAGDTR03")
		//	0^2^B74736131
		//
		//	or
		//
		//	"RTN","MAGDTR03",1,0)
		//	MAGDTR03 ;WOIFO/PMK/NST - Read a DICOM image file ; 12 Apr 2012 1:24 PM
		
		String routineName = nodeParts.getPart(2).replaceAll("\"","");;
		
		if (nodeParts.getNumParts() == 2) {		// "RTN","MAGDTR03")
			routineName = routineName.substring(0,routineName.length() - 1);
			Routine component = new Routine();
			component.setName(routineName);
			DelimitedString dataParts = new DelimitedString(dataLine, "\\^", true);
			component.setAction(dataParts.getPart(1));
			components.add(component);
			return;
		}
		
		Routine component = this.getRoutine(routineName);
		if (component == null) {
			logger.error("Error finding component: " + routineName + " : " + nodeParts.getOriginal());
			return;
		}

		GlobalNode node = new GlobalNode(nodeParts.getParts(3,99), dataLine);
		component.getBody().add(node);
	}

	private void processKRNNode(DelimitedString nodeParts, String dataLine) {
		KernelComponent component;
		GlobalNode node;
		DelimitedString dataParts;
		int ien;
		switch (nodeParts.getPartOrEmpty(4)) {
		case "":
			break;
		case "-1)":
			dataParts = new DelimitedString(dataLine, "\\^", true);
			component = KernelComponentFactory.createKernelComponent(nodeParts
					.getPartOrEmpty(2));
			ien = Integer.parseInt(nodeParts.getPartOrEmpty(3));		// TODO Is IEN in KIDS unique?
			component.setIen(ien);
			component.setAction(dataParts.getPartOrEmpty(1));
			components.add(component);
			break;
		case "0)":
			dataParts = new DelimitedString(dataLine, "\\^", true);
			ien = Integer.parseInt(nodeParts.getPartOrEmpty(3));
			component = getKernelComponent(ien);
			component.setName(dataParts.getPartOrEmpty(1));
			node = createGlobalNode(nodeParts, dataLine, 4);
			component.getBody().add(node);
			break;
		default:
			component = getKernelComponent(Integer.parseInt(nodeParts
					.getPartOrEmpty(3)));
			node = createGlobalNode(nodeParts, dataLine, 4);
			component.getBody().add(node);
			break;
		}
	}
}
