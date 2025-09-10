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
 		location.href = '/tripnote/trip/plan?startDate='
 			+tripStartDate+'&endDate='+tripEndDate+'&tripDest='+tripDest
 			+'&userId='+userId;
 		
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
	let idx = btn.getAttribute("data-day");
	let timelineDoms = document.querySelectorAll(".trip-timelineForDay");
	timelineDoms.forEach((el, i) => {
		el.style.display = (i+1 == idx ? 'block' : 'none');
	});
}; 

// 장소를 타임라인에 추가
function insertToTimeline(btn, contentId, tourLocName, imgSrc, courseId) {

	const container = $(btn).closest(".trip-loc-card");
	const title = container.find('.sl-name').text();
	const type = container.find('.sl-type').text();
	let img = container.find('img');
	if (img){
		img =  img.attr('src')
	}
	  
	  // 표시 중인 타임라인(보이는 것) 선택
	const $target = $('.trip-timelineForDay:visible').first();
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
				tourNth : tourNth
			},
			function(data, status){
				$target.append(getTimelineEvent(title, type, img, order, data.id));
			});
	
	
};

function getTimelineEvent(title, type, img, order, tourLocId){
	let text = `
	<div class="trip-timeline-event d-flex flex-column p-2">
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
			    <i class="bi bi-trash ms-2" style="font-size:1.4em; cursor:pointer;"  onclick="deleteNote(this)"></i>
			</div>
			
		</div>
		<div class="trip-note-area">
			<textarea id="content_${tourLocId}" class="pe-5" placeholder="메모를 입력하세요..."></textarea>
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
function assignLocIndex(){
	$list = $('.trip-timelineForDay:visible');
	$list.children().each(function(idx, el){
		$(el).find('.trip-idx').text(idx+1);
	});
	let tourLocIds = document.querySelectorAll('.tour-loc-id');
	let dataObjs = [];
	tourLocIds.forEach((el, idx) => {
	    let dataObj = {};
	    dataObj.id = parseInt(el.textContent);
	    dataObj.tourOrder = (idx+1);   // 인덱스를 tourOrder로
	    dataObjs.push(dataObj);
	});

	$.ajax({
		url: "/tripnote/trip/updateTour",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(dataObjs),
		success: function(data, status) {console.log("done!");}
	});
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

function deleteNote(btn){
	$card = $(btn).closest('.trip-timeline-event');
	let tourLocId = $card.find(".tour-loc-id").text().trim();
	$card.remove();
	$.ajax({
		url: '/tripnote/trip/deleteTour?id='+tourLocId,
		type: "GET"
	});
	
	
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