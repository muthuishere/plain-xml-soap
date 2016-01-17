package com.service.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class WebServiceClientTest {

	static String readfileasString(String filename) {

		byte[] fileContent = readfileasbytes(filename);
		String content = "";

		content = new String(fileContent);

		return content;
	}

	static byte[] readfileasbytes(String filename) {

		File file = new File(filename);
		byte[] fileContent = null;
		FileInputStream fin = null;
		try {
			// create FileInputStream object
			fin = new FileInputStream(file);

			fileContent = new byte[(int) file.length()];

			// Reads up to certain bytes of data from this input stream into an
			// array of bytes.
			fin.read(fileContent);
			// create string from byte array

		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading file " + ioe);
		} finally {
			// close the streams using close method
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}
		}

		return fileContent;
	}

	/*
	 * Method to test Upload Attachments to webservice using plain xml request
	 */
	public static void testMtomWebservice() {

		// Modify below data suites your request
		String url = "http://localhost:8080/WSEndpoint";
		String soap_req_xml_file = "C:\\soap_request.xml";
		String soap_action = "/Processes/Test/WSACTION";

		// Attachment data
		String upload_file = "C:\\ar.doc";
		String upload_file_mime = "application/msword";
		String upload_file_name = "ar.doc";
		
	
		

		WebServiceClient js = new WebServiceClient();

		// Testing for single attachment
		byte[][] attachment = new byte[1][];
		String[] mime = new String[1];
		String[] docs = new String[1];

		attachment[0] = readfileasbytes(upload_file);
		js.setAttachment_data(attachment);

		mime[0] = upload_file_mime;
		js.setAttachment_mime(mime);

		docs[0] = upload_file_name;
		js.setAttachment_name(docs);

		// Set Endpoint & SOAP Action

		js.setEndpoint(url);
		js.setSoap_action(soap_action);

		// Set SOAP Request PLain XML
		String sx = readfileasString(soap_req_xml_file);
		js.setRequest_xml(sx);


		
		/*
		 * To add basic auth headers
		String basicauth_user = "xxx";		
		String basicauth_pwd = "yyy";
		js.setBasicauth_pwd(basicauth_pwd);
		js.setBasicauth_user(basicauth_user);
		*/
		
		
		String response = "";
		try {
			response = js.getWebserviceMOMresult();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("MTOM Web Service Response");
		System.out.println(response);

	}

	/*
	 * Method to test webservice directly via plain xml
	 */
	public static void testWebservice() {

		// Modify below data suites your request
		String url = "http://localhost:8080/WSEndpoint";
		String soap_req_xml_file = "C:\\soap_request.xml";
		String soap_action = "/Processes/Test/WSACTION";

		WebServiceClient js = new WebServiceClient();

		// Set Endpoint & SOAP Action

		js.setEndpoint(url);
		js.setSoap_action(soap_action);

		// Set SOAP Request PLain XML
		String sx = readfileasString(soap_req_xml_file);
		js.setRequest_xml(sx);

		String response = "";
		try {
			response = js.getWebserviceMOMresult();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Web Service Response");
		System.out.println(response);

	}

	/*
	 * Test to Invoke web service
	 */
	public static void main(String[] args) {

		testMtomWebservice();
		testWebservice();
	}

}
