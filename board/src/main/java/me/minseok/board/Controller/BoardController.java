package me.minseok.board.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import me.minseok.board.Service.BoardService;
import me.minseok.board.constant.Method;
import me.minseok.board.domain.Board;
import me.minseok.board.paging.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import me.minseok.board.util.UiUtils;

@Controller
public class BoardController extends UiUtils{

	@Autowired
	private BoardService boardService;

	@GetMapping("/")
	public String home(Model model){
		LocalDateTime time = LocalDateTime.now();
		model.addAttribute("time", time);
		return "board/home";
	}

	@GetMapping("/home")
	public String index(){
		return "board/home";
	}


	@GetMapping(value = "/board/write.do")
	public String openBoardWrite(@ModelAttribute("params") Board params, @RequestParam(value = "id", required = false) Long id, Model model) {
		if (id == null) {
			model.addAttribute("board", new Board());
		} else {
			Board board = boardService.getBoardDetail(id);
			if (board == null || board.getDeleteYn() == true) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
			}
			model.addAttribute("board", board);

		}

		return "board/write";
	}

	@PostMapping(value = "/board/register.do")
	public String registerBoard(@ModelAttribute("params") final Board params, Model model) {
		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isRegistered = boardService.registerBoard(params);
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
	}


	@GetMapping(value = "/board/view.do")
	public String openBoardDetail(@ModelAttribute("params") Board params, @RequestParam(value = "id", required = false) Long id, Model model) {
		if (id == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		Board board = boardService.getBoardDetail(id);
		if (board == null || board.getDeleteYn() == true) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);

		return "board/view";
	}

	@GetMapping(value = "/board/list.do")
	public String openBoardList(@ModelAttribute("params") Board params, Model model) {
		List<Board> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);

		return "board/list";
	}

	@PostMapping(value = "/board/delete.do")
	public String deleteBoard(@ModelAttribute("params") Board params, @RequestParam(value = "id", required = false) Long id, Model model) {
		if (id == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isDeleted = boardService.deleteBoard(id);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
	}
}