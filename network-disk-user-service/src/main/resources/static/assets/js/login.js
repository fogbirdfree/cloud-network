var LOGIN = {
		checkInput : function() {
			if (!$("#loginForm input[name='username']").val()) {
				return false;
			}
			if (!$("#loginForm input[name='password']").val()) {
				return false;
			}
			return true;
		},
		doLogin : function() {
			 var encrypt = new JSEncrypt();
             encrypt.setPublicKey($("#publicKey").val());
             var password = $("#loginForm input[name='password']").val();
             var passwordEnc = encrypt.encrypt(password);
			var user = {
				username : $("#loginForm input[name='username']").val(),
				password : passwordEnc,
				RSAKey : $("#RSAKey").val(),
				appId : $("#appId").val(),
				redirectUri : $("#redirectUri").val()
			}
			$.ajax({
				// url : "http://47.110.83.145:8095/api/user/login",
				url : "http://47.110.83.145:8080/login",
				type : "post",
				data : user,
				xhrFields : {
					withCredentials : true
				},
				crossDomain : true,
				dataType : "json",
				success : function(data) {
					if(data.respCode==1){
					window.location.reload();
					}else{
						$("#TANGRAM__PSP_4__error").css('display','block'); 
					}
				}
			});
		},
		login : function() {
			if (this.checkInput()) {
				this.doLogin();
			}
		}

	};
	$(function() {
		$("#TANGRAM__PSP_4__submit").click(function() {
			LOGIN.login();
		});
		 $("#TANGRAM__PSP_4__password").focus(function(){
			 if(!$(this).hasClass("input-focus")){
			 $(this).addClass("input-focus");
				$.get("http://47.110.83.145:8080/getPublicKey", function(data){
				   $("#publicKey").val(data.respMap.publicKey);
				   $("#RSAKey").val(data.respMap.RSAKey);
				  });
			 }
		}); 
		 $("#TANGRAM__PSP_4__password").blur(function(){
			 $(this).removeClass("input-focus");
		});
	});