package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public BoardListVO getAllBoard() {
		// 게시글 목록 화면에 데이터를 전송해주기 위해서 게시글의 건수와 게시글의 목록을 조회해 반환시킨다
		
		// 1. 게시글의 건수를 조회한다.
		int boardCount = this.boardDao.selectBoardAllCount();
		
		// 2. 게시글의 목록을 조회한다.
		List<BoardVO> boardList = this.boardDao.selectAllBoard();
		
		// 3. BoardListVO를 만들어서 게시글의 건수와 목록을 할당한다.
		BoardListVO boardListVO = new BoardListVO();
		boardListVO.setBoardCnt(boardCount);
		boardListVO.setBoardList(boardList);
		
		// 4. BoardListVO 인스턴스를 반환한다.
		return boardListVO;
	}
	
	@Override
	public boolean createNewBoard(WriteBoardVO writeBoardVO) {
		
		// 파일 업로드 처리
		MultipartFile file = writeBoardVO.getFile();
		
		// 1. 클라이언트가 파일을 전송했는지 확인.
		
		
		// 2. 클라이언트가 파일을 전송했다면
		if(file != null && !file.isEmpty()) {
			// 3. 파일을 서버 컴퓨터의 특정 폴더로 저장시킨다.
			File uploadFile = new File("C:\\uploadfiles", file.getOriginalFilename());
			// getOriginalFilename 사용자가 보내준 파일의 이름과 확장자가 들어있음.
			// c 드라이브의 uploadfiles이라는 폴더에 보내줌.
			if(!uploadFile.getParentFile().exists()) {
				uploadFile.getParentFile().mkdirs();
			}
			// uploadfiles가 없으면 생성해주는 코드
			try {
				file.transferTo(uploadFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				throw new RuntimeException("예기치 못한 이유로 업로드에 실패했습니다. 잠시 후 다시 시도해주세요.");
			}
			// 쓰고 있는 디스크의 용량이 꽉찰 때 IOException 발생 혹은 uploadfiles라는 폴더가 없을 때 발생 혹은 허가지되 않은 경로.
			// 그러나 파일을 생성하기 때문에 폴더 없는 경우는 발생하지 않음.
			// 일반적으로 윈도우는 C 드라이브 막아놓음.
			// 보통 이런 식은 checkedExpcetion 보통 할 수 있는게 없기 때문에 예외를 만들어서 던져버림.
			
			
			// 4. 파일의 정보를 데이터베이스에 저장시킨다.
			writeBoardVO.setOriginFileName(file.getOriginalFilename());
		}		
		
		int numberCreation = this.boardDao.inserNewBoard(writeBoardVO);
		return numberCreation > 0;
	}

	@Override
	public BoardVO selectOneBoard(int id, boolean isIncrease) {
		if(isIncrease) {
			int updateCount = boardDao.updateViewCount(id);
			if(updateCount == 0) {
				throw new IllegalArgumentException("잘못된 정보입니다");
			}
		}
		BoardVO boardVO = boardDao.selectOneBoard(id);
		if(boardVO == null) {
			throw new IllegalArgumentException("잘못된 접근입니다");
		}
		return boardVO;
	}
	
	@Override
	public boolean updateOneBoard(ModifyBoardVO modifyBoardVO) {
		int updateCount = boardDao.updateOneBoard(modifyBoardVO);
		return updateCount > 0;
	}
	
	@Override
	public boolean deleteOneBoard(int id) {
		int deleteCount = boardDao.deleteOneBoard(id);
		return deleteCount > 0;
	}
}
