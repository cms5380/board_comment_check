package me.minseok.board.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.minseok.board.Service.CommentService;
import me.minseok.board.adapter.GsonLocalDateTimeAdapter;
import me.minseok.board.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;



    @GetMapping("/comment/{boardId}")
    public JsonObject getCommentList(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute("params") Comment params){

        JsonObject jsonObj = new JsonObject();
        List<Comment> comments = commentService.getCommentList(params);


        if (CollectionUtils.isEmpty(comments) == false) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
            JsonArray jsonArr = gson.toJsonTree(comments).getAsJsonArray();
            jsonObj.add("commentList", jsonArr);
        }

        return jsonObj;
    }


    @RequestMapping(value = {"/comment", "/comment/{id}"},  method = { RequestMethod.POST, RequestMethod.PATCH })
    public JsonObject registerComment(
            @PathVariable(value = "id", required = false) Long id,
            @RequestBody final Comment params) {
        JsonObject jsonObj = new JsonObject();


        try {
            if (id != null) {
                params.setId(id);
            }
            boolean isClean = commentService.getPredict(params);
            System.out.println(isClean);
            params.setCleanCommentYn(isClean);

            boolean isRegistered = commentService.registerComment(params);
            jsonObj.addProperty("result", isRegistered);

        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
        }

        return jsonObj;
    }

    @DeleteMapping("/comment/{id}")
    public JsonObject deleteComment(@PathVariable("id") final Long id) {
        JsonObject jsonObj = new JsonObject();
        try{
            boolean isDel = commentService.deleteComment(id);
            jsonObj.addProperty("result", isDel);

        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
        }
        return jsonObj;
    }
}
