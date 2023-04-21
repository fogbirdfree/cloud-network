package com.dongruan.graduation.networkdiskshareservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.dongruan.graduation.networkdiskcommon.param.AddShareViewCountParam;
import com.dongruan.graduation.networkdiskcommon.param.SaveShareParam;
import com.dongruan.graduation.networkdiskcommon.param.ShareListParam;
import com.dongruan.graduation.networkdiskcommon.param.ShareParam;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskshareservice.service.PageService;
import com.dongruan.graduation.networkdiskshareservice.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("/share")
public class ShareController {
    @Autowired
    private PageService pageService;
    @Autowired
    private ShareService shareService;

    /**
     * 分享文件
     *
     * @author: duyubo
     */
    @ResponseBody
    @PostMapping(value = "share")
    public RestResult<String> share(@RequestBody ShareParam request) {
        RestResult<String> result = new RestResult<>();
        String share = shareService.share(request);
        if (share == null) {
            return null;
        }
        result.success(null);
        result.setRespData(share);
        return result;
    }

    /**
     * 获取分享列表
     *
     * @author: duyubo
     */
    @ResponseBody
    @GetMapping(value = "shareList")
    public RestResult<String> shareList(ShareListParam param) {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = shareService.shareList(param);
        if (map == null) {
            result.success(null);
        } else {
            result.setRespMap(map);
            result.setRespData("200");
        }
        return result;
    }

    /**
     * 取消分享
     *
     * @author: duyubo
     */
    @ResponseBody
    @GetMapping(value = "unShare")
    public RestResult<String> unShare(String uid, String vid) {
        RestResult<String> result = new RestResult<>();
        shareService.unShare(uid, vid);
        result.success(null);
        return result;
    }

    /**
     * 获取分享用户信息
     *
     * @author: duyubo
     */
    @ResponseBody
    @GetMapping(value = "getShareUser")
    public RestResult<String> getShareUser(String shareId) {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = shareService.getShareUser(shareId);
        if (map == null) {
            return null;
        }
        result.setRespData("success");
        result.setRespMap(map);
        return result;
    }

    /**
     * 保存分享
     *
     * @author: duyubo
     */
    @ResponseBody
    @PostMapping(value = "saveShare")
    public RestResult<String> saveShare(@RequestBody SaveShareParam param) {
        RestResult<String> result = new RestResult<>();
        try {
            shareService.saveShare(param);
        } catch (Exception e) {
            result.error(e.getMessage());
            return result;
        }
        result.success(null);
        return result;
    }

    /**
     * 查询分享是否带密码
     *
     * @author: duyubo
     */
    @ResponseBody
    @GetMapping(value = "checkLock")
    public RestResult<String> checkLock(@RequestParam("shareId") String shareId) {
        RestResult<String> result = new RestResult<>();
        int i = shareService.checkLock(shareId);
        if (i == 1) {
            result.success(null);
            result.setRespData("Lock");
        } else if (i == 0) {
            result.success(null);
            result.setRespData("Lock");
        } else {
            result.error();
        }
        return result;
    }

    /**
     * 验证分享密码
     *
     * @author: duyubo
     */
    @ResponseBody
    @GetMapping(value = "verifyLock")
    public RestResult<String> verifyLock(@RequestParam("lockPassword") String lockPassword, @RequestParam("shareId") String shareId) {
        RestResult<String> result = new RestResult<>();
        boolean res = shareService.verifyLock(lockPassword, shareId);
        if (res) {
            result.success(null);
            result.setRespData("200");
        } else {
            result.error();
            result.setRespData("500");
        }
        return result;
    }

    /**
     * 获取分享虚拟地址信息
     *
     * @author: duyubo
     */
    @ResponseBody
    @GetMapping(value = "getVInfo")
    public RestResult<String> getUid(@RequestParam("shareId") String shareId, @RequestParam("lockPassword") String lockPassword) {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = shareService.getVInfo(shareId, lockPassword);
        if (map == null) {
            result.error();
        } else {
            result.setRespMap(map);
        }
        return result;
    }

    /**
     * 增加分享访问量
     *
     * @author: duyubo
     */
    @ResponseBody
    @PostMapping(value = "addShareView")
    public void addShareView(@RequestParam("shareId") String shareId) {
        shareService.addShareViewCount(shareId);
    }

    /**
     * 增加分享下载量
     *
     * @author: duyubo
     */
    @PostMapping(value = "addShareDownload")
    public void addShareDownload(@RequestParam("shareId") String shareId) {
        shareService.addShareDownloadCount(shareId);
    }

    @RequestMapping("/s/{shareId}")
    public String s(Model model, @PathVariable String shareId, HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        String uid = null;
        for (Cookie cookie : cookies) {
            if ("user".equals(cookie.getName())) {
                String value = URLDecoder.decode(cookie.getValue(), "utf-8");
                uid = JSONObject.parseObject(value).getString("id");
            }
        }
        return pageService.sHandle(model, shareId, uid);
    }
}
