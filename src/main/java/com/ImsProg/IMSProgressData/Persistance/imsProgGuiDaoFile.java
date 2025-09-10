package com.ImsProg.IMSProgressData.Persistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
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
    private String FOLDER_PATH;
    private imsProgGui[] fileContents;

    public imsProgGuiDaoFile(@Value("${csvFolder.path}") String FOLDER_PATH_FOLDER){
        this.FOLDER_PATH_FOLDER = FOLDER_PATH_FOLDER;
        readFile();
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
    public byte[] partialPrint(imsProgGui[] data){
        try {
            String masterString = "";
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
            masterString += border + "\n";
            for (int r = 0; r < rows.size(); r++) {
                masterString += String.format(rowFormat + "%n", (Object[]) rows.get(r));
                if (r == 0) { // after header row
                    masterString += border + "\n";
                }
            }      
            masterString += border + "\n";
            //return masterString;       
            byte[] fileBytes = masterString.getBytes(StandardCharsets.UTF_8);  
            return fileBytes;   
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

    @Override
    public imsProgGui deleteImsProgGui(imsProgGui thisOne){
        boolean found = false;
        readFile();
        try {
            File file = new File(FOLDER_PATH);
            if (file.exists()) {
                FileWriter file2 = new FileWriter(FOLDER_PATH);
                // file.createNewFile();
                BufferedWriter bw = new BufferedWriter(file2);
                for (imsProgGui imsProgGui : fileContents) {
                    if (!imsProgGui.equals(thisOne)) {
                        bw.append(imsProgGui.getProgramName() + ',' + imsProgGui.getCust() + ',' + imsProgGui.getDescription()
                        + ',' + imsProgGui.getUpdates() + ',' + imsProgGui.getType() + "\n");
                    } else {
                        found = true;
                    }
                }
                bw.close();
                file2.close();   
            }
            if (found){
                return thisOne;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public imsProgGui updateImsProgGui(imsProgGui[] objects){
        readFile();
        boolean found = false;
        try {
            File file = new File(FOLDER_PATH);
            if (file.exists()) {
                FileWriter file2 = new FileWriter(FOLDER_PATH);
                // file.createNewFile();
                BufferedWriter bw = new BufferedWriter(file2);
                for (imsProgGui imsProgGui : fileContents) {
                    if (!imsProgGui.equals(objects[0]) && found == false) {
                        bw.append(imsProgGui.getProgramName() + ',' + imsProgGui.getCust() + ',' + imsProgGui.getDescription()
                        + ',' + imsProgGui.getUpdates() + ',' + imsProgGui.getType() + "\n");
                    } else {
                        found = true;
                        bw.append(objects[1].getProgramName() + ',' + objects[1].getCust() + ',' + objects[1].getDescription()
                        + ',' + objects[1].getUpdates() + ',' + objects[1].getType() + "\n");
                    }
                }
                bw.close();
                file2.close();   
            }
            if (found){
                return objects[1];
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
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
