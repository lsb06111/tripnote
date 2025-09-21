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
    
	//팔로우만 보기 관련
	const followCheckbox = $('#followOnlySwitch');
    followCheckbox.on('change', function() {
        setBoardParam('filter', $(this).prop('checked') ? 'follow' : null);
    });
    
	// 팔로우만 조회 쿼리스트링에서 읽어오기
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get('filter') === 'follow') {
	    followCheckbox.prop('checked', true);
	}
	
	//후기 쓰러가기 모달 
	$('#modalReviewSelection').on('show.bs.modal', function (e) {
		
		  const $body = $(this).find('.modal-body');
			
		  $.get('/tripnote/board/mycourse', function (list) {
			  $body.html("");
			  $.each(list,function(idx, el){
				  $body.append(`
					<form id="mycourse-form-${idx}" action="/tripnote/board/form" method="post" style="display:none;">
						<input name="courseId" value=${el.courseId} />
						<input name="user" value="인증정보" />
						<input type="submit" value="제출"> 
					</form>
					<section id="services" class="services section" style="padding: 0">
						<div class="container">
						<div class="row gy-4" id="tripCardsRow">
							<div class="col-12">
								
								<div class="service-card" data-location="title"
									style="padding: 18px 24px;">
									<div style="display: flex; align-items: center; gap: 8px; margin-bottom: 10px">
										<div class="service-icon" style="margin-bottom: 0">
											<i class="bi"></i>
										</div>
										<div style="margin-left: 10px;">
											<h3 style="margin: 0;">${el.title}</h3>
											<ol class="breadcrumb mb-0" style="display: flex;">
												<li class="breadcrumb-item">${el.startDate} ~ ${el.endDate}</li>
												<li class="breadcrumb-item"><strong
													style="color: #5c99ee">${el.loc}</strong>
												</li>
											</ol>
										</div>
										<span  class="service-link ms-auto"
											style="width: fit-content; transition: color 0.3s; color: inherit; cursor: pointer;"
											onclick="document.getElementById('mycourse-form-${idx}').submit()"
											onmouseover="this.style.color='#5c99ee';"
											onmouseout="this.style.color='inherit';"> 리뷰 작성하기 <i class="bi bi-arrow-right"></i>
										</a>
									</div>
								</div>
							</div>
						</div>
						</div>
					</section>
					`	  
				  );
			  });
		    
		  }); 
		});

	
});

function setBoardParam(key, value) {
	  const url = new URL(window.location.href);
	  url.searchParams.delete('page');
	  if (value != null){
		  url.searchParams.set(key, value);  // 기존 query 유지하면서 order만 세팅
	  }else{
		  url.searchParams.delete(key);
	  }
	  window.location.href = url.toString(); // url로 이동
}

