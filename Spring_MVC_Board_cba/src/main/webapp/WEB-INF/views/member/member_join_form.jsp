<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- css/main.css 파일 불러오기 -->
<link href="css/inc.css" rel="stylesheet" type="text/css">
<link href="css/subpage.css" rel="stylesheet" type="text/css">
<script src="js/jquery-3.6.4.js"></script>
<script type="text/javascript">
	$(function() {
		// 각 규칙 통과 여부를 저장할 변수 선언
		let nameStatus = false;
		let idStatus = false;
		let passwdStatus = false;
		let passwd2Status = false;
		
		
		// 중복확인 버튼 클릭됐을 때의 이벤트 처리		
// 		$("#btnCheckId").on("click", function() {
// 			// 입력받은 아이디 가져와서 변수에 저장 후 출력
// 			let id = $("#id").val();
			
// 			// 만약, 기존 방식대로 아이디 중복 확인을 수행할 경우
// 			// => location.href 속성을 사용하여 서블릿 주소 요청하고 아이디를 함께 전달 후
// 			//    중복 확인 결과값을 가지고 현재 페이지로 다시 이동(포워딩)
// 			location.href = "MemberCheckId.me?id=" + id;
// 		});
		// ----------------------------------------------------------
		// 아이디 입력 시 중복 체크 수행을 위한 AJAX 작업 처리
		// 아이디 입력란에서 포커스가 해제될 때(커서가 빠질 때)의 이벤트 처리
		$("#id").on("blur", function() {
			// 입력된 아이디가 널스트링이면 "checkIdResult" 영역에
			// "아이디는 필수 입력 항목입니다"(빨간색) 출력 후 함수 처리 종료(return)
			if($("#id").val() == "") {
				idStatus = false;
				$("#checkIdResult").html("아이디는 필수 입력 항목입니다").css("color", "red");
				return; // 함수 처리 종료
			} else {
				// 아이디 정규표현식 확인
				let regex = /^[A-Za-z0-9!@#$%]{4,8}$/;
				if(!regex.exec($("#id").val())) {
					$("#checkIdResult").html("영문자, 숫자, 특수문자 조합4 ~ 8글자").css("color", "red");
					idStatus = false;
				} else {	// 규칙일치
					// AJAX 를 활용하여 "MemberCheckDupId.me" 서블릿 요청을 통해
					// 아이디 중복 검사 작업 수행 후 결과값을 리턴받아 처리
					// => 전달할 파라미터 : 아이디
					$.ajax({
						url: "MemberCheckDupId.me", // MemberCheckDupIdAction 매핑
						data: {
							id: $("#id").val()
						},
						success: function(result) { // 성공 시에만 작업 처리
							// 처리 페이지(비즈니스 로직)에서 처리 성공 후 "true" or "false" 값 리턴
							// 리턴받은 결과값에 대해 "true" 또는 "false" 판별
							if(result == "true") {
								// checkIdResult 영역에 "이미 사용중인 아이디"(빨간색) 출력
								$("#checkIdResult").html("이미 사용중인 아이디").css("color", "red");
								idStatus = false;
							} else if(result == "false") {
								// checkIdResult 영역에 "사용 가능한 아이디"(파란색) 출력
								$("#checkIdResult").html("사용 가능한 아이디").css("color", "blue");
								idStatus = true;
							}
						}
					});	// ajax 끝
					
				}
			}
			
		});
		
		// 이름 입력 항목에 한글 외의 다른 데이터 입력 시(change)
		// "한글만 입력 가능합니다" 출력 후 이름 입력란 포커스
		// => 주의! blur 이벤트 처리 시 alert() 함수로 인해 다시 blur 이벤트 동작함
		$("#name").on("change", function() {
			let name = $("#name").val(); // 입력값 가져오기
			
			// 이름 검증에 사용할 정규표현식 작성(한글 2 ~ 5글자)
			let regex = /^[가-힣]{2,10}$/;
			
			// if문 내에서 정규표현식 객체의 exec() 메서드를 사용하여 입력값 검증 수행
			if(!regex.exec(name)) {
				// "한글 2 ~ 5글자 필수" 출력 후 입력창 포커스 요청
				$("#checkNameResult").html("한글 2 ~ 5자 필수!").css("color", "red");
				$("#name").select(); // 입력창 포커스 요청 및 입력항목 블럭지정
				nameStatus = false;
			} else {
				// "사용 가능" 출력
				$("#checkNameResult").html("사용 가능!").css("color", "blue");
				nameStatus = true;
			}
		});
		
		// 비밀번호 입력 시(change 이벤트) 정규표현식 판별
		// => 판별 결과를 id 선택자 checkPasswdResult 영역에 출력
		//    ("사용 가능한 패스워드" or "패스워드 규칙 어긋남")
		$("#passwd").on("change", function() {
			let passwd = $("#passwd").val(); // 입력값 가져오기
			
			let lengthRegex = /^[A-Za-z0-9!@#$%]{8,16}$/;
			let engUpperRegex = /[A-Z]/;
			let engLowerRegex = /[a-z]/;
			let numberRegex = /[0-9]/;
			let specRegex = /[!@#$%]/;
			
			if(!lengthRegex.exec(passwd)) {
				$("#checkPasswdResult").html("영문자, 숫자, 특수문자 8 ~ 16자 필수").css("color", "red");
				$("#name").select();
				passwdStatus = false;
			} else {
				let count = 0;
				if(engUpperRegex.exec(passwd)) { count++ }
				if(engLowerRegex.exec(passwd)) { count++ }
				if(numberRegex.exec(passwd)) { count++ }
				if(specRegex.exec(passwd)) { count++ }
				
				switch (count) {
					case 4:	$("#checkPasswdResult").html("안전").css("color", "green");
						passwdStatus = true;
						break;
					case 3:	$("#checkPasswdResult").html("보통").css("color", "yellow");
						passwdStatus = true;
						break;
					case 2:	$("#checkPasswdResult").html("위험").css("color", "orange");
						passwdStatus = true;
						break;
					case 1:
					case 0:
						$("#checkPasswdResult").html("사용 불가능한 패스워드").css("color", "red");
						passwdStatus = false;
				}
				
			}
		});
		
		$("#passwd2").on("change", function() {
			if($("#passwd2").val() == $("#passwd").val()) {
				$("#checkPasswd2Result").html("비밀번호 일치").css("color", "blue");
				passwd2Status = true;
			} else {
				$("#checkPasswd2Result").html("비밀번호 불일치").css("color", "red");
				passwd2Status = false;
			}
		});
		
		// 폼 태그에 대한 submit 이벤트 처리
		$("form").submit(function() {
			// 이름, 아이디, 패스워드, 패스워드확인란에 대한 모든 규칙이 통과했을 경우에만
			// submit 기능이 동작
			if(!nameStatus) {
				alert("이름을 확인하세요!");
				$("#name").focus();
				return false;
			} else if(!idStatus) {
				alert("아이디를 확인하세요!");
				$("#id").focus();
				return false;
			} else if(!passwdStatus) {
				alert("비밀번호를 확인하세요!");
				$("#passwd").focus();
				return false;
			} else if(!passwd2Status) {
				alert("비밀번호확인을 확인하세요!");
				$("#passwd2").focus();
				return false;
			}
			// 모든 규칙 통과시
			return true;
		});
	});
</script>
</head>
<body>
	<%-- 세션 아이디가 존재할 경우 메인페이지로 돌려보내기 --%>
	<c:if test="${not empty sessionScope.sId }">
		<script type="text/javascript">
			alert("잘못된 접근입니다!");
			location.href = "./";
		</script>
	</c:if>
	<header>
		<%-- inc/top.jsp 페이지 삽입(jsp:include 액션태그 사용 시 / 경로는 webapp 가리킴) --%>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article id="joinForm">
		<h1>회원 가입</h1>
		<form action="MemberJoinPro.me" method="post" name="joinForm">
			<table border="1">
				<tr>
					<th class="td_left">이름</th>
					<td class="td_right">
						<input type="text" name="name" id="name" required="required">
						<span id="checkNameResult"></span>
					</td>
				</tr>
				<tr>
					<th class="td_left">ID</th>
					<td class="td_right">
						<input type="text" name="id" id="id" placeholder="4 ~ 8글자 사이 입력" required="required">
<!-- 						<input type="button" value="중복확인" id="btnCheckId"> -->
						<span id="checkIdResult"></span>
					</td>
				</tr>
				<tr>
					<th class="td_left">비밀번호</th>
					<td class="td_right">
						<input type="text" name="passwd" id="passwd" placeholder="8 ~ 16글자 사이 입력" required="required">
						<span id="checkPasswdResult"></span>
					</td>
				</tr>
				<tr>
					<th class="td_left">비밀번호확인</th>
					<td class="td_right">
						<input type="password" name="passwd2" id="passwd2" onchange="checkConfirmPasswd(this.value)" required="required">
						<span id="checkPasswd2Result"></span>
					</td>
				</tr>
				<tr>
					<th class="td_left">E-Mail</th>
					<td class="td_right">
						<input type="text" name="email1" class="email" required="required">@<input type="text" name="email2" class="email" required="required">
						<select id="emailDomain" onchange="selectDomain(this.value)">
							<option value="">직접입력</option>
							<option value="naver.com">naver.com</option>
							<option value="nate.com">nate.com</option>
							<option value="gmail.com">gmail.com</option>
						</select>
					</td>
				</tr>
				<tr>
					<th class="td_left">성별</th>
					<td class="td_right">
						<input type="radio" name="gender" value="남">남
						<input type="radio" name="gender" value="여">여
					</td>
				</tr>
				<tr>
					<td colspan="2" id="btnArea">
						<input type="submit" value="가입">
						<input type="reset" value="초기화">
						<input type="button" value="돌아가기">
					</td>
				</tr>
			</table>
		</form>
	</article>
</body>
</html>



