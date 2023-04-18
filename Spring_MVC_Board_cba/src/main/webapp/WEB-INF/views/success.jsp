<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 전달받은 상태메세지 출력 후 지정된 페이지로 이동 - 자바스크립트 --%>
<script type="text/javascript">
	alert("${msg}");
	location.href = "${target}";
</script>