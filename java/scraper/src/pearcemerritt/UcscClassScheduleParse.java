package pearcemerritt;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/*
 * A scraper that looks at a University of California, Santa Cruz website with public
 * class information and returns information about a specified class's enrollment
 * status (a.k.a. is it full or is there a spot open). This would ideally be used
 * by a web service to periodically scrape the website and return whether certain
 * classes have opened so that others don't have to spend all their time refreshing
 * the student class search portal
 * 
 * @author Pearce Merritt
 */
public class UcscClassScheduleParse {

	/*
	 * Usage:
	 * args[0] class number of watch class (5 digit unique id)
	 * args[1] department watch class is in (4 letter abbreviation code)
	 */
	public static void main(String[] args) throws Exception {
		
		String classNumberArg = args[0];
		String deptArg = args[1];
		
		final String term = "2132"; // Spring 2013
		
		InputStream htmlStream = getDeptClassesHTML(deptArg, term);

		Document responseDom = parseHTML(htmlStream);

		final XPath xpath = XPathFactory.newInstance().newXPath();

		// Get every row (i.e. every class) from the html class table
		NodeList rowNodes = (NodeList) xpath.evaluate(
				"//table[@id=\"results_table\"]/tbody/tr", responseDom,
				XPathConstants.NODESET);

		// Find the row that corresponds to the class we wanted and save it
		Node rowNode = null;
		for (int i = 0; i < rowNodes.getLength(); ++i) {
			
			// Row nodes look like this: <tr><td><a href="..."> id </a></td></tr>
			// So the below gets the inner id text.
			String classNumber = rowNodes.item(i)		  // tr node
										 .getFirstChild() // td node
										 .getFirstChild() // a node
										 .getFirstChild() // link text node
										 .getNodeValue(); // text node text
			
			// If this row is the class we're looking for, save it and bail
			if (classNumber.equals(classNumberArg)) {
				rowNode = rowNodes.item(i);
				break;
			}
		}
		
		if (rowNode == null) {
			throw new Exception("Invalid class number and department.");
		}
		
		// Get the column entries for the class we found in the loop
		NodeList colNodes = (NodeList) xpath.evaluate("./td", rowNode, 
												XPathConstants.NODESET);
		
		// Column headers for each row in the table are:
		// 0 Class #        7 Status
		// 1 Class ID       8 Capacity
		// 2 Class Title    9 Enrollment Total
		// 3 Type           10 Available Seats
		// 4 Days           11 Location
		// 5 Times          12 Course Materials
		// 6 Instructor
		
		// Get the class id (i.e. English 1A or Math 100)
		// Looks like <td>text</td>
		String classId = colNodes.item(1).getFirstChild().getNodeValue();
		System.out.println(classId);
		
		// Get the class title (i.e. Intro to Music Theory or Advanced Nueroscience)
		// Looks like <td><a>text</a></td>
		String classTitle = colNodes.item(2).getFirstChild().getFirstChild().getNodeValue();
		System.out.println(classTitle);
		
		// Get the status of the class (i.e. Closed, Wait List, Open)
		// Looks like <td><center><img alt="status" .../></center></td>
		String classStatus = colNodes.item(7).getFirstChild().getFirstChild()
									 .getAttributes().getNamedItem("alt").getNodeValue();
		System.out.println(classStatus);

		htmlStream.close();
	}
	
	/*
	 * Do a POST request to a public site that has UCSC class search available exactly the
	 * same way that my.ucsc.edu has and return the HTML response. The HTML is a table
	 * of classes for a specific department.
	 * 
	 * @param dept The department for classes to request
	 * @param term The term for classes to request
	 * @return the HTML response in the form of a stream
	 */
	public static InputStream getDeptClassesHTML(String dept, String term) throws Exception {
		
		final String SITE_URL = "https://pisa.ucsc.edu/class_search/index.php";
		
		// Other necessary parameters to the POST, things like wheter to show only
		// open classes or not, etc.
		final String SITE_PARAMS = "action=results&binds%5B%3Areg_status%5D=all&binds%5B%3Acatalog_nbr_op%5D=%3D&binds%5B%3Acatalog_nbr%5D=&binds%5B%3Atitle%5D=&binds%5B%3Ainstr_name_op%5D=%3D&binds%5B%3Ainstructor%5D=&binds%5B%3Age%5D=&binds%5B%3Acrse_units_op%5D=%3D&binds%5B%3Acrse_units_from%5D=&binds%5B%3Acrse_units_to%5D=&binds%5B%3Acrse_units_exact%5D=&binds%5B%3Adays%5D=&binds%5B%3Atimes%5D=&binds%5B%3Aacad_career%5D=";

		URL url = new URL(SITE_URL);
		
		StringBuilder sb = new StringBuilder(SITE_PARAMS);
		sb.append("&binds%5B%3Asubject%5D=").append(dept);
		sb.append("&binds%5B%3Aterm%5D=").append(term);
		
		// All the POST parameters in the form of one UTF-8 encoded query string.
		String query = sb.toString();
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Necessary for POST method
		connection.setDoOutput(true);

		// Things that should be set when a query string is included with a POST request
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded;charset=UTF-8");

		// Do the POST request
		OutputStream output = null;
		try {
			output = connection.getOutputStream();
			
			if (output != null)
				output.write(query.getBytes());
		}
		finally {
			if (output != null)
				output.close();
		}

		return connection.getInputStream();
	}
	
	/*
	 * Use the jTidy library to parse an html stream and make it well formed so
	 * that it can be traversed by other tools such as Xpath.
	 * @param htmlStream a stream that represents and html page that is not
	 *                   not necessarily in XHTML
	 * @return the document parsed and returned as XHTML
	 */
	public static Document parseHTML(InputStream htmlStream) throws Exception {
		
		// Initialize singleton for jTidy library
		Tidy parser = new Tidy();
		
		// jTidy has a rough time with non-XHTML sites (like pisa.ucsc.edu)
		// and will want to complain and give up a lot. This is the best that
		// can be done in an effort to shut it up.
		parser.setXHTML(true); // make sure parsed DOM is in XHTML
		parser.setQuiet(true);
		parser.setShowWarnings(false);
		parser.setForceOutput(true); // despite shit loads of warnings

		Document responseDom = null;
		try {
			responseDom = parser.parseDOM(htmlStream, null);
		} finally {
			if (htmlStream != null)
				htmlStream.close();
		}
		
		return responseDom;
	}
}
