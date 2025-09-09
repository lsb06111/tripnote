<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf"%>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
	<%
		String boardSearchValue = request.getParameter("search");
		boardSearchValue = boardSearchValue == null ? "" : boardSearchValue;

		HashMap<String, String[]> map = new HashMap<>();
		map.put("capital", new String[] { "서울", "인천", "경기도" });
		map.put("middle", new String[] { "대전", "세종", "충청북도", "충청남도"});
		map.put("kangwon", new String[] { "강원도" });
		map.put("honam", new String[] { "광주", "전라북도", "전라남도" });
		map.put("youngnam", new String[] { "부산", "대구", "울산", "경상북도", "경상남도" });
		map.put("jeju", new String[] { "제주" });
	%>
	<section id="portfolio" class="portfolio services section mt-5">

		<div class="container section-title" style="padding-bottom: 60px;">
			<h2>리뷰 게시판</h2>
			<a href="" id="modal-reviewSelection" data-bs-toggle="modal"
				data-bs-target="#modalReviewSelection">리뷰 쓰러가기<i
				class="bi bi-arrow-right"></i></a>
		</div>
		<div class="container">
			<div class="isotope-layout" data-default-filter="*" data-layout="fitRows" data-sort="original-order">
				<div
					class="portfolio-filters-wrapper position-relative d-flex align-items-center w-100"
					style="margin-bottom: 0; width: 90% !important; margin: auto;">

					<div class="dropdown dropend">
						<a
							class="dropdown-toggle text-decoration-none text-dark fw-semibold"
							href="" role="button" data-bs-toggle="dropdown"
							aria-expanded="false"> 최신순 </a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" onclick="setBoardParam('order','latest')" style="cursor: pointer;">최신순</a></li>
							<li><a class="dropdown-item" onclick="setBoardParam('order','like')" style="cursor: pointer;">추천순</a></li>
							<li><a class="dropdown-item" href="" style="cursor: pointer;">팔로워만</a></li>
						</ul>
					</div>

					<ul class="portfolio-filters isotope-filters mb-0 position-absolute start-50 translate-middle-x d-flex gap-3">
						<li onclick="setBoardParam('loc')"><a class="text-reset">전지역</a></li>
						<li onclick="setBoardParam('loc','수도권')"><a class="text-reset" >수도권</a></li>
						<li onclick="setBoardParam('loc','충청도')"><a class="text-reset" >충청도</a></li>
						<li onclick="setBoardParam('loc','강원도')"><a class="text-reset" >강원도</a></li>
						<li onclick="setBoardParam('loc','전라도')"><a class="text-reset" >전라도</a></li>
						<li onclick="setBoardParam('loc','경상도')"><a class="text-reset" >경상도</a></li>
						<li onclick="setBoardParam('loc','제주도')"><a class="text-reset" >제주도</a></li>
					</ul>

					<form action="/tripnote/board" method="get" class="input-group ms-auto" style="width: 250px;">
						<input type="text" id="search-input" class="form-control" placeholder="검색어를 입력해주세요" name="keyword" >
						<button type="submit" id="search-button" class="btn" 
								style="-bs-btn-bg: #5c99ee; -bs-btn-hover-bg: #447fcc; -bs-btn-border-color: #5c99ee; -bs-btn-hover-border-color: #447fcc;">
							검색
						</button>
					</form>
				</div>
				<div class="row gy-4 portfolio-grid isotope-container"
					style="position: relative; width: 95%; height: 264px; margin: 0px auto auto;">
					<c:forEach var="item" items="${posts}">
						<!-- 여행 후기 item -->
						<div class="col-lg-3 col-md-6 portfolio-item isotope-item">
							<div class="service-card"
								style="padding: 15px 12px; cursor: pointer"
								onclick="location.href='/oti_team3/board/view.jsp?title=강원도 주말 힐링 여행'">
	
								<figure
									style="display: flex; align-items: center; margin: 0; width: 100%; margin: 5px">
									<img style="width: 15%; border: 1px solid black; border-radius: 50%; margin-right: 10px; cursor: pointer"
										onclick="event.stopPropagation();location.href='/oti_team3/profile.jsp?identify=9uiopa@gmail.com&name=김성배'"
										src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5gLM6Ory_xq5m06Wz-ClWzfw9Yhpst-gDRA&s"
										alt="avatar">
									<div style="display: flex; align-items: center; width: 100%">
										<div style="display: flex; flex-direction: column; margin-right: 20px; cursor: pointer"
											onclick="event.stopPropagation();location.href='/oti_team3/profile.jsp?identify=9uiopa@gmail.com&name=김성배'">
											<h6 style="margin: 0; font-size: 12px;">${item.nickname}</h6>
											<h6 style="margin: 0; font-size: 10px;">${item.createdAt}</h6>
										</div>
	
										<div
											style="display: flex; align-items: center; margin-left: auto; margin-right: 5px;">
											<i class="bi bi-heart-fill" style="color: var(- -accent-color);"></i> 
											<small style="margin-left: 2px;">${item.likes}</small>
										</div>
									</div>
								</figure>
	
								<figure>
									<img style="width: 100%; border-radius: 3px;"
										src="/oti_team3/assets/img/busan/busan1-id.jpg" alt="cover">
								</figure>
	
								<h3 class="mb-1" 
									style=" display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;">${item.title}
								</h3>
								<small style="color: #5c99ee;">${item.areaName}</small>
								<p style= "min-height: 5rem; max-height: 5rem; display: -webkit-box; -webkit-line-clamp: 4; -webkit-box-orient: vertical; overflow: hidden;">${item.boardContent}</p>
								<a href="view.jsp" class="service-link"> 자세히보기 <i
									class="bi bi-arrow-right"></i>
								</a>
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
				<div class="modal-body p-4">
					<%@ include file="/WEB-INF/views/jspf/board/reviewselection.jspf"%>
				</div>
			</div>
		</div>
	</div>
	<nav aria-label="Page navigation example">
		<ul class="pagination justify-content-center">
		  <li class="page-item ">
		    <a class="page-link">&lt;</a>
		  </li>
		  <li class="page-item active"><a class="page-link" href="">1</a></li>
		  <li class="page-item"><a class="page-link" href="">2</a></li>
		  <li class="page-item"><a class="page-link" href="">3</a></li>
		  <li class="page-item">
		    <a class="page-link" href="">&gt;</a>
		  </li>
		</ul>
	</nav>
	<script>
		document.addEventListener("DOMContentLoaded", function () {
			// 정렬 기준 드롭다운 쿼리스트링에서 읽어오기
			let board_order = new URLSearchParams(location.search).get('order');
			let orderName = '최신순';
			if (board_order == 'latest'){
				orderName = '최신순'
			}else if(board_order == 'like'){
				orderName = '추천순'
			}
			document.querySelector('.dropdown-toggle').textContent = orderName;
			
			// 지역 탭 쿼리스트링에서 읽어오기
			let loc = new URLSearchParams(location.search).get('loc');
			console.log("loc : " + loc);
			if (loc != null && loc != '' && loc != 'undefined'){
				$(".isotope-filters li a").each(function(idx, el){
					if (el.textContent == loc){
						el.parentElement.classList.add('filter-active');
					}
				});
			}else{
				$(".isotope-filters li").first().addClass('filter-active');
			}
		});
		
		function setBoardParam(key, value) {
			  const url = new URL(window.location.href);
			  if (value != null){
				  console.log("value not null : " + value);
				  url.searchParams.set(key, value);  // 기존 query 유지하면서 order만 세팅
			  }else{
				  console.log("value null : " + value);
				  url.searchParams.delete(key);
			  }
			  window.location.href = url.toString(); // url로 이동
		}
	</script>
	<%@ include file="/WEB-INF/views/jspf/footer.jspf"%>
</body>
</html>