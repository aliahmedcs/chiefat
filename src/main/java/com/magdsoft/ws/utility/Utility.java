package com.magdsoft.ws.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import com.magdsoft.ws.controller.UserController;

//import com.magdsoft.sindbad.ws.controller.Register;
@Service
public class Utility {

	 private Pattern pattern;
	   private Matcher matcher;
	   String fileName=null;
	   @Async
	public void uploadFile(MultipartFile fl) throws NoSuchAlgorithmException, IOException {
		//String fileName=null;
		
			fileName = "UL" + new Date().getTime() + SecureRandom.getInstanceStrong().nextInt(Integer.MAX_VALUE) + fl.getOriginalFilename();
			Path path = Paths.get(UserController.PATH, fileName);
			File file = path.toFile();
			fl.transferTo(file);
		
		// Set owner to sindbadm, and attributes to be editable by the dashboard
		UserPrincipal user = FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName("chiefatm");
		Files.setOwner(path, user);
		Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rwxr--r--"));

//		if(fileName==null){
//			return fileName;
//		}
		//return "http://chiefat.magdsoft.com/uploads/" + fileName;
		//return "http://apis.magdsoft.com/uploads/" + fileName;
	}
//	   public String image(MultipartFile fl) throws NoSuchAlgorithmException, IOException {
//		  
//		uploadFile(fl);
//		   return "http://chiefat.magdsoft.com/uploads/" + fileName;
//	   }
//	public boolean validate(final String image){
//		 final String IMAGE_PATTERN =
//	                "([a-zA-Z0-9_ ]+(\\.(?i)(jpg|png|gif|bmp))$)";
////		 final String IMAGE_PATTERN =
////	                "([^\s]+(\.(?i)(jpg|png|gif|bmp))$)";
//		 pattern = Pattern.compile(IMAGE_PATTERN);
//		 image.replaceAll(" ", "_");
//		  matcher = pattern.matcher(image);
//		  return matcher.matches();
//
//	   }
	
	   public String image(MultipartFile fl) throws NoSuchAlgorithmException, IOException {
			  
			uploadFile(fl);
			   return "http://chiefat.magdsoft.com/uploads/" + fileName;
		   }
		public boolean validate(String image){
			 final String IMAGE_PATTERN =
		              
					 "([a-zA-Z0-9_ ]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";
					 		 //  "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
			 pattern = Pattern.compile(IMAGE_PATTERN);
			 String name="ali"+image.substring(image.lastIndexOf("."));
			  matcher = pattern.matcher(name);
			  return matcher.matches();

		   }

}
	