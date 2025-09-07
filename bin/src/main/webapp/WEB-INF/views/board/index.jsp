<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf" %> </head>
<body>
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
<%
	String boardSearchValue = request.getParameter("search");
	boardSearchValue = boardSearchValue == null ? "" : boardSearchValue;
	
	HashMap<String,String[]> map = new HashMap<>();
	map.put("capital", new String[]{"서울", "인천", "경기도"});
	map.put("middle", new String[]{"대전", "세종", "충청북도", "충청남도", "강원도"});
	map.put("honam", new String[]{"광주", "전라북도", "전라남도"});
	map.put("youngnam", new String[]{"부산", "대구", "울산", "경상북도", "경상남도"});
	map.put("jeju", new String[]{"제주"});
%>
<section id="portfolio" class="portfolio services section mt-5">

      <div class="container section-title" style="padding-bottom:60px;">
        <h2>리뷰 게시판</h2>
        <a href="#" id="modal-reviewSelection" data-bs-toggle="modal" data-bs-target="#modalReviewSelection">리뷰 쓰러가기<i class="bi bi-arrow-right"></i></a>
      </div><div class="container">

        <div class="isotope-layout" data-default-filter="*" data-layout="fitRows" data-sort="original-order">

	<div class="portfolio-filters-wrapper position-relative d-flex align-items-center w-100" style="margin-bottom:0;width:90% !important; margin:auto;">
		  
		<div class="dropdown dropend">
		  <a class="dropdown-toggle text-decoration-none text-dark fw-semibold" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
		    최신순
		  </a>
		  <ul class="dropdown-menu">
		    <li><a class="dropdown-item" href="#">최신순</a></li>
		    <li><a class="dropdown-item" href="#">추천순</a></li>
		    <li><a class="dropdown-item" href="#">팔로워만</a></li>
		  </ul>
		</div>
		
		  <ul class="portfolio-filters isotope-filters mb-0 position-absolute start-50 translate-middle-x d-flex gap-3">
		    <li data-filter="*" class="filter-active">전 지역</li>
		    <li data-filter=".filter-capital">수도권</li>
		    <li data-filter=".filter-middle">중부권</li>
		    <li data-filter=".filter-honam">호남권</li>
		    <li data-filter=".filter-youngnam">영남권</li>
		    <li data-filter=".filter-jeju">제주</li>
		  </ul>

		  <div class="input-group ms-auto" style="width: 250px;">
		    <input type="text" id="search-input" class="form-control" placeholder="검색어를 입력해주세요" value="<%= boardSearchValue %>">
		    <button type="button" id="search-button" class="btn text-white"
		      style="--bs-btn-bg:#5c99ee; 
		             --bs-btn-hover-bg:#447fcc; 
		             --bs-btn-border-color:#5c99ee; 
		             --bs-btn-hover-border-color:#447fcc;">
		      검색
		    </button>
		  </div>

	</div>



          <div class="row gy-4 portfolio-grid isotope-container" style="position: relative; width: 95%; height: 264px; margin: 0px auto auto;">

				<%
				String[] exampleInfo = {
					    "김민준 @minjun_kim",
					    "이서준 @seo_jun123",
					    "박서연 @seoyeon_park",
					    "최지우 @jiwoo_choi",
					    "정하준 @hajun_jung",
					    "윤서현 @seohyun_yoon",
					    "임도윤 @doyun_lim",
					    "한지민 @jimin_han",
					    
					    // 부산 관련 더미 유저
					    "오지훈 @jihoon_oh",
					    "강유나 @yuna_kang",
					    "배도현 @dohyun_bae",
					    "송지우 @jiwoo_song",
					    "장태현 @taehyun_jang",
					    "백하린 @harin_baek",
					    "조민서 @minseo_jo",
					    "권시우 @siwoo_kwon",
					    "윤채원 @chaewon_yoon",
					    "홍준혁 @junhyuk_hong"
					};

					String[] exampleDate = {
					    "2025.08.27",
					    "2025.08.12",
					    "2025.07.30",
					    "2025.07.05",
					    "2025.06.25",
					    "2025.06.08",
					    "2025.05.21",
					    "2025.05.03",
					    
					    // 부산 관련 (모두 2025.05.03 이전)
					    "2025.04.28",
					    "2025.04.20",
					    "2025.04.15",
					    "2025.04.07",
					    "2025.03.25",
					    "2025.03.10",
					    "2025.02.22",
					    "2025.02.05",
					    "2025.01.18",
					    "2025.01.03"
					};

					String[] exampleTitle = {
					    "제주도의 여름 바다 여행기",
					    "부산 골목길 탐방 후기",
					    "빗속의 광주 여행",
					    "서울 야시장 밤 산책",
					    "강원도 주말 힐링 여행",
					    "전주 숨은 카페 투어",
					    "동해안 드라이브 코스 추천",
					    "인천에서의 템플스테이 체험",
					    
					    // 부산 관련 제목 10개
					    "부산 해운대 아침 산책",
					    "광안리 해수욕장에서의 하루",
					    "부산 자갈치 시장 미식 여행",
					    "부산 타워에서 바라본 야경",
					    "부산 감천문화마을 컬러풀 여행",
					    "서면 먹거리 골목 투어",
					    "부산 국제시장 탐방기",
					    "부산 송정 해변에서의 서핑",
					    "부산 온천장 힐링 체험",
					    "봄꽃 가득한 부산 동백섬 산책"
					};

					String[] exampleContents = {
					    "푸른 바다와 하얀 파도가 어우러진 제주에서 시원한 여름을 만끽하며, 바닷가 카페와 전통시장을 돌며 하루를 보냈습니다.",
					    "부산의 골목골목을 걸으며 맛집과 사람들의 따뜻한 정을 느끼고, 자갈치 시장과 해운대에서 다양한 해산물을 맛봤습니다.",
					    "비 오는 날 경주를 거닐며 고즈넉한 전통의 매력을 새삼 느끼고, 첨성대와 대릉원을 돌아보며 시간을 천천히 보냈습니다.",
					    "서울 야시장에서 다양한 길거리 음식을 맛보고 즐거운 밤을 보내며, 공연과 버스킹 무대도 함께 즐길 수 있었습니다.",
					    "강원도의 맑은 공기와 조용한 자연 속에서 완벽한 휴식을 취하고, 숲길 산책과 강가에서의 피크닉을 즐겼습니다.",
					    "대구의 숨겨진 카페들을 찾아다니며 특별한 커피와 디저트를 즐기고, 감각적인 인테리어와 분위기에 반했습니다.",
					    "동해안 도로를 달리며 시원한 바닷바람과 멋진 풍경을 감상하고, 해변마다 잠시 멈춰 사진을 남기는 즐거움을 느꼈습니다.",
					    "산사에서의 하룻밤은 마음을 비우고 나를 돌아보는 소중한 시간이었으며, 스님의 말씀과 명상으로 깊은 평화를 얻었습니다.",
					    
					    // 부산 관련 컨텐츠 10개
					    "부산 해운대에서 아침 햇살을 맞으며 산책을 하고, 근처 카페에서 여유로운 브런치를 즐겼습니다.",
					    "광안리 해변에서 파도 소리를 들으며 하루 종일 휴식을 취하고, 밤에는 불꽃놀이를 감상했습니다.",
					    "자갈치 시장에서 싱싱한 해산물을 맛보고, 상인들의 활기찬 모습을 직접 느낄 수 있었습니다.",
					    "부산 타워에 올라 도시의 전경과 바다 풍경을 한눈에 담으며, 사진 찍기에 좋은 시간을 보냈습니다.",
					    "감천문화마을의 알록달록한 집들과 예술 작품들을 구경하며, 곳곳에서 특별한 포토존을 찾았습니다.",
					    "서면 골목길에서 다양한 길거리 음식을 맛보고, 밤늦게까지 이어지는 부산의 열기를 체험했습니다.",
					    "부산 국제시장에서 전통적인 물건들과 다양한 먹거리를 둘러보며 한국적인 정취를 느꼈습니다.",
					    "송정 해변에서 파도를 타며 서핑을 배우고, 해변가 레스토랑에서 친구들과 저녁을 즐겼습니다.",
					    "온천장에서 따뜻한 물에 몸을 담그며 피로를 풀고, 전통 찜질방 문화도 함께 경험했습니다.",
					    "동백섬에서 만개한 봄꽃을 보며 산책을 즐기고, 해안 산책로에서 바다 경치를 감상했습니다."
					};
					//"capital", "middle", "honam", "youngnam"
					String[] exampleLoc = {
						"jeju:제주도",
						"youngnam:부산",
						"honam:광주",
						"capital:서울",
						"middle:강원도",
						"honam:전라북도",
						"middle:강원도",
						"capital:인천",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산",
						"youngnam:부산"
					};
					

					Random rand2 = new Random();
					String filters[] = {"capital", "middle", "honam", "youngnam"};
					for(int i=0; i<18; i++){ 
						String[] infoSplit = exampleInfo[i].split(" ");
						String infoName = infoSplit[0];
						String infoIdentify = infoSplit[1];
						String[] locInfo = exampleLoc[i].split(":");
						int likes = rand2.nextInt(999);
						int photoID = rand2.nextInt(5)+1;
				%>
						
						<div class="col-lg-3 col-md-6 portfolio-item isotope-item filter-<%= locInfo[0] %>"
							 data-date="<%= exampleDate[i] %>"
							 data-likes="<%= likes %>">
						  <div class="service-card" style="padding: 15px 12px; cursor:pointer" onclick="location.href='/oti_team3/board/view.jsp?title=<%= exampleTitle[i] %>'">
						
						    <figure style="display:flex; align-items:center; margin:0; width:100%; margin:5px">
						      <img
						        style="width:15%; border:1px solid black; border-radius:50%; margin-right:10px; cursor:pointer"
						        onclick="event.stopPropagation();location.href='/oti_team3/profile.jsp?identify=<%= infoIdentify %>&name=<%= infoName %>'"
						        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5gLM6Ory_xq5m06Wz-ClWzfw9Yhpst-gDRA&s"
						        alt="avatar"
						      >
						      <div style="display:flex; align-items:center; width:100%">
						        <div style="display:flex; flex-direction:column; margin-right:20px; cursor:pointer"
						             onclick="event.stopPropagation();location.href='/oti_team3/profile.jsp?identify=<%= infoIdentify %>&name=<%= infoName %>'">
						          <h6 style="margin:0; font-size:12px;"><%= exampleInfo[i] %></h6>
						          <h6 style="margin:0; font-size:10px;"><%= exampleDate[i] %></h6>
						        </div>
						
						        <div style="display:flex; align-items:center; margin-left:auto; margin-right:5px;">
						          <i class="bi bi-heart-fill" style="color: var(--accent-color);"></i>
						          <small style="margin-left:2px;"><%= likes %></small>
						        </div>
						      </div>
						    </figure>
						
						    <figure>
						      <img
						        style="width:100%; border-radius:3px;"
						        src="/oti_team3/assets/img/busan/busan1-<%= photoID %>.jpg"
						        alt="cover"
						      >
						    </figure>
						
						    <h3 class="mb-1"><%= exampleTitle[i] %></h3>
						    <small style="color:#5c99ee;"><%= locInfo[1] %></small>
						    <p><%= exampleContents[i] %></p>
						    <a href="view.jsp" class="service-link">
						      자세히보기
						      <i class="bi bi-arrow-right"></i>
						    </a>
						
						  </div>
						</div>
				
				<%
					}
				%>
			

          </div></div>

      </div>

    </section>    
    
<div class="modal fade" id="modalReviewSelection" tabindex="-1" aria-labelledby="modalReviewSelectionLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content border-0 shadow">
      <div class="modal-header">
        <h5 class="modal-title" id="modalReviewSelectionLabel">여행 선택</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
      </div>
      <div class="modal-body p-4">
        <%@ include file="/WEB-INF/views/jspf/board/reviewselection.jspf" %>
      </div>
    </div>
  </div>
</div>
    
<script>
document.addEventListener("DOMContentLoaded", function () {
  var grid = document.querySelector('.isotope-container');
  var iso = new Isotope(grid, {
    itemSelector: '.portfolio-item',
    layoutMode: 'fitRows',
    getSortData: {
      date: function (itemElem) {
        var dateStr = itemElem.getAttribute('data-date');
        return Date.parse(dateStr.replace(/\./g, '-'));
      },
      likes: '[data-likes] parseInt'
    },
    sortAscending: {
      date: false, 
      likes: false 
    }
  });

  // === Dropdown handling ===
  document.querySelectorAll('.dropdown-menu a').forEach(function(el){
    el.addEventListener('click', function(e){
      e.preventDefault();
      let choice = this.textContent.trim();
      let currentFilter = iso.options.filter;

      if(choice === "최신순"){
        iso.arrange({ filter: currentFilter, sortBy: 'date' });
      }
      else if(choice === "추천순"){
        iso.arrange({ filter: currentFilter, sortBy: 'likes' });
      }
      else if(choice === "팔로워만"){
        // do nothing for now
      }

      document.querySelector('.dropdown-toggle').textContent = choice;
    });
  });

  // === Region filters handling ===
  document.querySelectorAll('.portfolio-filters li').forEach(function(el){
    el.addEventListener('click', function(){
      let filterValue = this.getAttribute('data-filter');
      iso.arrange({ filter: filterValue, sortBy: 'date' }); 

      document.querySelectorAll('.portfolio-filters li').forEach(li => li.classList.remove('filter-active'));
      this.classList.add('filter-active');
      
      document.getElementById('search-input').value = '';

      document.querySelector('.dropdown-toggle').textContent = "최신순";
    });
  });
  
  // === Search handling ===
  const searchButton = document.getElementById('search-button');
  const searchInput = document.getElementById('search-input');

  function performSearch() {
    var searchValue = searchInput.value.toLowerCase();
    
    iso.arrange({
      filter: function(itemElem) {
        var itemText = itemElem.textContent.toLowerCase();
        return itemText.includes(searchValue);
      }
    });

    document.querySelectorAll('.portfolio-filters li').forEach(li => li.classList.remove('filter-active'));
  }

  searchButton.addEventListener('click', performSearch);
  
  searchInput.addEventListener('keyup', function(event) {
    if (event.key === 'Enter') {
      performSearch();
    }
  });
  
  // === Initial search on page load ===
  // If the search input has a value from the URL parameter, trigger the search function.
  if (searchInput.value.trim() !== '') {
    performSearch();
  }
});
</script>
    
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %> </body>
</html>