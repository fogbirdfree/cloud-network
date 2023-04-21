$(function () {
    $("#TANGRAM__PSP_4__password").focus(function () {
        if (!$(this).hasClass("input-focus")) {
            $(this).addClass("input-focus");
            $.get("http://47.110.83.145:8080/getPublicKey", function (data) {
                $("#publicKey").val(data.respMap.publicKey);
                $("#RSAKey").val(data.respMap.RSAKey);
            });
        }
    });
    $("#TANGRAM__PSP_4__password").blur(function () {
        $(this).removeClass("input-focus");
    });
});