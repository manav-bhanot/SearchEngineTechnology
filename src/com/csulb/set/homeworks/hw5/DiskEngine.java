package com.csulb.set.homeworks.hw5;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Scanner;

import com.csulb.set.homeworks.hw4.PorterStemmer;


public class DiskEngine {

   public static void main(String[] args) {
	   
      Scanner scan = new Scanner(System.in);

      System.out.println("Menu:");
      System.out.println("1) Build index");
      System.out.println("2) Read and query index");
      System.out.println("Choose a selection:");
      int menuChoice = scan.nextInt();
      scan.nextLine();

      switch (menuChoice) {
         case 1:
            System.out.println("Enter the name of a directory to index: ");
            String folder = scan.nextLine();
            
            Path vocab = Paths.get(folder + "\\vocab.bin");
			Path postings = Paths.get(folder + "\\postings.bin");
			Path vocabTable = Paths.get(folder + "\\vocabTable.bin");
			
			if (vocab.toFile().exists() && !vocab.toFile().isDirectory() && postings.toFile().exists()
					&& !postings.toFile().isDirectory() && vocabTable.toFile().exists()
					&& !vocabTable.toFile().isDirectory()) {
				
				BasicFileAttributes attributes = null;
				try {
					attributes = Files.readAttributes(postings, BasicFileAttributes.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileTime creationTime = attributes.lastModifiedTime();
				System.out.println("Creation Time : "+creationTime);
			}

            break;

         case 2:
            System.out.println("Enter the name of an index to read:");
            String indexName = scan.nextLine();

            DiskInvertedIndex index = new DiskInvertedIndex(indexName);

            while (true) {
               System.out.println("Enter one or more search terms, separated " +
                "by spaces:");
               String input = scan.nextLine();

               if (input.equals("EXIT")) {
                  break;
               }

               int[] postingsList = index.GetPostings(
                PorterStemmer.processToken(input.toLowerCase())
               );

               if (postingsList == null) {
                  System.out.println("Term not found");
               }
               else {
                  System.out.print("Docs: ");
                  for (int post : postingsList) {
                     System.out.print(index.getFileNames().get(post) + " ");
                  }
                  System.out.println();
                  System.out.println();
               }
            }

            break;
      }
   }
}
