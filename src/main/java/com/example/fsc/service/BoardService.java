package com.example.fsc.service;

import com.example.fsc.domain.Board;
import com.example.fsc.dto.BoardDto;
import com.example.fsc.dto.UpdateBoardDto;
import com.example.fsc.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.RequestEntity.put;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public ResponseEntity<List<BoardDto>> findAll() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDTOList = new ArrayList<>();
        for (Board board : boardList) {
            BoardDto boardDTO = BoardDto.builder()
                    .boardId(board.getBoardId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .author(board.getAuthor())
                    .emailId(board.getEmailId())
                    .createdAt(board.getCreatedAt())
                    .build();
            boardDTOList.add(boardDTO);
        }
        return ResponseEntity.status(200).body(boardDTOList);
    }

    public ResponseEntity<BoardDto> findBoardById(Long id){
        Optional<Board> byId = boardRepository.findById(id);
            Board board = byId.get();
            BoardDto boardDTO = BoardDto.builder()
                    .boardId(board.getBoardId())
                    .emailId(board.getEmailId())
                    .title(board.getTitle())
                    .author(board.getAuthor())
                    .content(board.getContent())
                    .createdAt(board.getCreatedAt())
                    .build();
            return ResponseEntity.status(200).body(boardDTO);
        }

    public ResponseEntity<List<BoardDto>> findBoardListByEmail(String email) {
        List<Board> searchedBoardEntityList = boardRepository.findBoardsByAuthorContainingOrderByCreatedAtDesc(email);
        List<BoardDto> searchedBoardDtoList = new ArrayList<>();

        for(Board board : searchedBoardEntityList) {
            BoardDto boardDTO = BoardDto.builder()
                    .boardId(board.getBoardId())
                    .emailId(board.getEmailId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .author(board.getAuthor())
                    .createdAt(board.getCreatedAt())
                    .build();
            searchedBoardDtoList.add(boardDTO);
        }
        return ResponseEntity.status(200).body(searchedBoardDtoList);
    }

        public ResponseEntity<Map<String ,String>> deleteBoard(Long boardId) {

        Map<String,String> deleteMap= new HashMap<>();
        boardRepository.deleteById(boardId);
        Optional<Board> byId = boardRepository.findById(boardId);
        if (!byId.isPresent()){
            deleteMap.put("message" ," 게시물이 성공적으로 삭제되었습니다.");
        }else{
            deleteMap.put("message", "게시물이 삭제 되지 않았습니다.");
        }
        return ResponseEntity.status(200).body(deleteMap);
        }


    public ResponseEntity<Map<String, String>> updateBoard(UpdateBoardDto updateBoardDto, Long boardId) {

        Map<String, String> map = new HashMap<>();

        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if(boardOptional.isPresent()){
            Board board = boardOptional.get();
            // dto -> entity 변환
            board.setTitle(updateBoardDto.getTitle());
            board.setContent(updateBoardDto.getContent());

            boardRepository.save(board);
            map.put("message", "게시물이 성공적으로 수정되었습니다.");
        } else {
            map.put("message", "게시물 수정에 실패하였습니다.");
        }
        return ResponseEntity.status(200).body(map);
    }


    public ResponseEntity<Map<String, String>> saveBoard(BoardDto boardDTO) {

        // dto -> entity 변환
        Board board = Board.builder().
                author(boardDTO.getAuthor())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .emailId(boardDTO.getEmailId())
                .createdAt(LocalDateTime.now())
                .build();

        Map<String, String> map = new HashMap<>();
        Long boardId = boardRepository.save(board).getBoardId();
        Optional<Board> findId = boardRepository.findById(boardId);
        if(findId.isPresent()){
            map.put("message", "게시물이 성공적으로 작성되었습니다.");
        } else {
            map.put("message", "게시물 작성에 실패하였습니다.");
        }
        return ResponseEntity.status(200).body(map);
    }




}



