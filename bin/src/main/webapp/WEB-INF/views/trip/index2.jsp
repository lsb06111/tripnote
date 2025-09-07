<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>  
  
<!-- 헤드 부분 고정 -->
<link rel="stylesheet" href="/tripnote/resources/assets/css/trip.css">
<link rel="stylesheet" href="/tripnote/resources/assets/css/map.css">
<link  href="https://code.jquery.com/ui/1.13.3/themes/base/jquery-ui.css" rel="stylesheet">

</head>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.13.3/jquery-ui.min.js"></script>

<!-- Modal -->
<div class="modal fade" id="trip-inviteModal" tabindex="-1" aria-labelledby="trip-inviteModalLabel" aria-hidden="true">
  <div class="modal-dialog  modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="trip-inviteModalLabel">초대 / 공유</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      <div class = "p-2">
       	<div class="fs-6 mb-1">공동 편집을 위한 초대링크</div>
        <div class="input-group mb-4">
		    <input readonly type="text" class="form-control" value ="http://localhost:8088/oti_team3/trip">
		    <button id = "trip-copy-btn" type="button" class="btn text-white" onclick="copy(this)" data-bs-dismiss="modal" style="--bs-btn-bg:#5c99ee; 
		             --bs-btn-hover-bg:#447fcc; 
		             --bs-btn-border-color:#5c99ee; 
		             --bs-btn-hover-border-color:#447fcc;">복사
		    </button>
  		</div>
       	<div class="fs-6 mb-1">간단히 확인만 하려면...</div>
        <div class="input-group mb-2">
		    <input readonly type="text" class="form-control" value ="http://localhost:8088/oti_team3/trip/index2.jsp">
		    <button id = "trip-copy-btn" type="button" class="btn text-white" onclick="copy(this)" data-bs-dismiss="modal" style="--bs-btn-bg:#5c99ee; 
		             --bs-btn-hover-bg:#447fcc; 
		             --bs-btn-border-color:#5c99ee; 
		             --bs-btn-hover-border-color:#447fcc;">복사
		    </button>
  		</div>
      </div>
      </div>
    </div>
  </div>
</div>

<body class="trip-body">
	<main class="trip-main">              
		<!-- 왼쪽 네비 -->
		<nav class="nav my-4 mx-3 d-flex flex-column justify-content-between" role="tablist" style="flex:1;">
			<div class="step-nav">  
	      		<a href="/oti_team3/" class="trip-sitename">TripNote</a>
				<button id = "trip-pre-step" class="nav-link step-item" data-bs-toggle="tab" data-bs-target="" role="tab" onclick="location.href='/oti_team3/trip'">
					<span class="step-title">날짜/지역</span> 
				</button> 
				<button id = "trip-loc-step" class="nav-link step-item active" data-bs-toggle="tab" data-bs-target="" role="tab" onclick="location.href='/oti_team3/trip/index2.jsp'">
					<span class="step-title">장소 선택</span>
				</button>
			</div>
			<div>
				<button id="trip-invite-btn" class= "btn btn-primary" data-bs-toggle="modal" data-bs-target="#trip-inviteModal" style="background: #5c99ee; margin-bottom: 10px;">초대/공유</button>
				<button id="trip-done-btn" class= "btn btn-primary" style="background: #5c99ee; margin-bottom: 10px;">일정 저장</button>
			</div>
		</nav>  
		<div class="trip-step" style="flex:17;">
			<%@ include file="/WEB-INF/views/jspf/trip/make-course.jspf" %>
		</div>
		
	</main> 
  <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f4e8f97abff93943bc5ef379ccb04e38&libraries=services"></script>
  <script src="/tripnote/resources/assets/js/map.js"></script>
  <script src="/tripnote/resources/assets/js/trip.js"></script>
  <script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
	  <!-- Vendor JS Files -->
  <script src="/tripnote/resources/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/tripnote/resources/assets/vendor/php-email-form/validate.js"></script>
  <script src="/tripnote/resources/assets/vendor/glightbox/js/glightbox.min.js"></script>
  <script src="/tripnote/resources/assets/vendor/swiper/swiper-bundle.min.js"></script>
  <script src="/tripnote/resources/assets/vendor/purecounter/purecounter_vanilla.js"></script>
  <script src="/tripnote/resources/assets/vendor/imagesloaded/imagesloaded.pkgd.min.js"></script>
  <script src="/tripnote/resources/assets/vendor/isotope-layout/isotope.pkgd.min.js"></script>
  <!-- Main JS File -->
  <script src="/tripnote/resources/assets/js/main.js"></script>
</body>
</html>