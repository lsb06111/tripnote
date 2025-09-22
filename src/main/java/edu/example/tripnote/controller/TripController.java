package edu.example.tripnote.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.SessionAttributes;
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
	                             // URL에서 courseId를 직접 받도록 파라미터 추가
	                             @RequestParam(value = "courseId", required = false) Integer courseId,
	                             @RequestParam(value="createdUserId", required = false) Integer fromUserId,
	                             CourseDTO courseDTOFromFlash,
	                             HttpSession session,
	                             Model model) {

		if(session.getAttribute("loginUser") == null) {
			model.addAttribute("requireLogin", "true");
			return "redirect:/";
		}
		if(courseId != null) { // if invited
			// should add to member table with the url or idk lol
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
	        // 1. URL에 courseId가 있으면 무조건 우선!
	        //    만약 세션의 courseId와 다르다면, 세션을 새로운 정보로 덮어쓴다.
	        if (courseInSession == null || courseInSession.getId() != (long)courseId) {
	            finalCourseDTO = tripDao.getCourseById(courseId);
	            if (finalCourseDTO != null) {
	                session.setAttribute("courseDTO", finalCourseDTO);
	            }
	        } else {
	            // URL의 courseId와 세션의 courseId가 같으면 그냥 세션 값 사용
	            finalCourseDTO = courseInSession;
	        }
	    } else if (courseInSession != null) {
	        // 2. URL에 courseId는 없지만 세션에 정보가 있는 경우
	        finalCourseDTO = courseInSession;
	    } else {
	        // 3. 둘 다 없고 Redirect로 들어온 경우
	        finalCourseDTO = courseDTOFromFlash;
	        if(finalCourseDTO != null && finalCourseDTO.getId() != 0) {
	            session.setAttribute("courseDTO", finalCourseDTO);
	        }
	    }
	    
	    // ... (기존 DB 조회 로직)
	    List<AttractionDTO> list = attDao.getAttractionList(tripDest);
	    List<AttractionDTO> list2 = attDao.getRestaurantList(tripDest);
	    List<AttractionDTO> list3 = attDao.getHotelList(tripDest);
	    
	    model.addAttribute("tripInfoDTO", tripInfoDTO);
	    model.addAttribute("attList", list);
	    model.addAttribute("restaurantList", list2);
	    model.addAttribute("hotelList", list3);
	    model.addAttribute("friendRecList", list2);
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
	
	@ResponseBody
	@RequestMapping("/transit")
	public Map<String, Object> getTransit(
            @RequestParam String start,
            @RequestParam String goal
    ) {
        try {
            String url = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/transit?start=" + start + "&goal=" + goal;

            // 1. RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            // 2. HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            // 3. API 호출 및 응답을 Map<String, Object>으로 받기
            //    - ParameterizedTypeReference는 중첩된 JSON 구조를 Map으로 변환할 때 사용합니다.
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {};
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

            // 4. 응답 본문(Map)을 직접 반환
            return response.getBody();

        } catch (HttpClientErrorException e) {
            // 5. API 호출 중 에러 발생 시 (4xx, 5xx 등)
            System.err.println("네이버 API 호출 오류: " + e.getResponseBodyAsString());
            // 클라이언트에게 에러 상황을 알리기 위한 간단한 Map 반환
            return Collections.singletonMap("error", e.getResponseBodyAsString());
        }
    }
	@ResponseBody
    @RequestMapping("/odsay/searchPath")
    public Map<String, Object> searchPath(
            @RequestParam String start, // "경도,위도" 형식
            @RequestParam String goal   // "경도,위도" 형식
    ) {
        // start와 goal 파라미터를 경도(sx, ex)와 위도(sy, ey)로 분리
        String[] startCoords = start.split(",");
        String[] goalCoords = goal.split(",");
        
        String sx = startCoords[0];
        String sy = startCoords[1];
        String ex = goalCoords[0];
        String ey = goalCoords[1];

        // UriComponentsBuilder를 사용해 파라미터를 안전하게 인코딩하며 URL 생성
        String url = UriComponentsBuilder.fromHttpUrl("https://api.odsay.com/v1/api/searchPubTransPathT")
                .queryParam("apiKey", ODSAY_API_KEY)
                .queryParam("SX", sx)
                .queryParam("SY", sy)
                .queryParam("EX", ex)
                .queryParam("EY", ey)
                .toUriString();

        return callOdsayApi(url);
    }
	
	
	@ResponseBody
    @RequestMapping("/odsay/loadLane")
    public Map<String, Object> loadLane(@RequestParam String mapObj) {
        
        String url = UriComponentsBuilder.fromHttpUrl("https://api.odsay.com/v1/api/loadLane")
                .queryParam("apiKey", ODSAY_API_KEY)
                .queryParam("mapObject", "0:0@" + mapObj)
                .toUriString();
        
        return callOdsayApi(url);
    }
	
	private Map<String, Object> callOdsayApi(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // ODsay API의 응답을 바로 Map으로 변환하여 받음
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return response;
        } catch (HttpClientErrorException e) {
            System.err.println("ODsay API 호출 오류: " + e.getResponseBodyAsString());
            return Collections.singletonMap("error", e.getResponseBodyAsString());
        }
    }
	
}