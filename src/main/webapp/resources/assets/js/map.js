var container = document.getElementById('rcmd-map'); //지도를 담을 영역의 DOM 레퍼런스
var options = { //지도를 생성할 때 필요한 기본 옵션
	center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
	level: 3 //지도의 레벨(확대, 축소 정도)
};
var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();
var markers = [];
var previousMarker;
function drawMarkerWithXY(mapx, mapy){
	
	if(previousMarker){
		previousMarker.setMap(null);
	}
	
	var coords = new kakao.maps.LatLng(mapy, mapx);
    
    // 2. 결과 좌표로 지도에 마커를 표시합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: coords
    });

    // 3. 생성된 마커를 마커 배열에 추가합니다
    previousMarker = marker;
    //markers.push(marker);

    // 4. 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
    map.setCenter(coords);
}
var transportSelected = '';
var fixMarkers = [];

function setTransport(transportType){
	transportSelected = transportType;
	
	drawRoute(transportSelected);
}

function drawFixMarker(mapx, mapy, title) {
	var coords = new kakao.maps.LatLng(mapy, mapx);

	//1. 빨간색 마커 이미지 생성
	var imageSrc = '/tripnote/resources/assets/img/trip/fixMarker.png', // 마커 이미지 url
		imageSize = new kakao.maps.Size(42, 45), // 마커 이미지의 크기
		imageOption = { offset: new kakao.maps.Point(21, 42) }; // 마커의 좌표와 일치시킬 이미지 안에서의 좌표

	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

	// 2. 결과 좌표로 지도에 마커를 표시합니다 (수정된 부분)
	var fixMarker = new kakao.maps.Marker({
		map: map,
		position: coords,
		image: markerImage //  2. 생성한 마커 이미지를 설정합니다.
	});

	fixMarkers.push(fixMarker);

	// info window creation
	if (title) {
		var infowindow = new kakao.maps.InfoWindow({
			// content에 HTML을 넣을 수 있습니다.
			content: '<div style="padding:5px;font-size:12px;">' + title + '</div>',
			removable: true // 닫기 버튼(X)를 표시합니다.
		});

		kakao.maps.event.addListener(fixMarker, 'mouseover', makeOverListener(map, fixMarker, infowindow));
		kakao.maps.event.addListener(fixMarker, 'mouseout', makeOutListener(infowindow));

		// 마커를 클릭했을 때 인포윈도우를 표시하려면 아래 코드를 사용하세요.
		// kakao.maps.event.addListener(fixMarker, 'click', function() {
		//	 infowindow.open(map, fixMarker);
		// });
	}
	
	map.setCenter(coords);
	
	drawRoute(transportSelected);
			
}

/**
 * [추가된 통합 함수]
 * 현재 화면에 보이는 타임라인을 기준으로 모든 마커와 경로를 완전히 다시 그립니다.
 * 이 함수 하나만 호출하면 지도의 상태가 항상 최신으로 유지됩니다.
 */
function redrawAllMarkersAndRoute() {
    
    // --- 1. 기존 지도 정보 초기화 ---
    // 기존의 모든 고정 마커를 지도에서 제거
    fixMarkers.forEach(fmarker => fmarker.setMap(null));
    fixMarkers = []; // 마커 배열 비우기

    // 기존의 모든 경로(폴리라인)를 지도에서 제거
    routePolylines.forEach(line => line.setMap(null));
    routePolylines = []; // 경로 배열 비우기

    // 기존의 모든 시간 오버레이를 지도에서 제거
    routeTimeOverlays.forEach(overlay => overlay.setMap(null));
    routeTimeOverlays = []; // 시간 오버레이 배열 비우기
    
    // DOM에 추가된 시간 텍스트 제거 (callDirectionsAPI에서 생성됨)
    document.querySelectorAll('.route-time').forEach(el => el.remove());

    // --- 2. 현재 보이는 타임라인에서 장소 정보 다시 읽기 ---
    // 현재 display:none이 아닌, 활성화된 타임라인 Day 요소를 찾음
    const visibleTimeline = document.querySelector(".trip-timelineForDay:not([style*='display: none'])");

    if (visibleTimeline) {
        // 보이는 타임라인 안의 모든 장소 카드(.trip-timeline-event)를 순회
        const eventCards = visibleTimeline.querySelectorAll(".trip-timeline-event");
        
        eventCards.forEach(card => {
            // 각 카드의 data-* 속성에서 좌표와 제목을 추출
            const { mapx, mapy, title } = card.dataset;

            // 유효한 데이터가 있을 경우에만 마커를 그림
            if (mapx && mapy && title) {
                // --- 3. 순서대로 마커 다시 그리기 ---
                // 기존 drawFixMarker 함수를 재사용. 이 함수는 fixMarkers 배열에 마커를 순서대로 추가함.
                drawFixMarker(mapx, mapy, title);
            }
        });
    }

    // --- 4. 경로 다시 그리기 ---
    // drawFixMarker 함수 마지막에 이미 drawRoute(transportSelected)가 호출되고 있으므로,
    // 마지막 마커가 그려진 후 경로는 자동으로 업데이트됩니다.
    // 만약 마커가 하나도 없다면, drawRoute 함수 내부 로직에 따라 아무것도 그리지 않습니다.
    if (fixMarkers.length === 0) {
        drawRoute(transportSelected); // 마커가 0개일 때 경로를 확실히 지우기 위해 호출
    }
}

function makeOverListener(map, marker, infowindow) {
	return function () {
		infowindow.open(map, marker);
	};
}

// 인포윈도우를 닫는 클로저를 만드는 함수입니다
function makeOutListener(infowindow) {
	return function () {
		infowindow.close();
	};
}

var polyline;
function drawRoute(transportType) {
	// 출발지, 도착지, 경유지 설정
	if (fixMarkers.length < 2) {
		return;
	}

	const origin = fixMarkers[0];
	const destination = fixMarkers[fixMarkers.length - 1];
	document.querySelectorAll('.route-time').forEach(function(element) {
	    element.remove();
	});
	let waypoints = [];
	if (fixMarkers.length > 2) {
		// 중간 마커들을 경유지로 추가
		for (let i = 1; i < fixMarkers.length - 1; i++) {
			waypoints.push(fixMarkers[i]);
		}
	}
	switch (transportType) {
		case 'car':
			callDirectionsAPI(origin, destination, waypoints);
			break;
		case 'walk':
			drawSimplePolyline('walk');
			break;
		case 'transit':
			callOdsayTransitAPI(origin, destination);
			break;
		default:
			console.error("지원하지 않는 transportType 입니다:", transportType);
	}
}


////
const colorPalette = [
	"#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#00FFFF",
	"#0000FF", "#8B00FF", "#FF00FF", "#FF1493", "#4B0082", "#000000"
];

// 2. 생성된 폴리라인들을 관리할 배열입니다.
var routePolylines = [];
var routeTimeOverlays = [];

function callDirectionsAPI(origin, destination, waypoints) {
	const REST_API_KEY = '40030219edd182e4ad2c7cf1dd6edb07'; // 발급받은 REST API 키를 여기에 입력하세요.

	let url = 'https://apis-navi.kakaomobility.com/v1/directions';
	let params = new URLSearchParams({
		origin: `${origin.getPosition().getLng()},${origin.getPosition().getLat()}`,
		destination: `${destination.getPosition().getLng()},${destination.getPosition().getLat()}`
	});

	if (waypoints.length > 0) {
		let waypointsStr = waypoints.map(wp => `${wp.getPosition().getLng()},${wp.getPosition().getLat()}`).join('|');
		params.append('waypoints', waypointsStr);
	}
	
	const finalUrl = `${url}?${params.toString()}`;

	$.ajax({
		type: "GET",
		url: finalUrl,
		headers: {
			"Authorization": `KakaoAK ${REST_API_KEY}`
		},
		success: function (data) {
			// ✅ 3. 이전에 그려진 폴리라인들이 있다면 모두 제거합니다.
			routePolylines.forEach(line => line.setMap(null));
			routePolylines = [];
			
			document.querySelectorAll('.route-time').forEach(function(element) {
			    element.remove();
			});

			const sections = data.routes[0].sections;

			// ✅ 4. API 응답에 포함된 각 section에 대해 별도의 폴리라인을 생성합니다.
			sections.forEach((section, index) => {
				const linePath = [];
				section.roads.forEach(road => {
					for (let i = 0; i < road.vertexes.length; i += 2) {
						linePath.push(new kakao.maps.LatLng(road.vertexes[i+1], road.vertexes[i]));
					}
				});

				// ✅ 5. 각 section에 대한 폴리라인을 생성하고 색상을 지정합니다.
				let polyline = new kakao.maps.Polyline({
					path: linePath,
					strokeWeight: 7,
					// colorPalette 배열에서 순서대로 색상을 가져옵니다.
					strokeColor: colorPalette[index % colorPalette.length],
					strokeOpacity: 0.8,
					strokeStyle: 'solid',
					endArrow: true
				});

				polyline.setMap(map);          // 지도에 폴리라인을 표시합니다.
				routePolylines.push(polyline); // 관리 배열에 추가합니다.
				
				//////////////
				const duration = section.duration;

				// 2. 경로의 중간 지점 좌표 찾기
				const midPointIndex = Math.floor(linePath.length / 2);
				const midPoint = linePath[midPointIndex];

				// 3. 커스텀 오버레이에 표시할 내용 생성
				let content = `<div class="route-time" style="color:${colorPalette[index % colorPalette.length]}">${formatDuration(duration)}</div>`;
				
				// 4. 커스텀 오버레이 생성 및 지도에 표시
				let timeOverlay = new kakao.maps.CustomOverlay({
					content: content,
					position: midPoint,
					yAnchor: 1
				});
				
				timeOverlay.setMap(map);
				routeTimeOverlays.push(timeOverlay);
			});
		},
		error: function (request, status, error) {
			console.error("길찾기 API 오류", request, status, error);
			alert("길찾기 서비스에 오류가 발생했습니다.");
		}
	});
}

function formatDuration(seconds) {
    if (seconds < 60) {
        return `${seconds}초`;
    }
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    
    if (minutes < 60) {
        if (remainingSeconds === 0) {
            return `약 ${minutes}분`;
        }
        return `${minutes}분 ${remainingSeconds}초`;
    }
    
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    
    return `${hours}시간 ${remainingMinutes}분`;
}

function drawSimplePolyline(type) {
	let linePath = fixMarkers.map(marker => marker.getPosition());

	let styleOptions = {};
	if (type === 'walk') {
		// 도보: 파란색 점선
		styleOptions = {
			strokeColor: '#0000FF',
			strokeStyle: 'dash'
		};
	} else { // transit
		// 대중교통: 녹색 실선
		styleOptions = {
			strokeColor: '#28A745',
			strokeStyle: 'solid'
		};
	}

	routePolylines.forEach(line => line.setMap(null));
	routePolylines = [];
	document.querySelectorAll('.route-time').forEach(function(element) {
	    element.remove();
	});
	
	let polyline = new kakao.maps.Polyline({
		path: linePath,
		strokeWeight: 6,
		strokeOpacity: 0.8,
		...styleOptions // ES6 spread syntax로 스타일 옵션 적용
	});

	polyline.setMap(map);
	routePolylines.push(polyline); // 관리 배열에 추가
}

function callOdsayTransitAPI(origin, destination) {
    // 이전에 그려진 폴리라인과 시간 오버레이를 먼저 제거
    routePolylines.forEach(line => line.setMap(null));
    routePolylines = [];
    routeTimeOverlays.forEach(overlay => overlay.setMap(null));
    routeTimeOverlays = [];

    const params = {
        start: `${origin.getPosition().getLng()},${origin.getPosition().getLat()}`,
        goal: `${destination.getPosition().getLng()},${destination.getPosition().getLat()}`
    };

    $.ajax({
        type: "GET",
        url: "/tripnote/trip/odsay/searchPath", // ✅ 우리 서버의 첫 번째 주소 호출
        data: params,
        success: function(data) {
            if (data.result) {
                const mapObj = data.result.path[0].info.mapObj;
                // 성공 시, mapObj로 상세 경로를 요청하는 2단계 함수 호출
                callOdsayLoadLaneAPI(mapObj);
            } else {
                console.error("ODsay API 오류(searchPath):", data.error);
                alert("대중교통 길찾기 중 오류가 발생했습니다.");
            }
        },
        error: function(request, status, error) {
            console.error("서버 호출 실패(searchPath)", request.status, request.responseText);
            alert("서버 통신에 실패했습니다.");
        }
    });
}

// 2단계: 우리 서버에 'mapObj'를 보내 상세 경로(그래픽 데이터)를 요청하는 함수
function callOdsayLoadLaneAPI(mapObj) {
    const params = {
        mapObj: mapObj
    };

    $.ajax({
        type: "GET",
        url: "/tripnote/trip/odsay/loadLane", // ✅ 우리 서버의 두 번째 주소 호출
        data: params,
        success: function(data) {
            if (data.result) {
                // 성공 시, 반환된 데이터로 폴리라인을 그리는 함수 호출 (이 함수는 수정할 필요 없음)
                drawDetailedTransitPolyline(data.result);
            } else {
                console.error("ODsay API 오류(loadLane)", data.error);
                alert("경로 그래픽 정보를 불러오는 데 실패했습니다.");
            }
        },
        error: function(request, status, error) {
            console.error("서버 호출 실패(loadLane)", request.status, request.responseText);
            alert("경로 그래픽 정보 호출에 실패했습니다.");
        }
    });
}


function drawDetailedTransitPolyline(resultData) {
    // 경로 전체를 포함하는 지도 범위를 만들기 위한 LatLngBounds 객체 생성
    var bounds = new kakao.maps.LatLngBounds();

    // resultData에 포함된 모든 경로(lane)에 대해 반복
    resultData.lane.forEach(lane => {
        // 각 경로를 구성하는 구간(section)에 대해 반복
        lane.section.forEach(section => {
            // 구간의 경로를 그릴 좌표(LatLng) 배열 생성
            let linePath = [];
            
            // 구간을 구성하는 모든 그래픽 좌표(graphPos)에 대해 반복
            section.graphPos.forEach(pos => {
                // 카카오맵의 LatLng 객체로 변환 (y: 위도, x: 경도)
                const point = new kakao.maps.LatLng(pos.y, pos.x);
                linePath.push(point);
                bounds.extend(point); // LatLngBounds 객체에 현재 좌표 추가
            });

            // 이동 수단(lane.type)에 따른 경로 색상 지정
            let strokeColor = '#000000'; // 기본 검은색
            if (lane.type === 1) { // 지하철
                strokeColor = '#003499'; // 예시: 파란색 계열
            } else if (lane.type === 2) { // 버스
                strokeColor = '#37b42d'; // 예시: 녹색 계열
            }
            
            // 카카오맵에 Polyline 객체 생성 및 지도에 표시
            let polyline = new kakao.maps.Polyline({
                map: map,
                path: linePath,
                strokeWeight: 4,
                strokeColor: strokeColor,
                strokeOpacity: 0.9
            });

            // 생성된 폴리라인을 관리 배열에 추가
            routePolylines.push(polyline);
        });
    });

    // 모든 경로가 한눈에 보이도록 지도의 범위를 재설정
    if (bounds.isEmpty() === false) {
        map.setBounds(bounds);
    }
}


function drawRcmdMarker(el){
    const $el = $(el);
    const address = $el.find('.trip-sl-address').text();
    
    // 주소로 좌표 얻기
    geocoder.addressSearch(address, function(result, status) {
    
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
            
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            
            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            markers.push(marker);

            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(coords);
        }
    });
}

function relayoutKeepCenter(map) {
	const center = map.getCenter();
	map.relayout();
	map.setCenter(center);
}

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();
let isRestaurant = false;
////


////

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    var keyword = document.getElementById('trip-search-input').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch( keyword, placesSearchCB); 
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
//장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
	
	if (status === kakao.maps.services.Status.OK) {
        displayPlaces(data);
        displayPagination(pagination);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
        return;
    }
	
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var placeListEl = document.getElementById('placesList'), 
    menuEl = document.getElementById('trip-search-result'),
    fragment = document.createDocumentFragment(), 
    bounds = new kakao.maps.LatLngBounds();
    
    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(placeListEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();
    
    for ( var i=0; i<places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i), 
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);
        
        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    placeListEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

	
	let courseId = document.querySelector('.courseId').textContent;
    var el = document.createElement('li'),
    itemStr = '<div><span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info">' +
                '   <span class="sl-name">' + places.place_name + '</span>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>'; 
    }
                 									//event, btn, contentId, tourLocName, imgSrc, courseId, tourType, mapx, mapy)
      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
                `</div></div><button class="sl-searched-add" onclick="insertToTimeline(event, this, '2343', '${places.place_name}', '/tripnote/resources/assets/img/trip/sampleTourImg.jpg', '${courseId}', '명소', '${places.x}', '${places.y}' )">+</button>`;  

    el.innerHTML = itemStr;
    el.className = 'item trip-searchedItem trip-loc-card align-items-center';

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage 
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }   
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i; 

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) { 
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}