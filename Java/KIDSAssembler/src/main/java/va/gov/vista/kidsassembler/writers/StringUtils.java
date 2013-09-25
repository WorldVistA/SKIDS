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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

	/**
	 * 
	 * @param s
	 * @param delimiter
	 * @return
	 */
	public static String[] Split(String s, String delimiter) {
		if (s.endsWith(delimiter))
			s += delimiter;

		ArrayList<String> al = new ArrayList<String>();
		int tokenStart = 0;
		int tokenEnd = 0;
		String prev = delimiter;
		while ((tokenEnd = s.indexOf(delimiter, tokenStart)) != -1) {
			String token = s.substring(tokenStart, tokenEnd);
			if (token.equals(delimiter) && prev.equals(delimiter))
				al.add("");
			else if (!token.equals(delimiter))
				al.add(token);

			prev = token;
			tokenStart = tokenEnd + delimiter.length();
		}
		if (tokenStart != s.length())
			al.add(s.substring(tokenStart));

		return al.toArray(new String[al.size()]);
	}

	public static String Piece(String s, String delimiter, int pieceNum) {
		// JMW 8/7/2006 changed to pieceNum > flds.length from pieceNum >=
		// flds.length
		String[] flds = Split(s, delimiter);
		if (pieceNum > flds.length)
			return null;
		return flds[pieceNum - 1];
	}

	public static String MagPiece(String s, String delimiter, int pieceNum) {
		String[] flds = Split(s, delimiter);
		if (pieceNum > flds.length)
			return s;
		return flds[pieceNum - 1];
	}

	public static String getFileManDate(Date date) {

		SimpleDateFormat fileManDateFormat = new SimpleDateFormat("yyyMMdd");

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1700);

		Date newFileManDate = cal.getTime();

		return fileManDateFormat.format(newFileManDate);
	}

	public static boolean isEmpty(String source) {
		return source == null || source.isEmpty();
	}

	public static boolean existsAndEquals(String source, String value) {
		return source != null && source.equals(value);
	}
}
