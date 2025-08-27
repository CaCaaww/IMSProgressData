package com.ImsProg.IMSProgressData.Persistance;

import com.ImsProg.IMSProgressData.Model.imsProgGui;

public interface imsProgGuiDao {
    
    imsProgGui[] getAllImsProgGui();

    imsProgGui[] getImsProgByType(String type);

    String[] getImsProgTypes();
}
