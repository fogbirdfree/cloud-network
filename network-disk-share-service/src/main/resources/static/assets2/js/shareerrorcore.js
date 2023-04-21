var historyMap = new Map();
var finishMap = new Map();
var folderMap = new Map();

function flashContent() {
    $(".vdAfKMb").empty();
    historyMap.clear();
    finishMap.clear();
    history.pushState(null, "", location.href);
    if (location.href.indexOf("search?key=") != -1) {
        loadContent2(decodeURI(location.href.split("search?key=")[1]))
    } else {
        loadContent()
    }
}

function exit() {
    $("#my-confirm").modal({
        closeViaDimmer: 0, onConfirm: function (b) {
            $.ajax({
                url: "http://47.110.83.145:8095/api/user/logout?token=" + $.cookie("token"),
                type: "GET",
                xhrFields: {withCredentials: true},
                crossDomain: true,
                success: function (c) {
                    if (c.respCode == 1) {
                        location.href = "http://47.110.83.145:8097/"
                    } else {
                        alert(c.respMsg)
                    }
                }
            })
        }, onCancel: function () {
        }
    })
}

function changePwd() {
    var a = $("#changePwd").modal({
        closeViaDimmer: 0, onConfirm: function (b) {
            var o = $("input[name='oldPassword']").val();
            var f = $("input[name='newPassword']").val();
            var d = $("input[name='repeatPassword']").val();
            var n = $("#uid").val();
            if (o != "" && f != "" && f == d) {
                var e = new JSEncrypt();
                e.setPublicKey($("#publicKey").val());
                var m = e.encrypt(o);
                var c = e.encrypt(f);
                $.ajax({
                    url: "http://47.110.83.145:8080/regCheckPwd",
                    type: "post",
                    data: {"password": c, "RSAKey": $("#RSAKey").val()},
                    xhrFields: {withCredentials: true},
                    crossDomain: true,
                    dataType: "json",
                    success: function (g) {
                        if (g.respCode == 0) {
                            $(".am-modal-prompt-input").val("");
                            alert(g.respMsg)
                        } else {
                            $.ajax({
                                url: "http://47.110.83.145:8086/user/modifyPassword",
                                type: "post",
                                data: JSON.stringify({
                                    "username": n,
                                    "password": m,
                                    "newPassword": c,
                                    "rsaKey": $("#RSAKey").val()
                                }),
                                xhrFields: {withCredentials: true},
                                crossDomain: true,
                                contentType:'application/json;charset=UTF-8',
                                dataType: "json",
                                success: function (h) {
                                    if (h.respCode == 1) {
                                        alert("修改成功");
                                    } else {
                                        alert(h.respMsg);
                                    }
                                },
                                error: function () {
                                    alert("服务器错误")
                                }
                            })
                        }
                    }
                })
            }
        }, onCancel: function () {
            $(".am-modal-prompt-input").val("");
            $("#changePwd").modal("close")
        }
    });
    a.find("[data-am-modal-cancel]").off("click.close.modal.amui");
    a.find("[data-am-modal-confirm]").off("click.close.modal.amui")
}

function getFileUrl(b) {
    var a;
    if (navigator.userAgent.indexOf("MSIE") >= 1) {
        a = document.getElementById(b).value
    } else {
        if (navigator.userAgent.indexOf("Firefox") > 0) {
            a = window.URL.createObjectURL(document.getElementById(b).files.item(0))
        } else {
            if (navigator.userAgent.indexOf("Chrome") > 0) {
                a = window.URL.createObjectURL(document.getElementById(b).files.item(0))
            }
        }
    }
    return a
}

function preImg(d, b) {
    $("#photo").css("display", "inline-block");
    var a = getFileUrl(d);
    var c = document.getElementById(b);
    c.src = a
}

function uploadPic() {
    $("#photo").css("display", "none");
    var a = $("#uploadPic").modal({
        closeViaDimmer: 0, onConfirm: function (c) {
            var d = $("#pic").get(0).files[0];
            var b = d.size;
            var g = 1048576;
            var e = d.name.substring(d.name.lastIndexOf(".") + 1, d.name.length).toUpperCase();
            if (e != "PNG" && e != "GIF" && e != "JPG" && e != "JPEG" && e != "BMP") {
                alert("文件类型错误,请上传图片类型");
                return false
            } else {
                if (parseInt(b) >= parseInt(g)) {
                    alert("上传的文件不能超过1MB");
                    return false
                } else {
                    var f = new FormData();
                    f.append("uid", $('#uid').val());
                    f.append("file", d);
                    $.ajax({
                        url: "http://47.110.83.145:8086/user/uploadPic",
                        type: "post",
                        contentType: false,
                        data: f,
                        cache: false,
                        processData: false,
                        xhrFields: {withCredentials: true},
                        crossDomain: true,
                        dataType: "json",
                        success: function (h) {
                            if (h.dataCode == 200) {
                                alert("上传成功");
                                loadImg();
                                $("#uploadPic").modal("close")
                            } else {
                                alert("服务器错误")
                            }
                        },
                        error: function () {
                            alert("服务器错误")
                        }
                    })
                }
            }
        }, onCancel: function () {
            $(".am-modal-prompt-input").val("");
            $("#uploadPic").modal("close")
        }
    });
    a.find("[data-am-modal-cancel]").off("click.close.modal.amui");
    a.find("[data-am-modal-confirm]").off("click.close.modal.amui")
}

function loadImg() {
    $.ajax({
        url: "http://47.110.83.145:8086/user/loadImg?uid=" + $('#uid').val(),
        type: "GET",
        xhrFields: {withCredentials: true},
        crossDomain: true,
        success: function (b) {
            $(".user-photo").css("background-image", "url(http://47.110.83.145/" + b.respData + ")")
        },
        error: function () {
        }
    })
}

function loadFolder(b, c) {
    var a = folderMap.get(b);
    if (a == null) {
        $.ajax({
            url: "http://47.110.83.145:8086/share/core/listFolder",
            type: "get",
            data: {"uid": $("#uid").val(), "parentPath": b},
            xhrFields: {withCredentials: true},
            crossDomain: true,
            dataType: "json",
            success: function (e) {
                var d = e.respMap;
                folderMap.set(b, d);
                if (b == "/") {
                    showFolder(d, $(".treeview-root"))
                } else {
                    showFolder(d, c)
                }
            }
        })
    } else {
        if (b == "/") {
            showFolder(a, $(".treeview-root"))
        } else {
            showFolder(a, c)
        }
    }
}

function showFolder(content, obj) {
    for (var key in content) {
        var contentVal = eval($.parseJSON(content[key]));
        if (contentVal.dir_empty == 0) {
            obj.siblings("ul").append("<li><div class='treeview-node treenode-empty' data-padding-left='" + (parseInt(obj.attr("data-padding-left")) + 15) + "' style='padding-left:" + (parseInt(obj.attr("data-padding-left")) + 15) + "px'><span class='treeview-node-handler'><em class='b-in-blk plus icon-operate '></em><dfn class='b-in-blk treeview-ic treeview-dir'></dfn><span class='treeview-txt' data-file-path='" + contentVal.path + "'>" + contentVal.path.substring(contentVal.path.lastIndexOf("/") + 1, contentVal.path.length) + "</span></span></div><ul class='treeview  treeview-content treeview-collapse' data-padding-left='30px'></ul></li>")
        } else {
            obj.siblings("ul").append("<li><div class='treeview-node' data-padding-left='" + (parseInt(obj.attr("data-padding-left")) + 15) + "' style='padding-left:" + (parseInt(obj.attr("data-padding-left")) + 15) + "px'><span class='treeview-node-handler'><em class='b-in-blk plus icon-operate '></em><dfn class='b-in-blk treeview-ic treeview-dir'></dfn><span class='treeview-txt' data-file-path='" + contentVal.path + "'>" + contentVal.path.substring(contentVal.path.lastIndexOf("/") + 1, contentVal.path.length) + "</span></span></div><ul class='treeview  treeview-content treeview-collapse' data-padding-left='30px'></ul></li>")
        }
    }
    $(".treeview-node").unbind();
    $(".treeview-node").live("mouseenter", function () {
        $(this).addClass("treeview-node-hover")
    });
    $(".treeview-node").live("mouseleave", function () {
        $(this).removeClass("treeview-node-hover")
    });
    $(".treeview-node").bind("click", function (e) {
        if (!$(this).hasClass("treeview-node-on") && !$(this).hasClass("treenode-empty")) {
            $(".treeview-node").removeClass("treeview-node-on");
            $(this).addClass("treeview-node-on");
            if (!$(this).siblings("ul").hasClass("treeview-collapse")) {
            } else {
                $(this).addClass("_minus");
                $(this).children("span").children("em").addClass("minus")
            }
            if ($(this).siblings("ul").text() == "" && !$(this).hasClass("treenode-empty")) {
                loadFolder($(this).children("span").children("span").attr("data-file-path"), $(this))
            } else {
                $(this).children("span").children("em").addClass("minus");
                $(this).siblings("ul").removeClass("treeview-collapse")
            }
            $(this).siblings("ul").removeClass("treeview-collapse")
        } else {
            if (!$(this).hasClass("treenode-empty")) {
                if ($(this).siblings("ul").hasClass("treeview-collapse")) {
                    $(this).addClass("_minus");
                    $(this).siblings("ul").removeClass("treeview-collapse");
                    $(this).children("span").children("em").addClass("minus")
                } else {
                    $(this).removeClass("_minus");
                    $(this).siblings("ul").addClass("treeview-collapse");
                    $(this).children("span").children("em").removeClass("minus")
                }
            } else {
                $(".treeview-node").removeClass("treeview-node-on");
                $(this).addClass("treeview-node-on")
            }
        }
    })
}

function save(a, b) {
    $.ajax({
        url: "http://47.110.83.145:8086/share/checkLock",
        type: "get",
        data: {"shareId": a},
        xhrFields: {withCredentials: true},
        crossDomain: true,
        dataType: "json",
        success: function (c) {
            if (c.respCode != 0) {
                if (c.respData == "Lock") {
                    $("#Lock1").modal({
                        closeViaDimmer: 0, onConfirm: function (f) {
                            var d = $("input[name='LockPassword1']").val();
                            $.ajax({
                                url: "http://47.110.83.145:8086/share/verifyLock",
                                type: "get",
                                data: {"shareId": a, "lockPassword": d},
                                xhrFields: {withCredentials: true},
                                crossDomain: true,
                                dataType: "json",
                                success: function (e) {
                                    if (e.respData == "200") {
                                        saveShare(a, b, d)
                                    } else {
                                        alert("验证失败");
                                        $("input[name='LockPassword1']").val("")
                                    }
                                },
                                error: function () {
                                    $("input[name='LockPassword1']").val("");
                                    alert("服务器错误，操作失败")
                                }
                            })
                        }, onCancel: function (d) {
                        }
                    })
                } else {
                    saveShare(a, b, "")
                }
            } else {
                $("input[name='LockPassword1']").val("");
                alert("参数不正确")
            }
        },
        error: function () {
            $("input[name='LockPassword1']").val("");
            alert("服务器错误，操作失败")
        }
    })
}

function saveShare(b, c, a) {
    $.ajax({
        url: "http://47.110.83.145:8086/share/saveShare",
        type: "post",
        data: JSON.stringify({"uid": $("#uid").val(), "shareId": b, "dest": c, "lockPassword": a}),
        xhrFields: {withCredentials: true},
        contentType:'application/json;charset=UTF-8',
        crossDomain: true,
        dataType: "json",
        success: function (d) {
            if (d.respCode == 1) {
                alert("保存成功");
                $("input[name='LockPassword1']").val("");
                $("#fileTreeDialog").css("display", "none");
                $(".module-canvas").css("display", "none")
            } else {
                $("input[name='LockPassword1']").val("");
                alert(d.respMsg)
            }
        },
        error: function () {
            $("input[name='LockPassword1']").val("");
            alert("服务器错误，操作失败")
        }
    })
}

function downloadShare(a) {
    $.ajax({
        url: "http://47.110.83.145:8086/share/checkLock",
        type: "get",
        data: { "shareId": a},
        xhrFields: {withCredentials: true},
        crossDomain: true,
        dataType: "json",
        success: function (b) {
            if (b.respCode != 0) {
                if (b.respData == "Lock") {
                    $("#Lock").modal({
                        closeViaDimmer: 0, onConfirm: function (d) {
                            var c = $("input[name='LockPassword']").val();
                            $.ajax({
                                url: "http://47.110.83.145:8086/share/verifyLock",
                                type: "get",
                                data: {"shareId": a, "lockPassword": c},
                                xhrFields: {withCredentials: true},
                                crossDomain: true,
                                dataType: "json",
                                success: function (e) {
                                    if (e.respData == "200") {
                                        download(a, c)
                                    } else {
                                        alert("验证失败");
                                        $("input[name='LockPassword']").val("")
                                    }
                                },
                                error: function () {
                                    $("input[name='LockPassword']").val("");
                                    alert("服务器错误，操作失败")
                                }
                            })
                        }, onCancel: function (c) {
                        }
                    })
                } else {
                    download(a, "")
                }
            } else {
                $("input[name='LockPassword']").val("");
                alert("参数不正确")
            }
        },
        error: function () {
            $("input[name='LockPassword']").val("");
            alert("服务器错误，操作失败")
        }
    })
}

function download(d, c) {
    var e = $("<form id='abc'>");
    e.attr("style", "display:none");
    e.attr("method", "get");
    e.attr("action", "http://47.110.83.145:8086/file/downloadShare");
    var b = $("<input>");
    b.attr("type", "hidden");
    b.attr("name", "shareId");
    b.attr("value", d);
    var f = $("<input>");
    f.attr("type", "hidden");
    f.attr("name", "lockPassword");
    f.attr("value", c);
    $("body").append(e);
    e.append(b);
    e.append(f);
    e.submit();
    e.empty()
};