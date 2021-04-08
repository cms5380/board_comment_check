package me.minseok.board.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import me.minseok.board.Mapper.CommentMapper;
import me.minseok.board.domain.Board;
import me.minseok.board.domain.Comment;
import org.python.antlr.ast.Str;
import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_SERVER_HOST  = "http://5f2bb8bd1284.ngrok.io";
    private static final String PREDICT_PATH = "/predict";

    public boolean getPredict(Comment comment) {

        String s = comment.getContent();

        String url = API_SERVER_HOST+PREDICT_PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = new JSONObject();
        Map<String, String> answer;

        try {
            jsonObject.put("comment", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(),headers);
        answer = restTemplate.postForObject(url, entity, Map.class);


        return "negative".equals(answer.get("result"))  ? false : true;
    }

    @Override
    public boolean registerComment(Comment comment){
        int ret = 0;

        if(comment.getId() == null){
            ret = commentMapper.insertComment(comment);
        }else{
            ret = commentMapper.updateComment(comment);
        }

        return ret == 1 ? true : false;
    }

    @Override
    public boolean deleteComment(Long id) {
        int ret = 0;
        Comment comment = commentMapper.selectCommentDetail(id);
        if(comment != null && comment.getDeleteYn() == false){
            ret = commentMapper.deleteComment(id);
        }

        return ret == 1 ? true : false;
    }

    @Override
    public List<Comment> getCommentList(Comment comment) {
        List<Comment> list = Collections.emptyList();

        int commentTotalCount = commentMapper.selectCommentTotalCount(comment);

        if (commentTotalCount > 0) {
            list = commentMapper.selectCommentList(comment);
        }


        return list;
    }
}
