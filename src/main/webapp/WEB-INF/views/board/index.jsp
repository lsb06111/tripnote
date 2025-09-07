<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
</head>
<body>
	<%@ include file="/WEB-INF/views/jspf/header.jspf"%>
	<%
		String boardSearchValue = request.getParameter("search");
		boardSearchValue = boardSearchValue == null ? "" : boardSearchValue;

		HashMap<String, String[]> map = new HashMap<>();
		map.put("capital", new String[] { "서울", "인천", "경기도" });
		map.put("middle", new String[] { "대전", "세종", "충청북도", "충청남도", "강원도" });
		map.put("honam", new String[] { "광주", "전라북도", "전라남도" });
		map.put("youngnam", new String[] { "부산", "대구", "울산", "경상북도", "경상남도" });
		map.put("jeju", new String[] { "제주" });
	%>
	<section id="portfolio" class="portfolio services section mt-5">

		<div class="container section-title" style="padding-bottom: 60px;">
			<h2>리뷰 게시판</h2>
			<a href="#" id="modal-reviewSelection" data-bs-toggle="modal"
				data-bs-target="#modalReviewSelection">리뷰 쓰러가기<i
				class="bi bi-arrow-right"></i></a>
		</div>
		<div class="container">
			<div class="isotope-layout" data-default-filter="*"
				data-layout="fitRows" data-sort="original-order">

				<div
					class="portfolio-filters-wrapper position-relative d-flex align-items-center w-100"
					style="margin-bottom: 0; width: 90% !important; margin: auto;">

					<div class="dropdown dropend">
						<a
							class="dropdown-toggle text-decoration-none text-dark fw-semibold"
							href="#" role="button" data-bs-toggle="dropdown"
							aria-expanded="false"> 최신순 </a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="#">최신순</a></li>
							<li><a class="dropdown-item" href="#">추천순</a></li>
							<li><a class="dropdown-item" href="#">팔로워만</a></li>
						</ul>
					</div>

					<ul
						class="portfolio-filters isotope-filters mb-0 position-absolute start-50 translate-middle-x d-flex gap-3">
						<li data-filter="*" class="filter-active">전 지역</li>
						<li data-filter=".filter-capital">수도권</li>
						<li data-filter=".filter-middle">중부권</li>
						<li data-filter=".filter-honam">호남권</li>
						<li data-filter=".filter-youngnam">영남권</li>
						<li data-filter=".filter-jeju">제주</li>
					</ul>

					<div class="input-group ms-auto" style="width: 250px;">
						<input type="text" id="search-input" class="form-control"
							placeholder="검색어를 입력해주세요" value="<%=boardSearchValue%>">
						<button type="button" id="search-button" class="btn text-white"
							style="-bs-btn-bg: #5c99ee; - -bs-btn-hover-bg: #447fcc; - -bs-btn-border-color: #5c99ee; - -bs-btn-hover-border-color: #447fcc;">
							검색</button>
					</div>
				</div>
				<div class="row gy-4 portfolio-grid isotope-container"
					style="position: relative; width: 95%; height: 264px; margin: 0px auto auto;">
					<div class="col-lg-3 col-md-6 portfolio-item isotope-item"
						data-date="" data-likes="">
						<!-- 여행 후기 item -->
						<div class="service-card"
							style="padding: 15px 12px; cursor: pointer"
							onclick="location.href='/oti_team3/board/view.jsp?title=강원도 주말 힐링 여행'">

							<figure
								style="display: flex; align-items: center; margin: 0; width: 100%; margin: 5px">
								<img
									style="width: 15%; border: 1px solid black; border-radius: 50%; margin-right: 10px; cursor: pointer"
									onclick="event.stopPropagation();location.href='/oti_team3/profile.jsp?identify=9uiopa@gmail.com&name=김성배'"
									src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5gLM6Ory_xq5m06Wz-ClWzfw9Yhpst-gDRA&s"
									alt="avatar">
								<div style="display: flex; align-items: center; width: 100%">
									<div
										style="display: flex; flex-direction: column; margin-right: 20px; cursor: pointer"
										onclick="event.stopPropagation();location.href='/oti_team3/profile.jsp?identify=9uiopa@gmail.com&name=김성배'">
										<h6 style="margin: 0; font-size: 12px;">김성배</h6>
										<h6 style="margin: 0; font-size: 10px;">2025.05.06</h6>
									</div>

									<div
										style="display: flex; align-items: center; margin-left: auto; margin-right: 5px;">
										<i class="bi bi-heart-fill"
											style="color: var(- -accent-color);"></i> <small
											style="margin-left: 2px;">555</small>
									</div>
								</div>
							</figure>

							<figure>
								<img style="width: 100%; border-radius: 3px;"
									src="/oti_team3/assets/img/busan/busan1-id.jpg" alt="cover">
							</figure>

							<h3 class="mb-1">강원도 주말 힐링 여행</h3>
							<small style="color: #5c99ee;">강원도</small>
							<p>"강원도의 맑은 공기와 조용한 자연 속에서 완벽한 휴식을 취하고, 숲길 산책과 강가에서의 피크닉을
								즐겼습니다."</p>
							<a href="view.jsp" class="service-link"> 자세히보기 <i
								class="bi bi-arrow-right"></i>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<div class="modal fade" id="modalReviewSelection" tabindex="-1"
		aria-labelledby="modalReviewSelectionLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered modal-lg"
			role="document">
			<div class="modal-content border-0 shadow">
				<div class="modal-header">
					<h5 class="modal-title" id="modalReviewSelectionLabel">여행 선택</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="닫기"></button>
				</div>
				<div class="modal-body p-4">
					<%@ include file="/WEB-INF/views/jspf/board/reviewselection.jspf"%>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
</body>
</html>