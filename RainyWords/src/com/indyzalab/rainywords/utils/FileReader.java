package com.indyzalab.rainywords.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/** Assumes UTF-8 encoding. JDK 7+. */
public class FileReader {

  public static void main(String... aArgs) throws IOException {
	FileReader parser = new FileReader("corncob_lowercase.txt");
    parser.processLineByLine();
    log("Done.");
  }
  ArrayList<String> wordsList = new ArrayList<String>();
  /**
   Constructor.
   @param aFileName full name of an existing, readable file.
  */
  public FileReader(String aFileName){
    fFilePath = Paths.get(aFileName);
  }
  
  
  /** Template method that calls {@link #processLine(String)}.  */
  public final ArrayList<String> processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
//    	System.out.println("Start Reading")
      while (scanner.hasNextLine()){
        String name = processLine(scanner.nextLine());
        if(!name.trim().isEmpty()) wordsList.add(name);
      }      
    }
    return wordsList;
  }
  
  /** 
   Overridable method for processing lines in different ways.
    
   <P>This simple default implementation expects simple name-value pairs, separated by an 
   '=' sign. Examples of valid input: 
   <tt>height = 167cm</tt>
   <tt>mass =  65kg</tt>
   <tt>disposition =  "grumpy"</tt>
   <tt>this is the name = this is the value</tt>
  */
  protected String processLine(String aLine){
    //use a second Scanner to parse the content of each line 
    Scanner scanner = new Scanner(aLine);
    scanner.useDelimiter("=");
    if (scanner.hasNext()){
      //assumes the line has a certain structure
      String name = scanner.next();
      
//      log("Name is : " + quote(name.trim()) );
      return name;
    }
    else {
      log("Empty or invalid line. Unable to process.");
      return "";
    }
  }
  
  // PRIVATE 
  private final Path fFilePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }
  
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
} 

