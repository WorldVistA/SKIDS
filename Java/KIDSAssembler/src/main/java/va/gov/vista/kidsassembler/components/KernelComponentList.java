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

public class KernelComponentList<T extends KernelComponent> extends
		KidsComponentList<T> {
	private static final long serialVersionUID = -6885399726365201511L;
	protected final float fileNumber;
	protected final int specialInstructionIndex;

	public KernelComponentList(String name, float fileNumber,
			int specialInstructionPosition) {
		super(name);
		this.fileNumber = fileNumber;
		this.specialInstructionIndex = specialInstructionPosition;
	}

	@Override
	public int compareTo(KidsComponentList<? extends KidsComponent> o) {
		if (o instanceof KernelComponentList<?>) {
			KernelComponentList<?> other = (KernelComponentList<?>) o;
			return Float.compare(this.getFileNumber(), other.getFileNumber());
		}
		return super.compareTo(o);
	}

	public float getFileNumber() {
		return fileNumber;
	}

	public String getFileNumberString() {
		String result = Float.toString(fileNumber);
		if (result.startsWith("0"))
			result = result.substring(1);
		else if (result.endsWith(".0"))
			result = result.substring(0, result.length() - 2);
		return result;
	}

	public int getSpecialInstructionIndex() {
		return specialInstructionIndex;
	}
}
