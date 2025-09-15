<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
<!-- 헤드 부분 고정 -->
<!-- 커스텀 CSS import 존 -->

</head>
<body class="portfolio-details-page">
	<%@ include file="/WEB-INF/views/jspf/header.jspf"%>
	<!-- 헤더부분 고정 -->

	<main class="main"> <!-- Page Title --> <%
 	String writeTitle = request.getParameter("title");
 %>

	<div class="page-title light-background">
		<div class="container">
			<h1 id="write_title"><%=writeTitle == null ? "제목 없음" : writeTitle%></h1>
			<nav class="breadcrumbs">
			</nav>
		</div>
	</div>
	<section id="portfolio-details" class="portfolio-details section"
		style="-subtle-border: none; padding-bottom: 0px; padding-top: 0px;">

		<div class="container">
			<section class="d-flex justify-content-center" id="write_alert"
				style="padding-bottom: 0">
				<div class="container">
					<div class="alert alert-dismissible fade show alert-danger mb-5"
						role="alert"
						style="background-color: #FFF7DE; border-color: #FFEFC2;">

						<strong><i class="bi bi-exclamation-octagon-fill"></i> 안내
							사항</strong> <br>
						<ol class="mb-0 fs-5">
							<li>각 리뷰당 사진은 <strong>3장</strong>만 올릴 수 있어요.
							</li>
							<li>리뷰의 글 작성은 선택이에요. 다만 다른 이용자분들을 위해서 작성이 권장돼요.</li>
							<li>사진을 잘못 올리셨나요? 다시 드래그 앤 드롭으로 올려주세요.</li>
							<li>각 사진당 최대 용량은 <strong>5Mb</strong>로 제한돼요.
							</li>
						</ol>

						<!-- Correct dismiss button -->
						<button type="button" class="btn-close" data-bs-dismiss="alert"
							aria-label="Close"></button>
					</div>
				</div>
			</section>

			<div class="project-hero mb-0">
				<div class="project-meta-grid" style="padding-top: 0">


					<div class="meta-column">
						<div class="meta-label">여행일정</div>
						<div class="meta-value">${locTemplate.course.startDate} ~ ${locTemplate.course.endDate}</div>
					</div>

					<div class="meta-column position-relative">
						<div class="meta-label">
							<strong>리뷰제목</strong>
						</div>
						<input type="text" id="write_title_input"
							class="form-control fs-5" placeholder="제목을 입력해주세요."
							value='<%=writeTitle == null ? "제목 없음" : writeTitle%>'
							style="text-align: center; border: 2px solid;" autofocus />

					</div>

					<div class="meta-column">
						<div class="meta-label">작성일</div>
						<div class="meta-value"><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %></div>
					</div>
				</div>
			</div>
			<div>
				<!-- Textarea -->
				<textarea class="form-control mt-5"
					style="width: 50%; resize: none; margin: 0 auto; min-height: 10rem;"
					placeholder="이번 여행을 소개해주세요"></textarea>
			</div>



			<%
				String reqTitle = request.getParameter("title");
			%>
			<section id="tabs" class="tabs section">
				<%
					String[] nodeIcons = {"house-door", "leaf-fill", "fork-knife", "leaf-fill", "house-door"};
					String[] tagNames = {"관광지", "숙소", "식당"};
					HashMap<String, String> iconMap = new HashMap<>();
					iconMap.put("leaf-fill", "관광지");
					iconMap.put("house-door", "숙소");
					iconMap.put("fork-knife", "식당");

					HashMap<String, String> tagMap = new HashMap<>();
					tagMap.put("관광지", "멋있어요,예뻐요,다양하게 볼게 많아요");
					tagMap.put("숙소", "청결해요,방음이 잘돼요,가격이 저렴해요");
					tagMap.put("식당", "식사가 맛있어요,가격이 저렴해요,매장이 청결해요");
					int num = 0;
				%>
				<div class="container">
					<div class="tabs-wrapper">
						<div class="tabs-header mb-0">
							<ul class="nav nav-tabs">
								<c:forEach var="day" items="${locTemplate.tourlocs}"
									varStatus="outerStatus">
									<li class="nav-item"><a
										class="nav-link ${outerStatus.first ? 'active' : ''}"
										data-bs-toggle="tab"
										data-bs-target="#tabs-tab-${outerStatus.index}">
											<div class="tab-content-preview">
												<span class="tab-number">${outerStatus.count}</span>
												<div class="tab-text">
													<h6 style="margin-bottom: 0">${outerStatus.count}일차</h6>
												</div>
											</div>
									</a></li>
								</c:forEach>
							</ul>
						</div>

						<div class="tab-content">
							<!-- 각 일자별 탭 패널 -->
							<c:forEach var="day" items="${locTemplate.tourlocs}"
								varStatus="outerStatus">
								<div id="tabs-tab-${outerStatus.index}"
									class="tab-pane fade ${outerStatus.first ? 'active show' : ''}">

									<!-- routeBar: 해당 일자의 loc 리스트를 사용 -->
									<div id="routeBar-${outerStatus.index}"
										data-group="${outerStatus.index}"
										class="d-flex justify-content-center my-5">
										<div
											class="d-flex align-items-center justify-content-center w-75 pb-2">
											<!-- inner: 각 loc (노드) -->
											<c:forEach var="loc" items="${day}" varStatus="innerStatus">
												<div class="d-flex flex-column align-items-center">
													<button type="button"
														class="route-node btn btn-outline-primary rounded-circle fw-bold d-flex align-items-center justify-content-center p-0 ${innerStatus.first ? 'active' : ''}"
														data-target="#spot-pane-${outerStatus.index}-${innerStatus.index}"
														data-group="${outerStatus.index}"
														style="width: 60px; height: 60px;">
														<!-- loc.icon 은 실제 필드명으로 교체 -->
														<i class="bi" style="font-size: 1.4em"></i>
													</button>
													<!-- loc.name 은 실제 필드명으로 교체 -->
													<small class="mt-1 text-muted" style="font-size: 1.05em">${loc.tourLocName}</small>
												</div>

												<c:if test="${!innerStatus.last}">
													<div
														class="d-flex flex-column align-items-center mx-2 flex-grow-1">
														<div class="border-top border-2 w-100"></div>
														<span
															class="badge rounded-pill text-primary bg-primary-subtle fw-semibold mt-1 px-3 py-2"
															style="font-size: 0.85em"> <i
															class="bi bi-bus-front"></i> <!-- loc.time 은 실제 필드명으로 교체 (다음 노드까지의 소요) -->
															${loc.timeTaken}
														</span>
													</div>
												</c:if>
											</c:forEach>
										</div>
									</div>
									<!-- /routeBar -->

									<!-- spot panes: 각 노드별 콘텐츠 -->
									<div id="spotPanes-${outerStatus.index}"
										class="tab-content mt-4">
										<c:forEach var="loc" items="${day}" varStatus="innerStatus">
											<div id="spot-pane-${outerStatus.index}-${innerStatus.index}"
												class="tab-pane fade ${innerStatus.first ? 'show active' : ''}"
												style="margin-bottom: 50px;">

												<div class="visual-showcase">
													<div class="main-visual">
														<div class="portfolio-details-slider swiper init-swiper">
															<div class="swiper-wrapper"
																style="text-align: center; margin-bottom: 50px;">
																<div class="swiper-slide">
																	<div class="content-area mb-2">
																		<h2>리뷰 작성 (사진)</h2>
																	</div>

																	<div
																		class="post-contents d-flex justify-content-center align-items-stretch gap-4 mb-1">
																		<!-- input/img: id는 유니크하게 -->
																		<input
																			id="postImgInput-${outerStatus.index}-${innerStatus.index}"
																			class="postImg-input" type="file" accept="image/*"
																			style="position: absolute; left: -9999px;" /> <img
																			id="postImg-${outerStatus.index}-${innerStatus.index}"
																			src="/tripnote/assets/img/alt/no_image.png"
																			class="postImg img-fluid" alt="클릭하여 업로드"
																			style="width: 50%; height: 50%; max-width: 400px; object-fit: cover; border-radius: 10px; cursor: pointer;" />
																		<textarea class="form-control"
																			style="width: 50%; resize: none;"
																			placeholder="리뷰 내용을 입력해주세요"></textarea>
																	</div>
																</div>

																<!-- swiper nav -->
																<div class="swiper-button-next"
																	style="right: 1px; z-index: 9999;"></div>
																<div class="swiper-button-prev"
																	style="left: 1px; z-index: 9999;"></div>

															</div>
															<!-- /swiper-wrapper -->

															<!-- 태그(예: loc.tags 가 있다면) -->
															<div class="technology-stack"
																style="margin-bottom: 10px;">
																<div class="row">
																	<div class="col-lg-3">
																		<h3>여행 태그</h3>
																	</div>
																	<div class="col-lg-9">
																		<div class="tech-categories">
																			<div class="tech-category">
																				<div
																					id="tag-buttons-${outerStatus.index}-${innerStatus.index}"
																					class="tech-list">
	<%-- 																				<c:forEach var="tag" items="${loc.tags}"
																						varStatus="tagStatus">
																						<input type="checkbox" class="btn-check"
																							id="btn-${outerStatus.index}-${innerStatus.index}-${tagStatus.index}"
																							autocomplete="off" />
																						<label class="custom-toggle"
																							for="btn-${outerStatus.index}-${innerStatus.index}-${tagStatus.index}">${tag}</label>
																					</c:forEach> --%>
																				</div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>

															<!-- 추천 버튼 -->
															<div class="technology-stack">
																<div class="row">
																	<div class="col-lg-3">
																		<h3>관광지 추천</h3>
																	</div>
																	<div class="col-lg-9">
																		<div class="tech-categories">
																			<div class="tech-category">
																				<div class="tech-list">
																					<i class="recommendation-btn bi bi-hand-thumbs-up"
																						data-place-id="${loc.id}"
																						style="font-size: 2.5rem; cursor: pointer; color: #5c99ee; position: relative; top: -10px;"></i>
																				</div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>

															<button type="button" class="btn btn-primary float-end"
																style="background-color: #5c99ee; margin-left: 10px;">업로드</button>
															<button type="button" class="btn btn-primary float-end"
																style="background-color: #5c99ee">임시저장</button>

														</div>
													</div>
												</div>

											</div>
										</c:forEach>
									</div>
									<!-- /spotPanes -->
								</div>
								<!-- /tab-pane -->
							</c:forEach>
						</div>
						<!-- /tab-content -->
					</div>
				</div>
			</section>
			<!-- /Tabs Section -->

			<script type="application/json" class="swiper-config">
              {
                "loop": true,
                "speed": 600,
                "effect": "creative",
                "creativeEffect": {
                  "prev": {
                    "shadow": false,
                    "translate": ["-120%", 0, -500]
                  },
                  "next": {
                    "shadow": false,
                    "translate": ["120%", 0, -500]
                  }
                },
                "slidesPerView": 1,
                "navigation": {
                  "nextEl": ".swiper-button-next",
                  "prevEl": ".swiper-button-prev"
                }
              }
</script>
			<script>
$(function() {
	  // 각 .post-contents 블록(또는 이미지+input 쌍)을 기준으로 처리합니다.
	  // 구조가 다르다면 selector를 적절히 바꿔주세요.
	  $('.post-contents').each(function() {
	    const $block = $(this);
	    const $input = $block.find('.postImg-input');
	    const $img = $block.find('.postImg');

	    // input 숨김 방식 개선 (display:none 대신 화면 밖으로 빼기 권장)
	    // 이 CSS를 JS로 적용하는건 선택사항입니다. 이미 style에 display:none이 있다면 바꿔주세요.
	    $input.css({
	      position: 'absolute',
	      left: '-9999px',
	      width: '1px',
	      height: '1px',
	      overflow: 'hidden',
	      opacity: 0
	    });

	    $img.on('click', function(e) {
	      // 같은 블록의 input만 클릭 (전역 trigger 금지)
	      // 리셋(같은 파일 재선택 허용) - 브라우저가 같은 파일 선택 시 change 이벤트가 안 될 수 있음
	      $input.val('');
	      // 네이티브 클릭 사용
	      const el = $input[0];
	      if (el) el.click();
	    });

	    $input.on('change', function(e) {
	      const file = this.files && this.files[0];
	      if (!file) return;

	      const allowed = ['image/png','image/jpeg','image/gif','image/webp'];
	      const maxSize = 5 * 1024 * 1024; // 5MB

	      if (!allowed.includes(file.type)) {
	        alert('지원하지 않는 파일 형식입니다. PNG/JPG/GIF/WebP만 허용됩니다.');
	        $input.val('');
	        return;
	      }
	      if (file.size > maxSize) {
	        alert('파일 크기는 5MB 이하로 업로드 해주세요.');
	        $input.val('');
	        return;
	      }

	      const reader = new FileReader();
	      reader.onload = function(ev) {
	        $img.attr('src', ev.target.result);
	      };
	      reader.readAsDataURL(file);
	    });

	    // input 클릭 시 이벤트 버블/포커스로 인해 슬라이더 버튼이 먹히지 않는 문제 예방
	    $input.on('click', function(ev) { ev.stopPropagation(); });
	  });

	});


<!-- recommendation button -->
  const root = document.getElementById('itineraryRoot') || document;

  root.addEventListener('click', (e) => {
    const btn = e.target.closest('.recommendation-btn');
    if (!btn || !root.contains(btn)) return;

    if (btn.classList.contains('bi-hand-thumbs-up')) {
      btn.classList.remove('bi-hand-thumbs-up');
      btn.classList.add('bi-hand-thumbs-up-fill');
      btn.style.color = '#5c99ee';
      
    } else {
      btn.classList.remove('bi-hand-thumbs-up-fill');
      btn.classList.add('bi-hand-thumbs-up');
      btn.style.color = '#5c99ee';
    }
  });
</script>



			<script>

const writeTitleInputDom = document.querySelector('#write_title_input');
const writeTitleDom = document.querySelector('#write_title');
writeTitleInputDom.addEventListener('keyup', ()=> {
	
	if(writeTitleInputDom.value == '')
		writeTitleDom.textContent = '제목 없음';
	else
		writeTitleDom.textContent = writeTitleInputDom.value;
});


(function(){
  // initialize all day groups
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
</style>

		</div>
	</section>

	</main>

	<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
	<!-- 푸터 부분 고정 -->
</body>
</html>