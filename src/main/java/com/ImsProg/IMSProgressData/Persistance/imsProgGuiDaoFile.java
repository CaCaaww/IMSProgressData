package com.ImsProg.IMSProgressData.Persistance;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
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
