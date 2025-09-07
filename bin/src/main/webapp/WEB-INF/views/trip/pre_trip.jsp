<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
<!-- 헤드 부분 고정 -->
<link rel="stylesheet" href="/oti_team3/assets/css/trip.css">
</head>
<body class="trip-body">
<%@ include file="/WEB-INF/views/jspf/header.jspf"%>
	<main class="trip-main">
		<section class="trip-left-nav">
			<nav class="nav step-nav" role="tablist">
				<button class="nav-link step-item" data-bs-toggle="tab" data-bs-target="#date" role="tab">
					<span class="step-title">날짜/지역</span>
				</button>
				<button class="nav-link step-item active" data-bs-toggle="tab" data-bs-target="#loc" role="tab">
					<span class="step-title">장소 선택</span>
				</button>
				<button class="nav-link step-item" data-bs-toggle="tab" data-bs-target="#confirm" role="tab">
					<span class="step-title">일정 확인</span>
				</button>
			</nav>
		</section>

		<!-- 왼쪽 사이드바 -->
		<section class="trip-sidebar-left" aria-label="장소 리스트" >
			<%@ include file="/WEB-INF/views/jspf/trip/calendar.jspf" %> 
		</section>
		<!-- 중간 일정 타임라인 -->
		<section class="portfolio section" aria-label="일정 타임라인" style="flex: 3.2;">
			<div class="container">
		        <div class="isotope-layout" data-default-filter="*" data-layout="fitRows" data-sort="original-order">
		          <div class="portfolio-filters-wrapper" style="margin-bottom:0">
		            <ul class="portfolio-filters isotope-filters">
		              <li data-filter="*" class="filter-active">전 지역</li>
		              <li data-filter=".filter-capital">수도권</li>
		              <li data-filter=".filter-middle">중부권</li>
		              <li data-filter=".filter-honam">호남권</li>
		              <li data-filter=".filter-youngnam">영남권</li>
		            </ul>
		          </div>
		          <%
		          	HashMap<String,String[]> map = new HashMap<>();
		          	map.put("capital", new String[]{"서울", "인천", "경기도"});
		          	map.put("middle", new String[]{"대전", "세종", "충청북도", "충청남도", "강원도"});
		          	map.put("honam", new String[]{"광주", "전라북도", "전라남도"});
		          	map.put("youngnam", new String[]{"부산", "대구", "울산", "경상북도", "경상남도"});
		          %>
		
		          <div class="row gy-4 portfolio-grid isotope-container" style="position: relative;width: 80%;height: 256px;margin: auto;margin-top:0">
		
					<%for(String category : map.keySet()) {
						String[] locations = map.get(category);
						for(String location : locations){
					%>
							<div class="col-lg-3 col-md-6 portfolio-item isotope-item filter-<%= category %>">
							  <div class="location-card">
							    <a href="portfolio-details.html" 
								   class="btn text-white fw-semibold w-100 shadow-sm py-2 rounded"
								   style="--bs-btn-bg: #5c99ee; --bs-btn-hover-bg: #447fcc; --bs-btn-border-color: #5c99ee; --bs-btn-hover-border-color: #447fcc;">
								  <%= location %>
								</a>
							  </div>
							</div>
		            <%
						}
					} 
					%>
		          </div><!-- End Portfolio Grid -->
		        </div>
		        <div class="d-flex justify-content-end me-5" >
			        <button class="btn btn-primary"> 저장 </button>
		        </div>
   			</div>
		</section>
	</main>
	<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
	  <!-- Vendor JS Files -->
  <script src="/oti_team3/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/oti_team3/assets/vendor/php-email-form/validate.js"></script>
  <script src="/oti_team3/assets/vendor/glightbox/js/glightbox.min.js"></script>
  <script src="/oti_team3/assets/vendor/swiper/swiper-bundle.min.js"></script>
  <script src="/oti_team3/assets/vendor/purecounter/purecounter_vanilla.js"></script>
  <script src="/oti_team3/assets/vendor/imagesloaded/imagesloaded.pkgd.min.js"></script>
  <script src="/oti_team3/assets/vendor/isotope-layout/isotope.pkgd.min.js"></script>
  <!-- Main JS File -->
  <script src="/oti_team3/assets/js/main.js"></script>
</body>
</html>