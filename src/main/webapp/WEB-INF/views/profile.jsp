<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/jspf/head.jspf" %> <!-- 헤드 부분 고정 -->
<!-- 커스텀 CSS import 존 -->

</head>
<body>
<%@ include file="/WEB-INF/views/jspf/header.jspf" %> <!-- 헤더부분 고정 -->
<%
    String identify = request.getParameter("identify");
    boolean isMe = identify != null && identify.equals("myself");
    
%>

<c:set var="reqNickname"  value="${empty param.name ? loginUser.nickname : param.name}" />
<c:set var="reqUsername" value="${empty param.username ? loginUser.username : param.username}"/>
<c:set var="reqEmail" value="${empty param.email ? loginUser.email : param.email}"/>

<section>
  <div class="container py-5">
    <div class="row">
      <div class="col">
        <nav aria-label="breadcrumb" class="bg-body-tertiary rounded-3 p-3 mb-4">
          <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item active" aria-current="page">${loginUser.nickname}님의 프로필</li>
            <% if(isMe){ %>
              <li class="breadcrumb-item">
                <a href="#" id="modal-changeinfo" data-bs-toggle="modal" data-bs-target="#modalChangeinfo">정보 수정</a>
              </li>
            <% } %>
          </ol>
        </nav>
      </div>
    </div>

    <div class="row">
      <!-- LEFT -->
      <div class="col-lg-4">
        <div class="card mb-4" style="border-radius: 15px;box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);border: none;">
          <div class="card-body text-center">
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp" alt="avatar"
                 class="rounded-circle img-fluid" style="width: 150px;">
            <h5 class="mt-3 mb-1">${reqNickname}${' @'}${reqUsername}</h5>
            <ol class="breadcrumb mb-2" style="display:flex; justify-content:center;">
              <li class="breadcrumb-item">
                팔로워 :
                <a href="#" id="modal-following" data-bs-toggle="modal" data-bs-target="#modalFollowing" onclick="changeModalTitle('팔로워')">213명</a>
              </li>
              <li class="breadcrumb-item" id="modal-following" data-bs-toggle="modal" data-bs-target="#modalFollowing" onclick="changeModalTitle('팔로잉')">
                팔로잉 : <a href="#">129명</a>
              </li>
            </ol>

            <% if(!isMe) { %>
              <div class="d-flex justify-content-center mb-2">
                <button type="button" class="btn btn-primary"
                        style="--bs-btn-bg:#5c99ee; --bs-btn-hover-bg:#447fcc; --bs-btn-border-color:#5c99ee; --bs-btn-hover-border-color:#447fcc;">
                  팔로잉
                </button>
                <button type="button" class="btn btn-outline-primary ms-1"
                        style="--bs-btn-hover-bg:#447fcc; --bs-btn-border-color:#5c99ee; --bs-btn-hover-border-color:#447fcc;">
                  팔로우
                </button>
              </div>
            <% } %>
          </div>
        </div>

        <!-- Location filter card -->
        <div class="card mb-4 mb-lg-0" style="border-radius: 15px;box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);border: none;">
          <div class="card-header fw-semibold" style="border-bottom:none">지역별 보기</div>
          <div class="card-body p-0">
            <ul class="list-group list-group-flush rounded-3" id="locList">
				<li class="list-group-item d-flex justify-content-between align-items-center p-3 active"
		          data-location="*" style="cursor:pointer;">
		        <span><i class="bi bi-geo text-primary me-2"></i>전체 지역</span>
		        <i class="bi bi-chevron-right text-muted"></i>
		      </li>
              <li class="list-group-item d-flex justify-content-between align-items-center p-3" data-location="강원도" style="cursor:pointer;">
                <span><i class="bi bi-geo-alt text-primary me-2"></i>강원도</span>
                <i class="bi bi-chevron-right text-muted"></i>
              </li>
              <li class="list-group-item d-flex justify-content-between align-items-center p-3" data-location="부산" style="cursor:pointer;">
                <span><i class="bi bi-geo-alt text-primary me-2"></i>부산</span>
                <i class="bi bi-chevron-right text-muted"></i>
              </li>
              <li class="list-group-item d-flex justify-content-between align-items-center p-3" data-location="제주도" style="cursor:pointer;">
                <span><i class="bi bi-geo-alt text-primary me-2"></i>제주도</span>
                <i class="bi bi-chevron-right text-muted"></i>
              </li>
              <li class="list-group-item d-flex justify-content-between align-items-center p-3" data-location="전주" style="cursor:pointer;">
                <span><i class="bi bi-geo-alt text-primary me-2"></i>전주</span>
                <i class="bi bi-chevron-right text-muted"></i>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <!-- RIGHT -->
      <div class="col-lg-8">
        <section id="services" class="services section" style="padding:0">
          <div class="container">
            <div class="row gy-4" id="tripCardsRow">
            
              <c:forEach var="course" items="${ courses }" varStatus="status">
              <div class="col-12">
                <!-- tag this card with its location -->
               <div class="service-card"
			     data-location="${areas[status.index].areaName}"
			     style="padding: 18px 24px; cursor:pointer;"
			     onclick="location.href='/tripnote/<%=isMe?"details":"board/view"%>?title=${courses[status.index].title}&nBaks=${nBaks[status.index]}&courseId=${ courses[status.index].id }'">
                  <div style="display:flex; align-items:center; gap:8px; margin-bottom:10px">
                    <div class="service-icon" style="margin-bottom:0">
                      <i class="bi bi-bus-front"></i>
                    </div>
                    <div style="margin-left: 10px;">
                      <h3 style="margin:0;">${courses[status.index].title}</h3>
                      <ol class="breadcrumb mb-0" style="display:flex;">
                        <li class="breadcrumb-item">${ nBaks[status.index] }</li>
                        <li class="breadcrumb-item">지역 :
                          <strong style="color:#5c99ee">${areas[status.index].areaName}</strong>
                        </li>
                      </ol>
                    </div>
                    <!-- action button aligned to the right -->
                    
                    <%if(isMe){ %>
                    <a href="/tripnote/trip/"
                       class="service-link ms-auto"
                       onclick="event.stopPropagation();"
                       style="width:fit-content; transition:color 0.3s; color:inherit;"
                       onmouseover="this.style.color='#5c99ee';"
                       onmouseout="this.style.color='inherit';">
                      일정 편집하기
                    </a> / 
                    <div>
                    <a href="/tripnote/board/write.jsp?"title=titles[i]
                       class="service-link ms-auto"
                       onclick="event.stopPropagation();"
                       style="width:fit-content; transition:color 0.3s; color:inherit;"
                       onmouseover="this.style.color='#5c99ee';"
                       onmouseout="this.style.color='inherit';">
                      리뷰 작성하기 <i class="bi bi-arrow-right"></i>
                    </a></div>
                    <%} %>
                  </div>
                </div>
              </div>
              </c:forEach>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</section>

<!-- 팔로잉/팔로워 모달 -->
<div class="modal fade" id="modalFollowing" tabindex="-1" aria-labelledby="modalFollowingLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content border-0 shadow">
      <div class="modal-header">
        <h5 class="modal-title" id="modalFollowingLabel">팔로잉</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
      </div>
      <div class="modal-body p-4">
        <%@ include file="/WEB-INF/views/jspf/follows/followinglist.jspf" %>
      </div>
    </div>
  </div>
</div>

<!-- 정보수정 모달 -->
<div class="modal fade" id="modalChangeinfo" tabindex="-1" aria-labelledby="modalChangeinfoLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content border-0 shadow">
      <div class="modal-body" style="padding:0">
        <%@ include file="/WEB-INF/views/jspf/user/changeinfo.jspf" %>
      </div>
    </div>
  </div>
</div>

 
<%
	String toastTitle = "알림";
	String toastMsg = "정보 변경에 성공하였습니다!";
	Boolean toastSuccess = true;
%>

<%@ include file="/WEB-INF/views/jspf/toasts/toast.jspf" %>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %> <!-- 푸터 부분 고정 -->

<script>
  const modalTitleDom = document.querySelector('#modalFollowingLabel');
  document.querySelector('.list-group-item.active > i').style
	.setProperty('color', 'white', 'important');
	document.querySelector('.list-group-item.active > span > i').style
	.setProperty('color', 'white', 'important');
	document.querySelector('.list-group-item.active').style
	.setProperty('background', '#5c99ee', 'important');
	
  function changeModalTitle(titleName) {
    modalTitleDom.textContent = titleName;
  }

  (function () {
    const list = document.getElementById('locList');
    if (!list) return;

    const items = list.querySelectorAll('.list-group-item');
    const cards = document.querySelectorAll('#tripCardsRow .service-card[data-location]');

    function filterByLocation(loc) {
      cards.forEach(function (card) {
        const col = card.closest('.col-12');
        if (!col) return;

        const match = (loc === '*') || (card.getAttribute('data-location') === loc);
        col.style.display = match ? '' : 'none';
      });
    }

    // click handlers
    items.forEach(function (li) {
      li.addEventListener('click', function () {
        // active highlight
        items.forEach(function (x) { x.classList.remove('active'); });
        this.classList.add('active');

        //all 
        document.querySelectorAll('.list-group-item > i').forEach(e => e.style
        		.setProperty('color', 'black', 'important'));
        
        //active color change
        document.querySelector('.list-group-item.active > i').style
        		.setProperty('color', 'white', 'important');
        
        document.querySelectorAll('.list-group-item > span > i').forEach(e => e.style
        		.setProperty('color', 'rgb(13,110,253)', 'important'));
        
        document.querySelector('.list-group-item.active > span > i').style
				.setProperty('color', 'white', 'important');
        
        
        document.querySelectorAll('.list-group-item').forEach(e => e.style
				.setProperty('background', 'white', 'important'));
        
        document.querySelector('.list-group-item.active').style
    			.setProperty('background', '#5c99ee', 'important');
        
        // filter
        const loc = this.getAttribute('data-location');
        filterByLocation(loc);
        
        
        
      });
    });

    // initial state: show all
    filterByLocation('*');
  })();
  
  
  const toastTrigger = document.getElementById('liveToastBtn')
  const toastLiveExample = document.getElementById('liveToast')

  if (toastTrigger) {
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
    toastTrigger.addEventListener('click', () => {
      toastBootstrap.show()
    })
  }
  
  
</script>

</body>
</html>