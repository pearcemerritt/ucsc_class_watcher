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

		URL url = new URL("https://pisa.ucsc.edu/class_search/index.php");

		String query = "action=results&binds%5B%3Aterm%5D=2130&binds%5B%3Areg_status%5D=all&binds%5B%3Asubject%5D=CHIN&binds%5B%3Acatalog_nbr_op%5D=%3D&binds%5B%3Acatalog_nbr%5D=&binds%5B%3Atitle%5D=&binds%5B%3Ainstr_name_op%5D=%3D&binds%5B%3Ainstructor%5D=&binds%5B%3Age%5D=&binds%5B%3Acrse_units_op%5D=%3D&binds%5B%3Acrse_units_from%5D=&binds%5B%3Acrse_units_to%5D=&binds%5B%3Acrse_units_exact%5D=&binds%5B%3Adays%5D=&binds%5B%3Atimes%5D=&binds%5B%3Aacad_career%5D=";

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

		// for (int i = 0; i < rowNodes.getLength(); ++i)
		// System.out.println(xpath.evaluate(".", rowNodes.item(i)));

		for (int i = 0; i < rowNodes.getLength(); ++i) {
			Node rowNode = rowNodes.item(i);
			Node imgNode = (Node) xpath.evaluate(".//img", rowNode, XPathConstants.NODE);
			System.out.println(imgNode.getAttributes().getNamedItem("alt").getNodeValue());
		}

		responseStream.close();

	}
}
