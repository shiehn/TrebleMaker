package com.treblemaker.utils;

import com.treblemaker.Application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.treblemaker.configs.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileStructure {

    @Autowired
    public AppConfigs appConfigs;

    public void createDirectoryStructure(String songName) {
        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput()))) {
            (new File(appConfigs.getCompositionOutput())).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/midioutput"))) {
            (new File(appConfigs.getCompositionOutput() + "/midioutput")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioparts"))) {
            (new File(appConfigs.getCompositionOutput() + "/audioparts")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/monoparts"))) {
            (new File(appConfigs.getCompositionOutput() + "/monoparts")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/stereoparts"))) {
            (new File(appConfigs.getCompositionOutput() + "/stereoparts")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioshims"))) {
            (new File(appConfigs.getCompositionOutput() + "/audioshims")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioloops"))) {
            (new File(appConfigs.getCompositionOutput() + "/audioloops")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audiooutput"))) {
            (new File(appConfigs.getCompositionOutput() + "/audiooutput")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/midioutput/" + songName))) {
            (new File(appConfigs.getCompositionOutput() + "/midioutput/" + songName)).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getFinalMixOutput()))) {
            (new File(appConfigs.getFinalMixOutput())).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getTarPackage()))) {
            (new File(appConfigs.getTarPackage())).mkdirs();
        }
    }

    public String createShimsDirectory(String songId) {

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput()))) {
            (new File(appConfigs.getCompositionOutput())).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioshims"))) {
            (new File(appConfigs.getCompositionOutput() + "/audioshims")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioshims/" + songId))) {
            (new File(appConfigs.getCompositionOutput() + "/audioshims/" + songId)).mkdirs();
        }

        return appConfigs.getCompositionOutput() + "/audioshims/" + songId;
    }

    public static File[] getFilesInDirectory(String directory) {

        File folder = new File(directory);
        if(!folder.exists()){
            return null;
        }
        
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Application.logger.debug("LOG: File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                Application.logger.debug("LOG: Directory " + listOfFiles[i].getName());
            }
        }

        return listOfFiles;
    }

    public static void deleteAllFilesInDirectory(String directoryName) {
        File directory = new File(directoryName);

        //make sure directory exists
        if(!directory.exists()){

            Application.logger.debug("LOG: Directory does not exist.");
            return;
        }else{

            try{

                deleteFile(directory);

            }catch(IOException e){
                Application.logger.debug("LOG:", e);
                return;
            }
        }
    }

    public static void deleteFile(File file)
            throws IOException{

        if(file.isDirectory()){

            //directory is empty, then deleteFile it
            if(file.list().length==0){

                file.delete();
                Application.logger.debug("LOG: Directory is deleted : "
                        + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive deleteFile
                    deleteFile(fileDelete);
                }

                //check the directory again, if empty then deleteFile it
                if(file.list().length==0){
                    file.delete();
                    Application.logger.debug("LOG: Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }
        }else{
            //if file, then deleteFile it
            file.delete();
            Application.logger.debug("LOG: File is deleted : " + file.getAbsolutePath());
        }
    }

    public static void deleteSingleFile(String fileWithPath){
        try{

            File file = new File(fileWithPath);

            if(file.delete()){
                Application.logger.debug("LOG:" + file.getName() + " is deleted!");
            }else{
                Application.logger.debug("LOG: Delete operation is failed.");
            }
        }catch(Exception e){
            Application.logger.debug("LOG:", e);
        }
    }

    public static void copyFile(String sourcePath, String destPath) throws IOException {

        File source = new File(sourcePath);
        File dest = new File(destPath);

        Files.copy(source.toPath(), dest.toPath());
    }

    public static void copyFolder(File src, File dest)
            throws IOException{

        if(src.isDirectory()){

            //if directory not exists, create it
            if(!dest.exists()){
                dest.mkdir();
                Application.logger.debug("LOG: Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
            String files[] = src.list();

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile,destFile);
            }

        }else{
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            Application.logger.debug("LOG: File copied from " + src + " to " + dest);
        }
    }
}
