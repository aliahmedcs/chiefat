package com.magdsoft.ws.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.magdsoft.ws.form.ApiSendMessage;
import com.magdsoft.ws.password.DefaultPasswordHasher;
import com.magdsoft.ws.utility.Upload;
import com.magdsoft.ws.utility.Utility;
import com.magdsoft.ws.model.AcceptanceStatus;
import com.magdsoft.ws.model.AppSetting;
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
import com.magdsoft.ws.controller.SendMessageApi;
//import com.magdsoft.ws.form.ApiSendMessage;
import com.magdsoft.ws.form.Aboutus;
import com.magdsoft.ws.form.AddDish;
import com.magdsoft.ws.form.AddOrder;
import com.magdsoft.ws.form.ChangePassword;
import com.magdsoft.ws.form.Follower;
import com.magdsoft.ws.form.GetCityChiefs;
import com.magdsoft.ws.form.GetDish;
import com.magdsoft.ws.form.GetFollowing;
import com.magdsoft.ws.form.GetOrder;
import com.magdsoft.ws.form.GetOrders;
import com.magdsoft.ws.form.GetVideo;
import com.magdsoft.ws.form.LikeDish;
import com.magdsoft.ws.form.Pagination;
import com.magdsoft.ws.form.UpdateInfo;

@Controller
@RequestMapping("/api")
public class UserController {
	@Autowired
	Upload upload;
	@Autowired
	SendMessageApi sendMessageApi;
	
	
	ApiSendMessage apiSend ;
	@Autowired
	public Utility utility;
	@Autowired
	public EntityManager entityManager;
	public static final String PATH = "/www/chiefatm/uploads";
	public static final byte[] userName = new byte[]{'S', 'h', 'i', 'f', 'a', 't'};
	public static final int PAGINATION = 20;
	// public static final Map<String,Object> point=new HashMap<>();
	public static final int PAGINATION_GET_HELP = PAGINATION;
	public static final int PAGINATION_GET_HISTORY = PAGINATION;
	// public EntityManager entityManager;
	private List<Dish> dishes1 = new ArrayList<>();
	Query q1;
	 private static String prepareTelephoneNumber(String telNum) {
		    return new BigInteger(telNum).toString();
	    }
	@Transactional
	@RequestMapping("/register")
	public @ResponseBody Map<String, Object> register(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			if (user.getPhone() == null || user.getPassword() == null || user.getName() == null
					|| user.getName().isEmpty()) {
				map.put("status", 403);
				return map;

			} else if (user.getPhone().length() < 9) {
				map.put("status", 405);
				return map;

			} else if (user.getPassword().length() < 6) {
				map.put("status", 407);
				return map;
			}
			String regx3 = "^[\\p{L} .'-]+$";
			if(!user.getName().matches(regx3)){
				map.put("status", 408);
				return map;
			}
//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//
//			if (!user.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 406);
//				return map;
//			}

			String apiToken = null;

//			Query q1 = entityManager.createQuery("from User where email=:em");
//			q1.setParameter("em", user.getEmail());
//			try {
//				User use1 = (User) q1.getSingleResult();
//				map.put("status", 401);
//				return map;
//			} catch (NoResultException ex) {
//
//			}

			Query q = entityManager.createQuery("from User where phone=:ph");
			q.setParameter("ph", user.getPhone());

			try {
				User use = (User) q.getSingleResult();
				map.put("status", 400);
				return map;
			} catch (NoResultException ex) {

				SecureRandom random = new SecureRandom();
				apiToken = new BigInteger(500, random).toString(32);
				SecureRandom rand = new SecureRandom();
				 int num = rand.nextInt(100000);
				 String formatted = String.format("%05d", num);
				 int verificationCode1 = Integer.valueOf(formatted);
				//int verificationCode1 = 12345;
				User newUser = new User();
				try {
					newUser.setPhone(user.getPhone());
				} catch (Exception e) {
					map.put("status", 400);
					return map;
				}
				newUser.setName(user.getName());
//				try {
//					newUser.setEmail(user.getEmail());
//				} catch (Exception e) {
//					map.put("status", 406);
//					return map;
//				}
				// newUser.setIsActive(false);
				newUser.setVerificationCode(verificationCode1);
				newUser.setApiToken(apiToken);
				newUser.setPassword(DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
				// newUser.setIsActive(false);
				entityManager.persist(newUser);

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

	// @Transactional
	// @RequestMapping("/login")
	// public @ResponseBody Map<String, Object> login(User user,
	// @RequestBody(required = false) User userBody) {
	//
	// Map<String, Object> map = new HashMap<>();
	// // em.getTransaction().begin();
	// try {
	// if (userBody != null) {
	// user = userBody;
	// }
	//
	// if (user.getPassword().length() < 6) {
	// map.put("status", 401);
	// return map;
	// }
	//
	// if (user.getPhone() == null || user.getPassword() == null) {
	// map.put("status", 402);
	// return map;
	// }
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
	// Query q = entityManager.createQuery("from User where phone=:ph");
	// q.setParameter("ph", user.getPhone());
	// User use = null;
	// try {
	// use = (User) q.getSingleResult();
	//
	// } catch (NoResultException ex) {
	// map.put("status", 403);
	// return map;
	// }
	// // if (use!= null) {
	// if
	// (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
	// use.getPassword())
	// && use.getIsActive()) {
	// map.put("status", 200);
	// // map.put("userId", use.getId());
	// map.put("apiToken", use.getApiToken());
	// map.put("name", use.getName());
	// // map.put("password", u.getPassword());
	// map.put("phone", use.getPhone());
	// map.put("userImage", use.getPic());
	// // map.put("isActive", u.getIsActive());
	// // map.put("veryficationCode", u.getVeryficationCode());
	// map.put("email", use.getEmail());
	// // map.put("address", u.getAddress());
	//
	// // map.put("theOrder_id",u.getTheOrder_id().get(0).getUser_id());
	// // map.put("createdAt", u.getCreatedAt());
	// // map.put("updatedAt", u.getUpdatedAt());
	// return map;
	// } else if
	// (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
	// use.getPassword())
	// && use.getIsActive() == false) {
	// map.put("status", 406);
	// return map;
	// }
	// //
	//
	// } catch (Exception e) {
	// // Map<String,Object> map=new HashMap<>();
	// // map.put("status", 404);
	//
	// throw e;
	// // return map;
	// }
	// map.put("status", "you are not registered");
	// return map;
	//
	// }

	@Transactional
	@RequestMapping("/login")
	public @ResponseBody Map<String, Object> login(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}
			if (user.getPhone() == null || user.getPassword() == null) {
				map.put("status", 403);
				return map;
			}

//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

//			if (!user.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}

			// try {
			// user.getEmail();
			// } catch (Exception e) {
			// map.put("status", 405);
			// return map;
			// }

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
			Query q = entityManager.createQuery("from User where phone=:em");
			q.setParameter("em", user.getPhone());
			User use = null;
			try {
				use = (User) q.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
//			 if (use.getVerificationCode()== null) {
//				 map.put("status", 111);
//					return map;
//			 }
			if (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(), use.getPassword())) {
				map.put("status", 200);
				map.put("userId", use.getId());
				map.put("apiToken", use.getApiToken());
				map.put("name", use.getName());
				// map.put("password", u.getPassword());
				map.put("phone", use.getPhone());
				map.put("userImage", use.getPic());

				map.put("email", use.getEmail());

				return map;
			} else if (!DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(), use.getPassword())) {
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
			// map.put("status", 404);

			// throw e;
			return map;
		}
		map.put("status", 222);
		return map;

	}

	@Transactional
	@RequestMapping("/resetPassword")
	public @ResponseBody Map<String, Object> resetPassword(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}
			if (user.getPhone() == null ) {
				map.put("status", 403);
				return map;
			}
//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//
//			if (!user.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}
			
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where phone=:em");
			q1.setParameter("em", user.getPhone());
			try {
				use1 = (User) q1.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			SecureRandom rand = new SecureRandom();
			int num = rand.nextInt(100000);
			 String formatted = String.format("%05d", num);
			 int verificationCode1 = Integer.valueOf(formatted);
			//int verificationCode1 = 12345;
			use1.setVerificationCode(verificationCode1);
			entityManager.persist(use1);
			apiSend= new ApiSendMessage();
			
			apiSend.setUserName(new String(userName));
			apiSend.setNumbers(prepareTelephoneNumber(user.getPhone()));
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
	public @ResponseBody Map<String, Object> validateCode(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}
			if (user.getPhone() == null || user.getVerificationCode() == null) {
				map.put("status", 403);
				return map;
			}
//			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

//			if (!user.getEmail().matches(EMAIL_REGEX)) {
//				map.put("status", 405);
//				return map;
//			}
//			
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where phone=:em");
			q1.setParameter("em", user.getPhone());
			try {
				use1 = (User) q1.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			if (user.getVerificationCode().equals(use1.getVerificationCode())) {
				use1.setVerificationCode(null);
				SecureRandom random = new SecureRandom();
				String apiToken = new BigInteger(500, random).toString(32);
				use1.setTempApiToken(apiToken);
				entityManager.persist(use1);
				map.put("status", 200);
				map.put("apiToken", use1.getTempApiToken());

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
		return map;
	}

	@Transactional
	@RequestMapping("/resetCode")
	public @ResponseBody Map<String, Object> resetCode(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

			if (!user.getEmail().matches(EMAIL_REGEX)) {
				map.put("status", 405);
				return map;
			}
			if (user.getEmail() == null) {
				map.put("status", 403);
				return map;
			}
			if (user.getVerificationCode().toString().length() != 5) {
				map.put("status", 406);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where email=:em");
			q1.setParameter("em", user.getEmail());
			try {
				use1 = (User) q1.getSingleResult();

			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if (user.getVerificationCode().equals(use1.getVerificationCode())) {
				use1.setVerificationCode(null);
				entityManager.persist(use1);
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 401);
				map.put("apiToken", use1.getApiToken());
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
	public @ResponseBody Map<String, Object> updatePassword(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}
			 if (user.getPassword() == null || user.getApiToken() == null || user.getPassword().isEmpty()
					|| user.getApiToken().isEmpty()) {
				map.put("status", 403);
				return map;
			}else if (user.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;

			}else if (user.getPassword().length() < 6) {
				map.put("status", 406);
				return map;

			}
			

			Query q = entityManager.createQuery("from User where tempApiToken=:api");
			q.setParameter("api", user.getApiToken());
			User use = null;
			try {
				use = (User) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			// int num = rand.nextInt(100000);
			// String formatted = String.format("%05d", num);
			// int veryficationCode1 = Integer.valueOf(formatted);
			// int veryficationCode1 = 12345;
			// User currUser = entityManager.find(User.class, use.getId());
			if (use.getVerificationCode() == null && user.getApiToken().equals(use.getTempApiToken())) {
				use.setPassword(DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
				use.setTempApiToken(null);
				entityManager.persist(use);
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
	@RequestMapping("/followChief")
	public @ResponseBody Map<String, Object> followChief(Follower follower,
			@RequestBody(required = false) Follower followerBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (followerBody != null) {
				follower = followerBody;
			}
			if (follower.getApiToken() == null || follower.getChiefId() == null) {
				map.put("status", 403);
				return map;
			}
			if (follower.getApiToken().length() < 68) {
				map.put("status", 406);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", follower.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Chief currChief = null;

			currChief = entityManager.find(Chief.class, follower.getChiefId());
			if (currChief == null) {
				map.put("status", 401);
				return map;
			}
			User currUser = entityManager.find(User.class, use1.getId());
			Boolean exist = false;
			for (int i = 0; i < currChief.getUser_id().size(); i++) {
				if (use1.getId() == currChief.getUser_id().get(i).getId()) {
					exist = true;
					break;
				}
			}
			// User currUser = entityManager.find(User.class, use1.getId());
			if (exist == true) {
				map.put("status", 405);
				return map;
			}
			if (currChief.getIsActive() == true) {
				currUser.getChief_id().add(currChief);
				currChief.getUser_id().add(currUser);
				entityManager.persist(currChief);
				Query q2 = entityManager.createQuery("from NotificationType where name='chief followed'");
				NotificationType notifiType = null;
				try {
					notifiType = (NotificationType) q2.getSingleResult();
				} catch (NoResultException e) {

				}
				Notification notification = new Notification();

				notification.setNotificationType_id(notifiType);
				notification.setUser_id(currUser);
				entityManager.persist(notification);

				//for (int i = 0; i < use1.getChief_id().size(); i++) {
					// Notification
					// notification=entityManager.find(Notification.class, 1);
					NotifiedPerson person = new NotifiedPerson();
					//Chief chie = entityManager.find(Chief.class, currChief);
					//person.setUser_id(use1);
					person.setIsSeen(false);
					person.setNotification_id(notification);
					person.setChief_id(currChief);
					//person.setNotificationType_id(notifiType);

					entityManager.merge(person);
					// entityManager.persist(notification);
				//}
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 111);
				return map;
		}

		} catch (Exception e) {

			map.put("status", 404);
			//return map;
			 throw e;
		}
		
	}

	@Transactional
	@RequestMapping("/unfollowChief")
	public @ResponseBody Map<String, Object> unFollowChief(Follower follower,
			@RequestBody(required = false) Follower followerBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (followerBody != null) {
				follower = followerBody;
			}
			if (follower.getApiToken() == null || follower.getChiefId() == null) {
				map.put("status", 403);
				return map;
			}
			if (follower.getApiToken().length() < 68) {
				map.put("status", 406);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", follower.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Chief currChief = null;

			currChief = entityManager.find(Chief.class, follower.getChiefId());
			if (currChief == null) {
				map.put("status", 401);
				return map;
			}
			Boolean exist = false;
			for (int i = 0; i < currChief.getUser_id().size(); i++) {
				if (use1.getId() == currChief.getUser_id().get(i).getId()) {
					exist = true;
					break;
				}
			}
			// User currUser = entityManager.find(User.class, use1.getId());
			if (exist == false) {
				map.put("status", 405);
				return map;
			}
			use1.getChief_id().remove(currChief);
			currChief.getUser_id().remove(use1);
			entityManager.persist(currChief);
			//entityManager.persist(use1);
			// Query q = entityManager.createQuery(
			// "select notifiedPerson from NotifiedPerson notifiedPerson join
			// notifiedPerson.notification_id notifi where
			// notifiedPerson.chief_id.id=:di and
			// notifiedPerson.user_id.id=:ui");
			// q.setParameter("di", follower.getChiefId());
			// q.setParameter("ui", use1.getId());
			// List<NotifiedPerson> notifications = q.getResultList();
			// for (NotifiedPerson n : notifications) {
			// entityManager.remove(n);
			// entityManager.flush();
			// }

//			Query q = entityManager.createNativeQuery(
//					"delete np,n from notified_person np join notification n on n.id=np.notification_id_id where np.chief_id_id=:di and np.user_id_id=:ui");
			Query q = entityManager.createNativeQuery("delete from notified_person where chief_id_id=:di and user_id_id=:ui");
			
			q.setParameter("di", follower.getChiefId());
			q.setParameter("ui", use1.getId());
			q.executeUpdate();
			q = entityManager.createNativeQuery("delete from notification where id NOT IN (select notification_id_id from notified_person)");
			q.executeUpdate();
			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getFollowing")
	public @ResponseBody Map<String, Object> getFollowing(GetFollowing getFollowing,
			@RequestBody(required = false) GetFollowing getFollowingBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getFollowingBody != null) {
				getFollowing = getFollowingBody;
			}
			if (getFollowing.getApiToken() == null || getFollowing.getLang() == null|| getFollowing.getPage() == null) {
				map2.put("status", 403);
				return map2;
			}
			if (getFollowing.getApiToken().length() < 68) {
				map2.put("status", 405);
				return map2;
			}
			if (!getFollowing.getLang().equals("ar") && !getFollowing.getLang().equals("en")) {
				map2.put("status", 406);
				return map2;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getFollowing.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map2.put("status", 400);
				return map2;
			}

			List<Map<String, Object>> ret = new ArrayList<>();
			if(getFollowing.getChiefName()==null){
				q1 = entityManager.createQuery(
						"select chief from Chief chief join chief.user_id user where user.id =:id order by chief.createdAt asc",
						Chief.class);
				q1.setParameter("id", use1.getId());
				
				q1.setFirstResult((getFollowing.getPage()) * PAGINATION_GET_HELP);
				q1.setMaxResults(50);
			}else if(getFollowing.getChiefName()!=null&&!getFollowing.getChiefName().isEmpty()){
				q1 = entityManager.createQuery(
						"select chief from Chief chief join chief.user_id user where user.id =:id and chief.name LIKE CONCAT(:n,'%') order by chief.createdAt asc",
						Chief.class);
				q1.setParameter("id", use1.getId());
				q1.setParameter("n", getFollowing.getChiefName());
				q1.setFirstResult((getFollowing.getPage()) * PAGINATION_GET_HELP);
				q1.setMaxResults(50);
			}
			List<Chief> chiefs = q1.getResultList();
			for (Chief c : chiefs) {
				if (c.getIsActive() == true) {
					Map<String, Object> oneObjAsMap = new HashMap<>();
					oneObjAsMap.put("chief_id", c.getId());
					oneObjAsMap.put("name", c.getName());
					oneObjAsMap.put("image_url", c.getPic());
					try {
						if (getFollowing.getLang().equals("ar")) {
							oneObjAsMap.put("city", c.getCity_id().getNameAr());
						} else if (getFollowing.getLang().equals("en")) {
							oneObjAsMap.put("city", c.getCity_id().getNameEn());
						}

					} catch (Exception e) {
						oneObjAsMap.put("city",null);
					}

					ret.add(oneObjAsMap);
				}
			}
			if (ret.isEmpty()) {
				map2.put("status", 300);
				return map2;
			}

			map2.put("status", 200);
			map2.put("Chiefs", ret);
			return map2;
		} catch (Exception e) {
			map2.put("status", 404);
			return map2;
			 //throw e;
		}
	}

	@Transactional
	@RequestMapping("/likeDish")
	public @ResponseBody Map<String, Object> likeDish(LikeDish likeDish,
			@RequestBody(required = false) LikeDish likeDishBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (likeDishBody != null) {
				likeDish = likeDishBody;
			}
			if (likeDish.getApiToken() == null || likeDish.getDishId() == null) {
				map.put("status", 403);
				return map;
			}
			if (likeDish.getApiToken().length() < 68) {
				map.put("status", 406);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", likeDish.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Dish currDish = null;

			currDish = entityManager.find(Dish.class, likeDish.getDishId());
			if (currDish == null) {
				map.put("status", 401);
				return map;
			}
			User currUser = entityManager.find(User.class, use1.getId());
			Boolean exist = false;
			for (int i = 0; i < currDish.getUser_id().size(); i++) {
				if (use1.getId() == currDish.getUser_id().get(i).getId()) {
					exist = true;
					break;
				}
			}
			// User currUser = entityManager.find(User.class, use1.getId());
			if (exist == true) {
				map.put("status", 405);
				return map;
			}

			currUser.getDish_id().add(currDish);
			currDish.getUser_id().add(currUser);
			entityManager.persist(currDish);
			Query q2 = entityManager.createQuery("from NotificationType where name='dish liked'");
			NotificationType notifiType = (NotificationType) q2.getSingleResult();
			Notification notification = new Notification();
			
			notification.setNotificationType_id(notifiType);
			notification.setDish_id(currDish);
			notification.setUser_id(currUser);
			entityManager.persist(notification);
			//for (int i = 0; i < use1.getChief_id().size(); i++) {
				// Notification
				// notification=entityManager.find(Notification.class, 1);
				NotifiedPerson person = new NotifiedPerson();
				Chief chie = entityManager.find(Chief.class, currDish.getChief_id().getId());
				//person.setUser_id(use1);
				person.setIsSeen(false);
				person.setNotification_id(notification);
				person.setChief_id(chie);
				//person.setNotificationType_id(notifiType);

				entityManager.persist(person);
				// entityManager.persist(notification);
			//}
			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/unlikeDish")
	public @ResponseBody Map<String, Object> unlikeDish(LikeDish likeDish,
			@RequestBody(required = false) LikeDish likeDishBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (likeDishBody != null) {
				likeDish = likeDishBody;
			}
			if (likeDish.getApiToken() == null || likeDish.getDishId() == null) {
				map.put("status", 403);
				return map;
			}
			if (likeDish.getApiToken().length() < 68) {
				map.put("status", 406);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", likeDish.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Dish currDish = null;

			currDish = entityManager.find(Dish.class, likeDish.getDishId());
			if (currDish == null) {
				map.put("status", 401);
				return map;
			}
			User currUser = entityManager.find(User.class, use1.getId());
			Boolean exist = false;
			for (int i = 0; i < currDish.getUser_id().size(); i++) {
				if (use1.getId() == currDish.getUser_id().get(i).getId()) {
					exist = true;
					break;
				}
			}
			// User currUser = entityManager.find(User.class, use1.getId());
			if (exist == false) {
				map.put("status", 405);
				return map;
			}
			currUser.getDish_id().remove(currDish);
			currDish.getUser_id().remove(currUser);
			entityManager.persist(currDish);

			// Query q = entityManager.createQuery(
			// "select notifiedPerson from NotifiedPerson notifiedPerson join
			// notifiedPerson.notification_id notifi where notifi.dish_id.id=:di
			// and notifiedPerson.user_id.id=:ui");
			// q.setParameter("di", likeDish.getDishId());
			// List<NotifiedPerson> notifications = q.getResultList();
			// for (NotifiedPerson n : notifications) {
			// entityManager.remove(n);
			// entityManager.flush();
			// }
			// Query q=entityManager.createNativeQuery("delete n,np from
			// Notification n join NotifiedPerson np on n.id=np.notification_id
			// where n.dish_id=:di and notifiedPerson.user_id=:ui");
//			Query q = entityManager.createNativeQuery(
//					"delete np,n from notified_person np join notification n on n.id=np.notification_id_id where n.dish_id_id=:di and np.user_id_id=:ui");
			Query q = entityManager.createNativeQuery(
					"delete np from notified_person np INNER JOIN notification n where n.id=np.notification_id_id and n.dish_id_id=:di and np.user_id_id=:ui");
			q.setParameter("di", likeDish.getDishId());
			q.setParameter("ui", use1.getId());
			q.executeUpdate();
			q = entityManager.createNativeQuery("delete from notification where id NOT IN (select notification_id_id from notified_person)");
			q.executeUpdate();
			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getCityChiefs")
	public @ResponseBody Map<String, Object> getCityChiefs(GetCityChiefs getCityChiefs,
			@RequestBody(required = false) GetCityChiefs getCityChiefsBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getCityChiefsBody != null) {
				getCityChiefs = getCityChiefsBody;
			}
			if (getCityChiefs.getApiToken() == null || getCityChiefs.getCityId() == null) {
				map.put("status", 403);
				return map;
			}
			if (getCityChiefs.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getCityChiefs.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			City city = null;

			city = entityManager.find(City.class, getCityChiefs.getCityId());
			if (city == null) {
				map.put("status", 401);
				return map;
			}

			q1 = entityManager.createQuery("from Chief where city_id.id=:city and isActive=true and name  LIKE CONCAT(:n,'%')",
					Chief.class);
			q1.setParameter("city", getCityChiefs.getCityId());
			q1.setParameter("n", getCityChiefs.getChiefName());
			q1.setFirstResult((getCityChiefs.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Map<String, Object>> ret = new ArrayList<>();
			@SuppressWarnings("unchecked")
			List<Chief> chiefs = q1.getResultList();
			for (Chief c : chiefs) {
				Map<String, Object> oneObjAsMap = new HashMap<>();
				oneObjAsMap.put("chief_id", c.getId());

				oneObjAsMap.put("Name", c.getName());
				oneObjAsMap.put("image_url", c.getPic());
				ret.add(oneObjAsMap);
			}
			if (ret.isEmpty()) {
				map2.put("status", 300);
				return map2;
			}
			map2.put("status", 200);
			map2.put("chiefs", ret);
			return map2;

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getRecentDishes")
	public @ResponseBody Map<String, Object> getRecentDishes(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			if (pagination.getApiToken() == null || pagination.getPage() == null) {
				map2.put("status", 403);
				return map2;
			}
			if (pagination.getApiToken().length() < 68) {
				map2.put("status", 405);
				return map2;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map2.put("status", 400);
				return map2;
			}
//			if(use1.getVerificationCode()==null){
//				map2.put("status", 111);
//				return map2;
//			}
			// q1=entityManager.createQuery("from Dish d join d.user_id ui where
			// ui.id=:id", Dish.class);
//			q1 = entityManager.createQuery(
//					"select dish from User user join user.dish_id dish where user.id=:id order by dish.createdAt desc",
//					Dish.class);
			q1 = entityManager.createQuery("select dish from Dish dish join dish.chief_id chief join chief.user_id user where user.id=:id order by dish.createdAt desc");
			q1.setParameter("id", use1.getId());
			q1.setFirstResult((pagination.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Dish> dishes = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();

			for (Dish d : dishes) {
				Map<String, Object> oneObjAsMap = new HashMap<>();
				oneObjAsMap.put("dish_id", d.getId());
				oneObjAsMap.put("name", d.getName());
				oneObjAsMap.put("image_url", d.getPic());

				oneObjAsMap.put("num_of_likes", d.getUser_id().size());
				if(d.getUser_id().size()==0){
					oneObjAsMap.put("is_liked", null);
				}
				// try{
				for (int i = 0; i < d.getUser_id().size(); i++) { // if
																	// (use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId()))
					// if((use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId())))
					
					if ((use1.getId().equals(d.getUser_id().get(i).getId())))
						oneObjAsMap.put("is_liked", true);

					else
						oneObjAsMap.put("is_liked", false);
				}
				// }catch(Exception e){
				// oneObjAsMap.put("is_liked", false);
				// }
				oneObjAsMap.put("chief_name", d.getChief_id().getName());
				oneObjAsMap.put("chief_image", d.getChief_id().getPic());
				oneObjAsMap.put("time", d.getCreatedAt());
				ret.add(oneObjAsMap);
			}

			// List<Map<String, Object>> ret = new ArrayList<>();
			// for (int i = 0; i < use1.getDish_id().size(); i++) {
			// map.put("dish_id", use1.getDish_id().get(i).getId());
			// map.put("name", use1.getDish_id().get(i).getName());
			// map.put("image_url", use1.getDish_id().get(i).getPic());
			// map.put("num_of_likes",
			// use1.getDish_id().get(i).getUser_id().size());
			// if
			// (use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId()))
			// {
			// map.put("is_liked", true);
			// } else {
			// map.put("is_liked", false);
			// }
			// map.put("chief_name", use1.getChief_id().get(i).getName());
			// ret.add(map);
			//
			// }
			if (ret.isEmpty()) {
				map2.put("status", 300);
				return map2;
			}
			map2.put("status", 200);
			// map2.put("dishes",Paginate.getPaginate(ret,
			// pagination.getPage()));

			map2.put("dishes", ret);
			return map2;
		} catch (Exception e) {
			map2.put("status", 404);
			return map2;
			// throw e;
		}
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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getDish.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
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
			if(dish.getUser_id().size()==0){
				map.put("is_liked", false);
			}
			map.put("num_of_likes", dish.getUser_id().size());
			// try{
			for (int i = 0; i < dish.getUser_id().size(); i++) { // if
																// (use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId()))
				// if((use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId())))
				
				if ((use1.getId().equals(dish.getUser_id().get(i).getId())))
					map.put("is_liked", true);

				else
					map.put("is_liked", false);
			}
			return map;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getCites")
	public @ResponseBody Map<String, Object> getCites(Follower follower,
			@RequestBody(required = false) Follower followerBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (followerBody != null) {
				follower = followerBody;
			}
			if (follower.getApiToken() == null || follower.getLang() == null) {
				map2.put("status", 403);
				return map2;
			}
			if (!follower.getLang().equals("ar") && !follower.getLang().equals("en")) {
				map2.put("status", 406);
				return map2;
			}
			if (follower.getApiToken().length() < 68) {
				map2.put("status", 405);
				return map2;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", follower.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map2.put("status", 400);
				return map2;
			}
			q1 = entityManager.createQuery("from City");
			List<City> cities = new ArrayList<>();
			cities = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			for (City ci : cities) {
				Map<String, Object> oneObjectMap = new HashMap<>();
				oneObjectMap.put("city_id", ci.getId());
				if (follower.getLang().equals("ar"))
					oneObjectMap.put("name", ci.getNameAr());
				else if (follower.getLang().equals("en"))
					oneObjectMap.put("name", ci.getNameEn());
				ret.add(oneObjectMap);
			}
			map2.put("status", 200);
			map2.put("cities", ret);
			return map2;
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getChief")
	public @ResponseBody Map<String, Object> getChief(Follower follower,
			@RequestBody(required = false) Follower followerBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (followerBody != null) {
				follower = followerBody;
			}
			if (follower.getApiToken() == null || follower.getChiefId() == null || follower.getLang() == null) {
				map.put("status", 403);
				return map;
			}
			if (follower.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			if (!follower.getLang().equals("ar") && !follower.getLang().equals("en")) {
				map.put("status", 406);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", follower.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Chief chief = null;

			chief = entityManager.find(Chief.class, follower.getChiefId());
			if (chief == null) {
				map.put("status", 401);
				return map;
			}

			// List<Map<String, Object>> ret = new ArrayList<>();
			if (chief.getIsActive() == true) {
				// for (int i = 0; i < use1.getChief_id().size(); i++) {
				// if
				// (use1.getId().equals(use1.getChief_id().get(i).getUser_id().get(i).getId()))
				// {
				// map.put("status", 200);
				// // map.put("chief_id",
				// // use1.getChief_id().get(i).getId());
				// map.put("name", use1.getChief_id().get(i).getName());
				// map.put("image_url", use1.getChief_id().get(i).getPic());
				// if (follower.getLang().equals("ar"))
				// map.put("city",
				// use1.getChief_id().get(i).getCity_id().getNameAr());
				// if (follower.getLang().equals("en"))
				// map.put("city",
				// use1.getChief_id().get(i).getCity_id().getNameEn());
				// map.put("num_followers",
				// use1.getChief_id().get(i).getUser_id().size());
				// break;
				// // ret.add(map);
				// }
				try{
					map.put("status", 200);
					map.put("name", chief.getName());
					map.put("image_url", chief.getPic());
					map.put("num_followers", chief.getUser_id().size());
					map.put("phone", chief.getPhone());
					if (follower.getLang().equals("ar"))
						map.put("city", chief.getCity_id().getNameAr());
					if (follower.getLang().equals("en"))
						map.put("city", chief.getCity_id().getNameEn());
					
					
				}catch(Exception e){
					map.put("city", null);
				}
				return map;
			} else {
				map.put("status", 111);
				return map;
			}
			// Query q = entityManager.createQuery("from Chief where id=:ci");
			// //q.setParameter("ui", use1.getId());
			// q.setParameter("ci", follower.getChiefId());
			//
			// Chief currChief = null;
			// try {
			// currChief = (Chief) q.getSingleResult();
			// } catch (NoResultException ex) {
			//
			// }
			//
			// map.put("status", 200);
			// map.put("name", currChief.getName());
			// map.put("phone", currChief.getPhone());
			// map.put("cityAr", currChief.getCity_id().getNameAr());
			// map.put("cityEn", currChief.getCity_id().getNameEn());
			// map.put("image_url", currChief.getPic());
			// map.put("num_followers", currChief.getUser_id().size());

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
		
	}

	@Transactional
	@RequestMapping("/getChiefDishes")
	public @ResponseBody Map<String, Object> getChiefDishes(Follower follower,
			@RequestBody(required = false) Follower followerBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (followerBody != null) {
				follower = followerBody;
			}
			if (follower.getApiToken() == null || follower.getChiefId() == null || follower.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (follower.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", follower.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Chief chief = null;

			chief = entityManager.find(Chief.class, follower.getChiefId());
			if (chief == null) {
				map.put("status", 401);
				return map;
			}
			if (chief.getIsActive() == true) {
//				q1 = entityManager.createQuery(
//						"select dish from User user join user.dish_id dish join user.chief_id chief where user.id=:id and chief.id=:ci order by dish.createdAt desc",
//						Dish.class);
				 q1=entityManager.createQuery("from Dish where chief_id.id=:ci and name  LIKE CONCAT(:n,'%')");
//				q1.setParameter("id", use1.getId());
				q1.setParameter("ci", follower.getChiefId());
				q1.setParameter("n", follower.getDishName());
				q1.setFirstResult((follower.getPage()) * PAGINATION_GET_HELP);
				q1.setMaxResults(PAGINATION_GET_HELP);
				List<Dish> dishes = q1.getResultList();
				List<Map<String, Object>> ret = new ArrayList<>();

				for (Dish d : dishes) {
					try{
						Map<String, Object> oneObjAsMap = new HashMap<>();
						oneObjAsMap.put("dish_id", d.getId());
						oneObjAsMap.put("name", d.getName());
						oneObjAsMap.put("image_url", d.getPic());
	
						oneObjAsMap.put("num_of_likes", d.getUser_id().size());
						if(d.getUser_id().size()==0){
							oneObjAsMap.put("is_liked", false);
						}
						for (int i = 0; i < d.getUser_id().size(); i++) {
							// for (int i = 0; i < use1.getDish_id().size(); i++) {
							// if
							// (use1.getId().equals(use1.getDish_id().get(i).getUser_id().get(i).getId()))
							if ((use1.getId().equals(d.getUser_id().get(i).getId())))
								oneObjAsMap.put("is_liked", true);
							else
								oneObjAsMap.put("is_liked", false);
						}
	
						// oneObjAsMap.put("chief_name", d.getChief_id().getName());
						ret.add(oneObjAsMap);
					}catch(Exception e){
						
					}
				}

				if (ret.isEmpty()) {
					map2.put("status", 300);
					return map2;
				}
				map2.put("status", 200);
				map2.put("dishes", ret);
				return map2;
			} else {
				map2.put("status", 111);
				return map2;
			}

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getChiefVideos")
	public @ResponseBody Map<String, Object> getChiefVideos(Follower follower,
			@RequestBody(required = false) Follower followerBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (followerBody != null) {
				follower = followerBody;
			}
			if (follower.getApiToken() == null || follower.getChiefId() == null || follower.getPage() == null) {
				map.put("status", 403);
				return map;
			}
			if (follower.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", follower.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Chief chief = null;

			chief = entityManager.find(Chief.class, follower.getChiefId());
			if (chief == null) {
				map.put("status", 401);
				return map;
			}

			if (chief.getIsActive() == true) {
				q1 = entityManager.createQuery(
						"select video from Video video join video.chief_id chief join chief.user_id user where user.id=:id and video.chief_id.id=:ci and video.name LIKE CONCAT(:n,'%') order by video.createdAt desc",
						Video.class);
				// q1=entityManager.createQuery("from Video where
				// chief_id.id=:ci");
				q1.setParameter("id", use1.getId());
				q1.setParameter("ci", follower.getChiefId());
				q1.setParameter("n", follower.getVideoName());
				q1.setFirstResult((follower.getPage()) * PAGINATION_GET_HELP);
				q1.setMaxResults(PAGINATION_GET_HELP);
				List<Video> videos = new ArrayList<>();
				List<Map<String, Object>> ret = new ArrayList<>();
				videos = q1.getResultList();
				for (Video v : videos) {
					try{
						Map<String, Object> oneObjAsMap = new HashMap<>();
						oneObjAsMap.put("video_id", v.getId());
						oneObjAsMap.put("title", v.getName());
						oneObjAsMap.put("date", v.getCreatedAt());
						oneObjAsMap.put("image_url", v.getPic());
						ret.add(oneObjAsMap);
					}catch(Exception e){
						
					}

				}
				if (ret.isEmpty()) {
					map2.put("status", 300);
					return map2;
				}
				map2.put("status", 200);
				map2.put("videos", ret);
				return map2;
			} else {
				map2.put("status", 111);
				return map2;
			}
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/getVideo")
	public @ResponseBody Map<String, Object> getVideo(GetVideo getVideo,
			@RequestBody(required = false) GetVideo getVideoBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (getVideoBody != null) {
				getVideo = getVideoBody;
			}
			if (getVideo.getApiToken() == null || getVideo.getVideoId() == null) {
				map.put("status", 403);
				return map;
			}
			if (getVideo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getVideo.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Video v = null;

			v = entityManager.find(Video.class, getVideo.getVideoId());
			if (v == null) {
				map.put("status", 401);
				return map;
			}
			map.put("status", 200);
			// map.put("video_id", v.getId());
			map.put("title", v.getName());
			map.put("date", v.getCreatedAt());
			map.put("video_url", v.getSrc());
			map.put("image_url", v.getPic());
			return map;

		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/AboutUs")
	public @ResponseBody Map<String, Object> getAboutUs(Aboutus aboutus,
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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", aboutus.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", aboutus.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
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
	@RequestMapping("/changePassword")
	public @ResponseBody Map<String, Object> changePassword(ChangePassword changePassword,
			@RequestBody(required = false) ChangePassword changePasswordBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (changePasswordBody != null) {
				changePassword = changePasswordBody;
			}
			if (changePassword.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;

			} else if (changePassword.getOldPassword() == null || changePassword.getNewPassword() == null
					|| changePassword.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (changePassword.getOldPassword().length() < 6) {
				map.put("status", 406);
				return map;
			} else if (changePassword.getNewPassword().length() < 6) {
				map.put("status", 407);
				return map;
			}
			boolean isUserAvailable = false;

			Query q = entityManager.createQuery("from User where apiToken=:api");
			q.setParameter("api", changePassword.getApiToken());
			User use = null;
			try {
				use = (User) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			if (DefaultPasswordHasher.getInstance().isPasswordValid(changePassword.getOldPassword(),
					use.getPassword())) {
				use.setPassword(DefaultPasswordHasher.getInstance().hashPassword(changePassword.getNewPassword()));
				entityManager.persist(use);
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
	@RequestMapping("/addOrder")
	public @ResponseBody Map<String, Object> addOrder(AddOrder addOrder,
			@RequestBody(required = false) AddOrder addOrderBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (addOrderBody != null) {
				addOrder = addOrderBody;
			}
			if (addOrder.getApiToken() == null || addOrder.getDishId() == null || addOrder.getQuantity() == null) {
				map.put("status", 402);
				return map;
			}
			if (addOrder.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", addOrder.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Dish dish = null;

			dish = entityManager.find(Dish.class, addOrder.getDishId());
			if (dish == null) {
				map.put("status", 401);
				return map;
			}

			// AcceptanceStatus acceptance = null;
			// try {
			// acceptance = entityManager.find(AcceptanceStatus.class, 3);
			// } catch (Exception e) {
			//
			// }
			if (dish.getChief_id().getIsActive() == true) {
				Theorder theorder = new Theorder();
				theorder.setIsAccepted("انتظار");
				// theorder.setAcceptanceStatus_id(acceptance);
				theorder.setDish_id(dish);
				theorder.setQuantity(addOrder.getQuantity());
				theorder.setUser_id(use1);
				theorder.setTotalPrice(addOrder.getQuantity() * dish.getPrice());
				theorder.setChief_id(dish.getChief_id());
				entityManager.persist(theorder);
				Query q2 = entityManager.createQuery("from NotificationType where name='order added'");
				NotificationType notifiType = (NotificationType) q2.getSingleResult();
				Notification notification = new Notification();
				notification.setOrder_id(theorder);
				notification.setNotificationType_id(notifiType);
				notification.setUser_id(use1);
				entityManager.persist(notification);
				//for (int i = 0; i < use1.getChief_id().size(); i++) {
					// Notification
					// notification=entityManager.find(Notification.class, 1);
					NotifiedPerson person = new NotifiedPerson();
					Chief chie = entityManager.find(Chief.class, dish.getChief_id().getId());
					//person.setUser_id(use1);
					person.setIsSeen(false);
					person.setNotification_id(notification);
					person.setChief_id(chie);
					//person.setNotificationType_id(notifiType);

					entityManager.persist(person);
					// entityManager.persist(notification);
				//}
				map.put("status", 200);
				return map;
			} else {
				map.put("status", 111);
				return map;
			}
		} catch (Exception e) {

			map2.put("status", 404);
			return map2;
			 //throw e;
		}
	}

	@SuppressWarnings("unchecked")

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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getOrders.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager
					.createQuery("from Theorder where user_id.id=:id and isAccepted='انتظار' order by createdAt desc");
			q1.setParameter("id", use1.getId());
			q1.setFirstResult((getOrders.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Theorder> orders = new ArrayList<>();
			List<Map<String, Object>> ret = new ArrayList<>();
			orders = q1.getResultList();
			for (Theorder o : orders) {
				Map<String, Object> map3 = new HashMap<>();
				try{
					map3.put("order_id", o.getId());
					map3.put("order_date", o.getCreatedAt());
					map3.put("dish_name", o.getDish_id().getName());
					map3.put("chief_name", o.getDish_id().getChief_id().getName());
					map3.put("chief_image_url", o.getDish_id().getChief_id().getPic());
			}catch(Exception e){
				map3.put("dish_name", null);
				map3.put("chief_name", null);
				map3.put("chief_image_url", null);
			}
				ret.add(map3);
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

	@SuppressWarnings("unchecked")
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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getOrders.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager
					.createQuery("from Theorder where user_id.id=:id and isAccepted='مقبول' order by createdAt desc");
			q1.setParameter("id", use1.getId());
			q1.setFirstResult((getOrders.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Theorder> orders = new ArrayList<>();
			List<Map<String, Object>> ret = new ArrayList<>();

			int i = 0;
			orders = q1.getResultList();
			for (Theorder o : orders) {
				Map<String, Object> map3 = new HashMap<>();
				try{
					map3.put("order_id", o.getId());
					map3.put("order_date", o.getCreatedAt());
					map3.put("dish_name", o.getDish_id().getName());
					map3.put("chief_name", o.getDish_id().getChief_id().getName());
					map3.put("chief_image_url", o.getDish_id().getChief_id().getPic());
				}catch(Exception e){
					map3.put("dish_name", null);
					map3.put("chief_name", null);
					map3.put("chief_image_url", null);
				}
				// ret.add(map);
				ret.add(i, map3);
				i++;

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

	//
	//
	// @SuppressWarnings("unchecked")
	// @Transactional
	// @RequestMapping("/getAcceptedOrders")
	// public @ResponseBody Map<String, Object> getAcceptedOrders(User user,
	// @RequestBody(required = false) User userBody) {
	//
	// Map<String, Object> map = new HashMap<>();
	// Map<String, Object> map2 = new HashMap<>();
	// // em.getTransaction().begin();
	// try {
	// if (userBody != null) {
	// user = userBody;
	// }
	// if (user.getApiToken() == null) {
	// map.put("status", 402);
	// return map;
	// }
	// if (user.getApiToken().length() < 68) {
	// map.put("status", 406);
	// return map;
	// }
	// User use1 = null;
	// Query q1 = entityManager.createQuery("from User where apiToken=:ap");
	// q1.setParameter("ap", user.getApiToken());
	// try {
	// use1 = (User) q1.getSingleResult();
	// } catch (NoResultException ex) {
	// map.put("status", 400);
	// return map;
	// }
	//
	// q1=entityManager.createQuery("from Theorder where user_id.id=:id and
	// isAccepted='مقبول'");
	// q1.setParameter("id", use1.getId());
	// List<Theorder> orders=new ArrayList<>();
	// List<Map<String,Object>> ret=new ArrayList<>();
	// orders=q1.getResultList();
	// for(Theorder o:orders){
	// map.put("order_id", o.getId());
	// map.put("dish_name", o.getDish_id().getName());
	// map.put("order_date", o.getCreatedAt());
	// map.put("chief_name", o.getDish_id().getChief_id().getName());
	// map.put("chief_image_url", o.getDish_id().getChief_id().getPic());
	// ret.add(map);
	// }
	// map2.put("status", 200);
	// map2.put("orders", ret);
	// return map2;
	// } catch (Exception e) {
	//
	// map2.put("status", 404);
	// // return map2;
	// throw e;
	// }
	// }

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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getOrders.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}

			q1 = entityManager
					.createQuery("from Theorder where user_id.id=:id and isAccepted='مرفوض' order by createdAt desc");
			q1.setParameter("id", use1.getId());
			q1.setFirstResult((getOrders.getPage()) * PAGINATION_GET_HELP);
			q1.setMaxResults(PAGINATION_GET_HELP);
			List<Theorder> orders = new ArrayList<>();
			List<Map<String, Object>> ret = new ArrayList<>();
			orders = q1.getResultList();
			for (Theorder o : orders) {
				Map<String, Object> map3 = new HashMap<>();
				try{
					map3.put("order_id", o.getId());
					map3.put("order_date", o.getCreatedAt());
					map3.put("dish_name", o.getDish_id().getName());
					map3.put("chief_name", o.getDish_id().getChief_id().getName());
					map3.put("chief_image_url", o.getDish_id().getChief_id().getPic());
			}catch(Exception e){
				map3.put("dish_name", null);
				map3.put("chief_name", null);
				map3.put("chief_image_url", null);
			}
				ret.add(map3);
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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", getOrder.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			q1 = entityManager.createQuery("from Theorder to where to.id=:oi and to.user_id.id=:iu");
			q1.setParameter("oi", getOrder.getOrderId());
			q1.setParameter("iu", use1.getId());
			Theorder theorder = null;
			try {
				theorder = (Theorder) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 401);
				return map;
			}
			
			try{
				map.put("status", 200);
				map.put("quantity", theorder.getQuantity());
				map.put("order_date", theorder.getCreatedAt());
				
				map.put("chief_name", theorder.getChief_id().getName());
				map.put("dish_name", theorder.getDish_id().getName());
				map.put("dish_image", theorder.getDish_id().getPic());
				map.put("total_prize", theorder.getDish_id().getPrice());
				map.put("chief_image", theorder.getChief_id().getPic());
				
			}catch(Exception e){
				map.put("chief_image", null);
				map.put("dish_name", null);
				map.put("dish_image", null);
				map.put("chief_name", null);
				map.put("total_prize", null);
			    map.put("chief_image", null);
			}
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

			if (updateInfo.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (updateInfo.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			//System.out.println("QQQQQQQQQQQQQQQQQQQQ"+updateInfo.getImage().getOriginalFilename().toString());
			if(updateInfo.getImage() != null){
				if(!utility.validate(updateInfo.getImage().getOriginalFilename().toString())){
					map.put("status", 404);
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
			Query q = entityManager.createQuery("from User where apiToken=:api");
			q.setParameter("api", updateInfo.getApiToken());
			User use = null;
			try {
				use = (User) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
//			if( updateInfo.getEmail()!=null){
//		    q = entityManager.createQuery("from User where email=:em");
//			q.setParameter("em", updateInfo.getEmail());
//			User use2 = null;
//			try {
//				use2 = (User) q.getSingleResult();
//				if(!use.getApiToken().equals(use2.getApiToken())){
//					map.put("status", 401);
//					return map;
//				}
//			} catch (NoResultException ex) {
//				
//			}
//			}
			
			use.setName(updateInfo.getName());
			
			//	use.setEmail(updateInfo.getEmail());
			
			if (updateInfo.getImage() != null)
				//use.setPic(utility.uploadFile(updateInfo.getImage()));
			//use.setPic(utility.image(updateInfo.getImage()));
				use.setPic(upload.uploadFile(updateInfo.getImage()));
			entityManager.persist(use);
			map.put("status", 200);
			return map;

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
	}

	@Transactional
	@RequestMapping("/updatePhone")
	public @ResponseBody Map<String, Object> updatePhone(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			if (user.getPhone() == null || user.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (user.getPhone().length() < 9) {
				map.put("status", 406);
				return map;
			} else if (user.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}

			Query q = entityManager.createQuery("from User where apiToken=:api");
			q.setParameter("api", user.getApiToken());
			User use = null;
			try {
				use = (User) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			
			q = entityManager.createQuery("from User where tempPhone=:ph");
			q.setParameter("ph", user.getPhone());
			User use2 = null;
			try {
				use2 = (User) q.getSingleResult();
				map.put("status", 401);
				return map;
			} catch (NoResultException ex) {
				
			}
			q = entityManager.createQuery("from User where phone=:ph");
			q.setParameter("ph", user.getPhone());
			User use1 = null;
			try {
				use1 = (User) q.getSingleResult();
				map.put("status", 401);
				return map;
			} catch (NoResultException ex) {
				use.setTempPhone(user.getPhone());
				SecureRandom rand = new SecureRandom();
				 int num = rand.nextInt(100000);
				 String formatted = String.format("%05d", num);
				 int verificationCode1 = Integer.valueOf(formatted);
				//int verificationCode1 = 12345;
				
				use.setVerificationCode(verificationCode1);
				entityManager.persist(use);
				apiSend= new ApiSendMessage();
				
				apiSend.setUserName(new String(userName));
				apiSend.setNumbers(prepareTelephoneNumber(user.getPhone()));
				apiSend.setMessage("Your Verification Code Is : "+verificationCode1);
				apiSend.setPassword("1475321");
				apiSend.setSender("admin");
//				SimpleDateFormat timeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//				apiSend.setDatetime(timeDate.format(new Date()));
				apiSend.setUnicode("E");
				apiSend.setReturnValue("full");
				
				

				sendMessageApi.sendSms(apiSend);
				map.put("status", 200);
				return map;
				/////////////////////
				////// send sms//////
				///////////////////
			}

		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}

	}

	@Transactional
	@RequestMapping("/validatePhone")
	public @ResponseBody Map<String, Object> validatePhone(User user, @RequestBody(required = false) User userBody) {

		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}
			if (user.getVerificationCode() == null || user.getApiToken() == null) {
				map.put("status", 403);
				return map;
			} else if (user.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}

			Query q = entityManager.createQuery("from User where apiToken=:api");
			q.setParameter("api", user.getApiToken());
			User use = null;
			try {
				use = (User) q.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			try {
				if (user.getVerificationCode().equals(use.getVerificationCode()) && use.getTempPhone() != null) {
					use.setPhone(use.getTempPhone());
					use.setVerificationCode(null);
					use.setTempPhone(null);
					entityManager.persist(use);
					map.put("status", 200);
					return map;
				} else if (!user.getVerificationCode().equals(use.getVerificationCode())) {
					map.put("status", 401);
					return map;
				}
			} catch (Exception e) {
				map.put("status", 404);
				return map;
			}
		} catch (Exception e) {

			map.put("status", 404);
			return map;
			// throw e;
		}
		map.put("status", 404);
		return map;
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
			User use1 = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", aboutus.getApiToken());
			try {
				use1 = (User) q1.getSingleResult();
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
			User use = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				use = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			q1 = entityManager.createQuery("from Message where user_id.id=:ci order by createdAt desc");
			q1.setParameter("ci", use.getId());
			q1.setFirstResult((pagination.getPage()) * 10);
			q1.setMaxResults(10);
			List<Message> messages = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			Integer[] chieArr= new Integer[messages.size()];
			int userId=0;
			int chiefId=0;
			int count=0,found=0;
			for (Message m : messages) {
				found=0;
				chieArr[count]=m.getChief_id().getId();
				for(int i=0;i<chieArr.length;i++){
					
					if(m.getChief_id().getId()==chieArr[i]){
						found++;
					}
					if(found>3){
						break;
					}
				}
				Map<String, Object> oneObjectMap = new HashMap<>();
				if(found<=1){
				oneObjectMap.put("chief_id", m.getChief_id().getId());
				oneObjectMap.put("message_date", m.getCreatedAt());
				if (m.getMsgContent().length() > 50)
					oneObjectMap.put("message_content", m.getMsgContent().substring(0, 50));
				else
					oneObjectMap.put("message_content", m.getMsgContent());
				if( m.getIsSeen()==true)
					oneObjectMap.put("is_seen", 1);
				else if(m.getIsSeen()==false)
					oneObjectMap.put("is_seen", 0);
				else
					oneObjectMap.put("is_seen", null);
				if( m.getIsUserSender()==true)
				oneObjectMap.put("sender_type", 0);
				if( m.getIsUserSender()==false)
					oneObjectMap.put("sender_type", 1);
				if(m.getIsUserSender()==null)
					oneObjectMap.put("sender_type",null);
				oneObjectMap.put("chief_name", m.getChief_id().getName());
				oneObjectMap.put("chief_image_url", m.getChief_id().getPic());
				// oneObjectMap.put("user_name", m.getChief_id().getName());
				ret.add(oneObjectMap);
				}
				userId=m.getUser_id().getId();
				chiefId=m.getChief_id().getId();
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
			//return map2;
			 throw e;
		}
	}

	@Transactional
	@RequestMapping("/getChiefMessages")
	public @ResponseBody Map<String, Object> getChiefMessages(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			if (pagination.getApiToken() == null || pagination.getPage() == null || pagination.getChiefId() == null) {
				map.put("status", 403);
				return map;
			}
			if (pagination.getApiToken().length() < 68) {
				map.put("status", 405);
				return map;
			}
			User use = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				use = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			Chief chie = null;
			chie=entityManager.find(Chief.class, pagination.getChiefId());
			if(chie==null){
				map.put("status", 401);
				return map;
			}
			q1 = entityManager.createQuery(
					"from Message where user_id.id=:ci and chief_id.id=:ui order by createdAt desc");
			q1.setParameter("ci", use.getId());
			q1.setParameter("ui", pagination.getChiefId());
			q1.setFirstResult((pagination.getPage()) * 15);
			q1.setMaxResults(15);
			List<Message> messages = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
//			Integer[] useArr=new Integer[messages.size()];
//			Integer[] chieArr=new Integer[messages.size()];
//			int count=0,found=0;
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
			//		if(found<=1){
					// oneObjectMap.put("user_id", m.getChief_id().getId());
					// oneObjectMap.put("message_date", m.getCreatedAt());
					if (m.getMsgContent().length() > 50)
						oneObjectMap.put("message_content", m.getMsgContent().substring(0, 50));
					else
						oneObjectMap.put("message_content", m.getMsgContent());
					// oneObjectMap.put("is_seen", m.getIsSeen());
					if(m.getIsUserSender()==true)
					oneObjectMap.put("sender_type",0);
					if(m.getIsUserSender()==false)
						oneObjectMap.put("sender_type",1);
					if(m.getIsUserSender()==null)
						oneObjectMap.put("sender_type",null);
					// oneObjectMap.put("user_name", m.getChief_id().getName());
					/// oneObjectMap.put("user_image_url",
					// m.getChief_id().getPic());
					// oneObjectMap.put("user_name", m.getChief_id().getName());
					ret.add(oneObjectMap);
//				
//					count++;
//					}
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
			User use = null;
			Query q1 = entityManager.createQuery("from User where apiToken=:ap");
			q1.setParameter("ap", pagination.getApiToken());
			try {
				use = (User) q1.getSingleResult();
			} catch (NoResultException ex) {
				map.put("status", 400);
				return map;
			}
			q1 = entityManager.createQuery("from NotifiedPerson where user_id.id=:ci order by createdAt desc");
			q1.setParameter("ci", use.getId());
			q1.setFirstResult((pagination.getPage()) * 10);
			q1.setMaxResults(10);
			List<NotifiedPerson> notifications = q1.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();

			for (NotifiedPerson n : notifications) {
				Map<String, Object> oneObjectMap = new HashMap<>();
				
				oneObjectMap.put("notification_id", n.getNotification_id().getId());
				
				oneObjectMap.put("notification_type", n.getNotification_id().getNotificationType_id().getId());
				oneObjectMap.put("notification_date", n.getNotification_id().getCreatedAt());
				oneObjectMap.put("is_seen", n.getIsSeen());
				if(n.getNotification_id().getChief_id().getName()==null)
					oneObjectMap.put("chief_name", null);
				else
					oneObjectMap.put("chief_name", n.getNotification_id().getChief_id().getName());
				if(n.getNotification_id().getChief_id().getPic()==null)
					oneObjectMap.put("chief_image_url",null);
				else
				    oneObjectMap.put("chief_image_url", n.getNotification_id().getChief_id().getPic());
				if(n.getNotification_id().getNotificationType_id().getId()==1)
					oneObjectMap.put("item_id",n.getNotification_id().getDish_id().getId());
				if(n.getNotification_id().getNotificationType_id().getId()==2)
					oneObjectMap.put("item_id",n.getNotification_id().getVideo_id().getId());
				if(n.getNotification_id().getNotificationType_id().getId()==3||n.getNotification_id().getNotificationType_id().getId()==4)
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
			//return map2;
			 throw e;
		}
	}

	@Transactional
	@RequestMapping(value="upload", consumes = MediaType.ALL_VALUE)
	public @ResponseBody Map<String, Object> upload(AddDish addDish) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			
			
			String str=upload.uploadFile(addDish.getImage());
			map.put("image", str);
			return map;
		} catch (Exception e) {

			map.put("status", 404);
			//return map2;
			 throw e;
		}
	}
	@Transactional
	@RequestMapping(value="space", consumes = MediaType.ALL_VALUE)
	public @ResponseBody Map<String, Object> space(Pagination pagination,
			@RequestBody(required = false) Pagination paginationBody) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (paginationBody != null) {
				pagination = paginationBody;
			}
			
			String str=pagination.getApiToken();
			//str=str.replaceAll("[ ()]", "");
			str=str.replaceAll("\\W+", "");
			map.put("str", str);
			return map;
		} catch (Exception e) {

			map.put("status", 404);
			//return map2;
			 throw e;
		}
	}
}
