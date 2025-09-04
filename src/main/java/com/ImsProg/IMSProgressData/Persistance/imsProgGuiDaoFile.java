package com.ImsProg.IMSProgressData.Persistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ImsProg.IMSProgressData.Model.imsProgGui;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Component
public class imsProgGuiDaoFile implements imsProgGuiDao{
    
    private String FOLDER_PATH_FOLDER;
    private String OUTPUT_PATH_FOLDER;
    private String FOLDER_PATH;
    private String OUTPUT_PATH;
    private imsProgGui[] fileContents;

    public imsProgGuiDaoFile(@Value("${csvFolder.path}") String FOLDER_PATH_FOLDER, @Value("${outputFolder.path}") String OUTPUT_PATH_FOLDER){
        this.FOLDER_PATH_FOLDER = FOLDER_PATH_FOLDER;
        this.OUTPUT_PATH_FOLDER = OUTPUT_PATH_FOLDER;

        readFile();
        SetOutPutFolderPath();
    }

    @Override
    public imsProgGui[] getAllImsProgGui(){
        readFile();
        return fileContents;
    }

    @Override
    public imsProgGui[] getImsProgByType(String type){
        readFile();
        ArrayList<imsProgGui> result = new ArrayList<imsProgGui>();
        for (imsProgGui item : fileContents){
            if (item.getType().equals(type)){
                result.add(item);
            }
        }
        return result.toArray(new imsProgGui[result.size()]);

    }

    @Override
    public String[] getImsProgTypes(){
        readFile();
        Set<String> set = new LinkedHashSet<String>();
        for (imsProgGui item : fileContents){
            set.add(item.getType());
        }
        return set.toArray(new String[set.size()]);
    }

    @Override
    public imsProgGui addData(imsProgGui newData){
        SetFolderPath();
        String newLine = newData.getProgramName() + ',' + newData.getCust() + ',' + newData.getDescription() + ',' + newData.getUpdates() + ',' + newData.getType();
        File file = new File(FOLDER_PATH);
        try {
            if (file.exists()) {
                FileWriter file2 = new FileWriter(FOLDER_PATH, true);
                // file.createNewFile();
                BufferedWriter bw = new BufferedWriter(file2);
                bw.append(newLine + "\n");
                bw.close();
                file2.close();   
            } 
            return newData;
        } catch (Exception e) {
            throw new RuntimeException("Error creating CSV file", e);
        }
    }

    @Override
    public String partialPrint(imsProgGui[] data){
        SetOutPutFolderPath();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss");
        String date = LocalDateTime.now().format(formatter);
        String fileName = OUTPUT_PATH + "partialPrint-" + date + ".txt";
        File newFile = new File(fileName);
        try {
            if (!newFile.exists()) {
                FileWriter file2 = new FileWriter(fileName);
                BufferedWriter bw = new BufferedWriter(file2);

                //create list of rows
                ArrayList<String[]> rows = new ArrayList<String[]>();
                rows.add(new String[]{"Program Name", "Customer", "Description", "Updates", "Type"});
                for (imsProgGui imsProgGui : data){
                    rows.add(new String[]{imsProgGui.getProgramName(), imsProgGui.getCust(), imsProgGui.getDescription(), imsProgGui.getUpdates(), imsProgGui.getType()});
                }
                //count greatest length of each row
                int[] colWidths = new int[rows.get(0).length];
                for (String[] row : rows) {
                    for (int i = 0; i < row.length; i++) {
                        colWidths[i] = Math.max(colWidths[i], row[i].length());
                    }
                }
                //build format string
                StringBuilder formatBuilder = new StringBuilder();
                formatBuilder.append("|");
                for (int width : colWidths) {
                    formatBuilder.append(" %-").append(width).append("s |");
                }
                String rowFormat = formatBuilder.toString();
                // Build border line
                StringBuilder borderBuilder = new StringBuilder();
                borderBuilder.append("+");
                for (int width : colWidths) {
                    borderBuilder.append("-".repeat(width + 2)).append("+");
                }
                String border = borderBuilder.toString();
                // write to file
                bw.write(border + "\n");
                for (int r = 0; r < rows.size(); r++) {
                    bw.write(String.format(rowFormat + "%n", (Object[]) rows.get(r)));
                    if (r == 0) { // after header row
                        bw.write(border + "\n");
                    }
                }
                bw.write(border);                
                bw.close();
                file2.close();
                return fileName;
            } else {
                return "Error";
            }    
        } catch (Exception e) {
            throw new RuntimeException("Error creating file", e);
        }
    }

    @Override
    public String[] getUserGroups(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<String> temp = new ArrayList<>();
        auth.getAuthorities().forEach(authority -> {
            System.out.println("User is in group: " + authority.getAuthority());
            temp.add(authority.getAuthority());
            
        });
        
        return temp.toArray(new String[temp.size()]);
    }
    

    private void SetFolderPath(){
        try {
            Path path = Path.of(FOLDER_PATH_FOLDER);
            String str = Files.readString(path);

            // Printing the string
            System.out.println(str);
            FOLDER_PATH = str;
        } catch (Exception e) {
            System.out.println("error reading file location");
            e.printStackTrace();
        }
    }
    private void SetOutPutFolderPath(){
        try {
            Path path = Path.of(OUTPUT_PATH_FOLDER);
            String str = Files.readString(path);

            // Printing the string
            System.out.println(str);
            OUTPUT_PATH = str;
        } catch (Exception e) {
            System.out.println("error reading file location");
            e.printStackTrace();
        }
    }

    private void readFile(){
        SetFolderPath();
        try {
        FileReader filereader = new FileReader(FOLDER_PATH);
        CSVReader csvReader = new CSVReaderBuilder(filereader)
                                  .withSkipLines(1)
                                  .build();
        List<String[]> allData = csvReader.readAll();

        ArrayList<imsProgGui> contents = new ArrayList<imsProgGui>();
        for (String[] row : allData) {
            imsProgGui item = new imsProgGui(row[0], row[1], row[2], row[3], row[4]);
            contents.add(item);
        }
        fileContents = contents.toArray(new imsProgGui[contents.size()]);

    }
    catch (Exception e) {
        e.printStackTrace();
    }
    }
}
