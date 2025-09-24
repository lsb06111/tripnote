<%@page import="edu.example.tripnote.domain.board.BoardDetailDTO"%>
<%@page import="java.util.stream.Collectors"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="edu.example.tripnote.domain.board.BoardDetailDTO"%>
<%@ page import="java.util.stream.Collectors"%>
<style>
.custom-toggle {
	background-color: #5c99ee;
	color: white;
	border: 1px solid #ccc;
	border-radius: 30px;
	border-color: #0d6efd;
	padding: 6px 16px;
	display: inline-block;
	cursor: pointer;
	transition: all 0.3s ease;
}

.btn-check:checked+.custom-toggle {
	background-color: lightgray;
	border-color: gray;
	color: black;
}
/* 댓글 기본 숨김 */
.default-hide-comment {
	display: none;
}

/* 체크박스 체크 시 댓글 표시 */
#btn2:checked ~ .default-hide-comment {
	display: block;
}

/* ▼▼▼ CSS FOR "READ MORE" ▼▼▼ */
#replyContainer {
	/* Smooth transition for height change */
	transition: max-height 0.5s ease-in-out;
	position: relative; /* Needed for the gradient overlay */
}

#replyContainer.collapsed {
	max-height: 300px; /* Your desired initial height */
	overflow: hidden;
}

/* Optional: Adds a fade-out effect at the bottom to indicate more content */
#replyContainer.collapsed::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	height: 60px;
	background: linear-gradient(to bottom, transparent, white);
}
</style>

</head>
<body class="portfolio-details-page">
	<%@ include file="/WEB-INF/views/jspf/header.jspf"%>

		<c:if test="${not empty sessionScope.loginUser}">
			<span id="user_id" style="display:none;">${sessionScope.loginUser.id}</span>
		</c:if>

	<main class="main">
	<div class="page-title light-background">
		<div class="container">
			<h1>${board.title}</h1>
			<div id="boardId" style="display:none;'">${board.id}</div>
		</div>
	</div>
	<section id="portfolio-details" class="portfolio-details section" style="--subtle-border: none; padding-bottom: 0px;">
		<div class="container">
			<div class="project-hero" style="margin-bottom: 0;">
				<div class="project-meta-grid" style="padding-top: 0">
					<div class="meta-column">
						<div class="meta-label">작성자</div>
						<div class="meta-value">${board.nickname}</div>
						<div id="writer" style="display:none;'">${board.userId}</div>
					</div>

					<div class="meta-column">
						<div class="meta-label">여행 지역</div>
						<div class="meta-value">${board.areaName}</div>
					</div>
					<div class="meta-column">
						<div class="meta-label">여행일정</div>
						<div class="meta-value">${board.startDate}~ ${board.endDate}</div>
					</div>
					<div class="meta-column">
						<div class="meta-label">작성일</div>
						<div class="meta-value">${board.createdAt}</div>
					</div>
					<div class="meta-column">
						<c:choose>
							<c:when test="${not empty sessionScope.loginUser}">
								<c:if test="${sessionScope.loginUser.username != board.username}">
									<div class="meta-value">
										<input type="checkbox" class="btn-check" autocomplete="off" id="follow-btn-2" ${isFollowing ? 'checked' : ''} /> 
										<label class="custom-toggle" for="follow-btn-2" onclick="sendFollow()">팔로우</label>
									</div>
								</c:if>
							</c:when>
							<c:otherwise>
								<div class="meta-value">
								    <label class="custom-toggle" for="follow-btn-1" data-bs-toggle="modal" data-bs-target="#modalLogin">팔로우</label>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="post-contents d-flex justify-content-center align-items-stretch gap-4 mt-5">
				    <input id="thumbnailImgInput"
				           class="postImg-input" type="file" accept="image/*"
				           style="position: absolute; left: -9999px; display:none;" />
		
				    <!-- 대표 이미지 미리보기 영역 -->
				    <div class="thumbnail-preview text-center" style="width: 50%; max-width: 400px;">
				        <img id="boardThumbnailImg"
				             src="${board.thumbnail}"
				             class="postImg img-fluid border"
				             alt="대표 이미지"
				             style="width: 20rem; height: 15rem; object-fit: contain; border-radius: 10px; cursor: pointer;" />
				        <small class="text-muted d-block mt-1"></small>
				    </div>
		
				    <!-- 여행 소개 입력 -->
				    <textarea id="post_intro" class="form-control" style="width: 50%; resize: none; height: 15rem;"
				              placeholder="이번 여행을 소개해주세요" readonly>${board.boardContent}</textarea>
				</div>
			</div>

			<section id="tabs" class="tabs section">
				<div class="container">
					<div class="tabs-wrapper">
						<div class="tabs-header">
							<ul class="nav nav-tabs">
								<%
									List contents = (List) request.getAttribute("contents");
									int tabNumber = (contents == null) ? 0 : contents.size(); // 날짜 탭 수
									for (int i = 0; i < tabNumber; i++) {
										List<BoardDetailDTO> reviewsOfDay = (List<BoardDetailDTO>) contents.get(i);
										int dayValue = reviewsOfDay.get(0).getTourNth();
										
								%>
								<li class="nav-item">
								<a class="nav-link <%=i == 0 ? "active show" : ""%>"
									data-bs-toggle="tab" data-bs-target="#tabs-tab-<%=(i + 1)%>">
										<div class="tab-content-preview">
											<span class="tab-number">0<%=(i + 1)%></span>
											<div class="tab-text">
												<h6 style="margin-bottom: 0"><%=dayValue + "일차"%></h6>
											</div>
										</div>
								</a></li>
								<%
									}
								%>

							</ul>
						</div>

						<div class="tab-content">

							<%
								int nodeNumber = 0;
								int totalReviewNumber = 0;
								for (int ii = 0; ii < tabNumber; ii++) {
									List<BoardDetailDTO> reviewsOfDay = (List<BoardDetailDTO>) contents.get(ii);
									// 특정날짜의 장소별 리뷰내용 리스트
									List<List<BoardDetailDTO>> contentsGroupByLoc = new ArrayList<>(
											(reviewsOfDay.stream()
															.collect(Collectors.groupingBy(BoardDetailDTO::getTourOrder))
															.values()));
									nodeNumber = contentsGroupByLoc.size();
							%>
							<div class="tab-pane fade <%=ii == 0 ? "active show" : ""%>"
								id="tabs-tab-<%=(ii + 1)%>">

								<div id="routeBar-<%=ii%>" data-group="<%=ii%>"
									class="d-flex justify-content-center my-4">
									<div
										class="d-flex align-items-center justify-content-center w-100 pb-2">
										<%
												String[] nodeIcons = {"house-door", "leaf-fill", "fork-knife", "leaf-fill", "leaf-fill"};

												HashMap<String, String> tagContents = new HashMap<>();
												tagContents.put("leaf-fill", "멋있어요,예뻐요,다양하게 볼게 많아요");
												tagContents.put("house-door", "청결해요,방음이 잘 돼요,가격이 저럼해요");
												tagContents.put("fork-knife", "식사가 맛있어요,가격이 저렴해요,매장이 청결해요");
												
												HashMap<String, String> courseIcons = new HashMap<>();
												courseIcons.put("명소","leaf-fill");
												courseIcons.put("숙소","house-door");
												courseIcons.put("음식점","fork-knife");

												for (int i = 0; i < nodeNumber; i++) {
													String icon = contentsGroupByLoc.get(i).get(0).getTypeName();
										%>
										<div class="d-flex flex-column align-items-center">
											<button type="button"
												class="route-node btn btn-outline-primary rounded-circle fw-bold d-flex align-items-center justify-content-center p-0 <%=i == 0 ? "active" : ""%>"
												data-target="#spot-pane-<%=ii%>-<%=i%>"
												data-group="<%=ii%>"
												style="width: 60px; height: 60px; - -bs-btn-color: #5c99ee; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-bg: #5c99ee; - -bs-btn-hover-border-color: #5c99ee; - -bs-btn-hover-color: #fff; - -bs-btn-active-bg: #5c99ee; - -bs-btn-active-border-color: #5c99ee; - -bs-btn-active-color: #fff;">
												<i class="bi bi-<%=courseIcons.get(icon)%>"
													style="font-size: 1.4em"></i>
											</button>
											<small class="mt-1 text-muted" style="font-size: 1.05em"><%=contentsGroupByLoc.get(i).get(0).getTourLocName() %></small>
										</div>

										<%
											if (i < nodeNumber - 1) {
										%>
										<div
											class="d-flex flex-column align-items-center mx-2 flex-grow-1">
											<div class="border-top border-2 w-100"></div>
										</div>
										<%
											}
										%>
										<%
											}
										%>
									</div>
								</div>

								<div id="spotPanes-<%=ii%>" class="tab-content mt-4">
									<%
											for (int i = 0; i < nodeNumber; i++) {
												List<BoardDetailDTO> reviewsOfLoc = (List)contentsGroupByLoc.get(i);
												totalReviewNumber = reviewsOfLoc.size(); // 관광지별 리뷰 내용 (최대3개)
												String[] tagContent = tagContents.get(nodeIcons[i]).split(",");
									%>
									<div id="spot-pane-<%=ii%>-<%=i%>"
										class="tab-pane fade <%=i == 0 ? "show active" : ""%>"
										style="margin-bottom: 50px;">

										<div class="technology-stack mb-0" style="border-top: none">
											<div class="tech-categories">
												<div class="tech-category">
													<div class="tech-list">
														<%
															for (String tc : tagContent) {
														%>
														<span class="tech-item"><%=tc%></span>
														<%
															}
														%>
													</div>
												</div>
											</div>
										</div>
										<div class="row" style="position: relative;">


											<div class="col-lg-6 col-md-12 mt-4">
												<div class="visual-showcase mb-0">
													<div class="main-visual">
														<div class="portfolio-details-slider swiper init-swiper">
															<script type="application/json" class="swiper-config">
{
  "loop": true,
  "speed": 0,
  "slidesPerView": 1,
  "allowTouchMove": false,
  "navigation": {
    "nextEl": ".swiper-button-next-<%=ii%>-<%=i%>",
    "prevEl": ".swiper-button-prev-<%=ii%>-<%=i%>"
  }
}
                  </script>
															<div class="swiper-wrapper" style="text-align:center;">
																<%
																	for (int imgIndex = 0; imgIndex < totalReviewNumber; imgIndex++) {
																%>
																<div class="swiper-slide"
																	style="display: flex; align-items: center; justify-content: center; margin-top: auto !important;">
																	<img src="<%=reviewsOfLoc.get(imgIndex).getImgSrc()%>"
																		alt="Project showcase" class="img-fluid"
																		loading="lazy"
																		style="width: 20rem; height: 15rem; object-fit: contain; border-radius: 10px; cursor: pointer;"/>
																</div>
																<%
																	}
																%>

															</div>

														</div>
													</div>
												</div>
											</div>

											<div class="col-lg-6 col-md-12 mt-4">
												<%
															for (int reviewIndex = 0; reviewIndex < totalReviewNumber; reviewIndex++) {
																String reviewStyle = reviewIndex == 0 ? "" : "display:none";
												%>
												<div
													class="project-overview review-contents-filter review-content-<%=i%>"
													style="<%=reviewStyle%>; padding-right:40px;">
													<h2><%=contentsGroupByLoc.get(i).get(0).getTourLocName()%></h2>
													<p class="overview-text" style="text-align: justify">
														<%=reviewsOfLoc.get(reviewIndex).getContent()%>
													</p>
												</div>
												<%
													}
												%>
											</div>

											<div
												class="swiper-button-next swiper-button-next-<%=ii%>-<%=i%>"
												onclick="sliderNextImg(<%=ii%>,<%=i%>, <%=totalReviewNumber%>)"></div>
											<div
												class="swiper-button-prev swiper-button-prev-<%=ii%>-<%=i%>"
												onclick="sliderPrevImg(<%=ii%>,<%=i%>,<%=totalReviewNumber%>)"></div>

										</div>

									</div>
									<%
										}
									%>
								</div>

							</div>
							<%
								}
							%>
						</div>

					</div>
				</div>
			</section>
<script>
let sliderImageIndices = Array.from({ length: <%= tabNumber %> }, () => new Array(<%= nodeNumber %>).fill(0));

function sliderNextImg(tIndex, nIndex, ReviewNumber){
   let totalSliderNumber = ReviewNumber;
   // 후기수만큼 슬라이드 이동했으면 다시 1번 후기로 돌아감
   if(++sliderImageIndices[tIndex][nIndex] == totalSliderNumber)
      sliderImageIndices[tIndex][nIndex] = 0; 
   setReviewContent(tIndex, nIndex)
}
function sliderPrevImg(tIndex, nIndex, ReviewNumber){
   let totalSliderNumber = ReviewNumber;
   if(--sliderImageIndices[tIndex][nIndex] == -1)
      sliderImageIndices[tIndex][nIndex] = totalSliderNumber-1;
   setReviewContent(tIndex, nIndex)
}
function setReviewContent(tIndex, nIndex){
   let reviewI = sliderImageIndices[tIndex][nIndex];
   const reviewDoms = document.querySelectorAll('#tabs-tab-' + (parseInt(tIndex)+1) + ' .review-contents-filter.review-content-'+nIndex); // NodeList
   reviewDoms.forEach((el, i) => {
       el.style.display = (i === reviewI ? 'block' : 'none');
     });
}

(function(){

  document.querySelectorAll('[id^="routeBar-"]').forEach(function(bar){
    var group = bar.getAttribute('data-group');
    var nodes = bar.querySelectorAll('.route-node');
    var panes = document.querySelectorAll('#spotPanes-' + group + ' .tab-pane');

    function showPane(id){
      panes.forEach(function(p){ p.classList.remove('show','active'); });
      var el = document.querySelector(id);
      if (el) el.classList.add('show','active');
    }

    nodes.forEach(function(btn){
      btn.addEventListener('click', function(){
        nodes.forEach(function(b){ b.classList.remove('active'); });
        btn.classList.add('active');
        showPane(btn.getAttribute('data-target'));
      });
    });
  });
})();
</script>
			<div style="text-align: center;" onclick="sendLike()">
				<i id="heartIcon" class="bi ${isLike ? 'bi-heart-fill':'bi-heart'}"
					style="font-size: 2.5rem; cursor: pointer; color: #5c99ee"></i>
				<p id="likesCount" class="mb-0" style="color: #5c99ee">
					<strong>${board.likes}</strong>
				</p>
			</div>
			<hr style="color: lightgray; margin-bottom: 50px; margin-top: 50px;">
<script>
heartIcon.addEventListener("click", () => {
	if(isLogined()){
     heartIcon.classList.toggle("bi-heart");
     heartIcon.classList.toggle("bi-heart-fill");
	}
   });

</script>
			<div class="features-intro">
				<h3>댓글</h3>

				<section>
					<div class="d-flex mb-3">
						<a href=""> <img
							src="/tripnote${loginUser.profileImage}"
							class="border rounded-circle me-2" alt="Avatar"
							style="height: 40px" />
						</a>
						<div data-mdb-input-init class="form-outline w-100 replyBox">
							<textarea class="form-control" id="textAreaExample" rows="2"></textarea>
							<div class="w-100" style="text-align: right;">
								<button type="submit" class="btn btn-primary mt-1" onclick="saveReply(this)"
									style="-bs-btn-bg: #5c99ee; - -bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;">
									등록</button>
							</div>
						</div>
					</div>

					<div id="replyContainer"></div>
					<button id="loadMoreRepliesBtn"
						class="btn btn-outline-secondary w-100 mt-2"
						style="-bs-btn-color: #5c99ee; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-bg: #5c99ee; - -bs-btn-hover-border-color: #5c99ee; - -bs-btn-hover-color: #fff;">
						댓글 더보기
					</button>
				</section>
			</div>


<!-- 			<hr style="color: lightgray; margin-bottom: 50px; margin-top: 50px;">

			<div style="display: inline-block; margin-bottom: 30px;">
				<h3 style="margin-bottom: 2rem;">다른 리뷰글</h3>
				<a href=index.jsp class="service-link" id="write_review"> 다른 리뷰글 보러가기 <i class="bi bi-arrow-right"></i>
				</a>
			</div> -->
		</div>

	</section>
	</main>

<script>
$(document).ready(function() {
	loadReplies();
});

function sendFollow(){
	const followerId = $('#user_id').text();
	const followeeId = $('#writer').text();
	let request = `/tripnote/follow?follower=\${followerId}&followee=\${followeeId}`
	$.get(request, function(){
		console.log("succeed follow request");
	});
}

function sendLike(){
	if(isLogined()){
		const userId = $('#user_id').text();
		const boardId = $('#boardId').text();
		let request = `/tripnote/board/like?userId=\${userId}&boardId=\${boardId}`
			$.get(request, function(){
				console.log("succeed like request");
				getLikesCnt();
		});		
	}else{
		alert("로그인이 필요합니다");
	}
}

function getLikesCnt(){
	const boardId = $('#boardId').text();
	let request = `/tripnote/board/like/count?boardId=\${boardId}`
		$.get(request, function(data){
			console.log("succeed like count request : " + data);
			$('#likesCount').text(data);
	});
}

function showReReplyDiv(replyIndex){
   document.querySelector('#re_reply-'+replyIndex).style.display = 'block';
}
function disableReReplyDiv(replyIndex){
   document.querySelector('#re_reply-'+replyIndex).style.display = 'none';
}

document.addEventListener('DOMContentLoaded', function() {
    const replyContainer = document.getElementById('replyContainer');
    const loadMoreBtn = document.getElementById('loadMoreRepliesBtn');

    if (replyContainer) {
        replyContainer.classList.add('collapsed');
    }

    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', () => {
            if (replyContainer.classList.contains('collapsed')) {
                replyContainer.classList.remove('collapsed');
                loadMoreBtn.textContent = '댓글 접기';
            } else {
                replyContainer.classList.add('collapsed');
                loadMoreBtn.textContent = '댓글 더보기';
            }
        });
    }
});

function saveChildReply(btn){
	if(!isLogined()){
		alert('로그인이 필요합니다');
		return;
	}
	saveReply(btn);
	$el = $(btn).closest('.replyBox');
	$el[0].style.display = 'none';
}

function saveReply(btn){
	if(!isLogined()){
		alert('로그인이 필요합니다');
		return;
	}
	
	$el = $(btn).closest('.replyBox');
	const boardId = $('#boardId').text();
	const content = $el.find('textarea').val();
	$el.find('textarea').val("");
	const userId = $('#user_id').text();
	const $reply =  $el.closest('.reply');
	let replyId = null;
	if($reply.length){
		replyId = $reply.attr('data-reply-id');
	}
	
	fetch('/tripnote/reply/save', {
		  method: 'POST',
		  headers: {
		    'Content-Type': 'application/json',
		  },
		  body: JSON.stringify({
		    boardId: boardId,
		    replyContent: content,
		    userId: userId,
		    replyId: replyId
		  })
		})
		 .then(res=>{ 
			loadReplies();
		 }
		);
}
//등록한 댓글로 focus를 옮기는 함수 - 추후 부착
function replyFocus(){
	const element = document.getElementById('targetElement');
	element.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function isLogined(){
	$el = $('#user_id');
	if($el.length){
		return true
	}else{
		return false
	}
}

function loadReplies(){
	const container = $('#replyContainer');
	container.html("");
	const boardId = $('#boardId').text();
    $.getJSON(`/tripnote/reply/list?id=\${boardId}`, function(data) {
    	$.each(data.parents, function(idx, reply){
    		const html = `
    			<div class="d-flex flex-column mb-3 reply " data-reply-id=\${reply.id}  id="replies-\${idx}">
    				<div class="d-flex">
						<a href="/tripnote/profile?username=\${reply.username}">
							<img src="/tripnote\${reply.profileImage}"
							class="border rounded-circle me-1" alt="Avatar" style="height: 40px; border: 1px solid black; border-radius: 50%;" />
						</a>
						<div>
							<div class="bg-body-tertiary rounded-3 px-3 py-1"
								style="width: 400px;">
								<span class="text-dark mb-0 fw-bold">\${reply.nickname}</span> <small>\${reply.createdAt}</small> 
								<div class="text-muted d-block mt-1">\${reply.replyContent}</div>
							</div>
							<button type="button" class="text-muted small me-2" style="border: none; background: none;" onclick="showReReplyDiv(\${idx})">
								<strong>댓글달기</strong>
							</button>
							<div id="re_reply-\${idx}" class="replyBox" style="display: none;">
								<textarea class="form-control" rows="2" style="width: 400px;"></textarea>
								<div class="d-flex my-2" style="justify-content: right">
									<button type="button" class="btn btn-primary" onclick="saveChildReply(this)" style="-bs-btn-bg: #5c99ee; - -bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;">등록</button>
									<button type="button" class="btn btn-outline-primary ms-1"
										style="-bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;"
										onclick="disableReReplyDiv(\${idx})">취소</button>
								</div>
							</div>
						</div>
    				</div>
    				<div class="childReplyContainer mt-2"></div>
				</div>
    		`;
    		container.append(html);
    	});
    	$.each(data.childs,function(idx, reply){
    		const parent = container.find(`[data-reply-id="\${reply.replyId}"]`).find(".childReplyContainer");
    		console.log("parent : " + parent +parent.attr('data-reply-id'));
    		const html = `
    			<div class="d-flex mb-3 reply ms-5"  id="replies-\${idx}">
				<a href="/tripnote/profile?username=\${reply.username}">
					<img src="/tripnote\${reply.profileImage}"
					class="border rounded-circle me-1" alt="Avatar" style="height: 40px; border: 1px solid black; border-radius: 50%;" />
				</a>
				<div>
					<div class="bg-body-tertiary rounded-3 px-3 py-1"
						style="width: 400px;">
						<span class="text-dark mb-0 fw-bold">\${reply.nickname}</span> <small>\${reply.createdAt}</small> 
						<div class="text-muted d-block mt-1">\${reply.replyContent}</div>
					</div>
				</div>
			</div>
    		`;
    		parent.append(html);
    	});
    });
}

</script>


	<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
</body>
</html>