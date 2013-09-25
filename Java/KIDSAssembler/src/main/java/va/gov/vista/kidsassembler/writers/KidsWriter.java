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
package va.gov.vista.kidsassembler.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import va.gov.vista.kidsassembler.KidsPackage;
import va.gov.vista.kidsassembler.components.*;
import va.gov.vista.kidsassembler.writers.StringUtils;

public class KidsWriter {
	private static final Logger logger = Logger.getLogger(KidsWriter.class);
	private String strBLDIEN;
	protected final KidsPackage kidsPackage;

	public KidsWriter(KidsPackage kidsPackage) {
		this.kidsPackage = kidsPackage;
		this.strBLDIEN = "\"BLD\","
				+ Integer.toString(kidsPackage.getBuildIEN()) + ",";
		this.kidsPackage.updateBuildLabel();
	}

	public String getStrBLDIen() {
		return strBLDIEN;
	}

	private void writeFooter(BufferedWriter writer) throws IOException {
		writer.write("**END**");
		writer.newLine();
		writer.write("**END**");
		writer.newLine();
	}

	private String getListTotals(String File, int count) {
		// e.g., ^9.611^5^5

		return "^" + File + "^" + Integer.toString(count) + "^"
				+ Integer.toString(count);
	}

	private void writeFileHeader(BufferedWriter writer) throws IOException {
		// e.g.,
		//
		// KIDS Distribution saved on Nov 06, 2012@08:53:15
		// VistA Imaging V3.0 - Patch 118 - Test 33 11/06/2012 08:53AM

		Date date = new Date();
		SimpleDateFormat commentDateFormat = new SimpleDateFormat(
				"MMM dd, yyyy@HH:mm:ss");
		SimpleDateFormat commentDateFormat2 = new SimpleDateFormat(
				"MM/dd/yyyy hh:mma");

		writer.write("KIDS Distribution saved on ");
		writer.write(commentDateFormat.format(date));
		writer.newLine();
		writer.write(this.kidsPackage.getPatchName());
		writer.write(" - ");
		if (kidsPackage.getTestVersion() > 0) {
			writer.write("Test " + kidsPackage.getTestVersion() + " ");
		}
		writer.write(commentDateFormat2.format(date));
		writer.newLine();
	}

	// write the KIDS out
	public void writeKids(Path outputFilePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				outputFilePath.toFile()))) {

			writeFileHeader(writer); // writes first two lines

			writeKIDSHeader(writer); // **KIDS**:

			// TODO do we need to handle multiple installs (patches) in one KIDS

			writeInstallHeader(writer); // **INSTALL NAME**

			writeDescription(writer); // "BLD",3463,1

			writeDataDictionary4Header(writer); // "BLD",3463,4,0

			writeDataDictionary4(writer); // "BLD",3463,4

			writeDataDictionary4PartialDefinition(writer); // "BLD",3463,4,"APDD"

			writeDataDictionary4BCrossReference(writer); // "BLD",3463,4,"B"

			writeTransportBuildNumber(writer); // "BLD",3463,6.3

			writePackageNamespaceOrPrefix(writer); // "BLD",3463,"ABNS"

			writeABPKG(writer); // "BLD",3463,"ABPKG"

			writePreInstallRoutine(writer); // "BLD",3463,"INI"

			writeDeleteRoutineFlags(writer); // "BLD",3463,"INID"

			writePostInstallRoutine(writer); // "BLD",3463,"INIT"

			writeKernelComponentsHeader(writer); // "BLD",3463,"KRN",0)

			writeKernelComponents(writer); // "BLD",3463,"KRN",

			writeKernelComponentsBCrossReference(writer); // "BLD",3463,"KRN","B",.4,.4)

			writeEnvCheckRoutine(writer); // "BLD",12345,"PRE"

			writeRequiredBuilds(writer); // "BLD",12345,"REQB"

			writeDataDictionaryDATA(writer); // "DATA"

			writeDataDictionaryFileAtributes(writer); // "FIA"

			writeFileManFRV1(writer); // "FRV1"

			writeRootPreInstallRoutine(writer); // "INI"

			writeRootPostInstallRoutine(writer); // "INIT"

			writeFileManNewIndecesIX(writer); // "IX"

			writeFileManKEY(writer); // "KEY"

			writeFileManKeyPtr(writer); // "KEYPTR"

			writeKernelComponentsBody(writer); // "KRN"

			writeMBREQ(writer); // "MBREQ"

			writerKernelSpecialInstructions(writer); // "ORD"

			writeFileManPointerToGlobal(writer); // "PGL"

			writePackageHistory(writer); // "PKG"

			writeRootEnvCheckRoutine(writer); // "PRE"

			writeQuestions(writer); // "QUES"

			writeRoutinesBody(writer); // "RTN"

			writeDataDictionariesSecurity(writer); // "SEC"

			writeFileManVersion(writer); // "VER"

			writeFileManUpLink(writer); // "UP"

			writeDataDictionaries(writer); // "^DD", and all DDs fields

			writeDataDictionariesDic(writer); // "^DIC", and all DDs fields

			writeFooter(writer); // **END**

			writer.flush();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	private void writeKernelComponents(BufferedWriter writer)
			throws IOException {
		// writes Kernel components
		for (KernelComponentList<? extends KidsComponent> collection : kidsPackage
				.getKernelComponentCollections()) {

			ComponentWriter componentWriter = ComponentWriterFactory
					.CreateWriter(collection, this.kidsPackage.getBuildIEN());

			if (componentWriter == null)
				continue;

			componentWriter.writeHeader(collection, writer);

			componentWriter.writeHeaderList(collection, writer);

			componentWriter.writeBCrossReference(collection, writer);
		}

	}

	private void writeTransportBuildNumber(BufferedWriter writer)
			throws IOException {
		// "BLD",3463,6.3)
		// V3.0p127Build101_T5

		writer.write(this.strBLDIEN + "6.3)");
		writer.newLine();
		writer.write(this.kidsPackage.getBuildLabel());
		writer.newLine();

	}

	private void writePackageHistory(BufferedWriter writer) throws IOException {
		// "PKG",454,-1)
		// 1^1
		// "PKG",454,0)
		// IMAGING^MAG^Imaging Release History
		String pkg = "\"PKG\","
				+ Integer.toString(this.kidsPackage.getPackageNumber()) + ",";
		Date date = new Date();

		writer.write(pkg + "-1)");
		writer.newLine();
		writer.write("1^1"); // TODO single package for now
		writer.newLine();
		writer.write(pkg + "0)");
		writer.newLine();
		// IMAGING^MAG^Imaging Release History
		writer.write(this.kidsPackage.getPackageName() + "^"
				+ this.kidsPackage.getPackagePrefix() + "^"
				+ this.kidsPackage.getPackageShortDescription());
		writer.newLine();

		writer.write(pkg + "22,0)");
		writer.newLine();

		writer.write("^9.49I^1^1");
		writer.newLine();

		writer.write(pkg + "22,1,0)");
		writer.newLine();

		// "3.0^3020328^3020328^.5"
		writer.write(this.kidsPackage.getPackageVersion() + "^"
				+ this.kidsPackage.getPackageDateDistributed() + "^"
				+ this.kidsPackage.getPackageDateInstalled() + "^"
				+ this.kidsPackage.getPackageInstalledBy());
		writer.newLine();

		writer.write(pkg + "22,1,\"PAH\",1,0)");
		writer.newLine();

		writer.write(Integer.toString(this.kidsPackage.getPatch()) + "^"
				+ StringUtils.getFileManDate(date) + "^"
				+ this.kidsPackage.getPackageInstalledBy());
		writer.newLine();

		ArrayList<String> description = this.getPKGDescription();

		String root = pkg + "22,1,\"PAH\",1,1"; // "PKG",454,22,1,"PAH",1,1
		String fileNumber = "9.49011";

		this.writeWordProcessingField(writer, root, fileNumber, description);

	}

	private void writePackageNamespaceOrPrefix(BufferedWriter writer)
			throws IOException {
		// TODO ABNS write
		// "BLD",3463,"ABNS",0)
		// ^9.66A^^

		String abns = this.strBLDIEN + "\"ABNS\",";
		writer.write(abns + "0)");
		writer.newLine();
		writer.write("^9.66A^" + "" + "^" + "");
		writer.newLine();
		// int count = this.kidsPackage.getNamespaceCollection().size();
		// String countText = count > 0 ? Integer.toString(count) : "";
		// writer.write("^9.66A^" + countText + "^" + countText);
		// writer.newLine();
		// for (int i=1; i <= count; count++) {
		//
		// writer.write(abns + i + ",0)");
		// writer.newLine();
		// writer.write(this.kidsPackage.getNamespaceCollection().)
		// }
	}

	private void writeABPKG(BufferedWriter writer) throws IOException {
		// "BLD",3463,"ABPKG")
		// y^y^G.IMAGING DEVELOPMENT TEAM@FORUM.VA.GOV

		writer.write(this.strBLDIEN + "\"ABPKG\")");
		writer.newLine();

		writer.write(this.kidsPackage.getAlphaBetaTesting() == null ? "n"
				: this.kidsPackage.getAlphaBetaTesting());
		writer.write("^");
		writer.write(this.kidsPackage.getInstallationMessage() == null ? "n"
				: this.kidsPackage.getInstallationMessage());
		writer.write("^");
		writer.write(this.kidsPackage.getAddressForUsageReporting() == null ? ""
				: this.kidsPackage.getAddressForUsageReporting());
		writer.newLine();

	}

	private void writerKernelSpecialInstructions(BufferedWriter writer)
			throws IOException {

		String fileNumber;

		for (KernelComponentList<? extends KidsComponent> collection : kidsPackage
				.getAllSpecialInstructionCollections()) {

			if (collection == null || collection.size() < 1)
				continue; // Zero elements. Get next component

			fileNumber = collection.getFileNumberString();

			switch (fileNumber) {
			case "9.2":
				writer.write("\"ORD\",1,9.2)");
				writer.newLine();
				writer.write("9.2;1;;;HELP^XPDTA1;HLPF1^XPDIA1;HLPE1^XPDIA1;HLPF2^XPDIA1;;HLPDEL^XPDIA1");
				writer.newLine();
				writer.write("\"ORD\",1,9.2,0)");
				writer.newLine();
				writer.write("HELP FRAME");
				writer.newLine();
				break;
			case "3.6":
				writer.write("\"ORD\",2,3.6)");
				writer.newLine();
				writer.write("3.6;2;1;;BUL^XPDTA1;;BULE1^XPDIA1;;;BULDEL^XPDIA1");
				writer.newLine();
				writer.write("\"ORD\",2,3.6,0)");
				writer.newLine();
				writer.write("BULLETIN");
				writer.newLine();
				break;
			case "19.1":
				writer.write("\"ORD\",3,19.1)");
				writer.newLine();
				writer.write("19.1;3;1;;KEY^XPDTA1;;;KEYF2^XPDIA1;;KEYDEL^XPDIA1");
				writer.newLine();
				writer.write("\"ORD\",3,19.1,0)");
				writer.newLine();
				writer.write("SECURITY KEY");
				writer.newLine();
				break;
			case ".5":
				writer.write("\"ORD\",4,.5)");
				writer.newLine();
				writer.write(".5;4;;;EDEOUT^DIFROMSO(.5,DA,\"\",XPDA);FPRE^DIFROMSI(.5,\"\",XPDA);EPRE^DIFROMSI(.5,DA,\"\",XPDA,\"\",OLDA);;EPOST^DIFROMSI(.5,DA,\"\",XPDA)");
				writer.newLine();
				writer.write("\"ORD\",4,.5,0)");
				writer.newLine();
				writer.write("FUNCTION");
				writer.newLine();
				break;
			case ".4":
				writer.write("\"ORD\",5,.4)");
				writer.newLine();
				writer.write(".4;5;;;EDEOUT^DIFROMSO(.4,DA,\"\",XPDA);FPRE^DIFROMSI(.4,\"\",XPDA);EPRE^DIFROMSI(.4,DA,$E(\"N\",$G(XPDNEW)),XPDA,\"\",OLDA);;EPOST^DIFROMSI(.4,DA,\"\",XPDA);DEL^DIFROMSK(.4,\"\",%)");
				writer.newLine();
				writer.write("\"ORD\",5,.4,0)");
				writer.newLine();
				writer.write("PRINT TEMPLATE");
				writer.newLine();
				break;
			case ".401":
				writer.write("\"ORD\",6,.401)");
				writer.newLine();
				writer.write(".401;6;;;EDEOUT^DIFROMSO(.401,DA,\"\",XPDA);FPRE^DIFROMSI(.401,\"\",XPDA);EPRE^DIFROMSI(.401,DA,$E(\"N\",$G(XPDNEW)),XPDA,\"\",OLDA);;EPOST^DIFROMSI(.401,DA,\"\",XPDA);DEL^DIFROMSK(.401,\"\",%)");
				writer.newLine();
				writer.write("\"ORD\",6,.401,0)");
				writer.newLine();
				writer.write("SORT TEMPLATE");
				writer.newLine();
				break;
			case ".402":
				writer.write("\"ORD\",7,.402)");
				writer.newLine();
				writer.write(".402;7;;;EDEOUT^DIFROMSO(.402,DA,\"\",XPDA);FPRE^DIFROMSI(.402,\"\",XPDA);EPRE^DIFROMSI(.402,DA,$E(\"N\",$G(XPDNEW)),XPDA,\"\",OLDA);;EPOST^DIFROMSI(.402,DA,\"\",XPDA);DEL^DIFROMSK(.402,\"\",%)");
				writer.newLine();
				writer.write("\"ORD\",7,.402,0)");
				writer.newLine();
				writer.write("INPUT TEMPLATE");
				writer.newLine();
				break;
			case ".403":
				writer.write("\"ORD\",8,.403)");
				writer.newLine();
				writer.write(".403;8;;;EDEOUT^DIFROMSO(.403,DA,\"\",XPDA);FPRE^DIFROMSI(.403,\"\",XPDA);EPRE^DIFROMSI(.403,DA,$E(\"N\",$G(XPDNEW)),XPDA,\"\",OLDA);;EPOST^DIFROMSI(.403,DA,\"\",XPDA);DEL^DIFROMSK(.403,\"\",%)");
				writer.newLine();
				writer.write("\"ORD\",8,.403,0)");
				writer.newLine();
				writer.write("FORM");
				writer.newLine();
				break;
			case ".84":
				writer.write("\"ORD\",9,.84)");
				writer.newLine();
				writer.write(".84;9;;;EDEOUT^DIFROMSO(.84,DA,\"\",XPDA);FPRE^DIFROMSI(.84,\"\",XPDA);EPRE^DIFROMSI(.84,DA,\"\",XPDA,\"\",OLDA);;EPOST^DIFROMSI(.84,DA,\"\",XPDA);DEL^DIFROMSK(.84,\"\",%)");
				writer.newLine();
				writer.write("\"ORD\",9,.84,0)");
				writer.newLine();
				writer.write("DIALOG");
				writer.newLine();
				break;
			case ".3.8":
				writer.write("\"ORD\",11,3.8)");
				writer.newLine();
				writer.write("3.8;11;;;MAILG^XPDTA1;MAILGF1^XPDIA1;MAILGE1^XPDIA1;MAILGF2^XPDIA1;;MAILGDEL^XPDIA1(%)");
				writer.newLine();
				writer.write("\"ORD\",11,3.8,0)");
				writer.newLine();
				writer.write("MAIL GROUP");
				writer.newLine();
				break;
			case "870":
				writer.write("\"ORD\",13,870)");
				writer.newLine();
				writer.write("870;13;1;;HLLL^XPDTA1;;HLLLE^XPDIA1;;;HLLLDEL^XPDIA1(%)");
				writer.newLine();
				writer.write("\"ORD\",13,870,0)");
				writer.newLine();
				writer.write("HL LOGICAL LINK");
				writer.newLine();
				break;
			case "771":
				writer.write("\"ORD\",14,771)");
				writer.newLine();
				writer.write("771;14;;;HLAP^XPDTA1;HLAPF1^XPDIA1;HLAPE1^XPDIA1;HLAPF2^XPDIA1;;HLAPDEL^XPDIA1(%)");
				writer.newLine();
				writer.write("\"ORD\",14,771,0)");
				writer.newLine();
				writer.write("HL7 APPLICATION PARAMETER");
				writer.newLine();
				break;
			case "101":
				writer.write("\"ORD\",15,101)");
				writer.newLine();
				writer.write("101;15;;;PRO^XPDTA;PROF1^XPDIA;PROE1^XPDIA;PROF2^XPDIA;;PRODEL^XPDIA");
				writer.newLine();
				writer.write("\"ORD\",15,101,0)");
				writer.newLine();
				writer.write("PROTOCOL");
				writer.newLine();
				break;
			case "8994":
				writer.write("\"ORD\",16,8994)");
				writer.newLine();
				writer.write("8994;16;1;;;;;;;RPCDEL^XPDIA1");
				writer.newLine();
				writer.write("\"ORD\",16,8994,0)");
				writer.newLine();
				writer.write("REMOTE PROCEDURE");
				writer.newLine();
				break;
			case "409.61":
				writer.write("\"ORD\",17,409.61)");
				writer.newLine();
				writer.write("409.61;17;1;;;;;;;LMDEL^XPDIA1");
				writer.newLine();
				writer.write("\"ORD\",17,409.61,0)");
				writer.newLine();
				writer.write("LIST TEMPLATE");
				writer.newLine();
				break;
			case "19":
				writer.write("\"ORD\",18,19)");
				writer.newLine();
				writer.write("19;18;;;OPT^XPDTA;OPTF1^XPDIA;OPTE1^XPDIA;OPTF2^XPDIA;;OPTDEL^XPDIA");
				writer.newLine();
				writer.write("\"ORD\",18,19,0)");
				writer.newLine();
				writer.write("OPTION");
				writer.newLine();
				break;
			case "8994.2":
				// not implemented yet
				writer.write("\"ORD\",19,8994.2)");
				writer.newLine();
				writer.write("8994.2;19;1;;;;CRC32PE^XPDIA1;;;CRC32DEL^XPDIA1");
				writer.newLine();
				writer.write("\"ORD\",19,8994.2,0)");
				writer.newLine();
				writer.write("Kernel RPCs CRC32");
				writer.newLine();
				break;
			case "8989.51":
				writer.write("\"ORD\",20,8989.51)");
				writer.newLine();
				writer.write("8989.51;20;;;PAR1E1^XPDTA2;PAR1F1^XPDIA3;PAR1E1^XPDIA3;PAR1F2^XPDIA3;;PAR1DEL^XPDIA3(%)");
				writer.newLine();
				writer.write("\"ORD\",20,8989.51,0)");
				writer.newLine();
				writer.write("PARAMETER DEFINITION");
				writer.newLine();
				break;
			case "8989.52":
				writer.write("\"ORD\",21,8989.52)");
				writer.newLine();
				writer.write("8989.52;21;1;;PAR2E1^XPDTA2;PAR2F1^XPDIA3;PAR2E1^XPDIA3;PAR2F2^XPDIA3;;PAR2DEL^XPDIA3(%)");
				writer.newLine();
				writer.write("\"ORD\",21,8989.52,0)");
				writer.newLine();
				writer.write("PARAMETER TEMPLATE");
				writer.newLine();
				break;
			}
		}
	}

	private void writeRoutinesBody(BufferedWriter writer) throws IOException {

		ComponentWriter routineComponentWriter = null;

		KernelComponentList<Routine> routineCollection = kidsPackage
				.getRoutineCollection();

		routineComponentWriter = ComponentWriterFactory.CreateWriter(
				routineCollection, this.kidsPackage.getBuildIEN());

		if (routineComponentWriter != null) {

			((RoutineWriter) routineComponentWriter).writeBody(
					routineCollection, writer);
		}

	}

	private void writeKernelComponentsBody(BufferedWriter writer)
			throws IOException {

		for (KernelComponentList<? extends KidsComponent> collection : kidsPackage
				.getKernelComponentCollections()) {

			if (collection.getFileNumber() == 9.8f) { // Skip routines. We will
														// print them after QUES
														// section
				continue;
			}

			ComponentWriter componentWriter = ComponentWriterFactory
					.CreateWriter(collection, this.kidsPackage.getBuildIEN());

			if (componentWriter == null)
				continue;

			componentWriter.writeBody(collection, writer);

		}

	}

	private void writeMBREQ(BufferedWriter writer) throws IOException {
		// "MBREQ")
		// 0
		writer.write("\"MBREQ\")");
		writer.newLine();

		writer.write("0");
		writer.newLine();
	}

	private void writeDataDictionaries(BufferedWriter writer)
			throws IOException {
		// "^DD", and all DDs fields

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getBody().size() - 1; i++) {
				node = collection.getBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}
	}

	private void writeDataDictionariesDic(BufferedWriter writer)
			throws IOException {
		// "^DIC" and all DDs fields

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getDicBody().size() - 1; i++) {
				node = collection.getDicBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}
	}

	private void writeDataDictionariesSecurity(BufferedWriter writer)
			throws IOException {
		// "SEC" and all DDs fields

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getSecBody().size() - 1; i++) {
				node = collection.getSecBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}
	}

	private void writeDataDictionary4Header(BufferedWriter writer)
			throws IOException {
		// "BLD",3463,4,0)
		// ^9.64PA^2006.9422^40

		String root = this.getStrBLDIen();

		int ddCount = kidsPackage.getDataDictionaryCollection().size();
		String lastFileNumber = "";
		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {
			lastFileNumber = Float.toString(collection.getFileNumber());
		}

		writer.write(root + "4,0)");
		writer.newLine();
		writer.write("^9.64PA^" + lastFileNumber + "^"
				+ Integer.toString(ddCount));
		writer.newLine();
	}

	private void writeDataDictionary4(BufferedWriter writer) throws IOException {
		// "BLD",3463,4,2005,0)
		// 2005
		// "BLD",3463,4,2005,2,0)
		// ^9.641^2005^1

		String root = this.getStrBLDIen();

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getDefinitionBody().size() - 1; i++) {
				node = collection.getDefinitionBody().get(i);
				writer.write(root + node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}

	}

	private void writeDataDictionary4PartialDefinition(BufferedWriter writer)
			throws IOException {
		// "BLD",3463,4,"APDD",2006.575,2006.575)
		//

		String root = this.getStrBLDIen();

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getPartialDefinitionBody().size() - 1; i++) {
				node = collection.getPartialDefinitionBody().get(i);
				writer.write(root + node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}

	}

	private void writeDataDictionary4BCrossReference(BufferedWriter writer)
			throws IOException {
		// "BLD",3463,4,"B",2006.575,2006.575)
		//

		String root = this.getStrBLDIen();

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getBCrossReferenceBody().size() - 1; i++) {
				node = collection.getBCrossReferenceBody().get(i);
				writer.write(root + node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}

	}

	private void writeDataDictionaryDATA(BufferedWriter writer)
			throws IOException {
		// "DATA",2006.927,1,0)
		// Async Storage Request Queue^S^1^5^1200^1200

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getDataBody().size() - 1; i++) {
				node = collection.getDataBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}

	}

	private void writeDataDictionaryFileAtributes(BufferedWriter writer)
			throws IOException {

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getFiaBody().size() - 1; i++) {
				node = collection.getFiaBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();

			}
		}
	}

	private void writeFileManFRV1(BufferedWriter writer) throws IOException {

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getFrv1Body().size() - 1; i++) {
				node = collection.getFrv1Body().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();

			}
		}
	}

	private void writeFileManUpLink(BufferedWriter writer) throws IOException {

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getUpBody().size() - 1; i++) {
				node = collection.getUpBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}
	}

	private void writeFileManPointerToGlobal(BufferedWriter writer)
			throws IOException {

		GlobalNode node;

		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			for (int i = 0; i <= collection.getPglBody().size() - 1; i++) {
				node = collection.getPglBody().get(i);
				writer.write(node.getKey());
				writer.newLine();
				writer.write(node.getData());
				writer.newLine();
			}
		}
	}

	private void writeFileManNewIndecesIX(BufferedWriter writer)
			throws IOException {
		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			Index index;
			GlobalNode node;

			for (int i = 0; i <= collection.getIndeces().size() - 1; i++) {
				index = collection.getIndeces().get(i);

				for (int j = 0; j <= index.getBody().size() - 1; j++) {
					node = index.getBody().get(j);
					writer.write(node.getKey());
					writer.newLine();
					writer.write(node.getData());
					writer.newLine();
				}
			}
		}
	}

	private void writeFileManKEY(BufferedWriter writer) throws IOException {
		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			Key key;
			GlobalNode node;

			for (int i = 0; i <= collection.getKeys().size() - 1; i++) {
				key = collection.getKeys().get(i);

				for (int j = 0; j <= key.getBody().size() - 1; j++) {
					node = key.getBody().get(j);
					writer.write(node.getKey());
					writer.newLine();
					writer.write(node.getData());
					writer.newLine();

				}
			}
		}
	}

	private void writeFileManKeyPtr(BufferedWriter writer) throws IOException {
		for (DataDictionary collection : kidsPackage
				.getDataDictionaryCollection()) {

			KeyPtr keyPtr;
			GlobalNode node;

			for (int i = 0; i <= collection.getKeyPtrs().size() - 1; i++) {
				keyPtr = collection.getKeyPtrs().get(i);

				for (int j = 0; j <= keyPtr.getBody().size() - 1; j++) {
					node = keyPtr.getBody().get(j);
					writer.write(node.getKey());
					writer.newLine();
					writer.write(node.getData());
					writer.newLine();
				}
			}
		}
	}

	private void writeKernelComponentsHeader(BufferedWriter writer)
			throws IOException {

		// e.g.,
		// "BLD",3463,"KRN",0)
		// ^9.67PA^8994^19

		writer.write(this.strBLDIEN + "\"KRN\"" + ",0)");
		writer.newLine();

		String lastKRNComponent = ""; // Last Kernel Component number e.g., 8994

		for (KernelComponentList<? extends KidsComponent> collection : kidsPackage
				.getKernelComponentCollections()) {
			lastKRNComponent = collection.getFileNumberString();
		}

		writer.write("^9.67PA^"
				+ lastKRNComponent
				+ "^"
				+ Integer.toString(kidsPackage.getKernelComponentCollections()
						.size()));
		writer.newLine();

	}

	private void writeKernelComponentsBCrossReference(BufferedWriter writer)
			throws IOException {

		// e.g.,
		// "BLD",3463,"KRN","B",.4,.4)
		//
		// "BLD",3463,"KRN","B",.401,.401)
		//

		String nodeB = this.strBLDIEN + "\"KRN\"" + ",\"B\","; // "BLD",3463,"KRN","B",
		String fileNumber;
		for (KernelComponentList<? extends KidsComponent> collection : kidsPackage
				.getKernelComponentCollections()) {

			fileNumber = collection.getFileNumberString();

			writer.write(nodeB + fileNumber + "," + fileNumber + ")"); // .4,.4)
			writer.newLine();
			writer.newLine();
		}
	}

	private void writeInstallHeader(BufferedWriter writer) throws IOException {
		// e.g., **INSTALL NAME**
		// MAG*3.0*118
		// "BLD",3463,0)
		// MAG*3.0*118^IMAGING^0^3121106^y --> Build 1.0^Package File
		// Link^Type^Date Distributed^Track Nationally

		Date date = new Date();

		writer.write("**INSTALL NAME**");
		writer.newLine();

		writer.write(this.kidsPackage.getName());
		writer.newLine();

		writer.write(this.strBLDIEN + "0)");
		writer.newLine();

		writer.write(this.kidsPackage.getName());
		writer.write("^");
		writer.write(this.kidsPackage.getPackageName());
		writer.write("^");
		writer.write("0"); // 0 - single package
		writer.write("^");
		writer.write(StringUtils.getFileManDate(date));
		writer.write("^");
		writer.write("y"); // y - track nationally
		writer.newLine();

	}

	private void writeKIDSHeader(BufferedWriter writer) throws IOException {
		// KIDS Header:
		// **KIDS**: (build 1^build 2^�^build n)
		// Build 1 is the primary build

		// TODO Need to add logic for multiple builds in one KIDS
		writer.write("**KIDS**:");
		writer.write(this.kidsPackage.getName() + "^");
		writer.newLine();

		writer.newLine();

	}

	private void writeDescription(BufferedWriter writer) throws IOException {

		ArrayList<String> description = new ArrayList<String>();
		Set<String> deletedRoutines = new HashSet<String>();

		description.add(this.kidsPackage.getPatchName());
		description.add(" ");
		description.add(" ");
		description.add("Routines:");

		// Add all routines with their checksums
		for (Routine routine : this.kidsPackage.getRoutineCollection()) {
			description.add(routine.getName() + "    new value = "
					+ routine.getCheckSum()); // e.g., MAGGTU4D new value =
												// 5300968
		}

		if (StringUtils.existsAndEquals(
				this.kidsPackage.getDeleteEnvCheckRoutine(), "Yes")) {
			if (this.kidsPackage.getEnvCheckRoutine().length() > 0) {

				String rtn = this.kidsPackage.getEnvCheckRoutine();
				int piece = rtn.indexOf("^") > 0 ? 2 : 1;
				deletedRoutines.add(StringUtils.MagPiece(
						this.kidsPackage.getEnvCheckRoutine(), "^", piece));

			}
		}

		if (StringUtils.existsAndEquals(
				this.kidsPackage.getDeletePreInstallRoutine(), "Yes")) {
			if (this.kidsPackage.getPreInstallRoutine().length() > 0) {

				String rtn = this.kidsPackage.getPreInstallRoutine();
				int piece = rtn.indexOf("^") > 0 ? 2 : 1;
				deletedRoutines.add(StringUtils.MagPiece(
						this.kidsPackage.getPreInstallRoutine(), "^", piece));

			}
		}

		if (StringUtils.existsAndEquals(
				this.kidsPackage.getDeletePostInstallRoutine(), "Yes")) {
			if (this.kidsPackage.getPostInstallRoutine().length() > 0) {

				String rtn = this.kidsPackage.getPostInstallRoutine();
				int piece = rtn.indexOf("^") > 0 ? 2 : 1;
				deletedRoutines.add(StringUtils.MagPiece(
						this.kidsPackage.getPostInstallRoutine(), "^", piece));

			}
		}

		for (String deletedRotine : deletedRoutines) {
			description.add(" ");
			description.add("Please note that routine " + deletedRotine
					+ " is deleted after the KIDS Build is");
			description.add("installed.");
		}

		String root = this.strBLDIEN + "1";
		String fileNumber = "";

		writeWordProcessingField(writer, root, fileNumber, description);

	}

	private void writeDeleteRoutineFlags(BufferedWriter writer)
			throws IOException {
		// e.g, "BLD",3463,"INID")
		// n^y^y

		// Node
		writer.write(this.strBLDIEN + "\"INID\")");
		writer.newLine();

		// Data
		writer.write(StringUtils.existsAndEquals(
				this.kidsPackage.getDeleteEnvCheckRoutine(), "Yes") ? "y" : "n");
		writer.write(StringUtils.existsAndEquals(
				this.kidsPackage.getDeletePostInstallRoutine(), "Yes") ? "^y"
				: "^n");
		writer.write(StringUtils.existsAndEquals(
				this.kidsPackage.getDeletePreInstallRoutine(), "Yes") ? "^y"
				: "^n");
		writer.newLine();

	}

	private void writeEnvCheckRoutine(BufferedWriter writer) throws IOException {
		// e.g., "BLD",3463,"PRE")
		// MAGIP118

		if (!StringUtils.isEmpty(this.kidsPackage.getEnvCheckRoutine())) {
			writer.write(this.strBLDIEN + "\"PRE\")");
			writer.newLine();

			writer.write(this.kidsPackage.getEnvCheckRoutine());
			writer.newLine();
		}
	}

	private void writePreInstallRoutine(BufferedWriter writer)
			throws IOException {
		// e.g., "BLD",3463,"INI")
		// PRE^MAGIP130

		if (!StringUtils.isEmpty(this.kidsPackage.getPreInstallRoutine())) {
			writer.write(this.strBLDIEN + "\"INI\")");
			writer.newLine();

			writer.write(this.kidsPackage.getPreInstallRoutine());
			writer.newLine();
		}
	}

	private void writePostInstallRoutine(BufferedWriter writer)
			throws IOException {
		// e.g., "BLD",3463,"INIT")
		// POS^MAGIP130

		if (!StringUtils.isEmpty(this.kidsPackage.getPostInstallRoutine())) {
			writer.write(this.strBLDIEN + "\"INIT\")");
			writer.newLine();

			writer.write(this.kidsPackage.getPostInstallRoutine());
			writer.newLine();
		}
	}

	private void writeRequiredBuilds(BufferedWriter writer) throws IOException {
		// e.g,
		// "BLD",3463,"REQB",0)
		// ^9.611^1^1
		// "BLD",3463,"REQB",1,0)
		// MAG*3.0*116^2
		// "BLD",3463,"REQB","B","MAG*3.0*116",1)

		String strNodeREQB = "\"REQB\",";
		String qt = "\"";

		// Node
		writer.write(this.strBLDIEN + strNodeREQB + "0)");
		writer.newLine();

		// Data
		writer.write(this.getListTotals("9.611", this.kidsPackage
				.getRequiredBuilds().size()));
		writer.newLine();

		for (int i = 0; i < this.kidsPackage.getRequiredBuilds().size(); i++) {
			// Node
			writer.write(this.strBLDIEN + strNodeREQB + Integer.toString(i + 1)
					+ ",0)");
			writer.newLine();

			// Data
			writer.write(this.kidsPackage.getRequiredBuilds().get(i).getName());
			writer.write("^");
			writer.write(this.kidsPackage.getRequiredBuilds().get(i)
					.getAction());
			writer.newLine();
		}

		// Write the "B" cross-reference e.g.,
		// "BLD",3463,"REQB","B","MAG*3.0*116",1)

		for (int i = 0; i < this.kidsPackage.getRequiredBuilds().size(); i++) {
			// Node
			writer.write(this.strBLDIEN + strNodeREQB + "\"B\",");
			writer.write(qt
					+ this.kidsPackage.getRequiredBuilds().get(i).getName()
					+ qt);
			writer.write("," + Integer.toString(i + 1) + ")");
			writer.newLine();

			// Data
			writer.newLine();

		}
	}

	private void writeWordProcessingField(BufferedWriter writer, String root,
			String fileNumber, ArrayList<String> lines) throws IOException {

		// Writes a Word-Processing Field
		// e.g.,
		// "BLD",3463,1,0)
		// ^^2^2^3121106^
		// "BLD",3463,1,1,0)
		// Version 3.0 Patch 118 - DICOM Importer II
		// "BLD",3463,1,2,0)

		String count = Integer.toString(lines.size());
		String date = StringUtils.getFileManDate(new Date());

		writer.write(root + ",0)");
		writer.newLine();
		writer.write("^" + fileNumber + "^" + count + "^" + count + "^" + date);
		writer.newLine();

		for (int i = 0; i < lines.size(); i++) {
			// Node
			writer.write(root + "," + Integer.toString(i + 1) + ",0)");
			writer.newLine();

			writer.write(lines.get(i));
			writer.newLine();
		}
	}

	private void writeQuestions(BufferedWriter writer) throws IOException {

		writer.write("\"QUES\",\"XPF1\",0)");
		writer.newLine();
		writer.write("Y");
		writer.newLine();
		writer.write("\"QUES\",\"XPF1\",\"??\")");
		writer.newLine();
		writer.write("^D REP^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPF1\",\"A\")");
		writer.newLine();
		writer.write("Shall I write over your |FLAG| File");
		writer.newLine();
		writer.write("\"QUES\",\"XPF1\",\"B\")");
		writer.newLine();
		writer.write("YES");
		writer.newLine();
		writer.write("\"QUES\",\"XPF1\",\"M\")");
		writer.newLine();
		writer.write("D XPF1^XPDIQ");
		writer.newLine();
		writer.write("\"QUES\",\"XPF2\",0)");
		writer.newLine();
		writer.write("Y");
		writer.newLine();
		writer.write("\"QUES\",\"XPF2\",\"??\")");
		writer.newLine();
		writer.write("^D DTA^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPF2\",\"A\")");
		writer.newLine();
		writer.write("Want my data |FLAG| yours");
		writer.newLine();
		writer.write("\"QUES\",\"XPF2\",\"B\")");
		writer.newLine();
		writer.write("YES");
		writer.newLine();
		writer.write("\"QUES\",\"XPF2\",\"M\")");
		writer.newLine();
		writer.write("D XPF2^XPDIQ");
		writer.newLine();
		writer.write("\"QUES\",\"XPI1\",0)");
		writer.newLine();
		writer.write("YO");
		writer.newLine();
		writer.write("\"QUES\",\"XPI1\",\"??\")");
		writer.newLine();
		writer.write("^D INHIBIT^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPI1\",\"A\")");
		writer.newLine();
		writer.write("Want KIDS to INHIBIT LOGONs during the install");
		writer.newLine();
		writer.write("\"QUES\",\"XPI1\",\"B\")");
		writer.newLine();
		writer.write("NO");
		writer.newLine();
		writer.write("\"QUES\",\"XPI1\",\"M\")");
		writer.newLine();
		writer.write("D XPI1^XPDIQ");
		writer.newLine();
		writer.write("\"QUES\",\"XPM1\",0)");
		writer.newLine();
		writer.write("PO^VA(200,:EM");
		writer.newLine();
		writer.write("\"QUES\",\"XPM1\",\"??\")");
		writer.newLine();
		writer.write("^D MG^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPM1\",\"A\")");
		writer.newLine();
		writer.write("Enter the Coordinator for Mail Group '|FLAG|'");
		writer.newLine();
		writer.write("\"QUES\",\"XPM1\",\"B\")");
		writer.newLine();
		writer.write("");
		writer.newLine();
		writer.write("\"QUES\",\"XPM1\",\"M\")");
		writer.newLine();
		writer.write("D XPM1^XPDIQ");
		writer.newLine();
		writer.write("\"QUES\",\"XPO1\",0)");
		writer.newLine();
		writer.write("Y");
		writer.newLine();
		writer.write("\"QUES\",\"XPO1\",\"??\")");
		writer.newLine();
		writer.write("^D MENU^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPO1\",\"A\")");
		writer.newLine();
		writer.write("Want KIDS to Rebuild Menu Trees Upon Completion of Install");
		writer.newLine();
		writer.write("\"QUES\",\"XPO1\",\"B\")");
		writer.newLine();
		writer.write("NO");
		writer.newLine();
		writer.write("\"QUES\",\"XPO1\",\"M\")");
		writer.newLine();
		writer.write("D XPO1^XPDIQ");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ1\",0)");
		writer.newLine();
		writer.write("Y");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ1\",\"??\")");
		writer.newLine();
		writer.write("^D OPT^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ1\",\"A\")");
		writer.newLine();
		writer.write("Want to DISABLE Scheduled Options, Menu Options, and Protocols");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ1\",\"B\")");
		writer.newLine();
		writer.write("NO");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ1\",\"M\")");
		writer.newLine();
		writer.write("D XPZ1^XPDIQ");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ2\",0)");
		writer.newLine();
		writer.write("Y");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ2\",\"??\")");
		writer.newLine();
		writer.write("^D RTN^XPDH");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ2\",\"A\")");
		writer.newLine();
		writer.write("Want to MOVE routines to other CPUs");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ2\",\"B\")");
		writer.newLine();
		writer.write("NO");
		writer.newLine();
		writer.write("\"QUES\",\"XPZ2\",\"M\")");
		writer.newLine();
		writer.write("D XPZ2^XPDIQ");
		writer.newLine();
	}

	private void writeFileManVersion(BufferedWriter writer) throws IOException {
		writer.write("\"VER\")");
		writer.newLine();
		writer.write("8.0^22.0");
		writer.newLine();
	}

	private ArrayList<String> getPKGDescription() {

		ArrayList<String> description = new ArrayList<String>();
		Set<String> deletedRoutines = new HashSet<String>();

		String string;

		string = "Routines for Patch " + this.kidsPackage.getPatch();
		if (this.kidsPackage.getTestVersion() > 0) {
			string += ", Test Build "
					+ Integer.toString(this.kidsPackage.getTestVersion()) + ".";
		}

		description.add(string);
		description.add(" ");

		description.add("Routines:");

		// Add all routines with their checksums
		for (Routine routine : this.kidsPackage.getRoutineCollection()) {
			description.add(routine.getName() + "    value = "
					+ routine.getOldCheckSum()); // e.g., MAGGTU4D value =
													// 5300968 Use old checksum
													// algorithm
		}

		if (StringUtils.existsAndEquals(
				this.kidsPackage.getDeleteEnvCheckRoutine(), "Yes")) {
			if (this.kidsPackage.getEnvCheckRoutine().length() > 0) {

				String rtn = this.kidsPackage.getEnvCheckRoutine();
				int piece = rtn.indexOf("^") > 0 ? 2 : 1;
				deletedRoutines.add(StringUtils.MagPiece(
						this.kidsPackage.getEnvCheckRoutine(), "^", piece));

			}
		}

		if (StringUtils.existsAndEquals(
				this.kidsPackage.getDeletePreInstallRoutine(), "Yes")) {
			if (this.kidsPackage.getPreInstallRoutine().length() > 0) {

				String rtn = this.kidsPackage.getPreInstallRoutine();
				int piece = rtn.indexOf("^") > 0 ? 2 : 1;
				deletedRoutines.add(StringUtils.MagPiece(
						this.kidsPackage.getPreInstallRoutine(), "^", piece));

			}
		}

		if (StringUtils.existsAndEquals(
				this.kidsPackage.getDeletePostInstallRoutine(), "Yes")) {
			if (this.kidsPackage.getPostInstallRoutine().length() > 0) {

				String rtn = this.kidsPackage.getPostInstallRoutine();
				int piece = rtn.indexOf("^") > 0 ? 2 : 1;
				deletedRoutines.add(StringUtils.MagPiece(
						this.kidsPackage.getPostInstallRoutine(), "^", piece));

			}
		}

		for (String deletedRotine : deletedRoutines) {
			description.add(" ");
			description.add("Please note that routine " + deletedRotine
					+ " is deleted after the KIDS Build is");
			description.add("installed.");
		}

		return description;
	}

	private void writeRootEnvCheckRoutine(BufferedWriter writer)
			throws IOException {
		// e.g., "PRE")
		// MAGIP118

		// TODO we may need to have a top level Env check routine
		if (!StringUtils.isEmpty(this.kidsPackage.getEnvCheckRoutine())) {
			writer.write("\"PRE\")");
			writer.newLine();

			writer.write(this.kidsPackage.getEnvCheckRoutine());
			writer.newLine();
		}
	}

	private void writeRootPreInstallRoutine(BufferedWriter writer)
			throws IOException {
		// e.g., "INI")
		// PRE^MAGIP130

		// TODO How does it work if we have to patches in a single transport?
		if (!StringUtils.isEmpty(this.kidsPackage.getPreInstallRoutine())) {
			writer.write("\"INI\")");
			writer.newLine();

			writer.write(this.kidsPackage.getPreInstallRoutine());
			writer.newLine();
		}
	}

	private void writeRootPostInstallRoutine(BufferedWriter writer)
			throws IOException {
		// e.g., "INIT")
		// POS^MAGIP130

		// TODO How does it work if we have to patches in a single transport?
		if (!StringUtils.isEmpty(this.kidsPackage.getPostInstallRoutine())) {
			writer.write("\"INIT\")");
			writer.newLine();

			writer.write(this.kidsPackage.getPostInstallRoutine());
			writer.newLine();
		}
	}

}
