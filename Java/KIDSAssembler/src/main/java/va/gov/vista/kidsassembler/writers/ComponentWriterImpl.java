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
import java.io.IOException;

import va.gov.vista.kidsassembler.components.GlobalNode;
import va.gov.vista.kidsassembler.components.KernelComponent;
import va.gov.vista.kidsassembler.components.KernelComponentList;
import va.gov.vista.kidsassembler.components.KidsComponent;

public class ComponentWriterImpl implements ComponentWriter {

	protected int buildIen;
	
	public int getBuildIen() {
		return buildIen;
	}

	public void setBuildIen(int buildIen) {
		this.buildIen = buildIen;
	}

	@Override
	public void writeBody(KernelComponentList<? extends KidsComponent> collection,
			BufferedWriter writer) throws IOException {
	
		String nodeKRN = "\"KRN\"," +  collection.getFileNumberString() + ",";
		String node = null;
		
		KernelComponent component;
		
		for (int i=1; i <= collection.size(); i++) {
			
			component = collection.get(i-1);
			
			node = nodeKRN + Integer.toString(component.getIen()) + ",";
				
			writer.write(node + "-1)");		// "KRN",8994,123457,-1)
			writer.newLine();
				
			writer.write(component.getAction() + "^" + Integer.toString(i));	// 0^1
			writer.newLine();
						
			for (GlobalNode globalNode : component.getBody()) {
				writer.write(node +  globalNode.getKey());
				writer.newLine();
				
				writer.write(globalNode.getData());
				writer.newLine();
			}
			
		}
	}
	
	@Override
	public void writeHeader(KernelComponentList<? extends KidsComponent> collection,
			BufferedWriter writer) throws IOException {
	
		String node ="\"BLD\"," + Integer.toString(this.buildIen) + ",";
		
		node = node +  "\"KRN\"," +  collection.getFileNumberString() + ",0)";
		
		writer.write(node);									// "BLD",3463,"KRN",.4,0)
		writer.newLine();
	
		writer.write(collection.getFileNumberString());
		writer.newLine();
		
	}
	
	@Override
	public void writeHeaderList(KernelComponentList<? extends KidsComponent> collection,
			BufferedWriter writer) throws IOException {
	
		if (collection.size() < 1 ) return;		// Exit if there is nothing to write

		String count = Integer.toString(collection.size());		// Total elements

		String node ="\"BLD\"," + Integer.toString(this.buildIen) + ",";
		
		node = node +  "\"KRN\"," +  collection.getFileNumberString() + ",";
			
		node = node +  "\"NM\",";
		
		writer.write(node + "0)");								// "BLD",3463,"KRN",.4,"NM",0)
		writer.newLine();

		writer.write("^9.68A^" + count + "^" + count);			// ^9.68A^4^4
		writer.newLine();
		
		for (int i=1; i <= collection.size(); i++) {
			writer.write(node + Integer.toString(i) + ",0)"); 		// "BLD",3463,"KRN",.4,"NM",1,0) 
			writer.newLine();
			
			writer.write(collection.get(i-1).getName() + "^^0");	// "MAGV-PAT-QUERY^^0
			writer.newLine();
		}
	}
		
	
	@Override
	public void writeBCrossReference(KernelComponentList<? extends KidsComponent> collection,
			BufferedWriter writer) throws IOException {
		
		String node ="\"BLD\"," + Integer.toString(this.buildIen) + ",";
		
		node = node +  "\"KRN\"," +  collection.getFileNumberString() + ",";

	
		node = node +  "\"NM\",\"B\",";

		// Write the "B" Cross-reference
				for (int i=1; i <= collection.size(); i++) {
					writer.write(node + "\"" + collection.get(i-1).getName() + "\"," + Integer.toString(i) + ")"); 		// "BLD",3463,"KRN",9.8,"NM","B","MAGGTU4D",1)
					writer.newLine();
					
					writer.newLine();
				}
	}
	
}
