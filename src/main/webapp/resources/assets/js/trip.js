//Calendar
function formatDate(date) {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1);
  const d = String(date.getDate());
  return `${y}.${m}.${d}`;
}

(function(factory) {
    "use strict";
    if (typeof define === "function" && define.amd) {
        // AMD. Register as an anonymous module.
        define([ "../widgets/datepicker" ], factory);
    } else {
        // Browser globals
        factory(jQuery.datepicker);
    }
})(function(datepicker) {
    "use strict";
    datepicker.regional.ko = {
        closeText : "닫기",
        prevText : "이전달",
        nextText : "다음달",
        currentText : "오늘",
        monthNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        monthNamesShort : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        dayNames : [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
        dayNamesShort : [ "일", "월", "화", "수", "목", "금", "토" ],
        dayNamesMin : [ "일", "월", "화", "수", "목", "금", "토" ],
        weekHeader : "주",
        dateFormat : "yy. m. d.",
        firstDay : 0,
        isRTL : false,
        showMonthAfterYear : true,
        yearSuffix : "년"
    };
    datepicker.setDefaults(datepicker.regional.ko);
    return datepicker.regional.ko;
});

$("#trip-start-date").datepicker({
	minDate: new Date(),
    onSelect : function(dateText) {
        const start = $(this).datepicker("getDate");
        const end = new Date(start);
        end.setDate(end.getDate() + 5);
        $("#trip-end-date").datepicker("option", "minDate", start);
        $("#trip-end-date").datepicker("option", "maxDate", end);
        tripStartDate = formatDate($("#trip-start-date").datepicker("getDate"));
    },

});

$("#trip-end-date").datepicker({
	onSelect : function(dateText) {
		tripEndDate = formatDate($("#trip-end-date").datepicker("getDate"));
	}
});

let tripStartDate = "";
let tripEndDate = "";
let tripDest = ""; // 여행 지역
function checkForNextStep(userId){
	event.preventDefault();
	tripDest = $('input[name="locationRadio"]:checked').next().text().trim();
 	if (tripStartDate && tripEndDate && tripDest){
 		const courseTitle = document.querySelector('#course_title').value;
 		location.href = '/tripnote/trip/plan?startDate='
 			+tripStartDate+'&endDate='+tripEndDate+'&tripDest='+tripDest
 			+'&userId='+userId+'&courseTitle='+courseTitle;
 		
 		//$('#trip-loc-step').trigger('click');
 	}else{
 		alert("여행 날짜와 여행지를 먼저 선택하세요.");
 	}
}


// 탭 인디케이터 위치/너비 갱신
function setTab(id){
  const tabs = document.getElementById(id);
  if(!tabs) return;

  // 인디케이터 없으면 만들어 줌
  let indicator = tabs.querySelector('.trip-tab-indicator');
  if(!indicator){
    indicator = document.createElement('span');
    indicator.className = 'trip-tab-indicator';
    indicator.style.position = 'absolute';
    indicator.style.left = '0';
    indicator.style.bottom = '0';
    indicator.style.height = '2px';
    indicator.style.width = '0';
    indicator.style.transition = 'transform 150ms ease, width 150ms ease, left 150ms ease';
    tabs.style.position = tabs.style.position || 'relative';
    tabs.appendChild(indicator);
  }

  const getItems = () => Array.from(tabs.querySelectorAll('.trip-tab'));

  function updateIndicator(target){
    if(!target || !indicator) return;
    // 한 프레임 양보해 레이아웃이 안정된 뒤 측정
    requestAnimationFrame(() => {
      const left  = target.offsetLeft - (tabs.scrollLeft || 0);
      const width = target.offsetWidth;
      indicator.style.left  = left + 'px';
      indicator.style.width = width + 'px';
    });
  }

  function setActive(btn){
    if(!btn) return;
    const items = getItems();
    items.forEach(b => {
      const active = (b === btn);
      b.classList.toggle('is-active', active);
      b.setAttribute('aria-selected', active ? 'true' : 'false');
    });
    updateIndicator(btn);
    // 필요 시 실제 날짜 변경 로직 호출
    // onChangeDay?.(btn.dataset.day);
  }

  function init(){
    const items = getItems();
    if(items.length === 0) return;
    const current = tabs.querySelector('.trip-tab.is-active') || items[0];
    setActive(current);
  }

  // 클릭 위임
  tabs.addEventListener('click', (e) => {
    const btn = e.target.closest('.trip-tab');
    if(btn && tabs.contains(btn)) setActive(btn);
  });

  // 초기화: 로드/폰트 로드/리사이즈 시 재계산
  window.addEventListener('load', init);
  if (document.fonts && document.fonts.ready) document.fonts.ready.then(init).catch(()=>{});
  window.addEventListener('resize', () => {
    const current = tabs.querySelector('.trip-tab.is-active');
    updateIndicator(current);
  });

  // 동적 추가 대응: 자식 변화를 감지해 다시 초기화
  new MutationObserver(() => init())
    .observe(tabs, { childList: true, subtree: true });
  // 즉시 한 번 시도
  init();
} //setTab 함수 종료
setTab("trip-loc-tab");
setTab("dayTabs");

// 직접 검색 or 추천 선택 탭
function toggleLocTab(btn){
	const searchFormE = document.getElementById('trip-search-form');
	const recomE = document.getElementById('trip-rcmds');
	const searchResult = document.getElementById('trip-search-result');
	if(btn.id === 'trip-tab-search'){
		searchFormE.style.display = "block";
		searchResult.style.display = "block";
		recomE.style.display = "none";
	}else{
		searchFormE.style.display = "none";
		searchResult.style.display = "none";
		recomE.style.display = "block";
	}
	removeMarker();
}


// 일자별 여행 코스 탭 전환
function switchDay(btn){
	fixMarkers.forEach(function(fmarker) {
		console.log(fmarker);
	    fmarker.setMap(null);
	});
	fixMarkers = [];
	routePolylines.forEach(line => line.setMap(null));
	routePolylines = [];
	routeTimeOverlays.forEach(overlay => overlay.setMap(null));
	routeTimeOverlays = [];
	document.querySelectorAll('.route-time').forEach(function(element) {
	    element.remove();
	});
	let idx = btn.getAttribute("data-day");
	
	let timelineDoms = document.querySelectorAll(".trip-timelineForDay");
	timelineDoms.forEach((el, i) => {
		el.style.display = (i+1 == idx ? 'block' : 'none');
		if(i+1 == idx){
			console.log(idx);
			el.querySelectorAll(".trip-timeline-event")
			.forEach(e => drawFixMarker(e.dataset.mapx, e.dataset.mapy));
		}
	});
}; 
var isInsertedToTimeline = false;
// 장소를 타임라인에 추가
function insertToTimeline(event, btn, contentId, tourLocName, imgSrc, courseId, tourType, mapx, mapy) {
	event.stopPropagation();
	drawFixMarker(mapx, mapy);
	isInsertedToTimeline = true;
	const container = $(btn).closest(".trip-loc-card");
	const title = container.find('.sl-name').text();
	const type = container.find('.sl-type').text();
	let img = container.find('img');
	if (img){
		img =  img.attr('src')
	}
	  
	  // 표시 중인 타임라인(보이는 것) 선택
	const $target = $('.trip-timelineForDay:visible').first();
	console.log($target);
	const order = $target.children().length+1;
	
	let tourNth = document.querySelector('#dayTabs .trip-tab.is-active').textContent.split(' ')[1].substring(0,1);
	let tourLocId = -1;
	$.post("/tripnote/trip/saveTour",
			{
				code : contentId,
				tourLocName : tourLocName,
				imgSrc : imgSrc,
				courseId : courseId,
				tourOrder : order,
				tourNth : tourNth,
				typeName : tourType
			},
			function(data, status){
				$target.append(getTimelineEvent(title, type, img, order, data.id, '', mapx, mapy));
			});
	
	
};

function getTimelineEvent(title, type, img, order, tourLocId, noteContent, mapx, mapy){
	let text = `
	<div class="trip-timeline-event d-flex flex-column p-2" data-mapx="${mapx}" data-mapy="${mapy}">
		<div class="tour-loc-id" style="display:none">${tourLocId}</div>
		<div class="d-flex w-100">
			<div class="trip-idx m-1">${order}</div>
			<div class="event-info mx-2">
				<div class="time">12:55-12:55</div>
				<div style="color: #ff7a7a;">${type}</div>
				<div style="white-space: nowrap">${title}</div>
			</div>
			<img src="${img}" alt="이미지 없음" class="event-img" />
			<div class="d-flex flex-column justify-content-around">
			    <i class="trip-note-toggle bi bi-pencil-square ms-2" onclick="toggleNote(this)" style="font-size:1.4em"></i>
			    <i class="bi bi-trash ms-2" style="font-size:1.4em; cursor:pointer;"  onclick="deleteNote(this, '${mapx}', '${mapy}')"></i>
			</div>
			
		</div>
		<div class="trip-note-area">
			<textarea id="content_${tourLocId}" class="pe-5" placeholder="메모를 입력하세요...">${noteContent}</textarea>
			<button class="btn btn-light save-note-btn"onclick="saveNote(${tourLocId})">저장</button>
		</div>
	</div>`;
	return text;
}
function saveNote(tourLocId){
	const noteContent = document.querySelector('#content_'+tourLocId).value;
	$.post('/tripnote/trip/saveNote',
			{
				tourLocId : tourLocId,
				noteContent : noteContent
			});
}
// tripnote 관광지의 순서번호 부여
function assignLocIndex() {
    let dataObjs = [];
    let $list = $('.trip-timelineForDay:visible');

    // 1. 새 마커 순서를 담을 임시 배열 생성
    let newFixMarkers = [];
    const epsilon = 0.000001;

    // 2. DOM 순서대로 타임라인 카드를 순회
    $list.children().each(function (idx, el) {
        // 화면의 순번 업데이트 (기존 로직)
        $(el).find('.trip-idx').text(idx + 1);

        // DB 업데이트용 데이터 준비 (기존 로직)
        let dataObj = {};
        dataObj.id = parseInt($(el).find('.tour-loc-id').text()); // 올바른 ID 선택
        dataObj.tourOrder = (idx + 1);
        dataObjs.push(dataObj);

        // --- ✅ fixMarkers 재정렬 로직 시작 ---
        
        // 3. data 속성에서 좌표 읽기
        const cardMapx = $(el).data('mapx');
        const cardMapy = $(el).data('mapy');

        // 4. 기존 fixMarkers 배열에서 좌표가 일치하는 마커 찾기
        const foundMarker = fixMarkers.find(fmarker => {
            const markerPosition = fmarker.getPosition();
            return Math.abs(markerPosition.getLng() - cardMapx) < epsilon &&
                   Math.abs(markerPosition.getLat() - cardMapy) < epsilon;
        });

        // 5. 찾은 마커를 새 배열에 순서대로 추가
        if (foundMarker) {
            newFixMarkers.push(foundMarker);
        }
        // --- fixMarkers 재정렬 로직 종료 ---
    });

    // 6. 기존 fixMarkers 배열을 재정렬된 새 배열로 교체
    fixMarkers = newFixMarkers;

    // DB에 순서 업데이트 요청 (기존 로직)
    $.ajax({
        url: "/tripnote/trip/updateTour",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(dataObjs),
        success: function (data, status) { console.log("Tour order updated!"); }
    });

    // 경로 다시 그리기 (기존 로직)
    if (fixMarkers.length < 2) {
        // 마커가 2개 미만이면 기존 경로 지우기
        routePolylines.forEach(line => line.setMap(null));
        routeTimeOverlays.forEach(overlay => overlay.setMap(null));
        routePolylines = [];
        routeTimeOverlays = [];
    } else if (transportSelected == 'car') {
        drawRoute(transportSelected);
    } else if (transportSelected != '') {
        drawSimplePolyline('walk');
    }
}

function toggleNote(btn) {
	btn.classList.toggle("active");
	const note = btn.parentElement.parentElement.nextElementSibling;
	if (note.style.display === "block") {
		note.style.display = "none";
	} else {
		note.style.display = "block";
	}
}

function deleteNote(btn, mapx, mapy){
	console.log(fixMarkers);
	//
	const epsilon = 0.000001;

	for (var i = fixMarkers.length - 1; i >= 0; i--) {
		var fmarker = fixMarkers[i];
		var markerPosition = fmarker.getPosition();

		// ✅ '==' 대신 두 좌표의 차이가 허용 오차보다 작은지 확인
		if (Math.abs(markerPosition.getLng() - mapx) < epsilon &&
			Math.abs(markerPosition.getLat() - mapy) < epsilon) {

			// 1. 지도에서 마커를 보이지 않게 합니다.
			fmarker.setMap(null);

			// 2. fixMarkers 배열에서 해당 마커를 제거합니다.
			fixMarkers.splice(i, 1);
		}
	}
	
	//
	$card = $(btn).closest('.trip-timeline-event');
	let tourLocId = $card.find(".tour-loc-id").text().trim();
	console.log(tourLocId);
	$card.remove();
	$.ajax({
		url: '/tripnote/trip/deleteTour?id='+tourLocId,
		type: "GET"
	});
	if(transportSelected == 'car')
		drawRoute(transportSelected);
	else if(transportSelected != '')
		drawSimplePolyline('walk');
	
	assignLocIndex();
}


// tripnote의 관광지 드래그 가능하도록 만들기 - jquery ui api
$( function() {
	$tripNote = $( ".trip-timelineForDay" );
  	$tripNote.sortable({
		update: function() { // sorting 되었을 때
			assignLocIndex();
		}
	});
} );

// url 복사 버튼
async function copy(btn){
	const input = btn.previousElementSibling;
	  try {
    await navigator.clipboard.writeText(input.value);
    alert("복사되었습니다!");
  } catch (err) {
    // 구형 브라우저 fallback
    input.select();
    document.execCommand("copy");
    alert("복사되었습니다!(fallback)");
  }
	
}