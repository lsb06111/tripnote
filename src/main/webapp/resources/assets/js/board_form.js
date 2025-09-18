// 게시물 저장
function savePost() {
    const $btn = $('#board-save-btn');
    $btn.prop("disabled", true);

    const formData = new FormData();

    // 1. 게시물 기본 정보
    formData.append("title", $('#write_title_input').val());
    formData.append("intro", $('#post_intro').val());
    formData.append("userId", 1);
    formData.append("courseId", $('.board-form').attr('data-id'));
    const thumbnail = $('#thumbnailImgInput')[0].files[0];
    if(thumbnail){
    	formData.append("thumbnail", thumbnail);
    }
    // 2. 각 탭(날짜) 별 컨텐츠와 이미지
    let content_idx = 0;
    $('.swiper-slide').each(function(index, elem) {
        const $el = $(elem);
        const texterea = $el.find('.loc-textera').val();
        const file = $el.find('.postImg-input')[0].files[0];

        // text나 이미지 입력이 있으면 formData에 저장
        if (texterea !== ''){
            formData.append(`contents[${content_idx}].content`, texterea);
            formData.append(`contents[${content_idx}].title`, $el.attr('data-tourloc-name'));
            formData.append(`contents[${content_idx}].boardId`, $el.attr('data-tourloc-id'));
        }
        if(file){
        	formData.append(`contents[${content_idx}].file`, file);
        }
        content_idx++;
    });
    console.log("ajax 뭐가 전달되나 : " + [...formData.entries()]);
    // 3. Ajax 전송
    $.ajax({
        url: "/tripnote/board/form/save",
        type: "POST",
        data: formData,
        processData: false, // 반드시 false
        contentType: false, // 반드시 false
        success: function(response) {
            alert("저장 성공!");
            window.location.href = "/tripnote/board";
        },
        error: function(xhr, status, err) {
            console.error(xhr.responseText);
            alert("저장 실패: " + status);
        },
        complete: function() {
            $btn.prop("disabled", false);
        }
    });
}
// 게시물 임시 저장
function saveDraft() {
	$btn = $('#board-draft-btn');
	$btn.prop("disabled", true);
	let postData = {
			boardDTO: {
				title: $('#write_title_input').val(),
				intro: $('#post_intro').val(),
				userId: 1,
				courseId: $('.board-form').attr('data-id')
			},
			contents: []
	};
	
	$('.tab-per-day').each(function(index, elem) {
		let $el = $(elem);
		let data = {
				content: $el.find('.loc-textera').val(),
				tourLocId: $('.board-form').attr('data-id'),
				img: $el.find('.postImg').attr('src')
		};
		postData.contents.push(data);
	});
	$.ajax({
		url: "/tripnote/board/form/draft",
		type: "POST",
		contentType: "application/json", 
		data: JSON.stringify(postData), // JSON 형식의 문자열을 만들어 반환
		dataType: "text", // 응답 데이터 타입
		success: function(response) {
			alert("임시 저장 완료");
		},
		error: function(xhr, status, err) {
			console.error(xhr.responseText);
			alert("임시 저장 실패: " + status);
		},
        complete: function() {
            // 요청이 끝나면 다시 버튼 활성화
            $btn.prop("disabled", false);
        }
	});
}

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


//recommendation button
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
