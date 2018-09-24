package com.magdsoft.ws.controller;

//import static org.mockito.Matchers.notNull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.magdsoft.ShefatApplication;
import com.magdsoft.ws.form.Aboutus;
import com.magdsoft.ws.form.AddDish;
import com.magdsoft.ws.form.AddOrder;
import com.magdsoft.ws.form.AddVideo;
import com.magdsoft.ws.form.ApiSendMessage;
import com.magdsoft.ws.form.ChangePassword;
import com.magdsoft.ws.form.EmptyObject;
import com.magdsoft.ws.form.GetCities;
import com.magdsoft.ws.form.GetDish;
import com.magdsoft.ws.form.GetDishes;
import com.magdsoft.ws.form.GetOrder;
import com.magdsoft.ws.form.GetOrders;
import com.magdsoft.ws.form.Pagination;
import com.magdsoft.ws.form.SetSchedule;
import com.magdsoft.ws.form.UpdateInfo;
import com.magdsoft.ws.model.AppSetting;
import com.magdsoft.ws.model.Bill;
import com.magdsoft.ws.model.Chief;
import com.magdsoft.ws.model.City;
import com.magdsoft.ws.model.Dish;
import com.magdsoft.ws.model.Message;
import com.magdsoft.ws.model.Notification;
import com.magdsoft.ws.model.NotificationType;
import com.magdsoft.ws.model.NotifiedPerson;

import com.magdsoft.ws.model.Theorder;
import com.magdsoft.ws.model.User;
import com.magdsoft.ws.model.Video;
import com.magdsoft.ws.password.DefaultPasswordHasher;
import com.magdsoft.ws.utility.Upload;
import com.magdsoft.ws.utility.Utility;

@Controller
@RequestMapping("/apiChief")
public class ChiefWebserviceController {
	@Autowired
	Upload upload;
	@Autowired
	public Utility utility;
	@Autowired
	public static EntityManager entityManager;
	//public static final String PATH = "/home/chiefatm/public_html/uploads";
	public static final String PATH = "/www/chiefatm/uploads";
	public static final int PAGINATION = 20;
	public static final int PAGINATION_GET_HELP = PAGINATION;
	public static final int PAGINATION_GET_HISTORY = PAGINATION;
	@Autowired
	SendMessageApi sendMessageApi;
	
	
	ApiSendMessage apiSend ;
	public static final byte[] userName = new byte[]{'S', 'h', 'i', 'f', 'a', 't'};
	 private static String prepareTelephoneNumber(String telNum) {
		    return new BigInteger(telNum).toString();
	    }
	 
	
		
		public static void main(String[] args) {
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			SpringApplication.run(ShefatApplication.class, args);
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			Date date=new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			//if(day==1) {
			float percentage=0;
			Query q = entityManager.createQuery("select percentage from AppSetting");
		    percentage = (float) q.getSingleResult();
			Long currentDate =Long.valueOf( new Timestamp(date.getTime()).toString());
		    Long before= (long) (currentDate-76032000.0);
			Query q1 = entityManager.createQuery("select sum(totalPrice),chiefId from Theorder t where t.chiefId=(select id from Chief) and t.createdAt "
			 		+ "between"+ before+ " and "+ currentDate);
			List<EmptyObject> orders = q1.getResultList();
	        for(EmptyObject e:orders) {
	        	Bill bill=null;
	        	Chief chie=entityManager.find(Chief.class, e.getChiefIds());
	        	bill.setChief_id(chie);
	        	bill.setCost((int) (e.getSumCost()*percentage));
	        	bill.setIsPaid(false);
	        	bill.setEndDate(new Date());
	        	bill.setStartDate(new Date(before));
	        	entityManager.persist(bill);
	        	 
	       //  } 
	         }
			
		}
	 
	 
	 
	 
	 
	 
	 
	 
	@Transactional
	@RequestMapping("/login")
	public @ResponseBody Map<String, Object> login(Chief chief, @RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}

	//		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

			
			if (chief.getPhone() == null || chief.getPassword() == null) {
				map.put("status", 403);
				return map;
			}
//			if (!chief.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}
			
			
			// } else if (user.getPhone().length() < 10) {
			// map.put("status", 402);
			// return map;
			// }
			// boolean isUserAvailable = false;

			// String name = null;
			// Integer userId = null;
			// String apiToken = null;
			// String apiToken2=null;
			// em.getTransaction().begin();
			Query q = entityManager.createQuery("from Chief where phone=:em");
			q.setParameter("em", chief.getPhone());
			Chief chi = null;
			try {
				chi = (Chief) q.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if(chi.getCity_id()==null){
				map.put("status", 300);
				map.put("apiToken", chi.getApiToken());
				map.put("name", chi.getName());
				map.put("email", chi.getEmail());
				return map;
			}
			// if (use!= null) {
			if (DefaultPasswordHasher.getInstance().isPasswordValid(chief.getPassword(), chi.getPassword())) {
				map.put("status", 200);
				map.put("id", chi.getId());
				map.put("apiToken", chi.getApiToken());
				map.put("name", chi.getName());
				// map.put("password", u.getPassword());
				map.put("phone", chi.getPhone());
				map.put("userImage", chi.getPic());
				try{
				map.put("email", chi.getEmail());
				map.put("cityAr", chi.getCity_id().getNameAr());
				map.put("cityEn", chi.getCity_id().getNameEn());
				map.put("follower_num", chi.getUser_id().size());
				}catch(Exception e){
					map.put("cityAr", null);
					map.put("cityEn", null);
					map.put("follower_num", 0);
				}
				return map;
			} else if (!DefaultPasswordHasher.getInstance().isPasswordValid(chief.getPassword(), chi.getPassword())) {
				map.put("status", 406);
				return map;
			}
			// } else if
			// (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
			// use.getPassword())) {
			// map.put("status", 406);
			// return map;
			// }
			//

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);

			// throw e;
			return map;
		}
		map.put("status", 222);
		return map;

	}

	// @Transactional
	// @RequestMapping("/login")
	// public @ResponseBody Map<String, Object> login(Chief chief,
	// @RequestBody(required = false) Chief chiefBody) {
	//
	// Map<String, Object> map = new HashMap<>();
	// // em.getTransaction().begin();
	// try {
	// if (chiefBody != null) {
	// chief = chiefBody;
	// }
	// if (chief.getEmail() == null || chief.getPassword() == null) {
	// map.put("status", 403);
	// return map;
	// }
	//
	// if (user.getPassword().length() < 6) {
	// map.put("status", 406);
	// return map;
	// }
	//
	// String EMAIL_REGEX =
	// "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	//
	// if(!user.getEmail().matches(EMAIL_REGEX)){
	// map.put("status", 405);
	// return map;
	// }
	//
	//// try {
	//// user.getEmail();
	//// } catch (Exception e) {
	//// map.put("status", 405);
	//// return map;
	//// }
	//
	// // } else if (user.getPhone().length() < 10) {
	// // map.put("status", 402);
	// // return map;
	// // }
	// // boolean isUserAvailable = false;
	//
	// // String name = null;
	// // Integer userId = null;
	// // String apiToken = null;
	// // String apiToken2=null;
	// // em.getTransaction().begin();
	// Query q = entityManager.createQuery("from User where email=:em");
	// q.setParameter("em", user.getEmail());
	// User use = null;
	// try {
	// use = (User) q.getSingleResult();
	//
	// } catch (NoResultException ex) {
	// map.put("status", 400);
	// return map;
	// }
	// // if (use!= null) {
	// if
	// (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
	// use.getPassword())) {
	// map.put("status", 200);
	// // map.put("userId", use.getId());
	// map.put("apiToken", use.getApiToken());
	// map.put("name", use.getName());
	// // map.put("password", u.getPassword());
	// map.put("phone", use.getPhone());
	// map.put("userImage", use.getPic());
	//
	// map.put("email", use.getEmail());
	//
	// return map;
	// }else
	// if(!DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
	// use.getPassword())){
	// map.put("status", 406);
	// return map;
	// }
	// // } else if
	// //
	// (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
	// // use.getPassword())) {
	// // map.put("status", 406);
	// // return map;
	// // }
	// //
	//
	// } catch (Exception e) {
	// // Map<String,Object> map=new HashMap<>();
	// // map.put("status", 404);
	//
	// // throw e;
	// return map;
	// }
	// map.put("status", "you are not registered");
	// return map;
	//
	// }

	@Transactional
	@RequestMapping("/register")
	public @ResponseBody Map<String, Object> register(Chief chief, @RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();

		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}

			if (chief.getPhone() == null || chief.getPassword() == null || chief.getName() == null
					|| chief.getName().isEmpty()) {
				map.put("status", 403);
				return map;

				// } else if (chief.getPhone().length() < 9) {
				// map.put("status", 400);
				// return map;

			} else if (chief.getPassword().length() < 6) {
				map.put("status", 407);
				return map;
			}
			String regex = "[0-9]+";
			if (!chief.getPhone().matches(regex)||chief.getPhone().length() < 9) {
				map.put("status", 405);
				return map;
			}
//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//
//			if (!chief.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 406);
//				return map;
//			}
			String regx3 = "^[\\p{L} .'-]+$";
			if(!chief.getName().matches(regx3)){
				map.put("status", 408);
				return map;
			}
			String apiToken = null;

//			Query q1 = entityManager.createQuery("from Chief where email=:em");
//			q1.setParameter("em", chief.getEmail());
//			try {
//				Chief chie = (Chief) q1.getSingleResult();
//				map.put("status", 401);
//				return map;
//			} catch (NoResultException ex) {
//
//			}

			Query q = entityManager.createQuery("from Chief where phone=:ph");
			q.setParameter("ph", chief.getPhone());

			try {
				Chief use = (Chief) q.getSingleResult();
				map.put("status", 400);
				return map;
			} catch (NoResultException ex) {

				SecureRandom random = new SecureRandom();
				apiToken = new BigInteger(500, random).toString(32);
				SecureRandom rand = new SecureRandom();
				// int num = rand.nextInt(100000);
				// String formatted = String.format("%05d", num);
				// int veryficationCode1 = Integer.valueOf(formatted);
				int verificationCode1 = 12345;
				Chief newChief = new Chief();
				newChief.setPhone(chief.getPhone());

				newChief.setName(chief.getName());
//				try {
//					newChief.setEmail(chief.getEmail());
//				} catch (Exception e) {
//					map.put("status", 401);
//					return map;
//				}
				newChief.setIsOnline(false);
				newChief.setIsActive(false);
				newChief.setVerificationCode(verificationCode1);
				newChief.setApiToken(apiToken);
				newChief.setPassword(DefaultPasswordHasher.getInstance().hashPassword(chief.getPassword()));
				// newUser.setIsActive(false);
				entityManager.persist(newChief);

				map.put("status", 200);
				map.put("apiToken", apiToken);
				
				

				// map.put("userId", newUser.getId());
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);

			// throw e;
			return map;

		}
		// return null;
	}

	@Transactional
	@RequestMapping("/resetPassword")
	public @ResponseBody Map<String, Object> resetPassword(Chief chief,
			@RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}
			//String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
			if (chief.getPhone() == null) {
				map.put("status", 403);
				return map;
			}
//			if (!chief.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}
			
			Chief chie1 = null;
			Query q1 = entityManager.createQuery("from Chief where phone=:em");
			q1.setParameter("em", chief.getPhone());
			try {
				chie1 = (Chief) q1.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			SecureRandom rand = new SecureRandom();
			 int num = rand.nextInt(100000);
			 String formatted = String.format("%05d", num);
			 int verificationCode1 = Integer.valueOf(formatted);
			//int verificationCode1 = 12345;
			chie1.setVerificationCode(verificationCode1);
			entityManager.persist(chie1);
			apiSend= new ApiSendMessage();
			
			apiSend.setUserName(new String(userName));
			apiSend.setNumbers(prepareTelephoneNumber(chief.getPhone()));
			apiSend.setMessage("Your Verification Code Is : "+verificationCode1);
			apiSend.setPassword("1475321");
			apiSend.setSender("admin");
//			SimpleDateFormat timeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//			apiSend.setDatetime(timeDate.format(new Date()));
			apiSend.setUnicode("E");
			apiSend.setReturnValue("full");
			
			

			sendMessageApi.sendSms(apiSend);
			// ################
			// #add send sms
			// ################
			map.put("status", 200);
			// map.put("apiToken", use1.getApiToken());
			return map;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);

			// throw e;
			return map;
		}
	}

	@Transactional
	@RequestMapping("/validateCode")
	public @ResponseBody Map<String, Object> validateCode(Chief chief, @RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}

			if (chief.getPhone() == null || chief.getVerificationCode() == null) {
				map.put("status", 403);
				return map;
			}

//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//
//			if (!chief.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}
			Chief chie1 = null;
			Query q1 = entityManager.createQuery("from Chief where phone=:em");
			q1.setParameter("em", chief.getPhone());
			try {
				chie1 = (Chief) q1.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			if (chief.getVerificationCode().equals(chie1.getVerificationCode())) {
				chie1.setVerificationCode(null);
				SecureRandom random = new SecureRandom();
				String apiToken = new BigInteger(500, random).toString(32);
				chie1.setTempApiToken(apiToken);
				entityManager.persist(chie1);
				map.put("status", 200);
				map.put("apiToken", chie1.getTempApiToken());
				return map;
			} else {
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);

			// throw e;
			return map;
		}

	}

	@Transactional
	@RequestMapping("/updatePassword")
	public @ResponseBody Map<String, Object> updatePassword(Chief chief,
			@RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}
			if (chief.getPassword() == null || chief.getApiToken() == null || chief.getPassword().isEmpty()
					|| chief.getApiToken().isEmpty()) {
				map.put("status", 403);
				return map;
			} else if (chief.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;

			}
			if (chief.getPassword().length() < 6) {
				map.put("status", 406);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where tempApiToken=:api");
			q.setParameter("api", chief.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			// int num = rand.nextInt(100000);
			// String formatted = String.format("%05d", num);
			// int veryficationCode1 = Integer.valueOf(formatted);
			// int veryficationCode1 = 12345;
			// User currUser = entityManager.find(User.class, use.getId());
			if (chie.getVerificationCode() == null && chief.getApiToken().equals(chie.getTempApiToken())) {
				chie.setPassword(DefaultPasswordHasher.getInstance().hashPassword(chief.getPassword()));
				chie.setTempApiToken(null);
				entityManager.persist(chie);
				map.put("status", 200);
				// map.put("apiToken", use.getApiToken());
			}
			return map;

			// }else{
			// map.put("status", 401);
			// return map;
			// }
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/AboutUs")
	public @ResponseBody Map<String, Object> aboutUs(Aboutus aboutus,
			@RequestBody(required = false) Aboutus aboutusBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (aboutusBody != null) {
				aboutus = aboutusBody;
			}
			if (aboutus.getApiToken() == null || aboutus.getLang() == null) {
				map.put("status", 403);
				return map;
			}
			if (aboutus.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			if (!aboutus.getLang().equals("ar") && !aboutus.getLang().equals("en")) {
				map.put("status", 406);
				return map;
			}
			Chief use1 = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", aboutus.getApiToken());
			try {
				use1 = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			String message = null;
			try {
				if (aboutus.getLang().equals("ar")) {
					q1 = entityManager.createQuery("select appMsgAr from AppSetting");
					message = (String) q1.getSingleResult();
				} else if (aboutus.getLang().equals("en")) {
					q1 = entityManager.createQuery("select appMsgEn from AppSetting");
					message = (String) q1.getSingleResult();
				}
			} catch (Exception NoResultException) {

			}
			map.put("status", 200);
			map.put("mssage", message);
			return map;

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/contactUs")
	public @ResponseBody Map<String, Object> contactUs(Aboutus aboutus,
			@RequestBody(required = false) Aboutus aboutusBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (aboutusBody != null) {
				aboutus = aboutusBody;
			}
			if (aboutus.getApiToken() == null) {
				map.put("status", 403);
				return map;
			}
			if (aboutus.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief use1 = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", aboutus.getApiToken());
			try {
				use1 = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager.createQuery("from AppSetting");
			AppSetting appSetting = (AppSetting) q1.getSingleResult();
			map.put("status", 200);
			map.put("email", appSetting.getConteractMail());
			map.put("phone", appSetting.getConteractPhone());
			return map;

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/WelcomeMsg")
	public @ResponseBody Map<String, Object> WelcomeMsg(Aboutus aboutus,
			@RequestBody(required = false) Aboutus aboutusBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (aboutusBody != null) {
				aboutus = aboutusBody;
			}
			if (aboutus.getApiToken() == null || aboutus.getLang() == null) {
				map.put("status", 403);
				return map;
			}
			if (aboutus.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			if (!aboutus.getLang().equals("ar") && !aboutus.getLang().equals("en")) {
				map.put("status", 406);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", aboutus.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			String message = null;
			try {
				if (aboutus.getLang().equals("ar")) {
					q1 = entityManager.createQuery("select welcomeMsgAr from AppSetting");
					message = (String) q1.getSingleResult();
				} else if (aboutus.getLang().equals("en")) {
					q1 = entityManager.createQuery("select welcomeMsgEn from AppSetting");
					message = (String) q1.getSingleResult();
				}
			} catch (Exception NoResultException) {

			}
			map.put("status", 200);
			map.put("mssage", message);
			return map;

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/updateInfo")
	public @ResponseBody Map<String, Object> updateInfo(UpdateInfo updateInfo) throws Exception {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
//			if (updateInfo.getName() == null || updateInfo.getEmail() == null || updateInfo.getApiToken() == null
//					|| updateInfo.getCityId() == null) {
//				map.put("status", 403);
//				return map;
//			} else if (updateInfo.getApiToken().length() < 68) {
//				map.put("status", 405);
//				return map;
//
//			}
			if (updateInfo.getApiToken()==null){
				map.put("status", 403);
				return map;
			} else if (updateInfo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			if(updateInfo.getImage() != null){
				if(!utility.validate(updateInfo.getImage().getOriginalFilename().toString())){
					map.put("status", 408);
					return map;
				}
			}
//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//			if (!updateInfo.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 406);
//				return map;
//			}
			String regx3 = "^[\\p{L} .'-]+$";
			if(!updateInfo.getName().matches(regx3)){
				map.put("status", 407);
				return map;
			}
			
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", updateInfo.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
//			if( updateInfo.getEmail()!=null){
//			 q = entityManager.createQuery("from Chief where email=:em");
//			q.setParameter("em", updateInfo.getEmail());
//			Chief chie1 = null;
//			try {
//				chie1 = (Chief) q.getSingleResult();
//				if(!chie.getApiToken().equals(chie1.getApiToken())){
//					map.put("status", 409);
//					return map;
//				}
//			} catch (NoResultException ex) {
//				
//			}
//			
//			}
			
			City city = null;

			city = entityManager.find(City.class, updateInfo.getCityId());
			if (city == null) {
				map.put("status", 402);
				return map;
			}

			if (chie.getIsActive() == true) {
				chie.setName(updateInfo.getName());
//				try {
//					chie.setEmail(updateInfo.getEmail());
//				} catch (Exception e) {
//					map.put("status", 406);
//					return map;
//				}
				if (updateInfo.getImage() != null)
					//chie.setPic(utility.uploadFile(updateInfo.getImage()));
					//chie.setPic(utility.image(updateInfo.getImage()));
					chie.setPic(upload.uploadFile(updateInfo.getImage()));
				chie.setCity_id(city);
				entityManager.persist(chie);
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				return map;
			}
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/changePassword")
	public @ResponseBody Map<String, Object> changePassword(ChangePassword changePassword,
			@RequestBody(required = false) ChangePassword changePasswordBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (changePasswordBody != null) {
				changePassword = changePasswordBody;
			}
			if (changePassword.getOldPassword() == null || changePassword.getNewPassword() == null
					|| changePassword.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (changePassword.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;

			} else if (changePassword.getNewPassword().length() < 6) {
				map.put("status", 406);
				return map;
			}

			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", changePassword.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if (DefaultPasswordHasher.getInstance().isPasswordValid(changePassword.getOldPassword(),
					chie.getPassword())) {
				chie.setPassword(DefaultPasswordHasher.getInstance().hashPassword(changePassword.getNewPassword()));
				entityManager.persist(chie);
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getCites")
	public @ResponseBody Map<String, Object> getCities(GetCities getCities,
			@RequestBody(required = false) GetCities getCitiesBody) {

		Map<String, Object> map = new HashMap<>();
		try {
			if (getCitiesBody != null) {
				getCities = getCitiesBody;
			}
			if (getCities.getLang() == null || getCities.getPhone() == null) {
				map.put("status", 403);
				return map;
			}
			if (!getCities.getLang().equals("ar") && !getCities.getLang().equals("en")) {
				map.put("status", 406);
				return map;
			}
			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

//			if (!getCities.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}
			Query q = entityManager.createQuery("from Chief where phone=:em");
			q.setParameter("em", getCities.getPhone());

			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			List<Map<String, Object>> ret = new ArrayList<>();
			if (getCities.getLang().equals("ar")) {
				q = entityManager.createQuery("from City order by nameAr asc");
				List<City> cities = new ArrayList<>();
				cities = q.getResultList();
				for (City c : cities) {
					Map<String, Object> objectOneMap = new HashMap<>();

					objectOneMap.put("city_id", c.getId());
					objectOneMap.put("name", c.getNameAr());
					ret.add(objectOneMap);
				}
			} else if (getCities.getLang().equals("en")) {
				q = entityManager.createQuery("from City order by nameEn asc");
				List<City> cities = new ArrayList<>();
				cities = q.getResultList();
				for (City c : cities) {
					Map<String, Object> objectOneMap = new HashMap<>();
					objectOneMap.put("city_id", c.getId());
					objectOneMap.put("name", c.getNameEn());
					ret.add(objectOneMap);
				}
			}
			map.put("status", 200);
			map.put("cities", ret);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/setCity")
	public @ResponseBody Map<String, Object> setCity(GetCities getCities,
			@RequestBody(required = false) GetCities getCitiesBody) {

		Map<String, Object> map = new HashMap<>();
		try {
			if (getCitiesBody != null) {
				getCities = getCitiesBody;
			}
			if (getCities.getCityId() == null || getCities.getPhone() == null) {
				map.put("status", 403);
				return map;
			}

//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//
//			if (!getCities.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}

			Query q = entityManager.createQuery("from Chief where phone=:em");
			q.setParameter("em", getCities.getPhone());

			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			City city = null;
			
				city = entityManager.find(City.class, getCities.getCityId());
			if(city==null){
				map.put("status", 401);
				return map;
			}
			if(chie.getCity_id()!=null){
				map.put("status", 300);
				return map;
			}
			chie.setCity_id(city);
			entityManager.persist(chie);

			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/setSchedule")
	public @ResponseBody Map<String, Object> setSchedule(SetSchedule setSchedule,
			@RequestBody(required = false) SetSchedule setScheduleBody) {

		Map<String, Object> map = new HashMap<>();
		try {
			if (setScheduleBody != null) {
				setSchedule = setScheduleBody;
			}
			if ( setSchedule.getPhone() == null) {
				map.put("status", 403);
				return map;
			}
//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//
//			if (!setSchedule.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}

			Query q = entityManager.createQuery("from Chief where phone=:em");
			q.setParameter("em", setSchedule.getPhone());

			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
//			City cit = null;
//			
//				cit = entityManager.find(City.class, setSchedule.getScheduleId());
//			if(cit==null){
//				map.put("status", 401);
//				return map;
//			}
//			if (chie.getCity_id() == null) {
//				chie.setCity_id(cit);
//				entityManager.persist(chie);
//
//			} else {
//				map.put("status", 300);
//				return map;
//			}
			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getDishes")
	public @ResponseBody Map<String, Object> getDishes(GetDishes getDishes,
			@RequestBody(required = false) GetDishes getDishesBody) {

		Map<String, Object> map = new HashMap<>();
		try {
			if (getDishesBody != null) {
				getDishes = getDishesBody;
			}
			if (getDishes.getApiToken() == null || getDishes.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (getDishes.getApiToken().length()<68){
				map.put("status", 405);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", getDishes.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			q = entityManager.createQuery("from Dish where chief_id.id=:id order by createdAt desc");
			q.setParameter("id", chie.getId());
			q.setFirstResult((getDishes.getPage()) * PAGINATION_GET_HELP);
			q.setMaxResults(PAGINATION_GET_HELP);
			List<Map<String, Object>> ret = new ArrayList<>();
			List<Dish> dishes = new ArrayList<>();
			dishes = q.getResultList();
			for (Dish d : dishes) {
				Map<String, Object> oneObjectMap = new HashMap<>();
				oneObjectMap.put("dish_id", d.getId());
				oneObjectMap.put("name", d.getName());
				oneObjectMap.put("image_url", d.getPic());
				ret.add(oneObjectMap);
			}
			if (ret.isEmpty()) {
				map.put("status", 300);
				return map;
			}
			map.put("status", 200);
			map.put("dishes", ret);
			return map;
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/addDish")
	public @ResponseBody Map<String, Object> addDish(AddDish addDish) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {

			if (addDish.getApiToken() == null || addDish.getName() == null || addDish.getDescription() == null
					|| addDish.getPrice() == null||addDish.getImage()==null) {
				map.put("status", 403);
				return map;
			}
			if(addDish.getApiToken().length()<68){
				map.put("status", 405);
				return map;
			}
			String regx3 = "^[\\p{L} .'-]+$";
			if(!addDish.getName().matches(regx3)){
				map.put("status", 406);
				return map;
			}
			if(addDish.getImage() != null){
				if(!utility.validate(addDish.getImage().getOriginalFilename().toString())){
					map.put("status", 407);
					return map;
				}
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", addDish.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if (chie.getIsActive() == true) {
				Dish dish = new Dish();
				dish.setName(addDish.getName());
				//dish.setPic(utility.uploadFile(addDish.getImage()));
				//dish.setPic(utility.image(addDish.getImage()));
				dish.setPic(upload.uploadFile(addDish.getImage()));
				dish.setDescription(addDish.getDescription());
				dish.setPrice(addDish.getPrice());
				dish.setChief_id(chie);
				
				entityManager.persist(dish);
				// NotificationType notifi=new NotificationType();
				// notifi.setId(dish.getId());
				// notifi.setName("dish added");
				// entityManager.merge(notifi);
				// entityManager.persist(notifi);

				q = entityManager.createQuery("from NotificationType where name='dish added'");
				NotificationType notifiType = (NotificationType) q.getSingleResult();
				Notification notification = new Notification();
				notification.setDish_id(dish);
				notification.setNotificationType_id(notifiType);
				notification.setChief_id(chie);
				entityManager.persist(notification);
				for (int i = 0; i < chie.getUser_id().size(); i++) {
					// Notification
					// notification=entityManager.find(Notification.class, 1);
					NotifiedPerson person = new NotifiedPerson();
					User user = entityManager.find(User.class, chie.getUser_id().get(i).getId());
					person.setUser_id(user);
					person.setIsSeen(false);
					
					person.setNotification_id(notification);
					//person.setChief_id(chie);
					//person.setNotificationType_id(notifiType);

					entityManager.persist(person);
					// entityManager.persist(notification);
				}
			} else {
				map.put("status", 401);
				return map;
			}
			map.put("status", 200);
			return map;
		} catch (Exception e) {

			map.put("status", 404);
			//return map;
			 throw e;
		}
	}

	@Transactional
	@RequestMapping("/editDish")
	public @ResponseBody Map<String, Object> editDish(AddDish addDish) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {

			if (addDish.getApiToken() == null || addDish.getName() == null || addDish.getDescription() == null||
					addDish.getDishId()==null) {
				map.put("status", 403);
				return map;
			}
			if (addDish.getApiToken().length()<68){
				map.put("status", 405);
				return map;
			}
			if(addDish.getImage() != null){
				if(!utility.validate(addDish.getImage().getOriginalFilename().toString())){
					map.put("status", 407);
					return map;
				}
			}
			String regx3 = "^[\\p{L} .'-]+$";
			if(!addDish.getName().matches(regx3)){
				map.put("status", 406);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", addDish.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Dish dish = null;

			dish = entityManager.find(Dish.class, addDish.getDishId());
			if (dish == null) {
				map.put("status", 402);
				return map;
			}
			if(chie.getId()!=dish.getChief_id().getId()){
				map.put("status", 408);
				return map;
			}
			if (chie.getIsActive() == true) {

				dish.setName(addDish.getName());
				if (addDish.getImage() != null)
					//dish.setPic(utility.uploadFile(addDish.getImage()));
					//dish.setPic(utility.image(addDish.getImage()));
				dish.setPic(upload.uploadFile(addDish.getImage()));
				dish.setDescription(addDish.getDescription());
				dish.setPrice(addDish.getPrice());
				dish.setChief_id(chie);
				entityManager.persist(dish);
			} else {
				map.put("status", 401);
				return map;
			}
			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/deleteDish")
	public @ResponseBody Map<String, Object> deleteDish(AddDish addDish,
			@RequestBody(required = false) AddDish addDishBody) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {
			if (addDishBody != null) {
				addDish = addDishBody;
			}
			if (addDish.getApiToken() == null || addDish.getDishId() == null) {
				map.put("status", 403);
				return map;
			}
			if (addDish.getApiToken().length()<68){
				map.put("status", 405);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", addDish.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Dish dish = null;

			dish = entityManager.find(Dish.class, addDish.getDishId());
			if (dish == null) {
				map.put("status", 402);
				return map;

			}
			if(chie.getId()!=dish.getChief_id().getId()){
				map.put("status", 406);
				return map;
			}
			if (chie.getIsActive() == true) {

				dish.setChief_id(null);
				entityManager.persist(dish);
				q = entityManager.createQuery("update Theorder set dish_id=null where dish_id.id=:di");
				q.setParameter("di", dish.getId());
				q.executeUpdate();

				// q = entityManager.createQuery("update Notification set
				// dish_id=null where dish_id.id=:di");

//				q = entityManager
//						.createQuery("update NotifiedPerson set notification_id=null where notificationType_id.id=:di");
//				q.setParameter("di", dish.getId());
//				q.executeUpdate();
				q = entityManager.createQuery(
						"select notifiedPerson from NotifiedPerson notifiedPerson join notifiedPerson.notification_id notifi where notifi.dish_id.id=:di");
				q.setParameter("di", dish.getId());
				List<NotifiedPerson> notifications = q.getResultList();
				for (NotifiedPerson n : notifications) {
					entityManager.remove(n);
					entityManager.flush();
				}

				// q = entityManager.createQuery("delete from Notification a
				// inner join b NotifiedPerson on a.id=b.notification_id where
				// a.dish_id.id=:di");
				// q.setParameter("di", dish.getId());
				// q.executeUpdate();

				q = entityManager.createQuery("delete from Notification where dish_id.id=:di");
				q.setParameter("di", dish.getId());
				q.executeUpdate();
//				q = entityManager.createQuery("delete from NotifiedPerson where notificationType_id.id=:di");
//				q.setParameter("di", dish.getId());
//				q.executeUpdate();

				q = entityManager.createQuery("select use from User use join use.dish_id dish where dish.id=:di");
				q.setParameter("di", dish.getId());
				List<User> users = q.getResultList();
				for (User u : users) {
					u.setDish_id(null);
					entityManager.persist(u);
				}

				q = entityManager.createQuery("delete from Dish where id=:di");
				q.setParameter("di", dish.getId());
				q.executeUpdate();
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {

			map.put("status", 404);
			//return map;
			 throw e;
		}
	}

	@Transactional
	@RequestMapping("/getWaitingOrders")
	public @ResponseBody Map<String, Object> getWaitingOrders(GetOrders getOrders,
			@RequestBody(required = false) GetOrders getOrdersBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getOrdersBody != null) {
				getOrders = getOrdersBody;
			}
			if (getOrders.getApiToken() == null || getOrders.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (getOrders.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", getOrders.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager
					.createQuery("from Theorder where chief_id.id=:id and isAccepted='انتظار' order by createdAt desc");
			q1.setParameter("id", chie.getId());
			q1.setFirstResult((getOrders.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Theorder> orders = new ArrayList<>();
			List<Map<String, Object>> ret = new ArrayList<>();
			orders = q1.getResultList();
			for (Theorder o : orders) {
				Map<String, Object> onemap = new HashMap<>();
				try{
				
				onemap.put("order_id", o.getId());
				onemap.put("order_date", o.getCreatedAt());
				onemap.put("dish_name", o.getDish_id().getName());
				onemap.put("chief_name", o.getChief_id().getName());
				onemap.put("chief_image_url", o.getChief_id().getPic());
				}catch(Exception e){
					onemap.put("dish_name", null);
					onemap.put("chief_name", null);
					onemap.put("chief_image_url", null);
				}
				ret.add(onemap);
			}
			if (ret.isEmpty()) {
				map2.put("status", 300);
				return map2;
			}
			map2.put("status", 200);
			map2.put("orders", ret);
			return map2;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getAcceptedOrders")
	public @ResponseBody Map<String, Object> getAcceptedOrders(GetOrders getOrders,
			@RequestBody(required = false) GetOrders getOrdersBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getOrdersBody != null) {
				getOrders = getOrdersBody;
			}
			if (getOrders.getApiToken() == null || getOrders.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (getOrders.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", getOrders.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager
					.createQuery("from Theorder where chief_id.id=:id and isAccepted='مقبول' order by createdAt desc");
			q1.setParameter("id", chie.getId());
			q1.setFirstResult((getOrders.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Theorder> orders = new ArrayList<>();
			List<Map<String, Object>> ret = new ArrayList<>();
			orders = q1.getResultList();
			for (Theorder o : orders) {
				Map<String, Object> onemap = new HashMap<>();
				try{
				
					onemap.put("order_id", o.getId());
					onemap.put("order_date", o.getCreatedAt());
					onemap.put("dish_name", o.getDish_id().getName());
					onemap.put("user_name", o.getUser_id().getName());
					onemap.put("user_image_url", o.getUser_id().getPic());
				}catch(Exception e){
					onemap.put("dish_name", null);
					onemap.put("chief_name", null);
					onemap.put("chief_image_url", null);
				}
				ret.add(onemap);
			}
			if (ret.isEmpty()) {
				map2.put("status", 300);
				return map2;
			}
			map2.put("status", 200);
			map2.put("orders", ret);
			return map2;
		} catch (Exception e) {

			map2.put("status", 404);
			//return map2;
			 throw e;
		}
	}

	@Transactional
	@RequestMapping("/getRefusedOrders")
	public @ResponseBody Map<String, Object> getRefusedOrders(GetOrders getOrders,
			@RequestBody(required = false) GetOrders getOrdersBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getOrdersBody != null) {
				getOrders = getOrdersBody;
			}
			if (getOrders.getApiToken() == null || getOrders.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (getOrders.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", getOrders.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager
					.createQuery("from Theorder where chief_id.id=:id and isAccepted='مرفوض' order by createdAt desc");
			q1.setParameter("id", chie.getId());
			q1.setFirstResult((getOrders.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Theorder> orders = new ArrayList<>();
			List<Map<String, Object>> ret = new ArrayList<>();
			orders = q1.getResultList();
			for (Theorder o : orders) {
				Map<String, Object> onemap = new HashMap<>();
				try{
					onemap.put("order_id", o.getId());
					onemap.put("order_date", o.getCreatedAt());
					onemap.put("dish_name", o.getDish_id().getName());
					onemap.put("chief_name", o.getDish_id().getChief_id().getName());
					onemap.put("chief_image_url", o.getDish_id().getChief_id().getPic());
			}catch(Exception e){
				onemap.put("dish_name", null);
				onemap.put("chief_name", null);
				onemap.put("chief_image_url", null);
			}
				ret.add(onemap);
			}
			if (ret.isEmpty()) {
				map2.put("status", 300);
				return map2;
			}
			map2.put("status", 200);
			map2.put("orders", ret);
			return map2;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getOrder")
	public @ResponseBody Map<String, Object> getOrder(GetOrder getOrder,
			@RequestBody(required = false) GetOrder getOrderBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getOrderBody != null) {
				getOrder = getOrderBody;
			}
			if (getOrder.getApiToken() == null || getOrder.getOrderId() == null) {
				map.put("status", 403);
				return map;
			}
			if (getOrder.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", getOrder.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if (chie.getIsActive() == true) {
//				q1 = entityManager.createQuery(
//						"select to from Theorder to join to.user_id user join user.chief_id chief where to.id=:oi and chief.id=:ci");
				q1 = entityManager.createQuery("from Theorder where id=:oi");
				q1.setParameter("oi", getOrder.getOrderId());
				//q1.setParameter("ci", chie.getId());
				Theorder theorder = null;
				try {
					theorder = (Theorder) q1.getSingleResult();
				} catch (NoResultException ex) {
					map.put("status", 402);
					return map;
				}
				try{
					map.put("status", 200);
					map.put("user_name", theorder.getUser_id().getName());
					map.put("chief_name", theorder.getChief_id().getName());
					map.put("user_image", theorder.getUser_id().getPic());
				    map.put("dish_name", theorder.getDish_id().getName());
					map.put("dish_image", theorder.getDish_id().getPic());
					map.put("quantity", theorder.getQuantity());
					map.put("total_prize", theorder.getDish_id().getPrice());
					
				} catch (Exception e) {
					map.put("user_name", null);
					map.put("chief_name", null);
					map.put("user_image", null);
				    map.put("dish_name", null);
					map.put("dish_image", null);
					map.put("quantity", null);
					map.put("total_prize", null);
				}
				map.put("order_date", theorder.getCreatedAt());
				return map;
			} else {
				map.put("status", 401);
				return map;
			}
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/acceptOrder")
	public @ResponseBody Map<String, Object> acceptOrder(GetOrder getOrder,
			@RequestBody(required = false) GetOrder getOrderBody) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {
			if (getOrderBody != null) {
				getOrder = getOrderBody;
			}
			if (getOrder.getApiToken() == null || getOrder.getOrderId() == null) {
				map.put("status", 403);
				return map;
			}
			if (getOrder.getApiToken().length() < 68) {
				map.put("status", 406);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", getOrder.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Theorder theorder = null;

			theorder = entityManager.find(Theorder.class, getOrder.getOrderId());
			if (theorder == null) {
				map.put("status", 402);
				return map;

			}

			if (theorder.getIsAccepted().equals("مرفوض")) {
				map.put("status", 405);
				return map;
			}
			if (chie.getIsActive() == true) {
				q = entityManager.createQuery(
						"update Theorder set isAccepted='مقبول' " + "where id=:id and isAccepted='انتظار'");
				q.setParameter("id", getOrder.getOrderId());
				int modification = q.executeUpdate();

				q = entityManager.createQuery("from NotificationType where name='order accepted'");
				NotificationType notifiType = (NotificationType) q.getSingleResult();
				Notification notification = new Notification();
				notification.setOrder_id(theorder);
				notification.setChief_id(chie);
				notification.setNotificationType_id(notifiType);
				entityManager.persist(notification);
				//for (int i = 0; i < chie.getUser_id().size(); i++) {
					// Notification
					// notification=entityManager.find(Notification.class, 1);
					NotifiedPerson person = new NotifiedPerson();
					User user = entityManager.find(User.class, theorder.getUser_id().getId());
					person.setUser_id(user);
					person.setIsSeen(false);
					person.setNotification_id(notification);
					//person.setChief_id(chie);
					//person.setNotificationType_id(notifiType);

					entityManager.persist(person);
					// entityManager.persist(notification);
				//}
				if (modification != 0) {

					map.put("status", 200);
					return map;
				}

			} else {
				map.put("status", 401);
				return map;
			}
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
		map.put("status", 200);
		return map;
	}

	@Transactional
	@RequestMapping("/refuseOrder")
	public @ResponseBody Map<String, Object> refuseOrder(GetOrder getOrder,
			@RequestBody(required = false) GetOrder getOrderBody) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {
			if (getOrderBody != null) {
				getOrder = getOrderBody;
			}
			if (getOrder.getApiToken() == null || getOrder.getOrderId() == null) {
				map.put("status", 403);
				return map;
			}
			if (getOrder.getApiToken().length() < 68) {
				map.put("status", 406);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", getOrder.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Theorder theorder = null;

			theorder = entityManager.find(Theorder.class, getOrder.getOrderId());
			if (theorder == null) {
				map.put("status", 402);
				return map;

			}
			if (chie.getIsActive() == true) {
				q = entityManager.createQuery(
						"update Theorder set isAccepted='مرفوض' " + "where id=:id and isAccepted='انتظار'");
				q.setParameter("id", getOrder.getOrderId());
				int modification = q.executeUpdate();
				// NotificationType notifi=new NotificationType();
				// notifi.setId(theorder.getId());
				// notifi.setName("order refused");
				// entityManager.merge(notifi);
				// // entityManager.persist(notifi);
				//
				// for(int i=0;i<chie.getUser_id().size();i++){
				// Notification
				// notification=entityManager.find(Notification.class,
				// notifi.getId());
				// NotifiedPerson person=new NotifiedPerson();
				// User user=entityManager.find(User.class,
				// chie.getUser_id().get(i).getId());
				// person.setUser_id(user);
				// person.setIsSeen(false);
				// person.setNotification_id(notification);
				// person.setChief_id(chie);
				// person.setNotificationType_id(notifi);
				// person.setNotification_id(notification);
				// entityManager.merge(person);
				// }
				q = entityManager.createQuery("from NotificationType where name='order refused'");
				NotificationType notifiType = (NotificationType) q.getSingleResult();
				Notification notification = new Notification();
				notification.setOrder_id(theorder);
				notification.setNotificationType_id(notifiType);
				notification.setChief_id(chie);
				entityManager.persist(notification);
				//for (int i = 0; i < chie.getUser_id().size(); i++) {
					// Notification
					// notification=entityManager.find(Notification.class, 1);
					NotifiedPerson person = new NotifiedPerson();
					User user = entityManager.find(User.class, theorder.getUser_id().getId());
					person.setUser_id(user);
					person.setIsSeen(false);
					person.setNotification_id(notification);
					//person.setChief_id(chie);
					//person.setNotificationType_id(notifiType);

					entityManager.persist(person);
					// entityManager.persist(notification);
				//}
				if (modification != 0) {
					map.put("status", 200);
					return map;

				}
				if (theorder.getIsAccepted().equals("مقبول")) {
					map.put("status", 405);
					return map;
				}

			} else {
				map.put("status", 401);
				return map;
			}
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
		map.put("status", 200);
		return map;
	}

	@Transactional
	@RequestMapping("/addVideo")
	public @ResponseBody Map<String, Object> addVideo(AddVideo addVideo) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {

			if (addVideo.getApiToken() == null || addVideo.getImage() == null || addVideo.getName() == null
					|| addVideo.getVideo() == null) {
				map.put("status", 403);
				return map;
			}
			if (addVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			if(addVideo.getImage() != null){
				if(!utility.validate(addVideo.getImage().getOriginalFilename().toString())){
					map.put("status", 406);
					return map;
				}
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", addVideo.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			
			if (chie.getIsActive() == true) {
				Video video = new Video();
				video.setChief_id(chie);
				video.setName(addVideo.getName());
				//video.setPic(utility.uploadFile(addVideo.getImage()));
				//video.setSrc(utility.uploadFile(addVideo.getVideo()));
				//video.setPic(utility.image(addVideo.getImage()));
				video.setPic(upload.uploadFile(addVideo.getImage()));
				//video.setSrc(utility.image(addVideo.getVideo()));
						video.setSrc(upload.uploadFile(addVideo.getVideo()));
				entityManager.persist(video);
				q = entityManager.createQuery("from NotificationType where name='video added'");
				NotificationType notifiType = (NotificationType) q.getSingleResult();
				Notification notification = new Notification();
				notification.setVideo_id(video);
				notification.setNotificationType_id(notifiType);
				notification.setChief_id(chie);
				entityManager.persist(notification);
				for (int i = 0; i < chie.getUser_id().size(); i++) {
					// Notification
					// notification=entityManager.find(Notification.class, 1);
					NotifiedPerson person = new NotifiedPerson();
					User user = entityManager.find(User.class, chie.getUser_id().get(i).getId());
					person.setUser_id(user);
					person.setIsSeen(false);
					person.setNotification_id(notification);
					//person.setChief_id(chie);
					//person.setNotificationType_id(notifiType);

					entityManager.persist(person);
					// entityManager.persist(notification);
				}
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {

			map.put("status", 404);
			// return map;
			throw e;
		}
	}

	@Transactional
	@RequestMapping("/editVideo")
	public @ResponseBody Map<String, Object> editVideo(AddVideo addVideo) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {

			if (addVideo.getApiToken() == null || addVideo.getName() == null) {
				map.put("status", 403);
				return map;
			}
			if (addVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			if(addVideo.getImage() != null){
				if(!utility.validate(addVideo.getImage().getOriginalFilename().toString())){
					map.put("status", 406);
					return map;
				}
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", addVideo.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Video video = null;
			try {
				video = entityManager.find(Video.class, addVideo.getVideoId());
			} catch (Exception e) {
				//map.put("status", "there is no such video");
				map.put("status", 333);
				return map;
			}
			if(chie.getId()!=video.getChief_id().getId()){
				map.put("status", 402);
				return map;
			}
			if (chie.getIsActive() == true) {
				video.setChief_id(chie);
				video.setName(addVideo.getName());
				if (addVideo.getImage() != null)
					//video.setPic(utility.uploadFile(addVideo.getImage()));
				//video.setPic(utility.image(addVideo.getImage()));
					video.setPic(upload.uploadFile(addVideo.getImage()));
				if (addVideo.getVideo() != null)
					//video.setSrc(utility.uploadFile(addVideo.getVideo()));
				//video.setSrc(utility.image(addVideo.getVideo()));
					video.setSrc(upload.uploadFile(addVideo.getVideo()));
				entityManager.persist(video);
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/deleteVideo")
	public @ResponseBody Map<String, Object> deleteVideo(AddVideo addVideo,
			@RequestBody(required = false) AddVideo addVideoBody) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {
			if (addVideoBody != null) {
				addVideo = addVideoBody;
			}
			if (addVideo.getApiToken() == null || addVideo.getVideoId() == null) {
				map.put("status", 403);
				return map;
			}
			if (addVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", addVideo.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Video video = null;

			video = entityManager.find(Video.class, addVideo.getVideoId());
			 if(video==null) {
			 map.put("status", 404);
			 return map;
			
			 }
			if(chie.getId()!=video.getChief_id().getId()){
				map.put("status", 402);
				return map;
			}
			if (chie.getIsActive() == true) {
				video.setChief_id(null);
				entityManager.persist(video);

				 q = entityManager.createNativeQuery(
						"delete np from notified_person np INNER JOIN notification n where n.id=np.notification_id_id and n.video_id_id=:di and np.chief_id_id=:ui");
				q.setParameter("di", addVideo.getVideoId());
				q.setParameter("ui", chie.getId());
				q.executeUpdate();
				q = entityManager.createNativeQuery("delete from notification where id NOT IN (select notification_id_id from notified_person)");
				q.executeUpdate();
//				q = entityManager
//						.createQuery("update NotifiedPerson set notification_id=null where notificationType_id.id=:di");
//				q.setParameter("di", video.getId());
//				q.executeUpdate();
//				q = entityManager.createQuery("delete from Notification where video_id.id=:di");
//				q.setParameter("di", video.getId());
//				q.executeUpdate();
				// q = entityManager.createQuery("delete from NotifiedPerson
				// where notificationType_id.id=:di");
				// q.setParameter("di", video.getId());
				// q.executeUpdate();
				q = entityManager.createQuery(
						"select notifiedPerson from NotifiedPerson notifiedPerson join notifiedPerson.notification_id notifi where notifi.video_id.id=:di");
				q.setParameter("di", video.getId());
				List<NotifiedPerson> notifications = q.getResultList();
				for (NotifiedPerson n : notifications) {
					entityManager.remove(n);
					entityManager.flush();
				}
				// q = entityManager.createQuery("update Notification set
				// video_id=null where video_id.id=:di");
				// q.setParameter("di", video.getId());
				// q.executeUpdate();

				q = entityManager.createQuery("delete from Video where id=:di");
				q.setParameter("di", video.getId());
				q.executeUpdate();
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getVideo")
	public @ResponseBody Map<String, Object> getVideo(AddVideo addVideo,
			@RequestBody(required = false) AddVideo addVideoBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (addVideoBody != null) {
				addVideo = addVideoBody;
			}
			if (addVideo.getApiToken() == null || addVideo.getVideoId() == null) {
				map.put("status", 403);
				return map;
			}
			if (addVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", addVideo.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if (chie.getIsActive() == true) {
//				q1 = entityManager
//						.createQuery("select v from Video v join v.chief_id chief where v.id=:vi and chief.id=:ci");
				q1 = entityManager.createQuery("from Video where id=:vi");
				q1.setParameter("vi", addVideo.getVideoId());
				//q1.setParameter("ci", chie.getId());
				Video video = null;
				try {
					video = (Video) q1.getSingleResult();
				} catch (NoResultException e) {
					map.put("status", 401);
					return map;
				}
				if(chie.getId()!=video.getChief_id().getId()){
					map.put("status", 402);
					return map;
				}
				map.put("status", 200);
				map.put("name", video.getName());
				map.put("video", video.getSrc());
				map.put("image", video.getPic());
				map.put("date", video.getCreatedAt());
				return map;
			} else {
				map.put("status",111);
				return map;
			}
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getVideos")
	public @ResponseBody Map<String, Object> getVideos(AddVideo addVideo,
			@RequestBody(required = false) AddVideo addVideoBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (addVideoBody != null) {
				addVideo = addVideoBody;
			}
			if (addVideo.getApiToken() == null || addVideo.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (addVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", addVideo.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager.createQuery("from Video where chief_id.id=:ci order by createdAt desc");
			q1.setParameter("ci", chie.getId());
			q1.setFirstResult((addVideo.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Video> videos = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();

			for (Video v : videos) {
				Map<String, Object> oneObjectMap = new HashMap();
				oneObjectMap.put("video_id", v.getId());
				oneObjectMap.put("name", v.getName());
				oneObjectMap.put("date", v.getCreatedAt());
				oneObjectMap.put("image_url", v.getPic());
				ret.add(oneObjectMap);
			}
			if (ret.isEmpty()) {
				map.put("status", 300);
				return map;
			}
			map.put("status", 200);
			map.put("videos", ret);
			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getChief")
	public @ResponseBody Map<String, Object> getChief(AddVideo addVideo,
			@RequestBody(required = false) AddVideo addVideoBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (addVideoBody != null) {
				addVideo = addVideoBody;
			}
			if (addVideo.getApiToken() == null||addVideo.getLang()==null) {
				map.put("status", 403);
				return map;
			}
			
			if (addVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", addVideo.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			try{
				if(chie.getIsActive()==true){
					try {
						map.put("status", 200);
						map.put("name", chie.getName());
						map.put("Phone", chie.getPhone());
						map.put("image_url", chie.getPic());
						map.put("num_followers", chie.getUser_id().size());
						map.put("email", chie.getEmail());
						if ("ar".equals(addVideo.getLang()))
							
								map.put("city", chie.getCity_id().getNameAr());
								if ("en".equals(addVideo.getLang()))
									map.put("city", chie.getCity_id().getNameEn());
							} catch (Exception e) {
								map.put("city", null);
							}
						
				}	
			}catch(Exception e){
				
				}
			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getNotifications")
	public @ResponseBody Map<String, Object> getNotifications(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			if (pagination.getApiToken() == null || pagination.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (pagination.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			q1 = entityManager.createQuery("from NotifiedPerson where chief_id.id=:ci order by createdAt desc");
			q1.setParameter("ci", chie.getId());
			q1.setFirstResult((pagination.getPage()) * 10);
			q1.setMaxResults(10);
			List<NotifiedPerson> notifications = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();

			for (NotifiedPerson n : notifications) {
				Map<String, Object> oneObjectMap = new HashMap<>();
				oneObjectMap.put("notification_id", n.getNotification_id().getId());
//				if( n.getNotification_id().getNotificationType_id().getId()==6)
//					oneObjectMap.put("notification_id",1);
//					if( n.getNotification_id().getNotificationType_id().getId()==5)
//						oneObjectMap.put("notification_id",2);
//					if( n.getNotification_id().getNotificationType_id().getId()==7)
//						oneObjectMap.put("notification_id",3);
				if( n.getNotification_id().getNotificationType_id().getId()==6)
					oneObjectMap.put("notification_type",1);
					if( n.getNotification_id().getNotificationType_id().getId()==5)
						oneObjectMap.put("notification_type",2);
					if( n.getNotification_id().getNotificationType_id().getId()==7)
						oneObjectMap.put("notification_type",3);
				oneObjectMap.put("notification_date", n.getNotification_id().getCreatedAt());
				oneObjectMap.put("is_seen", n.getIsSeen());
				if(n.getNotification_id().getUser_id().getName()==null)
					oneObjectMap.put("user_name", null);
				else
					oneObjectMap.put("user_name", n.getNotification_id().getUser_id().getName());
				if(n.getNotification_id().getUser_id().getPic()==null)
					oneObjectMap.put("user_image_url", null);
				else
				    oneObjectMap.put("user_image_url", n.getNotification_id().getUser_id().getPic());
			//	oneObjectMap.put("item_id", n.getNotification_id().getNotificationType_id().getId());
				if( n.getNotification_id().getNotificationType_id().getId()==6)
					oneObjectMap.put("item_id",n.getNotification_id().getDish_id().getId());
				if( n.getNotification_id().getNotificationType_id().getId()==5)
						oneObjectMap.put("item_id",n.getUser_id().getId());
				if( n.getNotification_id().getNotificationType_id().getId()==7)
						oneObjectMap.put("item_id",n.getNotification_id().getOrder_id().getId());
				
				
				ret.add(oneObjectMap);
				n.setIsSeen(true);
				entityManager.persist(n);
			}
			if (ret.isEmpty()) {
				map.put("status", 300);
				return map;
			}
			map.put("status", 200);
			map.put("notifications", ret);

			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			 //throw e;
		}
	}

	@Transactional
	@RequestMapping("/getMessages")
	public @ResponseBody Map<String, Object> getMessages(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			if (pagination.getApiToken() == null || pagination.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (pagination.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			q1 = entityManager.createQuery("from Message where chief_id.id=:ci order by createdAt desc");
			q1.setParameter("ci", chie.getId());
			q1.setFirstResult((pagination.getPage()) * 10);
			q1.setMaxResults(10);
			List<Message> messages = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();

Integer[] useArr= new Integer[messages.size()];
			
			int usefId=0;
			int count=0,found=0;
			for (Message m : messages) {
				found=0;
				useArr[count]=m.getUser_id().getId();
				for(int i=0;i<useArr.length;i++){
					if(m.getUser_id().getId()==useArr[i]){
						found++;
					}
					if(found>2){
						break;
					}
				}
				Map<String, Object> oneObjectMap = new HashMap<>();
				if(found<=1){
				oneObjectMap.put("user_id", m.getUser_id().getId());
				oneObjectMap.put("message_date", m.getCreatedAt());
				if (m.getMsgContent().length() > 50)
					oneObjectMap.put("message_content", m.getMsgContent().substring(0, 50));
				else 
					oneObjectMap.put("message_content", m.getMsgContent());
					if( m.getIsSeen()==true)
						oneObjectMap.put("is_seen", 1);
					else if(m.getIsSeen()==false)
						oneObjectMap.put("is_seen",0);
					else
						oneObjectMap.put("is_seen", null);
					if(m.getIsUserSender()==true)
						oneObjectMap.put("sender_type",0);
					if(m.getIsUserSender()==false)
						oneObjectMap.put("sender_type",1);
					if(m.getIsUserSender()==null)
						oneObjectMap.put("sender_type",null);
					oneObjectMap.put("user_name", m.getUser_id().getName());
					oneObjectMap.put("user_image_url",m.getUser_id().getPic());
					// m.getChief_id().getPic());
					// oneObjectMap.put("user_name", m.getChief_id().getName());
					ret.add(oneObjectMap);
				}
				
				usefId=m.getUser_id().getId();
				count++;
			
				}
			
			if (ret.isEmpty()) {
				map.put("status", 300);
				return map;
			}
			map.put("status", 200);
			map.put("messages", ret);
			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getUserMessages")
	public @ResponseBody Map<String, Object> getUserMessages(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			if (pagination.getApiToken() == null || pagination.getPage() == null || pagination.getUserId() == null) {
				map.put("status", 403);
				return map;
			}
			if (pagination.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			User use=null;
			use=entityManager.find(User.class, pagination.getUserId());
			if(use==null){
				map.put("status", 401);
				return map;
			}
			q1 = entityManager.createQuery(
					"from Message where chief_id.id=:ci and user_id.id=:ui order by createdAt desc");
			q1.setParameter("ci", chie.getId());
			q1.setParameter("ui", pagination.getUserId());
			q1.setFirstResult((pagination.getPage()) * 15);
			q1.setMaxResults(15);
			List<Message> messages = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			
//			Integer[] useArr=new Integer[messages.size()];
//			Integer[] chieArr=new Integer[messages.size()];
			
			int count=0,found=0;
			for (Message m : messages) {
//				found=0;
//				chieArr[count]=m.getChief_id().getId();
//				useArr[count]=m.getUser_id().getId();
//				for(int i=0;i<useArr.length;i++){
//					if(m.getChief_id().getId()==chieArr[i]&&m.getUser_id().getId()==useArr[i]){
//						found++;
//					}
//					if(found>3){
//						break;
//					}
//				}
					Map<String, Object> oneObjectMap = new HashMap<>();
//					if(found<=1){
				
//				oneObjectMap.put("user_id", m.getUser_id().getId());
//				oneObjectMap.put("message_date", m.getCreatedAt());
				if (m.getMsgContent().length() > 50)
					oneObjectMap.put("message_content", m.getMsgContent().substring(0, 50));
				else
					oneObjectMap.put("message_content", m.getMsgContent());
				
				//oneObjectMap.put("is_seen", m.getIsSeen());
				if(m.getIsUserSender()==true)
					oneObjectMap.put("sender_type",0);
					if(m.getIsUserSender()==false)
						oneObjectMap.put("sender_type",1);
					if(m.getIsUserSender()==null)
						oneObjectMap.put("sender_type",null);
//				oneObjectMap.put("user_name", m.getUser_id().getName());
//				oneObjectMap.put("user_image_url", m.getUser_id().getPic());
//				oneObjectMap.put("user_name", m.getChief_id().getName());
				ret.add(oneObjectMap);
			//	count++;
			//		}
			}
			if (ret.isEmpty()) {
				map.put("status", 300);
				return map;
			}
			map.put("status", 200);
			map.put("messages", ret);
			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getBill")
	public @ResponseBody Map<String, Object> getBill(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			if (pagination.getApiToken() == null) {
				map.put("status", 403);
				return map;
			}
			if (pagination.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			
		//	q1 = entityManager.createQuery("select s from Chief c join c.schedule_id s where c.id=:ci order by s.createdAt desc)");
			q1 = entityManager.createQuery("from Bill where chiefId=:ci order by createdAt desc)");
			q1.setParameter("ci", chie.getId());
			q1.setFirstResult(0);
			q1.setMaxResults(1);
			Bill b=(Bill) q1.getSingleResult();
//			Double s=null;
//			List list=new ArrayList<>();
//			list=q1.getResultList();
//			//Schedule s=list.get(0);
//			for(Object b:list){
//				s=((Schedule) b).getCost();
//			}
//		Schedule s=null;
//			try{
//				 s =  (Schedule) q1.getSingleResult();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			int last=s.getChiefs().size()-1;
//			if(chie.getId()!=s.getChiefs().get(last).getId()){
//				map.put("status", 402);
//				return map;
//			}
			map.put("status", 200);
			map.put("cost", b.getCost());
			return map;

		} catch (Exception e) {
			map2.put("status", 404);
			//return map2;
			 throw e;
		}
	}

	@Transactional
	@RequestMapping("/updatePhone")
	public @ResponseBody Map<String, Object> updatePhone(Chief chief, @RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}
			if (chief.getPhone() == null || chief.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (chief.getPhone().length() < 9) {
				map.put("status", 406);
				return map;
			} else if (chief.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			String regex = "[0-9*#+() -]*";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(chief.getPhone());

	        if (!matcher.matches()) {
	        	map.put("status", 406);
				return map;
	        }
			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", chief.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q = entityManager.createQuery("from Chief where tempPhone=:ph");
			q.setParameter("ph", chief.getPhone());
			Chief chie2 = null;
			try {
				chie2 = (Chief) q.getSingleResult();
				map.put("status", 401);
				return map;
			} catch (NoResultException ex) {
				
			}
			q = entityManager.createQuery("from Chief where phone=:ph");
			q.setParameter("ph", chief.getPhone());
			Chief chie1 = null;
			try {
				chie1 = (Chief) q.getSingleResult();
				map.put("status", 401);
				return map;
			} catch (NoResultException ex) {
				// chie1.setTempPhone(chief.getPhone());
				// entityManager.persist(chie1);
				// /////////////////////
				// ////// send sms//////
				// ///////////////////
				// map.put("status", 200);
				// return map;
			}
			if (chie1 == null) {
				SecureRandom rand = new SecureRandom();
				 int num = rand.nextInt(100000);
				 String formatted = String.format("%05d", num);
				 int verificationCode1 = Integer.valueOf(formatted);
				//int verificationCode1 = 12345;
				
				chie.setVerificationCode(verificationCode1);
				chie.setTempPhone(chief.getPhone());
				entityManager.persist(chie);
apiSend= new ApiSendMessage();
				
				apiSend.setUserName(new String(userName));
				apiSend.setNumbers(prepareTelephoneNumber(chief.getPhone()));
				apiSend.setMessage("Your Verification Code Is : "+verificationCode1);
				apiSend.setPassword("1475321");
				apiSend.setSender("admin");
//				SimpleDateFormat timeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//				apiSend.setDatetime(timeDate.format(new Date()));
				apiSend.setUnicode("E");
				apiSend.setReturnValue("full");
				
				

				sendMessageApi.sendSms(apiSend);
				map.put("status", 200);

			}
			return map;
		} catch (Exception e) {

			map.put("status", 404);
			// return map;
			throw e;
		}

	}

	@Transactional
	@RequestMapping("/validatePhone")
	public @ResponseBody Map<String, Object> validatePhone(Chief chief,
			@RequestBody(required = false) Chief chiefBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (chiefBody != null) {
				chief = chiefBody;
			}
			if (chief.getVerificationCode() == null || chief.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (chief.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}

			Query q = entityManager.createQuery("from Chief where apiToken=:api");
			q.setParameter("api", chief.getApiToken());
			Chief chie = null;
			try {
				chie = (Chief) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			try {
				if (chief.getVerificationCode().equals(chie.getVerificationCode()) && chie.getTempPhone() != null) {
					chie.setPhone(chie.getTempPhone());
					chie.setVerificationCode(null);
					chie.setTempPhone(null);
					entityManager.persist(chie);
					map.put("status", 200);
					return map;
				} else if (!chief.getVerificationCode().equals(chie.getVerificationCode())) {
					map.put("status", 401);
					return map;
				}
			} catch (Exception e) {
				//map.put("status", "veryfication code is null");
				map.put("status",333);
				return map;
			}
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
		//map.put("status", "tempPhone is null");
		map.put("status", 444);
		return map;
	}

	@Transactional
	@RequestMapping("/getDish")
	public @ResponseBody Map<String, Object> getDish(GetDish getDish,
			@RequestBody(required = false) GetDish getDishBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getDishBody != null) {
				getDish = getDishBody;
			}
			if (getDish.getApiToken() == null || getDish.getDishId() == null) {
				map.put("status", 403);
				return map;
			}
			if (getDish.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			Chief chie = null;
			Query q1 = entityManager.createQuery("from Chief where apiToken=:ap");
			q1.setParameter("ap", getDish.getApiToken());
			try {
				chie = (Chief) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Dish dish = null;

			dish = entityManager.find(Dish.class, getDish.getDishId());
			if (dish == null) {
				map.put("status", 401);
				return map;
			}
			if(dish.getChief_id().getId()!=chie.getId()){
				map.put("status", 402);
				return map;
			}
			// for (int i = 0; i < use1.getDish_id().size(); i++) {
			// if
			// (use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId()))
			// {
			// // map.put("chief_id", use1.getDish_id().get(i).getId());
			// map.put("status", 200);
			// map.put("name", use1.getDish_id().get(i).getName());
			// map.put("description",
			// use1.getDish_id().get(i).getDescription());
			// map.put("price", use1.getDish_id().get(i).getPrice());
			// map.put("image_url", use1.getDish_id().get(i).getPic());
			// break;
			// }
			// }
			map.put("status", 200);
			map.put("name", dish.getName());
			map.put("description", dish.getDescription());
			map.put("price", dish.getPrice());
			map.put("image_url", dish.getPic());
			
			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

}
