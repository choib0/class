<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 전달받은 오류메세지 출력 후 이전 페이지로 돌아가기 --%>
<script type="text/javascript">
	alert("${msg}");
	history.back();
</script>