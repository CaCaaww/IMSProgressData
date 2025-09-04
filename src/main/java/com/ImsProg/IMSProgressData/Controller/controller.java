package com.ImsProg.IMSProgressData.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import com.ImsProg.IMSProgressData.Model.imsProgGui;
import com.ImsProg.IMSProgressData.Persistance.imsProgGuiDao;

import io.micrometer.core.ipc.http.HttpSender.Response;

@EnableAutoConfiguration
@RestController
@RequestMapping("/imsProg")
public class controller {
    private static final Logger LOG = Logger.getLogger(controller.class.getName());
    private imsProgGuiDao ImsProgGuiDao;

    @Autowired
    public controller(imsProgGuiDao ImsProgGuiDao){
        this.ImsProgGuiDao = ImsProgGuiDao;
    }

    @GetMapping("")
    public ResponseEntity<imsProgGui[]> getAllData(){
        try {
            LOG.info("Get /imsProg");
            imsProgGui[] result = ImsProgGuiDao.getAllImsProgGui();
            return new ResponseEntity<imsProgGui[]>(result, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{type}")
    public ResponseEntity<imsProgGui[]> getDataByType(@PathVariable String type){
        try {
            LOG.info("Get /imsProg/" + type);
            imsProgGui[] result = ImsProgGuiDao.getImsProgByType(type);
            return new ResponseEntity<imsProgGui[]>(result, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/types")
    public ResponseEntity<String[]> getDataTypes(){
        try {
            LOG.info("Get /imsProg/types");
            String[] result = ImsProgGuiDao.getImsProgTypes();
            return new ResponseEntity<String[]>(result, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/success")
    public ResponseEntity<Boolean> success(){
        try {
            LOG.info("Get /imsProg/success");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }  
    }
    @GetMapping("/userGroups")
    public ResponseEntity<String[]> userGroups(){
        try {
            LOG.info("Get /imsProg/userGroups");
            String[] result = ImsProgGuiDao.getUserGroups();
            return new ResponseEntity<String[]>(result, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addData")
    public ResponseEntity<imsProgGui> addData(@RequestBody imsProgGui newData){
        LOG.info("POST /addData " + newData);
        try {
            imsProgGui result = ImsProgGuiDao.addData(newData);
            return new ResponseEntity<imsProgGui>(result, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/partialPrint")
    public ResponseEntity<String> partialPrint(@RequestBody imsProgGui[] data){
        LOG.info("POST /partialPrint");
        try {
            String result = ImsProgGuiDao.partialPrint(data);
            return new ResponseEntity<String>(result, HttpStatus.OK);
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
