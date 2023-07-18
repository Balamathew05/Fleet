package com.cab.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cab.Exception.AdminException;
import com.cab.Exception.CurrentUserSessionException;
import com.cab.Exception.CustomerException;
import com.cab.Exception.TripBookingException;
import com.cab.Model.User;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
public class UserServiceImpl {

	/*
	 * @Autowired private CurrentUserSessionRepo currRepo;
	 */
	@Autowired
	private JavaMailSender mailSenderObj;
	@Autowired
    private RedisTemplate redisTemplate;
	
	
	public void insertUser(User user) throws AdminException, AddressException, MessagingException {

		if (user.getUserRole().equalsIgnoreCase("Admin")) {

			user.setUuid("A" + RandomStringUtils.randomNumeric(6));

		} else if (user.getUserRole().equalsIgnoreCase("Customer")) {
			user.setUuid("C" + RandomStringUtils.randomNumeric(6));
		} else if (user.getUserRole().equalsIgnoreCase("Driver")) {
			user.setUuid("D" + RandomStringUtils.randomNumeric(6));
		}
		// Generate otp
		String otp = RandomStringUtils.randomNumeric(6);
//		Properties prop = new Properties();
//		prop.put("mail.smtp.auth", true);
//		prop.put("mail.smtp.starttls.enable", "true");
//		prop.put("mail.smtp.host", "smtp.gmail.com");
//		prop.put("mail.smtp.port", "587");
//		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		String username = "balamathew03@gamil.com";
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(username);
		message.setTo(user.getEmail());
		message.setSubject("Registration OTP");
		String msg = "Your one time password is : " + otp;
		message.setText(msg);
		mailSenderObj.send(message);
//		sendMail(user, otp, prop);
	}

//	public void sendMail(User user, String otp, Properties prop) throws MessagingException, AddressException {
//		String username = "balamathew03@gamil.com";
//		String password = "pznoaslcvlwiqfhg";
//		Session session = Session.getInstance(prop, new Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(username,password);
//			}
//		});
//		Message message = new MimeMessage(session);
//		message.setFrom(new InternetAddress(username));
//		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
//		message.setSubject("Registration OTP");
//
//		String msg = "Your one time password is : " + otp;
//
//		MimeBodyPart mimeBodyPart = new MimeBodyPart();
//		mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
//
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(mimeBodyPart);
//
//		message.setContent(multipart);
////		HashOperations hashOperations = redisTemplate.opsForHash();
////		redisTemplate.opsForHash().put(user.getEmail(),otp,user);
//		Transport.send(message);
//	
////	cache
//	
//	}
	
	
	
//	}
//	
//
//	@Override
//	public Admin updateAdmin(Admin admin, String uuid) throws AdminException, CurrentUserSessionException {
//
//		Optional<CurrentUserSession> validCustomer = currRepo.fifindByUuidndByUuidAndRole(uuid);
//		if (validCustomer.isPresent()) {
//			Optional<Admin> adn = adminRepo.findByEmail(admin.getEmail());
//			if (adn.isPresent()) {
//				Admin forUpdate = adn.get();
//				forUpdate.setAddress(admin.getAddress());
//				forUpdate.setMobileNumber(admin.getMobileNumber());
//				forUpdate.setPassword(admin.getPassword());
//				forUpdate.setUserName(admin.getUserName());
//				adminRepo.save(forUpdate);
//				return forUpdate;
//			} else {
//				throw new AdminException("Admin with this Credential is not present");
//			}
//		} else {
//			throw new CurrentUserSessionException("Admin is Not Logged In");
//		}
//	}
//
//	@Override
//	public Admin deleteAdmin(Integer adminId, String uuid) throws AdminException, CurrentUserSessionException {
//
//		Optional<CurrentUserSession> validCustomer = currRepo.fifindByUuidndByUuidAndRole(uuid);
//		if (validCustomer.isPresent()) {
//			Optional<Admin> adn = adminRepo.findById(adminId);
//			if (adn.isPresent()) {
//				Admin forDelete = adn.get();
//				adminRepo.delete(forDelete);
//				return forDelete;
//			} else {
//				throw new AdminException("Admin with this Credential is not present");
//			}
//		} else {
//			throw new CurrentUserSessionException("Admin is Not Logged In");
//		}
//	}
//
//	@Override
//	public List<TripBooking> getAllTrips(String uuid)
//			throws AdminException, TripBookingException, CurrentUserSessionException {
//
//		Optional<CurrentUserSession> validCustomer = currRepo.fifindByUuidndByUuidAndRole(uuid);
//		if (validCustomer.isPresent()) {
//			List<TripBooking> allTrips = tripbookingRepo.findAll();
//			if (allTrips.isEmpty()) {
//				throw new TripBookingException("No Trip is Booked Currently By any Customer");
//			} else {
//				return allTrips;
//			}
//		} else {
//			throw new CurrentUserSessionException("Admin is Not Logged In Or User is not Admin");
//		}
//	}
//
//	@Override
//	public List<TripBooking> getTripsCabwise(String carType, String uuid)
//			throws TripBookingException, CurrentUserSessionException {
//
//		Optional<CurrentUserSession> validCustomer = currRepo.fifindByUuidndByUuidAndRole(uuid);
//		if (validCustomer.isPresent()) {
//			List<TripBooking> allTrips = tripbookingRepo.findAll();
//			if (allTrips.isEmpty()) {
//				throw new TripBookingException("No Trip is Booked Currently By any Customer");
//			} else {
//				List<TripBooking> cabWiseTrips = new ArrayList<>();
//				for (TripBooking tb : allTrips) {
//					if (tb.getCab().getCarType().equalsIgnoreCase(carType)) {
//						cabWiseTrips.add(tb);
//					}
//				}
//				if (cabWiseTrips.isEmpty()) {
//					throw new TripBookingException("No Trip Found With this carType");
//				} else {
//					return cabWiseTrips;
//				}
//			}
//		} else {
//			throw new CurrentUserSessionException("Admin is Not Logged In");
//		}
//	}
//
//	@Override
//	public List<TripBooking> getTripsCustomerwise(Integer customerId, String uuid)
//			throws TripBookingException, CustomerException, CurrentUserSessionException {
//		Optional<CurrentUserSession> validCustomer = currRepo.fifindByUuidndByUuidAndRole(uuid);
//		if (validCustomer.isPresent()) {
//			Optional<Customer> cust = customerRepo.findById(customerId);
//			if (cust.isPresent()) {
//				Customer customer = cust.get();
//				List<TripBooking> customerTrips = customer.getTripBooking();
//				if (customerTrips.isEmpty()) {
//					throw new CustomerException("No Trip Bookked by the customer");
//				} else {
//					return customerTrips;
//				}
//			} else {
//				throw new CustomerException("Customer with this Credential is not present");
//			}
//		} else {
//			throw new CurrentUserSessionException("Admin is Not Logged In Or User is not Admin");
//		}
//	}
//
//	@Override
//	public List<TripBooking> getAllTripsForDays(Integer customerId, String fromDateTime, String toDateTime, String uuid)
//			throws TripBookingException, CustomerException, CurrentUserSessionException {
//
//		Optional<CurrentUserSession> validCustomer = currRepo.fifindByUuidndByUuidAndRole(uuid);
//		if (validCustomer.isPresent()) {
//			Optional<Customer> cust = customerRepo.findById(customerId);
//			if (cust.isPresent()) {
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//				LocalDateTime fromDT = LocalDateTime.parse(fromDateTime, formatter);
//				LocalDateTime toDT = LocalDateTime.parse(toDateTime, formatter);
//				Customer customer = cust.get();
//				List<TripBooking> customerTrips = customer.getTripBooking();
//				for (TripBooking tb : customerTrips) {
//					LocalDateTime currentTripfromDT = LocalDateTime.parse(tb.getFromDateTime(), formatter);
//					LocalDateTime currentTriptoDT = LocalDateTime.parse(tb.getToDateTime(), formatter);
//					if (currentTripfromDT.isAfter(fromDT) && currentTriptoDT.isBefore(toDT)) {
//						customerTrips.add(tb);
//					}
//				}
//
//				if (customerTrips.isEmpty()) {
//					throw new TripBookingException("No Trip has been booked in between of the given Dates");
//				} else {
//					return customerTrips;
//				}
//			} else {
//				throw new CustomerException("No Customer Found with this Credentials");
//			}
//		} else {
//			throw new CurrentUserSessionException("Admin is Not Logged In");
//		}
//	}
//
//	

}
