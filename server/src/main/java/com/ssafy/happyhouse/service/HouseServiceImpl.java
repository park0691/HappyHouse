package com.ssafy.happyhouse.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.happyhouse.dao.BookMarkDao;
import com.ssafy.happyhouse.dao.HouseDao;
import com.ssafy.happyhouse.dto.HouseDealDto;
import com.ssafy.happyhouse.dto.HouseDetailDto;
import com.ssafy.happyhouse.dto.HouseOnGoingDto;
import com.ssafy.happyhouse.dto.HouseOnGoingParamDto;
import com.ssafy.happyhouse.dto.HouseOnGoingResultDto;
import com.ssafy.happyhouse.dto.HouseResultDto;
import com.ssafy.happyhouse.dto.HouseReviewDto;
import com.ssafy.happyhouse.dto.HouseReviewParamDto;
import com.ssafy.happyhouse.dto.HouseReviewResultDto;
import com.ssafy.happyhouse.dto.UserDto;

@Service
public class HouseServiceImpl implements HouseService {

	@Autowired
	private HouseDao houseDao;
	@Autowired
	private BookMarkDao bookmarkDao;
	
	private static final int SUCCESS = 1;
	private static final int FAIL = -1;
	
	// 매물 검색 (동이름)
	@Override
	public HouseResultDto getHouseDongDetail(String dongString) {
		HouseResultDto houseResultDto = new HouseResultDto();
		List<HouseDetailDto> list = null;
		try {
			list = houseDao.getHouseDongDetail(dongString);
			houseResultDto.setHouseDetailDto(list);
			houseResultDto.setResult(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			houseResultDto.setResult(FAIL);
		}
		return houseResultDto;
	}
	
	// 매물 검색 (동 + 아파트 이름)
	@Override
	public HouseResultDto getHouseSearchDetail(String searchWord) {
		HouseResultDto houseResultDto = new HouseResultDto();
		List<HouseDetailDto> list = null;
		try {
			list = houseDao.getHouseSearchDetail(searchWord);
			houseResultDto.setHouseDetailDto(list);
			houseResultDto.setResult(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			houseResultDto.setResult(FAIL);
		}
		return houseResultDto;
	}

	// 매물 실거래가 조회
	@Override
	public HouseResultDto getHouseDeal(int houseNo) {
		HouseResultDto houseResultDto = new HouseResultDto();
		List<HouseDealDto> list = null;
		try {
			list = houseDao.getHouseDeal(houseNo);
			houseResultDto.setHouseDealDto(list);
			houseResultDto.setResult(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			houseResultDto.setResult(FAIL);
		}
		return houseResultDto;
	}

	// 매물 등록
	@Override
	public HouseOnGoingResultDto houseOnGoingRegister(HouseOnGoingDto houseDto) {
		HouseOnGoingResultDto houseOnGoingResultDto = new HouseOnGoingResultDto();
		try {
			if (houseDao.houseOnGoingRegister(houseDto) == 1) {
				houseOnGoingResultDto.setDto(houseDto);
				houseOnGoingResultDto.setResult(SUCCESS);
			} else {
				houseOnGoingResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			houseOnGoingResultDto.setResult(FAIL);
		}
		return houseOnGoingResultDto;
	}

	// 등록된 매물 자세히 보기
	@Override
	public HouseOnGoingResultDto houseOnGoingDetail(HouseOnGoingParamDto houseOnGoingParamDto) {
		HouseOnGoingResultDto houseOnGoingResultDto = new HouseOnGoingResultDto();
		
		try {
			HouseOnGoingDto houseOnGoingDto = houseDao.houseOnGoingDetail(houseOnGoingParamDto);
			if(houseOnGoingDto.getCompSeq() != 0 && houseOnGoingDto.getCompSeq() == houseOnGoingParamDto.getCompSeq()){
				houseOnGoingDto.setSameUser(true);
			}else {
				houseOnGoingDto.setSameUser(false);
			}			
			houseOnGoingResultDto.setDto(houseOnGoingDto);
			houseOnGoingResultDto.setResult(SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
			houseOnGoingResultDto.setResult(FAIL);
		}
		return houseOnGoingResultDto;
	}

	// 등록된 매물 리스트(전체)
	@Override
	public HouseOnGoingResultDto houseOnGoingList(HouseOnGoingParamDto houseOnGoingParamDto) {
		HouseOnGoingResultDto houseOnGoingResultDto = new HouseOnGoingResultDto();
	    
	    try {
	        List<HouseOnGoingDto> list = houseDao.houseOnGoingList(houseOnGoingParamDto);
	        int count = houseDao.houseOnGoingListTotalCount();

	        houseOnGoingResultDto.setList(list);
	        houseOnGoingResultDto.setCount(count);
	        houseOnGoingResultDto.setResult(SUCCESS);
	        
	    }catch(Exception e) {
	        e.printStackTrace();
	        houseOnGoingResultDto.setResult(FAIL);
	    }
	    return houseOnGoingResultDto;
	}
	
	// 등록된 매물 리스트(특정 매물 클릭)
	@Override
	@Transactional
	public HouseOnGoingResultDto houseNoOnGoingList(int houseNo, UserDto userDto) {
		HouseOnGoingResultDto houseOnGoingResultDto = new HouseOnGoingResultDto();
	    
	    try {
	        List<HouseOnGoingDto> list = houseDao.houseNoOnGoingList(houseNo);
	        int count = houseDao.houseNoOnGoingListTotalCount(houseNo);   
	        System.out.println("NOONGOING   " + userDto);
	        // 세션이 있다면, 해당 사용자의 북마크 데이터인지 여부 판별
	        if (userDto != null) {
	        	List<HouseOnGoingDto> userBookMarkList = bookmarkDao.getBookmarkHouseOngoingListById(userDto.getUserId());
	        	
	        	for (HouseOnGoingDto item : userBookMarkList) {
	        		for (HouseOnGoingDto innerItem : list) {
	        			if (item.getHouseNo() == innerItem.getHouseNo()) {
	        				innerItem.setBookmark(true);
	        			}
	        		}
	        	}
	        }
	        
	        houseOnGoingResultDto.setList(list);
	        houseOnGoingResultDto.setCount(count);
	        
	        houseOnGoingResultDto.setResult(SUCCESS);
	        
	    }catch(Exception e) {
	        e.printStackTrace();
	        houseOnGoingResultDto.setResult(FAIL);
	    }
	    return houseOnGoingResultDto;
	}
	


	// 등록된 매물 리스트(5개)
	@Override
	public HouseOnGoingResultDto houseOnGoingLimitList(HouseOnGoingParamDto houseOnGoingParamDto) {
		HouseOnGoingResultDto houseOnGoingResultDto = new HouseOnGoingResultDto();
	    
	    try {
	        List<HouseOnGoingDto> list = houseDao.houseOnGoingLimitList(houseOnGoingParamDto);

	        houseOnGoingResultDto.setList(list);
	        houseOnGoingResultDto.setResult(SUCCESS);
	        
	    }catch(Exception e) {
	        e.printStackTrace();
	        houseOnGoingResultDto.setResult(FAIL);
	    }
	    return houseOnGoingResultDto;
	}
	
	// 리뷰 등록
	@Override
	public HouseReviewResultDto houseReviewRegister(HouseReviewDto houseReviewDto, HttpServletRequest request) {
		 HouseReviewResultDto houseReviewResultDto = new HouseReviewResultDto();
		try {
			if (houseDao.houseReviewRegister(houseReviewDto) == 1) {
				houseReviewResultDto.setDto(houseReviewDto);
				houseReviewResultDto.setResult(SUCCESS);
			} else {
				houseReviewResultDto.setResult(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			houseReviewResultDto.setResult(FAIL);
		}
		return houseReviewResultDto;
	}

	// 리뷰 조회
	@Override
	public HouseReviewResultDto houseReviewList(HouseReviewParamDto houseReviewParamDto) {
		HouseReviewResultDto houseReviewResultDto = new HouseReviewResultDto();
		
		try {
			List<HouseReviewDto> houseReviewList = houseDao.houseReviewList(houseReviewParamDto);
			
			if (houseReviewParamDto.getUserSeq() != 0 && houseReviewList != null && !houseReviewList.isEmpty()) {
				for (HouseReviewDto houseReviewDto : houseReviewList) {
					if(houseReviewParamDto.getUserSeq() == houseReviewDto.getUserSeq()){
						houseReviewDto.setSameUser(true);
					}else {
						houseReviewDto.setSameUser(false);
					}
				}
			}
			
			houseReviewResultDto.setList(houseReviewList);
			houseReviewResultDto.setResult(SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
			houseReviewResultDto.setResult(FAIL);
		}
		return houseReviewResultDto;
	}
}
