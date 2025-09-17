<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
<!-- 헤드 부분 고정 -->
<!-- 커스텀 CSS import 존 -->
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
</head>
<body class="portfolio-details-page">
	<%@ include file="/WEB-INF/views/jspf/header.jspf"%>
	<!-- 헤더부분 고정 -->

	<main class="main board-form" data-id="${locTemplate.course.id}"> <%
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
							<strong>후기 제목</strong>
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
			<div class="post-contents d-flex justify-content-center align-items-stretch gap-4 mt-5">
			    <input id="thumbnailImgInput"
			           class="postImg-input" type="file" accept="image/*"
			           style="position: absolute; left: -9999px; display:none;" />
	
			    <!-- 대표 이미지 미리보기 영역 -->
			    <div class="thumbnail-preview text-center" style="width: 50%; max-width: 400px;">
			        <img id="boardThumbnailImg"
			             src="/tripnote/resources/assets/img/alt/no_image.png"
			             class="postImg img-fluid border"
			             alt="클릭하여 업로드"
			             style="width: 100%; height: 15rem; object-fit: cover; border-radius: 10px; cursor: pointer;" />
			        <small class="text-muted d-block mt-1">대표 이미지 설정</small>
			    </div>
	
			    <!-- 여행 소개 입력 -->
			    <textarea id="post_intro" class="form-control"
			              style="width: 50%; resize: none; height: 15rem;"
			              placeholder="이번 여행을 소개해주세요"></textarea>
			</div>
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
							<c:forEach var="day" items="${locTemplate.tourlocs}" varStatus="outerStatus">
								<div id="tabs-tab-${outerStatus.index}" data-index="${outerStatus.count}" 
									class="tab-per-day tab-pane fade ${outerStatus.first ? 'active show' : ''}">

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
														style="width: 60px; height: 60px;
														     --bs-btn-color:#5c99ee;
									                         --bs-btn-border-color:#5c99ee;
									                         --bs-btn-hover-bg:#5c99ee;
									                         --bs-btn-hover-border-color:#5c99ee;
									                         --bs-btn-hover-color:#fff;
									                         --bs-btn-active-bg:#5c99ee;
									                         --bs-btn-active-border-color:#5c99ee;
									                         --bs-btn-active-color:#fff;">
														<!-- loc.icon 은 실제 필드명으로 교체 -->
														<i class="bi" style="font-size: 1.4em"></i>
													</button>
													<small id="" class="mt-1 text-muted" style="font-size: 1.05em">${loc.tourLocName}</small>
												</div>

												<c:if test="${!innerStatus.last}">
													<div
														class="d-flex flex-column align-items-center mx-2 flex-grow-1">
														<div class="border-top border-2 w-100"></div>
													</div>
												</c:if>
											</c:forEach>
										</div>
									</div>
									<!-- /routeBar -->

									<!-- spot panes: 각 노드별 콘텐츠 -->
									<div id="spotPanes-${outerStatus.index}" class="tab-content mt-4">
										<c:forEach var="loc" items="${day}" varStatus="innerStatus">
											<div id="spot-pane-${outerStatus.index}-${innerStatus.index}"
												class="tab-pane fade ${innerStatus.first ? 'show active' : ''}"
												style="margin-bottom: 50px;">

												<div class="visual-showcase">
													<div class="main-visual">
														<div class="portfolio-details-slider swiper init-swiper">
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
															<div class="swiper-wrapper" style="text-align: center; margin-bottom: 50px;">
																<c:forEach var="idx" begin="1" end="3">
																	<div class="swiper-slide"  data-tourloc-id="${loc.id}" data-tourloc-name="${loc.tourLocName}">
																		<div class="content-area mb-2">
																			<h2>${loc.tourLocName}-${idx}</h2>
																		</div>
																		<div class="post-contents d-flex justify-content-center align-items-stretch gap-4 mb-1">
																			<input id="postImgInput-${outerStatus.index}-${innerStatus.index}"
																				class="postImg-input" type="file" accept="image/*"
																				style="position: absolute; left: -9999px; display:none" />
																				
																			<img id="postImg-${outerStatus.index}-${innerStatus.index}"
																				src="/tripnote/resources/assets/img/alt/no_image.png"
																				class="postImg img-fluid" alt="클릭하여 업로드"
																				style="width: 50%; height: 50%; max-width: 400px; object-fit: cover; border-radius: 10px; cursor: pointer;" />
																			<textarea class="form-control loc-textera"
																				style="width: 50%; resize: none;"
																				placeholder="리뷰 내용을 입력해주세요"></textarea>
																		</div>
																	</div>
																</c:forEach>
																<div class="swiper-button-next" style="right: 1px;"></div>
																<div class="swiper-button-prev" style="left: 1px;"></div>

															</div>

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

															<button id="board-save-btn" class="btn btn-primary float-end" onclick="savePost()"
																style="background-color: #5c99ee; margin-left: 10px;">완료</button>
															<button id="board-draft-btn" type="button" class="btn btn-primary float-end" onclick="saveDraft()"
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
		</div>
	</section>

	</main>

<script src="/tripnote/resources/assets/js/board_form.js"></script>
<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
	<!-- 푸터 부분 고정 -->
</body>
</html>