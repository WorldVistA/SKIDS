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

import java.util.ArrayList;
import java.util.List;

public class DataDictionary extends KidsComponent {
	protected final float fileNumber;
	protected final List<Index> indeces;
	protected final List<Key> keys;
	protected final List<KeyPtr> keyPtrs;
	protected final List<GlobalNode> fiaBody;
	protected final List<GlobalNode> dataBody;
	protected final List<GlobalNode> definitionBody;
	protected final List<GlobalNode> partialDefinitionBody;
	protected final List<GlobalNode> bCrossReferenceBody;
	protected final List<GlobalNode> dicBody;
	protected final List<GlobalNode> secBody;
	protected final List<GlobalNode> frv1Body;
	protected final List<GlobalNode> upBody;
	protected final List<GlobalNode> pglBody;

	public DataDictionary(float fileNumber, String name) {
		this.definitionBody = new ArrayList<GlobalNode>();
		this.dicBody = new ArrayList<GlobalNode>();
		this.secBody = new ArrayList<GlobalNode>();
		this.partialDefinitionBody = new ArrayList<GlobalNode>();
		this.bCrossReferenceBody = new ArrayList<GlobalNode>();
		this.indeces = new ArrayList<Index>();
		this.keys = new ArrayList<Key>();
		this.keyPtrs = new ArrayList<KeyPtr>();
		this.dataBody = new ArrayList<GlobalNode>();
		this.fiaBody = new ArrayList<GlobalNode>();
		this.frv1Body = new ArrayList<GlobalNode>();
		this.upBody = new ArrayList<GlobalNode>();
		this.pglBody = new ArrayList<GlobalNode>();
		this.fileNumber = fileNumber;
		this.name = name;
	}

	@Override
	public int compareTo(KidsComponent o) {
		if (!(o instanceof DataDictionary))
			return super.compareTo(o);
		DataDictionary other = (DataDictionary) o;
		return Integer.compare(Float.floatToIntBits(fileNumber),
				Float.floatToIntBits(other.fileNumber));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof DataDictionary))
			return false;
		DataDictionary other = (DataDictionary) obj;
		if (Float.floatToIntBits(fileNumber) != Float
				.floatToIntBits(other.fileNumber))
			return false;
		return true;
	}

	public List<GlobalNode> getDefinitionBody() {
		return definitionBody;
	}
	
	public List<GlobalNode> getDicBody() {
		return dicBody;
	}
	
	public List<GlobalNode> getSecBody() {
		return secBody;
	}
	
	public List<GlobalNode> getFrv1Body() {
		return frv1Body;
	}
	
	public List<GlobalNode> getUpBody() {
		return upBody;
	}

	public List<GlobalNode> getPglBody() {
		return pglBody;
	}

	public List<GlobalNode> getPartialDefinitionBody() {
		return partialDefinitionBody;
	}
	
	public List<GlobalNode> getBCrossReferenceBody() {
		return bCrossReferenceBody;
	}
	
	public List<GlobalNode> getDataBody() {
		return dataBody;
	}
	
	public List<GlobalNode> getFiaBody() {
		return fiaBody;
	}

	public float getFileNumber() {
		return fileNumber;
	}

	public List<Index> getIndeces() {
		return indeces;
	}

	public List<Key> getKeys() {
		return keys;
	}

	public List<KeyPtr> getKeyPtrs() {
		return keyPtrs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(fileNumber);
		return result;
	}

	@Override
	public String toString() {
		return "DataDictionary [fileNumber=" + fileNumber + ", name=" + name
				+ "]";
	}
}
