package edu.example.tripnote.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import edu.example.tripnote.dao.AttractionsDAO;
import edu.example.tripnote.dao.TripDAO;
import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.trip.AttractionDTO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.MembersDTO;
import edu.example.tripnote.domain.trip.NoteDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;
import edu.example.tripnote.domain.trip.TripInfoDTO;
import edu.example.tripnote.util.Attractions;

@Controller
@RequestMapping("/trip")
public class TripController {

	private final String CLIENT_ID = "vy9ul8xfsx";
    private final String CLIENT_SECRET = "5kb2wpyLJiQ7wTpJbtZo0ZYxd8Zd1T2OAVErw5Cn";
    private String ODSAY_API_KEY = "qGXcPcWqZb17HoE%2FFsuIeg";
	@Autowired
	private AttractionsDAO attDao;
	
	@Autowired
	private TripDAO tripDao;
	
	
	
	@RequestMapping("")
	public String goTrip() {
		return "trip/index";
	}
	
	@RequestMapping("/plan")
	public String goPlan(TripInfoDTO tripInfoDTO,
						 @ModelAttribute CourseDTO courseDTO, int userId,
						 String courseTitle,
						 RedirectAttributes redirectAttributes,
						 HttpSession session) {
		
		if(session.getAttribute("courseDTO") != null)
			session.removeAttribute("courseDTO");
		courseDTO.setTitle(courseTitle);
		courseDTO.setStartDate(tripInfoDTO.getStartDate());
		courseDTO.setEndDate(tripInfoDTO.getEndDate());
		courseDTO.setUserId(userId);
		System.out.println(userId);
		courseDTO.setAreaId(tripDao.getAreaId(tripInfoDTO.getTripDest())); // need to be changed.
		
		boolean isCreated = tripDao.insertCourse(courseDTO);
		if(!isCreated)
			System.out.println("insertion error 발생~~");
		redirectAttributes.addAttribute("tripDest", tripInfoDTO.getTripDest());
	    redirectAttributes.addAttribute("startDate", tripInfoDTO.getStartDate());
	    redirectAttributes.addAttribute("endDate", tripInfoDTO.getEndDate());
	    redirectAttributes.addFlashAttribute("courseDTO", courseDTO);
		return "redirect:/trip/plan/result";
	}
	
	@GetMapping("/plan/result")
	public String showPlanResult(@RequestParam String tripDest,
	                             @RequestParam String startDate,
	                             @RequestParam String endDate,
	                             @RequestParam(value = "courseId", required = false) Integer courseId,
	                             @RequestParam(value="createdUserId", required = false) Integer fromUserId,
	                             CourseDTO courseDTOFromFlash,
	                             HttpSession session,
	                             Model model) {
		UserDTO loginUser = (UserDTO)session.getAttribute("loginUser");
		if(session.getAttribute("loginUser") == null) {
			model.addAttribute("requireLogin", "true");
			return "redirect:/";
		}
		if(courseId != null) { // if invited
			MembersDTO mdto = new MembersDTO();
			mdto.setFromUserId(fromUserId);
			mdto.setToUserId(((UserDTO)session.getAttribute("loginUser")).getId());
			mdto.setCourseId((long)courseId);
			String invUrl = String.format("/tripnote/trip/plan/result?tripDest=%s&startDate=%s&endDate=%s&inviteMode=true&createdUserId=%d&courseId=%d", tripDest, startDate, endDate, fromUserId, courseId);
			mdto.setInvitationUrl(invUrl);
			if(tripDao.getMembersCount(mdto) == 0) {
				tripDao.insertMember(mdto);
			}
		}
	    TripInfoDTO tripInfoDTO = new TripInfoDTO();
	    tripInfoDTO.setTripDest(tripDest);
	    tripInfoDTO.setStartDate(startDate);
	    tripInfoDTO.setEndDate(endDate);

	    CourseDTO courseInSession = (CourseDTO) session.getAttribute("courseDTO");
	    CourseDTO finalCourseDTO = null;

	    if (courseId != null) {
	        if (courseInSession == null || courseInSession.getId() != (long)courseId) {
	            finalCourseDTO = tripDao.getCourseById(courseId);
	            if (finalCourseDTO != null) {
	                session.setAttribute("courseDTO", finalCourseDTO);
	            }
	        } else {
	            finalCourseDTO = courseInSession;
	        }
	    } else if (courseInSession != null) {
	        finalCourseDTO = courseInSession;
	    } else {
	        finalCourseDTO = courseDTOFromFlash;
	        if(finalCourseDTO != null && finalCourseDTO.getId() != 0) {
	            session.setAttribute("courseDTO", finalCourseDTO);
	        }
	    }
	    
	    List<AttractionDTO> list = attDao.getAttractionList(tripDest);
	    List<AttractionDTO> list2 = attDao.getRestaurantList(tripDest);
	    List<AttractionDTO> list3 = attDao.getHotelList(tripDest);
	    List<AttractionDTO> list4 = new ArrayList<>();
	    for(AttractionDTO att : attDao.getAttRecList(tripDest, loginUser.getId())) {
	    	list4.add(att);
	    }
	    for(AttractionDTO att : attDao.getResRecList(tripDest, loginUser.getId())) {
	    	list4.add(att);
	    }
	    for(AttractionDTO att : attDao.getHotRecList(tripDest, loginUser.getId())) {
	    	list4.add(att);
	    }
	    
	    model.addAttribute("tripInfoDTO", tripInfoDTO);
	    model.addAttribute("attList", list);
	    model.addAttribute("restaurantList", list2);
	    model.addAttribute("hotelList", list3);
	    model.addAttribute("friendRecList", list4);
	    model.addAttribute("courseDTO", finalCourseDTO); 

	    return "trip/index2";
	}
	
	@ResponseBody
	@RequestMapping("/getAllTour")
	public List<TourLocDTO> getAllTour(int courseId,
			String inviteMode, Long createdUserId){
		System.out.println("invitemode: " + inviteMode);
		if(inviteMode.equals("true")) {
			
		}
		List<TourLocDTO> tourLocDTOList = tripDao.getAllTourLoc(courseId);
		for(TourLocDTO dto : tourLocDTOList) {
			System.out.println(dto);
			AttractionDTO attDTO = tripDao.getAttraction(dto.getCode());
			if(attDTO != null) {
				dto.setMapx(attDTO.getMapx());
				dto.setMapy(attDTO.getMapy());
			}
			
		}
		return tourLocDTOList;
	}
	
	@ResponseBody
	@RequestMapping("/saveDuration")
	public String saveDuration(TourLocDTO tourLocDTO) {
		tripDao.updateDuration(tourLocDTO);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/saveTour")
	public TourLocDTO saveTour(TourLocDTO tourLocDTO) {
		System.out.println("courseId "+tourLocDTO.getCourseId());
		tourLocDTO.setStartTime("09:00");
		tourLocDTO.setEndTime("10:00");
		tourLocDTO.setTourOrder(tourLocDTO.getTourOrder());
		tourLocDTO.setTourNth(tourLocDTO.getTourNth());
		System.out.println(tourLocDTO.getImgSrc());
		tripDao.insertTourLoc(tourLocDTO);
		tripDao.insertTourLocXY(tourLocDTO);
		//tripDao.insertTourType(tourLocDTO);
		
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setTourLocId(tourLocDTO.getId());
		tripDao.insertNote(noteDTO);
		return tourLocDTO;
	}
	
	@ResponseBody
	@RequestMapping("/saveNote")
	public String saveNote(int tourLocId, String noteContent) {
		NoteDTO noteDTO = tripDao.getNote(tourLocId);
		noteDTO.setNoteContent(noteContent);
		tripDao.updateNote(noteDTO);
		return "ok";
	}
	
	
	@PostMapping("/updateTour")
	@ResponseBody
	public String updateTour(@RequestBody List<TourLocDTO> dtoList) {
		for(TourLocDTO dto : dtoList) {
			tripDao.updateTourLoc(dto);
		}
		
		return "ok";
	}
	
	@GetMapping("/deleteTour")
	@ResponseBody
	public String deleteTour(int id) {
		tripDao.deleteNote(id);
		tripDao.deleteTourLoc(id);
		return "ok";
	}
	
	@RequestMapping("/testtrip")
	public String test() {
		System.out.println(Attractions.getAvailableDestinations());
		List<String> attractionList = Attractions.getAvailableDestinations();
		//after getting list
		String cCode = "12"; // 12 관광지 / 39 음식점 / 32 숙박
		for(String att : attractionList) {
			List<AttractionDTO> dtos = Attractions.getAttractions(att, cCode);
			
			//for each dto, insert data
			for(AttractionDTO dto : dtos) {
				if(cCode.equals("12"))
					attDao.insert(dto);
				else if(cCode.equals("39"))
					attDao.insertRestaurant(dto);
				else
					attDao.insertHotel(dto);
			}
				
		}
		
		return "trip/index";
	}
	
	
	
	@RequestMapping("/modify")
	public String modify() {
		
		List<AttractionDTO> attractionList = attDao.getAtt();
		List<AttractionDTO> resList = attDao.getRes();
		List<AttractionDTO> hotList = attDao.getHot();
		
		doModify(attractionList);
		doModify(resList);
		doModify(hotList);
		
		for(AttractionDTO dto : attractionList)
			attDao.updateAtt(dto);
		for(AttractionDTO dto : resList)
			attDao.updateRes(dto);
		for(AttractionDTO dto : hotList)
			attDao.updateHot(dto);
		
		return "trip/index";
	}
	
	public void doModify(List<AttractionDTO> attractionList) {
		String apiKey = "lko%2FQ9p5fG2SHKaH17oj9oV1ozNgrXqumBMjwhqzbnO90KLkpGLEDHBAXlfxGwSaAyRzRJOywKsyp%2Bnm9TVThA%3D%3D";

		for (AttractionDTO dto : attractionList) {
			// contentId가 없는 경우 건너뛰기
			if(dto.getContentId() == null || dto.getContentId().isEmpty()) continue;
			
			try {
				StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService2/detailCommon2");
				urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
				urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=ETC");
				urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=AppTest");
				urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=json");
				urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + dto.getContentId());

				URL url = new URL(urlBuilder.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();
				
				JSONObject jsonResponse = new JSONObject(sb.toString());
				if (jsonResponse.getJSONObject("response").getJSONObject("body").getInt("totalCount") > 0) {
					JSONObject item = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").getJSONObject(0);
					// DTO에 overview 정보 저장
					dto.setOverview(item.optString("overview", "소개 정보가 없습니다."));
				}

			} catch (Exception e) {
				dto.setOverview("소개가 없습니다.");
				//e.printStackTrace();
			}
		}
	}
	
}