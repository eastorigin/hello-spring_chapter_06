<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정하기</title>
<link rel="stylesheet" type="text/css" href="/css/common.css">
</head>
<body>
	<h1>게시글 수정</h1>
	<form method="post" method="/board/modify/${boardVO.id}">
		<div class="grid">
			<label for="subject">제목</label>
			<input id="subject" type="text" name="subject" value="${boardVO.subject}"/>
			<br />
			<label for="email">이메일</label>
			<input id="email" type="email" name="email" value="${boardVO.email}"/>
			<br />
			
			<label for="content">내용</label>
			<textarea id="content" name="content">${boardVO.content}</textarea>
			<br />
			
			<div class="btn-group">
				<div class="right-align">
					<input type="submit" value="수정" />
				</div>
			</div>
		</div>
	</form>
</body>
</html>