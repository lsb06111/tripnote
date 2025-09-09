package edu.example.tripnote.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.example.tripnote.domain.trip.AttractionDTO;

public class Attractions {
	
	public static void main(String[] args) {
		System.out.println(getAvailableDestinations());

	}
	
	public static List<String> getAvailableDestinations() {
		List<String> destinations = new ArrayList<>();
		String apiKey = "lko%2FQ9p5fG2SHKaH17oj9oV1ozNgrXqumBMjwhqzbnO90KLkpGLEDHBAXlfxGwSaAyRzRJOywKsyp%2Bnm9TVThA%3D%3D";

		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService2/areaCode2");
			urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=20"); // 모든 지역 정보를 가져오기 위해 충분한 수 설정
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
			urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=ETC");
			urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=AppTest");
			urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=json");

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

			// JSON 파싱하여 지역명(name)만 리스트에 추가
			JSONObject jsonResponse = new JSONObject(sb.toString());
			JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
			
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				destinations.add(item.getString("name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return destinations;
	}
	
	public static List<AttractionDTO> getAttractions(String destinationName, String cCode){
		String apiKey = "lko%2FQ9p5fG2SHKaH17oj9oV1ozNgrXqumBMjwhqzbnO90KLkpGLEDHBAXlfxGwSaAyRzRJOywKsyp%2Bnm9TVThA%3D%3D";
		String areaCode = "";

		// 1. 지역명을 기반으로 '지역 코드' 조회
		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService2/areaCode2");
			urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=20"); 
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
			urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=ETC");
			urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=AppTest");
			urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=json");

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
			JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
			
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				if (destinationName.equals(item.getString("name"))) {
					areaCode = String.valueOf(item.getInt("code"));
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 2. 조회된 지역 코드로 '관광지(contentTypeId=12)' 목록 조회
		List<AttractionDTO> attractionList = new ArrayList<>();
		if (!areaCode.isEmpty()) {
			try {
				StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService2/areaBasedList2");
				urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
				urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=500");
				urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
				urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=ETC");
				urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=AppTest");
				urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=json");
				urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=A");
				urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "="+cCode);
				urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + areaCode);

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
					JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
				
					for (int i = 0; i < items.length(); i++) {
						JSONObject item = items.getJSONObject(i);
						AttractionDTO dto = new AttractionDTO();
						// ✨ contentId를 DTO에 반드시 저장해야 합니다.
						dto.setContentId(item.optString("contentid", "")); 
						dto.setTitle(item.optString("title", "정보 없음"));
						dto.setAddr1(item.optString("addr1", ""));
						dto.setFirstimage(item.optString("firstimage", ""));
						dto.setMapx(item.optString("mapx", "0.0"));
						dto.setMapy(item.optString("mapy", "0.0"));
						dto.setLocations(destinationName);
						attractionList.add(dto);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 3. ✨ 각 관광지의 '개요(overview)' 정보 상세 조회 (추가된 부분)
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
		
		return attractionList;
	}

	

}
