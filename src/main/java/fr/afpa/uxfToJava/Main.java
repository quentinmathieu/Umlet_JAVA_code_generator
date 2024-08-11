package fr.afpa.uxftojava;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


class Main
{
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		System.out.println("\n----- UXF parser -----");

		UxfParser uxfParser = new UxfParser();
		uxfParser.parseFile("uxfForTest/uxfParser.uxf");
	}
}

