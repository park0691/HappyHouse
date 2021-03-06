package com.ssafy.happyhouse.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ssafy.happyhouse.dto.HouseResultDto;
import com.ssafy.happyhouse.dto.UserDto;
import com.ssafy.happyhouse.dto.UserResultDto;

public interface UserService {
	public UserResultDto userRegister(UserDto userDto);
	
	public UserResultDto userModify(UserDto userDto);
	public UserResultDto userPasswordModify(UserDto userDto);
	
	public UserResultDto userDelete(UserDto userDto);
	
	public UserResultDto userIdCheck(String userId);
	
	public UserResultDto login(UserDto UserDto);
	
	public UserResultDto findPassword(UserDto UserDto);
	public UserResultDto updatePassword(UserDto userDto);
	
	public UserResultDto userFileInsert(UserDto userDto, MultipartHttpServletRequest request);
	
	public UserResultDto friendSearch(String searchWord, UserDto userDto); // 친구 찾기
	
}
