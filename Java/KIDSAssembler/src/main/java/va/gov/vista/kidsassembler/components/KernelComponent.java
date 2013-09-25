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

public abstract class KernelComponent extends KidsComponent {
	protected String action;
	protected int ien;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof KernelComponent))
			return false;
		KernelComponent other = (KernelComponent) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (ien != other.ien)
			return false;
		return true;
	}

	public String getAction() {
		return action;
	}

	public int getIen() {
		return ien;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ien;
		return result;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setIen(int ien) {
		this.ien = ien;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [action=" + action + ", ien="
				+ ien + ", name=" + name + "]";
	}

	@Override
	public int compareTo(KidsComponent o) {
		if (!(o instanceof KernelComponent))
			return super.compareTo(o);
		KernelComponent other = (KernelComponent) o;
		Class<? extends KidsComponent> class1 = this.getClass();
		Class<? extends KidsComponent> class2 = o.getClass();
		if (class1 != class2)
			return super.compareTo(o);
		return Integer.compare(ien,other.ien);
	}
}
