package com.service.soap;

import java.io.*;
import java.net.*;

public class WebServiceClient {

	OutputStream outputStream;
	PrintWriter writer;

	// Change Boundary
	String boundary = "----=_Part_00_363537920.1452964624179--";

	String CRLF = "\r\n";
	String charset = "UTF-8";

	protected String request_xml = "";
	protected String endpoint = "";
	protected String soap_action = "";
	protected String[] attachment_name = null;
	protected String[] attachment_mime = null;
	protected byte[][] attachment_data = null;
	protected String xmlstring = "";

	String basicauth_user="";
			String basicauth_pwd="";
			
	public String getBasicauth_user() {
				return basicauth_user;
			}

			public void setBasicauth_user(String basicauth_user) {
				this.basicauth_user = basicauth_user;
			}

			public String getBasicauth_pwd() {
				return basicauth_pwd;
			}

			public void setBasicauth_pwd(String basicauth_pwd) {
				this.basicauth_pwd = basicauth_pwd;
			}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getRequest_xml() {
		return request_xml;
	}

	public void setRequest_xml(String request_xml) {
		this.request_xml = request_xml;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getSoap_action() {
		return soap_action;
	}

	public void setSoap_action(String soap_action) {
		this.soap_action = soap_action;
	}

	public String[] getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String[] attachment_name) {
		this.attachment_name = attachment_name;
	}

	public String[] getAttachment_mime() {
		return attachment_mime;
	}

	public void setAttachment_mime(String[] attachment_mime) {
		this.attachment_mime = attachment_mime;
	}

	public byte[][] getAttachment_data() {
		return attachment_data;
	}

	public void setAttachment_data(byte[][] attachment_data) {
		this.attachment_data = attachment_data;
	}

	public String getXmlstring() {
		return xmlstring;
	}

	public void setXmlstring(String xmlstring) {
		this.xmlstring = xmlstring;
	}

	public void addsoaprequest() throws IOException {

		writer.append(CRLF);
		writer.append("--" + boundary).append(CRLF).append("Content-Type: application/xop+xml; charset=")
				.append(charset).append("; type=\"text/xml\"").append(CRLF).append("Content-Transfer-Encoding: 8bit")
				.append(CRLF).append("Content-ID: <rootpart@soapui.org>").append(CRLF).append(CRLF).append(request_xml)
				.append(CRLF);

	}

	public void addattachment(String name, String mime, byte[] data) throws IOException {

		writer.append("--" + boundary).append(CRLF)
				// .append("Content-Type: ").append(attachment_mime).append(";
				// charset=").append(CHARSET).append("; name=")
				.append("Content-Type: ").append(mime).append("; name=").append(name).append(CRLF)
				.append("Content-Transfer-Encoding: binary").append(CRLF).append("Content-ID: <").append(name)
				.append(">").append(CRLF).append("Content-Disposition: attachment; name=\"").append(name)
				.append("\"; filename=\"").append(name).append("\"").append(CRLF).append(CRLF);

		writer.flush();

		outputStream.write(data);
		outputStream.flush();

		writer.append(CRLF);
		writer.flush();

	}

	 public final static String base64encode(byte[] d)
	  {
	    if (d == null) return null;
	    byte data[] = new byte[d.length+2];
	    System.arraycopy(d, 0, data, 0, d.length);
	    byte dest[] = new byte[(data.length/3)*4];

	    // 3-byte to 4-byte conversion
	    for (int sidx = 0, didx=0; sidx < d.length; sidx += 3, didx += 4)
	    {
	      dest[didx]   = (byte) ((data[sidx] >>> 2) & 077);
	      dest[didx+1] = (byte) ((data[sidx+1] >>> 4) & 017 |
	                  (data[sidx] << 4) & 077);
	      dest[didx+2] = (byte) ((data[sidx+2] >>> 6) & 003 |
	                  (data[sidx+1] << 2) & 077);
	      dest[didx+3] = (byte) (data[sidx+2] & 077);
	    }

	    // 0-63 to ascii printable conversion
	    for (int idx = 0; idx <dest.length; idx++)
	    {
	      if (dest[idx] < 26)     dest[idx] = (byte)(dest[idx] + 'A');
	      else if (dest[idx] < 52)  dest[idx] = (byte)(dest[idx] + 'a' - 26);
	      else if (dest[idx] < 62)  dest[idx] = (byte)(dest[idx] + '0' - 52);
	      else if (dest[idx] < 63)  dest[idx] = (byte)'+';
	      else            dest[idx] = (byte)'/';
	    }

	    // add padding
	    for (int idx = dest.length-1; idx > (d.length*4)/3; idx--)
	    {
	      dest[idx] = (byte)'=';
	    }
	    return new String(dest);
	  }

	  /**
	   * Encode a String using Base64 using the default platform encoding
	   **/
	  public final static String base64encode(String s) {
	    return base64encode(s.getBytes());
	  }
	  
	public void addAuth(HttpURLConnection connection){
		
		basicauth_user="";
		String basicauth_pwd="";
		
		if("" != basicauth_user && "" != basicauth_pwd  ){
		
		String encoded = base64encode(basicauth_user+":"+basicauth_pwd);
		connection.setRequestProperty("Authorization", "Basic "+encoded);
		}
		
	}
	public String getWebserviceresult() throws MalformedURLException, IOException {

		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";

		URL url = new URL(endpoint);
		
		//TODO modify for fiddler use
		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new
		// InetSocketAddress("127.0.0.1", 8888));
		
		//	URLConnection connection = url.openConnection(proxy);

		

		
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;

		addAuth(httpConn);
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=" + charset + ";action=\"" + soap_action + "\"");

		httpConn.addRequestProperty("MIME-Version", "1.0");

		httpConn.addRequestProperty("SOAPAction", "\"" + soap_action + "\"");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);

		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

		writer.append(request_xml);

		writer.flush();

		// outputStream.close();
		// Ready with sending the request.

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);
		// Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}

		return outputString;

	}

	public String getWebserviceMOMresult() throws MalformedURLException, IOException {

		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";

		// proxy

		URL url = new URL(endpoint);
		
		//TODO modify for fiddler use
		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new
		// InetSocketAddress("127.0.0.1", 8888));
		
		//	URLConnection connection = url.openConnection(proxy);

		
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;

		addAuth(httpConn);
		httpConn.setRequestProperty("Content-Type",
				"multipart/related; type=\"application/xop+xml\"; start=\"<rootpart@soapui.org>\"; start-info=\"text/xml\";action=\""
						+ soap_action + "\"; boundary=\"" + boundary + "\"");

		httpConn.addRequestProperty("MIME-Version", "1.0");

		httpConn.addRequestProperty("SOAPAction", "\"" + soap_action + "\"");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		// httpConn.setDoInput(true);

		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
		addsoaprequest();

		for (int i = 0; i < attachment_name.length; i++) {

			addattachment(attachment_name[i], attachment_mime[i], attachment_data[i]);

		}

		writer.append("--" + boundary).append(CRLF);
		writer.flush();

		// outputStream.close();
		// Ready with sending the request.

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);
		// Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}

		return outputString;
	}

}
