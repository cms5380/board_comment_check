package me.minseok.board.util;

import me.minseok.board.constant.Method;
import me.minseok.board.paging.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class UiUtils {

    public Map<String, Object> getPagingParams(Criteria criteria) {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("currentPageNo", criteria.getCurrentPageNo());
        params.put("recordsPerPage", criteria.getRecordsPerPage());
        params.put("pageSize", criteria.getPageSize());
        params.put("searchType", criteria.getSearchType());
        params.put("searchKeyword", criteria.getSearchKeyword());

        return params;
    }

    public String showMessageWithRedirect(@RequestParam(value = "message", required = false) String message,
                                          @RequestParam(value = "redirectUri", required = false) String redirectUri,
                                          @RequestParam(value = "method", required = false) Method method,
                                          @RequestParam(value = "params", required = false) Map<String, Object> params, Model model) {

        model.addAttribute("message", message);
        model.addAttribute("redirectUri", redirectUri);
        model.addAttribute("method", method);
        model.addAttribute("params", params);

        return "utils/message-redirect";
    }

}
