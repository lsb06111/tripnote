<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
</head>
<body>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ include file="/WEB-INF/views/jspf/header.jspf"%>

	<section id="portfolio" class="portfolio services section mt-5">
		<div class="container section-title" style="padding-bottom: 60px;">
			<h2 onclick="location.href='/tripnote/board'" style="cursor:pointer;">여행 후기</h2>
			<c:choose>
			  <c:when test="${not empty sessionScope.loginUser}">
			    <!-- 로그인 된 사용자: 후기 작성 모달을 연다 -->
	   			<span id="modal-reviewSelection" data-bs-toggle="modal" style="color:#5c99ee; cursor:pointer;" 
	   			   data-bs-target="#modalReviewSelection">후기 쓰러가기<i class="bi bi-arrow-right"></i></span>
			  </c:when>
			  <c:otherwise>
			    <!-- 비로그인 : 로그인 모달 사용 -->
			    <span data-bs-toggle="modal" data-bs-target="#modalLogin" style="color:#5c99ee; cursor:pointer;" >후기 쓰러가기</span>
			  </c:otherwise>
			</c:choose>
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
							href="" role="button" data-bs-toggle="dropdown"
							aria-expanded="false"> 최신순 </a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item"
								onclick="setBoardParam('order','latest')"
								style="cursor: pointer;">최신순</a></li>
							<li><a class="dropdown-item"
								onclick="setBoardParam('order','like')" style="cursor: pointer;">추천순</a></li>
						</ul>
					</div>
					<c:if test="${not empty sessionScope.loginUser}">
						<div class="d-flex align-items-center ms-4">
						    <span class="me-2">팔로우</span>
						    <div class="form-check form-switch m-0">
						        <input class="form-check-input" type="checkbox" id="followOnlySwitch">
						    </div>
						</div>
					</c:if>

					<ul
						class="portfolio-filters isotope-filters mb-0 position-absolute start-50 translate-middle-x d-flex gap-3">
						<li onclick="setBoardParam('loc')"><a class="text-reset">전체</a></li>
						<c:forEach var="dto" items="${locList}">
							<li onclick="setBoardParam('loc','${dto.areaName}')"><a
								class="text-reset">${dto.areaName}</a></li>
						</c:forEach>
					</ul>

					<form action="/tripnote/board" method="get"
						class="input-group ms-auto" style="width: 250px;">
						<input type="text" id="search-input" class="form-control"
							placeholder="검색어를 입력해주세요" name="keyword">
						<button type="submit" id="search-button" class="btn"
							style="-bs-btn-bg: #5c99ee; -bs-btn-hover-bg: #447fcc; -bs-btn-border-color: #5c99ee; -bs-btn-hover-border-color: #447fcc;">
							검색</button>
					</form>
				</div>
				<div class="row gy-4 portfolio-grid isotope-container"
					style="position: relative; width: 95%; height: 264px; margin: 0px auto auto;">

					<c:forEach var="item" items="${response.content}">
						<!-- 여행 후기 item -->
						<div class="col-lg-3 col-md-6 portfolio-item isotope-item">
							<div class="service-card"
								style="padding: 15px 12px;">
								<figure style="display: flex; align-items: center; margin: 0; width: 100%; margin: 5px">
									<img
										style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px; cursor: pointer"
										onclick="event.stopPropagation();"
										src="/tripnote${item.profileImage}"
										alt="avatar">
									<div style="display: flex; align-items: center; width: 100%">
										<div
											style="display: flex; flex-direction: column; margin-right: 20px; cursor: pointer"
											onclick="event.stopPropagation(); location.href='/tripnote/profile?username=${item.username}'">
											<h6 style="margin: 0; font-size: 12px;">${item.nickname} @${item.username}</h6>
											<h6 style="margin: 0; font-size: 10px;">${item.createdAt}</h6>
										</div>

										<div
											style="display: flex; align-items: center; margin-left: auto; margin-right: 5px;">
											<i class="bi bi-heart-fill"
												style="color: var(- -accent-color);"></i> <small
												style="margin-left: 2px;">${item.likes}</small>
										</div>
									</div>
								</figure>
								<div class="mt-2" style="cursor:pointer;" onclick="location.href='/tripnote/board/detail?id=${item.id}'">
									<figure>
										<img src="${item.thumbnail}" alt="cover" style=" width: 200px; height: 150px; border-radius: 3px; object-fit: contain;">
									</figure>
	
									<h3 class="mb-1"
										style="display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;">${item.title}
									</h3>
									<small style="color: #5c99ee;">${item.areaName}</small>
									<p
										style="min-height: 5rem; max-height: 5rem; display: -webkit-box; -webkit-line-clamp: 4; -webkit-box-orient: vertical; overflow: hidden;">${item.boardContent}</p>
									<a href="/tripnote/board/detail?id=${item.id}" class="service-link"> 자세히보기 <i
										class="bi bi-arrow-right"></i>
									</a>
								
								</div>
							</div>
						</div>
					</c:forEach>
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
				<div class="modal-body p-4"></div>
			</div>
		</div>
	</div>
	<c:if test="${response.totalPages > 0 }">
		<nav aria-label="Page navigation example">
			<ul class="pagination justify-content-center">
				<c:if test="${response.hasPrev}">
					<li class="page-item "><a class="page-link"
						onclick="setBoardParam('page', ${response.start - response.blockSize})"
						style="cursor: pointer;">&lt;</a></li>
				</c:if>
				<c:forEach var="num" begin="${response.start}" end="${response.end}">
					<c:choose>
						<c:when test="${num == response.page}">
							<li class="page-item active"><a class="page-link "
								onclick="setBoardParam('page', ${num})" style="cursor: pointer;">${num}</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link "
								onclick="setBoardParam('page', ${num})" style="cursor: pointer;">${num}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${response.hasNext}">
					<li class="page-item"><a class="page-link"
						onclick="setBoardParam('page', ${response.end + 1})"
						style="cursor: pointer;">&gt;</a></li>
				</c:if>
			</ul>
		</nav>

	</c:if>
<script src="/tripnote/resources/assets/js/board.js"></script>
<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
</body>
</html>