package com.ssafy.happyhouse.service;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ssafy.happyhouse.dao.FriendDao;
import com.ssafy.happyhouse.dao.UserDao;
import com.ssafy.happyhouse.dto.FriendDto;
import com.ssafy.happyhouse.dto.UserDto;
import com.ssafy.happyhouse.dto.UserImgFileDto;
import com.ssafy.happyhouse.dto.UserResultDto;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private FriendDao friendDao;

	private static final int SUCCESS = 1;	
	private static final int INCORRECT_INFO = 2;
	private static final int DUPLICATED = 3; 
	private static final int NOT_DUPLICATED = 4; 
	private static final int FAIL = -1;
	
	@Value("${app.fileupload.uploadDir}")
	private String uploadFolder;

	@Value("${app.fileupload.uploadPath}")
	private String uploadPath;

	@Override
	public UserResultDto userRegister(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			if (userDao.userRegister(userDto) == 1) {
				userResultDto.setDto(userDto);
				userResultDto.setResult(SUCCESS);
			} else {
				userResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}

	@Override
	public UserResultDto userModify(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			if (userDao.userModify(userDto) == 1) {
				userResultDto.setDto(userDto);
				userResultDto.setResult(SUCCESS);
			} else {
				userResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}

	@Override
	public UserResultDto userPasswordModify(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			if (userDao.userPasswordModify(userDto) == 1) {
				userResultDto.setDto(userDto);
				userResultDto.setResult(SUCCESS);
			} else {
				userResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}
	
	@Override
	public UserResultDto userDelete(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			if (userDao.userDelete(userDto) == 1) {
				userResultDto.setResult(SUCCESS);
			} else {
				userResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}
	
	@Override
	public UserResultDto userIdCheck(String userId){
		UserResultDto userResultDto = new UserResultDto();
		try {
			if (userDao.userIdCheck(userId) == 1) {
				userResultDto.setResult(DUPLICATED);
			} else {
				userResultDto.setResult(NOT_DUPLICATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}
	
	@Override
	public UserResultDto login(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			UserDto resultDto = userDao.login(userDto.getUserId());
			
			if (resultDto != null && resultDto.getUserPassword().equals(userDto.getUserPassword())) {
				userResultDto.setDto(resultDto);
				userResultDto.setResult(SUCCESS);
			} else {
				userResultDto.setResult(FAIL);
			}
		} catch(Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}
	
	@Override
	public UserResultDto findPassword(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		System.out.println("findPw");
//		System.out.println(userDto);
		try {
			UserDto findPwResultDto = userDao.findPassword(userDto.getUserId());
//			System.out.println(findPwResultDto);
			if (findPwResultDto != null && findPwResultDto.getUserEmail().equals(userDto.getUserEmail())) {
				// ?????? PW??? ??????
				final String newPassword = getRandomPassword(12);
				findPwResultDto.setUserPassword(newPassword);
				// ????????? PW??? DB??? ?????? ???, ?????? ??????
				// ?? ????????????..??
				if (userDao.updatePassword(findPwResultDto) == 1 && sendInitPwEmail(findPwResultDto)) {
					userResultDto.setResult(SUCCESS);
				} else {
					userResultDto.setResult(FAIL);
				}
			} else {
				userResultDto.setResult(INCORRECT_INFO);
			}
		} catch(Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}

	@Override
	public UserResultDto updatePassword(UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			if (userDao.updatePassword(userDto) == 1) {
				userResultDto.setDto(userDto);
				userResultDto.setResult(SUCCESS);
			} else {
				userResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}
	

	private static String getRandomPassword(int len) {

        char[] charSet = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '@', '#', '$', '%', '^', '&', '!', '*'
        };
        StringBuilder sb = new StringBuilder(len);
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(charSet[random.nextInt(charSet.length)]);
        }
        return sb.toString();
    }
	
	private static boolean sendInitPwEmail(UserDto userDto) {

		final String charSet = "utf-8";
        final String hostSMTP = "smtp.naver.com";
        final String hostSMTPid = "pde0691";
        final String hostSMTPpwd = "ssafy6th!";

        final String fromEmail = "pde0691@naver.com";
        final String fromName = "HAPPY HOUSE";
        final String subject = "Happy House ?????? ???????????? ????????? ???????????????.";
        String msg = "<div style='border: 1px solid black; padding: 10px; font-family: verdana;'>";
        msg += "<h2>???????????????. <span style='color: blue;'>" + userDto.getUserName() + "</span>???.</h2>";
        msg += "<p>???????????? ??????????????? ????????? ????????????. ??????????????? ???????????? ???????????????.</p>";
        msg += "<p>?????? ???????????? : <span style='color: blue;'>" + userDto.getUserPassword() + "</span></p></div>";

        try {
            HtmlEmail email = new HtmlEmail();
            email.setDebug(true);
            email.setCharset(charSet);
            email.setSSLOnConnect(true);
            email.setHostName(hostSMTP);
            email.setSmtpPort(587);

            email.setAuthentication(hostSMTPid, hostSMTPpwd);
            email.setStartTLSEnabled(true);
            email.addTo(userDto.getUserEmail(), userDto.getUserName(), charSet);
            email.setFrom(fromEmail, fromName, charSet);
            email.setSubject(subject);
            email.setHtmlMsg(msg);
            email.send();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	@Override
	public UserResultDto userFileInsert(UserDto userDto, MultipartHttpServletRequest request) {
		UserResultDto userResultDto = new UserResultDto();
		try {
			int userSeq = userDto.getUserSeq();
			// ?????? ?????? ??????
	        File uploadDir = new File(uploadPath + File.separator + uploadFolder);
	        if (!uploadDir.exists()) uploadDir.mkdir();
	        
	        // ?????? ?????? ?????? (?????? ???)
	        String fileUrl = userDto.getUserProfileImage();
	        if (fileUrl != null && !fileUrl.isEmpty()) {
	        	File file = new File(uploadPath + File.separator, fileUrl);
	            if (file.exists()) file.delete();
	        }
	        
	        // ?????? ??????????????? ??????
	        userDao.userImgFileDelete(userSeq);
	        MultipartFile part = request.getFile("profileImg");
            String fileName = part.getOriginalFilename();
            
            // Random File Id
            UUID uuid = UUID.randomUUID();
            
            // file ?????????
            String extension = FilenameUtils.getExtension(fileName); // vs FilenameUtils.getBaseName()

            String savingFileName = uuid + "." + extension;
        
            File destFile = new File(uploadPath + File.separator + uploadFolder + File.separator + savingFileName);
            
//            System.out.println("??????????????????");
            System.out.println(uploadPath + File.separator + uploadFolder + File.separator + savingFileName);
            part.transferTo(destFile);
        
            // Table Insert
            UserImgFileDto userFileDto = new UserImgFileDto();
	        userFileDto.setUserSeq(userDto.getUserSeq());
            userFileDto.setFileName(fileName);
            userFileDto.setFileSize(part.getSize());
            userFileDto.setFileContentType(part.getContentType());
            String userFileUrl = "/" + uploadFolder + "/" + savingFileName;
            userFileDto.setFileUrl(userFileUrl);
//            System.out.println(userFileDto);
	        userDao.userImgFileInsert(userFileDto);
	        userResultDto.setUploadProfileImgUrl(userFileUrl);
	        userResultDto.setResult(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}

	@Override
	public UserResultDto friendSearch(String searchWord, UserDto userDto) {
		UserResultDto userResultDto = new UserResultDto();
		List<UserDto> list = null;
		try {
			list = userDao.friendSearch(searchWord);
			
			List<FriendDto> friendList = friendDao.friendFollowing(userDto.getUserId());
			
			for (UserDto user : list) {
				for (FriendDto friend : friendList) {
					if (user.getUserId().equals(friend.getToId())) {
						user.setFriend(true);
					}
				}
				if (user.getUserId().equals(userDto.getUserId())) {
					user.setSameUser(true);
				}
			}

			userResultDto.setUserDto(list);
			userResultDto.setResult(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			userResultDto.setResult(FAIL);
		}
		return userResultDto;
	}
}
