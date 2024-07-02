package fr.afpa.uxfToJava;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


class Main
{
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		System.out.println("\n----- UXF parser -----");
		
		
		UxfParser parsedUxf = new UxfParser("uxfForTest/test.uxf");
	}
}

