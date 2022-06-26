package com.assignment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main
{

    public String delimiter = ";";
    public List<String> file1 = new ArrayList<>();
    public List<String> file2 = new ArrayList<>();

    /*
     Reading two files from data directory and storing in two list files
     for merging
     */
    public  void read(String csvFile, List<String> list) throws Exception
    {
        try
        {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while((line = br.readLine()) != null)
            {
                list.add(line);
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    /*
    comparing , removing duplicates in the file2
    and merging file1 and file2
     */
    public void compareTwoFiles(List<String> file1, List<String> file2)
    {
        int index = 0;

        while(file1.contains(file2.get(index)))
        {
            //removing duplicates
            file2.remove(index);
        }

        while(file2.size()!=0)
        {
            //Merging
            file1.add(file2.get(index));
            file2.remove(index);
        }
    }

    /*
    After merging data into the list
    Creating new tempCSV file and writing data in CSV File
     */
    public void createTempCSV()
    {
        try
        {
            FileWriter writer = new FileWriter("C:\\Users\\CSS\\Downloads\\Assessment\\temp\\temp.csv");
            BufferedWriter bwr = new BufferedWriter(writer);

            for(int i=0;i<file1.size();i++)
            {
                bwr.write(file1.get(i));
                bwr.write("\n");
            }
            bwr.close();
            System.out.println("succesfully Created and written to a temp.csv");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    //Method to get HeaderCount
    public int getHeaderCount(String CSVfile) throws Exception {
        int header = 0;
        BufferedReader csvReader = new BufferedReader(new FileReader(CSVfile));
        String row = "";
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            header = data.length;
            break;
        }
        csvReader.close();
        return header;
    }

    /*
    Creating new OutputCSV file , Calculating average and making to
    Single 'A' for All coountries Starting with 'A' and so on.
     */
    public void createOutputCSV(String CSVfile) throws Exception
    {
        try
        {
            File file = new File(CSVfile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            FileWriter writer = new FileWriter("C:\\Users\\CSS\\Downloads\\Assessment\\output\\output.csv");
            BufferedWriter bwr = new BufferedWriter(writer);

            int lineNumber = 1,header = getHeaderCount(CSVfile);

            // Writing the header name and header type in outputCSV
            while((lineNumber!=3)&&(line = br.readLine()) != null)
            {
                bwr.write(line);
                bwr.write("\n");
                lineNumber++;
            }

            //Managing the lineNumber
            lineNumber -= 1;

            /*
             Pointers to track Previous country Starting letter
             and current country starting letter
             */
            char previous = 'A';
            char current = 'A';

            //Total alphabet count
            int totalAlphabet = 26;

            for(int i=1;i<=totalAlphabet;i++) {
                // Array to add and calculate average for countries
                double digits[] = new double[header - 1];

                //Variable to track of count of particular alphabet country name
                int alphabetCount = 0;
                while ((line = br.readLine()) != null)
                {
                    lineNumber++;
                    String data[] = line.split(";");
                    current = data[0].charAt(0);
                    //if same alphabet found
                    if(current==previous) {
                        int index = 1;
                        for (int j = 0; j < digits.length && index < data.length; j++) {
                            String digitString = data[index];
                            //Watching out null values
                            if ((digitString != null) && (digitString.length() != 0)) {
                                digits[j] += Double.parseDouble(digitString);
                            }
                            else{
                                digits[j] += 0;
                            }
                            index++;
                            alphabetCount++;
                        }
                    }
                    // if alphabet varies
                    else {
                        bwr.write( previous+ ";");
                        for (double d : digits) {
                            //Average Calculation
                            d /= alphabetCount;
                            bwr.write(d + ";");
                        }
                        bwr.write("\n");
                        previous = current;
                        //getting into correct line of a file
                        line = Files.readAllLines(Paths.get(CSVfile)).get(lineNumber-1);
                    }
                    /*
                    Since after 'Z' alphabet, there is no letter
                    A if block is created seperately to write those data
                     */
                if(previous=='Z') {
                    bwr.write(previous + ";");
                    for (double d : digits) {
                        d /= alphabetCount;
                        bwr.write(d + ":");
                    }
                    bwr.write("\n");}
                }
            }
            br.close();
            bwr.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        System.out.println("Successfully Created and written the output.csv");
    }

    public static void main(String args[]) throws Exception
    {
       Main main = new Main();
       main.read("C:\\Users\\CSS\\Downloads\\Assessment\\data\\factbook.csv",main.file1);
       main.read("C:\\Users\\CSS\\Downloads\\Assessment\\data\\factbook-2.csv",main.file2);
       main.compareTwoFiles(main.file1,main.file2);
       main.createTempCSV();
       main.createOutputCSV("C:\\Users\\CSS\\Documents\\Ass.csv");
    }
}
