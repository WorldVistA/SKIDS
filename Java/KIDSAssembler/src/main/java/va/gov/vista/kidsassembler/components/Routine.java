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

import org.apache.log4j.Logger;

public class Routine extends KernelComponent {
	private static final Logger logger = Logger.getLogger(KernelComponent.class);

	private String checkSum;		// New style Checkum
	
	private String oldCheckSum;		// Old Style Check Sum	
	
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	
	public void setCheckSum() {
		this.checkSum = calculateCheckSum();
	}
	

	public String getCheckSum() {
		if (this.checkSum == null) {
			this.setCheckSum();
		}
		return this.checkSum;
	}
	
	public void setOldCheckSum() {
		this.oldCheckSum = calculateOldCheckSum();
	}
	

	public String getOldCheckSum() {
		if (this.oldCheckSum == null) {
			this.setOldCheckSum();
		}
		return this.oldCheckSum;
	}
	
	private String calculateCheckSum() {
	
		//	^%ZOSF("RSUM1")="N %,%1,%3 ZL @X S Y=0 F %=1,3:1 S %1=$T(+%),%3=$F(%1,"" "") Q:'%3  S %3=$S($E(%1,%3)'="";"":$L(%1),$E(%1,%3+1)="";"":$L(%1),1:%3-2) F %2=1:1:%3 S Y=$A(%1,%2)*(%2+%)+Y"
		int result = 0;
		        
		String line;

		for (int i = 1; i <= this.getBody().size(); i++) {		// variable i is %
			if (i == 2) continue;		// Skip second line
		            
		    line = this.getBody().get(i-1).data;		// line is %
		            
		    int var3 = mFind(line, " ");		// var3 is %3
		    if (var3 == 0) {
		    	break;
		    }

		    if (mExtract(line, var3) != ';') {
		    	var3 = line.length();
		    } else if (mExtract(line, var3 + 1) == ';') {
		         var3 = line.length();
		    } else {
		         var3 = var3 - 2;
		    }

		    for (int j = 1; j <= var3; j++) {		// j is %2; i is % (line number)
		    	result += (((int) line.charAt(j-1)) * ( i + j));
		    }
		}

		return Integer.toString(result);
	}
	
	private String calculateOldCheckSum() {
		
		//	^%ZOSF("RSUM")="N %,%1,%3 ZL @X S Y=0 F %=1,3:1 S %1=$T(+%),%3=$F(%1,"" "") Q:'%3  S %3=$S($E(%1,%3)'="";"":$L(%1),$E(%1,%3+1)="";"":$L(%1),1:%3-2) F %2=1:1:%3S Y=$A(%1,%2)*%2+Y"
	        
		int result = 0;
        
		String line;

		for (int i = 1; i <= this.getBody().size(); i++) {		// variable i is %
			if (i == 2) continue;		// Skip second line
		            
		    line = this.getBody().get(i-1).data;		// line is %
		            
		    int var3 = mFind(line, " ");		// var3 is %3
		    if (var3 == 0) {
		    	break;
		    }

		    if (mExtract(line, var3) != ';') {
		    	var3 = line.length();
		    } else if (mExtract(line, var3 + 1) == ';') {
		         var3 = line.length();
		    } else {
		         var3 = var3 - 2;
		    }

		    for (int j = 1; j <= var3; j++) {		// j is %2; i is % (line number)
		    	result += (((int) line.charAt(j-1)) * j);
		    }
		}

		return Integer.toString(result);
	}
	
	
    /**
     * M $FIND
     * http://docs.intersystems.com/cache20121/csp/docbook/DocBook.UI.Page.cls?KEY=RCOS_ffind#RCOS_B55072
     *
     * @param string
     * @param substring
     * @return
     */
	protected int mFind(String string, String substring) {
        try {
            int f = string.indexOf(substring);
            return f == -1 ? 0 : f + 2;			// Next position. Offset 0-based string		
        } catch (NullPointerException e) {
            logger.error(e);
        }
        return 0;
    }

    /**
     * M $EXTRACT
     * http://docs.intersystems.com/cache20121/csp/docbook/DocBook.UI.Page.cls?KEY=RCOS_fextract
     *
     * @param string
     * @param position 1's based char array
     * @return
     */
    protected char mExtract(String string, int position) {
        try {
            char[] chars = string.toCharArray();
            return chars[--position];
        } catch (Exception e) {
            return (char) 0;
        }
    }

    /**
     * M $ASCII
     * http://docs.intersystems.com/cache20121/csp/docbook/DocBook.UI.Page.cls?KEY=RCOS_fascii#RCOS_B54341
     *
     * @param string
     * @param position
     * @return
     */
    protected int mAscii(String string, int position) {
        if ((position == 0) || (position > string.length())) {
            return -1;
        }

        return mExtract(string, position);
    }
	
}
