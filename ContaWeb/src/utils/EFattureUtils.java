package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class EFattureUtils {

	public static void checkAndCreateDirectory(String directoryPath){
        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdir();
        }
    }
	
	public static void removeFiles(List<File> files) throws IOException{
        for(int i=0; i<files.size(); i++){
            removeFileOrDirectory(files.get(i));
        }
    }
    
    public static void removeFilesByPath(List<String> filesPaths) throws IOException{
        for(int i=0; i<filesPaths.size(); i++){
            removeFileOrDirectory(new File(filesPaths.get(i)));
        }
    }
    
    private static void removeFileOrDirectory(File file) throws IOException{
        if(file.isDirectory()){
            FileUtils.deleteDirectory(file);
        }else{
            file.delete();
        }
    }
	
}
