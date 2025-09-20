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
	background-color: white;
	color: black;
	border: 1px solid #ccc;
	border-radius: 30px;
	padding: 6px 16px;
	display: inline-block;
	cursor: pointer;
	transition: all 0.3s ease;
}

.btn-check:checked+.custom-toggle {
	background-color: #5c99ee;
	color: white;
	border-color: #0d6efd;
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

	<main class="main">
	<div class="page-title light-background">
		<div class="container">
			<h1>${board.title}</h1>
		</div>
	</div>
	<section id="portfolio-details" class="portfolio-details section"
		style="-subtle-border: none; padding-bottom: 0px;">
		<div class="container">
			<div class="project-hero" style="margin-bottom: 0;">
				<div class="project-meta-grid" style="padding-top: 0">
					<div class="meta-column">
						<div class="meta-label">작성자</div>
						<div class="meta-value">${board.nickname}</div>
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
						<div class="meta-value">
							<input type="checkbox" class="btn-check" id="btn1"
								autocomplete="off" /> <label class="custom-toggle" for="btn1">팔로우
								신청</label>

						</div>
					</div>
				</div>
			</div>


			<%
				String reqTitle = request.getParameter("title");
			%>
			<section id="tabs" class="tabs section mt-5">
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
											String[] spots = {"게스트하우스 감천", // 시작 숙소
														"감천문화마을", // 메인 관광지
														"할매 가야밀면", // 관광지
														"자갈치 시장", // 관광지
														"부산타워" // 마지막 숙소 (처음과 동일)
												};
												String[] times = {"15분", "12분", "20분", "18분"};
												String[] nodeIcons = {"house-door", "leaf-fill", "fork-knife", "leaf-fill", "leaf-fill"};

												HashMap<String, String> tagContents = new HashMap<>();
												tagContents.put("leaf-fill", "멋있어요,예뻐요,다양하게 볼게 많아요");
												tagContents.put("house-door", "청결해요,방음이 잘 돼요,가격이 저럼해요");
												tagContents.put("fork-knife", "식사가 맛있어요,가격이 저렴해요,매장이 청결해요");

												for (int i = 0; i < nodeNumber; i++) {
										%>
										<div class="d-flex flex-column align-items-center">
											<button type="button"
												class="route-node btn btn-outline-primary rounded-circle fw-bold d-flex align-items-center justify-content-center p-0 <%=i == 0 ? "active" : ""%>"
												data-target="#spot-pane-<%=ii%>-<%=i%>"
												data-group="<%=ii%>"
												style="width: 60px; height: 60px; - -bs-btn-color: #5c99ee; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-bg: #5c99ee; - -bs-btn-hover-border-color: #5c99ee; - -bs-btn-hover-color: #fff; - -bs-btn-active-bg: #5c99ee; - -bs-btn-active-border-color: #5c99ee; - -bs-btn-active-color: #fff;">
												<i class="bi bi-<%=nodeIcons[i]%>"
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


															<div class="swiper-wrapper" style="text-align: center;">

																<%
																	for (int imgIndex = 0; imgIndex < totalReviewNumber; imgIndex++) {
																%>
																<div class="swiper-slide"
																	style="display: flex; align-items: center; justify-content: center; margin-top: auto !important;">
																	<img src="<%=reviewsOfLoc.get(imgIndex).getImgSrc()%>"
																		alt="Project showcase" class="img-fluid"
																		loading="lazy"
																		style="width: 80%; border-radius: 10px; height: auto;">
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
	   console.log("***************sliderImageIndices :" + sliderImageIndices);
	   console.log("***************reviewI :" + reviewI);
	   console.log("***************i :" + i);
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

			<div style="text-align: center;">
				<i id="heartIcon" class="bi bi-heart"
					style="font-size: 2.5rem; cursor: pointer; color: #5c99ee"></i>
				<p class="mb-0" style="color: #5c99ee">
					<strong>253</strong>
				</p>
			</div>
			<hr style="color: lightgray; margin-bottom: 50px; margin-top: 50px;">
			<script>
heartIcon.addEventListener("click", () => {
     heartIcon.classList.toggle("bi-heart");
     heartIcon.classList.toggle("bi-heart-fill");

     
   });

</script>

			<div class="features-intro">
				<h3>댓글</h3>

				<section>
					<%
						int totalReply = 6;
						String[] replyIdentify = {"jdy19823", "lsb112323", "ksb234", "jdy19823", "lsb112323", "ksb234"};
						String[] replyName = {"정동윤", "이수빈", "김성배", "박지원", "오상민", "최은영"};
						String[] replyContents = {"사진 보니까 당장이라도 가고 싶네요 ㅎㅎ", "감천문화마을 진짜 알록달록해서 사진 맛집이죠!",
								"여기 가면 꼭 벽화 앞에서 사진 찍어야 합니다 👍", "저도 지난달에 다녀왔는데 분위기가 너무 좋았어요", "부산 가면 무조건 들르는 코스 중 하나에요 ^^",
								"와 설명이 자세해서 참고 많이 됐습니다~ 감사합니다!"};
					%>

					<div class="d-flex mb-3">
						<a href=""> <img
							src="https://mdbcdn.b-cdn.net/img/new/avatars/18.webp"
							class="border rounded-circle me-2" alt="Avatar"
							style="height: 40px" />
						</a>
						<div data-mdb-input-init class="form-outline w-100">
							<textarea class="form-control" id="textAreaExample" rows="2"></textarea>
							<div class="w-100" style="text-align: right;">
								<button type="submit" class="btn btn-primary mt-1"
									style="-bs-btn-bg: #5c99ee; - -bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;">
									등록</button>
							</div>
						</div>
					</div>

					<div id="replyContainer">
						<%
							for (int i = 0; i < totalReply; i++) {
						%>
						<div class="d-flex mb-3" id="replies-<%=i%>">
							<a
								href="/oti_team3/profile.jsp?identify=<%=replyIdentify[i]%>&name=<%=replyName[i]%>">
								<img
								src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5gLM6Ory_xq5m06Wz-ClWzfw9Yhpst-gDRA&s"
								class="border rounded-circle me-2" alt="Avatar"
								style="height: 40px; border: 1px solid black; border-radius: 50%;" />
							</a>
							<div>
								<div class="bg-body-tertiary rounded-3 px-3 py-1"
									style="width: 400px;">
									<strong class="text-dark mb-0"><%=replyName[i]%></strong> <small
										class="text-muted d-block"><%=replyContents[i]%></small>
								</div>
								<button type="button" class="text-muted small me-2"
									style="border: none; background: none;"
									onclick="showReReplyDiv(<%=i%>)">
									<strong>댓글달기</strong>
								</button>
								<div id="re_reply-<%=i%>" style="display: none;">
									<textarea class="form-control" rows="2" style="width: 400px;"></textarea>
									<div class="d-flex my-2" style="justify-content: right">
										<button type="button" class="btn btn-primary"
											style="-bs-btn-bg: #5c99ee; - -bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;">
											등록</button>
										<button type="button" class="btn btn-outline-primary ms-1"
											style="-bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;"
											onclick="disableReReplyDiv(<%=i%>)">취소</button>
									</div>
								</div>

							</div>
						</div>
						<%
							}
						%>
					</div>
					<button id="loadMoreRepliesBtn"
						class="btn btn-outline-secondary w-100 mt-2"
						style="-bs-btn-color: #5c99ee; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-bg: #5c99ee; - -bs-btn-hover-border-color: #5c99ee; - -bs-btn-hover-color: #fff;">
						댓글 더보기
					</button>
				</section>
			</div>


			<hr style="color: lightgray; margin-bottom: 50px; margin-top: 50px;">

			<div style="display: inline-block; margin-bottom: 30px;">
				<h3 style="margin-bottom: 2rem;">다른 리뷰글</h3>
				<a href=index.jsp class="service-link" id="write_review"> 다른 리뷰글 보러가기 <i class="bi bi-arrow-right"></i>
				</a>
			</div>
			<%@ include file="/WEB-INF/views/jspf/board/others.jspf"%>

		</div>

	</section>
	</main>

	<script>
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
</script>


	<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
</body>
</html>