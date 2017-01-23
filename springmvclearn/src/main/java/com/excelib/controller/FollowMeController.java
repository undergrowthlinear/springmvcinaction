package com.excelib.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 测试
 * @date 2017-01-23-22:19
 */
@Controller
@SessionAttributes("articleId")
public class FollowMeController {
    private final Log logger = LogFactory.getLog(FollowMeController.class);
    private final String[] sensitiveWords = new String[]{"k1", "s2"};

    @ModelAttribute("comment")
    public String replaceSensitiveWords(String comment) throws IOException {
        if(comment != null){
            logger.info("原始comment："+comment);
            for(String sw : sensitiveWords)
                comment = comment.replaceAll(sw, "");
            logger.info("去敏感词后comment：" + comment);
        }
        return comment;
    }

    @RequestMapping(value={"/articles/{articleId}/comment"})
    public String doComment(@PathVariable String articleId, RedirectAttributes attributes, Model model) throws Exception {
        attributes.addFlashAttribute("comment", model.asMap().get("comment"));
        model.addAttribute("articleId", articleId);
        // 此处将评论内容保存到数据库
        return "redirect:/showArticle";
    }

    @RequestMapping(value={"/showArticle"}, method= {RequestMethod.GET})
    public String showArticle(Model model, SessionStatus sessionStatus) throws Exception {
        String articleId = (String)model.asMap().get("articleId");
        model.addAttribute("articleTitle", articleId+"号文章标题");
        model.addAttribute("article", articleId+"号文章内容");
        sessionStatus.setComplete();
        return "article";
    }
}
