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

import java.util.LinkedList;

public class KidsComponentList<T extends KidsComponent> extends LinkedList<T>
		implements Comparable<KidsComponentList<? extends KidsComponent>> {
	private static final long serialVersionUID = 7557083867373811816L;
	protected final String name;

	public KidsComponentList(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(KidsComponentList<? extends KidsComponent> o) {
		if (this == o)
			return 0;
		if (o == null)
			return 1;
		return this.name.compareTo(o.name);
	}

	public String getName() {
		return name;
	}
}
