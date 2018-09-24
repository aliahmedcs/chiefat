package com.magdsoft.ws.utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class Upload {
	private final Logger logger = LoggerFactory.getLogger(Upload.class);

    //Save the uploaded file to this folder
   // private static String UPLOADED_FOLDER = "E://temp//";
	private static String UPLOADED_FOLDER = "/www/chiefatm/uploads/";
	
    // 3.1.1 Single file upload
  //  @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
   // public ResponseEntity<?> uploadFile(
    public String uploadFile(
           // @RequestParam("file") MultipartFile uploadfile) {
    		 MultipartFile uploadfile) throws IOException {
        logger.debug("Single file upload!");
        String name=null;
//        if (uploadfile.isEmpty()) {
//          //  return new ResponseEntity("please select a file!", HttpStatus.OK);
//        	return "Empty";
//        }

        try {

           name= saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
           // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        	//return "bad request";
        	//e.printStackTrace();
        	throw e;
        }

//        return new ResponseEntity("Successfully uploaded - " +
//                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
        return "http://chiefat.magdsoft.com/uploads/"+name;

    }

    // 3.1.2 Multiple file upload
    @PostMapping("/api/upload/multi")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles) {

        logger.debug("Multiple file upload!");

        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfiles));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }

    // 3.1.3 maps html form to a Model
//    @PostMapping("/api/upload/multi/model")
//    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {
//
//        logger.debug("Multiple file upload! With UploadModel");
//
//        try {
//
//            saveUploadedFiles(Arrays.asList(model.getFiles()));
//
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
//
//    }
//
//    //save file
//    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
//    	
//    	String newName=null;
//        for (MultipartFile file : files) {
//
//            if (file.isEmpty()) {
//                continue; 
//            }
//            
//            byte[] bytes = file.getBytes();
//            
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//           
//            Files.write(path, bytes);
//           // FTPClient ftpClient=new FTPClient();
////            if(file.getOriginalFilename().contains(" ")){
////            	
////                newName=file.getOriginalFilename().replaceAll(" ","_");
////                Path path2=Paths.get(UPLOADED_FOLDER +newName);
////            
////             ftpClient.rename(path.toString(), path2.toString());
////            }
//        }
//
//    }
////}
//}
    
    
//    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
//
//        
//
//        String newName=null;
//
//         for (MultipartFile file : files) {
//
//            if (file.isEmpty()) {
//
//                 continue;
//
//             }
//
//             
//
//            // Replace all characters that are not [a-z][A-Z][0-9] or _ with nothing.
//
//           // String name = file.getOriginalFilename().replaceAll("s/\\W+//g", "");
//            String name = file.getOriginalFilename().replaceAll("[ ()]", "");
//            System.out.println("KKKKKKKKK"+name);
//            Files.write(Paths.get(UPLOADED_FOLDER, name), file.getBytes());
//
//         }
//
//    }

 private String saveUploadedFiles(List<MultipartFile> files) throws IOException {

        

        String newName=null;

         for (MultipartFile file : files) {

            if (file.isEmpty()) {

                 continue;

             }

             

            // Replace all characters that are not [a-z][A-Z][0-9] or _ with nothing.

           // String name = file.getOriginalFilename().replaceAll("s/\\W+//g", "");
            String name = file.getOriginalFilename();
            try {
            	SecureRandom rand = new SecureRandom();
				int num = rand.nextInt(100000);
				String formatted = String.format("%05d", num);
				newName=new Date().getTime() +formatted+name.substring(name.lastIndexOf("."));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           // System.out.println("KKKKKKKKK"+name);
            Files.write(Paths.get(UPLOADED_FOLDER, newName), file.getBytes());

         }
         return newName;
    }
    
  } 
