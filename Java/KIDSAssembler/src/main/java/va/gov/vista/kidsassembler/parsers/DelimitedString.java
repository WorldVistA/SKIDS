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

public class DelimitedString {

	protected final String original;
	protected final String regex;
	protected final String[] parts;
	protected final boolean isOneBasedIndex;

	public DelimitedString(String original, String regex) {
		this(original, regex, false);
	}

	public DelimitedString(String original, String regex,
			boolean isOneBasedIndex) {
		this.original = original;
		this.regex = regex;
		this.parts = original.split(regex);
		this.isOneBasedIndex = isOneBasedIndex;
	}

	public String getOriginal() {
		return original;
	}
	
	public int getNumParts(){
		return parts.length;
	}

	public String getPart(int index) {
		if (isOneBasedIndex) {
			index--;
		}
		return parts[index];
	}

	public String getPartOrEmpty(int index) {
		if (isOneBasedIndex) {
			index--;
		}
		if (parts.length > index)
			return parts[index];
		return "";
	}

	public String getPartOrNull(int index) {
		if (isOneBasedIndex) {
			index--;
		}
		if (parts.length > index)
			return parts[index];
		return null;
	}

	public String getParts(int indexFrom, int indexTo) {
		if (isOneBasedIndex) {
			indexFrom--;
			indexTo--;
		}
		
		String result = "";
		
		if (parts.length > indexFrom)
			result = parts[indexFrom];
		

		for (int i=indexFrom + 1; (i < parts.length) && i <= indexTo; i++) 
		{
			result += regex + parts[i];
		}
				
		return result;
	}

	public String[] getParts() {
		return parts;
	}
	
	public String getRegex() {
		return regex;
	}

	public boolean isOneBasedIndex() {
		return isOneBasedIndex;
	}

	@Override
	public String toString() {
		return original;
	}
}
