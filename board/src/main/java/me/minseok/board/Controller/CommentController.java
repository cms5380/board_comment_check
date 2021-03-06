package me.minseok.board.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.minseok.board.Service.CommentService;
import me.minseok.board.Service.KafkaMessageSender;
import me.minseok.board.adapter.GsonLocalDateTimeAdapter;
import me.minseok.board.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private KafkaMessageSender kafkaMessageSender;

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
            @RequestBody Comment params) {
        JsonObject jsonObj = new JsonObject();



        try {
            if (id != null) {
                params.setId(id);
            }

            Map<String, Long> map = commentService.registerComment(params);
            if(map.get("isRegistered") == 1){
                jsonObj.addProperty("result", true);
            }
            else{
                jsonObj.addProperty("result", false);
            }

            params.setId(map.get("id"));
            kafkaMessageSender.sendMessage(params);
        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "?????????????????? ?????? ????????? ????????? ?????????????????????.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "???????????? ????????? ?????????????????????.");
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
            jsonObj.addProperty("message", "?????????????????? ?????? ????????? ????????? ?????????????????????.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "???????????? ????????? ?????????????????????.");
        }
        return jsonObj;
    }
}
