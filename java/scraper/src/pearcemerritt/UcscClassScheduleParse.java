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

public class UcscClassScheduleParse {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		
		String classNumberArg = args[0];
		String deptArg = args[1];

		URL url = new URL("https://pisa.ucsc.edu/class_search/index.php");
		
		// Attach department to generic query parameters needed to get a class
		// search going on pisa.ucsc.edu for all classes in Spring 2013
		String query = "binds%5B%3Asubject%5D=" + deptArg;
		query += "&action=results&binds%5B%3Aterm%5D=2132&binds%5B%3Areg_status%5D=all&binds%5B%3Acatalog_nbr_op%5D=%3D&binds%5B%3Acatalog_nbr%5D=&binds%5B%3Atitle%5D=&binds%5B%3Ainstr_name_op%5D=%3D&binds%5B%3Ainstructor%5D=&binds%5B%3Age%5D=&binds%5B%3Acrse_units_op%5D=%3D&binds%5B%3Acrse_units_from%5D=&binds%5B%3Acrse_units_to%5D=&binds%5B%3Acrse_units_exact%5D=&binds%5B%3Adays%5D=&binds%5B%3Atimes%5D=&binds%5B%3Aacad_career%5D=";

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);

		// More just for if you have a query string
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded;charset=UTF-8");

		OutputStream output = null;
		try {
			output = connection.getOutputStream();
			output.write(query.getBytes());
		} finally {
			if (output != null)
				output.close();
		}

		InputStream responseStream = connection.getInputStream();

		Tidy parser = new Tidy();
		parser.setXHTML(false); // make sure parsed DOM is in XHTML
		parser.setQuiet(true);
		parser.setShowWarnings(false);
		parser.setForceOutput(true); // despite shit loads of warnings

		Document responseDom = null;
		try {
			responseDom = parser.parseDOM(responseStream, null);
		} finally {
			if (responseStream != null)
				responseStream.close();
		}

		final XPath xpath = XPathFactory.newInstance().newXPath();

		NodeList rowNodes = (NodeList) xpath.evaluate(
				"//table[@id=\"results_table\"]/tbody/tr", responseDom,
				XPathConstants.NODESET);

		Node rowNode = null;
		
		// Find the row that corresponds to the class we wanted and save it as rowNode
		for (int i = 0; i < rowNodes.getLength(); ++i) {
			
			// Row nodes look like this: <tr><td><a href="..."> id </a></td></tr>
			// So the below gets the inner id text.
			String classNumber = rowNodes.item(i)		 // tr node
								  	.getFirstChild() // td node
								  	.getFirstChild() // a node
								  	.getFirstChild() // link text node
								  	.getNodeValue(); // text node text
			System.out.println(classNumber);
			
			// If this row is the class we're looking for, save it and bail
			if (classNumber.equals(classNumberArg)) {
				rowNode = rowNodes.item(i);
				break;
			}
			
			//for (int j = 0; j < idNode.getLength(); ++j) System.out.println(((Node) idNode.item(j)).);
			//Node imgNode = (Node) xpath.evaluate(".//img", rowNode, XPathConstants.NODE);
			//System.out.println(imgNode.getAttributes().getNamedItem("alt").getNodeValue());
		}
		
		if (rowNode == null) {
			throw new Exception("Oh shit! no row node found :(");
		}
		
		NodeList colNodes = (NodeList) xpath.evaluate("./td", rowNode, 
												XPathConstants.NODESET);
		
		String classId = colNodes.item(1).getFirstChild().getNodeValue();
		System.out.println(classId);
		
		String classTitle = colNodes.item(2).getFirstChild().getFirstChild().getNodeValue();
		System.out.println(classTitle);

		responseStream.close();

	}
}
