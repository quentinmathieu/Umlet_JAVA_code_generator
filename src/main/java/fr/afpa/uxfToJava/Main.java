package fr.afpa.uxfToJava;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe principale du projet, contient la fonction "main"
 */
class Main
{
	public static void main(String[] args) 
	{
		System.out.println("\n----- UXF parser -----");

		
		UxfParser parsedUxf = new UxfParser("uxfForTest/uml-employee.uxf");
	}
}

